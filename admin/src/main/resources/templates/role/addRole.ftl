<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>新增角色</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
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
    <legend style="font-weight: bold">添加角色</legend>
</fieldset>
<form class="layui-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">角色名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="roleName" id="roleName" lay-verify="required" autocomplete="off"
                   placeholder="请输入角色名称" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">备注说明</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="remark" id="remark" lay-verify="required" autocomplete="off"
                   placeholder="请输入角色备注" class="layui-input">
        </div>
    </div>

</form>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
    });

    /**
     * 新增角色
     *
     * @param successFunc 成功回调函数
     */
    function addRole(successFunc) {
        var roleName = $("#roleName").val();
        var remark = $("#remark").val();
        if (roleName) {
            $.ajax({
                url: '${ctx}/role/addRole',
                data: {
                    roleName: roleName,
                    remark: remark,
                    enabled: 1
                },
                success: function (data) {
                    data = JSON.parse(data);
                    // 针对新增角色结果做相应提示
                    if (data) {
                        window.top.layer.msg("新增成功！", {icon: 1, time: 2000, end: successFunc});
                    } else {
                        window.top.layer.msg("新增失败！", {icon: 2})
                    }
                }
            })
        } else if (!roleName) {
            window.top.layer.msg("角色名称不能为空！", {icon: 2})
        }
    }
</script>
</body>
</html>