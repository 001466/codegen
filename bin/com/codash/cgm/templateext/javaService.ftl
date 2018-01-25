<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign path=model.variables.path>

package com.codash.platform.${package}.service;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.codash.core.db.IEntityDao;
import com.codash.core.service.BaseStringPkService;

import com.codash.platform.${package}.dao.${class}Dao;
import com.codash.platform.${package}.model.${class};



/**
 * 对象功能:${model.tabComment} Service类
 <#if vars.company??>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer??>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
@Service
public class ${class}Service extends BaseStringPkService<${class}>
{
	@Resource
	private ${class}Dao dao;
	
	public ${class}Service()
	{
	}
	
	@Override
	protected IEntityDao<${class}, String> getEntityDao() 
	{
		return dao;
	}
}
