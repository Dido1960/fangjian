<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>印模签章配置修改</title>
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
    <legend style="font-weight: bold">印模签章配置修改</legend>
</fieldset>
<form class="layui-form" method="post" id="signature-config-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" value="${signatureConfigInfo.id}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">所属区域</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 600px">
            <input type="text" name="regName" id="reg-name" lay-verify="required"
                   class="layui-input" value="${signatureConfigInfo.regName}" readonly>
            <input type="hidden" name="regNo" value="${signatureConfigInfo.regNo}">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">印模编号</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 600px">
            <input type="text" name="impressionNo" id="impression-no" lay-verify="required"
                   class="layui-input" placeholder="请输入印模编号" value="${signatureConfigInfo.impressionNo}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 600px">
            <input type="text" name="remark" id="remark" lay-verify="required" autocomplete="off" class="layui-input" placeholder="请输入备注"  value="${signatureConfigInfo.remake}">
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

        form.on('submit(*)', function (data) {
            layer.load();
            $.ajax({
                url: '${ctx}/signatureConfig/updateSignatureConfigInfo',
                type: 'post',
                cache: false,
                async: false,
                data: $("#signature-config-form").serialize(),
                success: function (data) {
                    if (!isNull(data)) {
                        data = JSON.parse(data);
                    }
                    successFunc();
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("修改失败！")
                },
            });
        });
        form.render();
    });

    /**
     * 修改印模签章配置
     *
     * @param successFunc1 成功回调函数
     */
    function updateSignatureConfigInfo(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }
</script>
</body>
</html>