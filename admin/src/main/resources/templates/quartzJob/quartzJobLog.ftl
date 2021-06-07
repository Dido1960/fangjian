<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>定时任务管理</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card" style="margin: 15px 0 0 15px; width: calc(100% - 30px)">
            <table class="layui-hide" id="list-quartz-log" lay-filter="list-quartz-log">
                <script type="text/html" id="order-num">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.render();
    });

    layui.use('table', function () {
        var table = layui.table;

        table.render({
            height: 'full-60',
            elem: '#list-quartz-log',
            url: '${ctx}/quartzJob/pagedQuartzJobLog',
            where: {
                jobId: ${RequestParameters.jobId}
            },
            toolbar: '#quartz-toolbar',
            cols: [[
                {field: 'jobName', title: '任务名称'},
                {field: 'jobGroup', title: '任务组'},
                {field: 'jobTime', title: '任务执行时间'},
                {field: 'className', title: '任务执行类'},
            ]],
            page: true
        });
    });
</script>
</body>
</html>

