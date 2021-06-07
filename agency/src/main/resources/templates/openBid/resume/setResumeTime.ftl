<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<script src="${ctx}/js/common.js"></script>
<style>
    html,body{
        overflow: hidden;
    }
    .tan h3 {
        width: 100%;
        height: 30px;
        font-size: 20px;
        font-family: Microsoft YaHei;
        font-weight: 900;
        line-height: 30px;
        text-align: center;
        color: rgba(34, 49, 101, 1);
        margin-top: 80px;
    }
    .time-div {
        font-weight: 900;
    }

    #resumeTime {
        width: 75%;
        height: 40px;
        background: rgba(255, 255, 255, 1);
        opacity: 1;
        float: right;
    }
    .cont{
        width: 50%;
        height: 40px;
        margin: 1% auto;
        line-height: 40px;
        font-size: 16px;
    }
</style>
<div class="time-div">
    <div class="cont">
        复会时间 <input type="text" class="layui-input" id="resumeTime" placeholder="请选择复会时间" value="${bidSection.resumeTime}" readonly />
    </div>
</div>
<script>
    layui.use('form', function () {
        var form = layui.form;
        var laydate = layui.laydate;
        $(this).removeAttr("lay-key");
        laydate.render({
            elem: '#resumeTime',
            min: '${bidSection.resumeTime}',
            type: 'datetime',
            trigger : 'click'
        });
        form.render();
    });

    /**
     * 更新复会时间
     */
    function updateResume(callBack) {
        var resumeTime = $("#resumeTime").val();
        $.ajax({
            url: '${ctx}/staff/bidSection/updateBidSection',
            type: 'post',
            cache: false,
            async: false,
            data: {
                "id": '${bidSection.id}',
                "resumeTime": resumeTime
            },
            success: function (data) {
                if (data) {
                    callBack(data);
                }
            },
            error: function (data) {
                console.log(data);
            }
        });
    }
</script>

