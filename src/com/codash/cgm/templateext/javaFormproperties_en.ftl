<#assign comment=model.tabComment>
<#assign classVar=model.variables.classVar>
# ${comment} 属性
 <#list model.columnList as col>
	<#if !col.isPK>
${classVar}.${col.columnName}=${col.columnName}
	</#if>
</#list>
${classVar}.editing=${comment} editing
${classVar}=${comment}


