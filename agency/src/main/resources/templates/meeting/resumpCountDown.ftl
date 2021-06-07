<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>复会倒计时</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/count.css">
</head>
<body>
<div class="app">
    <#include "${ctx}/common/baseTitile.ftl"/>
    <section>
<#--        <div class="site-demo-button" id="layerDemo" style="margin-bottom: 0;">-->
<#--            <div class="tlt-btn layui-btn layui-btn-normal" data-method="offset" data-type="r">参加复会投标人</div>-->
<#--        </div>-->
        <div class="name-num">
            <div class="ming"><p>标段名称</p><span>${bidSection.bidSectionName}</span></div>
            <div class="num"><p>标段编号</p><span>${bidSection.bidSectionCode}</span></div>
        </div>
        <div class="center">
            <div class="left">
                <div class="top">
                    <h3>复会时间</h3>
                    <p>${bidSection.resumeTime} </p>
                </div>
                <div class="bottom">
                    <img src="${ctx}/img/left_hum.png" alt="">
                    <p>代理机构：${tenderProject.tenderAgencyName}</p>
                    <p>联系电话：${tenderProject.tenderAgencyPhone}</p>
                </div>
            </div>
            <div class="right">
                <h3>距离复会还剩</h3>
                <ul class="time">
                    <li class="hour-one">2</li>
                    <li class="hour-two">4</li>
                    <p>:</p>
                    <li class="min-one">0</li>
                    <li class="min-two">0</li>
                    <p>:</p>
                    <li class="second-one">0</li>
                    <li class="second-two">0</li>
                </ul>
                <p class="bj"> 北京时间<br />
                    （国家授时中心）</p>
            </div>
        </div>
<#--        <div class="foot">-->
<#--            暂无投标人信息-->
<#--        </div>-->
    </section>
</div>
</body>

<div  style="display: none;">
    <div class="tan">
        <ul class="list">
            <li>投标人名称</li>
            <li>联系方式</li>
            <li>签到时间</li>
            <li>签到状态</li>
        </ul>
        <ul class="all-list">
        </ul>
    </div>

</div>

<form id="join-meeting-form" action="${ctx}/staff/joinMeetingPage" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" id="this-id" value="${bidSection.id}">
</form>
<script>
    layui.use('layer', function () { //独立版的layer无需执行这一句
        var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
        //触发事件
        var active = {
            offset: function (othis) {
                var type = othis.data('type'),
                    text = othis.text();
                layer.open({
                    type: 1,
                    offset: ['80px','50%'],
                    title: ['已签到投标人', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                    id: 'layerDemo' + type,
                    content: $('.tan').parent().html(),
                    area: ['50%', 'calc(100% - 80px)'],
                    shade: 0.3,
                    yes: function () {
                        layer.closeAll();
                    },
                    end:function () {
                        $('.tan').hide();
                    }
                });
            }
        };

        $('#layerDemo .layui-btn').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });

    });

    var  ajaxShowBidderCount=0;
    setInterval(function () {
        ajaxShowBidder();
    }, 5000)

    /**
     * bidder信息获取
     * **/
    function ajaxShowBidder() {
        if (ajaxShowBidderCount!=0) {
            return;
        }
        ajaxShowBidderCount++;

        <#--$.ajax({-->
        <#--    url: '${ctx}/staff/bidder/listBidderEnabled?bidSectionId=${bidSection.id}',-->
        <#--    cache:false,-->
        <#--    success: function (data) {-->
        <#--        if (data.code === "1") {-->
        <#--            var bidders = data.data;-->
        <#--            $(".all-list").html("");-->
        <#--            for (var i = 0; i < bidders.length; i++) {-->
        <#--                $(".foot").html("共计" + bidders.length + "家投标人");-->
        <#--                var bidder = bidders[i];-->
        <#--                var html = "<li>";-->
        <#--                html += "<div class='hum-name'>" + bidder.bidderName + "</div>";-->
        <#--                html += "<div class='hum-do'>" + (!isNull(bidder.bidderOpenInfo.clientPhone) ? bidder.bidderOpenInfo.clientPhone : '') + "</div>";-->
        <#--                html += "<div class='hum-time'>" + (!isNull(bidder.bidderOpenInfo.signinTime) ?bidder.bidderOpenInfo.signinTime : '') + "</div>";-->
        <#--                html += "<div class='hum-how'>" + (isNull(bidder.bidderOpenInfo.signinTime) ? '<span id="blove-do">未签到</span>' : '<span id="gray-do">已签到</span>') + "</div>";-->
        <#--                html += "</li>"-->
        <#--                $(".all-list").append(html);-->
        <#--            }-->
        <#--        } else {-->
        <#--            $(".all-list").html("");-->
        <#--            $(".all-list").html("<li style='line-height: 97px; font-size: 20px; padding-left: 45%; font-weight: 900'>暂无投标人信息</li>");-->
        <#--            $(".foot").html("");-->
        <#--            $(".foot").html("暂无投标人信息");-->
        <#--        }-->
        <#--        loadBidder = false;-->
        <#--    }-->
        <#--})-->
    }

    /**
     * 重载layui
     */
    function reloadLayui() {
        layui.use('form', function () {
            var form = layui.form;

            form.render();
        });
    }

    // 计时器实现开标倒计时
    var runDate = '${countDownTime}';
    var interval = setInterval(function(){
        runDate = runDate - 1;
        if (runDate >= 0) {
            var runTime = runDate;
            var day = Math.floor(runTime / 86400);
            runTime = runTime % 86400;

            var hour = Math.floor(runTime / 3600);
            var hourOne = Math.floor(hour / 10);
            var hourTwo = hour % 10;
            runTime = runTime % 3600;

            var minute = Math.floor(runTime / 60);
            var minuteOne = Math.floor(minute / 10);
            var minuteTwo = minute % 10;
            runTime = runTime % 60;

            var second = runTime;
            var secondOne = Math.floor(second / 10);
            var secondTwo = second % 10;
            $(".hour-one").text(hourOne);
            $(".hour-two").text(hourTwo);
            $(".min-one").text(minuteOne);
            $(".min-two").text(minuteTwo);
            $(".second-one").text(secondOne);
            $(".second-two").text(secondTwo);
        } else {
            clearInterval(interval);
            // layer.confirm('时间到，进入开标', {
            //     btn: ['确认', '取消']
            // }, function(){
                $("#join-meeting-form").submit();
            // }, function(){
            //     layer.msg('已取消');
            // });
            layer.msg("复会时间到，进入复会",{icon:1});
        }
    },1000);
</script>

</html>