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
    <script src="${ctx}/js/webService.js"></script>
    <script src="${ctx}/js/base64.js"></script>
    <script type="text/javascript" src="${ctx}/js/webService.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/baseStaff.css">
    <link rel="stylesheet" href="${ctx}/css/countDown.css">
    <style>
        /* 解决遮罩层占不满屏幕的问题 */
        .layui-layer, .layui-layer-shade {
            position: fixed !important;
        }

        .min-position-top {
            top: 184px !important;
            left: calc(100% - 283px) !important;
            position: absolute !important;
        }

        body .open-time-box .layui-layer {
            border-radius: 10px;
            overflow: hidden;
        }

        .open-time-box .layui-layer-title {
            height: 57px;
            background: #fff;
            text-align: center;
            line-height: 57px;
            font-weight: bold;
            font-size: 16px;
            padding: 0 50px;
        }
        body .project-liubiao .layui-layer-title {
            height: 57px;
            background: #ffffff;
            line-height: 57px;
            text-align: center;
            padding: 0 50px;
            color: #223165;
            font-weight: bold;
            font-size: 16px;
        }
        body .project-liubiao .layui-layer-setwin {
            top: 22px;
        }
    </style>
</head>
<body>
<#include "${ctx}/common/baseTitile.ftl"/>
<section>
    <div class="content">
        <div class="tlt">
            <div>
                <span>当前开标：</span>
                <p>${bidSection.bidSectionName}</p>
            </div>
            <span class="current-time">2020年06月20日 星期一15:00:00</span>

            <#--当前标段的主键-->
            <input type="hidden" id="bid-section-id" value="${bidSection.id}">
            <input type="hidden" id="bid-type" value="${bidType}">
        </div>
        <div class="matter">
            <div class="left">
                <ul>
                    <#if bidSection.bidClassifyCode == "A12">
                        <li data-flowname="controlPrice" data-img="left_1"
                            onclick="goToUrl('${ctx}/staff/maxBidPricePage', this)">
                            <img src="${ctx}/img/left_1_gray.png" alt="">
                            <p class="left_menu">最高投标限价</p>
                            <i></i>
                        </li>
                    </#if>
                    <#if bidSection.bidClassifyCode == "A08">
                        <li data-flowname="controlPrice" data-img="left_1"
                            onclick="goToUrl('${ctx}/staff/addControlPricePage', this)">
                            <img src="${ctx}/img/left_1_gray.png" alt="">
                            <p class="left_menu">招标控制价</p>
                            <i></i>
                        </li>
                        <li data-flowname="floatPoint" data-img="left_2"
                            onclick="goToUrl('${ctx}/staff/floatPointPage', this)">
                            <img src="${ctx}/img/left_2_gray.png" alt="">
                            <p class="left_menu">抽取浮动点</p>
                            <i></i>
                        </li>
                    </#if>
                    <li id="publishLi" data-flowname="publishBidder" data-img="left_3"
                        onclick="goToUrl('${ctx}/staff/publishBidderPage/1', this)">
                        <img src="${ctx}/img/left_3_gray.png" alt="">
                        <p class="left_menu">公布投标人名单</p>
                        <i></i>
                    </li>
                    <li id="checkLi" data-flowname="checkPrincipalIdentity" data-img="left_4"
                        onclick="goToUrl('${ctx}/clientCheck/clientCheckPage', this)">
                        <img src="${ctx}/img/left_4_gray.png" alt="">
                        <p class="left_menu">委托人身份检查</p>
                        <i></i>
                    </li>
                    <li id="fileDecLi" data-flowname="bidderFileDecrypt" data-img="left_5"
                        onclick="goToUrl('${ctx}/staff/bidderFileDecryptPage', this)">
                        <img src="${ctx}/img/left_5_gray.png" alt="">
                        <p class="left_menu">文件上传及解密</p>
                        <i></i>
                    </li>
                    <#if bidSection.bidClassifyCode == "A08">
                        <li data-flowname="controlPriceAnalysis" data-img="left_6"
                            onclick="goToUrl('${ctx}/staff/controlPriceAnalysisPage', this)">
                            <img src="${ctx}/img/left_6_gray.png" alt="">
                            <p class="left_menu">控制价分析</p>
                            <i></i>
                        </li>
                    </#if>
                    <#if bidSection.bidClassifyCode == "A12">
                        <li data-flowname="controlPriceAnalysis" data-img="left_6"
                            onclick="goToUrl('${ctx}/staff/controlPriceAnalysisPage', this)">
                            <img src="${ctx}/img/left_6_gray.png" alt="">
                            <p class="left_menu">最高投标限价分析</p>
                            <i></i>
                        </li>
                    </#if>
                    <#if bidSection.bidClassifyCode != "A10">
                        <li id="fileCursorLi" data-flowname="fileCursor" data-img="left_7"
                            onclick="goToUrl('${ctx}/staff/fileCursorPage', this)">
                            <img src="${ctx}/img/left_7_gray.png" alt="">
                            <p class="left_menu">文件唱标</p>
                            <i></i>
                        </li>
                    </#if>
                    <li data-flowname="bidOpenRecord" data-img="left_8"
                        onclick="goToUrl('${ctx}/staff/bidOpenRecordPage/1', this)">
                        <img src="${ctx}/img/left_8_gray.png" alt="">
                        <p class="left_menu">开标记录表</p>
                        <i></i>
                    </li>
                    <#if bidSection.paperEval?? && bidSection.paperEval == "1">
                        <li data-flowname="bidFileUpload" data-img="left_11"
                            onclick="goToUrl('${ctx}/staff/paperBidFileUploadPage', this)">
                            <img src="${ctx}/img/left_11_gray.png" alt="">
                            <p class="left_menu">数据上传评标</p>
                            <i></i>
                        </li>
                    <#else >
                        <li data-flowname="bidFileUpload" data-img="left_11"
                            onclick="goToUrl('${ctx}/staff/bidFileUploadPage', this)">
                            <img src="${ctx}/img/left_11_gray.png" alt="">
                            <p class="left_menu">数据上传评标</p>
                            <i></i>
                        </li>
                    </#if>

                    <li data-flowname="bidOpenEnd" data-img="left_9"
                        onclick="goToUrl('${ctx}/staff/bidOpenEndPage', this)">
                        <img src="${ctx}/img/left_9_gray.png" alt="">
                        <p class="left_menu">开标结束</p>
                        <i></i>
                    </li>
                    <li data-flowname="resumeTime" data-img="left_10"
                        onclick="goToUrl('${ctx}/staff/resumeTimePage', this)">
                        <img src="${ctx}/img/left_10_gray.png" alt="">
                        <p class="left_menu">复会时间</p>
                        <i></i>
                    </li>
                </ul>
            </div>
            <div class="right">
                <div class="right-head">
                    <div class="top">
                        项目进行环节：<b class="now_flow"></b>
                        <#--<i onclick="showMinOnlinePage()"></i>-->
                        <#--<span id="message_count">..</span>-->
                    </div>
                    <div class="countDown" id="openTime">开启倒计时</div>
                </div>
                <div class="load_url_content" id="scrollbar_base" style="height: calc(100vh - 270px);">
                    <iframe overflow="hidde" style="width: 100%;min-height: calc(100vh - 300px); border: 0px"
                            frameborder="0" id="showPdfView1"></iframe>
                </div>
                <div class="clear" style="clear:both;"></div>
            </div>
        </div>
        <div id="setTime" class="time-box" style="display: none">
            <div class="time-box-content">
                <div class="set-time-box">
                    <div class="set-time-box_input">
                        <input type="text"
                                placeholder="请选择"
                                id="checkTime"/>
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
    </div>
</section>
<script>
    var bidOpenStatus = '${bidSection.bidOpenStatus}';
    var bidOpenEndTime = '${bidSection.bidOpenEndTime}';
    var countDownInterval;
    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param e
     */
    function goToUrl(targetUrl, e) {
        judgmentBidSectionCancel(e, function (data, bidderSize, msg) {
            if (!data) {
                if (bidderSize < 3 && !isNull(msg)) {
                    loadComplete();
                    chooseCancelBidSection(msg);
                } else {
                    jumpPage(targetUrl, e);
                }
            } else {
                loadComplete();
                chooseCancelBidSection(msg);
            }
        })
    }

    /**
     * 跳转页面
     * @param targetUrl 目标路由
     * @param e
     */
    function jumpPage(targetUrl, e){
        if ((bidOpenStatus === "2" || !isNull(bidOpenEndTime)) && targetUrl != "${ctx}/staff/resumeTimePage") {
            return false;
        }
        // 点击开标结束判断文件上传评标完成情况
        if ($(e).attr("data-flowname") == "bidOpenEnd" && isAllBidderUpload() == 1) {
            $(e).prev().addClass("complete");
        }
        var $left_li = $(".left ul li");
        if (!($(e).prev().hasClass("complete")) && !$(e).is($left_li.eq(0))) {
            layerWarning("当前环节还未开始!");
            return;
        }
        // 添加load
        layerLoading("数据加载中...", 16, 1);
        setTimeout(function () {
            $(".left ul li").each(function (index) {
                if ($(this).is($(e))) {
                    localStorage.setItem("now_flow_${bidSection.id}", index);
                    listBidOpenProcessComplete();
                }
            });

            var iframe = $(".load_url_content iframe")[0];
            iframe.src = targetUrl + encodeURI("?id=${bidSection.id}");
            if (iframe.attachEvent) {
                iframe.attachEvent("onload", function () {
                    window.top.layer.closeAll();
                    //$(".now_flow").html($(e).children(".left_menu").html())
                });
            } else {
                iframe.onload = function () {
                    // window.top.layer.closeAll();
                    //$(".now_flow").html($(e).children(".left_menu").html())
                };
            }
        }, 400)
    }

    $(window).on("load", function () {
        //加载消息盒子
        showMinOnlinePage();
    })

    // 全局加载layui组件，无需在子页面单独引入
    var form, layer, laytpl, laypage, element;
    $(function () {
        // getMessageCount();
        listBidOpenProcessComplete();
        setInterval(showtime, 1000);
        layui.use(['form', 'layer', 'element', 'laytpl', 'laypage', "laydate"], function () {
            form = layui.form;
            element = layui.element;
            laypage = layui.laypage;
            laytpl = layui.laytpl;

            //监听提交
            form.on('submit(formSub)', function (data) {
                updateTenderDoc();
                // 阻止表单提交
                return false;
            });

            layui.laydate.render({
                elem: "#checkTime",
                type: "time",
            });

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
                localStorage.setItem("countDownTime_${bidSection.id}", countDownTime);
                startCountDown();
            });
            $("#onConcel").click(function () {
                layer.closeAll();
                $("#setTime").css("display", "none");
            });
            $("#onReset").click(function () {
                // 移除倒计时缓存
                localStorage.removeItem("countDownTime_${bidSection.id}");
                $("#checkTime").val("");
                layer.msg('重置成功！', {icon: 1});
            });
            $("#onClose").click(function () {
                layer.closeAll();
                $("#setTime").css("display", "none");
            });
        });
        var $left_ul_li = $(".left ul li");
        if (bidOpenStatus === "2" || !isNull(bidOpenEndTime)) {
            $left_ul_li.eq($left_ul_li.length - 1).trigger("click");
            // 开标结束，禁用非复会环节的所有页面跳转
            $left_ul_li.each(function (index) {
                if (index !== ($left_ul_li.length - 1)) {
                    $(this).addClass("disable-click");
                    $(this).removeAttr("onclick");
                    $(this).css("cursor", "not-allowed");
                }
            });
        } else {
            if (localStorage.getItem("now_flow_${bidSection.id}") != null) {
                $left_ul_li.eq(localStorage.getItem("now_flow_${bidSection.id}")).trigger("click");
            } else {
                $left_ul_li.eq(0).trigger("click");
            }
        }

        // 开启倒计时绑定点击事件
        $("#openTime").click(function () {
            if (!isNull(localStorage.getItem("countDownTime_${bidSection.id}"))) {
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

        });
    })

    /**
     * 开启倒计时
     */
    function startCountDown() {
       clearInterval(countDownInterval);
       countDownInterval = setInterval(function () {
            var countDownTime = parseInt(localStorage.getItem("countDownTime_${bidSection.id}"));
            if (countDownTime > 0) {
                $(".set-time-box").css("display", "none");
                $(".show-time-box").css("display", "block");
                $("#setTimeBtn").css("display", "none");
                $("#showTimeBtn").css("display", "block");
                --countDownTime;
                localStorage.setItem("countDownTime_${bidSection.id}", countDownTime);
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
                localStorage.removeItem("countDownTime_${bidSection.id}");
                clearInterval(countDownInterval);
                $(".set-time-box").css("display", "block");
                $(".show-time-box").css("display", "none");
                $("#setTimeBtn").css("display", "block");
                $("#showTimeBtn").css("display", "none");
                // layer.closeAll();
            }
        }, 1000);
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
     * 数据上传评标是否结束
     */
    function isAllBidderUpload() {
        var upFlag = 0;
        $.ajax({
            type: "post",
            url: "${ctx}/staff/isBidderFileUploadAll",
            cache: false,
            async: false,//必须用同步
            success: function (data) {
                upFlag = data;
            }
        });
        return 1;
    }

    /**
     * 获取开标进行环节的完成情况
     */
    function listBidOpenProcessComplete() {
        $.ajax({
            type: "post",
            url: "${ctx}/staff/listBidOpenProcessComplete",
            cache: false,
            async: false,//必须用同步
            data: {
                "id": "${bidSection.id}"
            },
            success: function (data) {
                updateNowFlowStep(data);
            }
        });
    }

    /**
     * 更新开标流程左侧导航
     * @param data 完成的流程
     */
    function updateNowFlowStep(data) {
        var $left_ul = $(".left ul li");
        // 初始状态
        $left_ul.removeClass("blue");
        $left_ul.find("i").removeClass("nowa");
        var flowNameArr = [];
        $left_ul.each(function (index) {
            var img = $(this).data('img');
            $(this).find("img").attr("src", "${ctx}/img/" + img + "_gray.png");
            flowNameArr.push($(this).attr("data-flowname"));
        });

        for (var i = 0; i < flowNameArr.length; i++) {
            var questioning = '${lineStatus.questionStatus}' === '1';
            // 资格预审，如果解密环节结束，判断质询状态
            if (!isNull('${isQualification}') && data[i] === 'bidderFileDecrypt' && '${lineStatus.questionStatus}' !== '2') {
                $(".now_flow").html(questioning ? '正在质询' : '质询未开启');
                break;
            }

            // 非资格预审，如果唱标环节结束，判断质询状态
            if (isNull('${isQualification}') && data[i] === 'fileCursor' && '${lineStatus.questionStatus}' !== '2') {
                $(".now_flow").html(questioning ? '正在质询' : '质询未开启');
                break;
            }

            if (data.length >= i && data[i] !== flowNameArr[i]) {
                // 加载当前进行环节
                $(".now_flow").html($left_ul.eq(i).children(".left_menu").html());
                <#--localStorage.setItem("now_flow_${bidSection.id}", i);-->
                break;
            }
            var $cur_li = $(".left ul li[data-flowname='" + data[i] + "']");
            var img = $cur_li.data("img");
            $cur_li.addClass("blue complete");
            $cur_li.find("img").attr("src", "${ctx}/img/" + img + ".png");
        }

        // 点击左侧流程导航时选择当前
        var $li;
        if (localStorage.getItem("now_flow_${bidSection.id}") != null) {
            $li = $left_ul.eq(localStorage.getItem("now_flow_${bidSection.id}"));
        } else {
            if (bidOpenStatus === "2") {
                $li = $left_ul.eq($(".left ul li").length - 1);
            } else {
                $li = $left_ul.eq(0);
            }
        }
        var img = $li.data("img");
        $li.addClass("blue");
        $li.find("img").attr("src", "${ctx}/img/" + img + ".png");
        $li.find("i").removeClass("nowa").addClass("nowa");
    }


    /**
     * 点击生成pdf输出到页面
     */
    function outBidOpenRecord() {
        layerLoading("开标记录表生成中，请稍等...");
        $.ajax({
            url: '${ctx}/staff/createRecordTable',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId: ${bidSection.id},
                bidOpenPlace: $("#bidOpenPlace").val()
            },
            success: function (data) {
                if (data) {
                    layer.msg('生成成功！', {icon: 1});
                    setTimeout(function () {
                        parent.window.location.reload();
                    }, 1500);
                } else {
                    layer.msg("生成失败！", {icon: 5});
                }
                // window.top.layer.closeAll();

                // 加载消息盒子
                showMinOnlinePage();
            },
            error: function (data) {
                layer.msg("生成失败！", {icon: 5});
                console.error(data);
                // window.top.layer.closeAll();

                // 加载消息盒子
                showMinOnlinePage();
            },
        });
    }

    /**
     * 代理机构查看消息盒子
     */
    function messageBox(id) {
        $('#showPdfView').hide();
        window.top.layer.open({
            type: 2,
            title: '消息查看',
            area: ['50%', '100%'],
            btn: ['发送'],
            offset: 'r',
            content: '${ctx}/messageBox/viewAllMessagePage?bidSectionId=' + id,
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.userSendMsg("2");
            },
            end: function () {
                $('#showPdfView').show();
            }
        });
    }

    /**
     * 获取未读消息的条数
     */
    function getMessageCount() {
        $.ajax({
            type: "POST",
            url: "${ctx}/messageBox/getUnReadCount",
            cache: false,
            data: {
                "userId": '${user.userId}',
                "bidSectionId": ${bidSection.id},
                "userType": 2,
                "readSituation": 0
            },
            success: function (data) {
                $("#message_count").text(data)
            }
        });
    }


    /**
     * 更新网上开标状态
     * @param data 修改的数据
     * @param msg 提示消息
     * @param targetUrl 跳转到指定的URL
     * @param thisId 跳转到指定的li,格式：${'#fileCursorLi'}
     * @param overQuestion 是否结束质询 1是
     */
    function updateLineStatus(data, msg, targetUrl, thisId, overQuestion) {
        $.ajax({
            url: "${ctx}/staff/updateLineStatus",
            type: "POST",
            cache: false,
            data: data,
            success: function (data) {
                if (data) {
                    if (overQuestion == 1) {
                        // 结束质询 禁言所有投标单位
                        postMessageFun({clickJinyanBtn: true});
                    }
                    // 推送信息到消息盒子
                    postMessageFun({linkType: msg});
                    //goToUrl(targetUrl, thisId)
                    doLoading(msg, 1, 2, null, null, function () {
                        parent.window.location.reload();
                    });
                    return false;
                }
            },
            error: function (e) {
                layer.msg('操作失败', {icon: 5});
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }


    /**
     * 切换公布投标人检查状态
     * 切换页面，推送系统消息到消息盒子
     */
    function switchPublishBidderStatus(val) {
        var msg = val === 1 ? "公布投标人名单环节已开启" : "公布投标人名单环节已结束";
        updateLineStatus({
            "bidSectionId": '${bidSection.id}',
            "publishBidderStatus": val,
            "msg": msg
        }, msg, '${ctx}/staff/publishBidderPage/2', $("#publishLi"));
        // 添加公告
        postMessageFun({linkType: msg});
    }

    /**
     * 删除唱标语音
     */
    function removeSingingVioce() {
        $.ajax({
            url: '${ctx}/staff/removeSingingVoice',
            type: 'post',
            cache: false,
            data: {
                id: ${bidSection.id}
            },
            success: function (data) {
                if (!isNull(data)) {
                    if (data) {
                        layer.msg("操作成功！", {icon: 1});
                    }
                }
            }
        });
    }

    /**
     * 判断项目是否招标失败
     * @param e 当前对象
     * @param successCallback 成功回调
     * @param errorCallBack 失败回调
     */
    function judgmentBidSectionCancel(e, successCallback, errorCallBack) {
        $.ajax({
            url: "${ctx}/staff/validBidderCount",
            type: "POST",
            cache: false,
            success: function (data) {
                if (successCallback && typeof (successCallback) == "function") {
                    var bidSection = data.bidSection;
                    if (!isNull(bidSection.cancelStatus) && bidSection.cancelStatus == 2) {
                        successCallback(false, "", "");
                    } else if (!isNull(bidSection.cancelStatus) && bidSection.cancelStatus == 1) {
                        successCallback(data.status, data.bidderSize, "投标人不足三家，项目已招标失败！");
                    } else {
                        successCallback(data.status, "", "");
                    }
                }
            },
            error: function (e) {
                console.error("投标人不足三家检测失败:" + e)
                if (errorCallBack && typeof (errorCallBack) == "function") {
                    errorCallBack();
                }
            }
        });
    }

    /**
     * 选择流标进行状态
     */
    function chooseCancelBidSection(msg) {
        layer.confirm("经检测，当前投标人不足三家，项目将招标失败，请点击确认后前往下载开标记录", {
            icon: 3,
            title: '提示',
            btn: ['确认']
        }, function (index) {
            layer.close(index);
            if (isNull(msg)) {
                $.ajax({
                    url: "${ctx}/staff/cancelBidSection",
                    type: "POST",
                    data: {
                        "cancelStatus": 1,
                        "cancelReason": "投标人不足三家，项目招标失败！"
                    },
                    success: function (data) {
                        if (data) {
                            window.top.layer.open({
                                title: "开标记录表",
                                type: 2,
                                scrollbar: false,
                                move: false,
                                skin: "project-liubiao",
                                area: ["1200px", "88vh"],
                                content: "${ctx}/staff/cancelBidReportPage/1",
                            });
                        } else {
                            layerAlert("操作失败!", null, null, 2);
                        }
                    }
                })
            } else {
                window.top.layer.open({
                    title: "开标记录表",
                    type: 2,
                    scrollbar: false,
                    move: false,
                    skin: "project-liubiao",
                    area: ["1200px", "88vh"],
                    content: "${ctx}/staff/cancelBidReportPage/1",
                });
            }
        });
    }
</script>
</body>

</html>