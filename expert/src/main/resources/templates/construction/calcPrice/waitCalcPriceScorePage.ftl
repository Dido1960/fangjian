<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
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
    <style>
        html,
        body {
            background: url(${ctx}/img/back.png) no-repeat center center rgba(16, 113, 210, 1) !important;
            background-size: 100% 100% !important;
        }
        .cont-l {
            height: calc(100% - 220px);
        }

        .cont-r {
            height: 100%;
        }
        .cont {
            height: calc(100% - 100px);
            margin-top: 80px;
        }

        .btns {
            width: 240px;
            height: 36px;
            margin: 0 auto;
        }
        .btns-long{
            width: 380px;
            height: 36px;
            margin: 0 auto;
        }
        .btn {
            width: 110px;
            float: left;
        }
        .btn + .btn {
            margin-left: 20px;
        }
        .btns .btn:nth-child(2),
        .btns-long .btn:nth-child(2){
            background: #ffffff;
            color: #0066cc;
        }
    </style>
</head>
<body>
<#include "${ctx}/include/header.ftl">
<section>
    <div class="cont" >
        <div class="cont-l" style="position: relative; height: 100%; padding-top: 1%">
            <p>当前时间</p>
            <p class="current-time">2020年06月20日 14:00:00</p>
            <img src="${ctx}/img/wait_left.png" style="position: absolute;left: 0;top: 0;bottom: 0;right: 0; margin: auto; display: block;width: 80%; height: 60%;" alt="">
        </div>
        <div class="cont-r">
            <div class="title">
                <div class="tit-l">
                    <img src="${ctx}/img/right_1.png" alt="">
                </div>
                <div class="tit-r">
                    <h3>标段名称</h3>
                    <p>${bidSection.bidSectionName!}</p>
                </div>
            </div>
            <div class="title">
                <div class="tit-l" style="">
                    <img src="${ctx}/img/right_2.png" alt="">
                </div>
                <div class="tit-r">
                    <h3>标段编号</h3>
                    <p>${bidSection.bidSectionCode!}</p>
                </div>
            </div>
            <div class="group">
                评标组长<span>${leaderExpertUser.expertName!}</span>
            </div>
            <#if expert.isChairman == '1'>
                <div class="btns" style="width: 380px;">
                    <#if bidSectionRelate.calcPriceUid?? && bidSectionRelate.calcPriceUid != "">
                        <div class="btn" style="margin-top: 140px; width: 140px">正在计算报价得分</div>
                    <#else>
                        <div class="btn" style="margin-top: 140px; width: 106px" onclick="calcPriceScore()">报价分计算</div>
                    </#if>
                    <div class="btn" onclick="returnUpFlow()" style="margin-top: 140px;margin-left: 20px;">返回评标主页</div>
                </div>
            <#else>
                <div class="btns-long">
                    <div class="btn" style="margin-top: 140px; width: 220px; background: #BDCBD9;">请等待专家组长开启报价分计算</div>
                    <div class="btn" onclick="returnUpFlow()" style="margin-top: 140px;">返回评标主页</div>
                </div>
            </#if>
        </div>
    </div>
</section>
</body>

<script>
    var calc_price_score_index;
    var progress_bar;
    $(function () {
        setInterval(showtime, 1000);
        if ("${bidSectionRelate.calcPriceUid?? && bidSectionRelate.calcPriceUid != ''}") {
            calc_price_score_index = layer.msg("报价得分计算中,计算状态：已完成<span id='calc-process' class='red-f'>0%</span>", {icon: 16, time: 0, shade: 0.3});
            progress_bar = setInterval(validPriceScore, 1000);
        }
    });

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
     * 计算报价得分
     */
    function calcPriceScore() {
        calc_price_score_index = layer.msg("报价得分计算中,计算状态：已完成<span id='calc-process' class='red-f'>0%</span>", {icon: 16, time: 0, shade: 0.3});
        $.ajax({
            url: "${ctx}/expert/conBidEval/calcPriceScore",
            type: "POST",
            cache: false,
            success: function (data) {
                progress_bar = setInterval(validPriceScore, 1000);
            },
            error: function () {
                layer.close(calc_price_score_index);
                layerAlertAndEnd("报价得分计算失败！", null, null, function () {
                    removePriceUid();
                }, 2);
            }
        });
    }

    /**
     * 校验报价得分计算完成情况
     */
    var valid_price_score_num = 0;
    function validPriceScore() {
        if (valid_price_score_num !== 0) {
            return
        }
        valid_price_score_num++;
        $.ajax({
            url: "${ctx}/expert/conBidEval/validPriceScore",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code == "1") {
                    clearInterval(progress_bar);
                    layer.close(calc_price_score_index);
                    layer.msg(data.msg, {icon: 1, time: 3000, end: function () {
                            window.location.reload();
                        }});
                } else if (data.code == "-1" || data.code == "0") {
                    clearInterval(progress_bar);
                    layer.close(calc_price_score_index);
                    layerAlertAndEnd(data.msg, null, null, function () {
                        removePriceUid();
                    }, 2);
                } else if (data.code == "2") {
                    $("#calc-process").html(data.data + "%")
                }
                valid_price_score_num--;
            },
            error: function () {
                layer.close(calc_price_score_index);
                clearInterval(progress_bar);
                layerAlertAndEnd("报价得分计算失败！", null, null, function () {
                    removePriceUid();
                }, 2);
            }
        });
    }
    
    function removePriceUid() {
        $.ajax({
            url: "${ctx}/expert/conBidEval/removePriceUid",
            type: "POST",
            cache: false,
            success: function (data) {

            },
            error: function () {

            }
        });
    }
</script>
</html>