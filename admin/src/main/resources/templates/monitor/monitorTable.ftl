<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>监控管理</title>
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
    <script type="text/html" id="dep-table-enabled">
        <input type="checkbox" name="enabled" lay-skin="switch" lay-text="启用|禁用" lay-filter="gov-user-table-enabled"
               value="{{ d.enabled }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{ d.enabled== 1
               ? 'checked' : '' }}>
    </script>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card">
            <form class="layui-form">
                <input type="hidden" name="regId" value="${monitor.regId}"/>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">所属地区</label>
                        <div class="layui-input-block">
                            <input type="text" name="regName" value="${monitor.regName}" autocomplete="off"
                                   class="layui-input"
                                   disabled>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">用户名</label>
                        <div class="layui-input-block">
                            <input type="text" name="userName" id="user-name" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">启用状态</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="enabled" name="enabled" id="enabled" please="t"></@WordBookTag>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-input-block">
                            <button type="reset" class="layui-btn layui-btn-primary" id="reset">重置</button>
                            <button type="button" class="layui-btn layui-btn-normal" onclick="startSearch()">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <table class="layui-hide" id="list-dep" lay-filter="operate">
                <script type="text/html" id="orderNum">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
                <script type="text/html" id="dep-toolbar">
                    <button type="button" onclick="addMonitorPage()" class="layui-btn layui-btn-normal layui-btn-sm"><i
                                class="layui-icon"></i> 新增
                    </button>
                    <button type="button" onclick="deleteMonitor()" class="layui-btn layui-btn-primary layui-btn-sm"><i
                                class="layui-icon"></i>删除
                    </button>
                </script>
                <script type="text/html" id="barOperate">
                    <a class="layui-btn layui-btn-sm" lay-event="show">查看</a>
                    <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>
                </script>
            </table>
        </div>
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/javascript">
    // layui-table
    var tableIns;

    /**
     * 重载table
     *
     * @param layer_index 需要关闭的layer
     */
    function renderTable() {
        $("#reset").trigger("click");
        startSearch();
    }

    /**
     * 部门查询
     */
    function startSearch() {
        var userName = $("#user-name").val();
        var enabled = $('#enabled option:selected').val();

        layui.table.reload('list-dep', {
            where: {
                userName: encodeURI(userName),
                enabled: encodeURI(enabled)
            },
            page: {
                curr: 1
            }
        });
    }

    var lock = false;

    /**
     * 添加部门layer
     */
    function addMonitorPage() {
        window.layer.open({
            type: 2,
            title: '新增监控',
            area: ['60%', '50%'],
            btn: ['确认', '取消'],
            offset: 'auto',
            content: '${ctx}/monitor/addMonitorPage?regId=' + '${monitor.regId}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addDepartment(function () {
                    window.layer.close(index);
                    renderTable();
                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

    /**
     * 删除部门信息
     */
    function deleteMonitor() {
        var data = layui.table.checkStatus('list-dep').data;
        var arr = [];
        if (data.length) {
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].id);
            }
            layer.confirm('确认要删除吗？', {
                btn : [ '确定', '取消' ]//按钮
            }, function(index) {
                layer.close(index);
                $.ajax({
                    url: '${ctx}/monitor/deleteMonitor',
                    type: "POST",
                    async: false,
                    traditional: "true",
                    data: {
                        ids: arr
                    },
                    success: function (data) {
                        data = JSON.parse(data);
                        if (data === true) {
                            layer.msg("删除成功！", {icon: 1});
                            renderTable();
                        } else {
                            layer.msg("删除失败！", {icon: 2})
                        }
                    }
                });
            });
        } else {
            window.layer.msg("请至少选择一个！", {icon: 2, time: 2000});
        }
    }
</script>
<script>
    layui.use(['form', 'table', 'layer'], function () {
        var form = layui.form;
        var table = layui.table;

        // layui form 渲染
        form.render();

        tableIns = table.render({
            height: 'full-110',
            elem: '#list-dep',
            url: '${ctx}/monitor/pagedMonitor?regId=' + '${monitor.regId}',
            toolbar: '#dep-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '5%', align: 'center', hide: true}
                , {title: '序号', type: 'numbers', width: '10%'}
                , {field: 'userName', title: '用户名', align: 'center'}
                , {field: 'ip', title: '摄像头IP', align: 'center'}
                , {field: 'port', title: '摄像头端口', align: 'center'}
                , {field: 'status', title: '启用状态', width: 100, templet: '#dep-table-enabled', unresize: true}
                , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barOperate'}
            ]],
            page: true
        });

        /**
         * layui table 工具栏绑定事件
         */
        table.on('tool(operate)', function (obj) {
            var data = obj.data;
            switch (obj.event) {
                case "show":
                    showMonitor(data);
                    break;
                case "edit":
                    updateMonitor(data);
                    break;
            }
        });

        // 监听启用状态
        form.on('switch(gov-user-table-enabled)', function (obj) {
            var json = JSON.parse(decodeURIComponent($(this).data('json')));
            json.enabled = obj.elem.checked ? 1 : 0;
            $.ajax({
                url: '${ctx}/monitor/updateMonitor',
                type: "POST",
                cache: false,
                data:json,
                success: function (data) {
                    if (data) {
                        layer.msg("操作成功!", {icon: 1});
                    } else {
                        layer.msg("操作失败!", {
                            icon: 2, end: function () {
                                table.reload("list-gov-user");
                            }
                        });
                    }
                },
                error: function () {
                    layer.msg("操作失败!", {
                        icon: 2, end: function () {
                            table.reload("list-gov-user");
                        }
                    });
                }
            });

            json = table.clearCacheKey(json);
        });

        // 修改部门
        function updateMonitor(data) {
            window.layer.open({
                type: 2,
                title: '修改监控',
                area: ['60%', '50%'],
                btn: ['确认', '取消'],
                offset: 'auto',
                content: '${ctx}/monitor/updateMonitorPage?id=' + data.id,
                btn1: function (index, layero) {
                    var body = window.layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.updateDepartment(function () {
                        window.layer.close(index);
                        renderTable();

                    });
                },
                btn2: function (index) {
                    window.layer.close(index);
                }
            });
        }

        // 查看部门
        function showMonitor(data) {
            window.layer.open({
                type: 2,
                offset: 'auto',
                title: '查看监控',
                shadeClose: true,
                area: ['60%', '50%'],
                btn: ['取消'],
                content: '${ctx}/monitor/showMonitorPage?id=' + data.id,
                btn1: function (index) {
                    window.layer.close(index);
                }
            });
        }
    });
</script>
</body>
</html>

