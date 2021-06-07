<link rel="stylesheet" href="${ctx}/css/leftRightBase.css">
<div class="left">
    <ul>
        <#if bidSection.bidClassifyCode == "A12">
            <li id="controlPriceLi" data-flowname="controlPrice" onclick="goToUrl('${ctx}/bidderModel/controlPricePage', this)">
                最高投标限价
                <i></i>
            </li>
        </#if>
        <#if bidSection.bidClassifyCode == "A08">
            <li id="controlPriceLi" data-flowname="controlPrice" onclick="goToUrl('${ctx}/bidderModel/controlPricePage', this)">
                招标控制价
                <i></i>
            </li>
            <li id="floatPointLi" data-flowname="floatPoint" onclick="goToUrl('${ctx}/bidderModel/floatPointPage', this)">
                浮动点
                <i></i>
            </li>
        </#if>
        <li id="allBiddersLi" data-flowname="allBidders" onclick="goToUrl('${ctx}/bidderModel/allBiddersPage', this)">
            投标人名单
            <i></i>
        </li>
        <#if bidSection.paperEval?? && bidSection.paperEval == "1">
            <li id="bidderFileDecryptLi" class="decryptLi" data-flowname="bidderFileDecrypt"
                onclick="goToUrl('${ctx}/bidderModel/bidderPaperFileDecryptPage', this)">
                投标文件解密
                <i></i>
            </li>
        <#else>
            <li id="bidderFileDecryptLi" class="decryptLi" data-flowname="bidderFileDecrypt"
                onclick="goToUrl('${ctx}/bidderModel/bidderFileDecryptPage', this)">
                投标文件解密
                <i></i>
            </li>
        </#if>
        <#if bidSection.bidClassifyCode != "A10">
            <li id="confirmBidderPriceLi" data-flowname="confirmBidderPrice" onclick="goToUrl('${ctx}/bidderModel/confirmBidderPricePage', this)">
                报价确认
                <i></i>
            </li>
        </#if>
        <#if bidSection.bidClassifyCode == "A08">
            <li id="controlPriceAnalysisLi" data-flowname="controlPriceAnalysis"
                onclick="goToUrl('${ctx}/bidderModel/controlPriceAnalysisPage', this)">
                控制价分析
                <i></i>
            </li>
        </#if>
        <#if bidSection.bidClassifyCode == "A12">
            <li id="controlPriceAnalysisLi" data-flowname="controlPriceAnalysis"
                onclick="goToUrl('${ctx}/bidderModel/controlPriceAnalysisPage', this)">
                最高投标限价分析
                <i></i>
            </li>
        </#if>
        <li id="confirmBidOpenRecordLi" class="recordLi" data-flowname="confirmBidOpenRecord"
            onclick="goToUrl('${ctx}/bidderModel/confirmBidOpenRecordPage', this)">
            开标一览表确认
            <i></i>
        </li>
        <li id="bidOpenEndLi" data-flowname="bidOpenEnd" onclick="goToUrl('${ctx}/bidderModel/bidOpenEndPage', this)">
            开标结束
            <i></i>
        </li>
    </ul>
</div>
<div class="right">
    <div class="right-top">
        <h3><p>${bidSection.bidSectionName}</p></h3>
        <p class="year">2020年06月20日 16:12:55</p>
        <p class="time"></p>
        <div class="top-foot">
            <span>代理机构：${tenderProject.tenderAgencyName }</span>
            <span>联系电话：${tenderProject.tenderAgencyPhone }</span>
        </div>
        <div class="live" onclick="toLivePage()">
            <div class="tu">
                <img src="${ctx}/img/live.png" alt="">
            </div>
            <span>直播</span>
        </div>
<#--        <div class="message" onclick="showOnlinePage()">-->
<#--            <span>..</span>-->
<#--        </div>-->
    </div>
    <div id="bidder-content-right"></div>
</div>
<script>
    $(function () {
        setInterval(showtime, 1000);
        listOnlineProcessComplete();
        var $left_ul_li = $(".left ul li");
        var bidder_now = localStorage.getItem("bidder_now_${bidSection.id}_${bidder.id}");
        var gotoMessages = localStorage.getItem("gotoMessages_${bidSection.id}_${bidder.id}");
        if (gotoMessages != null) {
            localStorage.removeItem("gotoMessages_${bidSection.id}_${bidder.id}");
            $("#"+gotoMessages).trigger("click");
        } else if (bidder_now != null && !isNull($left_ul_li.eq(bidder_now))) {
            $left_ul_li.eq(bidder_now).trigger("click");
        }  else {
            $left_ul_li.eq(0).trigger("click");
        }
    })

    /**
     * 显示当前时间
     */
    function showtime() {
        var nowtime = new Date();
        var year = timeAdd0(nowtime.getFullYear().toString());
        var month = timeAdd0((nowtime.getMonth() + 1).toString());
        var day = timeAdd0(nowtime.getDate().toString());

        var hours = timeAdd0(nowtime.getHours().toString());
        var min = timeAdd0(nowtime.getMinutes().toString());
        var sen = timeAdd0(nowtime.getSeconds().toString());
        var week = '星期' + ['日', '一', '二', '三', '四', '五', '六'][nowtime.getDay()];
        var year = year + "年" + month + "月" + day + "日" + " " + hours + ":" + min + ":" + sen;
        $(".year").text(year);
    }

</script>