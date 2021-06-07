<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>标书拒绝</title>
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
<div class="tan layui-form layui-form-item layui-form-text" style="z-index: 99999;">
    <div class="layui-input-block">
        <textarea name="desc" maxlength="100" placeholder="请输入内容" class="layui-textarea" id="refuse-text"
                  style="height: 160px;margin: 40px 0 0 -50px;" <#if flag = 1>readonly</#if> >${bidderOpenInfo.tenderRejectionReason}</textarea>
    </div>
</div>


<script>
    /**
     * 修改当前投标人的标段信息
     * @param bidderId 投标人主键
     */
    var successFun = null;
    function update(reasonText) {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateBidderOpenInfo',
            type: 'post',
            cache: false,
            async: false,
            data: {
                // 标段主键
                'bidSectionId': ${bidderOpenInfo.bidSectionId},
                // 投标人主键
                'bidderId':  ${bidderOpenInfo.bidderId},
                // 标书拒绝标识 1
                'tenderRejection': 1,
                // 拒绝理由
                'tenderRejectionReason': reasonText
            },
            success: function (data) {
                loadComplete();
                if (data){
                    successFun();
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    function submitReason(func) {
        successFun = func;
        var reason = $("#refuse-text").val().trim();
        if (isNull(reason)){
            layer.msg("请填写拒绝理由!", {icon: 2});
            return false;
        }
        update(reason);
    }
</script>
</body>
</html>