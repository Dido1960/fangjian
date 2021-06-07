<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>系统访问日志</title>
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
                            <input type="text" name="searchTime" id="search-time" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">用户名</label>
                        <div class="layui-input-block">
                            <input type="text" name="userName" id="user-name" autocomplete="off" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">请求地址</label>
                        <div class="layui-input-block">
                            <input type="text" name="requestURI" id="request-uri" autocomplete="off" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">请求方法</label>
                        <div class="layui-input-block">
                            <input type="text" name="requestMethod" id="request-method" autocomplete="off"
                                   class="layui-input" style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">处理耗时</label>
                        <div class="layui-input-block">
                            <input type="text" name="processingTime" id="processing-time" autocomplete="off"
                                   class="layui-input" style="width: 300px">
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
            <table class="layui-hide" id="network-log-table">
                <script type="text/html" id="order-num">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
            </table>
        </div>
    </div>
</div>
<script>

    /**
     * 系统日志查询
     */
    function startSearch() {
        var search_time = $("#seach-time").val(),
            user_name = $("#user-name").val(),
            request_uri = $("#request-uri").val(),
            request_method = $("#request-method").val(),
            processing_time = $("#processing-time").val();

        layui.table.reload("network-log-table", {
            where: {
                searchTime: search_time,
                userName: user_name,
                requestURI: request_uri,
                requestMethod: request_method,
                processingTime: processing_time
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
            elem: '#search-time',
            range: '~',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime'
        });

        // layui form 渲染
        form.render();

        tableIns = table.render({
            height: 'full-150',
            elem: '#network-log-table',
            url: '${ctx}/log/pagedWebLog',
            toolbar: true,
            cols: [[
                {field: 'id', title: 'ID', align: 'center', hide: true}
                , {title: '序号', type: 'numbers'}
                , {field: 'insertTime', title: '记录创建时间', align: 'center'}
                , {field: 'userId', title: '用户主键', align: 'center'}
                , {field: 'userName', title: '用户名', align: 'center'}
                , {field: 'requestUri', title: '请求地址', align: 'center'}
                , {field: 'requestMethod', title: '请求方法', align: 'center'}
                , {field: 'requestParams', title: '请求参数', align: 'center'}
                , {field: 'remoteAddress', title: '来源地址', align: 'center'}
                , {field: 'userAgent', title: '用户代理', align: 'center'}
                , {field: 'deviceName', title: '设备名称', align: 'center'}
                , {field: 'browserName', title: '浏览器名称', align: 'center'}
                , {field: 'browserVersion', title: '浏览器版本', align: 'center'}
                , {field: 'processingTime', title: '处理耗时(ms)', align: 'center'}
                , {field: 'createTime', title: '创建时间', align: 'center'}
            ]],
            page: true
        });
    });
</script>
</body>
</html>

