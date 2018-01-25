package com.codash.cgm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据表对象
 * 
 * @author codash
 * 
 */
public class TableModel {
	Log log=LogFactory.getLog(TableModel.class);  

	// 表名
	private String tableName;
	// 表注释
	private String tabComment;
	//外键关联
	private String foreighKey = "";
	
	private Map<String, String> variables=new HashMap<String, String>();

	// 表所有的列对象列表
	private List<ColumnModel> columnList = new ArrayList<ColumnModel>();

	// 子表数据
	private List<TableModel> subTableList = new ArrayList<TableModel>();

	/**
	 * 表名
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
	 * 表注释
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
	 * 表的数据库列
	 * 
	 * @return
	 */
	public List<ColumnModel> getColumnList() {
		return columnList;
	}
	
	/**
	 * 取得主键列表
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
	 * 取得主键对象。
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
	 * 取得普通列的列表
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
	 * 子表数据
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
	 * 相对于主表的外键
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
