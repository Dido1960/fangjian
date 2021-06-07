<link rel="stylesheet" href="${ctx}/css/evalResult.css">
<style>
    .right-document ol li {
        width: 18.75%;
    }
    .right-document .right-table table tr td {
        width: 18.75%;
    }
</style>
<div class="right-document">
    <ol>
        <li>序号</li>
        <li class="long">投标人名称</li>
        <li>初步评审</li>
        <li>详细评审</li>
        <li>资格预审结果</li>
    </ol>
    <div class="right-table">
        <table cellpadding=0 cellspacing=0>
            <#list bidders as bidder>
                <tr>
                    <td>${bidder_index+1}</td>
                    <td class="long">${bidder.bidderName}</td>
                    <#if bidder.preReviewResult == "1">
                        <td>通过</td>
                        <#if bidder.detailReviewResult == "1">
                            <td>通过</td>
                        <#else >
                            <td>不通过</td>
                        </#if>
                    <#else >
                        <td>不通过</td>
                        <td>--</td>
                    </#if>

                    <#if bidder.reviewResult == "1">
                        <td>通过</td>
                    <#else >
                        <td>不通过</td>
                    </#if>
                </tr>
            </#list>
        </table>
    </div>
</div>