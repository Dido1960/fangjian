<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>评审回退</title>
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
    <link rel="stylesheet" href="${ctx}/css/evalBack.css">
</head>
<body>
<form class="layui-form" id="backForm" action="#">
    <label for="">回退环节：</label>
    <div class="layui-input-block">
        <select name="step" lay-verify="required" lay-filter="step">
            <option value="">请选择</option>
            <option value="0">评标组长推荐</option>
            <#if bidSection.bidClassifyCode == "A12" && qualification>
                <option value="5">资格审查</option>
            </#if>
            <#if preliminary>
            <option value="1">初步评审</option>
            </#if>
            <#if detailed>
            <option value="2">详细评审</option>
            </#if>
            <#if bidSection.bidClassifyCode == "A12" && calcPriceScore>
                <option value="6">报价得分</option>
            </#if>
            <#if bidSection.bidClassifyCode == "A08" && tenderDoc.mutualSecurityStatus == 1 && other>
                <option value="4">其他评审</option>
            </#if>
        </select>
    </div>
    <label for="">回退原因：</label>
    <textarea name="reason" onblur="checkEvalComments(this)" placeholder="请输入理由" lay-verify="required" maxlength="500"></textarea>
    <div class="layui-input-block" style="display: none">
        <button type="button" lay-submit lay-filter="add" id="submit">提交</button>
    </div>
</form>
<div class="foot">
    <div class="btns">
        <span onclick="backStep();">确定</span>
        <span onclick="closeWindow();">取消</span>
    </div>
</div>
<script>
    var step_name = "";
    var step_value = 0;
    layui.use('form', function () {
        var form = layui.form;
        form.on('select(step)', function(data){
            var value = data.value;
            step_value = value;
            step_name = $(data.elem).find("option[value='" + value + "']").text();
        });

        form.on('submit(add)', function (data) {
            var msg = "确认要申请回退到" + step_name + "环节吗?回退成功后，将移除" + step_name + "环节之后的已评标数据!";
            layerConfirm(msg, function () {
                layerLoading('数据提交中, 请稍候...', null, 0);
                $.ajax({
                    url: '${ctx}/evalBack/addFreeBackApply',
                    type: 'post',
                    cache: false,
                    data: $("#backForm").serialize(),
                    success: function (data) {
                        loadComplete();
                        if(data.code === "1") {
                            layerAlert("评审回退申请成功,请等待审核!", function () {
                                parent.returnUpFlow();
                            }, null, 1);
                        } else {
                            layerAlert("评审回退申请失败!",null, null, 2);
                        }
                    },
                    error: function (data) {
                        loadComplete();
                        console.error(data);
                        layerAlert("评审回退申请失败!",null, null, 2);
                    },
                });
            });
        });
        form.render();
    });

    function backStep() {
        $("#submit").trigger("click");
    }

    function closeWindow() {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    }

    function checkEvalComments(obj){
        var opinion = $(obj).val().trim();
        if(isNull(opinion)){
            $(obj).css('background','#FFE7E7');
            layer.msg("回退原因不能为空哦!");
            return;
        }
        if (opinion.length > 500){
            $(obj).css('background','#FFE7E7');
            layerAlert("输入回退原因过长，系统自动保存前500个字");
            // 防止理由过长，导致异常
            $(obj).val(opinion.substr(0,500))
        }
        $(obj).css('background','#FFFFFF');

    }
</script>
</body>
</html>