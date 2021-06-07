<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加定时任务</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
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
    <legend>添加定时任务</legend>
</fieldset>
<form class="layui-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">任务名称</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="name" id="name" lay-verify="required|job" autocomplete="off" placeholder="请输入任务名称" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">任务组</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="jobGroup" id="job-group" lay-verify="required|job" placeholder="请输入任务组" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">任务描述</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="description" id="description" lay-verify="required" placeholder="请输入任务描述" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">任务执行类</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <select name="jobClassName" id="job-class-name" lay-verify="required" lay-search="">
                <option value="">请直接选择或搜索</option>
                <#if quartzJobsList??>
                    <#list quartzJobsList as job>
                        <option value="${job.clazz.name}">${job.clazz.name}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">触发规则</label>
        <div class="layui-input-block" style="margin-left: 120px">
            <input type="text" name="cronExpression" id="cron-expression" lay-verify="required" placeholder="请输入触发规则" autocomplete="off" class="layui-input">
        </div>
    </div>
    <button type="submit" class="layui-btn layui-hide" lay-submit="" lay-filter="*" id="submit-btn"></button>
</form>
<script>
    function addQuartzJob() {
        $("#submit-btn").click();
    }
</script>
<script type="text/javascript">
    layui.use('form', function () {
        var form = layui.form;
        form.render();

        form.verify({
            job: function(value, item){
                var msg;
                var name = $("#name").val();
                var job_group = $("#job-group").val();
                if(name && job_group) {
                    $.ajax({
                        url: "${ctx}/quartzJob/validQuartzJob",
                        async: false,
                        data: {
                            name: name,
                            jobGroup: job_group
                        },
                        success: function (data) {
                            if (data) {
                                msg = "该定时任务已存在!";
                            }
                        }
                    });

                    if (msg) {
                        return msg;
                    }
                }
            }
        });

        // 监听submit事件
        form.on('submit(*)', function (data) {
            $.ajax({
                url: "${ctx}/quartzJob/addQuartzJob",
                type: "POST",
                async: false,
                data: data.field,
                success: function (data) {
                    if (data) {
                        parent.layui.table.reload("list-quartz");
                        layer.msg("添加成功!", {
                            icon: 1, end: function () {
                                parent.layer.closeAll();
                            }
                        })
                    } else {
                        layer.msg("添加失败!", {icon: 2});
                    }
                }
            });

            return false;
        });
    });
</script>
</body>
</html>