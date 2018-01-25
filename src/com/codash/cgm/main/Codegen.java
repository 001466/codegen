package com.codash.cgm.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;

import com.codash.cgm.db.IDbHelper;
import com.codash.cgm.exception.CodegenException;
import com.codash.cgm.model.ConfigModel;
import com.codash.cgm.model.TableModel;
import com.codash.cgm.model.ConfigModel.Files;
import com.codash.cgm.model.ConfigModel.GenAll;
import com.codash.cgm.model.ConfigModel.Table;
import com.codash.cgm.model.ConfigModel.Templates;
import com.codash.cgm.util.FileHelper;
import com.codash.cgm.util.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Codegen {
	Log log=LogFactory.getLog(Codegen.class);  
	private static String xmlPath;

	public static void setXmlPath(String path) {
		xmlPath = path;
	}

	/**
	 * 根据给定的schema校验xml,并返回是否校验成功.
	 * 
	 * @param xml
	 * @param schema
	 * @return
	 */
	public String validXmlBySchema(String xmlPath, InputStream schema) {
		String result = null;
		try {
			// 创建默认的XML错误处理器
			XMLErrorHandler errorHandler = new XMLErrorHandler();
			// 获取基于 SAX 的解析器的实例
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// 解析器在解析时验证 XML 内容。
			factory.setValidating(true);
			// 指定由此代码生成的解析器将提供对 XML 名称空间的支持。
			factory.setNamespaceAware(true);
			// 使用当前配置的工厂参数创建 SAXParser 的一个新实例。
			SAXParser parser = factory.newSAXParser();
			// 创建一个读取工具
			SAXReader xmlReader = new SAXReader();
			// 获取要校验xml文档实例
		
			org.dom4j.Document xmlDocument = (org.dom4j.Document) xmlReader.read(new File(xmlPath));
			// 设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在
			// [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schema);
			// 创建一个SAXValidator校验工具，并设置校验工具的属性
			SAXValidator validator = new SAXValidator(parser.getXMLReader());
			// 设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
			validator.setErrorHandler(errorHandler);
			// 校验
			validator.validate(xmlDocument);

			// 如果错误信息不为空，说明校验失败，打印错误信息
			if (errorHandler.getErrors().hasContent()) {
				result = "XML文件通过XSD文件校验失败:" + errorHandler.getErrors();
			}
		} catch (Exception ex) {
			result = "XML文件通过XSD文件校验失败:" + ex.getMessage();
		}
		return result;
	}
	
	/**
	 * 读取XML配置
	 * 
	 * @param xmlFile
	 * @return
	 * @throws CodegenException
	 */
	@SuppressWarnings("rawtypes")
	public ConfigModel loadXml(String xmlFile) throws CodegenException {
		// xsd文件
		InputStream xsd = this.getClass().getClassLoader().getResourceAsStream("codegen.xsd");

		// 验证XML
		String msg = validXmlBySchema(xmlFile, xsd);
		try {
			xsd.close();
		} catch (IOException e) {
			throw new CodegenException(e);
		}
		if (msg != null) {
			throw new CodegenException(msg);
		}
		

		// 验证通过， 开始读取XML
		ConfigModel cm = new ConfigModel();

		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new File(xmlFile));
		} catch (DocumentException e) {
			throw new CodegenException("读取XML出错!", e);
		}
		Element root = document.getRootElement();
	
		// 从XML中读取charset
		Element charsetEl= root.element("charset");
		cm.setCharset(charsetEl.getTextTrim());

		// 从XML中读取database
		Element databaseEl= root.element("database");
		String dbHelperClass = databaseEl.attributeValue("dbHelperClass");
		String url = databaseEl.attributeValue("url");
		String username = databaseEl.attributeValue("username");
		String password = databaseEl.attributeValue("password");
		cm.setDatabase(cm.new Database(dbHelperClass, url, username, password));
		
		// 从XML中读取variables
		Element variablesEl= root.element("variables");
		for (Iterator j = variablesEl.elementIterator("variable"); j.hasNext();) {
			Element variableEl = (Element) j.next();
			String name = variableEl.attributeValue("name");
			String value = variableEl.attributeValue("value");
			cm.getVariables().put(name, value);
		}

		// 从XML中读取templates
		Element templatesEl= root.element("templates");
		String basepath = templatesEl.attributeValue("basepath");
		Templates templates = cm.new Templates(basepath);
		cm.setTemplates(templates);
		for (Iterator j = templatesEl.elementIterator("template"); j.hasNext();) {
			Element templateEl = (Element) j.next();
			String id = templateEl.attributeValue("id");
			String path = templateEl.attributeValue("path");
			templates.getTemplate().put(id, path);
		}
		
		//从xml读取文件模版
		Element filesEl= root.element("files");
		Files files=cm.new Files();
		cm.setFiles(files);
		
		String baseDir = filesEl.attributeValue("baseDir");
		files.setBaseDir(baseDir);
		for (Iterator j = filesEl.elementIterator("file"); j.hasNext();) {
			
			Element fileEl = (Element) j.next();
			String refTemplate = fileEl.attributeValue("refTemplate");
			String filename = fileEl.attributeValue("filename");
			String dir = fileEl.attributeValue("dir");
			
			String template= templates.getTemplate().get(refTemplate);
			if(template==null)
				throw new CodegenException("没有找到" +refTemplate +"对应的模版!");
			
		
			String append=fileEl.attributeValue("append");
			if(append!=null && append.toLowerCase().equals("true")){
				String insertTag=fileEl.attributeValue("insertTag");
				String startTag=fileEl.attributeValue("startTag");
				String endTag=fileEl.attributeValue("endTag");
				if(insertTag==null)
					insertTag="<!--insertbefore-->";
				if(StringUtil.isEmpty(startTag)) startTag="";
				if(StringUtil.isEmpty(endTag)) endTag="";
				
				files.addFile(template, filename, dir,true,insertTag,startTag,endTag);
			}
			else{
				files.addFile(template, filename, dir,false,"","","");
			}
			
		}


		// 从XML中读取table
		for (Iterator i = root.elementIterator("table"); i.hasNext();) {
			Element tableEl = (Element) i.next();
			String tableName = tableEl.attributeValue("tableName");
		
			Table table = cm.new Table(tableName);
			cm.getTables().add(table);
			for (Iterator j = tableEl.elementIterator("variable"); j.hasNext();) {
				Element fileEl = (Element) j.next();
				String name = fileEl.attributeValue("name");
				String value = fileEl.attributeValue("value");
				table.getVariable().put(name, value);
			}
			//添加子表
			for (Iterator j = tableEl.elementIterator("subtable"); j.hasNext();) {
				Element fileEl = (Element) j.next();
				String tablename = fileEl.attributeValue("tablename");
				String foreignKey = fileEl.attributeValue("foreignKey");
				table.getSubtable().put(tablename, foreignKey);
			}
		
		}

		// 从XML中读取genAll
		for (Iterator i = root.elementIterator("genAll"); i.hasNext();) {
			Element tableEl = (Element) i.next();
			String tableNames = tableEl.attributeValue("tableNames");
			GenAll genAll = cm.new GenAll(tableNames);
			cm.setGenAll(genAll);
			for (Iterator j = tableEl.elementIterator("file"); j.hasNext();) {
				Element fileEl = (Element) j.next();
				String refTemplate = fileEl.attributeValue("refTemplate");
				String filename = fileEl.attributeValue("filename");
				String extName = fileEl.attributeValue("extName");
				String dir = fileEl.attributeValue("dir");
				String genMode = fileEl.attributeValue("genMode");
				String template = cm.getTemplates().getTemplate().get(refTemplate);
				if (template == null) {
					throw new CodegenException("找不到模板： " + refTemplate + "!");
				}
				if ("SingleFile".equals(genMode) && filename == null) {
					throw new CodegenException("当SingleFile模式时，必须要填filename!");
				} else if ("MultiFile".equals(genMode) && extName == null) {
					throw new CodegenException("当MultiFile模式时，必须要填extName!");
				}
				GenAll.File file = genAll.new File(template, filename, extName, dir, genMode);
				genAll.getFile().add(file);
				for (Iterator k = fileEl.elementIterator("variable"); k.hasNext();) {
					Element variableEl = (Element) k.next();
					String name = variableEl.attributeValue("name");
					String value = variableEl.attributeValue("value");
					file.getVariable().put(name, value);
				}
			}
		}
		return cm;
	}

	/**
	 * 根据配置生成。
	 * @param configModel
	 * @throws CodegenException
	 */
	public void genByConfig(ConfigModel configModel) throws CodegenException {
		try {
			// database helper
			IDbHelper dbHelper = getDbHelper(configModel);

			// freemark配置
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(configModel.getTemplates().getBasepath()));

			log.info("*********开始生成**********");

			// XML中的table配置
			List<ConfigModel.Table> tables = configModel.getTables();
			for (ConfigModel.Table table : tables) {
				String tbName=table.getTableName();
				// 从数据库中读取的tableModel
				TableModel tableModel = dbHelper.getByTable(table.getTableName());
				//设置表
				tableModel.setVariables(table.getVariable());
				
				//添加从表
				Map<String,String> subMap= table.getSubtable();
				for(Map.Entry<String, String> ent :subMap.entrySet())
				{
					String tableName=ent.getKey();
					String foreignKey=ent.getValue();
					TableModel subTable = dbHelper.getByTable(tableName);
					subTable.setForeighKey(foreignKey);
					
					tableModel.getSubTableList().add(subTable);
				}
				Files files= configModel.getFiles();
				String baseDir= files.getBaseDir();
				
				List<ConfigModel.Files.File> fileList= files.getFiles();
				Map<String, String> variables= table.getVariable();
				//遍历生成文件
				for(ConfigModel.Files.File file :fileList)
				{
					//文件名
					String filename=file.getFilename();
					String startTag=file.getStartTag();
					String endTag=file.getEndTag();
					startTag=StringUtil.replaceVariable(startTag, tbName);
					endTag=StringUtil.replaceVariable(endTag, tbName);
					//生成文件目录
					String dir=file.getDir();
					filename=StringUtil.replaceVariable(filename, variables);
					dir=StringUtil.replaceVariable(dir, variables);
					dir=StringUtil.combilePath(baseDir, dir);
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("model", tableModel);
					map.put("vars", configModel.getVariables());
					map.put("date", new Date());
					//文件添加。
					if(file.getAppend())
					{
						appendFile(dir,filename, file.getTemplate(), configModel.getCharset(), cfg, map,file.getInsertTag(),startTag, endTag);
					}
					else
					{
						genFile(dir,filename, file.getTemplate(), configModel.getCharset(), cfg, map);
					}
					
					log.info(filename + " 生成成功!");
				}
			}

			// XML中的genAll配置
			GenAll genAll = configModel.getGenAll();
			if (genAll != null) {
				List<String> tableNames = null;
				if (genAll.getTableNames() == null) {
					tableNames = dbHelper.getAllTable();
				} else {
					tableNames = new ArrayList<String>();
					for (String name : genAll.getTableNames().replaceAll(" ", "").split(",")) {
						if (name.length() > 0) {
							tableNames.add(name);
						}
					}
				}
				int size=genAll.getFile().size();
				
				if(size==0) return;
				
				log.info("----------------生成多表开始------------------");
				

				for (ConfigModel.GenAll.File file : genAll.getFile()) {
					if ("MultiFile".equals(file.getGenMode())) {
						for (String tableName : tableNames) {
							// 从数据库中读取的tableModel
							TableModel tableModel = dbHelper.getByTable(tableName);

							Map<String, Object> map = new HashMap<String, Object>();
						
							map.put("model", tableModel);
							map.put("date", new Date());

							genFile(file.getDir(), tableName + "." + file.getExtName(), file.getTemplate(),
									configModel.getCharset(), cfg, map);
							log.info(tableName + "." + file.getExtName() + " 生成成功!");
						}

					} else if ("SingleFile".equals(file.getGenMode())) {
						List<TableModel> models = new ArrayList<TableModel>();
						for (String tableName : tableNames) {
							// 从数据库中读取的tableModel
							TableModel tableModel = dbHelper.getByTable(tableName);
							models.add(tableModel);
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("models", models);
						map.put("date", new Date());
						genFile(file.getDir(), file.getFilename(), file.getTemplate(), configModel.getCharset(), cfg,map);
						log.info(file.getFilename() + " 生成成功!");
					}
				}
				log.info("----------------生成多表结束------------------");
			}

			log.info("*********所有文件生成成功!**********");
		} catch (IOException e) {
			throw new CodegenException(e);
		} catch (TemplateException e) {
			throw new CodegenException("freemarker执行出错!", e);
		}

	}

	/**
	 * 生成文件
	 * 
	 * @param fileDir
	 * @param fileName
	 * @param templatePath
	 * @param charset
	 * @param cfg
	 * @param data
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void genFile(String fileDir, String fileName, String templatePath, String charset, Configuration cfg,
			Map data) throws IOException, TemplateException {
		String path=StringUtil.combilePath(fileDir, fileName);
		//事先备份文件
		//FileHelper.backupFile(path);
		File newFile = new File(fileDir, fileName);
		if (!newFile.exists()) {
			if (!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}
			newFile.createNewFile();
		}
		Writer writer = new OutputStreamWriter(new FileOutputStream(newFile), charset);
		// 模板文件
		Template template = cfg.getTemplate(templatePath, charset);
		// 生成
		template.process(data, writer);
	}
	
	
	private void appendFile(String fileDir, String fileName, String templatePath, String charset, Configuration cfg,
			Map data,String insertTag,String startTag,String endTag) throws IOException, TemplateException
	{
		
		String path=StringUtil.combilePath(fileDir, fileName);
		File newFile = new File(fileDir, fileName);

		StringWriter out=new StringWriter();
		Template template = cfg.getTemplate(templatePath, charset);
		template.process(data, out);
		String str=out.toString();

		
		boolean exists=false;
		String content="";
		if(newFile.exists()){
			content=FileHelper.readFile(path,charset);
			if(StringUtil.isNotEmpty(startTag) && StringUtil.isNotEmpty(endTag)){
				if(StringUtil.isExist(content, startTag, endTag)){
					content=StringUtil.replace(content, startTag, endTag, str);
					exists=true;
				}
			}
		}
		//已经替换过，跳过此操作。
		if(!exists){
			if(StringUtil.isNotEmpty(startTag) && StringUtil.isNotEmpty(endTag)){
				str=startTag.trim() +"\r\n" + str  +"\r\n" + endTag.trim();
			}
			if(content.indexOf(insertTag)!=-1){
				String[] aryContent=FileHelper.getBySplit(content, insertTag);
				content=aryContent[0] + str + "\r\n" + insertTag + aryContent[1];
			}
			else{
				content=content + "\r\n" + str;
			}
		}
		FileHelper.writeFile(newFile, charset, content);
	
	}
	

	/**
	 * 获取DbHelper。
	 * 
	 * @param configModel
	 * @return
	 * @throws CodegenException
	 */
	private IDbHelper getDbHelper(ConfigModel configModel) throws CodegenException {
		IDbHelper dbHelper = null;
		String dbHelperClass = configModel.getDatabase().getDbHelperClass();

		try {
			dbHelper = (IDbHelper) Class.forName(dbHelperClass).newInstance();
		} catch (InstantiationException e) {
			throw new CodegenException("指定的dbHelperClass：" + dbHelperClass
					+ "无法实例化，可能该类是一个抽象类、接口、数组类、基本类型或 void, 或者该类没有默认构造方法!", e);
		} catch (IllegalAccessException e) {
			throw new CodegenException("指定的dbHelperClass： " + dbHelperClass + "没有默认构造方法或不可访问!", e);
		} catch (ClassNotFoundException e) {
			throw new CodegenException("找不到指定的dbHelperClass:" + dbHelperClass + "!", e);
		}
		dbHelper.setUrl(configModel.getDatabase().getUrl(), configModel.getDatabase().getUsername(), configModel
				.getDatabase().getPassword());
		return dbHelper;
	}

	public void execute() throws Exception {
		
		
		
		log.info("execute:" +xmlPath);
		
		if (xmlPath == null) {
			throw new CodegenException("未指定XML路径!");
		}

		ConfigModel cm = loadXml(xmlPath);
		genByConfig(cm);
	}

	public static void main(String[] args) throws Exception {
  		Codegen cg = new Codegen();
  	    String path = cg.getClass().getResource("/") .getPath() + "log4j.properties";
		System.err.println("log4j configfile path=" + path);
		PropertyConfigurator.configureAndWatch(path);

		cg.setXmlPath("D:\\fabranches\\dbi-v4\\metadata\\codegen\\codegenconfigext.xml");
		cg.execute();
 
	}

}
