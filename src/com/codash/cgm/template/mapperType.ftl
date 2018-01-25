<#import "function.ftl" as func>

<#list models as model>
<#assign tableName=model.tableName>
<#assign colList=model.columnList>
<#assign commonList=model.commonList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.getPkVar(model) >

-----------------start ${model.tableName}----------------------
	<#list colList as col>
	<#if (col.isPK) >
	<id property="${col.columnName}" column="${col.columnName}" jdbcType="${func.getJdbcType(col.colDbType)}"/>
	<#else>
	<result property="${col.columnName}" column="${col.columnName}" jdbcType="${func.getJdbcType(col.colDbType)}"/>
	</#if>
	</#list>

	INSERT INTO ${tableName}
	(<#list colList as col>${col.columnName}<#if col_has_next>,</#if></#list>)
	VALUES
	(<#list colList as col><#noparse>#{</#noparse>${col.columnName},jdbcType=${func.getJdbcType(col.colDbType)}<#noparse>}</#noparse><#if col_has_next>, </#if></#list>)

	UPDATE ${tableName} SET
	<#list commonList as col>
	${col.columnName}=<#noparse>#{</#noparse>${col.columnName},jdbcType=${func.getJdbcType(col.colDbType)}<#noparse>}</#noparse> <#if col_has_next>, </#if>
	</#list>
	WHERE
	${pk}=<#noparse>#{</#noparse>${pk}}
	
-----------------end ${model.tableName}----------------------
</#list>
	
	