<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>生成授权</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
</head>
<body>
<form class="layui-form" style="margin: 15px;">
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: 180px">选择授权的接口</label>
        <div class="layui-input-block" style="margin-left: 210px">
            <@WordBookTag key="apiName" name="apiName" id="api-name" verify="required" reqtext="请选择API NAME !" please="t"></@WordBookTag>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style=" width: 180px">授权平台名称（建议中文）</label>
        <div class="layui-input-block" style="margin-left: 210px">
            <input type="text" name="remark" id="remark" lay-verify="required" reqtext="授权平台名称 !" placeholder="请输入"
                   autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <button id="submit-btn" class="layui-btn layui-hide" lay-filter="add-api-auth" lay-submit></button>
</form>
<script>
    /**
     * 生成授权
     */
    function add() {
        $("#submit-btn").click();
    }
</script>
<script type="text/javascript">
    /**
     * 初始化layui
     */
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;

        form.render();

        form.on("submit(add-api-auth)", function () {
            var api_name = $("#api-name").val();
            var remark = $("#remark").val();

            var index = parent.layer.getFrameIndex(window.name);

            $.ajax({
                url: "${ctx}/apiAuth/addApiAuth",
                type: "POST",
                cache: false,
                data: {
                    apiName: api_name,
                    remark: remark
                },
                success: function (data) {
                    if (data) {
                        parent.layui.table.reload("api-auth-table");
                        parent.layer.close(index);
                    } else {
                        parent.layer.close(index);
                    }
                },
                error: function () {
                    parent.layer.close(index);
                }
            });
            return false;
        })
    });


</script>
</body>
</html>