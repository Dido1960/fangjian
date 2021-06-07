<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>角色管理</title>
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
    <script type="text/html" id="barOperate">
        {{# if( d.ipInfo!='' && d.ipInfo!=undefined){}}
            <a class="layui-btn layui-btn-sm" lay-event="show" style="background-color: #1E9FFF">IP定位</a>
        {{# } }}

    </script>
</head>
<body>
<div style="margin: 10px;width: calc(100vw - 20px);background: #fff">
    <div class="layui-tab layui-tab-brief">
        <ul class="layui-tab-title">
            <li class="layui-this">在线人员</li>
            <li>分布图</li>
        </ul>
        <div class="layui-tab-content" style="height: calc(100vh - 120px);">
            <div class="layui-tab-item layui-show">
                <table class="layui-hide" id="list-role-table" lay-filter="operate"></table>
            </div>
            <div class="layui-tab-item">
                <div class="map">
                    <div id="main" style="width: 100%;height:100%;"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;

        // layui form 渲染
        form.render();
    });

    // 新增角色弹窗
    var add_role_index;
    // 修改角色弹窗
    var update_role_index;
    // 菜单权限弹窗
    var menu_permission_index;

    var tableIns;
    layui.use('table', function () {
        var table = layui.table;

        tableIns = table.render({
            height: 'full-180',
            elem: '#list-role-table',
            url: '${ctx}/sys/pagedOnlineInfo',
            toolbar: '#role-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {title: '序号', type: 'numbers'}
                , {field: 'insertTime', title: '时间'}
                , {field: 'name', title: '用户名称'}
                , {field: 'module', title: '使用模块'}
                , {field: 'sessionId', title: 'sessionId', align: 'center'}
                , {field: 'userId', title: '用户名称', align: 'center'}
                , {field: 'ipInfo', title: 'ip信息', align: 'center'}
                , {field: 'addressInfo', title: '地址信息', align: 'center'}
                , {fixed: 'right', width: '10%', title: '操作', align: 'center', toolbar: '#barOperate'}
            ]],
            page: true
        });
        /**
         * layui table 工具栏绑定事件
         */
        table.on('tool(operate)', function (obj) {
            var data = obj.data;
            if (obj.event === 'show') {
                console.log("当前IP地址："+data.ipInfo)
                parsingIp(data.ipInfo);
            }
        });

    });


    /**
     * 重载table
     *
     * @param layer_index 需要关闭的layer
     */
    function reloadTable() {
        tableIns.reload("list-role-table");
    }

    /**
     * 解析IP地址，转换为所在省市
     * @param ip
     */
    function parsingIp(ip){
        var indexLoad= layer.load();
        $.ajax({
          url:'https://restapi.amap.com/v3/ip?ip='+ip+'&output=json&key=dc22ca1f49f2828a42ca26518ee5fc7f',
          type:'get',
          cache:false,
          async: false,
          dataType: 'jsonp',
          crossDomain: true,
          success:function(data){
            layer.close(indexLoad)
              console.log(data)
             if(!isNull(data) && data.status==1){
                 var ipLocation = data.province + data.city ;
                 layer.alert("你当前查询的IP所在地为：<br>"+ipLocation, {
                     icon: 0,
                     title: "温馨提示",
                     btn: "知道了",
                     closeBtn: 0
                 });
             }
          },
          error:function(data){
             console.error(data);
             layer.close(indexLoad)
          },
        });
    }
</script>
</body>
</html>

