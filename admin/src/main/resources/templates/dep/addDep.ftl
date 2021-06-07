<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>新增部门</title>
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
    <legend style="font-weight: bold">新增部门</legend>
</fieldset>
<form class="layui-form" method="post" id="dep-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-hide">
        <input type="text" name="regId" value="${dep.regId}">
        <input type="text" name="regName" value="${dep.regName}">
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">部门名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="depName" id="dep-name" lay-verify="required" autocomplete="off"
                   class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">部门类别</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <@WordBookTag key="govDepType" name="govDepType" id="govDepType" please="t" verify="required"></@WordBookTag>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: auto;">统一信用代码</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="code" id="code" onkeyup="value=value.replace(/[^\w\/]/ig,'')" lay-verify="required"
                   autocomplete="off" class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">部门电话</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="phone" id="phone" lay-verify="required|tell_phone" autocomplete="off"
                   class="layui-input" maxlength="20">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">部门职责</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="remark" id="remark" lay-verify="required" autocomplete="off" class="layui-input" maxlength="50">
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
<script type="text/javascript">
    var successFunc;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;

        var mobile = /^1[3|4|5|7|8]\d{9}$/, phone = /^0\d{2,3}-?\d{7,8}$/;
        form.verify({
            tell_phone: function (value, item) {
                var msg;
                var flag = mobile.test(value) || phone.test(value);
                if (!flag) {
                    return '请正确座机号码或手机号!';
                }
                return msg;
            }
        });

        form.on('submit(*)', function (data) {
            layer.load();
            $.ajax({
                url: '${ctx}/dep/addDep',
                type: 'post',
                cache: false,
                async: false,
                data: $("#dep-form").serialize(),
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
     * 新增部门
     *
     * @param successFunc1 成功回调函数
     */
    function addDepartment(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }
</script>
</body>
</html>