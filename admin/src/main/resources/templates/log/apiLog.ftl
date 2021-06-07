<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>接口日志</title>
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
                        <label class="layui-form-label">请求方法名</label>
                        <div class="layui-input-block">
                            <input type="text" name="methodName" id="method-name" autocomplete="off"
                                   class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">请求参数</label>
                        <div class="layui-input-block">
                            <input type="text" name="param" id="params" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">平台授权码</label>
                        <div class="layui-input-block">
                            <input type="text" name="platform" id="platform" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">API授权码</label>
                        <div class="layui-input-block">
                            <input type="text" name="apiKey" id="api-key" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">响应耗时</label>
                        <div class="layui-input-block">
                            <input type="number" name="responseTime" id="response-time" autocomplete="off"
                                   class="layui-input">
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
            <table class="layui-hide" id="api-log-table">
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
            method_name = $("#method-name").val(),
            params = $("#params").val(),
            platform = $("#platform").val(),
            api_key = $("#api-key").val(),
            response_time = $("#response-time").val();

        layui.table.reload("api-log-table", {
            where: {
                searchTime: search_time,
                methodName: method_name,
                params: params,
                platform: platform,
                apiKey: api_key,
                responseTime: response_time
            }
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        })
    }
</script>
<script type="text/javascript">
    // layui-table
    var tableIns;

    layui.use(['form', 'table', 'layer', 'laydate'], function () {
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
            height: 'full-150',
            elem: '#api-log-table',
            url: '${ctx}/log/pagedApiLog',
            toolbar: true,
            cols: [[
                {field: 'id', title: 'ID', align: 'center', hide: true}
                , {title: '序号', type: 'numbers'}
                , {field: 'insertTime', title: '记录创建时间', align: 'center'}
                , {field: 'apiName', title: '请求接口名称', align: 'center'}
                , {field: 'methodName', title: '请求方法名称', align: 'center'}
                , {field: 'params', title: '请求参数', align: 'center'}
                , {field: 'platform', title: '平台授权码', align: 'center'}
                , {field: 'apiKey', title: 'API授权码', align: 'center'}
                , {field: 'createApiTime', title: '创建时间', align: 'center'}
                , {field: 'response', title: '响应结果', align: 'center'}
                , {field: 'responseTime', title: '响应时间', align: 'center'}
                , {field: 'responseTimeConsume', title: '响应耗时', align: 'center'}
            ]],
            page: true
        });

    });
</script>
</body>
</html>

