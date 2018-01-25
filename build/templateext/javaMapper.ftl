<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign type="com.codash.platform.model."+class>
<#assign tableName=model.tableName>
<#assign colList=model.columnList>
<#assign commonList=model.commonList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.getPkVar(model) >

<#-- 模板开始  -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd"> 
<mapper namespace="${type}">
	<resultMap id="${class}" type="${type}">
		<#list colList as col>
		<#if (col.isPK) >
		<id property="${col.columnName}" column="${col.columnName}" jdbcType="${func.getJdbcType(col.colDbType)}"/>
		<#else>
		<result property="${col.columnName}" column="${col.columnName}" jdbcType="${func.getJdbcType(col.colDbType)}"/>
		</#if>
		</#list>
	</resultMap>
	
	<sql id="columns">
		<#list colList as col>${col.columnName}<#if col_has_next>,</#if></#list>
	</sql>
	
	<sql id="dynamicWhere">
		<where>
			<#list colList as col>
			<#if (col.colType=="String")>
			<if test="@Ognl@isNotEmpty(${col.columnName})"> AND ${col.columnName}  LIKE '%<#noparse>${</#noparse>${col.columnName}}%'  </if>
			<#else>
			<if test="@Ognl@isNotEmpty(${col.columnName})"> AND ${col.columnName}  =<#noparse>#{</#noparse>${col.columnName}} </if>
			</#if>
			</#list>
		</where>
	</sql>

	<insert id="add" parameterType="${type}">
		INSERT INTO ${tableName}
		(<#list colList as col>${col.columnName}<#if col_has_next>,</#if></#list>)
		VALUES
		(<#list colList as col><#noparse>#{</#noparse>${col.columnName},jdbcType=${func.getJdbcType(col.colDbType)}<#noparse>}</#noparse><#if col_has_next>, </#if></#list>)
	</insert>
	
	<delete id="delById" parameterType="java.lang.Long">
		DELETE FROM ${tableName} 
		WHERE
		${pk}=<#noparse>#{</#noparse>${pk}}
	</delete>
	
	<update id="update" parameterType="${type}">
		UPDATE ${tableName} SET
		<#list commonList as col>
		${col.columnName}=<#noparse>#{</#noparse>${col.columnName},jdbcType=${func.getJdbcType(col.colDbType)}<#noparse>}</#noparse> <#if col_has_next>, </#if>
		</#list>
		WHERE
		${pk}=<#noparse>#{</#noparse>${pk}}
	</update>
	
	<select id="getById" parameterType="java.lang.Long" resultMap="${class}">
		SELECT <include refid="columns"/>
		FROM ${tableName}
		WHERE
		${pk}=<#noparse>#{</#noparse>${pk}}
	</select>
	
	<select id="getAll" resultMap="${class}">
		SELECT <include refid="columns"/>
		FROM ${tableName}   
		<include refid="dynamicWhere" />
		<if test="@Ognl@isNotEmpty(orderField)">
		order by <#noparse>${orderField}</#noparse> <#noparse>${orderSeq}</#noparse>
		</if>
		<if test="@Ognl@isEmpty(orderField)">
		order by ${pk}  desc
		</if>
	</select>
</mapper>
