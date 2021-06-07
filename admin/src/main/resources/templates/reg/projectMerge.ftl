<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>项目合并</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/style/admin.css" media="all">
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div id="LAY_app">
    <div class="layui-col-md6 layui-layout layui-layout-admin">
        <div class="layui-card" style="margin: 15px 0 0 15px">
            <p style="text-align: center;background-color: #f2f2f2;font-size: 18px;padding-bottom: 10px">项目合并权限配置</p>
            <table class="layui-hide" id="list-role-table" lay-filter="operate"></table>
            <script type="text/html" id="order-num">
                {{d.LAY_TABLE_INDEX+1}}
            </script>
            <script type="text/html" id="barOperate">
                <a class="layui-btn layui-btn-sm" lay-event="menuPermission">权限设置</a>
            </script>
        </div>
    </div>
    <div class="layui-col-md6 layui-layout layui-layout-admin table2" style="display: none">
        <div class="layui-card" style="margin: 15px 0 0 15px">
            <p style="text-align: center;background-color: #f2f2f2;font-size: 18px;padding-bottom: 10px">选择备案部门</p>
            <table class="layui-hide" id="list-role-table1" lay-filter="operate"></table>
            <script type="text/html" id="order-num">
                {{d.LAY_TABLE_INDEX+1}}
            </script>
            <script type="text/html" id="role-toolbar1">
                <button type="button" onclick="updateUserReg()" class="layui-btn layui-btn-normal layui-btn-sm">
                    <i class="layui-icon"></i> 保存
                </button>
            </script>
            <script type="text/html" id="barOperate">
                <a class="layui-btn layui-btn-sm" lay-event="menuPermission">权限设置</a>
            </script>
        </div>
    </div>
</div>
</body>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/javascript">

    // var page = 1;
    // var limit = 10;

    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;
        // layui form 渲染
        form.render();
    });

    // 菜单权限弹窗
    var menu_permission_index;

    var tableIns;
    layui.use('table', function () {
        var table = layui.table;

        tableIns = table.render({
            height: 'full-80',
            elem: '#list-role-table',
            <#--url: '${ctx}/companyUser/listCompanyUser',-->
            <#--url: '${ctx}/govUser/listAllGovUser?page='+page+'&limit='+limit,-->
            url: '${ctx}/govUser/listAllGovUser',
            page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
                layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                // ,curr: 5 //设定初始在第 5 页
                ,groups: 3 //只显示 1 个连续页码
                ,first: false //不显示首页
                ,last: false //不显示尾页
            },
            toolbar: '#role-toolbar',
            cols: [[
                , {field: 'id', align: 'ID', fixed: 'left', hide: true}
                , {title: '序号', type: 'numbers'}
                // , {field: 'id', align: 'center', title: 'ID'}
                , {field: 'name', align: 'center', title: '用户名称'}
                , {field: 'phone', align: 'center', title: '联系方式'}
                , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barOperate'}
            ]],
            // page: true
        });

        /**
         * layui table 工具栏绑定事件
         */
        table.on('tool(operate)', function (obj) {
            var data = obj.data;
            switch (obj.event) {
                case "edit":
                    edit(data);
                    break;
                case "menuPermission":
                    menuPermission(data);
                    break;
            }
        })
    });



    var tableList = [];

    var govUserId;

    // 区划设置
    function menuPermission(data) {
        $(".table2").show();
        console.log(data)
        govUserId = data.id;
        var tableIns1;
        layui.use('table', function () {
            var table = layui.table;

            tableIns1 = table.render({
                id:"checkboxTable",
                height: 'full-80',
                elem: '#list-role-table1',
                url: '${ctx}/wordbook/listWordBook?userId='+data.id+'&type=2',
                // page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
                //     layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                //     // ,curr: 5 //设定初始在第 5 页
                //     ,groups: 3 //只显示 1 个连续页码
                //     ,first: false //不显示首页
                //     ,last: false //不显示尾页
                // },
                toolbar: '#role-toolbar1',
                cols: [[
                    {type:'checkbox'}
                    , {field: 'id', align: 'ID', fixed: 'left', hide: true}
                    , {title: '序号', type: 'numbers'}
                    // , {field: 'id', align: 'center', title: 'ID'}
                    , {field: 'bookKey', align: 'center', title: '编号'}
                    , {field: 'bookValue', align: 'center', title: '名称'}
                    // , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barOperate'}
                ]]
                ,done: function(res, page, count){
                    tableList = res.data;
                    console.log(res)
                //可以自行添加判断的条件是否选中
                //这句才是真正选中，通过设置关键字LAY_CHECKED为true选中，这里只对第一行选中
                // res.data[0]["LAY_CHECKED"]='true';
                // //下面三句是通过更改css来实现选中的效果
                // var index= res.data[0]['LAY_TABLE_INDEX'];
                // $('tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
                // $('tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
                    for (var i = 0; i < res.data.length; i++) {
                        if(res.data[i]["lAY_CHECKED"] == true){
                            res.data[i]["LAY_CHECKED"]='true';
                            var index= res.data[i]['LAY_TABLE_INDEX'];
                            $('tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
                            $('tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
                        }
                    }
            }
            });
            layui.form.render();
            /**
             * layui table 工具栏绑定事件
             */
            table.on('tool(operate)', function (obj) {
                var data = obj.data;
                switch (obj.event) {
                    case "edit":
                        edit(data);
                        break;
                    case "menuPermission":
                        menuPermission(data);
                        break;
                }
            })
        });
    }

    /**
     * 修改权限
     */
    function updateUserReg() {
        layer.confirm("您确定要修改吗？", {
            btn: ['确定', '取消']
        }, function () {
            var data = layui.table.checkStatus('checkboxTable').data;
            var arr = [];
            for (var i = 0; i < data.length; i++) {
                arr.push(data[i].bookKey);
            }
            console.log(data,"-----------------")
            console.log(arr,"-----------------")
            $.ajax({
                url: '/admin/govUserMerge/updateAuditSiteKeyByUserId',
                type: 'post',
                cache: false,
                async: true,
                data: {siteData:arr,userId:govUserId},
                dataType: "json",
                traditional: true,
                success: function (data) {
                    layer.alert("修改成功",function () {
                        $(".table2").hide();
                        layer.closeAll();
                    })

                },
                error: function (data) {
                    console.error(data);
                    layer.alert("系统异常，请稍后再试",function () {
                        layer.closeAll();
                    })

                },
            });
        }, function () {
            layer.closeAll();
        });
    }



    /**
     * 重载table
     *
     * @param layer_index 需要关闭的layer
     */
    function reloadTable() {
        tableIns.reload("list-role-table");
    }


</script>
</html>