<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助开标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <script src="${ctx}/js/convertMoney.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/css/resumptionMeeting.css">
    <script src="${ctx}/js/webService.js"></script>
    <style>
        .min-position-top {
            top: 212px !important;
            left: calc(100% - 292px) !important;
            position: absolute !important;
        }
    </style>
</head>
<body>
<#include "${ctx}/common/baseHeader.ftl"/>
<section>
    <div class="right">
        <div class="right-top">
            <h3><P title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</P></h3>
            <p class="year" id="dayBox">2020年12月24日</p>
            <p class="time" id="timeBox">20:25:00</p>
            <div class="top-foot">
                <span>代理机构：${tenderProject.tenderAgencyName!}</span>
                <span>联系电话：${tenderProject.tenderAgencyPhone}</span>
            </div>
            <div class="live" onclick="toLivePage()">
                <div class="tu">
                    <img src="${ctx}/img/live.png" alt="">
                </div>
                <span>直播</span>
            </div>
        </div>
        <div class="right-center">
            <#if bidSection.paperEval != '1'>
                <#include "${ctx}/meeting/meetingContent.ftl"/>
            <#else >
                <#include "${ctx}/meeting/paperMeetingContent.ftl"/>
            </#if>
        </div>
    </div>
</section>
<script>
    var bidOpenStatus = '${bidSection.bidOpenStatus}';
    var bidOpenEndTime = '${bidSection.bidOpenEndTime}';

    // 全局加载layui组件，无需在子页面单独引入
    var form, layer, laytpl, laypage, element;
    $(function () {
        setInterval(showtime, 1000);
        showMinOnlinePage();
    })

    /**
     * 显示系统当前时间
     */
    function showtime() {
        var objTime = new Date();
        var year = objTime.getFullYear();                 //年
        var month = timeAdd0((objTime.getMonth() + 1).toString());                   //月
        var day = timeAdd0(objTime.getDate().toString());                      //日期
        var hour = objTime.getHours() < 10 ? '0' + objTime.getHours() : objTime.getHours();                                      //时
        var minute = objTime.getMinutes() < 10 ? '0' + objTime.getMinutes() : objTime.getMinutes();                         //分
        var second = objTime.getSeconds() < 10 ? '0' + objTime.getSeconds() : objTime.getSeconds();                    //秒
        $("#dayBox").html(year + "年" + month + "月" + day + "日");
        // 时间
        $("#timeBox").html(hour + ":" + minute + ":" + second);                                                                                    //给span赋值显示时间
        objTime = new Date(year, month, day, objTime.getHours(), objTime.getMinutes(), objTime.getSeconds() + 1);   //当前时间加一秒
    }

    /**
     * 跳转直播查看页面
     */
    function toLivePage() {
        window.open("${ctx}/bidderModel/toLivePage", "_blank")
    }
</script>
</body>

</html>