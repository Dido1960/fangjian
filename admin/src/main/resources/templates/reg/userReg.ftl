<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>区划管理</title>
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
        <div class="layui-col-md12">
            <div class="layui-row">
                <div class="layui-card">
                    <div class="layui-card-body">
<#--                        <div class="layui-card-header layuiAdmin-card-header-auto">-->
<#--                            <button class="layui-btn layuiAdmin-btn-tags" onclick="add()">添加</button>-->
<#--                            <button class="layui-btn layui-btn-normal layuiAdmin-btn-tags" onclick="enabled()">启用-->
<#--                            </button>-->
<#--                            <button class="layui-btn layui-btn-primary layuiAdmin-btn-tags" onclick="forbidden()">禁用-->
<#--                            </button>-->
<#--                        </div>-->
                        <iframe id="left" name="left" src="${ctx}/reg/userTreeRegPage?id=${userId}" style="width: 100%;"
                                frameborder="0"></iframe>
                    </div>
                </div>
            </div>
        </div>
<#--        <div class="layui-col-md9">-->
<#--            <div class="layui-card">-->
<#--                <div class="layui-card-body">-->
<#--                    <iframe id="right" name="right" src="" style="width: 100%;" frameborder="0"></iframe>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
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
     * 添加区划
     */
    function add() {
        $('#right').attr('src', '${ctx}/reg/addRegPage');
    }

    /**
     * 启用区划
     */
    function enabled() {
        left.enabled();
    }

    /**
     * 禁用区划
     */
    function forbidden() {
        left.forbidden();
    }

</script>
</body>
</html>

