<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>接口授权</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card" style="margin: 15px 0 0 15px; width: calc(100% - 30px)">
            <form class="layui-form" style="padding-top: 15px;">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">API 名称</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="apiName" name="apiName" id="api-name" please="t"></@WordBookTag>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">平台授权码</label>
                        <div class="layui-input-block">
                            <input type="text" name="platform" id="platform" class="layui-input" style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">接口授权码</label>
                        <div class="layui-input-block">
                            <input type="text" name="apiKey" id="api-key" class="layui-input" style="width: 300px">
                        </div>
                    </div>

                    <div class="layui-inline">
                        <label class="layui-form-label">使用平台</label>
                        <div class="layui-input-block">
                            <input type="text" name="remark" id="remark" class="layui-input" style="width: 300px">
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
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            <button type="button" class="layui-btn layui-btn-normal" onclick="startSearch()">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <table class="layui-hide" id="api-auth-table" lay-filter="api-auth-table" style="margin-left: 15px">
                <script type="text/html" id="order-num">
                    {{d.LAY_TABLE_INDEX + 1}}
                </script>
            </table>
            <script type="text/html" id="api-auth-table-toolbar">
                <div class="layui-btn-container">
                    <button class="layui-btn layui-btn-sm" lay-event="add">
                        <i class="layui-icon"></i>生成授权
                    </button>
                    <button class="layui-btn layui-btn-sm layui-btn-warm"  onclick="deleteApiAuth()">
                       删除信息
                    </button>
                </div>
            </script>
            <script type="text/html" id="api-auth-table-enabled">
                <input type="checkbox" name="enabled" lay-skin="switch" lay-text="启用|禁用"
                       lay-filter="api-auth-table-enabled"
                       value="{{ d.enabled }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{ d.enabled== 1
                       ? 'checked' : '' }}>
            </script>
        </div>
    </div>
</div>
<script>
    /**
     * 模糊查询
     */
    function startSearch() {
        var api_name = $("#api-name").val();
        var platform = $("#platform").val();
        var api_key = $("#api-key").val();
        var remark = $("#remark").val();
        var enabled = $("#enabled").val();

        layui.table.reload("api-auth-table", {
            where: {
                apiName: api_name,
                platform: platform,
                apiKey: api_key,
                remark: remark,
                enabled: enabled
            },
            page: {
                curr: 1
            }
        })
    }

    /**
     * 新增授权
     */
    function add() {
        layer.open({
            type: 2,
            title: "生成接口授权码",
            content: "${ctx}/apiAuth/addApiAuthPage",
            area: ['60%', '60%'],
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.add();
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.close(index);
            }
        })
    }
</script>
<script type="text/javascript">
    /**
     * 初始化layui
     */
    layui.use(['form', 'table', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;

        // layui form 渲染
        form.render();

        // 创建静态表格实例
        table.render({
            id: "api-auth-table",
            elem: '#api-auth-table',
            height: 'full-150',
            url: '${ctx}/apiAuth/pagedApiAuth',
            toolbar: "#api-auth-table-toolbar",
            page: true,
            cols: [[
                {type: 'checkbox'},
                {title: '序号', type: 'numbers'},
                {field: 'remark', title: '使用平台', align: 'center'},
                {field: 'chineseApiName', title: 'API名称', align: 'center'},
                {field: 'platform', title: 'PLATFORM', align: 'center', edit: 'text'},
                {field: 'apiKey', title: 'API KEY', align: 'center'},
                {field: 'enabled', title: '启用', width: 100, templet: '#api-auth-table-enabled', unresize: true}
            ]]
        });

        // 监听头部工具栏
        table.on("toolbar(api-auth-table)", function (obj) {
            switch (obj.event) {
                case 'add':
                    add();
                    break;
            }
        });

        // 监听启用状态
        form.on('switch(api-auth-table-enabled)', function (obj) {
            var json = JSON.parse(decodeURIComponent($(this).data('json')));

            $.ajax({
                url: "${ctx}/apiAuth/updateApiAuthEnabled",
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
                                table.reload("api-auth-table");
                            }
                        });
                    }
                },
                error: function () {
                    layer.msg("操作失败!", {
                        icon: 2, end: function () {
                            table.reload("api-auth-table");
                        }
                    });
                }
            });

            json = table.clearCacheKey(json);
        });
    });


    /**
     * 删除角色
     */
    function deleteApiAuth() {
        var data = layui.table.checkStatus('api-auth-table').data;
        var arr = [];
        if (data.length) {
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].id);
            }
            layer.confirm("删除是一个不可逆的，请慎重删除！<span style='color: red'>非测试数据，不建议删除</span>!是否继续删除？？？",
                {
                    icon: 3,
                    title: '操作确认提示'
                },
                function (index) {
                    //点击确定回调事件
                    layer.close(index);
                    $.ajax({
                        url: '${ctx}/apiAuth/deleteApiAuth',
                        async: false,
                        traditional: "true",
                        data: {
                            ids: arr
                        },
                        success: function (data) {
                            data = JSON.parse(data);
                            if (data === true) {
                                layui.table.reload('api-auth-table');
                                window.top.layer.msg("删除成功！", {icon: 1, time: 2000});
                            } else {
                                window.top.layer.msg("删除失败！", {icon: 2});
                            }
                        }
                    })
                }
            );

        } else {
            window.top.layer.msg("请至少选择一个！", {icon: 2, time: 2000});
        }
    }
</script>
</body>
</html>