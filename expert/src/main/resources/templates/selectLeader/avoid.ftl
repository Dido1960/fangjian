<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>回避原因</title>
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
    <script src="${ctx}/js/convertMoney.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>

    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/css/avoid.css">
</head>
<body>
<form action="" class="layui-form">
    <div class="layui-input-block">
        <input type="checkbox" name="avoidReason" title="本人是投标人或者投标人主要负责人的近亲属" lay-skin="primary">
    </div>
    <div class="layui-input-block">
        <input type="checkbox" name="avoidReason" title="本人是项目主管部门或者行政监督部门的人员" lay-skin="primary">
    </div>
    <div class="layui-input-block">
        <input type="checkbox" name="avoidReason" title="本人与投标人有经济利益关系，可能会影响对投标公正评审" lay-skin="primary">
    </div>
    <div class="layui-input-block">
        <input type="checkbox" name="avoidReason" title="本人曾因在招标，评标以及其他与招标投标有关活动中从事违法行为而受过行政处罚或刑事处罚" lay-skin="primary">
    </div>
    <label for="">其他原因</label>
    <textarea class="reason otherReason" onblur="checkReason(this)"  placeholder="请输入其他原因" maxlength="200"></textarea>
</form>
<div class="foot">
    <div class="btns">
        <span onclick="avoidConfirm()">确定</span>
        <span onclick="parent.layer.closeAll()">取消</span>
    </div>
</div>
</body>
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<script>
    /**
     * 确认回避
     */
    function avoidConfirm() {
        var reason = $('.otherReason').val().trim();
        var selectedItems = $("input[name=avoidReason]:checked");
        if (isNull(reason)){
            layer.msg("回避原因不能为空哦!");
            return;
        }
        if (reason.length>255){
            layerAlert("输入回避原因过长，系统自动保存前255个字");
            // 防止理由过长，导致异常
            reason = reason.substr(0,255);
        }
        selectedItems.each(function (index, item) {
            reason += ";" + $(item).attr("title");
        });
        layerConfirm("是否确认回避？回避后将不能再参与此项目的评审！", function (index) {
            window.top.layer.load();
            $.ajax({
                url: '${ctx}/expert/updateExpertUser',
                type: 'post',
                cache: false,
                data: {
                    avoid: "1",
                    reason: reason
                },
                success: function (data) {
                    if (data) {
                        layer.msg("回避成功", {icon: 1});
                        parent.window.location.href = "${ctx}/login.html";
                    } else {
                        layer.msg("操作失败", {icon: 5});
                        setTimeout(function () {
                            parent.layer.closeAll();
                        }, 1500);
                    }
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("操作失败", {icon: 5});
                    setTimeout(function () {
                        parent.layer.closeAll();
                    }, 1500);
                },
            });
        });
    }

    /**
     * 限制输入的长度
     * @param obj
     */
    function checkReason(obj){
        var opinion = $(obj).val().trim();
        if(isNull(opinion)){
            $(obj).css('background','#FFE7E7');
            layer.msg("回避原因，不能为空哦!");
            return;
        }
        if (opinion.length > 200){
            $(obj).css('background','#FFE7E7');
            layerAlert("输入理由过长，系统自动保存前200个字");
            // 防止理由过长，导致异常
            $(obj).val(opinion.substr(0,200))
        }
        $(obj).css('background','#FFFFFF');

    }


</script>

</html>

