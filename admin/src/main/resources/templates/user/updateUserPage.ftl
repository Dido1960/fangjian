<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>人员修改</title>
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
    <legend>人员修改</legend>
</fieldset>
<form class="layui-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">用户名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="name" id="user-name" value="${userInfo.name}" lay-verify="required"
                   autocomplete="off" placeholder="请输入用户名称" class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">登录名</label>
        <div class="layui-input-block" style="margin-left: 120px;background-color: #eee !important;">
            <input type="text" name="loginName" id="login-name" value="${userInfo.loginName}" lay-verify="required"
                   placeholder="请输入登录名" autocomplete="off" class="layui-input layui-bg-gray" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: auto">所属行政区划</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="regName" id="reg-name" value="${reg.regName}" lay-verify="required"
                   autocomplete="off" class="layui-input layui-bg-gray" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">手机号码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="phone" id="phone" value="${userInfo.phone!}"
                   lay-verify="required" autocomplete="off" placeholder="请输入手机号码" class="layui-input" maxlength="11">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">所属角色</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="roleName" id="role-name" onclick="chooseRole()"
                   value="${rolesNmaes!}" class="layui-input" readonly>
            <input type="hidden" id="role-id" value="${userInfo.roleId!}">
        </div>
    </div>
</form>
<script type="text/javascript">
    layui.use('form', function () {
        var form = layui.form;

        form.render();
    });
    // 选择角色弹窗
    var choose_role_index;

    /**
     * 选择角色
     */
    function chooseRole() {
        var roleId = $("#role-id").val();
        choose_role_index = window.top.layer.open({
            type: 2,
            title: '选择角色',
            shadeClose: true,
            area: ['45%', '60%'],
            btn: ['确认', '取消'],
            content: '${ctx}/user/chooseRolePage?roleId=' + roleId,
            btn1: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var res = window.top[layero.find('iframe')[0]['name']].chooseRole();
                if (res !== null && res !== undefined) {
                    $("#role-name").val(res.roleName);
                    $("#role-id").val(res.id);
                    window.top.layer.close(choose_role_index);
                } else {
                    window.top.layer.msg("请至少选择一个角色！", {icon: 2})
                }
            },
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 修改人员信息
     *
     * @param successFunc 成功回调函数
     */
    function updateUser(successFunc) {
        var user_name = $("#user-name").val();
        var login_name = $("#login-name").val();
        var reg_id = '${reg.id}';
        var phone = $("#phone").val();
        var role_id = $("#role-id").val();

        if (!(/^[5A-Za-z0-9-\_\u4E00-\u9FFF]+$/.test(user_name))) {//数字,字母,下划线，汉字
            window.top.layer.msg("请输入正确的用户名称！", {icon: 2})
        }else if (!(/^1[3456789]\d{9}$/.test(phone))){
            $("#phone").val("");
            window.top.layer.msg("请输入正确的手机号码！", {icon: 2})
        } else if (user_name && login_name && reg_id) {
            $.ajax({
                url: '${ctx}/user/updateUser',
                data: {
                    id: '${userInfo.id}',
                    name: user_name,
                    loginName: login_name,
                    regId: reg_id,
                    phone: phone,
                    roleIds: role_id
                },
                success: function (data) {
                    data = JSON.parse(data);
                    if (data) {
                        window.top.layer.msg("修改成功！", {icon: 1, time: 2000, end: successFunc})
                    } else {
                        window.top.layer.msg("修改失败！", {icon: 2, time: 2000})
                    }
                }
            })
        } else if (!user_name) {
            window.top.layer.msg("用户名称不能为空！", {icon: 2})
        } else if (!login_name) {
            window.top.layer.msg("登录名不能为空！", {icon: 2})
        } else if (!reg_id) {
            window.top.layer.msg("所属行政区划不能为空！", {icon: 2})
        }
    }
</script>
</body>
</html>