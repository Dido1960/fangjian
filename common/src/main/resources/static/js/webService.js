/**
 * 初始化默认最小
 */
function showMinOnlinePage() {

    var index = layer.open({
        type: 2,
        title: '<span id="message_count">消息盒子</span>',
        id: 'onlineChatInfo',
        area: ['1005px', '768px'],
        offset: "c",
        closeBtn: 0,
        maxmin: true,
        shade: 0,
        resize: false,
        scrollbar: false,
        content: '/dist/index.html#/web-chat',
        full: function () {
            hide_IWeb2018()
        },
        success: function () {
            console.log('success')
            postMessageFun({listenType: "WINDOW_MIN"});
            //保证始终layer居中
        },
        min: function () {
            show_IWeb2018()
            //记录本地layer窗口的值
            localStorage.setItem("onlineChatInfo", "WINDOW_MIN");
            //保证始终layer居左下角
            $('#layui-layer' + index).css({
                'bottom': '0px',
                'left': '0px',
            })
        },
        restore: function () {
            hide_IWeb2018()
            //记录本地layer窗口的值
            localStorage.setItem("onlineChatInfo", "WINDOW_RESET");
            //保证始终layer居中
            $('#layui-layer' + index).css({
                'top': 'calc(50% - 384px)',
                'left': 'calc(50% - 502px)',
            })
            postMessageFun({listenType: "WINDOW_RESET"});

        },
        end: function () {
            show_IWeb2018()
        }
    });
    //获取用户是否已经点击过最小化
    var state = localStorage.getItem("onlineChatInfo");
    if (state == "WINDOW_MIN") {
        layer.min(index);
        postMessageFun({listenType: "WINDOW_MIN"});
    }
}

/***
 * 即时通讯当前窗口状态
 * WINDOW_MIN 最小化,WINDOW_RESET正常
 * ***/
function postMessageFun(data) {
    var onlineIframe = top.$('#onlineChatInfo').find('iframe')[0];
    // 消息盒子初始化成功，才执行
    if (onlineIframe!=undefined){
        var contentWindow = top.$('#onlineChatInfo').find('iframe')[0].contentWindow;
        contentWindow.postMessage(data, '*');
    }
}

/***
 * 即使通讯内部消息提示
 * ***/
window.addEventListener("message", function (e) {
    if (e.data.count > 0) {
        $("#message_count").html('未读 :<span  style="color: red ">' + e.data.count + '</span>');
    } else {
        $("#message_count").html('消息盒子');
    }

    if (e.data.isShowLayer) {
        layui.use("layer", function () {
            layui.layer.msg(e.data.str);
        });
    }

    console.log(e.data.forbiddenDataMessage);
    if (e.data.forbiddenDataMessage){
        // layer.msg(e.data.forbiddenDataMessage);
        layerLoading(e.data.forbiddenDataMessage, 0, 2);
    }
});

/**
 * 隐藏信息
 * * */
function hide_IWeb2018() {
    try {
        var iWebPDF2018 = document.getElementById("showPdfView1").contentWindow.document.getElementById('showPdfView').contentWindow.iWebPDF2018;
        // 如果直接通过组件获取插件信息获取不到，则通过外层iframe的id获取（注意id必须为showPdfIframe，iframe的id）
        if (isNull(iWebPDF2018)) {
            iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        }
        var addins = iWebPDF2018.iWebPDFFun;

        if (getIEVersion() >= 9) {
            addins.hide = false;
        } else {
            iWebPDF2018.HidePlugin(false);
        }
    } catch (e) {
        console.log(e)
    }
}

/**
 * 显示信息
 * * */
/**
 * 显示信息
 * * */
function show_IWeb2018() {
    try {
        var iWebPDF2018 = document.getElementById("showPdfView1").contentWindow.document.getElementById('showPdfView').contentWindow.iWebPDF2018;
        // 如果直接通过组件获取插件信息获取不到，则通过外层iframe的id获取（注意id必须为showPdfIframe，iframe的id）
        if (isNull(iWebPDF2018)) {
            iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        }
        var addins = iWebPDF2018.iWebPDFFun;
        console.log("hide_IWeb2018" + addins.hide)
        if (getIEVersion() >= 9) {
            addins.hide = true;
        } else {
            iWebPDF2018.HidePlugin(true);
        }

    } catch (e) {
        console.log(e)
    }
}

//消息导出
var noShowWidowIndex;
function initNoShowMesgBox(success) {
    noShowWidowIndex = layer.open({
        type: 2,
        title: '<span id="message_count">消息盒子</span>',
        id: 'onlineChatInfo',
        area: ['1005px', '768px'],
        offset: "c",
        skin: 'postion-out',
        closeBtn: 0,
        maxmin: true,
        shade: 0,
        resize: false,
        scrollbar: false,
        content: '/dist/index.html#/web-chat',
        success: function () {
            if (success && typeof (success) == "function") {
                success();
            }
        },
    });
}

function clearNoShow() {
    if (!isNull(noShowWidowIndex)){
        layer.close(noShowWidowIndex);
    }
}