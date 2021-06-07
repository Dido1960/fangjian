<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>甘肃省电子开评标平台</title>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>
<div id="baseDiv">

</div>
<script>
    $(function () {
        goToUrl("${ctx}/expertInput/expertInputPage");
    });

    function goToUrl(targetUrl) {
        $("#baseDiv").load(targetUrl + "?bidSectionId=${bidSectionId}", function () {
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
</script>
</body>
</html>