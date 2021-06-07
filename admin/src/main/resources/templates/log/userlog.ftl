<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>用户日志</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card" style="margin: 15px 0 0 15px; width: calc(100% - 30px)">
            <form class="layui-form" style="padding-top: 15px;">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">日志时限</label>
                        <div class="layui-input-block">
                            <input type="text" name="searchTime" id="search-time" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">用户名</label>
                        <div class="layui-input-block">
                            <input type="text" name="username" id="user-name" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">日志内容</label>
                        <div class="layui-input-block">
                            <input type="text" name="content" id="content" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">操作类型</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="dmlType" name="dmlType" id="dml-type" please="t"></@WordBookTag>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-input-block">
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            <button type="button" class="layui-btn layui-btn-normal" onclick="startSearch()">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <table class="layui-hide" id="user-log-table" style="margin-left: 15px">
                <script type="text/html" id="orderNum">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
            </table>
        </div>
    </div>
</div>
<script>
    /**
     * 用户日志查询
     */
    function startSearch() {
        var username = $("#user-name").val();
        var search_time = $("#search-time").val();
        var content = $("#content").val();
        var dmlType = $('#dml-type option:selected').val();

        layui.table.reload('user-log-table', {
            where: {
                username: username,
                searchTime: search_time,
                content: content,
                dmlType: dmlType
            }
            , page: {
                curr: 1
            }
        });
    }

</script>
<script type="text/javascript">
    /**
     * 初始化layui
     */
    layui.use(['form', 'table', 'layer', 'laydate'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;
        var laydate = layui.laydate;

        // 执行一个laydate实例
        laydate.render({
            elem: '#search-time',
            range: '~',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime'
        });

        // layui form 渲染
        form.render();

        // 创建静态表格实例
        table.render({
            id: "user-log-table",
            elem: '#user-log-table',
            height: 'full-108',
            url: '${ctx}/log/pagedUser',
            page: true,
            toolbar: true,
            cols: [[
                {title: '序号', type: 'numbers'},
                {field: 'userId', title: '用户主键', align: 'center'},
                {field: 'username', title: '用户名', align: 'center'},
                {field: 'dmlType', title: '操作类型', align: 'center'},
                {field: 'content', title: '日志内容', align: 'center'},
                {field: 'insertTime', title: '创建时间', align: 'center'}
            ]]
        });

    });
</script>
</body>
</html>