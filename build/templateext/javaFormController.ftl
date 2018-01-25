<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign path=model.variables.path>
<#assign comment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign pk=func.getPk(model) >
package com.codash.platform.controller.${package};

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.codash.core.annotion.Action;
import com.codash.core.util.UniqueIdUtil;
import com.codash.core.web.ResultMessage;
import com.codash.core.web.controller.BaseFormController;
import org.springframework.web.bind.annotation.ResponseBody;


import com.codash.platform.model.${class};
import com.codash.platform.service.${package}.${class}Service;

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
public class ${class}FormController extends BaseFormController
{
	@Resource
	private ${class}Service ${classVar}Service;
	
	/**
	 * 添加或更新${comment}。
	 * @param request
	 * @param response
	 * @param ${classVar} 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新${comment}")
	@ResponseBody
	public ResultMessage save(HttpServletRequest request, HttpServletResponse response, ${class} ${classVar},BindingResult bindResult) throws Exception
	{
		
		ResultMessage resultMessage=validForm("${classVar}", ${classVar}, bindResult, request,response);
		//add your custom validation rule here such as below code:
		//bindResult.rejectValue("name","errors.exist.student",new Object[]{"jason"},"重复姓名");
		if(resultMessage.isSuccess()==ResultMessage.Fail)
		{
			saveError(response,resultMessage);
			return resultMessage;
		}
		String resultMsg=null;
		if(${classVar}.get${pk?cap_first}()==null){
			${classVar}.set${pk?cap_first}(UniqueIdUtil.genId());
			${classVar}Service.add(${classVar});
			resultMsg=getText("record.added","${comment}");
		}else{
			${classVar}Service.update(${classVar});
			resultMsg=getText("record.updated","${comment}");
		}
		resultMessage.setMessage(resultMsg);
		saveMessage(response,resultMessage);
		resultMessage.setData(${classVar});
		return resultMessage;	
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param ${pk}
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected ${class} getFormObject(@RequestParam("${pk}") Long ${pk},Model model) throws Exception {
		logger.debug("enter ${class} getFormObject here....");
		${class} ${classVar}=null;
		if(${pk}!=null){
			${classVar}=${classVar}Service.getById(${pk});
		}else{
			${classVar}= new ${class}();
		}
		return ${classVar};
    }

}
