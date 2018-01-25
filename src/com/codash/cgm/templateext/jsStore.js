<#assign jspackage=model.variables.jsPackage>
<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign comment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign pk=func.getPk(model) >
<#assign path=model.variables.path>

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


Ext.define('app.store.${class}Store', {
	extend : 'Ext.data.Store',
	alias : 'widget.${classVar}Store',
	requires : [ 'app.model.${class}Model' ],
	constructor : function(cfg) {
		var me = this;
		cfg = cfg || {};
		me.callParent( [ Ext.apply( {
			model : 'app.model.${class}Model',
			remoteSort : false,
			autoLoad : false,
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'POST'
				},
				api : {
					save : 'platform/${path}/${classVar}/save.do',
					create : 'platform/${path}/${classVar}/save.do',
					update : 'platform/${path}/${classVar}/save.do',
					read :   'platform/${path}/${classVar}/list.do',
					remove : 'platform/${path}/${classVar}/del.do'
				},
				reader : {
					type : 'json',
					rootProperty : 'data'
				},
				simpleSortMode : true
			}

		}, cfg) ]);
	}
});