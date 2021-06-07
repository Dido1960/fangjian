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
    <script type="text/javascript">
        var successFunc;
        layui.use('form', function () {
            var form = layui.form;
            var url = '${ctx}/admin/companyUser/validLoginName';
            form.verify({
                loginName: function (value, item) {//value：表单的值、item：表单的DOM对象
                    var msg;
                    if (!new RegExp('^[a-zA-Z0-9_\u4e00-\u9fa5\s·]+$').test(value)) {
                        return '不能有特殊字符';
                    }
                    if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                        return '首尾不能出现下划线\'_\'';
                    }
                    var minlength = $(item).attr("minlength");
                    if (minlength && value.length < minlength) {
                        return '长度至少为' + minlength + "位";
                    }
                    if (/^\d+$/.test(value)) {
                        return '不能全为数字';
                    }
                    $.ajax({
                        type: "POST",
                        url: url,
                        cache: false,
                        async: false, // 使用同步的方法
                        data: {
                            loginName: value
                        },
                        dataType: 'json',
                        success: function (result) {
                            if (!result) {
                                msg = "该用户名已存在，请重新设置用户名称";
                            }
                        }
                    });
                    return msg;
                },
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
                },
                passwordConfirm: function (value, item) {
                    var msg;
                    if (value !== $("#password").val()) {
                        msg = "两次密码输入不一致";
                    }
                    return msg;
                }
            });
            form.on('submit(*)', function (data) {
                layer.load();
                $.ajax({
                    url: '${ctx}/admin/companyUser/addCompanyUser',
                    type: 'post',
                    cache: false,
                    async: false,
                    data: $("#form1").serialize(),
                    success: function (data) {
                        if (!isNull(data)) {
                            data = JSON.parse(data);
                        }
                        successFunc();
                    },
                    error: function (data) {
                        console.error(data);
                        layer.msg("操作失败！")
                    },
                });
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            });
            form.render();
        });

        /**
         * 新增人员
         *
         * @param successFunc1 成功回调函数
         */
        function submitCompanyUser(successFunc1) {
            successFunc = successFunc1;
            $("#formBtnSubmit").trigger("click");
        }
    </script>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>添加企业</legend>
</fieldset>
<form class="layui-form" method="post" id="form1">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="enabled" value="1">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">企业名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="name" id="name" lay-verify="required" autocomplete="off"
                   placeholder="请输入用户名称" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">登录名</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="loginName" id="login-name" lay-verify="required|loginName" minlength="6"
                   placeholder="请输入登录名"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="password" name="password" id="password" lay-verify="required|password" autocomplete="off"
                   placeholder="设置密码"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">确认密码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="password" id="password2" lay-verify="required|passwordConfirm" autocomplete="off"
                   placeholder="确认密码"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">统一社会信用代码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="type" lay-verify="required" name="code" autocomplete="off"
                   placeholder="组织机构代码" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">联系人</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="linkMan" lay-verify="required" placeholder="请输入联系人"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">联系电话</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="phone" id="phone" lay-verify="required" autocomplete="off"
                   placeholder="请输入手机号码" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">企业地址</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="address" lay-verify="required" placeholder="请输入联系人"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
</body>
</html>