<#assign package=model.variables.package>
<#assign class=model.variables.class>
/**
 * 对象功能:${model.tabComment} Dao类
 <#if vars.company??>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer??>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
package com.codash.platform.dao.${package};

import org.springframework.stereotype.Repository;
import com.codash.core.db.BaseDao;
import com.codash.platform.model.${package}.${class};

@Repository
public class ${class}Dao extends BaseDao<${class}>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return ${class}.class;
	}
}