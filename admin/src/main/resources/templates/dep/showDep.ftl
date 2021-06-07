<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门查看</title>
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
    <legend style="font-weight: bold">查看部门</legend>
</fieldset>
<form class="layui-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">部门名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="depName" id="dep-name" value="${dep.depName}" readonly lay-verify="required"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">部门类别</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <@WordBookTag key="govDepType" name="govDepType" id="govDepType" please="t" verify="required" disabled="disabled" value="${dep.govDepType}"></@WordBookTag>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: auto;">统一信用代码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="code" id="code" value="${dep.code!}" readonly lay-verify="required"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: auto;">所属行政区划</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="regName" id="reg-name" value="${dep.regName}" required lay-verify="required"
                   autocomplete="off" class="layui-input" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">部门电话</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="phone" id="phone" value="${dep.phone!}" readonly lay-verify="required"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">部门职责</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="remark" id="remark" value="${dep.remark!}" readonly lay-verify="required"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
</form>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.render();
    });
</script>
</body>
</html>