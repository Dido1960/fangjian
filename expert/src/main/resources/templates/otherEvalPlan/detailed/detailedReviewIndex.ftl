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
    <script src="${ctx}/js/calc.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/otherDetailed.css">
    <style type="text/css">
        .process ul li {
            width: 100% !important;
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
            <li class="sele">
                <a href="#">
                    <div class="flow-num">1</div>
                    <p>详细评审</p>
                </a>
            </li>
        </ul>
    </div>
    <div id="grade_base" class="cont">
        <iframe id="contIframe" style="height: 100%;width: 100%;" frameborder="0" border="0"></iframe>
    </div>
</section>
<script>

    $(function () {
        loadGradeDetailed("${bidSection.id}", "");
    })

    // 评分框架
    function loadGradeDetailed(bidSectionId, gradeId) {
        if (isNull(gradeId)) {
            gradeId = '${grades[0].id}';
        }
        var iframe = $(".cont iframe")[0];
        try {
            iframe.contentWindow.hide_IWeb2018();
        }catch (e){
            console.warn(e);
        }

        doLoading();
        iframe.src = "${ctx}/expert/otherBidEval/loadDetailedGradePage?gradeId=" + gradeId;
        if (iframe.attachEvent) {
            iframe.attachEvent("onload", function () {
                // layer.close(indexLoad);
                loadComplete();
            });
        } else {
            iframe.onload = function () {
                // layer.close(indexLoad);
                loadComplete();
            };
        }
    }

    $("#check-number-li").on("click", "li", function () {
        $(this).addClass("sele").siblings().removeClass("sele");
    })
</script>
</body>
</html>