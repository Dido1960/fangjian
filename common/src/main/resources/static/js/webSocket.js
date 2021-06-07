/****
 * 创建信息
 * **/
function webSocket(url, msg, message, open, error, close) {
    var websocket;
    if ('WebSocket' in window) {
        console.log("此浏览器支持websocket");
        websocket = new WebSocket("ws://" + window.location.host + "" + url);
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
    //发送消息

}

/****
 * 关闭连接
 * ****/
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