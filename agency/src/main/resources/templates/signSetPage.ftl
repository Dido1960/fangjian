<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>签到设置</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <style>
        html .layui-layer-title {
            padding: 0;
            border-bottom: 1px solid rgba(213, 213, 213, 1);
        }

        html .layui-layer-btn .layui-layer-btn0 {
            background-color: rgba(19, 97, 254, 1);
        }

        .signTan h3 {
            width: 60px;
            height: 27px;
            font-size: 16px;
            font-family: Microsoft YaHei;
            font-weight: 900;
            line-height: 27px;
            color: rgba(34, 49, 101, 1);
            opacity: 1;
            margin: 41px auto 0;
        }

        .signTan input {
            display: block;
            width: 300px;
            height: 40px;
            background: rgba(255, 255, 255, 1);
            border: 1px solid rgba(213, 213, 213, 1);
            opacity: 1;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 900;
            margin: 34px auto 0;
            padding: 0 10px;
            box-sizing: border-box;
        }

        .signTan p {
            width: 112px;
            height: 24px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 900;
            line-height: 24px;
            color: rgba(34, 49, 101, 1);
            opacity: 1;
            margin: 20px auto 0;
        }

        .layui-layer-btn,
        .layui-layer-btn-c {
            background: rgba(239, 243, 244, 1);
        }
    </style>
</head>
<body style="overflow: hidden">
<div class="signTan">
    <h3>开标前</h3>
    <form id="save" action="#">
        <input type="hidden" id="id" name="id" value="${bidSection.id}">
        <input type="text" id="signInStartTimeLeft" name="signInStartTimeLeft" value="${bidSection.signInStartTimeLeft}" placeholder="请输入">
    </form>
    <p>（分钟）开始签到</p>
</div>
</body>
<script>
    function saveSignTime() {
        var time = $("#signInStartTimeLeft").val();
        var r = /^\+?[1-9][0-9]*$/;
        if(!r.test(time)) {
            layer.msg("请填写合理的数字！");
            return;
        }
        $.ajax({
            url: '${ctx}/staff/bidSection/updateBidSection',
            type: 'post',
            cache: false,
            data: {
                id: $("#id").val(),
                signInStartTimeLeft: time
            },
            success: function (data) {
                if(data) {
                    parent.layer.closeAll();
                    parent.layer.msg("保存成功！", {icon: 1})
                } else {
                    parent.layer.msg("保存失败！", {icon: 5})
                }

            },
            error: function (data) {
                console.warn(data);
                parent.layer.msg("保存失败！", {icon: 5})
            },
        });
    }
</script>
</html>