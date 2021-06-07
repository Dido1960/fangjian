<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门管理</title>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/opTable/opTable.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src='${ctx}/layuiAdmin/opTable/opTable.js'></script>


    <script type="text/html" id="dep-table-enabled">
        <input type="checkbox" name="enabled" lay-skin="switch" lay-text="启用|禁用" lay-filter="gov-user-table-enabled"
               value="{{ d.enabled }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{ d.enabled== 1 ? 'checked' : '' }}>
    </script>
    <script type="text/html" id="dep-table-type">
        {{# if(d.govDepType == 1) { }}
        招标办
        {{# } else if(d.govDepType == 2) { }}
        交易中心（专家录入）
        {{# } else if(d.govDepType == 3) { }}
        交易中心（工程处）
        {{# } else { }}
        未配置类别
        {{# } }}
    </script>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card">
            <form class="layui-form">
                <input type="hidden" name="regId" value="${dep.regId}"/>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">所属地区</label>
                        <div class="layui-input-block">
                            <input type="text" name="regName" value="${dep.regName}" autocomplete="off"
                                   class="layui-input"
                                   disabled>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">部门名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="depName" id="dep-name" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">部门状态</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="enabled" name="enabled" id="enabled" please="t"></@WordBookTag>
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
            <table class="layui-hide" id="list-dep" lay-filter="operate">
                <script type="text/html" id="orderNum">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
                <script type="text/html" id="dep-toolbar">
                    <button type="button" onclick="addDepPage()" class="layui-btn layui-btn-normal layui-btn-sm"><i
                                class="layui-icon"></i> 新增
                    </button>
                    <button type="button" onclick="deleteDep()" class="layui-btn layui-btn-primary layui-btn-sm"><i
                                class="layui-icon"></i>删除
                    </button>
                </script>
                <script type="text/html" id="barOperate">
                    <a class="layui-btn layui-btn-sm" lay-event="show">查看</a>
                    <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>
                    <a class="layui-btn layui-btn-sm" onclick="addGovUser(this)">新增人员</a>
                </script>
                <script type="text/html" id="barDemo">
                    <a class="layui-btn layui-btn-sm" onclick="deleteGovUser(this)">删除</a>
                    <a class="layui-btn layui-btn-sm" onclick="updateGovUser(this)">修改</a>
                    <a class="layui-btn layui-btn-sm" onclick="bindCa('{{d.id}}','1')">绑定CA</a>
                </script>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    function bindCa(userId, userType) {
        window.location.href = '${ctx}/admin/userCert/listUserCertPage?userId=' + userId + "&type=" + userType + "&redirectPage=" + encodeURIComponent(window.location.href);

    }

    // layui-table
    var tableIns;

    /**
     * 重载table
     *
     * @param layer_index 需要关闭的layer
     */
    function renderTable() {
        tableIns.reload("list-dep");
    }

    /**
     * 部门查询
     */
    function startSearch() {
        var dep_name = $("#dep-name").val();
        var enabled = $('#enabled option:selected').val();

        layui.table.reload('list-dep', {
            where: {
                depName: encodeURI(dep_name),
                enabled: encodeURI(enabled)
            },
            page: {
                curr: 1
            }
        });
    }

    /**
     * 添加部门layer
     */
    function addDepPage() {
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '新增部门',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['确认', '取消'],
            content: '${ctx}/dep/addDepPage?regName=' + encodeURI('${dep.regName}') + '&regId=' + '${dep.regId}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addDepartment(function () {
                    window.layer.close(index);
                    renderTable();
                });
            },
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 删除部门信息
     */
    function deleteDep() {
        var data = layui.table.checkStatus('list-dep').data;
        layer.confirm("确定要删除该部门吗", {icon: 8}, function () {
            var arr = [];
            if (data.length) {
                for (var i = 0; i < data.length; i++) {
                    arr.push(data[i].id);
                }
                $.ajax({
                    url: '${ctx}/dep/delDep',
                    type: "POST",
                    async: false,
                    traditional: "true",
                    data: {
                        ids: arr
                    },
                    success: function (data) {
                        if (data.code == 1) {
                            layer.alert(data.msg, {icon: 6}, function () {
                                window.location.reload();
                            })
                        } else if (data.code == 2) {
                            layer.alert(data.msg, {icon: 5}, function () {
                                window.location.reload();
                            })
                        }
                    }
                })
            } else {
                window.top.layer.msg("请至少选择一个部门！", {icon: 2, time: 2000});
            }
        })

    }
</script>
<script>
    $(function () {
        tableIns = layui.opTable.render({
            height: 'full-110',
            elem: '#list-dep',
            url: '${ctx}/dep/pagedDep?regId=' + '${dep.regId}',
            toolbar: '#dep-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '100', align: 'center', hide: true}
                , {title: '序号', type: 'numbers', width: '10%'}
                , {field: 'depName', title: '部门名称', align: 'center'}
                , {field: 'govDepType', title: '部门类别',templet: '#dep-table-type', align: 'center'}
                , {field: 'status', title: '启用状态', width: 100, templet: '#dep-table-enabled', unresize: true}
                , {fixed: 'right', title: '操作', width: '20%', align: 'center', toolbar: '#barOperate'}
            ]],
            page: true,
            openType: 1,
            openTable: function (showItemData) {
                return {
                    elem: '#child'+showItemData.id
                    , id: 'child'+showItemData.id

                    , url: '${ctx}/govUser/pagedUser?depId=' + showItemData.id
                    , page: true
                    , cols: [[

                        {title: '序号', width: '60', type: 'numbers'},
                        {field: 'id', width: '60', title: 'ID'}
                        , {field: 'name', width: 200, title: '用户名'}
                        , {field: 'loginName', width: 200, title: '登录名'}
                        , {field: 'phone', width: 200, title: '电话'}
                        , {title: '操作', toolbar: '#barDemo', width: 400}
                    ]]

                }
            }
            , openType: 1

        });
        layui.table.on('tool(operate)', function (obj) {
            var data = obj.data;
            switch (obj.event) {
                case "show":
                    showDep(data);
                    break;
                case "edit":
                    updateDep(data);
                    break;
            }
        });
    })


    layui.config({
        base: '${ctx}/layuiAdmin/opTable'
    }).extend({
        opTable: '/opTable'
    });
    layui.use(['form', 'layer', 'opTable'], function () {
        var form = layui.form;
        // layui form 渲染
        form.render();
        // 监听启用状态
        form.on('switch(gov-user-table-enabled)', function (obj) {
            var json = JSON.parse(decodeURIComponent($(this).data('json')));
            $.ajax({
                url: '${ctx}/dep/updateDepStatus',
                type: "POST",
                cache: false,
                data: {
                    id: json.id,
                    enabled: obj.elem.checked ? 1 : 0
                },
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

    })
    /**
     * layui table 工具栏绑定事件
     */
    /*        table.on('tool(operate)', function (obj) {
                var data = obj.data;
                switch (obj.event) {
                    case "show":
                        showDep(data);
                        break;
                    case "edit":
                        updateDep(data);
                        break;
                }
            });*/

    /*        // 监听启用状态
            form.on('switch(gov-user-table-enabled)', function (obj) {
                var json = JSON.parse(decodeURIComponent($(this).data('json')));
                $.ajax({
                    url: '${ctx}/dep/updateDep',
                type: "POST",
                cache: false,
                data: {
                    id: json.id,
                    enabled: obj.elem.checked ? 1 : 0
                },
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
        });*/



    // 修改部门
    function updateDep(data) {
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '修改部门',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['修改', '取消'],
            content: '${ctx}/dep/updateDepPage?id=' + data.id,
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
    function showDep(data) {
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '查看部门',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['取消'],
            content: '${ctx}/dep/showDepPage?id=' + data.id,
            btn1: function (index) {
                window.layer.close(index);
            }
        });
    }


    function addGovUser(e) {
        var depId = $(e).parents("tr").find("td[data-field='id']").children().text();
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '新增人员',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ["新增", '取消'],
            content: '${ctx}/govUser/addGovUserPage?depId=' + depId + '&regId=' + '${dep.regId}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.submitGovUser(function () {
                    layer.msg("操作成功!", {icon: 1});
                    window.layer.close(index);
                    window.location.reload();
                    renderTable();

                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

    function deleteGovUser(e) {
        var id = $(e).parents("td").siblings("td[data-field='id']").children().text();
        layer.confirm("确定要删除吗？", {icon: 9}, function () {
            $.ajax({
                url: '${ctx}/govUser/deleteById?id=' + id,
                type: 'post',
                cache: false,
                async: false,
                data: {},
                success: function (data) {
                    layer.alert("删除成功！", {icon: 6}, function () {
                        window.location.reload();
                    })
                },
                error: function (data) {
                    layer.alert("删除失败！", {icon: 5}, function () {
                        window.location.reload();
                    })
                },
            });
        })
    }

    function updateGovUser(e, govDepType) {
        var id = $(e).parents("td").siblings("td[data-field='id']").children().text();
        window.layer.open({
            type: 2,
            offset: 'r',
            title: '修改人员',
            shadeClose: true,
            area: ['100%', '100%'],
            btn: ['修改', '取消'],
            content: '${ctx}/govUser/updateGovUserPage?id=' + id + "&regId=" + '${dep.regId}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.updateGovUser(function () {
                    window.layer.close(index);
                    renderTable();
                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

</script>
</body>
</html>

