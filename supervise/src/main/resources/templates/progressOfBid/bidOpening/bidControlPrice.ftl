<link rel="stylesheet" href="${ctx}/css/bidControlPrice.css">
<div class="right-top">
    <#if tenderDoc.controlPrice?if_exists>
        <#if bidSection.bidClassifyCode == 'A12'>最高投标限价:<#else >招标控制价：</#if>${((tenderDoc.controlPrice)?number)?string(",###.00")}(元)
    <#else >
        <#if bidSection.bidClassifyCode == 'A12'>暂未设置最高投标限价<#else >暂未设置控制价</#if>
    </#if>
</div>

<#if tenderDoc.controlPrice?if_exists>
    <div class="right-document">
        <ol>
            <li>投标人名称</li>
            <li>投标报价（元）</li>
            <li>结果</li>
        </ol>
        <div class="document-table">
            <table cellpadding=0 cellspacing=0>
                <#if bidders??>
                    <#list bidders as bidder>
                        <tr>
                            <td>${bidder.bidderName}</td>
                            <td>
                                <#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
                                    <#if bidder.bidderOpenInfo.bidPrice??>
                                        ${((bidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                                    <#else >
                                        /
                                    </#if>
                                <#else>
                                   ${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}
                                </#if>
                            </td>
                            <td>
                                <#if bidder.bidderOpenInfo.bidPrice?? && tenderDoc.controlPrice?number < bidder.bidderOpenInfo.bidPrice?number>
                                    <span class="yellow-f yellow-s">异常</span>
                                <#else >
                                    <span class="green-f green-s">正常</span>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                </#if>

            </table>
        </div>
    </div>
</#if>

