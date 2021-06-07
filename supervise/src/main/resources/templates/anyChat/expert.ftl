<html>
<head>
    <meta charset="utf-8">
    <title>专家评标音视频</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<script type="text/javascript">
    $(function () {
        validRemoteEvaluation();
    })

    function validRemoteEvaluation() {
        layer.open({
            type: 2,
            title: '在线通讯',
            content: "/gov/expertEval?bidSectionId=${bidSection.id}",
            area: ['310px', '276px'],
            offset: 'lb',
            maxmin: true,
            resize: false,
            shade: false,
            shadeClose: false,
            closeBtn: false,
            end: function() {
                // 禁止关闭 销毁后自动重启
                validRemoteEvaluation();
            }
        })
    }
</script>
</body>
</html>
