<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>专家评标</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/list.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <style>
        html, body {
            width: 100%;
            height: 100%;
            min-width: 1200px;
            min-height: 800px;
            background: #eee;
        }
    </style>

</head>
<body>
<div class="box">
    <header>
        <div class="text">
            <div class="name">甘肃省房建市政电子辅助评标系统</div>
            <div class="bao">
                <div class="try userName" onclick="exitSystem()"><b class="username"></b>
                    <i ></i>
                </div>
            </div>
        </div>
    </header>
    <section class="baseContent"></section>
    <input type="hidden" id="bidSectionId">
</div>


</body>
<script>
    var bidSectionId;
    $(function () {
        getUser();
        if (localStorage.getItem("currentPage") == "1") {
            goToUrl('${ctx}/selectLeader/commitBook?bidSectionId=' + $('#bidSectionId').val());
        } else {
            goToUrl('${ctx}/selectLeader/selectLeaderPage?bidSectionId=' + $('#bidSectionId').val());
        }

    });

    /**
     * 加载局部div
     * @param targetUrl 目标路由
     */
    function goToUrl(targetUrl) {
        // 添加load
        var indexLoad = layer.load();
        setTimeout(function () {
            $(".baseContent").hide();
            $(".baseContent").load(targetUrl, function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                });
                layer.close(indexLoad);
                $(".baseContent").fadeIn();
            });
        }, 100);
    }

    /**
     * 退出登录
     */
    function exitSystem() {
        hide_IWeb2018();
        layer.confirm("确认要退出系统？", {
            icon: 3,
            title: '提示'
        }, function (index) {
            layer.close(index);
            layer.load();
            $.ajax({
                url: '${ctx}/logout',
                type: 'post',
                cache: false,

                success: function () {
                    window.location.href = "${ctx}/login.html";
                }
            });
        }, function (index) {
            // 取消的回调函数
            layer.msg("已取消!");
            layer.close(index);
        });
    }

    /**
     * 获取当前用户
     */
    function getUser() {
        $.ajax({
            url: '${ctx}/getUser',
            type: 'post',
            cache: false,

            success: function (data) {
                $('.try .username').append(data.loginName);
                $('#bidSectionId').val(data.bidSectionId);
            }
        });
    }


</script>

</html>