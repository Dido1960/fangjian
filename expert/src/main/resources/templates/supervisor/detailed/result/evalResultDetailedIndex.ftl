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
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/process.css">
    <link rel="stylesheet" href="${ctx}/css/supDetailedResult.css">
</head>
<body>
<#include "${ctx}/include/header.ftl">
<section>
    <h3 class="head"><span onclick="window.location.href='${ctx}/expert/evalPlan/detailedReview'">返回</span>评审结果</h3>
    <div class="choice" style="background: none;">
        <ol id="sele-ol" class="select">
            <li class="sele" onclick="loadPersonDetailed()">个人评审结果</li>
            <li onclick="loadGroupDetailed()">小组评审结果</li>
        </ol>
        <span class="red-f" style="width: 420px;">
                <img src="${ctx}/img/warning.png" alt="">
                温馨提示：仅在个人评审未结束时，可单击更改评审结果</span>
    </div>
    <div id="grade_base" class="conts"></div>
</section>
<script>
    $(function () {
        loadPersonDetailed();
    });
    /**
     * 个人评审结果
     * @param bidSectionId
     */
    function loadPersonDetailed() {
        $("#grade_base").load("${ctx}/expert/supBidEval/detailedPersonPage",
            function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
        });
    }

    /**
     * 小组评审结果
     * @param bidSectionId
     */
    function loadGroupDetailed() {
        $("#grade_base").load("${ctx}/expert/supBidEval/detailedGroupResultBasePage",
            function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
            });
    }

    $("#sele-ol").on('click', 'li', function () {
        $(this).addClass("sele").siblings().removeClass("sele");
    });

</script>
</body>
</html>