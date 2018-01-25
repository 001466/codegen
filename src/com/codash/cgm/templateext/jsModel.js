<#assign jspackage=model.variables.jsPackage>
<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign comment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign pk=func.getPk(model) >

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


Ext.define('app.model.${class}Model', {
    extend: 'Ext.data.Model',
    alias: 'widget.${classVar}Model',
    idProperty:'${pk}',
    fields:[
	<#list model.columnList as columnModel>
		{name:'${columnModel.columnName}',type:'${func.getJsDataType("${columnModel.colType}")}'} <#if columnModel_has_next>,</#if> //${columnModel.comment}
 	</#list>
    ]
});    	