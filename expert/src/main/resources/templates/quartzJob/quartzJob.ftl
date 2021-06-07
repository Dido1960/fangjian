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
            <form class="layui-form" style="padding-top: 15px;">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">任务名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" id="name" autocomplete="off" class="layui-input">
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
            <table class="layui-hide" id="list-quartz" lay-filter="list-quartz">
                <script type="text/html" id="order-num">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
                <script type="text/html" id="quartz-toolbar">
                    <button type="button" onclick="add()" class="layui-btn layui-btn-normal layui-btn-sm"><i class="layui-icon"></i>新增</button>
                </script>
                <script type="text/html" id="quartz-bar">
                    <a class="layui-btn layui-btn-sm" lay-event="trigger">执行</a>
                    <a class="layui-btn layui-btn-sm" lay-event="paused">暂停</a>
                    <a class="layui-btn layui-btn-sm" lay-event="resume">恢复</a>
                    <a class="layui-btn layui-btn-sm" lay-event="update">修改</a>
                    <a class="layui-btn layui-btn-sm" lay-event="remove">移除</a>
                    <a class="layui-btn layui-btn-sm" lay-event="log">日志</a>
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
            elem: '#list-quartz',
            url: '${ctx}/quartzJob/pagedQuartzJob',
            toolbar: '#quartz-toolbar',
            cols: [[
                {field: 'name', title: '任务名称'},
                {field: 'jobGroup', title: '任务组'},
                {field: 'description', title: '任务描述'},
                {field: 'jobClassName', title: '任务执行类'},
                {field: 'triggerState', title: '触发器状态'},
                {field: 'cronExpression', title: '任务触发规则'},
                {fixed: 'right', width: '400', title: '操作', toolbar: '#quartz-bar'}
            ]],
            page: true
        });

        // layui table 工具栏事件监听
        table.on('tool(list-quartz)', function (obj) {
            var data = obj.data;
            switch (obj.event) {
                case 'trigger':
                    trigger(data.id);
                    break;
                case 'paused':
                    paused(data.id);
                    break;
                case 'resume':
                    resume(data.id);
                    break;
                case 'update':
                    update(data.id);
                    break;
                case 'remove':
                    remove(data.id);
                    break;
                case 'log':
                    log(data.id);
                    break;
                default:
                    break;
            }
        });
    });
</script>
<script>
    /**
     * 模糊查询
     */
    function startSearch() {
        var name = $("#name").val();
        layui.table.reload("list-quartz", {
            where: {
                name: name,
            }
            , page: {
                curr: 1
            }
        })
    }

    /**
     * 新增定时任务
     */
    function add() {
        layer.open({
            type: 2,
            title: "新增定时任务",
            content: "${ctx}/quartzJob/addQuartzJobPage",
            offset: 'r',
            area: ['40%', '100%'],
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                // 点击确认的回调函数
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addQuartzJob();
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.msg("已取消");
                layer.close(index);
            }
        })
    }

    /**
     * 执行定时任务
     *
     * @param id 定时任务主键
     */
    function trigger(id) {
        layer.confirm('确认立即执行该任务吗?', {
            icon: 3,
            title: '提示'
        }, function (index) {
            // 确认的回调函数
            layer.close(index);
            $.ajax({
                url: "${ctx}/quartzJob/triggerQuartzJob",
                type: "POST",
                data: {
                    id: id
                },
                success: function (data) {
                    if (data) {
                        layer.msg("执行成功!", {icon: 1});
                    } else {
                        layer.msg("执行失败!", {icon: 2});
                    }
                }
            })
        }, function (index) {
            // 取消的回调函数
            layer.msg("已取消!");
            layer.close(index);
        });
    }

    /**
     * 暂停定时任务
     *
     * @param id 定时任务主键
     */
    function paused(id) {
        layer.confirm('确认暂停该任务吗?', {
            icon: 3,
            title: '提示'
        }, function (index) {
            // 确认的回调函数
            layer.close(index);
            $.ajax({
                url: "${ctx}/quartzJob/pausedQuartzJob",
                type: "POST",
                data: {
                    id: id
                },
                success: function (data) {
                    if (data) {
                        layui.table.reload("list-quartz");
                        layer.msg("暂停成功!", {icon: 1});
                    } else {
                        layer.msg("暂停失败!", {icon: 2});
                    }
                }
            })
        }, function (index) {
            // 取消的回调函数
            layer.msg("已取消!");
            layer.close(index);
        });
    }

    /**
     * 恢复定时任务
     *
     * @param id 定时任务主键
     */
    function resume(id) {
        layer.confirm('确认恢复该任务吗?', {
            icon: 3,
            title: '提示'
        }, function (index) {
            layer.close(index);
            $.ajax({
                url: "${ctx}/quartzJob/resumeQuartzJob",
                type: "POST",
                data: {
                    id: id
                },
                success: function (data) {
                    if (data) {
                        layui.table.reload("list-quartz");
                        layer.msg("恢复成功!", {icon: 1});
                    } else {
                        layer.msg("恢复失败!", {icon: 2});
                    }
                }
            })
        }, function (index) {
            // 取消的回调函数
            layer.msg("已取消!");
            layer.close(index);
        });
    }

    /**
     * 修改定时任务
     *
     * @param id 定时任务主键
     */
    function update(id) {
        layer.open({
            type: 2,
            title: "修改定时任务",
            content: "${ctx}/quartzJob/updateQuartzJobPage?id=" + id,
            offset: 'r',
            area: ['40%', '100%'],
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                // 点击确认的回调函数
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.updateQuartzJob();
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.msg("已取消");
                layer.close(index);
            }
        })
    }

    /**
     * 移除定时任务
     *
     * @param id 定时任务主键
     */
    function remove(id) {
        layer.confirm('确认移除该任务吗?', {
            icon: 3,
            title: '提示'
        }, function (index) {
            layer.close(index);
            $.ajax({
                url: "${ctx}/quartzJob/delQuartzJob",
                type: "POST",
                data: {
                    id: id
                },
                success: function (data) {
                    if (data) {
                        layui.table.reload("list-quartz");
                        layer.msg("移除成功!", {icon: 1});
                    } else {
                        layer.msg("移除失败!", {icon: 2});
                    }
                }
            })
        }, function (index) {
            // 取消的回调函数
            layer.msg("已取消!");
            layer.close(index);
        });
    }

    function log(id) {
        layer.open({
            type: 2,
            title: "定时任务执行日志",
            content: "${ctx}/quartzJob/quartzJobLogPage?jobId=" + id,
            area: ['90%', '100%'],
            offset: 'r',
            btn: false,
        })
    }
</script>
</body>
</html>

