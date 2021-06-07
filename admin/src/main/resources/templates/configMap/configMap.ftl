<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>系统键值</title>
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
                        <label class="layui-form-label">KEY</label>
                        <div class="layui-input-block">
                            <input type="text" name="configKey" id="config-key" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">DES</label>
                        <div class="layui-input-block">
                            <input type="text" name="configDes" id="config-des" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">TYPE</label>
                        <div class="layui-input-block">
                            <select name="configType" id="config-type" lay-search="">
                                <#if please??>
                                    <option value="">请直接选择或搜索</option>
                                </#if>
                                <#if types??>
                                    <#list types as type>
                                        <option value="${type.code}">${type.des}</option>
                                    </#list>
                                </#if>
                            </select>
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
            <table class="layui-hide" id="config-table" lay-filter="config-table" style="margin-left: 15px">
                <script type="text/html" id="order-num">
                    {{d.LAY_TABLE_INDEX + 1}}
                </script>
            </table>
        </div>
    </div>
</div>
<script>
    /**
     * 模糊查询
     */
    function startSearch() {
        var config_key = $("#config-key").val();
        var config_type = $("#config-type").val();
        var config_des = $("#config-des").val();

        layui.table.reload("config-table", {
            where: {
                configKey: config_key,
                configType: config_type,
                configDes: config_des
            },
            page: {
                curr: 1
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
            id: "config-table",
            elem: '#config-table',
            height: 'full-90',
            url: '${ctx}/configMap/pagedConfigMap',
            page: true,
            toolbar: true,
            cols: [[
                {title: '序号', type: 'numbers'},
                {field: 'configKey', title: 'KEY', align: 'center'},
                {field: 'configValue', title: 'VALUE', align: 'center', edit: 'text'},
                {field: 'configDes', title: 'DES', align: 'center'},
                {field: 'configTypeName', title: 'TYPE', align: 'center'}
            ]]
        });

        // 监听单元格编辑
        table.on("edit(config-table)", function (obj) {
            var value = obj.value;
            var id = obj.data.id;
            var key = obj.data.configKey;

            $.ajax({
                url: "${ctx}/configMap/updateConfigValue",
                type: "POST",
                data: {
                    key: key,
                    value: value
                },
                success: function (data) {
                    layer.msg(key + "值成功修改为 : " + value, {icon: 1});
                },
                error: function () {
                    layer.alert("操作失败!", {
                        icon: 2, end: function () {
                            table.reload();
                        }
                    });
                }
            })
        })
    });
</script>
</body>
</html>