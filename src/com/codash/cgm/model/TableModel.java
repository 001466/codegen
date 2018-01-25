package com.codash.cgm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ���ݱ����
 * 
 * @author codash
 * 
 */
public class TableModel {
	Log log=LogFactory.getLog(TableModel.class);  

	// ����
	private String tableName;
	// ��ע��
	private String tabComment;
	//�������
	private String foreighKey = "";
	
	private Map<String, String> variables=new HashMap<String, String>();

	// �����е��ж����б�
	private List<ColumnModel> columnList = new ArrayList<ColumnModel>();

	// �ӱ�����
	private List<TableModel> subTableList = new ArrayList<TableModel>();

	/**
	 * ����
	 * 
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * ��ע��
	 * 
	 * @return
	 */
	public String getTabComment() {
		return tabComment.replaceAll("\r\n", "");
	}

	public void setTabComment(String tabComment) {
		this.tabComment = tabComment;
	}

	/**
	 * ������ݿ���
	 * 
	 * @return
	 */
	public List<ColumnModel> getColumnList() {
		return columnList;
	}
	
	/**
	 * ȡ�������б�
	 * @return
	 */
	public List<ColumnModel> getPkList()
	{
		List<ColumnModel> list=new ArrayList<ColumnModel>();
		for(ColumnModel col :columnList){
			if(col.getIsPK())
				list.add(col);
		}
		return list;
	}
	
	/**
	 * ȡ����������
	 * @return
	 */
	public ColumnModel getPkModel()
	{
		for(ColumnModel col :columnList){
			if(col.getIsPK()){
				return col;
			}
		}
		return null;
	}
	
	/**
	 * ȡ����ͨ�е��б�
	 * @return
	 */
	public List<ColumnModel> getCommonList()
	{
		List<ColumnModel> list=new ArrayList<ColumnModel>();
		for(ColumnModel col :columnList){
			if(!col.getIsPK())
				list.add(col);
		}
		return list;
	}
	

	public void setColumnList(List<ColumnModel> columnList) {
		this.columnList = columnList;
	}

	/**
	 * �ӱ�����
	 * 
	 * @return
	 */
	public List<TableModel> getSubTableList() {
		return subTableList;
	}

	public void setSubTableList(List<TableModel> subTableList) {
		this.subTableList = subTableList;
	}
	
	/**
	 * �������������
	 * @return
	 */
	public String getForeighKey() {
		return foreighKey;
	}

	public void setForeighKey(String foreighKey) {
		this.foreighKey = foreighKey;
	}
	
	
	public Map<String, String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
	
	
	
	

}
