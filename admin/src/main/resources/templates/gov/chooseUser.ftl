<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门选择</title>
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
            <form class="layui-form">
                <div class="layui-form-item">
                    <div class="layui-inline" style="margin: 20px 0 0 -90px">
                        <div class="layui-input-block">
                            <input type="text" style="width: 300px" name="depName" id="dep-name" autocomplete="off"
                                   placeholder="请输入机构名称" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline" style="margin: 20px 0 0 -90px">
                        <div class="layui-input-block">
                            <input type="text" style="width: 300px" name="Name" id="name" autocomplete="off"
                                   placeholder="请输入用户名称" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline" style="margin: 20px 0 0 -100px">
                        <div class="layui-input-block">
                            <button type="button" onclick="queryDep()" class="layui-btn layui-btn-normal">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <table class="layui-hide" id="list-dep" lay-filter="operate"></table>
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
            height: 'full-70'
            , elem: '#list-dep'
            , url: '${ctx}/govUser/pageUserByRegId'
            , where: {"regNo": '${regNo}'}
            , cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', fixed: 'left', hide: true}
                , {field: 'name', title: '人员名称'}
                , {field: 'depName', title: '机构名称'}
                , {field: 'loginName', title: '登录名'}
            ]],
            page: true

        })
    });

    /**
     * 查询部门
     */
    function queryDep() {
        var depName = $("#dep-name").val();
        var name=$("#name").val()
        layui.table.reload('list-dep', {
            where: {
                depName: depName
                ,name:name
            },
            page: {
                curr: 1
            }
        });
    }

    function configGovUser() {
        var data = layui.table.checkStatus('list-dep').data;
        var arr = [];
        if (data.length) {
            layer.confirm("确定将这些用户配置到该环节下吗",
                {icon: 3, title: '操作确认提示'},
                function () {
                    var loadIndex = layer.load();
                    //点击确定回调事件
                    for (var i = 0; i < data.length; i++) {
                        arr.push(data[i].id);
                    }
                    $.ajax({
                        url: '${ctx}/proProcess/configGovUser',
                        async: false,
                        cache: false,
                        traditional: "true",
                        data: {
                            ids: arr
                            ,id:'${id}'
                        },
                        success: function (data) {
                            if(data){
                                layer.alert("配置成功",{icon:6},function () {
                                    window.parent.location.reload();
                                })
                            }else {
                                layer.alert("配置失败",{icon:5},function () {
                                    window.location.reload();
                                })
                            }
                        }
                    })
                }
            )
        } else {
            window.top.layer.msg("请至少选择一个人员！", {icon: 2, time: 2000});
        }
    }



</script>
</body>
</html>

