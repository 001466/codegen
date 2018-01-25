<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign comment=model.tabComment>
<#assign classVar=model.variables.class?lower_case>
<#assign classVar=model.variables.classVar>
<#assign commonList=model.commonList>
<#assign pkModel=model.pkModel>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.getPkVar(model) >

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${comment }管理</title>
<%@include file="/commons/include/get.jsp" %>
<%@include file="/js/msg.jsp" %>
</head>
<body>
			<div class="panel">
				<div class="panel-top">
					<div class="tbar-title">
						<span class="tbar-label">${comment }管理列表</span>
					</div>
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group"><a class="search bar-button" id="btnSearch">查询</a></div>
							<div class="l-bar-separator"></div>
							<div class="group"><a class="add bar-button" href="edit.ht">添加</a></div>
							<div class="l-bar-separator"></div>
							<div class="group"><a class="upd bar-button" id="btnUpd" action="edit.ht">修改</a></div>
							<div class="l-bar-separator"></div>
							<div class="group"><a class="del bar-button"  action="del.ht">删除</a></div>
						</div>	
					</div>
				</div>
				<div class="panel-body">
					<div class="panel-search">
							<form id="searchForm" method="post" action="list.ht">
									<div class="row">
										<#list commonList as col>
												<#if (col.colType=="java.util.Date")>
												<span class="label">${col.comment} 从:</span> <input  name="Q_begin${col.columnName}_${func.getDataType("${col.colType}","1")}"  class="inputText date" />
												<span class="label">至: </span><input  name="Q_end${col.columnName}_${func.getDataType("${col.colType}","0")}" class="inputText date" />
												<#else>
												<span class="label">${col.comment}:</span><input type="text" name="Q_${col.columnName}_${func.getDataType("${col.colType}","0")}"  class="inputText" />
												</#if>
											
										</#list>
									</div>
							</form>
					</div>
					<br/>
					<div class="panel-data">
				    	<c:set var="checkAll">
							<input type="checkbox" id="chkall"/>
						</c:set>
					    <display:table name="${classVar}List" id="${classVar}Item" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" export="true"  class="table-grid">
							<display:column title="<#noparse>${checkAll}</#noparse>" media="html" style="width:30px;">
								  	<input type="checkbox" class="pk" name="${pk}" value="<#noparse>${</#noparse>${classVar}Item.${pk}}">
							</display:column>
							<#list model.commonList as col>
							<#if (col.colType=="java.util.Date")>
							<display:column  title="${col.getComment()}" sortable="true" sortName="${col.getColumnName()}">
								<fmt:formatDate value="<#noparse>${</#noparse>${classVar}Item.${col.columnName}}" pattern="yyyy-MM-dd"/>
							</display:column>
							<#elseif (col.length > 256) >
								<display:column property="${col.getColumnName()}" title="${col.getComment()}" sortable="true" sortName="${col.getColumnName()}" maxLength="80"></display:column>
							<#else>
							<display:column property="${col.getColumnName()}" title="${col.getComment()}" sortable="true" sortName="${col.getColumnName()}"></display:column>
							</#if>
							</#list>
							<display:column title="管理" media="html" style="width:180px">
								<a href="del.ht?${pk}=<#noparse>${</#noparse>${classVar}Item.${pk}}" class="link-del"><span class="link-btn">删除</span></a>
								<a href="edit.ht?${pk}=<#noparse>${</#noparse>${classVar}Item.${pk}}" class="link-edit"><span class="link-btn">编辑</span></a>
								<a href="get.ht?${pk}=<#noparse>${</#noparse>${classVar}Item.${pk}}" class="link-detail"><span class="link-btn">明细</span></a>
							</display:column>
						</display:table>
						<codash:paging tableId="${classVar}Item"/>
					</div>
				</div><!-- end of panel-body -->				
			</div> <!-- end of panel -->
</body>
</html>


