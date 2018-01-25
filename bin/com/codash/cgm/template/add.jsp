<#assign class=model.variables.class>
<#assign tabcomment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign commonList=model.commonList>
<%--
	time:${date?string("yyyy-MM-dd HH:mm:ss")}
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>添加 ${tabcomment}</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/servlet/ValidJs?form=${classVar}"></script>
	<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			valid(showRequest,showResponse);
			$("a.save").click(function() {
				<#noparse>$('#</#noparse>${classVar}Form').submit(); 
			});
		});
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">添加${tabcomment}</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="save bar-button" id="dataFormSave" href="#">保存</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="back bar-button" href="list.ht">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">

				<form id="${classVar}Form" method="post" action="add2.ht">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<#list commonList as col>
						<tr>
							<th width="20%">${col.comment}: <#if (col.isNotNull) > <span class="required">*</span></#if>:</th>
							
							<td><input type="text" id="${col.columnName}" name="${col.columnName}" value="" <#if (col.colType=="java.util.Date")>class="inputText date"<#else>class="inputText"</#if> /></td>
						</tr>
						</#list>
					</table>
				</form>
		</div>
</div>
</body>
</html>
