<link rel="stylesheet" href="${ctx}/css/control.css">
<link rel="stylesheet" href="${ctx}/css/utils.css">
<div class="right">
    <div class="tips">招标控制价<span>
            <#assign controlPrice = "">
            <#if tenderDoc.controlPrice??>
                <#assign controlPrice = tenderDoc.controlPrice>
                ${tenderDoc.controlPrice}（元）
            <#else>
                未设置控制价
            </#if>
        </span>
    </div>
    <ol class="head">
        <li>投标人名称</li>
        <li>投标报价(元)</li>
        <li>结果</li>
    </ol>
    <ul class="cont">
        <#list bidders as bidder>
            <li>
                <div>
                    <p>${bidder.bidderName}</p>
                </div>
                <div>
                    <#if bidder.bidderOpenInfo.bidPrice??>
                        ${bidder.bidderOpenInfo.bidPrice?number?string(",###.##") }
                    <#else>
                        /
                    </#if>
                </div>
                <div>
                    <#if controlPrice != "">
                        <#if bidder.bidderOpenInfo.bidPrice??>
                            <#if bidder.bidderOpenInfo.bidPrice?number gt controlPrice?number>
                                <span class="red-f red-s">异常</span>
                            <#else>
                                <span class="green-f green-s">正常</span>
                            </#if>
                        <#else>
                            <span>/</span>
                        </#if>
                    <#else>
                        <span>/</span>
                    </#if>
                </div>
            </li>

        </#list>
    </ul>
</div>