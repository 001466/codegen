<#--获取查询数据类型-->
<#function getDataType colType start>
<#if (colType=="long") > <#return "L">
<#elseif (colType=="int")><#return "N">
<#elseif (colType=="double")><#return "BD">
<#elseif (colType=="Short")><#return "SN">
<#elseif (colType=="Date" && start=="1")><#return "DL">
<#elseif (colType=="Date" && start=="0")><#return "DG">
<#else><#return "S"></#if>
</#function>

<#--将字符串 user_id 转换为 类似userId-->
<#function convertUnderLine field>
<#assign rtn><#list field?split("_") as x><#if (x_index==0)>${x?lower_case}<#else>${x?lower_case?cap_first}</#if></#list></#assign>
 <#return rtn>
</#function>

<#function getPk model>
<#assign rtn><#if (model.pkModel??) >${model.pkModel.columnName}<#else>"id"</#if></#assign>
 <#return rtn>
</#function>

<#function getPkVar model>
<#assign pkModel=model.pkModel>
<#assign rtn><#if (model.pkModel??) ><#noparse>${</#noparse>${model.pkModel.columnName}<#noparse>}</#noparse><#else>"id"</#if></#assign>
 <#return rtn>
</#function>

<#function getJdbcType dataType>
<#assign dbtype=dataType?lower_case>
<#assign rtn>
<#if  dbtype?ends_with("int") || (dbtype=="double") || (dbtype=="float") || (dbtype=="decimal") || dbtype?ends_with("number") >
NUMERIC
<#elseif (dbtype?index_of("char")>-1)  >
VARCHAR
<#elseif (dbtype=="date") || (dbtype=="datetime") >
DATE
<#elseif (dbtype?ends_with("text") || dbtype?ends_with("clob")) >
CLOB
</#if></#assign>
 <#return rtn?trim>
</#function>
