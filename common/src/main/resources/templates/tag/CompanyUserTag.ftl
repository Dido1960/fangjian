<#--单位上传标签文件上传-->
<#assign x = "">
<#assign y = "">
<#if id??>
    <#assign x = x + " id=\"" + id + "\"">
</#if>
<#if class??>
    <#assign x = x + " class=\"" + class + "\"">
</#if>
<#if style??>
    <#assign x = x + " style=\"" + style + " ;padding-right:10px\"">
</#if>
<#if reqtext??>
    <#assign x = x + " lay-reqtext=\"" + reqtext + "\"">
</#if>
<#if readonly??>
    <#assign x = x + " readonly=\"" + readonly + "\"">
</#if>
<#if disabled??>
    <#assign x = x + " disabled=\"" + disabled + "\"">
</#if>
<#if verify??>
    <#assign x = x + " lay-verify=\"" + verify + "\"">
</#if>
<#if placeholder??>
    <#assign x = x + " placeholder=\"" + placeholder + "\"">
</#if>
<#--隐藏域需要的-->
<#if name??>
    <#assign y = y + " name=\"" + name + "\"">
</#if>
<#if value??>
    <#assign y = y + " value=\"" + value + "\"">
</#if>
<#if allowType>
    <#assign y = y + " allow_type=\"" + allowType + "\"">
</#if>
<#if allowFileSize>
    <#assign y = y + " allow_file_size=\"" + allowFileSize + "\"">
</#if>


<input type="hidden" ${y} />
<input ${x} value="${compayUser.userName}"/>
