package com.codash.cgm.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codash.cgm.exception.CodegenException;

public class StringUtil {
	Log log=LogFactory.getLog(StringUtil.class);  

	public static boolean isEmpty(String str)
	{
		if(str==null) return true;
		if(str.trim().equals("")) return true;
		return false;
	}
	
	public static boolean isNotEmpty(String str)
	{
		
		return !isEmpty(str);
	}
	
	/**
	 * 鏇挎崲鏍囬噺銆�br>
	 * <pre>
	 * 浣跨敤鏂规硶濡備笅锛�
	 * String template="com/codash/{path}/model/{class}";
	 * Map<String,String> map=new HashMap<String,String>();
	 * map.put("path","platform");
	 * map.put("class","Role");
	 * String tmp=replaceVariable(template,map);
	 * </pre>
	 * @param template
	 * @param map
	 * @return
	 * @throws CodegenException 
	 */
	public static String replaceVariable(String template,Map<String,String> map) throws CodegenException
	{
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		while (regexMatcher.find()) {
			String key=regexMatcher.group(1);
			String toReplace=regexMatcher.group(0);
			String value=(String)map.get(key);
			if(value!=null)
				template=template.replace(toReplace, value);
			else
				throw new CodegenException("娌℃湁鎵惧埌["+ key +"]瀵瑰簲鐨勫彉閲忓�锛岃妫�煡琛ㄥ彉閲忛厤缃�");
		}  
		
		return template;
	}
	
	/**
	 * 鏇挎崲琛ㄥ彉閲�
	 * @param template
	 * @param tableName
	 * @return
	 */
	public static String replaceVariable(String template,String tableName) 
	{
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		if (regexMatcher.find()) {
			String toReplace=regexMatcher.group(0);
			template=template.replace(toReplace, tableName);
		}
		return template;
	}
	
	/**
	 * 鍘绘帀鍓嶉潰鐨勫瓧绗�
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimPrefix(String toTrim,String trimStr)
	{
		while(toTrim.startsWith(trimStr))
		{
			toTrim=toTrim.substring(trimStr.length());
		}
		return toTrim;
	}
	
	/**
	 * 鍒犻櫎鍚庨潰鐨勫瓧绗�
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimSufffix(String toTrim,String trimStr)
	{
		while(toTrim.endsWith(trimStr))
		{
			toTrim=toTrim.substring(0,toTrim.length()-trimStr.length());
		}
		return toTrim;
	}
	
	/**
	 * 鍒犻櫎鎸囧畾鐨勫瓧绗�
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trim(String toTrim,String trimStr)
	{
		return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
	}
	
	public static String combilePath(String baseDir,String dir)
	{
		baseDir=trimSufffix(baseDir, File.separator) ;
		dir=trimPrefix(dir,File.separator);
		
		return baseDir + File.separator + dir;
	}
	
	/**
	 * 浣跨敤瀛楃鎹㈡浛鎹㈠唴瀹�
	 * @param content 鍐呭
	 * @param startTag 寮�鏍囩
	 * @param endTag 缁撴潫鏍囩
	 * @param repalceWith 浣跨敤鏇挎崲
	 * @return
	 */
	public static String replace(String content,String startTag,String endTag,String repalceWith)
	{
		String tmp=content.toLowerCase();
		String tmpStartTag=startTag.toLowerCase();
		String tmpEndTag=endTag.toLowerCase();
		
		
		StringBuffer sb=new StringBuffer();
		int beginIndex=tmp.indexOf(tmpStartTag);
		int endIndex=tmp.indexOf(tmpEndTag);
		while(beginIndex!=-1 && endIndex!=-1 && beginIndex<endIndex)
		{
			String pre=content.substring(0,tmp.indexOf(tmpStartTag)+tmpStartTag.length());
			String suffix=content.substring(tmp.indexOf(tmpEndTag));
			
			tmp=suffix.toLowerCase();
			content=suffix.substring(endTag.length());
			
			beginIndex=tmp.indexOf(tmpStartTag);
			endIndex=tmp.indexOf(tmpEndTag);
			String replaced=pre + "\r\n" +  repalceWith  +"\r\n" + endTag;
			sb.append(replaced);
		}
		sb.append(content);
		return sb.toString();
	}
	
	/**
	 * 鍒ゆ柇鎸囧畾鐨勫唴瀹规槸鍚﹀瓨鍦�
	 * @param content 鍐呭
	 * @param begin 寮�鏍囩
	 * @param end 缁撴潫鏍囩
	 * @return
	 */
	public static boolean isExist(String content,String begin,String end)
	{
		String tmp=content.toLowerCase();
		begin=begin.toLowerCase();
		end=end.toLowerCase();
		int beginIndex=tmp.indexOf(begin);
		int endIndex=tmp.indexOf(end);
		if(beginIndex!=-1  && endIndex!=-1 && beginIndex<endIndex)
			return true;
		return false;
	}
	
	
	public static void main(String[] args) throws CodegenException
	{
		String template="com/codash/{path}/model/{class}";
		 Map<String,String> map=new HashMap<String,String>();
		 map.put("path","platform");
		 //map.put("class","Role");
		 String tmp=replaceVariable(template,map);
 	}
	

}
