<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助开标系统</title>
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
    <link rel="stylesheet" href="${ctx}/css/resume.css">
</head>
<body>
<#if bidSection.bidOpenStatus == 2>
    <div class="big-time">
        <h3>复会时间</h3>
        <div class="layui-inline time-div">
            <!-- 注意：这一层元素并不是必须的 -->
            <input type="text" class="layui-input time-input" id="resumeTime" placeholder="请选择复会时间"
                   value="${bidSection.resumeTime}" readonly>
        </div>
        <div class="over"><a href="javascript:void(0)" onclick="updateResume('${bidSection.id}')">确认</a></div>
    </div>
<#else>
    <div class="big-all">正在开标中...</div>
</#if>
<script>
    var form;
    var laydate;
    layui.use('form', function () {
        form = layui.form;
        laydate = layui.laydate;

        laydate.render({
            elem: '#resumeTime',
            min: '${bidSection.bidOpenEndTime}',
            type: 'datetime',
            change: function (value, date) { //监听日期被切换
            $('.laydate-main-list-0').on('click','td',function () {
                $(".laydate-btns-time").click();
            })
        }
        });
        form.render();
    });

    /**
     * 更新复会时间
     * @param id
     */
    function updateResume(id) {
        $.ajax({
            url: '${ctx}/staff/bidSection/updateBidSection',
            type: 'post',
            cache: false,
            async: true,
            data: {
                "id": id,
                "resumeTime": $("#resumeTime").val()
            },
            success: function (data) {
                if (data) {
                    layer.msg("设置成功！", {icon: 1})
                }
            },
            error: function (data) {
                console.error(data);
            }
        });
    }
</script>


</body>
