<link rel="stylesheet" type="text/css" href="${ctx}/css/rankingSummary.css">
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
<table class="layui-table" style="margin-top: 35px">
    <thead>
    <tr>
        <th>专家</th>
        <th>第一中标候选人</th>
        <th>第二中标候选人</th>
        <th>第三中标候选人</th>
    </tr>
    </thead>
    <tbody>
    <#list expertUsers as expert>
        <tr>
            <td>${expert.expertName}</td>
            <#if expert.candidateResults??>
                <#list expert.candidateResults as can>
                    <td>${can.bidderName}</td>
                </#list>
            <#else >
                <td></td>
                <td></td>
                <td></td>
            </#if>
        </tr>
    </#list>
    </tbody>
</table>


