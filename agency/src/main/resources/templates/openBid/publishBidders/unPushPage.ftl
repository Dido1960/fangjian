<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省市政房建电子开标辅助系统</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/publishBidder.css">

</head>
<body>
<div class="tan" style="z-index: 99999;">
    <form action="">
        <label><input type="radio" name="why" value="1">迟到</label>
        <label><input type="radio" name="why" value="2">弃标</label>
        <label><input type="radio" name="why" value="9" checked>其他</label>
    </form>
    <div class="foot">
        <span class="yes" onclick="unPushSureBtn()">确定</span>
        <span class="no" onclick="parent.layer.closeAll();">取消</span>
    </div>
</div>

<script>
    /**
     * 未递交确认操作
     */
    function unPushSureBtn() {
        layer.closeAll();
        var notCheckin = $('input[type="radio"]:checked').val();
        if ($('input:radio[value="9"]:checked').val()) {
            window.top.layer.prompt({
                formType: 2,
                title: ['其它原因', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                area: ['400px', '150px'],
                btn: ['确认', '取消'],
                btnAlign: 'c',
                // 其他原因字符限制100字
                maxlength: 100,
            }, function (value, index) {
                layer.close(index)
                sureOrRevokePush('${boiId}', notCheckin, value, 0);
            });
        } else {
            sureOrRevokePush('${boiId}', notCheckin, null, 0);
        }
    }

    /**
     *   保存未递交原因
     *   @param id 开标记录表id
     *   @param notCheckin 未递交状态
     *   @param value 未递交原因
     *   @param isPassBidOpen 是否通过开标
     */
    function sureOrRevokePush(id, notCheckin, value, isPassBidOpen) {
        var index = layer.msg("数据加载中...", {icon: 16, time: 0, shade: 0.3});
        var flag = false;
        $.ajax({
            url: '${ctx}/staff/updateBidderInfo',
            type: 'POST',
            cache: false,
            async: false,//这里得用同步
            data: {
                id: id,
                notCheckin: notCheckin,
                notCheckinReason: value,
                isPassBidOpen: isPassBidOpen,
            },
            success: function (data) {
                flag = data;
                if (data) {
                    layer.msg("操作成功！", {icon: 1});
                    setTimeout(function () {
                        parent.window.location.reload();
                    },1000);
                } else {
                    layer.msg("操作失败！", {icon: 5});
                }
                layer.close(index)
            },
            error: function (data) {
                layer.close(index);
                layer.msg("操作失败！", {icon: 5});
                console.error(data);
                if(data.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }

            },
        });

        return flag;

    }

</script>
</body>
</html>