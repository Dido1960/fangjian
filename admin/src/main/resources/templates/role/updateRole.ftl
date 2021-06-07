<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>角色修改</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
</head>
<body>
<form class="layui-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">角色名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="roleName" id="roleName" value="${role.roleName!}" lay-verify="required"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">排序编号</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="orderNo" id="orderNo" value="${role.orderNo!}" lay-verify="required"
                   autocomplete="off" class="layui-input" disabled>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">启用状态</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <@WordBookTag key="enabled" name="enabled" id="enabled" please="t"></@WordBookTag>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">备注说明</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <textarea name="remark" id="remark"
                      style="overflow: auto;height: 100px;width: 99%">${role.remark!}</textarea>
        </div>
    </div>
</form>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.render();
    });

    /**
     * 修改角色信息
     *
     * @param successFunc 成功回调函数
     */
    function updateRole(successFunc) {
        var roleName = $("#roleName").val();
        var enabled = $('#enabled option:selected').val();
        var remark = $("#remark").val();
        if (roleName && enabled && remark) {
            $.ajax({
                url: '${ctx}/role/updateRole',
                data: {
                    id: ${role.id},
                    roleName: roleName,
                    orderNo: $("#orderNo").val(),
                    enabled: enabled,
                    remark: remark
                },
                success: function (data) {
                    data = JSON.parse(data);
                    // 针对修改部门结果做相应提示
                    if (data) {
                        window.top.layer.msg("修改成功！", {icon: 1, time: 2000, end: successFunc});
                    } else {
                        window.top.layer.msg("修改失败！", {icon: 2, time: 2000})
                    }
                }
            })
        } else if (!roleName) {
            window.top.layer.msg("角色名称不能为空！", {icon: 2})
        } else if (!enabled) {
            window.top.layer.msg("请选择角色启用状态！", {icon: 2})
        } else if (!remark) {
            window.top.layer.msg("备注说明不能为空！", {icon: 2})
        }
    }

    $(function () {
        var enabled = '${role.enabled}';
        $("#enabled").val(encodeURI(enabled));
    });
</script>
</body>
</html>