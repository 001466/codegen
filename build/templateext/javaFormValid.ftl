<#assign classVar=model.variables.classVar>
<form name="${classVar}">
<#list model.columnList as column>
<#if !("version"==column.columnName || "createBy"==column.columnName || "updateBy"==column.columnName || "createtime"==column.columnName || "updatetime"==column.columnName || "deleted"==column.columnName) > 
	<#if !(column.isPK)>
		<#assign depends="">
		<#assign colsize=0>
		<#assign isDate=0>
		<#if (column.isNotNull)>
			<#assign depends = "required,">
		</#if>
		<#if ("String"==column.colType)>
			<#assign depends = depends+"maxlength">
			<#assign colsize = column.length >
		<#elseif ("Integer"==column.colType || "Short"==column.colType || "Long"==column.colType )>
			<#assign depends = depends+"digits">
		<#elseif ("Double"==column.colType || "Float"==column.colType)>
			<#assign depends = depends + "number">
		<#elseif ("java.util.Date"==column.colType)>
			<#assign depends = depends + "date">
			<#assign isDate = 1>
		</#if> 
		<#if depends!="">
		<field property="${column.columnName}" depends="${depends}">
			<arg position="0" key="${classVar}.${column.columnName}" />
			<#if (colsize > 0)> 
			<arg position="1" name="maxlength" key="<#noparse>${var:maxlength}</#noparse>" resource="false" />
			<var>
				<var-name>maxlength</var-name>
				<var-value>${colsize?string?replace(",","")}</var-value>
			</var>
			</#if>
			<#if (isDate > 0)>
			<var>
				<var-name>datePattern</var-name>
				<var-value>yyyy-MM-dd</var-value>
			</var>
			<var>
				<var-name>datePatternStrict</var-name>
				<var-value>yyyy-MM-dd HH:mm:ss</var-value>
			</var>
			</#if>
		</field>
		</#if>
	</#if>
</#if>
</#list>
</form>