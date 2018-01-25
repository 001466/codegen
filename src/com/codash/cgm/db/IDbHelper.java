package com.codash.cgm.db;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codash.cgm.exception.CodegenException;
import com.codash.cgm.model.TableModel;

/**
 * ��ȡ��ݿ����б�ͱ��TableMode�ӿ��ࡣ
 * @author codash
 *
 */
public interface IDbHelper {
	Log log=LogFactory.getLog(IDbHelper.class);  

	/**
	 * ����URL,username,password
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	void setUrl(String url, String username, String password);

	/**
	 * ��ݱ���ȡ��TableModel
	 * 
	 * @param tableName
	 * @return
	 */
	TableModel getByTable(String tableName) throws CodegenException;

	/**
	 * ȡ�����еı���
	 * 
	 * @return
	 */
	List<String> getAllTable() throws CodegenException;
}
