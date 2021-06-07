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
    <script src="${ctx}/js/common.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>个人资料</legend>
</fieldset>
<form class="layui-form" method="post" id="form1" enctype="multipart/form-data">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" value="${userInfo.id}">
    <div class="layui-form-item">
        <label class="layui-form-label">用户头像</label>
        <div class="layui-input-block" style="margin-left: 120px" id="user_img">
            <#if userInfo.url!>
                <img src="${userInfo.url}" width="100px" height="100px">
            <#else >
                <img src="http://61.178.200.56:8089/mygroup/M00/00/62/rBUBRl7EfAOAHU9RAAKkyX5DntI431.jpg" width="100px"
                     height="100px">
            </#if>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">用户名称</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="text" name="name" id="user-name" value="${userInfo.name}" lay-verify="required"
                   autocomplete="off" placeholder="请输入用户名称" class="layui-input  layui-bg-gray" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">登录名</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="text" name="loginName" id="login-name" value="${userInfo.loginName}" lay-verify="required"
                   placeholder="请输入登录名" autocomplete="off" class="layui-input  layui-bg-gray" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">手机号码</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="text" name="phone" id="phone" value="${userInfo.phone!}"
                   lay-verify="required" autocomplete="off" placeholder="请输入手机号码" class="layui-input  layui-bg-gray">
        </div>
    </div>

    </div>


</form>
<script type="text/javascript">


    layui.use('form', 'layer', 'upload', function () {
        var form = layui.form;
        form.render();
    });


</script>
</body>
</html>