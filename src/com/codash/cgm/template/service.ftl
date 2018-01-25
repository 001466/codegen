<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign path=model.variables.path>
package com.codash.platform.service.${package};

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.codash.core.db.IEntityDao;
import com.codash.core.service.BaseService;
import com.codash.platform.model.${package}.${class};
import com.codash.platform.dao.${package}.${class}Dao;

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
public class ${class}Service extends BaseService<${class}>
{
	@Resource
	private ${class}Dao dao;
	
	public ${class}Service()
	{
	}
	
	@Override
	protected IEntityDao<${class}, Long> getEntityDao() 
	{
		return dao;
	}
}
