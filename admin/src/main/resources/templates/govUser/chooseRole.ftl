<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>角色选择</title>
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
            <table class="layui-hide" id="listRoleTable" lay-filter="operate"></table>
        </div>
    </div>
</div>

<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.render();
    });

    layui.use('table', function () {
        var table = layui.table;

        table.render({
            height: 'full-20'
            , elem: '#listRoleTable'
            , url: '${ctx}/role/pagedRole'
            , cellMinWidth: 80
            , cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: 80, fixed: 'left', hide: true}
                , {field: 'roleName', title: '角色名称'}
                , {field: 'remark', title: '备注说明'}
            ]],
            parseData: function (res) {
                var ids = '${roleId!}';
                var checkedIds = ids.split("/");
                layui.each(res.data, function (i, item) {
                    item.LAY_CHECKED = (checkedIds.indexOf(item.id.toString()) !== -1);
                })
            }
        });
    });

    /**
     * 选择角色
     */
    function chooseRole() {
        var data = layui.table.checkStatus('listRoleTable').data;
        var roleName = '';
        var ids = '';
        if (data.length !== 0) {
            for (var i = 0; i < data.length; i++) {
                roleName += '/' + data[i].roleName;
                ids += '/' + data[i].id;
            }
            data[0].roleName = roleName;
            data[0].id = ids;
            return data[0];
        } else {
            return null;
        }
    }
</script>
</body>
</html>

