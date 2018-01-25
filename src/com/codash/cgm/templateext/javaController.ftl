<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign path=model.variables.path>
<#assign comment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign pk=func.getPk(model) >
package com.codash.platform.${package}.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.codash.core.annotion.Action;
import org.springframework.web.bind.annotation.ResponseBody;
import com.codash.core.web.util.RequestUtil;
import com.codash.core.web.controller.BaseController;
import com.codash.core.web.query.QueryFilter;

import com.codash.platform.${package}.model.${class};
import com.codash.platform.${package}.service.${class}Service;

import com.codash.core.web.result.Result;






/**
 * 对象功能:${comment} 控制器类
 <#if vars.company??>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer??>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@Controller
@RequestMapping("/platform/${path}/${classVar}/")
public class ${class}Controller extends BaseController
{
	@Resource
	private ${class}Service ${classVar}Service;
	
	/**
	 * 取得${comment}分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	//@Action(description="查看${comment}分页列表")
	@ResponseBody
	public Result list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		QueryFilter queryFilter=new QueryFilter(request,true);
		Result<List<${class}>> rsPage=${classVar}Service.getPage(queryFilter);
		return rsPage;
	}
	
	/**
	 * 删除${comment}
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	//@Action(description="删除${comment}")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Result message=null;
		try{
			String[] lAryId =RequestUtil.getStringAryByStr(request, "${pk}");
			${classVar}Service.delByIds(lAryId);
			message=new Result(Result.Success, getText("record.deleted",getText("${classVar}.${classVar}")));
		}
		catch(Exception ex){
			message=new Result(Result.Fail, getText("record.delete.fail",getText("${classVar}.${classVar}")));
		}
		saveMessage(response, message);
	}

	

	/**
	 * 取得${comment}明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	//@Action(description="查看${comment}明细")
	public ${class} get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String id=RequestUtil.getString(request,"${pk}");
		${class} ${classVar} = ${classVar}Service.getById(id);		
		return ${classVar};
	}

}
