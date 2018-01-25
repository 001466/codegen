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


Ext.define('${jspackage}.controller.${class}', {
	
	extend : 'sys.controller.BaseController',
	refs : [ {
		ref : 'searchForm',
		selector : '${jspackage} ${jspackage}SearchForm'
	}, {
		ref : 'searchGrid',
		selector : '${jspackage} ${jspackage}SearchGrid'
	} , {
		ref : 'updateForm',
		selector : '${jspackage}UpdateWin>form'
	}, {
		ref : 'updateWin',
		selector : '${jspackage}UpdateWin'
	}],
	init : function() {
	
		this.control( {
			// 保存
			'${jspackage}UpdateWin button[action=save]' : {
				click : function(b, e, eOpts) {
					this.doSave(this.getUpdateForm(), {
						idProperty : '${pk}',
						grid : this.getSearchGrid(),
						scope:this,
						callback:function(){
							this.getUpdateWin().close();
						}
					}

					);
				}
			},
			// 取消
			'${jspackage}UpdateWin button[action=cancel]' : {
				click : function(b, e, eOpts) {
					this.getUpdateWin().close();
				}
			},
			// 删除
			'${jspackage}UpdateWin button[action=delete]' : {
				click : function(b, e, eOpts) {
					this.doDelete(this.getSearchGrid(), {
						idProperty : '${pk}',
						callback:function(){
							this.getUpdateWin().close();
						}
					});
				}
			},
			// 查询
			'${jspackage} ${jspackage}SearchForm button[action=search]' : {
				click : function(b, e, eOpts) {
					this.doSearch(this.getSearchGrid(),this.getSearchForm());
				}
			},
			
			// 删除 多条
			'${jspackage}  button[action=delete]' : {
				click : function(b, e, eOpts) {
					this.doDelete(this.getSearchGrid(), {
						idProperty : '${pk}'
					});
				}
			},
			//新增
			'${jspackage}  button[action=add]' : {
				click : function(b, e, eOpts) {
					this.application.showOnDesktop('${jspackage}UpdateWin',{
						id:'${jspackage}UpdateWin'
					});
					this.getUpdateForm().reset();
				}
			},
			'${jspackage} ${jspackage}SearchGrid':{
				itemdblclick:function(grid, record, item, index, e, eOpts){
					this.application.showOnDesktop('${jspackage}UpdateWin').down('form').loadRecord(record);
				}
			}
			
		});

	}

});