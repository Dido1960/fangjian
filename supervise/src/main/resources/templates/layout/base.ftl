<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title></title>
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
    <script src="${ctx}/js/common.js"></script>
    <![endif]-->
    <!--layer-->
    <script type="text/javascript" src="${ctx}/plugin/layer/layer.js"></script>
    <#--替换图标-->
    <#--    <link rel="icon" href="//s01.mifile.cn/favicon.ico" type="image/x-icon">-->
    <#--    <link rel="icon" href="${ctx}/logo/logo.png" type="image/x-icon">-->
    <!--layuiamdin std-->
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/style/admin.css" media="all">
    <script src="${ctx}/js/sockjs.min.js"></script>
    <script src="${ctx}/js/stomp.min.js"></script>

    <@layout.block name="head"></@layout.block>
</head>
<@layout.block name="body"></@layout.block>
<@layout.block name="js"></@layout.block>-->
<script src="${ctx}/layuiAdmin/layui/layui.js"></script>
<script>
    layui.config({
        base: '${ctx}/layuiAdmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use('index');
</script>
<#--<script type="text/javascript">-->
<#--    // websocket客户端-->
<#--    var stompClient = null;-->

<#--    /**-->
<#--     * 连接websocket-->
<#--     */-->
<#--    window.connect = function () {-->
<#--        // 连接 endpoint-->
<#--        var socket = new SockJS('${base}/ws/actionMessage');-->
<#--        // 创建websocket客户端-->
<#--        stompClient = Stomp.over(socket);-->
<#--        // 连接websocket服务端-->
<#--        stompClient.connect({}, function (frame) {-->
<#--            // 订阅一对一消息-->
<#--            stompClient.subscribe('/user/${session_id}/ac', function (response) {-->
<#--                var socketJson = JSON.parse(response.body);-->
<#--                var content = socketJson.content;-->
<#--                var type = socketJson.type.toString();-->
<#--                var dialog = socketJson.dialog.toString();-->
<#--                // 判定弹窗方式-->
<#--                if(dialog === "MSG") {-->
<#--                    // 判定消息类型-->
<#--                    if(type === "INFO") {-->
<#--                        layer.msg(content, {icon: 1});-->
<#--                    }-->

<#--                    if(type === "ERROR") {-->
<#--                        layer.msg(content, {icon: 2});-->
<#--                    }-->
<#--                }-->

<#--                if(dialog === "ALERT") {-->
<#--                    if(type === "INFO") {-->
<#--                        layer.alert(content, {icon: 1});-->
<#--                    }-->

<#--                    if(type === "ERROR") {-->
<#--                        layer.alert(content, {icon: 2});-->
<#--                    }-->
<#--                }-->
<#--            });-->
<#--        });-->
<#--    };-->

<#--    /**-->
<#--     * 断开websocket连接-->
<#--     */-->
<#--    window.disconnect = function () {-->
<#--        if (stompClient != null) {-->
<#--            stompClient.disconnect();-->
<#--        }-->
<#--    };-->

<#--    /**-->
<#--     * 浏览器关闭事件-->
<#--     */-->
<#--    window.onunload = function () {-->
<#--        disconnect();-->
<#--    };-->

<#--    // 启动websocket连接-->
<#--    connect();-->
<#--</script>-->
</html>