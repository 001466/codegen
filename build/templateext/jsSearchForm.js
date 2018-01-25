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



Ext.define('app.view.${jspackage}.SearchForm',{
	extend:'Ext.form.Panel',
	alias:'widget.${jspackage}SearchForm',
	initComponent:function(){
		Ext.apply(this,{
			autoScroll:true,
        	items:[
				<#list commonList as col>
				<#if (func.getJsXType(col.colType) == "datefield") >
					{
						xtype:'datefield',
						fieldLabel:Ext.Res.${classVar}.${col.columnName},
						format:'Y-m-d',
						<#if (col.isNotNull)>
						allowBlank :false,
						</#if>
						name:'Q_${col.columnName}_${func.getDataType("${col.colType}","0")}'
					},
				<#elseif (func.getJsXType(col.colType) == "numberfield")>	
					{
						xtype:'numberfield',
						<#if (col.isNotNull)>
						allowBlank :false,
						</#if>
						fieldLabel:Ext.Res.${classVar}.${col.columnName},
						name:'Q_${col.columnName}_${func.getDataType("${col.colType}","0")}'
					},
				<#else>	
					{
						xtype:'${func.getJsXType("${col.colType}")}',
						<#if (col.isNotNull)>
						allowBlank :false,
						</#if>
						fieldLabel:Ext.Res.${classVar}.${col.columnName},
						name:'Q_${col.columnName}_${func.getDataType("${col.colType}","0")}'
					},
				</#if>
				</#list>
				
				{
					xtype:'hidden',
					fieldLabel:'${pk}',
					name:'Q_${pk}_L'
				}
 			],
			buttonAlign: 'center',
			buttons: [{
	                xtype: 'button',
	                text: Ext.Res.iconText.search,
	                iconCls: 'btn-search',
	                action:'search'
	            },
	            {
	                action: 'cancel',
	                scope: this,
	                iconCls: 'btn-cancel',
	                text: Ext.Res.iconText.reset
	        }]
		});
		this.callParent(arguments);
	}
});