<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>跟标记录维护</title>
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
    <style>
        .layui-form-label {
            width: 90px !important;
        }
    </style>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend style="font-weight: bold">跟标记录维护</legend>
</fieldset>
<form class="layui-form" method="post" id="record-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" value="${record.id}">
    <input type="hidden" name="bidSectionId" value="${record.bidSectionId}">
    <input type="hidden" name="operationMaintenanceId" value="${record.operationMaintenanceId}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">跟标人员名称</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 600px">
            <input type="text" name="operationMaintenanceName" id="operation-maintenance-name" lay-verify="required"
                   class="layui-input" placeholder="请输入跟标人员名称" value="${record.operationMaintenanceName}" readonly>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">是否异常</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 600px">
            <select name="abnormal" id="abnormal" lay-verify="required" lay-search="" lay-filter="abnormal">
                <option value="1" <#if !record.abnormal?? || (record.abnormal?? && record.abnormal == '1')>selected</#if>>正常</option>
                <option value="0" <#if record.abnormal?? && record.abnormal == '0'>selected</#if>>异常</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item" id="div-abnormal-time" style="margin-top: 20px;
            <#if !record.abnormal?? || (record.abnormal?? && record.abnormal == '1')>display: none</#if>">
        <label class="layui-form-label">异常处理时长</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 600px">
            <input type="text" name="abnormalTime" id="abnormal-time" lay-verify="required"
                   class="layui-input" placeholder="请输入异常处理时长（分钟）" value="${record.abnormalTime!0}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">跟标记录</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 600px">
            <textarea type="text" name="remark" id="remark" style="height: 100px;resize: none;" lay-verify="required"
                       autocomplete="off" class="layui-input" placeholder="请输入跟踪记录">${record.remark}</textarea>
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
<script type="text/javascript">
    var successFunc;
    var lock = false;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;

        form.on('select(abnormal)', function (data) {
            console.log(data.value)
            if (data.value == '0') {
                $("#abnormal-time").val("");
                $("#div-abnormal-time").fadeIn();
            } else {
                $("#abnormal-time").val("0");
                $("#div-abnormal-time").fadeOut();
            }
            form.render();
        });

        form.on('submit(*)', function (data) {
            if (lock) {
                return false;
            }
            lock = true;
            $.ajax({
                url: '${ctx}/projectInfo/saveOrUpdateOperationMaintenanceInfo',
                type: 'post',
                cache: false,
                async: false,
                data: $("#record-form").serialize(),
                success: function (data) {
                    layer.msg("操作成功！",successFunc());
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("操作失败！");
                },
            });
        });
        form.render();
    });

    /**
     * 修改或新增运维跟踪记录
     *
     * @param successFunc1 成功回调函数
     */
    function saveOrUpdateOperationMaintenanceInfo(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }
</script>
</body>
</html>