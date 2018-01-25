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




Ext.define('app.view.${jspackage}.SearchGrid',{
	extend:'Ext.grid.Panel',
	alias:'widget.${jspackage}SearchGrid',
	requires:['app.store.${class}Store'],
	store:null,
	initComponent:function(){
		this.store=Ext.widget('${classVar}Store',{
			pageSize:25,
			autoLoad:true
		});
 		Ext.apply(this,{
			forceFit:true,
			store:this.store,
			selModel:{
 				selType:'checkboxmodel'
 			},
			columns:{
	 			defaults :{
					width:125
				},
	 			items:[
					<#list commonList as col>
						{
							text:Ext.Res.${classVar}.${col.columnName},
							dataIndex:'${col.columnName}'
						}
						<#if col_has_next>,</#if>
					</#list>
				]
			},
			bbar:{ xtype:'xPagingToolbar',store:this.store }
		});
		this.callParent(arguments);
	}
});