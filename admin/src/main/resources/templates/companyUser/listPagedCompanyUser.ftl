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
<div id="LAY_app" style="margin: 10px">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card">
            <form class="layui-form" id="seach-Form">
                <div class="layui-form-item" style="padding: 5px">
                    <div class="layui-inline" style="margin-left: -40px">
                        <label class="layui-form-label">企业名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline" style="margin-left: -40px">
                        <label class="layui-form-label">信用代码</label>
                        <div class="layui-input-block">
                            <input type="text" name="code" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">启用状态</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="enabled" name="enabled"  please="t"></@WordBookTag>
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
    <button type="button" onclick="addCompanyUserPage()" class="layui-btn layui-btn-normal layui-btn-sm"><i
                class="layui-icon"></i>
        新增
    </button>
    <button type="button" onclick="deleteUser()" class="layui-btn layui-btn-primary layui-btn-sm"><i class="layui-icon"></i>删除
    </button>
</script>
<script type="text/html" id="barOperate">
    {{#  if(d.enabled === 1){ }}
    <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>
    <a class="layui-btn layui-btn-sm" lay-event="show">查看</a>
    <a class="layui-btn layui-btn-sm" lay-event="bindingCA">绑定CA</a>
    {{#  } else if(d.enabled === 0){ }}
    <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>
    <a class="layui-btn layui-btn-sm" lay-event="show">查看</a>
    <a class="layui-btn layui-btn-sm" lay-event="bindingCA">绑定CA</a>
    {{#  } }}
    <a class="layui-btn layui-btn-sm" lay-event="resetPassword">密码重置</a>
</script>
<script type="text/html" id="api-auth-table-enabled">
    <input type="checkbox" name="enabled" lay-skin="switch" lay-text="启用|禁用" lay-filter="api-auth-table-enabled"
           value="{{ d.enabled }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{ d.enabled== 1
           ? 'checked' : '' }}>
</script>
<script type="text/javascript">


    layui.use(['form', 'layer', 'table'], function () {
        var table = layui.table;
        var form = layui.form;

        tableIns = table.render({
            height: 'full-100'
            , elem: '#list-user'
            , url: '${ctx}/admin/companyUser/pagedCompanyUser'
            , toolbar: '#user-toolbar'
            , cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '0%', hide: true}
                , {title: '序号', type: 'numbers', width: '5%'}
                , {field: 'loginName', title: '登录名', align: "center"}
                , {field: 'name', title: '用户名称', align: "center"}
                , {field: 'code', title: '组织机构代码', align: "center"}
                , {field: 'linkMan', title: '联系人', align: "center"}
                , {field: 'phone', title: '手机号码', align: "center"}

                , {field: 'enabled', title: '启用状态', align: "center", templet: '#api-auth-table-enabled'}
                , {field: 'certCount', title: '申办次数', align: "center"}
                , {fixed: 'right', width: '25%', title: '操作', align: 'center', toolbar: '#barOperate'}
            ]],
            page: true
        });

        /**
         * layui table 工具栏绑定事件
         */
        table.on('tool(operate)', function (obj) {
            var data = obj.data;
            if (obj.event === 'edit') {
                window.top.layer.open({
                    type: 2,
                    offset: 'r',
                    title: '修改人员',
                    shadeClose: true,
                    area: ['45%', '100%'],
                    btn: ['修改', '取消'],
                    content: '${ctx}/user/updateUserPage?id=' + data.id,
                    btn1: function (index, layero) {
                        var body = window.top.layer.getChildFrame('body', index);
                        var iframeWin = window.top[layero.find('iframe')[0]['name']];
                        iframeWin.updateUser(function () {
                            refresh(update_user_index);
                        });
                    },
                    btn2: function (index) {
                        window.top.layer.close(index);
                    }
                });
            } else if (obj.event === 'disable') {
                updateUserStatus(data.id, 0, "禁用")
            } else if (obj.event === 'enable') {
                updateUserStatus(data.id, 1, "启用")
            } else if (obj.event === 'show') {
                show_user_index = window.top.layer.open({
                    type: 2,
                    offset: 'r',
                    title: '人员查看',
                    shadeClose: true,
                    area: ['45%', '100%'],
                    btn: ['关闭'],
                    content: '${ctx}/user/showUserPage?id=' + data.id,
                    btn1: function (index) {
                        window.top.layer.close(index);
                    }
                });
            }
        });

        // 监听启用状态
        form.on('switch(api-auth-table-enabled)', function (obj) {
            var json = JSON.parse(decodeURIComponent($(this).data('json')));
            layer.confirm("确认修改内容",
                {
                    icon: 3,
                    title: '操作确认提示',
                    cancel: function () {
                        renderTable();
                    }, btn2: function (index) {
                        renderTable();

                    }
                },
                function (indexCon) {
                    //点击确定回调事件
                    layer.close(indexCon);
                    var loadIndex = layer.load();
                    $.ajax({
                        url: "${ctx}/admin/companyUser/updateUserCompanyEnabled",
                        type: "POST",
                        cache: false,
                        data: {
                            id: json.id,
                            enabled: obj.elem.checked ? 1 : 0
                        },
                        success: function (data) {
                            layer.msg("操作成功!", {icon: 1});
                            renderTable();
                            layer.close(loadIndex);
                        },
                        error: function () {
                            layer.msg("操作失败!", {
                                icon: 2, end: function () {
                                    renderTable();
                                }
                            });
                            layer.close(loadIndex);
                        }
                    });
                    json = table.clearCacheKey(json);
                }
            )

        });

        table.on('tool(operate)', function (obj) {
            var data = obj.data;
            switch (obj.event) {
                case  'edit':
                    updateUserPage(data.id);
                    break;
                case 'disable':
                    updateEnabled(data.id, data.enable);
                    break;
                //CA绑定事件
                case 'bindingCA':
                    bindCa(data.id, 2)
                    break;
                case 'resetPassword':
                    resetPassword(data.id);
                    break;
                    case 'show':
                        show_user_index = window.top.layer.open({
                            type: 2,
                            offset: 'r',
                            title: '人员查看',
                            shadeClose: true,
                            area: ['50%', '100%'],
                            btn: ['关闭'],
                            content: '${ctx}/admin/companyUser/showComPanyInfoPage?id=' + data.id,
                            btn1: function (index) {
                                window.top.layer.close(index);
                            }
                        });
                        break;
                default:
                    console.error("事件未定义请设置" + obj.data)
                    break;
            }

        });
        form.render();
    });


    /**
     *
     * @return
     * @author lesgod
     * @date 2020/5/9 13:21
     */
    function startSearch() {
        var name = $("#name").val();
        var enabled = $('#enabled option:selected').val();
        layui.table.reload("list-user", {
            where: serializeObject($("#seach-Form"))
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        })
    }


    /**
     * 添加用户页面
     * @return
     * @author lesgod
     * @date 2020/5/11 13:48
     */
    function addCompanyUserPage() {
        window.top.layer.open({
            type: 2,
            offset: 'r',
            title: '添加角色',
            shadeClose: true,
            area: ['45%', '100%'],
            btn: ['确认', '取消'],
            content: "${ctx}/admin/companyUser/addCompanyUserPage",
            btn1: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.submitCompanyUser(function () {
                    renderTable();
                    window.top.layer.close(index);
                });
            },
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 重新渲染table
     * @return
     * @author lesgod
     * @date 2020/5/11 15:31
     */
    function renderTable() {
        layui.table.reload("list-user");
    }

    /**
     * 删除企业用户
     * @return
     * @author lesgod
     * @date 2020/5/11 15:36
     */
    function deleteUser() {
        var data = layui.table.checkStatus('list-user').data;
        var arr = [];
        if (data.length) {
            layer.confirm("确定删除勾选的" + data.length + "条信息？",
                {icon: 3, title: '操作确认提示'},
                function (index) {
                    layer.close(index);
                    var loadIndex = layer.load();
                    //点击确定回调事件
                    for (var i = 0; i < data.length; i++) {
                        arr.push(data[i].id);
                    }
                    $.ajax({
                        url: '/admin/companyUser/delCompanyUserByIds',
                        type: 'post',
                        cache: false,
                        async: false,
                        traditional: "true",
                        data: {
                            ids: arr
                        },
                        success: function (data) {
                            if (!isNull(data)) {
                                data = JSON.parse(data);
                            }
                            renderTable();
                            layer.close(loadIndex);
                        },
                        error: function (data) {
                            console.error(data);
                            layer.close(loadIndex);
                        },
                    });
                }
            )


        }

    }

    /**
     * 修改企业信息
     * @return
     * @author lesgod
     * @date 2020/5/11 16:10
     */
    function updateUserPage(id) {
        window.top.layer.open({
            type: 2,
            offset: 'r',
            title: '添加角色',
            shadeClose: true,
            area: ['45%', '100%'],
            btn: ['确认', '取消'],
            content: "${ctx}/admin/companyUser/updateCompanyUserPage?id=" + id,
            btn1: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.submitCompanyUser(function () {
                    renderTable();
                    window.top.layer.close(index);
                });
            },
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
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

    /**
     *
     * @return
     * @author lesgod
     * @date 2020/5/13 10:56
     */
    function resetPassword(id) {
        layer.prompt(
            {
                title: '请输入重置密码，不得少于6位'
            },
            function (text, index) {
                if (!isNull(text) && text.length > 5) {
                    layer.close(index);
                    var indexLoad = layer.load();
                    $.ajax({
                        url: '/admin/companyUser/resetUserCompanyPassword',
                        type: 'post',
                        cache: false,
                        async: false,
                        data: {
                            id: id,
                            password: text
                        },
                        success: function (data) {
                            layer.close(indexLoad)
                            if (!isNull(data)) {
                                data = JSON.parse(data);
                            }
                            renderTable();

                            layer.msg("操作成功");
                        },
                        error: function (data) {
                            console.error(data);
                            layer.close(indexLoad)
                        },
                    });
                } else {
                    layer.alert("密码不符合规范！");
                }
            });
    }

</script>
</body>
</html>

