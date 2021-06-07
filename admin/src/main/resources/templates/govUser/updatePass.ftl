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
    <legend>修改密码</legend>
</fieldset>
<form class="layui-form" method="post" id="form1">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">用户名称</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="text" name="userName" id="user-name" value="${userInfo.name}" lay-verify="required"
                   autocomplete="off" class="layui-input layui-bg-gray" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: auto">请输入密码</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="password" name="password" id="password" lay-verify="required|pass"
                   placeholder="请输入密码" autocomplete="off" class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: auto">再次输入密码</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="password" id="repass" lay-verify="required|pass|confirmPass"
                   placeholder="再次输入密码" autocomplete="off" class="layui-input" maxlength="50">
        </div>

    </div>
    <div class="layui-hide">
        <input type="button" lay-submit lay-filter="*" id="submitForm">
    </div>
</form>
<script type="text/javascript">
    layui.use('form', function () {
        var form = layui.form;
        var ptr_spec = /[^A-Za-z0-9]+/;
        form.verify({
            pass: function (value, item) {
                if (value == null) {
                    return "密码不能为空";
                }
                if (ptr_spec.test(value)) {
                    return "密码不能有特殊字符";
                }
                if (password.length < 3 || password.length > 16) {
                    return "密码需要在3-16位之间!";
                }
            }

            , confirmPass: function (value, item) {
                if (value == null) {
                    return "请在此输入密码";
                }
                if (value != $("#password").val()) {
                    return "两次输入密码不一致";
                }
            }
        });
        form.on('submit(*)', function (data) {
            layer.load();
            $.ajax({
                url: '${ctx}/govUser/updatePass',
                type: 'post',
                cache: false,
                async: false,
                data: $("#form1").serialize(),
                success: function (data) {
                    if (data) {
                        window.top.layer.msg("修改成功！", {icon: 1, time: 2000, end: successFunc});
                    }
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("操作失败！")
                },
            });
        })


        form.render();
    });
    // 选择角色弹窗
    var choose_role_index;


    /**
     * 修改人员信息
     *
     * @param successFunc 成功回调函数
     */
    function updateUser(successFunc1) {
        successFunc = successFunc1;
        $("#submitForm").trigger("click");

        /*   var user_name = $("#user-name").val();
           var password =$("#password").val();
           var repass=$("#repass").val();
           var ptr_spec=/[^A-Za-z0-9]+/;
          if (!user_name) {
               window.top.layer.msg("用户名称不能为空！", {icon: 2})
           } else if (!password) {
               window.top.layer.msg("密码不能为空！", {icon: 2})
           }
          else if (ptr_spec.test(password)){
              window.top.layer.msg("密码不能包含特殊字符！", {icon: 2})
          }
          else if(password.length<3||password.length>16){
              window.top.layer.msg("密码需要在3-16位之间！", {icon: 2})
          }
          else if (!repass){
              window.top.layer.msg("请再次输入密码！", {icon: 2})
          } else if(password!=repass){
               window.top.layer.msg("两次输入密码不一致", {icon: 2})
           } else{
                   $.ajax({
                       url: '
        ${ctx}/govUser/updatePass',
                    cache:false,
                    data: {password:password},
                    success: function (data) {
                        if (data) {
                            window.top.layer.msg("修改成功！", {icon: 1, time: 2000, end: successFunc})
                        } else {
                            window.top.layer.msg("修改失败！", {icon: 2, time: 2000})
                        }
                    }
                })

    }
</script>
</body>
</html>