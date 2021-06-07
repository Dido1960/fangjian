<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>印模签章配置管理</title>
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
                <input type="hidden" name="regNo" value="${signatureConfigInfo.regNo}"/>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">所属地区</label>
                        <div class="layui-input-block">
                            <input type="text" name="regName" value="${signatureConfigInfo.regName}" autocomplete="off"
                                   class="layui-input" disabled>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">印模编号</label>
                        <div class="layui-input-block">
                            <input type="text" name="impressionNo" id="impression-no" class="layui-input">
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
            <table class="layui-hide" id="list-signature-config" lay-filter="operate">
                <script type="text/html" id="orderNum">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
                <script type="text/html" id="signature-config-toolbar">
                    <button type="button" onclick="addSignatureConfigInfoPage()" class="layui-btn layui-btn-normal layui-btn-sm"><i
                                class="layui-icon"></i> 新增
                    </button>
                    <button type="button" onclick="deleteSignatureConfigInfo()" class="layui-btn layui-btn-primary layui-btn-sm"><i
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
        var name = $("#impression-no").val();

        layui.table.reload('list-signature-config', {
            where: {
                name: encodeURI(name),
            },
            page: {
                curr: 1
            }
        });
    }

    /**
     * 添加
     */
    function addSignatureConfigInfoPage() {
        window.layer.open({
            type: 2,
            title: '新增印模签章配置',
            area: ['60%', '80%'],
            btn: ['确认', '取消'],
            offset: 'auto',
            content: '${ctx}/signatureConfig/addSignatureConfigInfoPage?regName=' + '${signatureConfigInfo.regName}' + '&regNo=' + '${signatureConfigInfo.regNo}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addSignatureConfigInfo(function () {
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
    function deleteSignatureConfigInfo() {
        var data = layui.table.checkStatus('list-signature-config').data;
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
                    url: '${ctx}/signatureConfig/deleteSignatureConfigInfo',
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
            window.top.layer.msg("请至少选择一个印模签章信息！", {icon: 2, time: 2000});
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
            elem: '#list-signature-config',
            url: '${ctx}/signatureConfig/pagedSignatureConfig?regNo=' + '${signatureConfigInfo.regNo}',
            toolbar: '#signature-config-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '5%', align: 'center', hide: true}
                , {title: '序号', type: 'numbers', width: '10%'}
                , {field: 'impressionNo', title: '印模编号', align: 'center'}
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
                    showSignatureConfigInfo(data);
                    break;
                case "edit":
                    updateSignatureConfigInfo(data);
                    break;
            }
        });

        // 修改印模签章配置
        function updateSignatureConfigInfo(data) {
            window.layer.open({
                type: 2,
                offset: 'auto',
                title: '修改印模签章配置',
                area: ['60%', '80%'],
                btn: ['修改', '取消'],
                content: '${ctx}/signatureConfig/updateSignatureConfigInfoPage?id=' + data.id,
                btn1: function (index, layero) {
                    var body = window.layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.updateSignatureConfigInfo(function () {
                        window.layer.close(index);
                        renderTable();
                    });
                },
                btn2: function (index) {
                    window.layer.close(index);
                }
            });
        }

        // 查看印模签章配置
        function showSignatureConfigInfo(data) {
            window.layer.open({
                type: 2,
                offset: 'auto',
                title: '查看印模签章配置',
                shadeClose: true,
                area: ['60%', '80%'],
                btn: ['取消'],
                content: '${ctx}/signatureConfig/showSignatureConfigInfoPage?id=' + data.id,
                btn1: function (index) {
                    window.layer.close(index);
                }
            });
        }
    });
</script>
</body>
</html>

