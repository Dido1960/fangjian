<link rel="stylesheet" href="${ctx}/css/selectLeader.css">
<#if chairman??>
    <div class="current">当前专家组长：${chairman.expertName}</div>
</#if>
<ul>
    <#if experts??>
        <#list experts as expert>
            <li>
                <img src="${ctx}/img/face.png" alt="">
                <div class="word">
                    <p title="${expert.expertName!}">专家姓名:${expert.expertName!}</p>
                    <p title="${expert.company!}">工作单位:${expert.company!}</p>
                    <p title="${expert.categoryName!}">专家类别:${expert.categoryName!}</p>
                </div>
            </li>
        </#list>
    </#if>
</ul>
