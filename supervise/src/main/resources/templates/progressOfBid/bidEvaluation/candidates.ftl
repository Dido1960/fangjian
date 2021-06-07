<link rel="stylesheet" href="${ctx}/css/candidates.css">
<style>
    .right ol li {
        width: 25%;
    }

    .right .right-table table tr td {
        width: 25%;
    }
</style>
<ol>
    <li>候选人推荐</li>
    <li class="long">投标人</li>
    <li>综合得分</li>
</ol>
<div class="right-table">
    <table cellpadding=0 cellspacing=0>
        <#if data?size gt 0>
            <tr>
                <td>第一名</td>
                <td>${data[0].bidderName}</td>
                <td>${data[0].totalScore}</td>
            </tr>
        </#if>
        <#if data?size gt 1>
            <tr>
                <td>第二名</td>
                <td>${data[1].bidderName}</td>
                <td>${data[1].totalScore}</td>
            </tr>
        </#if>
        <#if data?size gt 2>
            <tr>
                <td>第三名</td>
                <td>${data[2].bidderName}</td>
                <td>${data[2].totalScore}</td>
            </tr>
        </#if>
    </table>
</div>