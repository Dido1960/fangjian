<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>菜单管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/style/admin.css" media="all">
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md3">
            <div class="layui-row">
                <div class="layui-card">
                    <div class="layui-card-body">
                        <div class="layui-card-header">
                            <a class="layui-btn layui-btn-sm" onclick="add()">添加</a>
                            <a class="layui-btn layui-btn-primary layui-btn-sm" onclick="del()">删除</a>
                            <a class="layui-btn layui-btn-normal layui-btn-sm" onclick="enabled()">启用
                            </a>
                            <a class="layui-btn layui-btn-danger layui-btn-sm" onclick="dis()">禁用</a>
                            <a id="help" name="help"
                               class="layui-btn layui-btn-warm layui-btn-sm"
                               onclick="help_me()" onfocus="help_me()"><i class="layui-icon layui-icon-help"
                                                      style="margin-right:14px"></i></a>
                        </div>
                        <div class="layui-card-body">
                            <iframe id="left" name="left"
                                    src="${ctx}/menu/treeMenuPage?parentId=${RequestParameters.parentId! }"
                                    style="width: 100%;" frameborder="0"></iframe>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-col-md9">
            <div class="layui-card">
                <div class="layui-card-body">
                    <iframe id="right" name="right" src="" style="width: 100%;" frameborder="0"></iframe>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script>
    // 使用帮助
    function help_me() {
        layui.use('layer', function () {
            var layer = layui.layer;
            layer.tips('1、点击左键新增菜单</br>2、点击右键修改菜单', '#help', {
                tips: [2, '#0FA6D8'] //还可配置颜色
            });
        });
    }
</script>
<script type="text/javascript">
    $(function () {
        resizeWindow();
    });

    /**
     * 绑定窗体大小改变事件
     */
    $(window).resize(resizeWindow);

    /**
     * 窗体大小改变事件
     */
    function resizeWindow() {
        var h = $(window).height();//获取文档高度
        $('#left').css("height", (h - 130) + "px");
        $('#right').css("height", (h - 60) + "px");
    }

    /**
     * 添加菜单
     */
    function add() {
        left.add();
    }

    /**
     * 删除菜单
     */
    function del() {
        left.del();
    }

    /**
     * 启用菜单
     */
    function enabled() {
        left.enabled();
    }

    /**
     * 禁用菜单
     */
    function dis() {
        left.dis();
    }
</script>
</body>
</html>

