<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign tabComment=model.tabComment>
<#assign commonList=model.commonList>
<%--
	time:${date?string("yyyy-MM-dd HH:mm:ss")}
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>${tabComment}明细</title>
	<%@include file="/commons/include/getById.jsp" %>
	<script type="text/javascript">
		//放置脚本
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">${tabComment}详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="back bar-button" href="list.ht">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
					<#list commonList as col>
					<tr>
						<th width="20%">${col.comment}:</th>
						<td><#noparse>${</#noparse>${classVar}.${col.columnName}}</td>
					</tr>
					</#list>
				</table>
		</div>
</div>

</body>
</html>
