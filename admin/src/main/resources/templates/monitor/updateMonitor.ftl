<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>监控修改</title>
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
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend style="font-weight: bold">监控修改</legend>
</fieldset>
<form class="layui-form" method="post" id="dep-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="regId" value="${monitor.regId}">
    <input type="hidden" name="id" value="${monitor.id}">
    <input type="hidden" name="regName" value="${monitor.regName}">
    <input type="hidden" name="regCode" value="${monitor.regCode}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">用户名</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="userName" id="userName" lay-verify="required" autocomplete="off"
                   class="layui-input" placeholder="请输入用户名" value="${monitor.userName}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="pwd" id="pwd" lay-verify="required"
                   autocomplete="off" class="layui-input"  placeholder="请输入密码" value="${monitor.pwd}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">摄像头Ip</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="ip" id="ip" lay-verify="required" autocomplete="off"
                   class="layui-input"  placeholder="请输入摄像头Ip" value="${monitor.ip}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">摄像头端口</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="port" id="port" lay-verify="required" autocomplete="off" class="layui-input"
                   placeholder="请输入摄像头端口" value="${monitor.port}">
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
<script type="text/javascript">
    var successFunc;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.on('submit(*)', function (data) {
            layer.load();
            $.ajax({
                url: '${ctx}/monitor/updateMonitor',
                type: 'post',
                cache: false,
                async: false,
                data: $("#dep-form").serialize(),
                success: function (data) {
                    if (!isNull(data)) {
                        data = JSON.parse(data);
                    }
                    successFunc();
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("修改失败！")
                },
            });
        });
        form.render();
    });

    /**
     * 修改部门
     *
     * @param successFunc1 成功回调函数
     */
    function updateDepartment(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }
</script>
</body>
</html>