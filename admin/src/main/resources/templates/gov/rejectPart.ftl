<!DOCTYPE html>
<html lang="zh">
<head>
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
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>
<form class="layui-form" id="form1">
    <div class="layui-hide">
        <input type="text" value="${id}" name="id">
    </div>
    <div class="layui-hide">
        <input type="text" value="${flowItemState}" name="flowItemState">
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">退回理由:</label>
        <div class="layui-input-block" style="margin-right: 15px">
            <textarea name="res" placeholder="请输入退回理由" class="layui-textarea" lay-verify="required"></textarea>
        </div>
    </div>
    <div class="layui-hide">
        <input type="button" lay-submit lay-filter="*" id="submitForm">
    </div>
</form>
</body>
<script>
    var successFunc;
    /**
     * 提交退回原因
     */
    layui.use('form', function () {
        var form = layui.form;

        //监听提交
        form.on('submit(*)', function (data) {
            var index_load=window.top.layer.load();
            $.ajax({
                url: '${ctx}/partAuditFlow/handleBack',
                type: 'post',
                cache: false,
                data: $("#form1").serialize(),
                success: function (data) {
                    window.top.layer.close(index_load);
                    if (data) {
                        successFunc();
                        // top.location.href=""
                    }
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("操作失败！")
                    window.top.layer.closeAll();
                },
            });
        });
    });

    function addContact(successFunc1) {
        successFunc = successFunc1;
        $("#submitForm").trigger("click");
    }


</script>
</html>