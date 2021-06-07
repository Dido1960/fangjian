<#assign x = "">
<#if id??>
    <#assign x = x + " id=\"" + id + "\"">
</#if>
<#if name??>
    <#assign x = x + " name=\"" + name + "\"">
</#if>
<#if class??>
    <#assign x = x + " class=\"" + class + "\"">
</#if>
<#if style??>
    <#assign x = x + " style=\"" + style + "\"">
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
<a href="#" ${x} value="${wordbook.bookKey}">${wordbook.bookValue}</a>