<#assign jspackage=model.variables.jsPackage>
<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign comment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign pk=func.getPk(model) >
<#assign path=model.variables.path>
<#assign commonList=model.commonList>


/**
 * 对象功能:${model.tableName}对象
 <#if vars.company??>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer??>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */



Ext.define('${jspackage}.view.${class}', {
	extend : 'Ext.window.DWindow',
	alias : 'widget.${jspackage}',
	requires : [
	        '${jspackage}.view.SearchForm',
			'${jspackage}.view.UpdateWin',
			'${jspackage}.view.SearchGrid'
	],
	layout : {
		type : 'border'
	},
	initComponent : function() {

		Ext.apply(this, {
					id:'${jspackage}',
					items : [{
								xtype : '${jspackage}SearchForm',
								region : 'north'
							}, {

								xtype : '${jspackage}SearchGrid',
								region : 'center'
							}],
					buttons : [{
								xtype : 'xButton',
								text : Ext.Res.iconText.add,
								iconCls : 'btn-add',
								action : 'add'
							}, {
								xtype : 'xButton',
								text : Ext.Res.iconText.del,
								iconCls : 'btn-del',
								action : 'delete'
							}]
					

				});
		this.callParent(arguments);
	}
});