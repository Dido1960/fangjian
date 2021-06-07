<link rel="stylesheet" href="${ctx}/css/allBidders.css">
<#--<p>暂未公布投标人信息</p>-->
<div class="right-center" style="display: none">
    <#assign isHide=data["isHide"] >
    <#assign currentBidder=data["currentBidder"] >
    <#assign otherBidders=data["otherBidders"] >
    <h3>当前共有投标人：
        <#if currentBidder != null>
            ${otherBidders?size+1}
        <#else >
            ${otherBidders?size}
        </#if>家
    </h3>
    <ul>
        <#if !isHide>
            <li>名称</li>
            <li>保证金状态</li>
            <li>法人授权委托人</li>
        <#else >
            <li style="width: 49%">名称</li>
            <li style="width: 50%">法人授权委托人</li>
        </#if>
    </ul>
    <div class="contentBox">
        <#--  当前投标人  -->
        <#if currentBidder != null>
            <div class="content">
                <#if !isHide>
                    <div>${currentBidder.bidderName!""}</div>
                    <#if currentBidder.bidderOpenInfo.marginPayStatus==0>
                        <div>未缴纳</div>
                    <#else >
                        <div>已缴纳</div>
                    </#if>
                    <div>${currentBidder.bidderOpenInfo.clientName}</div>
                <#else >
                    <div style="width: 50%">${currentBidder.bidderName!""}</div>
                    <div style="width: 50%">${currentBidder.bidderOpenInfo.clientName}</div>
                </#if>
            </div>
        </#if>

        <#--  其它投标人  -->
        <#if otherBidders??>
            <#list otherBidders as bidder>
                <div class="content">
                    <#if !isHide>
                        <div>${bidder.bidderName!""}</div>
                        <#if bidder.bidderOpenInfo.marginPayStatus==0>
                            <div>未缴纳</div>
                        <#else >
                            <div>已缴纳</div>
                        </#if>
                        <div>${bidder.bidderOpenInfo.clientName}</div>
                    <#else >
                        <div style="width: 49%">******</div>
                        <div style="width: 50%">******</div>
                    </#if>
                </div>
            </#list>
        </#if>

        <div style="height: 20px"></div>
    </div>
</div>

<div class="right-down2" style="display: none">
    <p>暂未到投标人名单环节，请耐心等待</p>
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
        if ('${lineStatus.publishBidderStatus}' == '2' && '${lineStatus.bidderCheckStatus}' == '2') {
            $(".right-center").show();
        } else {
            $(".right-down2").show();
        }
    }

</script>