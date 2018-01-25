<#--获取查询数据类型-->
<#function getDataType colType start>
<#if (colType=="Long") > <#return "L">
<#elseif (colType=="Integer")><#return "N">
<#elseif (colType=="Double")><#return "BD">
<#elseif (colType=="Short")><#return "SN">
<#elseif (colType=="Date" && start=="1")><#return "DL">
<#elseif (colType=="Date" && start=="0")><#return "DG">
<#else><#return "S"></#if>
</#function>



<#--获取查询数据类型-->
<#function getJsDataType colType>
<#if (colType=="Double")><#return "number">
<#elseif (colType=="Float")><#return "number">
<#elseif (colType=="Integer")><#return "int">
<#elseif (colType=="Short")><#return "boolean">
<#elseif (colType=="java.util.Date")><#return "date">
<#else><#return "string"></#if>
</#function>


<#--获取查询数据类型-->
<#function getJsXType colType>
<#if (colType=="Long") > <#return "numberfield">
<#elseif (colType=="Double")><#return "numberfield">
<#elseif (colType=="Float")><#return "numberfield">
<#elseif (colType=="Integer")><#return "numberfield">
<#elseif (colType=="Short")><#return "booleanComboBox">
<#elseif (colType=="java.util.Date")><#return "datefield">
<#else><#return "textfield"></#if>
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
