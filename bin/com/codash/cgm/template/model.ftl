<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign path=model.variables.path>
package com.codash.platform.model.${package};

import com.codash.core.model.BaseModel;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * 对象功能:${model.tabComment} Model对象
 <#if vars.company??>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer??>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 */
public class ${class} extends BaseModel
{
<#list model.columnList as columnModel>
	// ${columnModel.comment}
	protected ${columnModel.colType} ${columnModel.columnName};
</#list>
<#list model.columnList as columnModel>

	public void set${columnModel.columnName?cap_first}(${columnModel.colType} ${columnModel.columnName}) 
	{
		this.${columnModel.columnName} = ${columnModel.columnName};
	}
	/**
	 * 返回 ${columnModel.comment}
	 * @return
	 */
	public ${columnModel.colType} get${columnModel.columnName?cap_first}() 
	{
		return ${columnModel.columnName};
	}
</#list>

   
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof ${class})) 
		{
			return false;
		}
		${class} rhs = (${class}) object;
		return new EqualsBuilder()
		<#list model.columnList as column>
		.append(this.${column.columnName}, rhs.${column.columnName})
		</#list>
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		<#list model.columnList as column>
		.append(this.${column.columnName}) 
		</#list>
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		<#list model.columnList as column>
		.append("${column.columnName}", this.${column.columnName}) 
		</#list>
		.toString();
	}
   
  

}