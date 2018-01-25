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



Ext.define('app.view.${jspackage}.UpdateWin',{
	extend:'Ext.window.DWindow',
	alias:'widget.${jspackage}UpdateWin',
	
	initComponent:function(){
		Ext.apply(this,{
			autoScroll:true,
			title:Ext.Res.${classVar}.editing,
			buttons:[{
				xtype:'xButton',
				text:Ext.Res.iconText.save,
				iconCls:'btn-save',
				action:'save'
			},{
				xtype:'xButton',
				text:Ext.Res.iconText.del,
				iconCls:'btn-del',
				action:'delete'
			},{
				xtype:'button',
				text:Ext.Res.iconText.cancel,
				iconCls:'btn-cancel',
				action:'cancel'
			}
			],
			items:{
				xtype:'form',
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
								name:'${col.columnName}'
							},
						<#elseif (func.getJsXType(col.colType) == "numberfield")>	
							{
								xtype:'numberfield',
								<#if (col.isNotNull)>
								allowBlank :false,
								</#if>
								fieldLabel:Ext.Res.${classVar}.${col.columnName},
								name:'${col.columnName}'
							},
						<#else>	
							{
								xtype:'${func.getJsXType("${col.colType}")}',
								<#if (col.isNotNull)>
								allowBlank :false,
								</#if>
								fieldLabel:Ext.Res.${classVar}.${col.columnName},
								name:'${col.columnName}'
							},
						</#if>
					</#list>
					
					{
						xtype:'hidden',
						fieldLabel:'${pk}',
						name:'${pk}'
					}
					
				]
			}
		});
		this.callParent(arguments);
	}
});