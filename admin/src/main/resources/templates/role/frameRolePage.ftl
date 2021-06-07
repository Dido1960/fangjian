<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>角色管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
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
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card" style="margin: 15px 0 0 15px">
            <table class="layui-hide" id="list-role-table" lay-filter="operate"></table>
            <script type="text/html" id="order-num">
                {{d.LAY_TABLE_INDEX+1}}
            </script>
            <script type="text/html" id="role-toolbar">
                <button type="button" onclick="addRolePage()" class="layui-btn layui-btn-normal layui-btn-sm">
                    <i class="layui-icon"></i> 新增
                </button>
                <button type="button" onclick="deleteRole()" class="layui-btn layui-btn-primary layui-btn-sm">
                    <i class="layui-icon"></i>删除
                </button>
            </script>
            <script type="text/html" id="barOperate">
                <a class="layui-btn layui-btn-sm" lay-event="menuPermission">菜单权限</a>
                <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>

            </script>
        </div>
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;

        // layui form 渲染
        form.render();
    });

    // 新增角色弹窗
    var add_role_index;
    // 修改角色弹窗
    var update_role_index;
    // 菜单权限弹窗
    var menu_permission_index;

    var tableIns;
    layui.use('table', function () {
        var table = layui.table;

        tableIns = table.render({
            height: 'full-80',
            elem: '#list-role-table',
            url: '${ctx}/role/pagedRole',
            toolbar: '#role-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', fixed: 'left', hide: true}
                , {title: '序号', type: 'numbers'}
                , {field: 'roleName', align: 'center', title: '角色名称'}
                , {field: 'status', align: 'center', title: '启用状态'}
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
                case "edit":
                    edit(data);
                    break;
                case "menuPermission":
                    menuPermission(data);
                    break;
            }
        })
    });


    //修改角色
    function edit(data) {
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '修改角色',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['修改', '取消'],
            content: '${ctx}/role/updateRolePage?id=' + data.id,
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.updateRole(function () {
                    refresh(index);
                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

    // 菜单权限
    function menuPermission(data) {
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '菜单权限',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['确认', '取消'],
            content: '${ctx}/role/menuPermissionPage?id=' + data.id,
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.menuPermission();
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

    /**
     * 新增角色
     */
    function addRolePage() {
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '新增角色',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['确认', '取消'],
            content: '${ctx}/role/addRolePage',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addRole(function () {
                    refresh(index);
                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        })
    }

    /**
     * 重载table
     *
     * @param layer_index 需要关闭的layer
     */
    function reloadTable() {
        tableIns.reload("list-role-table");
    }

    /**
     * 删除角色
     */
    function deleteRole() {
        var data = layui.table.checkStatus('list-role-table').data;
        var arr = [];
        if (data.length) {
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].id);
            }
            $.ajax({
                url: '${ctx}/role/deleteRole',
                async: false,
                traditional: "true",
                data: {
                    ids: arr
                },
                success: function (data) {
                    data = JSON.parse(data);
                    if (data === true) {
                        tableIns.reload();
                        window.top.layer.msg("删除成功！", {icon: 1, time: 2000});
                    } else {
                        window.top.layer.msg("删除失败！", {icon: 2});
                    }
                }
            })
        } else {
            window.top.layer.msg("请至少选择一个部门！", {icon: 2, time: 2000});
        }
    }
</script>
</body>
</html>

