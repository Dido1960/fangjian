<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>系统选项卡</title>
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
            <div class="layui-tab layui-tab-brief" lay-filter="configTab">
                <ul class="layui-tab-title">
                    <#list typeList as type>
                        <li id="${type.code}" <#if type_index==0>class="layui-this"</#if> >${type.des}</li>
                    </#list>
                </ul>
                <div class="layui-tab-content">
                    <div class="layui-tab-item layui-show">
                        <iframe id="contents" src="${ctx}/configMap/configPage" style="width: 100%;" frameborder="0"
                                scrolling="auto"></iframe>
                    </div>
                </div>
            </div>
            <script>
                layui.use('element', function () {
                    var $ = layui.jquery
                        , element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
                });
            </script>
        </div>
    </div>
</div>
<script type="text/javascript">

    /**
     * 初始化layui
     */
    layui.use(['element'], function () {
        var element = layui.element;

        // 点击选项卡，触发跳转对应页面
        element.on('tab(configTab)', function (data) {
            var type = ($(this).attr("id"));
            $("#contents").attr("src", "${ctx}/configMap/configPage?type=" + type);
        });
    });


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
        $('#contents').css("height", h + "px");
    }
</script>
</body>
</html>