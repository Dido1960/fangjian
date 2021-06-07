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
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/reviewIndex.css">

    <style>
        .head ul li {
        <#if grades?? && grades?size gt 1>
            width: ${(100/grades?size)?string["0.##"]}% !important;
        <#else>
            width: 100% !important;
        </#if>
            height: 154px;
            float: left;
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
            <#list grades as grade>
                <li <#if grade_index == 0>class="sele"</#if>>
                    <a onclick="loadGradeDetailed('${grade.id }')">
                        <div class="flow-num">${grade_index + 1}
                            <i></i>
                        </div>
                        <p>${grade.name}</p>
                    </a>
                </li>
            </#list>
        </ul>
    </div>
    <div id="grade_base" class="cont">
        <iframe id="contIframe" style="height: 100%" width="100%" frameborder="0" border="0"></iframe>
    </div>
    <form id="review-result-form" action="${ctx}/expert/epcBidEval/qualifiedEvalResultPage" method="post" style="display: none">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </form>
</section>
<script>
    $(function () {
        loadGradeDetailed(null);
    });

    // 评分框架
    function loadGradeDetailed(gradeId) {
        if (isNull(gradeId)){
            gradeId= '${grades[0].id}';
        }
        layerLoadingForExpert("数据加载中...");
        setTimeout(function () {
            var iframe = $(".cont iframe")[0];
            iframe.src = "${ctx}/expert/epcBidEval/qualifyReviewContentPage?gradeId=" + gradeId;
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

    $("#check-number-li").on("click", "li .bound-jump", function () {
        $(this).parents("li").addClass("sele").siblings().removeClass("sele");
    });
</script>
</body>
</html>