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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/evalIndex.css">
    <style>
        .cont .document .choice {
        <#if bidSection.bidClassifyCode == "A12"> width: 1600px !important;
        <#elseif bidSection.bidClassifyCode == "A08"><#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1> width: 1600px !important;
        <#else> width: 1260px !important;
        </#if><#else> width: 920px !important;
        </#if>
        }
    </style>
</head>
<body>
<#include "${ctx}/include/header.ftl">
<section>
    <div class="title">
        <p>标段名称：<span>${bidSection.bidSectionName}</span></p>
        <p>标段编号：<span>${bidSection.bidSectionCode}</span></p>
        <p class="current-time" style="display: none"></p>
    </div>
    <div class="cont">
        <#if bidSection.bidClassifyCode == "A08">
            <div class="cont-btn" onclick="goToClearUrl('${bidSection.id}')"></div>
        </#if>
        <div class="document">
            <ul class="choice">
                <#if bidSection.bidClassifyCode == "A12">
                    <li data-evalflow="5" onclick="qualificationReview('${qualification}', this)"
                        <#if !(qualification?? && qualification)>class="disabledClass"</#if>>
                        <img src="${ctx}/img/start_1.png" alt="">
                        <div class="choice-btn">资格审查</div>
                    </li>
                </#if>

                <li data-evalflow="1" onclick="preliminaryReview('${preliminary}', this)"
                    <#if !(preliminary?? && preliminary)>class="disabledClass"</#if>>
                    <img src="${ctx}/img/start_2.png" alt="">
                    <div class="choice-btn">初步评审</div>
                </li>

                <li data-evalflow="2" onclick="detailedReview('${detail}', this)"
                    <#if !(detail?? && detail)>class="disabledClass"</#if>>
                    <img src="${ctx}/img/start_3.png" alt="">
                    <div class="choice-btn">详细评审</div>
                </li>

                <#if bidSection.bidClassifyCode == "A12" ||  bidSection.bidClassifyCode == "A08">
                    <li data-evalflow="6" onclick="priceReview('${price}','${bidSection.id}', this)"
                        <#if !(price?? && price)>class="disabledClass"</#if>>
                        <img src="${ctx}/img/start_4.png" alt="">
                        <div class="choice-btn">报价得分</div>
                    </li>
                </#if>

                <#if bidSection.bidClassifyCode == "A08" && tenderDoc.mutualSecurityStatus == 1>
                    <li data-evalflow="4" onclick="otherReview('${other}', this)"
                        <#if !(other?? && other)>class="disabledClass"</#if>>
                        <img src="${ctx}/img/start_6.png" alt="">
                        <div class="choice-btn">其他评审</div>
                    </li>
                </#if>

                <li data-evalflow="3" onclick="assessmentResult('${result}', this)"
                    <#if !(result?? && result)>class="disabledClass"</#if>>
                    <img src="${ctx}/img/start_5.png" alt="">
                    <div class="choice-btn">评审结果</div>
                </li>
            </ul>
        </div>
    </div>
</section>

<script>
    $(function () {
        setInterval(showtime, 1000);
    });

    /**
     * 展示当前时间
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
        var time = year + "年" + month + "月" + day + "日" + " " + week + " " + hours + ":" + min + ":" + sen;
        $(".current-time").text(time);
    }


    /**
     * 资格审查
     * @param flag 当前环节是否完成
     * @param e 当前对象
     */
    function qualificationReview(flag, e) {
        if (isNull(flag) || !flag) {
            layer.msg("开标尚未结束,不能进行评标！", {time: 3000, icon: 2});
            return;
        }
        doLoading();
        judgmentBidSectionCancel(e, function (data, bidderSize, msg) {
            if (!data) {
                if (bidderSize < 3 && !isNull(msg)) {
                    loadComplete();
                    chooseCancelBidSection('${expert.isChairman}', msg);
                } else {
                window.location.href = "${ctx}/expert/epcBidEval/qualifyReviewPage";
                }
            } else {
                loadComplete();
                chooseCancelBidSection('${expert.isChairman}', msg);
            }
        })
    }

    /**
     * 初步评审
     * @param flag 当前环节是否完成
     * @param e 当前对象
     */
    function preliminaryReview(flag, e) {
        var msg;
        if ('${bidSection.bidClassifyCode}' == 'A12') {
            msg = "当前环节还未开始！";
        } else {
            msg = "开标尚未结束,不能进行评标！";
        }
        if (isNull(flag) || !flag) {
            layer.msg(msg, {time: 3000, icon: 2});
            return;
        }
        doLoading();
        judgmentBidSectionCancel(e, function (data, bidderSize, msg) {
            if (!data) {
                if (bidderSize < 3 && !isNull(msg)) {
                    loadComplete();
                    chooseCancelBidSection('${expert.isChairman}', msg);
                } else {
                window.location.href = "${ctx}/expert/evalPlan/preliminaryReview";
                }
            } else {
                loadComplete();
                chooseCancelBidSection('${expert.isChairman}', msg);
            }
        })
    }

    /**
     * 详细评审
     * @param flag 当前环节是否完成
     * @param e 当前对象
     */
    function detailedReview(flag, e) {
        if (isNull(flag) || !flag) {
            layer.msg("当前环节还未开始！", {time: 3000, icon: 2});
            return;
        }
        doLoading();
        judgmentBidSectionCancel(e, function (data, bidderSize, msg) {
            if (!data) {
                if (bidderSize < 3 && !isNull(msg)) {
                    loadComplete();
                    chooseCancelBidSection('${expert.isChairman}', msg);
                } else {
                window.location.href = "${ctx}/expert/evalPlan/detailedReview";
                }
            } else {
                loadComplete();
                chooseCancelBidSection('${expert.isChairman}', msg);
            }
        })
    }

    /**
     * 报价得分计算
     * @param flag 当前环节是否完成
     * @param e 当前对象
     */
    function priceReview(flag,bId, e) {
        if (isNull(flag) || !flag) {
            layer.msg("当前环节还未开始！", {time: 3000, icon: 2});
            return;
        }
        doLoading();
        judgmentBidSectionCancel(e, function (data, bidderSize, msg) {
            if (!data) {
                if (bidderSize < 3 && !isNull(msg)) {
                    loadComplete();
                    chooseCancelBidSection('${expert.isChairman}', msg);
                } else {
                    if ("${bidSection.bidClassifyCode}" === 'A12') {
                        window.location.href = "${ctx}/expert/evalPlan/priceScoreReview";
                    } else if ("${bidSection.bidClassifyCode}" === 'A08') {
                        <!--  跳转至清标3.0 -> 报价得分页面 -->
                        window.location.href = "${ctx}/clearBidV3/toPriceScorePage?bId="+bId+"&backPage=" + encodeURIComponent(window.location.href);
                    }
                }
            } else {
                loadComplete();
                chooseCancelBidSection('${expert.isChairman}', msg);
            }
        })
    }

    /**
     * 其他评审
     * @param flag 当前环节是否完成
     * @param e 当前对象
     */
    function otherReview(flag, e) {
        if (isNull(flag) || !flag) {
            layer.msg("当前环节还未开始！", {time: 3000, icon: 2});
            return;
        }
        doLoading();
        judgmentBidSectionCancel(e, function (data, bidderSize, msg) {
            if (!data) {
                if (bidderSize < 3 && !isNull(msg)) {
                    loadComplete();
                    chooseCancelBidSection('${expert.isChairman}', msg);
                } else {
                window.location.href = "${ctx}/expert/evalPlan/otherReview";
                }
            } else {
                loadComplete();
                chooseCancelBidSection('${expert.isChairman}', msg);
            }
        })
    }

    /**
     * 评审结果
     * @param flag 当前环节是否完成
     * @param e 当前对象
     */
    function assessmentResult(flag, e) {
        if ('${bidSection.cancelStatus}' == '1') {
            window.location.href = "${ctx}/expert/evalPlan/endBasePage";
        } else {
            if (isNull(flag) || !flag) {
                layer.msg("当前环节还未开始！", {time: 3000, icon: 2});
                return;
            }
            doLoading();
            judgmentBidSectionCancel(e, function (data, bidderSize, msg) {
                if (!data) {
                    if (bidderSize < 3 && !isNull(msg)) {
                        loadComplete();
                        chooseCancelBidSection('${expert.isChairman}', msg);
                    } else {
                    window.location.href = "${ctx}/expert/evalPlan/endBasePage";
                    }
                } else {
                    loadComplete();
                    chooseCancelBidSection('${expert.isChairman}', msg);
                }
            })
        }

    }

    /**
     * 判断项目是否流标
     * @param e 当前对象
     * @param successCallback 成功回调
     * @param errorCallBack 失败回调
     */
    function judgmentBidSectionCancel(e, successCallback, errorCallBack) {
        $.ajax({
            url: "${ctx}/expert/validBidderCount",
            type: "POST",
            data: {
                evalFlow: $(e).data("evalflow")
            },
            cache: false,
            success: function (data) {
                if (successCallback && typeof (successCallback) == "function") {
                    var bidSection = data.bidSection;
                    if (!isNull(bidSection.cancelStatus) && bidSection.cancelStatus == 2) {
                        successCallback(false, "", "");
                    } else if (!isNull(bidSection.cancelStatus) && bidSection.cancelStatus == 1) {
                        successCallback(data.status, data.bidderSize, "标段已流标！");
                    } else {
                        successCallback(data.status, "", "");
                    }

                }
            },
            error: function (e) {
                console.error("流标检测失败:" + e)
                if (errorCallBack && typeof (errorCallBack) == "function") {
                    errorCallBack();
                }
            }
        });
    }

    /**
     * 跳转清标结果首页
     */
    function clearBidResult() {
       window.location.href="${ctx}/clearBid/clearBidIndex";
    }

    /**
     * 跳转到清标V3.0 页面
     * @param bId 标段ID
     */
    function goToClearUrl(bId){
        //跳转至前端页面
        window.location.href = '${ctx}/clearBidV3/toSysFront?bId='+bId+'&backPage=' + encodeURIComponent(window.location.href);
    }
</script>
</body>
</html>