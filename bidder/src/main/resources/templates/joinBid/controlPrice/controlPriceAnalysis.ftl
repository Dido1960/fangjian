<link rel="stylesheet" href="${ctx}/css/controlPriceAnalysis.css">

<#if controlPrice??>
<div class="right-center">
    <#if bidSection.bidClassifyCode == "A12">最高投标限价:<#else >招标控制价：</#if>${((controlPrice)?number)?string(",###.00")}（元）
</div>
</#if>
<div class="right-bottom" style="display: none">
    <ul>
        <li>投标人名称</li>
        <li>投标报价（元）</li>
        <li>结果</li>
    </ul>
    <div class="contentBox">
        <#if bidders??>
            <#list bidders as bidder>
                <div class="content">
                    <div>${bidder.bidderName!}</div>
                    <#if bidder.bidderOpenInfo.bidPrice == null || bidder.bidderOpenInfo.bidPrice == "">
                        <div>/</div>
                    <#else >
                        <div>${((bidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}</div>
                    </#if>
                    <div>
                        <#if bidder.bidderOpenInfo.bidPrice?if_exists&&controlPrice?if_exists>
                            <#if bidder.bidderOpenInfo.bidPrice?number <= controlPrice?number>
                                <span class="green-f green-s">正常</span>
                            <#else >
                                <span class="oragen-f oragen-s">异常</span>
                            </#if>
                        <#else >
                            <span class="oragen-f oragen-s">异常</span>
                        </#if>
                    </div>
                </div>
            </#list>
        </#if>
        <div style="height: 20px"></div>
    </div>
    <input type="hidden" value="${currentBidders.bidderOpenInfo.decryptStatus}" class="decryptStatus">
    <input type="hidden" id="tenderRejection" value="${currentBidders.bidderOpenInfo.tenderRejection!}">
</div>
<div class="right-down2 right-refuse" style="display: none">
    <#if bidSection.bidClassifyCode == "A12">
        <p>无法提供最高投标限价分析</p>
    <#else >
        <p>无法提供控制价分析</p>
    </#if>
</div>
<div class="controlPrice-no right-refuse" style="display: none">
    <#if bidSection.bidClassifyCode == "A12">
        <p>暂未设置最高投标限价</p>
    <#else >
        <p>暂未设置控制价</p>
    </#if>
</div>
<div class="right-msg tender-reject" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>标书已被拒绝</p>
    <p class="rejectReason">拒绝理由：${currentBidders.bidderOpenInfo.tenderRejectionReason}</p>
</div>

<script>
    $(function () {
        //初始化页面数据
        initPageData();
    });

    /**
     *  页面初始化
     */
    function initPageData() {
        var decryptStatus = $(".decryptStatus").val();
        var tenderRejection = $("#tenderRejection").val();
        if (tenderRejection == 1) {
            //被标书拒绝
            $(".tender-reject").show();
            return false;
        }
        if (decryptStatus == 1) {
            if (isNull('${controlPrice}')){
                $(".controlPrice-no").show();
            }else {
                $(".right-bottom").show();
            }
        }else {
            $(".right-down2").show();
        }
    }

</script>