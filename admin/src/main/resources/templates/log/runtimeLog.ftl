<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>运行日志</title>
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
    <script src="${ctx}/js/util.js"></script>
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
                            <input type="text" name="searchTime" id="seach-time" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">日志级别</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="logLevel" name="logLevel" id="log-level" please="t"></@WordBookTag>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-input-block">
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            <button type="button" class="layui-btn layui-btn-normal" onclick="startSearch()">查询</button>
                        </div>
                    </div>
                </div>
                <#--                <div class="layui-form-item">-->
                <#--                    <div class="layui-inline">-->
                <#--                        <label class="layui-form-label">持久化数据</label>-->
                <#--                        <div class="layui-input-block">-->
                <#--                            <@WordBookTag key="timeDiff" name="timeDiff" id="time-diff" please="t"></@WordBookTag>-->
                <#--                        </div>-->
                <#--                    </div>-->
                <#--                    <div class="layui-inline">-->
                <#--                        <div class="layui-btn-group">-->
                <#--                            <button type="button" id="question" onclick="questionMark()" class="layui-btn layui-btn-primary layui-btn-sm"><i class="layui-icon layui-icon-help"></i></button>-->
                <#--                            <button type="button" id="del-runtime-log" onclick="delRuntimeLog()" class="layui-btn layui-btn-primary layui-btn-sm"><i class="layui-icon layui-icon-delete"></i></button>-->
                <#--                        </div>-->
                <#--                    </div>-->
                <#--                </div>-->
            </form>
            <table class="layui-hide" id="runtime-log-table">
                <script type="text/html" id="order_num">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
            </table>

        </div>
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script>
    /**
     * 日志查询
     */
    function startSearch() {
        var search_time = $("#seach-time").val(),
            log_level = $("#log-level").val();

        layui.table.reload("runtime-log-table", {
            where: {
                searchTime: search_time,
                logLevel: log_level
            }
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        })
    };

    // 删除运行日志
    function delRuntimeLog() {
        var time_diff = $('#time-diff option:selected').val();
        if (time_diff) {
            layer.msg('此操作较为耗时,请耐心等待!', {icon: 7}, function () {
                alert(2)
            });
            $.ajax({
                url: '${ctx}/log/delRuntimeLog',
                type: "POST",
                async: false,
                traditional: "true",
                data: {
                    timeDiff: time_diff
                },
                success: function (data) {
                    data = JSON.parse(data);
                    if (data === true) {
                        window.top.layer.msg("删除成功！", {
                            icon: 1, time: 2000, end: function () {
                                //页面重载
                                window.location.reload();
                            }
                        });
                    } else {
                        window.top.layer.msg("删除失败！", {icon: 2});
                    }
                }
            })
        } else {
            window.top.layer.msg("请选择持久化时间！", {icon: 2, time: 2000});
        }
    }

    // 持久化数据的解释
    function questionMark() {
        layui.use('layer', function () {
            var layer = layui.layer;

            layer.tips('持久化数据会删除数据库记录，保存至本地磁盘。', '#question', {
                tips: [1, '#42adea'],
                time: 5000
            });
        });

    }
</script>
<script type="text/javascript">
    // layui-table
    var tableIns;

    layui.use(['form', 'table', 'layer', 'laydate', 'layer'], function () {
        var form = layui.form;
        var table = layui.table;
        var laydate = layui.laydate;

        //执行一个laydate实例
        laydate.render({
            elem: '#seach-time',
            range: '~',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime'
        });

        // layui form 渲染
        form.render();

        tableIns = table.render({
            height: 'full-110',
            elem: '#runtime-log-table',
            url: '${ctx}/log/pagedRuntimeLog',
            toolbar: true,
            cols: [[
                {field: 'id', title: 'ID', align: 'center', hide: true}
                , {title: '序号', type: 'numbers'}
                , {
                    field: 'timestmp', title: '创建时间', align: 'center',
                    templet: function (row) {
                        return timestampToDateTime(row.timestmp);
                    }
                }
                , {field: 'formattedMessage', title: '异常消息', align: 'center'}
                , {field: 'loggerName', title: '日志名称', align: 'center'}
                , {field: 'levelString', title: '日志级别', align: 'center'}
                , {field: 'threadName', title: '线程名', align: 'center'}
                , {field: 'callerFilename', title: '发出日志的文件名', align: 'center'}
                , {field: 'callerClass', title: '发出日志的类', align: 'center'}
                , {field: 'callerMethod', title: '发出日志的方法', align: 'center'}
                , {field: 'callerLine', title: '发出日志的行', align: 'center'}
            ]],
            page: true
        });

    });
</script>
</body>
</html>

