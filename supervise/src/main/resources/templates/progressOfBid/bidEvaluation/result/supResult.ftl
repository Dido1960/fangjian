<link rel="stylesheet" href="${ctx}/css/evalResult.css">
<style>
    .right-document ol li {
        width: 12.5%;
    }
    .right-document .right-table table tr td {
        width: 12.5%;
    }
</style>
<div class="right-document">
    <ol>
        <li class="long">投标人名称</li>
        <li>投标报价(元)</li>
        <li>商务标得分</li>
        <li>技术标得分</li>
        <li>违章行为扣分</li>
        <li>综合得分</li>
        <li>排名排序</li>
    </ol>
    <div class="right-table">
        <table cellpadding=0 cellspacing=0>
            <#list data as obj>
                <tr>
                    <td class="long">${obj.bidderName}</td>
                    <td>
                        <#if obj.bidPrice?? && obj.bidPrice != ''>
                            ${obj.bidPrice?number?string(",###.##")}
                        </#if>
                    </td>
                    <td>${obj.businessScore}</td>
                    <td>${obj.technicalScore}</td>
                    <td>${obj.violationDeduct}</td>
                    <td>${obj.totalScore}</td>
                    <td>${obj.orderNo}</td>
                </tr>
            </#list>
        </table>
    </div>
</div>