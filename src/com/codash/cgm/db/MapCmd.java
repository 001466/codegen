package com.codash.cgm.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  ResultSet ����ӳ��ӿڣ��û�����ͨ��������������ʵ�֡�
 *  
 * @author codash
 *
 * @param <T>
 */
public interface MapCmd<T> {
	Log log=LogFactory.getLog(MapCmd.class);  

	/**
	 * ���ResultSet ��¼�����󷵻ض���T ��
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public T getObjecFromRs(ResultSet rs) throws SQLException;
	

}
