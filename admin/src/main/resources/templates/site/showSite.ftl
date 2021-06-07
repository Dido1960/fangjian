<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>场地查看</title>
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
    <legend style="font-weight: bold">场地查看</legend>
</fieldset>
<form class="layui-form" method="post" id="dep-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" value="${site.id}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">所属区域</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="regName" id="reg-name" lay-verify="required"
                   class="layui-input" value="${site.regName}" readonly>
            <input type="hidden" name="regId" value="${site.regId}">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">场地名称</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="name" id="site-name" lay-verify="required"
                   class="layui-input" placeholder="请输入场地名称" value="${site.name}" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">场地类型</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="name" id="site-name" lay-verify="required"
                   class="layui-input"  value="${site.typeName}" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="remark" id="remark" lay-verify="required" autocomplete="off" class="layui-input" placeholder="请输入备注"  value="${site.remark}" readonly>
        </div>
    </div>
</form>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
    });
</script>
</body>
</html>