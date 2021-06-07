<link rel="stylesheet" href="${ctx}/css/leftRightBase.css">
<div class="left">
    <ul>
        <li data-flowname="resumeTime"  onclick="goToUrl('${ctx}/bidderModel/resumeTimePage', this)">
            复会时间
            <i></i>
        </li>
    </ul>
</div>
<div class="right">
    <div class="right-top">
        <h3><p>${bidSection.bidSectionName}</p></h3>
        <p class="year">2020年06月20日</p>
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
        listOnlineProcessComplete();
        setInterval(showtime, 1000);

        var $left_ul_li = $(".left ul li");
        var bidder_now = localStorage.getItem("bidder_now_${bidSection.id}_${bidder.id}");
        if ( bidder_now != null && !isNull($left_ul_li.eq(bidder_now))) {
            $left_ul_li.eq(bidder_now).trigger("click");
        } else {
            $left_ul_li.eq(0).trigger("click");
        }
    })

</script>