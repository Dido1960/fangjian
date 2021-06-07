<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加人员</title>
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
    <legend>添加人员</legend>
</fieldset>
<form class="layui-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">用户名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="name" id="name" lay-verify="required" autocomplete="off"
                   placeholder="请输入用户名称" class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">登录名</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="loginName" id="login-name" lay-verify="required" placeholder="请输入登录名"
                   autocomplete="off" class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="password" name="password" id="password" lay-verify="required|password" autocomplete="off"
                   class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: auto">所属行政区划</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="regName" id="reg-name" value="${reg.regName}" lay-verify="required"
                   autocomplete="off" class="layui-input layui-bg-gray" readonly>
            <input type="hidden" name="regId" value="${reg.id}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">手机号码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="phone" id="phone" lay-verify="required" autocomplete="off"
                   placeholder="请输入手机号码" class="layui-input" maxlength="11">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">所属角色</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="roleName" id="role-name" onclick="chooseRolePage()" lay-verify="required"
                   autocomplete="off" class="layui-input" readonly>
            <input type="hidden" name="roleId" id="role-id">
        </div>
    </div>
</form>
<script type="text/javascript">
    layui.use('form', function () {
        var form = layui.form;
        form.verify({
            password: function (value, item) {//value：表单的值、item：表单的DOM对象
                var msg;
                if (!new RegExp('^[a-zA-Z0-9_\u4e00-\u9fa5\s·]+$').test(value)) {
                    return '不能有特殊字符';
                }
                if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                    return '首尾不能出现下划线\'_\'';
                }
                var minlength = 6;
                if (minlength && value.length < minlength) {
                    return '密码长度至少为' + minlength + "位";
                }
                if (/^\d+$/.test(value)) {
                    return '密码不能全为数字';
                }
            }
        });
        form.render();
    });

    // 选择角色弹窗
    var choose_role_index;


    /**
     * 选择角色
     */
    function chooseRolePage() {
        var roleId = $("#roleId").val();
        choose_role_index = window.top.layer.open({
            type: 2,
            title: '选择角色',
            shadeClose: true,
            area: ['45%', '60%'],
            btn: ['确认', '取消'],
            content: '${ctx}/govUser/chooseRolePage',
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
     * 新增人员
     *
     * @param successFunc 成功回调函数
     */
    function addUser(successFunc) {
        var userName = $("#name").val();
        var loginName = $("#login-name").val();
        var password = $("#password").val();
        var regId = '${reg.id}';
        var phone = $("#phone").val();
        var roleId = $("#role-id").val();
        if (userName && loginName && password && regId && roleId) {
            $.ajax({
                url: '${ctx}/govUser/addUser',
                data: {
                    name: userName,
                    loginName: loginName,
                    password: password,
                    regId: regId,
                    phone: phone,
                    roleId: roleId,
                    enabled: 1
                },
                success: function (data) {
                    data = JSON.parse(data);
                    if (data) {
                        window.top.layer.msg("新增成功！", {icon: 1, time: 2000, end: successFunc})
                    } else {
                        window.top.layer.msg("新增失败！", {icon: 2, time: 2000})
                    }
                }
            })
        } else if (!userName) {
            window.top.layer.msg("用户名称不能为空！", {icon: 2})
        } else if (!loginName) {
            window.top.layer.msg("登录名不能为空！", {icon: 2})
        } else if (!password) {
            window.top.layer.msg("密码不能为空！", {icon: 2})
        } else if (!regId) {
            window.top.layer.msg("所属行政区划不能为空！", {icon: 2})
        } else if (!roleId) {
            window.top.layer.msg("所属角色不能为空！", {icon: 2})
        }
    }
</script>
</body>
</html>