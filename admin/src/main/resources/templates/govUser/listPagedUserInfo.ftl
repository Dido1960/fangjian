<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>人员管理</title>
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
        <div class="layui-card">
            <form class="layui-form">
                <div class="layui-form-item">
                    <div class="layui-inline" style="margin-left: -40px">
                        <label class="layui-form-label">姓名</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" id="name" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">启用状态</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="enabled" name="enabled" id="enabled" please="t"></@WordBookTag>
                        </div>
                    </div>
                    <div class="layui-inline" style="margin-left: -50px">
                        <div class="layui-input-block">
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            <button type="button" class="layui-btn layui-btn-normal" onclick="startSearch()">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <table class="layui-hide" id="list-user" lay-filter="operate"></table>
        </div>
    </div>
</div>


<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/html" id="orderNum">
    {{d.LAY_TABLE_INDEX+1}}
</script>
<script type="text/html" id="user-toolbar">
    <button type="button" onclick="addUserPage()" class="layui-btn layui-btn-normal layui-btn-sm"><i class="layui-icon"></i>
        新增
    </button>
    <button type="button" onclick="deleteUser()" class="layui-btn layui-btn-primary layui-btn-sm"><i class="layui-icon"></i>删除
    </button>
</script>
<script type="text/html" id="barOperate">
    <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>
    <a class="layui-btn layui-btn-sm" lay-event="show">查看</a>
    <a class="layui-btn layui-btn-sm" onclick="bindCa('{{d.id}}','1')">绑定CA</a>
</script>
<script type="text/html" id="userInfo-table-enabled">
    <input type="checkbox" name="enabled" lay-skin="switch" lay-text="启用|禁用" lay-filter="gov-user-table-enabled"
           value="{{ d.enabled }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{ d.enabled== 1
           ? 'checked' : '' }}>
</script>
<script type="text/javascript">

    var tableIns;

    /**
     * 重载table刷新
     *
     * @param layer_index 需要关闭的layer
     */
    function reloadTable() {
        tableIns.reload("list-user");
    }

    layui.use(['form', 'table', 'layer'], function () {
        var form = layui.form;
        var table = layui.table;

        form.render();

        tableIns = table.render({
            height: 'full-60'
            , elem: '#list-user'
            , url: '${ctx}/govUser/pagedUser?regId=' + '${regId}'
            , toolbar: '#user-toolbar'
            , cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '0%', hide: true}
                , {title: '序号', type: 'numbers', width: '5%'}
                , {field: 'loginName', title: '登录名', align: "center"}
                , {field: 'name', title: '用户名称', align: "center"}
                , {field: 'enabledName', title: '启用状态', templet: '#userInfo-table-enabled'}
                , {field: 'phone', title: '手机号码', align: "center"}
                , {fixed: 'right', width: '25%', title: '操作', align: 'center', toolbar: '#barOperate'}
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
                case "show":
                    show(data);
                    break;
            }
        });

        // 监听启用状态
        form.on('switch(gov-user-table-enabled)', function (obj) {
            var json = JSON.parse(decodeURIComponent($(this).data('json')));
            $.ajax({
                url: '${ctx}/govUser/updateUser',
                type: "POST",
                cache: false,
                data: {
                    id: json.id,
                    enabled: json.enabled
                },
                success: function (data) {
                    if (data) {
                        layer.msg("操作成功!", {icon: 1});
                    } else {
                        layer.msg("操作失败!", {
                            icon: 2, end: function () {
                                table.reload("list-user");
                            }
                        });
                    }
                },
                error: function () {
                    layer.msg("操作失败!", {
                        icon: 2, end: function () {
                            table.reload("list-user");
                        }
                    });
                }
            });

            json = table.clearCacheKey(json);
        });

    });

    // 修改人员
    function edit(data) {
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '修改人员',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['修改', '取消'],
            content: '${ctx}/govUser/updateUserPage?id=' + data.id,
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.updateUser(function () {
                    window.layer.close(index);
                    reloadTable();
                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

    // 人员查看
    function show(data) {
        window.layer.open({
            type: 2,
            title: '人员查看',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['关闭'],
            content: '${ctx}/govUser/showUserPage?id=' + data.id,
            btn1: function (index) {
                window.layer.close(index);
            }
        });
    }

    /**
     * 人员查询
     */
    function startSearch() {
        var name = $("#name").val();
        var enabled = $('#enabled option:selected').val();
        layui.table.reload("list-user", {
            where: {
                name: name,
                enabled: enabled
            }
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        })
    }

    /**
     * 添加人员layer
     */
    function addUserPage() {
        window.layer.open({
            type: 2,
            title: '添加角色',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['确认', '取消'],
            content: '${ctx}/govUser/addUserPage?regId=' + '${regId}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addUser(function () {
                    window.layer.close(index);
                    reloadTable(index);
                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

    /**
     * 删除人员信息
     */
    function deleteUser() {
        var data = layui.table.checkStatus('list-user').data;
        var arr = [];
        if (data.length) {
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].id);
            }
            $.ajax({
                url: '${ctx}/govUser/deleteUser',
                async: false,
                traditional: "true",
                data: {
                    ids: arr
                },
                success: function (data) {
                    data = JSON.parse(data);
                    if (data) {
                        window.top.layer.msg("删除成功！", {icon: 1, time: 2000});
                        tableIns.reload();
                    } else {
                        window.top.layer.msg("删除失败！", {icon: 2});
                    }
                }
            })
        } else {
            window.top.layer.msg("请至少选择一个人员！", {icon: 2, time: 2000});
        }
    }

    /**
     * @param userId 用户Id
     * @param userType 用户类型
     * @return
     * @author lesgod
     * @date 2020/5/12 10:09
     */
    function bindCa(userId, userType) {
        window.location.href = '${ctx}/admin/userCert/listUserCertPage?userId=' + userId + "&type=" + userType + "&redirectPage=" + encodeURIComponent(window.location.href);
        //信息
        <#--window.top.layer.open({-->
        <#--    type: 2,-->
        <#--    offset: 'r',-->
        <#--    title: 'CA绑定查看',-->
        <#--    shadeClose: true,-->
        <#--    area: ['45%', '100%'],-->
        <#--    btn: ['确认', '取消'],-->
        <#--    content: "${ctx}/admin/companyUser/updateCompanyUserPage?id=" + id,-->
        <#--    btn1: function (index, layero) {-->
        <#--        window.top.layer.close(index);-->
        <#--    },-->
        <#--    btn2: function (index) {-->

        <#--    }-->
        <#--});-->

    }

</script>
</body>
</html>

