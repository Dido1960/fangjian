<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>场地管理</title>
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
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card">
            <form class="layui-form">
                <input type="hidden" name="regId" value="${site.regId}"/>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">所属地区</label>
                        <div class="layui-input-block">
                            <input type="text" name="regName" value="${site.regName}" autocomplete="off"
                                   class="layui-input"
                                   disabled>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">场地名称</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" id="site-name" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">场地类型</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="siteType" name="type" id="siteType" please="t"></@WordBookTag>
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
                    <button type="button" onclick="addSitePage()" class="layui-btn layui-btn-normal layui-btn-sm"><i
                                class="layui-icon"></i> 新增
                    </button>
                    <button type="button" onclick="deleteSite()" class="layui-btn layui-btn-primary layui-btn-sm"><i
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
     * 查询
     */
    function startSearch() {
        var name = $("#site-name").val();
        var type = $('#siteType option:selected').val();

        layui.table.reload('list-dep', {
            where: {
                name: encodeURI(name),
                type: encodeURI(type)
            },
            page: {
                curr: 1
            }
        });
    }

    /**
     * 添加
     */
    function addSitePage() {
        window.layer.open({
            type: 2,
            title: '新增部门',
            area: ['60%', '80%'],
            btn: ['确认', '取消'],
            offset: 'auto',
            content: '${ctx}/site/addSitePage?regName=' + '${site.regName}' + '&regId=' + '${site.regId}',
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
     * 删除
     */
    function deleteSite() {
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
                    url: '${ctx}/site/deleteSite',
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
            window.top.layer.msg("请至少选择一个场地！", {icon: 2, time: 2000});
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
            url: '${ctx}/site/pagedSite?regId=' + '${site.regId}',
            toolbar: '#dep-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '5%', align: 'center', hide: true}
                , {title: '序号', type: 'numbers', width: '10%'}
                , {field: 'name', title: '场地名称', align: 'center'}
                , {field: 'typeName', title: '场地类型', align: 'center'}
                , {field: 'remark', title: '备注', align: 'center'}
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
                    showSite(data);
                    break;
                case "edit":
                    updateSite(data);
                    break;
            }
        });

        // 修改部门
        function updateSite(data) {
            window.layer.open({
                type: 2,
                offset: 'auto',
                title: '修改场地',
                area: ['60%', '80%'],
                btn: ['修改', '取消'],
                content: '${ctx}/site/updateSitePage?id=' + data.id,
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
        function showSite(data) {
            window.layer.open({
                type: 2,
                offset: 'auto',
                title: '查看场地',
                shadeClose: true,
                area: ['60%', '80%'],
                btn: ['取消'],
                content: '${ctx}/site/showSitePage?id=' + data.id,
                btn1: function (index) {
                    window.layer.close(index);
                }
            });
        }
    });
</script>
</body>
</html>

