<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>甘肃省电子开标评标平台</title>
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
    <script src="${ctx}/js/base64.js"></script>
    <script type="text/javascript" src="${ctx}/js/webService.js"></script>
    <link rel="stylesheet" href="${ctx}/css/process.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <style>
        <#if bidSection.paperEval?? && bidSection.paperEval == "1">
        .process ul li {
            width: 50%;
        }
        </#if>
    </style>
</head>
<body>
<div class="box">
    <!-- 头部 -->
    <#include "${ctx}/include/header.ftl">

    <section>
        <div class="process">
            <div class="head">
                <span onclick="window.location.href='/'">返回</span>
                <p title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</p>
            </div>
            <ul id="top-flow-ul">
                <li class="sele" data-instatus="true">
                    <a href="javascript:void(0);">
                        <div class="flow-left" style="opacity: 0;"></div>
                        <div class="flow-num" onclick="goToUrl('${ctx}/gov/projectInfoPage', this)">1</div>
                        <div class="flow-right"></div>
                        <p>项目信息</p>
                    </a>
                </li>
                <li data-instatus="${bidSection.bidOpenStatus gte 1 }"
                    <#if bidSection.bidOpenStatus gte 1>class="sele"</#if>>
                    <a href="javascript:void(0);">
                        <div class="flow-left"></div>
                        <div class="flow-num" onclick="goToUrl('${ctx}/gov/baseBidOpenPage', this)">2</div>
                        <#if !bidSection.paperEval?? || bidSection.paperEval != "1">
                            <div class="flow-right"></div>
                        </#if>
                        <p>开标流程</p>
                    </a>
                </li>
                <#if !bidSection.paperEval?? || bidSection.paperEval != "1">
                    <li data-instatus="${bidSection.bidOpenStatus gte 2 && bidSection.evalStatus gt 0}"
                        <#if bidSection.bidOpenStatus gte 2 && bidSection.evalStatus gt 0>class="sele"</#if>>
                        <a href="javascript:void(0);">
                            <div class="flow-left"></div>
                            <div class="flow-num" onclick="goToUrl('${ctx}/gov/bidEval/bidEvalBase', this)">3</div>
                            <div class="flow-right"></div>
                            <p>评标流程</p>
                        </a>
                    </li>
                    <li data-instatus="${bidSection.evalPdfGenerateStatus?? && bidSection.evalPdfGenerateStatus == 1 && relate.evaluationReportId??}"
                        <#if bidSection.evalPdfGenerateStatus?? && bidSection.evalPdfGenerateStatus == 1 && relate.evaluationReportId??>class="sele"</#if>>
                        <a href="javascript:void(0);">
                            <div class="flow-left"></div>
                            <div class="flow-num" onclick="goToUrl('${ctx}/gov/viewPdf/showEvaluationReportPage', this)">4</div>
                            <p>评标报告</p>
                        </a>
                    </li>
                </#if>
            </ul>
        </div>
        <div class="content-box"></div>
    </section>
</div>
<script>
    var layer;
    layui.use(['element', 'layer'], function () {
        layer = layui.layer;
    });

    $(window).on("load",function () {
        //加载消息盒子
        showMinOnlinePage();
    })

    $(function () {
        //刷新时加载默认项
        setTimeout(function () {
            var $base_ul_li = $(".process ul li");
            var $base_ul_li_index = localStorage.getItem("base_flow_${bidSection.id}");
            if ($base_ul_li_index != null) {
                $($base_ul_li.eq($base_ul_li_index)).find("div[class='flow-num']:eq(0)").click();
            } else {
                $base_ul_li.eq(0).find("div[class='flow-num']:eq(0)").click();
            }
        }, 200)
    });

    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param e
     * @param status
     */
    function goToUrl(targetUrl, e) {
        var status = $(e).parents("li").data("instatus");
        if (!status) {
            hide_IWeb2018();
            layer.msg("该环节还未开始！", {
                time: 2000, icon: 5, end: function () {
                    show_IWeb2018();
                }
            });
            return;
        }
        // 添加load
        var indexLoad = layer.load();
        setTimeout(function () {
            //设置默认加载项
            $(".process ul li").each(function (index) {
                if ($(this).is($(e).parents("li"))) {
                    localStorage.setItem("base_flow_${bidSection.id}", index);
                }
            });
            var $content_box = $(".content-box");
            $content_box.hide();
            $content_box.load(targetUrl, function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
                layer.close(indexLoad);
                $content_box.fadeIn();
            });
        }, 200);
    }


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

        var yearContent = year + "年" + month + "月" + day + "日";
        var timeContent = hours + ":" + min + ":" + sen;
        $(".year").text(yearContent);
        $(".time").text(timeContent);
    }

</script>
</body>

</html>