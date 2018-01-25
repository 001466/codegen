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
	 * ���ݸ�����schemaУ��xml,�������Ƿ�У��ɹ�.
	 * 
	 * @param xml
	 * @param schema
	 * @return
	 */
	public String validXmlBySchema(String xmlPath, InputStream schema) {
		String result = null;
		try {
			// ����Ĭ�ϵ�XML��������
			XMLErrorHandler errorHandler = new XMLErrorHandler();
			// ��ȡ���� SAX �Ľ�������ʵ��
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// �������ڽ���ʱ��֤ XML ���ݡ�
			factory.setValidating(true);
			// ָ���ɴ˴������ɵĽ��������ṩ�� XML ���ƿռ��֧�֡�
			factory.setNamespaceAware(true);
			// ʹ�õ�ǰ���õĹ����������� SAXParser ��һ����ʵ����
			SAXParser parser = factory.newSAXParser();
			// ����һ����ȡ����
			SAXReader xmlReader = new SAXReader();
			// ��ȡҪУ��xml�ĵ�ʵ��
		
			org.dom4j.Document xmlDocument = (org.dom4j.Document) xmlReader.read(new File(xmlPath));
			// ���� XMLReader �Ļ���ʵ���е��ض����ԡ����Ĺ��ܺ������б������
			// [url]http://sax.sourceforge.net/?selected=get-set[/url] ���ҵ���
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schema);
			// ����һ��SAXValidatorУ�鹤�ߣ�������У�鹤�ߵ�����
			SAXValidator validator = new SAXValidator(parser.getXMLReader());
			// ����У�鹤�ߵĴ�������������������ʱ�����ԴӴ����������еõ�������Ϣ��
			validator.setErrorHandler(errorHandler);
			// У��
			validator.validate(xmlDocument);

			// ���������Ϣ��Ϊ�գ�˵��У��ʧ�ܣ���ӡ������Ϣ
			if (errorHandler.getErrors().hasContent()) {
				result = "XML�ļ�ͨ��XSD�ļ�У��ʧ��:" + errorHandler.getErrors();
			}
		} catch (Exception ex) {
			result = "XML�ļ�ͨ��XSD�ļ�У��ʧ��:" + ex.getMessage();
		}
		return result;
	}
	
	/**
	 * ��ȡXML����
	 * 
	 * @param xmlFile
	 * @return
	 * @throws CodegenException
	 */
	@SuppressWarnings("rawtypes")
	public ConfigModel loadXml(String xmlFile) throws CodegenException {
		// xsd�ļ�
		InputStream xsd = this.getClass().getClassLoader().getResourceAsStream("codegen.xsd");

		// ��֤XML
		String msg = validXmlBySchema(xmlFile, xsd);
		try {
			xsd.close();
		} catch (IOException e) {
			throw new CodegenException(e);
		}
		if (msg != null) {
			throw new CodegenException(msg);
		}
		

		// ��֤ͨ���� ��ʼ��ȡXML
		ConfigModel cm = new ConfigModel();

		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new File(xmlFile));
		} catch (DocumentException e) {
			throw new CodegenException("��ȡXML����!", e);
		}
		Element root = document.getRootElement();
	
		// ��XML�ж�ȡcharset
		Element charsetEl= root.element("charset");
		cm.setCharset(charsetEl.getTextTrim());

		// ��XML�ж�ȡdatabase
		Element databaseEl= root.element("database");
		String dbHelperClass = databaseEl.attributeValue("dbHelperClass");
		String url = databaseEl.attributeValue("url");
		String username = databaseEl.attributeValue("username");
		String password = databaseEl.attributeValue("password");
		cm.setDatabase(cm.new Database(dbHelperClass, url, username, password));
		
		// ��XML�ж�ȡvariables
		Element variablesEl= root.element("variables");
		for (Iterator j = variablesEl.elementIterator("variable"); j.hasNext();) {
			Element variableEl = (Element) j.next();
			String name = variableEl.attributeValue("name");
			String value = variableEl.attributeValue("value");
			cm.getVariables().put(name, value);
		}

		// ��XML�ж�ȡtemplates
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
		
		//��xml��ȡ�ļ�ģ��
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
				throw new CodegenException("û���ҵ�" +refTemplate +"��Ӧ��ģ��!");
			
		
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


		// ��XML�ж�ȡtable
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
			//����ӱ�
			for (Iterator j = tableEl.elementIterator("subtable"); j.hasNext();) {
				Element fileEl = (Element) j.next();
				String tablename = fileEl.attributeValue("tablename");
				String foreignKey = fileEl.attributeValue("foreignKey");
				table.getSubtable().put(tablename, foreignKey);
			}
		
		}

		// ��XML�ж�ȡgenAll
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
					throw new CodegenException("�Ҳ���ģ�壺 " + refTemplate + "!");
				}
				if ("SingleFile".equals(genMode) && filename == null) {
					throw new CodegenException("��SingleFileģʽʱ������Ҫ��filename!");
				} else if ("MultiFile".equals(genMode) && extName == null) {
					throw new CodegenException("��MultiFileģʽʱ������Ҫ��extName!");
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
	 * �����������ɡ�
	 * @param configModel
	 * @throws CodegenException
	 */
	public void genByConfig(ConfigModel configModel) throws CodegenException {
		try {
			// database helper
			IDbHelper dbHelper = getDbHelper(configModel);

			// freemark����
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(configModel.getTemplates().getBasepath()));

			log.info("*********��ʼ����**********");

			// XML�е�table����
			List<ConfigModel.Table> tables = configModel.getTables();
			for (ConfigModel.Table table : tables) {
				String tbName=table.getTableName();
				// �����ݿ��ж�ȡ��tableModel
				TableModel tableModel = dbHelper.getByTable(table.getTableName());
				//���ñ�
				tableModel.setVariables(table.getVariable());
				
				//��Ӵӱ�
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
				//���������ļ�
				for(ConfigModel.Files.File file :fileList)
				{
					//�ļ���
					String filename=file.getFilename();
					String startTag=file.getStartTag();
					String endTag=file.getEndTag();
					startTag=StringUtil.replaceVariable(startTag, tbName);
					endTag=StringUtil.replaceVariable(endTag, tbName);
					//�����ļ�Ŀ¼
					String dir=file.getDir();
					filename=StringUtil.replaceVariable(filename, variables);
					dir=StringUtil.replaceVariable(dir, variables);
					dir=StringUtil.combilePath(baseDir, dir);
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("model", tableModel);
					map.put("vars", configModel.getVariables());
					map.put("date", new Date());
					//�ļ���ӡ�
					if(file.getAppend())
					{
						appendFile(dir,filename, file.getTemplate(), configModel.getCharset(), cfg, map,file.getInsertTag(),startTag, endTag);
					}
					else
					{
						genFile(dir,filename, file.getTemplate(), configModel.getCharset(), cfg, map);
					}
					
					log.info(filename + " ���ɳɹ�!");
				}
			}

			// XML�е�genAll����
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
				
				log.info("----------------���ɶ��ʼ------------------");
				

				for (ConfigModel.GenAll.File file : genAll.getFile()) {
					if ("MultiFile".equals(file.getGenMode())) {
						for (String tableName : tableNames) {
							// �����ݿ��ж�ȡ��tableModel
							TableModel tableModel = dbHelper.getByTable(tableName);

							Map<String, Object> map = new HashMap<String, Object>();
						
							map.put("model", tableModel);
							map.put("date", new Date());

							genFile(file.getDir(), tableName + "." + file.getExtName(), file.getTemplate(),
									configModel.getCharset(), cfg, map);
							log.info(tableName + "." + file.getExtName() + " ���ɳɹ�!");
						}

					} else if ("SingleFile".equals(file.getGenMode())) {
						List<TableModel> models = new ArrayList<TableModel>();
						for (String tableName : tableNames) {
							// �����ݿ��ж�ȡ��tableModel
							TableModel tableModel = dbHelper.getByTable(tableName);
							models.add(tableModel);
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("models", models);
						map.put("date", new Date());
						genFile(file.getDir(), file.getFilename(), file.getTemplate(), configModel.getCharset(), cfg,map);
						log.info(file.getFilename() + " ���ɳɹ�!");
					}
				}
				log.info("----------------���ɶ�����------------------");
			}

			log.info("*********�����ļ����ɳɹ�!**********");
		} catch (IOException e) {
			throw new CodegenException(e);
		} catch (TemplateException e) {
			throw new CodegenException("freemarkerִ�г���!", e);
		}

	}

	/**
	 * �����ļ�
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
		//���ȱ����ļ�
		//FileHelper.backupFile(path);
		File newFile = new File(fileDir, fileName);
		if (!newFile.exists()) {
			if (!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}
			newFile.createNewFile();
		}
		Writer writer = new OutputStreamWriter(new FileOutputStream(newFile), charset);
		// ģ���ļ�
		Template template = cfg.getTemplate(templatePath, charset);
		// ����
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
		//�Ѿ��滻���������˲�����
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
	 * ��ȡDbHelper��
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
			throw new CodegenException("ָ����dbHelperClass��" + dbHelperClass
					+ "�޷�ʵ���������ܸ�����һ�������ࡢ�ӿڡ������ࡢ�������ͻ� void, ���߸���û��Ĭ�Ϲ��췽��!", e);
		} catch (IllegalAccessException e) {
			throw new CodegenException("ָ����dbHelperClass�� " + dbHelperClass + "û��Ĭ�Ϲ��췽���򲻿ɷ���!", e);
		} catch (ClassNotFoundException e) {
			throw new CodegenException("�Ҳ���ָ����dbHelperClass:" + dbHelperClass + "!", e);
		}
		dbHelper.setUrl(configModel.getDatabase().getUrl(), configModel.getDatabase().getUsername(), configModel
				.getDatabase().getPassword());
		return dbHelper;
	}

	public void execute() throws Exception {
		
		
		
		log.info("execute:" +xmlPath);
		
		if (xmlPath == null) {
			throw new CodegenException("δָ��XML·��!");
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
