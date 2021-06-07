<link rel="stylesheet" href="${ctx}/css/epcCalcPriceScore.css">
<h3>投标报价</h3>
<div class="right-document">
    <ol>
        <li>序号</li>
        <li class="long">投标单位</li>
        <li>投标报价（元)</li>
        <li class="long">评标基准价</li>
        <li>差值百分比（%）</li>
        <li>报价得分</li>
    </ol>
    <div class="right-table">
        <table cellpadding=0 cellspacing=0>
            <#list bidders as bidder>
                <tr>
                    <td>${bidder_index + 1}</td>
                    <td class="long">${bidder.bidderName}</td>
                    <#if isUpdate == 0>
                        <td>
                            <#if bidder.quoteScoreResult.bidPrice?? && bidder.quoteScoreResult.bidPrice != ''>
                                ${bidder.quoteScoreResult.bidPrice?number?string(",###.00")}
                            </#if>
                        </td>
                        <#if bidder_index == 0>
                            <td class="long" rowspan="${bidders?size}">${calcScoreParam.basePrice?number?string(",###.00")}</td>
                        </#if>
                        <td>${bidder.quoteScoreResult.bidPriceOffset}</td>
                        <td>${bidder.quoteScoreResult.bidPriceScore}</td>
                    <#else >
                        <td>
                            <#if bidder.quoteScoreResultAppendix.bidPrice?? && bidder.quoteScoreResultAppendix.bidPrice != ''>
                                ${bidder.quoteScoreResultAppendix.bidPrice?number?string(",###.00")}
                            </#if>
                        </td>
                        <#if bidder_index == 0>
                            <td class="long"  rowspan="${bidders?size}">
                                <#if calcScoreParam.updateBasePrice?? && calcScoreParam.updateBasePrice != ''>
                                    ${calcScoreParam.updateBasePrice?number?string(",###.00")}
                                <#else >
                                    ${calcScoreParam.basePrice?number?string(",###.00")}
                                </#if>
                            </td>
                        </#if>
                        <td>${bidder.quoteScoreResultAppendix.bidPriceOffset}</td>
                        <td>${bidder.quoteScoreResultAppendix.bidPriceScore}</td>
                    </#if>
                </tr>
            </#list>
        </table>
    </div>
</div>