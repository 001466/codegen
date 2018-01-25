<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign path=model.variables.path>
package com.codash.platform.model;

/**
 * 对象功能:${model.tabComment} Model对象
 <#if vars.company??>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer??>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${class} {
<#list model.columnList as columnModel>
	// ${columnModel.comment}
	private ${columnModel.colType} ${columnModel.columnName?lower_case};
</#list>
<#list model.columnList as columnModel>

	public void set${columnModel.columnName?lower_case?cap_first}(${columnModel.colType} ${columnModel.columnName?lower_case}) {
		this.${columnModel.columnName?lower_case} = ${columnModel.columnName?lower_case};
	}
	
	public ${columnModel.colType} get${columnModel.columnName?lower_case?cap_first}() {
		return ${columnModel.columnName?lower_case};
	}
</#list>
}