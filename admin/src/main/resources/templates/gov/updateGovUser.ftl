<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改政府人员</title>
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
<form class="layui-form" method="post" id="form1">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <input type="hidden" name="id" value="${govUser.id}">
        <label class="layui-form-label">用户名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="name" id="name" value="${govUser.name!}" lay-verify="required" autocomplete="off"
                   placeholder="请输入用户名称" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">登录名</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="loginName" id="loginName" readonly value="${govUser.loginName!}"
                   lay-verify="required|loginName" placeholder="请输入登录名"
                   autocomplete="off" class="layui-input  layui-bg-gray">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">手机号码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="phone" id="phone" value="${govUser.phone!}" lay-verify="required|phone"
                   autocomplete="off"
                   placeholder="请输入手机号码" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">所属部门</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="roleName" id="depName" value="${govUser.depName!}" onclick="chooseRolePage()"
                   lay-verify="required"
                   autocomplete="off" class="layui-input" readonly>
            <input type="hidden" name="depId" id="depId">
        </div>
    </div>
    <#if dep.govDepType?? && dep.govDepType == 1>
        <div class="layui-form-item">
            <label class="layui-form-label">最高权限</label>
            <div class="layui-input-block" style="margin-left: 120px">
                <select name="leader" lay-filter="aihao">
                    <option value="0" <#if govUser.leader == 0>selected</#if>>不绑定</option>
                    <option value="1" <#if govUser.leader == 1>selected</#if>>绑定</option>
                </select>
            </div>
        </div>
    </#if>
    <div class="layui-form-item">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="password" name="password" id="password"  value="${govUser.password!}"
                   lay-verify="required|password" placeholder="密码修改"
                   autocomplete="off" class="layui-input  ">
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
<script type="text/javascript">
    var successFunc;
    layui.use('form', function () {
        var form = layui.form;
        var url = '${ctx}/gov/validLoginName';
        form.verify({
            loginName: function (value, item) { //value：表单的值、item：表单的DOM对象
                var msg;
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    return '用户名不能有特殊字符';
                }
                if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                    return '用户名首尾不能出现下划线\'_\'';
                }
                if (/^\d+\d+\d$/.test(value)) {
                    return '用户名不能全为数字';
                }
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
                url: '${ctx}/govUser/updateGovUser',
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
        });
        form.render();
    });

    /**
     * 修改政府人员
     *
     * @param successFunc1 成功回调函数
     */
    function updateGovUser(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }

    /**
     * 选择部门
     */
    function chooseRolePage() {
        window.top.layer.open({
            type: 2,
            title: '部门选择',
            shadeClose: true,
            area: ['45%', '60%'],
            btn: ['确认', '取消'],
            content: '${ctx}/user/chooseDepPage?regId=${regId}',
            btn1: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var res = window.top[layero.find('iframe')[0]['name']].chooseDep();
                if (res !== null && res !== undefined) {
                    $("#depId").val(res.id);
                    $("#depName").val(res.depName);
                    window.top.layer.close(index);
                } else {
                    window.top.layer.msg("请选择部门！", {icon: 2})
                }
            },
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
    }
</script>
</body>
</html>