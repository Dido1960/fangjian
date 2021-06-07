<link rel="stylesheet" href="${ctx}/css/evalResult.css">
<style>
    <#if tenderDoc.mutualSecurityStatus ?? && tenderDoc.mutualSecurityStatus ==1>
        .right-document ol li {
            width: 8.333%;
        }
        .right-document .right-table table tr td {
            width: 8.333%;
        }
    <#else >
        .right-document ol li {
            width: 9.375%;
        }
        .right-document .right-table table tr td {
            width: 9.375%;
        }
    </#if>

</style>
<div class="right-document">
    <ol>
        <li class="long">投标人名称</li>
        <li>投标报价(元)</li>
        <li>投标报价评分</li>
        <li>施工能力扣分</li>
        <li>施工组织设计扣分</li>
        <li>安全质量事故扣分</li>
        <li>不良行为扣分</li>
        <#if tenderDoc.mutualSecurityStatus ?? && tenderDoc.mutualSecurityStatus == 1>
            <li>互保共建</li>
        </#if>
        <li>综合得分</li>
        <li>排名排序</li>
    </ol>
    <div class="right-table">
        <table cellpadding=0 cellspacing=0>
            <#list data as obj>
                <tr>
                    <td class="long" title="${obj.bidderName}">${obj.bidderName}</td>
                    <td>
                        <#if obj.bidPrice?? && obj.bidPrice != ''>
                            ${obj.bidPrice?number?string(",###.##")}
                        </#if>
                    </td>
                    <td>${obj.businessScore}</td>
                    <td>${obj.ability}</td>
                    <td>${obj.organize}</td>
                    <td>${obj.quality}</td>
                    <td>${obj.badRecord}</td>
                    <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                        <td>${obj.mutualSecurity}</td>
                    </#if>
                    <td>${obj.totalScore}</td>
                    <td>${obj.orderNo}</td>
                </tr>
            </#list>
        </table>
    </div>
</div>