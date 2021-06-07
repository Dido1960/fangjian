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
    <script src="${ctx}/js/calc.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/conDetailedHead.css">
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
           <li class="sele" style="width: 100%">
                <a href="javascript:void(0)">
                    <div class="flow-left" style="opacity: 0;"></div>
                    <div class="flow-num" onclick="loadGradeDetailed('${grade.id }')">1<i></i></div>
                    <p>${grade.name}</p>
                </a>
           </li>
        </ul>
    </div>
    <div id="grade_base" class="cont">
        <iframe id="contIframe" style="height: 100%" width="100%" frameborder="0" border="0"></iframe>
    </div>
</section>
<script>

    $(function () {
        loadGradeDetailed('${grade.id}');
    })

    // 评分框架
    function loadGradeDetailed(gradeId) {
        var iframe = $(".cont iframe")[0];
        try {
            iframe.contentWindow.hide_IWeb2018();
        }catch (e){
            console.warn(e);
        }

        doLoading();
        iframe.src = "${ctx}/expert/conBidEval/otherMutualContentPage?gradeId=" + gradeId;
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

    $("#check-number-li").on("click", "li .flow-num", function () {
        $(this).parents("li").addClass("sele").siblings().removeClass("sele");
    })
</script>
</body>
</html>