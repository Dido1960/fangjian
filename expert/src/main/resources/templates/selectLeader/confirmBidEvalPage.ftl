<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>开始评标</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/confirmBidEval.css">

</head>
<style>
    html, body {
        background: url(${ctx}/img/back.png) no-repeat center center rgba(16, 113, 210, 1) !important;
        background-size: 100% 100% !important;
    }

    .cont-r {
    <#if expert.isChairman == '1' && bidSection.bidClassifyCode != "A08"> padding-top: 150px;
    </#if>
    }
</style>
<body>
<header>
    <div class="text">
        <div class="name">
            <img src="${ctx}/img/logo_blue.png" alt="">
            甘肃省房建市政电子辅助评标系统
        </div>
        <div class="bao">
            <#if expert.isChairman == 1>
                <div class="off blove-f">专家组长</div>
            <#else >
                <div class="off blove-f">专家组员</div>
            </#if>
            <div class="try" onclick="exitSystem()">
                <b class="username" title="${expert.name}">${expert.name}</b>
                <i></i>
            </div>
        </div>
    </div>
</header>
<section>
    <div class="cont">
        <div class="cont-l">
            <p>当前时间</p>
            <p class="current-time">2020年06月20日 14:00:00</p>
            <img src="${ctx}/img/left_back1.png" alt="">
        </div>
        <div class="cont-r">
            <div class="title">
                <div class="tit-l">
                    <img src="${ctx}/img/right_1.png" alt="">
                </div>
                <div class="tit-r">
                    <h3>标段名称</h3>
                    <p title="${bidSection.bidSectionName!}">${bidSection.bidSectionName!}</p>
                </div>
            </div>
            <div class="title">
                <div class="tit-l">
                    <img src="${ctx}/img/right_2.png" alt="">
                </div>
                <div class="tit-r">
                    <h3>标段编号</h3>
                    <p title="${bidSection.bidSectionCode!}">${bidSection.bidSectionCode!}</p>
                </div>
            </div>
            <div class="group">
                评标组长<span>${leaderExpertUser.expertName!}</span>
            </div>
            <#if expert.isChairman == '1'>
                <#if bidSection.bidClassifyCode == "A08">
                    <div class="radio">
                        <form action="javascript:void(0)" class="layui-form">
                            <div class="layui-input-block">
                                <input type="checkbox" name="structureStatus" value="${tenderDoc.structureStatus}"
                                        <#if !tenderDoc.structureStatus?? || tenderDoc.structureStatus == 1>
                                            checked
                                        </#if>
                                        <#if bidSection.evalReviewStatus?? && bidSection.evalReviewStatus != 0>
                                            disabled
                                        </#if>
                                       title="错漏项分析" lay-skin="primary" lay-filter="structureStatus">
                            </div>
                            <div class="layui-input-block">
                                <input type="checkbox" name="priceStatus" value="${tenderDoc.priceStatus}"
                                        <#if tenderDoc.priceStatus?? && tenderDoc.priceStatus == 1>
                                            checked
                                        </#if>
                                        <#if bidSection.evalReviewStatus?? && bidSection.evalReviewStatus != 0>
                                            disabled="disabled"
                                        </#if>
                                       title="零负报价分析" lay-skin="primary" lay-filter="priceStatus">
                            </div>
                            <div class="layui-input-block">
                                <input type="checkbox" name="fundBasisStatus" value="${tenderDoc.fundBasisStatus}"
                                        <#if tenderDoc.fundBasisStatus?? && tenderDoc.fundBasisStatus == 1>
                                            checked
                                        </#if>
                                        <#if bidSection.evalReviewStatus?? && bidSection.evalReviewStatus != 0>
                                            disabled="disabled"
                                        </#if>
                                       title="取费基础分析" lay-skin="primary" lay-filter="fundBasisStatus">
                            </div>
                            <#--<div class="layui-input-block">
                                <input type="checkbox" name="mutualSecurityStatus"
                                       value="${tenderDoc.mutualSecurityStatus}"
                                        <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                                            checked
                                        </#if>
                                        <#if bidSection.evalReviewStatus?? && bidSection.evalReviewStatus != 0>
                                            disabled="disabled"
                                        </#if>
                                       title="互保共健" lay-skin="primary" lay-filter="mutualSecurityStatus">
                            </div>-->
                        </form>
                    </div>
                </#if>
            <#else>
                <div class="radio">
                    <div class="layui-input-block evalText" style="text-align: center; width: 100%; font-size: 20px;"
                         data-status="${bidSection.evalReviewStatus}">
                        <#if bidSection.evalReviewStatus == 0>
                            评标尚未开始，请等待...
                        <#elseif bidSection.evalReviewStatus == 1>
                            开始评标申请中，请等待审核
                        <#elseif bidSection.evalReviewStatus == 2>
                            <#if (!bidSection.evalStatus?? || bidSection.evalStatus == 0) && bidSection.bidClassifyCode == 'A08'>
                                系统清标中，请等待...
                            <#else>
                                评标已开始
                            </#if>
                        </#if>
                    </div>
                </div>
            </#if>
            <#if expert.isChairman == '1'>
                <#if bidSection.evalReviewStatus == 0>
                    <div class="btns">
                        <div class="btn" style="margin-top: 40px; width: 106px" onclick="sendStartRequest()">开始评标</div>
                    </div>
                <#elseif bidSection.evalReviewStatus == 1>
                    <div class="btns">
                        <div class="btn" style="margin-top: 40px; width: 106px">请等待审核</div>
                    </div>
                <#else>
                    <#if (!bidSection.evalStatus?? || bidSection.evalStatus == 0) && bidSection.bidClassifyCode == 'A08'>
                    <#else>
                        <div class="btns">
                            <div class="btn" style="margin-top: 40px; width: 106px">评标已开始</div>
                        </div>
                    </#if>
                </#if>
            </#if>
        </div>
    </div>
</section>
</body>

<script>
    var structureStatus = "${(!tenderDoc.structureStatus?? || tenderDoc.structureStatus == 1)}" ? "1" : "0";
    var priceStatus = "${(tenderDoc.priceStatus?? && tenderDoc.priceStatus == 1)}" ? "1" : "0";
    var fundBasisStatus = "${(tenderDoc.fundBasisStatus?? && tenderDoc.fundBasisStatus == 1)}" ? "1" : "0";
    /*var mutualSecurityStatus = "${(tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1)}" ? "1" : "0";*/
    var getEvalStateCount = 0;
    var getGovAgreeCount = 0;
    var agreeInterval = null;
    var eval_state_interval = null;
    layui.use('form', function () {
        var form = layui.form;
        form.on('checkbox(structureStatus)', function () {
            structureStatus = this.checked ? "1" : "0";
        });

        form.on('checkbox(priceStatus)', function () {
            priceStatus = this.checked ? "1" : "0";
        });

        form.on('checkbox(fundBasisStatus)', function () {
            fundBasisStatus = this.checked ? "1" : "0";
        });

        /*form.on('checkbox(mutualSecurityStatus)', function () {
            mutualSecurityStatus = this.checked ? "1" : "0";
        });*/
        form.on('submit', function (data) {
            //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });
        form.render();
    });
    $(function () {
        setInterval(showtime, 1000);
        eval_state_interval = setInterval(getEvalState, 1000);

        if ('${expert.isChairman}' == 1) {
            checkStartEval();
        }
    });

    /**
     * 检查开始评标按钮事件
     */
    function checkStartEval() {
        if ('${bidSection.evalReviewStatus}' == 1) {
            agreeInterval = setInterval(getGovAgree, 1000);
        } else if ('${bidSection.evalReviewStatus}' == 2 && '${bidSection.evalStatus}' == 0) {
            startEval();
        }
    }

    /**
     * 获取监管是否同意
     */
    function getGovAgree() {
        if(getGovAgreeCount!=0){
            return false;
        }
        getEvalStateCount++;

        $.ajax({
            url: "${ctx}/expert/getBidSectionById",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.evalReviewStatus == 2) {
                    if (!isNull(agreeInterval)) {
                        clearInterval(agreeInterval)
                    }
                    startEval();
                }
                getEvalStateCount--;
            },
            error: function (e) {
                console.error(e);
                getEvalStateCount--;
            }
        });
    }

    /**
     * 发送开始评标请求
     */
    function sendStartRequest() {
        var msg = layer.msg("加载中，请等待...", {icon: 16, time: 0, shade: 0.3});
        var data = {};
        if ('${bidSection.bidClassifyCode}' == 'A08') {
            data.id = '${tenderDoc.id }';
            data.bidSectionId = '${bidSection.id }';
            data.structureStatus = structureStatus;
            data.priceStatus = priceStatus;
            data.fundBasisStatus = fundBasisStatus;
            /*data.mutualSecurityStatus = mutualSecurityStatus;*/
        } else {
            data.id = '${tenderDoc.id }';
            data.bidSectionId = '${bidSection.id }';
        }
        $.ajax({
            url: "${ctx}/expert/sendStartRequest",
            type: "POST",
            cache: false,
            data: data,
            success: function (data) {
                layer.close(msg);
                if (data) {
                    $("input[type='checkbox']").prop("disabled", "disabled");
                    layui.form.render();
                    $(".btns").children(":first").text("请等待审核");
                    $(".btns").children(":first").removeAttr("onclick");
                    agreeInterval = setInterval(getGovAgree, 1000);
                }
            },
            error: function (e) {
                layer.close(msg);
                console.error(e);
            }
        });
    }

    /**
     * 展示当前时间
     */
    function showtime() {
        var nowtime = new Date();
        // 年 月 日
        var year = timeAdd0(nowtime.getFullYear().toString());
        var month = timeAdd0((nowtime.getMonth() + 1).toString());
        var day = timeAdd0(nowtime.getDate().toString());
        var date = year + "年" + month + "月" + day + "日" + " ";
        // 时 分 秒
        var hours = timeAdd0(nowtime.getHours().toString());
        var min = timeAdd0(nowtime.getMinutes().toString());
        var sen = timeAdd0(nowtime.getSeconds().toString());
        var time = hours + ":" + min + ":" + sen;

        $(".current-time").text(date + time);
    }

    /**
     * 组长确认开始评标
     */
    function startEval() {
        if ('${bidSection.bidClassifyCode}' == 'A08') {
            $.ajax({
                url: "${ctx}/expert/updateStartEvalReviewFlag",
                type: "POST",
                data: {
                    "id": '${tenderDoc.id }'
                },
                cache: false,
                success: function (reslut) {
                    window.location.href = "${ctx}/expert/startEval";
                }
            });
        } else {
            window.location.href = "${ctx}/expert/startEval";
        }
    }


    /**
     * 获取评标状态
     */
    function getEvalState() {
        if (getEvalStateCount != 0) {
            return false;
        }
        getEvalStateCount++;
        var $textDiv = null;
        if ('${expert.isChairman}' != 1) {
            $textDiv = $(".evalText");
        }
        $.ajax({
            url: '${ctx}/expert/getBidSectionById',
            type: 'post',
            cache: false,
            success: function (data) {
                if (data.evalStatus == "1") {
                    window.top.location.href = "${ctx}/expert/startEval";
                    return;
                }
                if ('${expert.isChairman}' == 1 && data.evalReviewStatus == 2) {
                    clearInterval(eval_state_interval);
                }
                if (!isNull($textDiv)) {
                    if (data.evalReviewStatus == 1 && $textDiv.attr('data-status') == 0) {
                        $textDiv.text("开始评标申请中，请等待审核");
                        $textDiv.attr('data-status', 1);
                    }
                    if (data.evalReviewStatus == 2 && '${bidSection.bidClassifyCode}' == 'A08') {
                        $(".btns").empty();
                        $(".btns").html("<div class=\"btn\" style=\"margin-top: 40px; width: 106px;background: rgba(223, 152, 9, 1);\" onclick=\"clearBidProcess()\">清标进度</div>");
                        $textDiv.text("系统清标中，请等待...");
                    }
                }

                getEvalStateCount--;
            }
        });
    }

    /**
     * 校验清标是否完成
     * @param callback
     */
    var validClearBidCompleteNum = 0;

    function validClearBidComplete(callback) {
        var interval = setInterval(function () {
            if (validClearBidCompleteNum != 0) {
                return;
            }
            validClearBidCompleteNum++;
            $.ajax({
                url: '${ctx}/expert/validClearBidComplete',
                type: 'post',
                cache: false,
                success: function (data) {
                    if (data) {
                        clearInterval(interval);
                        if (callback && typeof (callback) == "function") {
                            callback();
                        }
                    } else {
                        $(".btns").empty();
                        $(".btns").css("width", "250px")
                        $(".btns").html("<div class=\"btn\" style=\"margin-top: 40px; width: 106px\">请等待清标</div>\n" +
                            "                            <div class=\"btn\" style=\"margin-top: 40px; width: 106px\" onclick=\"clearBidProcess()\">清标进度</div>");
                    }
                    validClearBidCompleteNum--;
                }
            });
        }, 5000);
    }

    /**
     * 查看清标进度
     */
    function clearBidProcess() {
        window.layer.open({
            type: 2,
            offset: 'c',
            title: '清标进度',
            shadeClose: false,
            move: false,
            resize: false,
            area: ['80%', '80%'],
            btn: ['关闭'],
            content: '${ctx}/expert/conBidEval/clearBidProcess',
            btn1: function (index) {
                window.layer.close(index);
            }
        });
    }
</script>
</html>