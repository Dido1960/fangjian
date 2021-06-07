<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>甘肃省电子开标评标平台</title>
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
    <script src="${ctx}/js/calc.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/process.css">
    <link rel="stylesheet" href="${ctx}/css/iframeDetailedHead.css">
    <style type="text/css">
        .process ul li {
            width: 100% !important;
        }
    </style>
</head>
<body>
<#include "${ctx}/include/header.ftl">
<section>
    <div class="process">
        <h3>
            <span onclick="returnUpFlow()">返回</span>
            <p title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</p>
        </h3>
        <ul id="check-number-li">
            <li class="sele">
                <div class="flow-left" style="opacity: 0;"></div>
                <div class="flow-num" onclick="loadGradeDetailed()">1</div>
                <p>详细评审</p>
            </li>
        </ul>
    </div>
    <div id="grade_base" class="cont">
        <iframe style="height: 100%" width="100%" frameborder="0" border="0"></iframe>
    </div>
</section>
<script>

    $(function () {
        loadGradeDetailed();
    })

    // 评分框架
    function loadGradeDetailed() {
        layerLoadingForExpert("数据加载中...");
        setTimeout(function () {
            var iframe = $(".cont iframe")[0];
            iframe.src = "${ctx}/expert/epcBidEval/loadDetailedGradeDetailedPage";
            if (iframe.attachEvent) {
                iframe.attachEvent("onload", function () {
                    loadComplete();
                });
            } else {
                iframe.onload = function () {
                    loadComplete();
                };
            }
        }, 400)
    }
</script>
</body>
</html>