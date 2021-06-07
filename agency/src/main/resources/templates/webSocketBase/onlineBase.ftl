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
    <script src="${ctx}/js/common.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
</head>

<script type="text/javascript">
    /**
     * 创建信息
     *
     */
    function webSocket(url, msg, message, open, error, close) {
        var websocket;
        if ('WebSocket' in window) {
            console.log("此浏览器支持websocket");
            websocket = new WebSocket("ws://" + window.location.host + url);
        } else if ('MozWebSocket' in window) {
            alert("此浏览器只支持MozWebSocket");
        } else {
            alert("此浏览器只支持SockJS");
        }
        websocket.onopen = function (evnt) {
            websocket.send(msg);
            if (open && typeof (open) == "function") {
                open();
            }
        };
        websocket.onmessage = function (evnt) {
            console.log(evnt.data + "收到消息");
            if (message && typeof (message) == "function") {
                message(evnt.data);
            }
        };
        websocket.onerror = function (evnt) {
            if (error && typeof (error) == "function") {
                error(evnt.data);
            }
        };
        websocket.onclose = function (evnt) {
            if (close && typeof (close) == "function") {
                close(evnt.data);
            }
        }

    }

    /**
     * 关闭连接
     */
    function webSocket_Close() {
        if ('WebSocket' in window) {
            console.log("此浏览器支持websocket");
            WebSocket.close();
        } else if ('MozWebSocket' in window) {
            alert("此浏览器只支持MozWebSocket");
        } else {
            alert("此浏览器只支持SockJS");
        }
    }
</script>
</html>