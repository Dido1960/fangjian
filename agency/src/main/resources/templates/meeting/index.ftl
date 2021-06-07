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
    <script src="${ctx}/js/webService.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/baseStaff.css">
    <link rel="stylesheet" href="${ctx}/css/resumptionList.css">
    <link rel="stylesheet" href="${ctx}/css/countDown.css">
    <style>
        .min-position-top {
            top: 184px !important;
            left: calc(100% - 283px) !important;
            position: absolute !important;
        }

        .no_bidders {
            width: 100%;
            height: 80px;
            font-size: 18px;
            font-family: Microsoft YaHei;
            font-weight: bold;
            line-height: 80px;
            color: #223165;
            text-align: center;
            position: relative;
        }

        .document-title span:not(:last-child) {
            float: left;
        }

        .document-title span:last-child {
            float: right;
        }

        .document-title .totalTime {
            float: left;
            line-height: 36px;
            margin-top: 20px;
            margin-left: 20px;
        }
    </style>

</head>
<body>
<#include "${ctx}/common/baseTitile.ftl"/>
<section>
    <div class="cont">
        <div class="head">
            <div><p>当前评标：</p><span title="${bidSection.bidSectionName!""}">${bidSection.bidSectionName!""}</span></div>
            <span class="current-time">2020年06月20日 星期一15:00:00</span>
        </div>
        <div class="message-head">
            <div class="message">当前环节：复会</div>
            <div class="countDown" id="openTime" onclick="startResumeCountDown()">开启倒计时</div>
        </div>
        <div style="clear: both"></div>
        <div class="jump">
            <div class="name">复会时间</div>
            <p></p>
            <#if bidSection.resumeStatus != 1 && bidSection.resumeStatus != 2>
                <span onclick="editResumeTime()">修改复会时间</span>
            </#if>
        </div>
        <div class="document">
            <div class="document-title">
                <#if bidSection.resumeStatus == 0>
                    <span class="green-b" onclick="staffStartMeeting()">开始复会</span>
                <#elseif bidSection.resumeStatus == 1>
                    <span class="red-b" onclick="staffEndMeeting()">结束复会</span>
                <#else >
                    <span class="red-b" onclick="staffStartMeeting()">重启复会</span>
                </#if>
                <p style="color: rgba(19, 97, 254, 1)" class="totalTime">复会总用时:0分0秒</p>
                <#if bidSection.paperEval != '1'>
                    <#if !(relate.resumptionReportUrl)>
                        <span class="blove-b" onclick="layer.msg('没有查询到数据',{icon:2})">复会报告</span>
                    <#else>
                        <span class="blove-b" onclick="showPdf('${relate.resumptionReportUrl!""}')">复会报告</span>
                    </#if>
                </#if>
            </div>
            <ol>
                <li>序号</li>
                <li>投标单位名称</li>
                <li>复会状态</li>
            </ol>
            <ul>
                <#if bidders>
                    <#list bidders as bidder>
                        <li>
                            <div>${bidder_index+1}</div>
                            <div>
                                <p>${bidder.bidderName}</p>
                            </div>
                            <div>
                                <#if bidder.bidderOpenInfo.resumeDetermine == 1>
                                    <p class="green-f" id="bidderStatus_${bidder.id}"
                                       data-value="${bidder.bidderOpenInfo.resumeDetermine }">无异议</p>
                                <#elseif bidder.bidderOpenInfo.resumeDetermine == 2>
                                    <p class="red-f" id="bidderStatus_${bidder.id}"
                                       data-value="${bidder.bidderOpenInfo.resumeDetermine }">有异议</p>
                                <#else >
                                    <p class="yellow-f" id="bidderStatus_${bidder.id}"
                                       data-value="${bidder.bidderOpenInfo.resumeDetermine }">尚无操作</p>
                                </#if>
                            </div>
                        </li>
                    </#list>
                <#else >
                    <div class="no_bidders">没有参加复会的投标人</div>
                </#if>
            </ul>
        </div>
    </div>
    <#--倒计时弹窗页面-->
    <div id="setTime" class="time-box" style="display: none">
        <div class="time-box-content">
            <div class="set-time-box">
                <div class="set-time-box_input">
                    <input
                            type="text"
                            placeholder="请选择"
                            id="checkTime"
                    />
                </div>
                <div class="set-time-box_label">开始时间</div>
            </div>
            <div class="show-time-box" style="display: none">
                <div class="show-time-box_item">00</div>
                <div class="show-time-box_hr">:</div>
                <div class="show-time-box_item">03</div>
                <div class="show-time-box_hr">:</div>
                <div class="show-time-box_item">55</div>
            </div>
        </div>
        <div class="time-box-btns">
            <div id="setTimeBtn">
                <div class="confirm" id="onConfirm">确认</div>
                <div class="concel" id="onConcel">取消</div>
            </div>
            <div id="showTimeBtn" style="display: none">
                <div class="confirm" id="onReset">重置</div>
                <div class="concel" id="onClose">关闭</div>
            </div>
        </div>
    </div>
</section>
<script>
    var bidOpenStatus = '${bidSection.bidOpenStatus}';
    var bidOpenEndTime = '${bidSection.bidOpenEndTime}';

    var getMeetingBidderListCount = 0;


    // 复会时间定时器
    var resumeTimeInterval;

    // 全局加载layui组件，无需在子页面单独引入
    var form, layer, laytpl, laypage, element;
    $(function () {
        showResumeTime('${bidSection.resumeTime}');

        setInterval(showtime, 1000);
        //加载消息盒子
        showMinOnlinePage();

        //获取投标人操作
        if ('${bidSection.resumeStatus}' == 1) {
            setInterval(getMeetingBidderList, 1000);
        }

        // 复会结束计算总用时（秒）
        var totalTime = '${lineStatus.resumeTime}';
        if (isNull(totalTime)) {
            totalTime = 0;
        }
        totalTime = parseInt(totalTime);

        // 刷新页面后，如果正在复会中
        if ('${lineStatus.resumeStatus}' == "1") {
            //最后一次解密开始时间
            var lastResumeStartTime = '${lastResumeStartTime}';
            //系统当前时间
            var nowTime = new Date('${nowTime}'.replace(/-/g, "/"));
            var startTime = new Date(lastResumeStartTime.replace(/-/g, "/"));
            var timeDiff = (nowTime.getTime() - startTime.getTime()) / 1000;
            // 当前质询总时间（页面加载时）
            var nowTotalTime = totalTime + parseInt(timeDiff + "");
            resumeTimeInterval = setInterval(function () {
                calResumeTime(nowTotalTime);
                nowTotalTime++;
            }, 1000);

        } else {
            calResumeTime(totalTime);
        }

    })

    /**
     * 计算总的复会时间
     */
    function calResumeTime(time) {
        var minutes = Math.floor(time / 60);
        var seconds = time % 60;
        $(".totalTime").text("复会总用时:" + minutes + '分' + seconds + '秒');
    }

    function showtime() {
        var nowtime = new Date();
        var year = timeAdd0(nowtime.getFullYear().toString());
        var month = timeAdd0((nowtime.getMonth() + 1).toString());
        var day = timeAdd0(nowtime.getDate().toString());

        var hours = timeAdd0(nowtime.getHours().toString());
        var min = timeAdd0(nowtime.getMinutes().toString());
        var sen = timeAdd0(nowtime.getSeconds().toString());
        var week = '星期' + ['日', '一', '二', '三', '四', '五', '六'][nowtime.getDay()];
        var time = year + "年" + month + "月" + day + "日" + " " + week + " " + hours + ":" + min + ":" + sen;
        $(".current-time").text(time);
    }

    /**
     * 预览pdf
     * @param url pdf的外网地址
     */
    function showPdf(url) {
        window.top.layer.open({
            type: 2,
            title: '复会报告',
            shadeClose: true,
            area: ['60%', '100%'],
            btn: 0,
            maxmin: true,
            offset: 'rb',
            content: "${ctx}/staff/previewPDFPage?url=" + url,
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 开始复会
     */
    function staffStartMeeting() {
        var now = new Date().getTime();
        var time = strToDate('${bidSection.resumeTime}').getTime();
        if (now >= time) {
            layerConfirm("确定要开始复会吗？", function () {
                $.ajax({
                    url: '${ctx}/staff/updateResumeStatus',
                    type: 'post',
                    cache: false,
                    data: {
                        id: ${bidSection.id},
                        // 开始复会
                        resumeStatus: 1
                    },
                    success: function (data) {
                        if (!isNull(data) && data.code === "2") {
                            doLoading("复会已开始！", 1, 2, null, null, function () {
                                window.location.reload();
                            });
                        }
                    },
                    error: function (data) {
                        console.error(data);
                    },
                });
            });
        } else {
            layerWarning("未到达复会时间！");
        }
    }

    //ie不支持代参newDate修复
    function strToDate(str) {
        //首先将日期分隔 ，获取到日期部分 和 时间部分
        var day = str.split(' ');
        //获取日期部分的年月日
        var days = day[0].split('-');
        //获取时间部分的 时分秒
        var mi = day[day.length - 1].split(':');
        //获取当前date类型日期
        var date = new Date();
        //给date赋值  年月日
        date.setUTCFullYear(days[0], days[1] - 1, days[2]);
        //给date赋值 时分秒  首先转换utc时区 ：+8
        date.setUTCHours(mi[0] - 8, mi[1], mi[2]);
        return date;
    }

    /**
     * 结束复会
     */
    function staffEndMeeting() {
        layerConfirm("确定要结束复会吗？", function () {
            $.ajax({
                url: '${ctx}/staff/updateResumeStatus',
                type: 'post',
                cache: false,
                async: false,
                data: {
                    id: ${bidSection.id},
                    // 结束复会
                    resumeStatus: 2
                },
                success: function (data) {
                    if (!isNull(data) && data.code === "2") {
                        //全部禁言
                        postMessageFun({clickJinyanBtn: true});
                        window.clearInterval(resumeTimeInterval);
                        doLoading("复会已结束！", 1, 3, null, null, function () {
                            window.location.reload();
                        });
                    }
                },
                error: function (data) {
                    console.error(data);
                },
            });
        });
    }

    function showResumeTime(time) {
        var timeArr = time.split(" ");
        var dateArr = timeArr[0].split("-");
        var dayArr = timeArr[1].split(":");

        $(".jump").find("p").text(dateArr[0] + "年" + dateArr[1] + "月" + dateArr[2] + "日 " + dayArr[0] + "时" + dayArr[1] + "分" + dayArr[2] + "秒");
    }

    function editResumeTime() {
        layer.open({
            type: 2,
            title: ['修改复会时间', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            content: "${ctx}/staff/setMeetingTimePage?bidSectionId=${bidSection.id}",
            area: ['600px', '450px'],
            btnAlign: 'c',
            shade: 0.3,
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.modifyResumeTime(function (result) {
                    layer.close(index);
                    if (result) {
                        window.top.layer.alert("修改成功", {
                            icon: 6, end: function () {
                                window.location.reload();
                            }
                        })
                    } else {
                        window.top.layer.alert("修改失败", {icon: 5});
                    }
                });
            },
            btn2: function (index) {
                parent.layer.close(index);
            }
        });
    }

    /**
     * 获取会议投标人数据
     */

    function getMeetingBidderList() {
        if (getMeetingBidderListCount != 0) {
            return;
        }
        getMeetingBidderListCount++;
        $.ajax({
            url: "${ctx}/staff/getMeetingBidderList",
            type: "POST",
            cache: false,
            success: function (data) {
                if (!isNull(data)) {
                    for (var i = 0; i < data.length; i++) {
                        var bidder = data[i];
                        var $bidder = $("#bidderStatus_" + bidder.id);
                        if ($bidder.attr("data-value") != bidder.bidderOpenInfo.resumeDetermine) {
                            $bidder.removeClass();
                            $bidder.attr("data-value", bidder.bidderOpenInfo.resumeDetermine);
                            if (bidder.bidderOpenInfo.resumeDetermine == 1) {
                                $bidder.addClass("green-f");
                                $bidder.text("无异议");
                            } else if (bidder.bidderOpenInfo.resumeDetermine == 2) {
                                $bidder.addClass("red-f");
                                $bidder.text("有异议");
                            }
                        }
                    }
                }
                getMeetingBidderListCount--;
            },
            error: function (e) {
                getMeetingBidderListCount--;
                console.error(e);
            }
        });
    }

</script>

<script>
    var countDownInterval;

    layui.use(["laydate"], function () {
        layui.laydate.render({
            elem: "#checkTime",
            type: "time",
        });
    });

   $(function () {
       $("#onConfirm").click(function () {
           var time = $("#checkTime").val();
           if (isNull(time)) {
               layer.msg("请设置倒计时长", {icon: 5});
               return;
           }

           $(".set-time-box").css("display", "none");
           $(".show-time-box").css("display", "block");
           $("#setTimeBtn").css("display", "none");
           $("#showTimeBtn").css("display", "block");
           var hour = time.split(":")[0];
           var minute = time.split(":")[1];
           var second = time.split(":")[2];
           $(".show-time-box")
               .find(".show-time-box_item:first-child")
               .text(hour);
           $(".show-time-box")
               .find(".show-time-box_item:nth-child(3)")
               .text(minute);
           $(".show-time-box")
               .find(".show-time-box_item:last-child")
               .text(second);
           //把设置的时间秒数存入缓存
           var countDownTime = parseInt(hour) * 60 * 60
               + parseInt(minute) * 60
               + parseInt(second);
           localStorage.setItem("resumeCountDownTime_${bidSection.id}", countDownTime);
           startCountDown();
       });
       $("#onConcel").click(function () {
           layer.closeAll();
           $("#setTime").css("display", "none");
       });
       $("#onReset").click(function () {
           // 移除复会倒计时缓存
           localStorage.removeItem("resumeCountDownTime_${bidSection.id}");
           $("#checkTime").val("");
           layer.msg('重置成功！', {icon: 1});
       });
       $("#onClose").click(function () {
           layer.closeAll();
           $("#setTime").css("display", "none");
       });
   });

    /**
     * 倒计时
     */
    function startCountDown() {
        clearInterval(countDownInterval);
        countDownInterval = setInterval(function () {
            var countDownTime = parseInt(localStorage.getItem("resumeCountDownTime_${bidSection.id}"));
            if (countDownTime > 0) {
                $(".set-time-box").css("display", "none");
                $(".show-time-box").css("display", "block");
                $("#setTimeBtn").css("display", "none");
                $("#showTimeBtn").css("display", "block");
                --countDownTime;
                localStorage.setItem("resumeCountDownTime_${bidSection.id}", countDownTime);
                var hours = Math.floor(countDownTime / (60 * 60)) + "";
                var minutes = Math.floor((countDownTime - (hours * (60 * 60))) / 60) + "";
                var seconds = Math.floor(countDownTime % 60) + "";
                hours = hours.length < 2 ? "0" + hours : hours;
                minutes = minutes.length < 2 ? "0" + minutes : minutes;
                seconds = seconds.length < 2 ? "0" + seconds : seconds;
                $(".show-time-box")
                    .find(".show-time-box_item:first-child")
                    .text(hours);
                $(".show-time-box")
                    .find(".show-time-box_item:nth-child(3)")
                    .text(minutes);
                $(".show-time-box")
                    .find(".show-time-box_item:last-child")
                    .text(seconds);
            } else {
                localStorage.removeItem("resumeCountDownTime_${bidSection.id}");
                clearInterval(countDownInterval);
                $(".set-time-box").css("display", "block");
                $(".show-time-box").css("display", "none");
                $("#setTimeBtn").css("display", "block");
                $("#showTimeBtn").css("display", "none");
                // layer.closeAll();
            }
        }, 1000);
    }

    /**
     * 复会倒计时弹窗
     */
    function startResumeCountDown() {
        if (!isNull(localStorage.getItem("resumeCountDownTime_${bidSection.id}"))) {
            startCountDown();
        }
        layer.open({
            type: 1,
            title: "时间设置",
            area: ["500px", "324px"],
            skin: "open-time-box",
            scrollbar: false,
            content: $("#setTime"),
            shade: 0.3,
            end: function () {
                $("#setTime").css("display", "none");
            },
        });
    }

</script>
</body>

</html>