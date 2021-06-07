<link rel="stylesheet" href="${ctx}/css/leftRightBase.css">
<div class="left">
    <ul>
        <#if bidSection.paperEval?? && bidSection.paperEval == "1">
            <li data-flowname="bidFile" class="bidFileLi" onclick="goToUrl('${ctx}/bidFile/bidPaperFileUpLoadPage', this)">
                文件递交
                <i></i>
            </li>
        <#else>
            <li data-flowname="bidFile" class="bidFileLi" onclick="goToUrl('${ctx}/bidFile/bidFileUpLoadPage', this)">
                文件递交
                <i></i>
            </li>
        </#if>
        <li data-flowname="identityAuth" class="identityAuthLi" onclick="goToUrl('${ctx}/identityAuth/msgInputPage', this)">
            签到
            <i></i>
        </li>
    </ul>
</div>
<div class="right">
    <div class="right-top">
        <h3><p>${bidSection.bidSectionName}</p></h3>
        <p class="year">2020年06月20日</p>
        <p class="time">03:21</p>
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
<#--        <div class="message" onclick="showMax()">-->
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

    function showMax(){
        $('.layui-layer-maxmin').click();
    }

</script>