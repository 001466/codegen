<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign path=model.variables.path>

<category id="${package}">
		<view name="${classVar}Add" value="/platform/${path}/${classVar}Add.jsp"/>
		<view name="${classVar}Upd" value="/platform/${path}/${classVar}Upd.jsp"/>
		<view name="${classVar}List" value="/platform/${path}/${classVar}List.jsp"/>
		<view name="${classVar}Get" value="/platform/${path}/${classVar}Get.jsp"/>
</category>