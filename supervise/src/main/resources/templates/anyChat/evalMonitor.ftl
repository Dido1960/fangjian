<html>
<head>
    <meta charset="utf-8">
    <title>监控音视频</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/logicfunc.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatsdk.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatevent.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/advanceset.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/js/anychat/bigScreenMoniter.js"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/js/dpsdk/dpocxfun.js"></script>
    <link type="text/css" href="${ctx}/css/anychat/bigScreenMoniter.css" rel="stylesheet"/>
</head>
<body style="overflow: hidden">
    <h2>甘肃交易通电子评标 (远程异地评标室)</h2>
    <div class="cont">
        <div class="left">
            <div class="left-top">
                <div>
                    <h3>标段名称</h3>
                    <p>${bidSection.bidSectionName}</p>
                </div>
                <div>
                    <h3>标段编号</h3>
                    <p>${bidSection.bidSectionCode}</p>
                </div>
            </div>
            <div class="left-cen">
                <div class="host">
                    <h3>主场<span>${homeReg.regName}公共资源交易中心${homeSite.name}</span></h3>
                    <div><object id="DPSDK_LEFT" classid="CLSID:D3E383B6-765D-448D-9476-DFD8B499926D" style="width: 100%; height: 100%" codebase="DpsdkOcx.cab#version=1.0.0.0"></object></div>
                </div>
                <div class="main">
                    <h3>客场<span>${awayReg.regName}公共资源交易中心${awaySite.name}</span></h3>
                    <div><object id="DPSDK_RIGHT" classid="CLSID:D3E383B6-765D-448D-9476-DFD8B499926D" style="width: 100%; height: 100%" codebase="DpsdkOcx.cab#version=1.0.0.0"></object></div>
                </div>
            </div>
            <div class="left-bottom">
                <div class="notice">
                    <div class="notice-cont">
                        <div class="cont-first">
                            <p>评标开始时间</p>
                            <p class="time">
                                <#if bidSection.evalStartTime?? && bidSection.evalStartTime != "">
                                    ${bidSection.evalStartTime}
                                <#else>
                                    尚未开始
                                </#if>
                            </p>
                        </div>
                        <div class="cont-second">
                            <p>当前环节</p>
                            <div id="now-eval-flow">
                                <#if bidSection.evalStartTime?? && bidSection.evalStartTime != "">
                                    ...
                                <#else>
                                    尚未开始
                                </#if>
                            </div>
                        </div>
                        <div class="cont-third">
                            <p>评标用时间</p>
                            <ul>
                                <li class="hour-one">-</li>
                                <li class="hour-two">-</li>
                                <b>:</b>
                                <li class="min-one">-</li>
                                <li class="min-two">-</li>
                                <b>:</b>
                                <li class="second-one">-</li>
                                <li class="second-two">-</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="company">四川权晟科技有限公司</div>
            </div>
        </div>
        <div class="right">
            <ul id="expert-video">
            </ul>
        </div>
    </div>
<script type="text/javascript">
    $(function () {
        setInterval(getNowEvalFlow, 60000);
        // 加载主场监控
        initDPSDK("DPSDK_LEFT", "${homeReg.regNo}", "${homeSite.name}");
        // 加载客场监控
        initDPSDK("DPSDK_RIGHT", "${awayReg.regNo}", "${awaySite.name}");

        //initDPSDK("DPSDK_RIGHT", "620901", "12楼第六评标室");

        // 基础信息的初始化
        ip = '${ip}';
        port = '${port}';
        roomId = '${bidSection.id}';

        var expert = {"name": "SPECIAL_JK"};
        loginJson = JSON.stringify(expert);

        onloadAnyChat();

        // 计时器实现评标时长
        if ('${bidSection.evalStatus}' == '2' && !isNull('${bidSection.evalEndTime}')) {
            var endtime = new Date('${bidSection.evalEndTime}'.replace(/-/g, "/")).getTime();
            var start = new Date('${bidSection.evalStartTime}'.replace(/-/g, "/")).getTime();
            calcTimeDiff(endtime, start);
        } else {
            if (!isNull('${bidSection.evalStartTime}')) {
                var interval = setInterval(function(){
                    $.ajax({
                        url: '${ctx}/gov/getBidSectionById',
                        type: 'post',
                        cache: false,
                        data: {
                            bidSectionId: '${bidSection.id }'
                        },
                        success: function (data) {
                            if (!isNull(data)) {
                                if (data.evalStatus == '2' && !isNull(data.evalEndTime)) {
                                    var endtime = new Date(data.evalEndTime.replace(/-/g, "/")).getTime();
                                    var start = new Date(data.evalStartTime.replace(/-/g, "/")).getTime();
                                    calcTimeDiff(endtime, start);
                                    clearInterval(interval);
                                } else {
                                    if (!isNull(data.evalStartTime)) {
                                        var nowtime = new Date().getTime();
                                        var start = new Date(data.evalStartTime.replace(/-/g, "/")).getTime();
                                        calcTimeDiff(nowtime, start);
                                    }
                                }

                            }
                        }
                    });
                },1000);

            }
        }
    })

    /**
     * 计算时间差
     */
    function calcTimeDiff(end, start) {
        var runDate = parseInt((end - start)/1000);
        runDate = runDate - 1;
        if (runDate >= 0) {
            console.log(runDate)
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
        }
    }

    /**
     * 获取当前评审环节
     */
    function getNowEvalFlow() {
        $.ajax({
            url: '${ctx}/gov/getNowEvalFlow',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: '${bidSection.id }'
            },
            success: function (data) {
                if (!isNull(data)) {
                    $("#now-eval-flow").html(data)
                }
            }
        });
    }
</script>
</body>
</html>
