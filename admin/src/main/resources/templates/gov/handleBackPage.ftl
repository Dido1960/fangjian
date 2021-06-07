<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加政府人员</title>
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
    <legend>退回选择</legend>
</fieldset>
<form class="layui-form" method="post" id="form1">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" value="${id}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label" style="width: 100px">退回至上一级</label>
        <div class="layui-input-block" style="margin-left: 50px">
            <input type="radio" name="flowItemState" id="flowItemState" autocomplete="off" value="4" checked
              class="layui-form-radio">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">退回至代理</label>
        <div class="layui-input-block" style="margin-left: 50px">
            <input type="radio" name="flowItemState" id="flowItemState" autocomplete="off" value="5"
                   class="layui-form-radio">
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

   /*     form.on('submit(*)', function (data) {
            layer.load();
            $.ajax({
                url: '${ctx}/partAuditFlow/handleBack',
                type: 'post',
                cache: false,
                async: false,
                data: $("#form1").serialize(),
                success: function (data) {
                    if (!isNull(data)) {
                        data = JSON.parse(data);
                    }
                    if (successFunc && typeof (successFunc) == "function") {
                        successFunc();
                    }

                },
                error: function (data) {
                    console.error(data);
                    layer.msg("操作失败！")
                },
            });
            return false;//阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });*/
        form.render();
    });

    /**
     * 新增政府人员
     *
     * @param successFunc1 成功回调函数
     */
  /*  function submitGovUser(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }*/
    function submitGovUser(callback,index_parent) {
     var val = $("input[name='flowItemState']:checked").val();

        window.top.layer.open({
            type: 2,
            title: '驳回理由编辑',
            shadeClose: false,
            shade: 0,
            maxmin: true,
            area: ['40%', ' 40%'],
            btn: ['提交', '关闭'],
            content: '${ctx}/partAuditFlow/rejectPage?id=${id}&flowItemState='+val ,
            success: function (layero, index) {
                window.top.layer.close(index_parent);

            },
            btn1: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.addContact(function () {
                    callback();
                    window.top.layer.close(index);
                },index);
            }
        });
    }

</script>
</body>
</html>