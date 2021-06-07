/** 本文件为 音视频逻辑JS **/

// 检查插件是否安装完成定时器
var mRefreshPluginTimer = -1;
// 本机用户编号
var mSelfUserId = -1;
// 房间号
var roomId = 1;
// 服务器地址
var ip = "117.156.94.24";
// 端口
var port = "23158";
// 专家信息
var expertJson = "";

var charMan = "0";

// 特殊人员列表 例如监控室 询标室人员等
var SPECIAL_PEOPLE = [
    // 监控室
    "SPECIAL_JK"
];

// 定义窗口状态常量
var WINDOW_NORMAL = 0;
var WINDOW_MIN = 1;
var WINDOW_MAX = 2;

// 音频状态
const AUDIO_OPEN = "1";
const AUDIO_CLOSE = "0";

// 定义窗口窗台变量 默认值 为 标准窗口
var windowState = WINDOW_NORMAL;

// 音频开启状态 默认开启
var audioState = "1";

/**
 * anychat插件启动函数
 */
function onloadAnyChat() {
    if (navigator.plugins && navigator.plugins.length) {
        window.navigator.plugins.refresh(false);
    }

    // 检查插件安装情况
    // 定义业务层需要的AnyChat API Level
    var NEED_ANYCHAT_APLLEVEL = "0";
    // 初始化插件
    var error_code = BRAC_InitSDK(NEED_ANYCHAT_APLLEVEL);
    console.log(error_code);

    if (error_code === GV_ERR_SUCCESS) {
        // 已安装AnyChat插件
        if (mRefreshPluginTimer !== -1) {
            // 清除插件安装检测定时器
            clearInterval(mRefreshPluginTimer);
        }

        // 延迟等待保证IE浏览器下 有足够的时间初始化SDK
        setTimeout(function () {
            // AnyChat 登录
            login();
        }, 500);
    } else {
        // 未安装AnyChat插件 或者 版本过低
        if (error_code === GV_ERR_PLUGINNOINSTALL) {
            alert("首次进入需要安装插件，请点击下载按钮进行安装！");
        } else if (error_code === GV_ERR_PLUGINOLDVERSION) {
            alert("检测到当前插件的版本过低，请下载安装最新版本！");
        }
        downloadAnyChatPlugin();
        // 设置检测定时器
        /*if(mRefreshPluginTimer === -1) {
            mRefreshPluginTimer = setInterval(onloadAnyChat, 1000);
        }*/
    }
}

/**
 * anychat登录
 */
function login() {
    // 设置回音抑制
    BRAC_SetSDKOption(BRAC_SO_AUDIO_ECHOCTRL, 1);
    BRAC_SetSDKOption(BRAC_SO_AUDIO_NSCTRL, 1);
    console.log("BRAC_SetSDKOption: BRAC_SO_AUDIO_ECHOCTRL");
    // 设置开启屏幕共享
    BRAC_SetSDKOption(BRAC_SO_CORESDK_SCREENCAMERACTRL, 1);
    console.log("BRAC_SetSDKOption: BRAC_SO_CORESDK_SCREENCAMERACTRL");
    // 连接服务器
    var error_code = BRAC_Connect(ip, port);
    changeAudio(audioState);
    console.log("connect: " + error_code);
}

/**
 * BRAC_Connect回调函数 调用后触发一次
 *
 * @param bSuccess 是否连接成功 0:成功
 * @param error_code 出错代码
 */
function OnAnyChatConnect(bSuccess, error_code) {
    console.log("OnAnyChatConnect: " + error_code);
    if (error_code === 0) {
        // 服务器连接成功 进行用户登录
        error_code = BRAC_Login(expertJson, "", 0);
        console.log("login: " + error_code);
    }
}

/**
 * BRAC_Login回调函数 调用后触发一次
 *
 * @param dwUserId 用户编号
 * @param error_code 出错代码 0表示成功
 */
function OnAnyChatLoginSystem(dwUserId, error_code) {
    console.log("OnAnyChatLoginSystem: " + error_code);
    if (error_code === 0) {
        // 设置本机用户编号
        mSelfUserId = dwUserId;
        // 进入房间
        enterRoom();
    }
}

/**
 * 进入房间函数
 */
function enterRoom() {
    var error_code = BRAC_EnterRoom(roomId, "", 0);
    console.log("enter_room: " + error_code);
}

/**
 * BRAC_EnterRoom回调函数 客户端进入房间
 * @param dwRoomId 进入房间的ID号
 * @param error_code 出错代码 0表示成功
 */
function OnAnyChatEnterRoom(dwRoomId, error_code) {
    console.log("OnAnyChatEnterRoom: " + error_code);

    if (error_code === 0) {
        // 评标页面开启屏幕共享但是不进行视频流显示
        // 请求摄像头视频流
        // 设置用户视频流信息
        for (var i = 0; i < 2; i++) {
            BRAC_SetUserStreamInfo(mSelfUserId, i, BRAC_STREAMINFO_VIDEOCODECID, i);
            // 请求用户视频数据
            BRAC_UserCameraControlEx(mSelfUserId, 1, i, 0, "");

            // i === 0 表示摄像头 i == 1表示屏幕共享
            if (i === 0) {
                // 请求用户音频数据
                BRAC_UserSpeakControl(mSelfUserId, 1);

                if (windowState === WINDOW_MAX) {
                    // 设置视频显示位置 根据ID
                    BRAC_SetVideoPosEx(mSelfUserId, GetID("localvideo"), "ANYCHAT_VIDEO_LOCAL");
                }

                if (windowState === WINDOW_NORMAL) {
                    // 设置视频显示位置 根据ID
                    BRAC_SetVideoPosEx(mSelfUserId, GetID("localvideo-normal"), "ANYCHAT_VIDEO_LOCAL_NORMAL");
                }
            }
        }

        // 请求视频录制
        /*record(mSelfUserId, 1);*/
        changeMai()
    }
}

/**
 * 启用视频录制函数
 */
function record(userId, state) {
    //BRAC_SetSDKOption(BRAC_SO_RECORD_VIDEOBR,600000); 视频码率设置
    //BRAC_SetSDKOption(BRAC_SO_RECORD_AUDIOBR,18000); 音频码率设置

    // 录制视频高度
    BRAC_SetSDKOption(BRAC_SO_RECORD_HEIGHT, 1080);
    // 录制视频宽度
    BRAC_SetSDKOption(BRAC_SO_RECORD_WIDTH, 1920);

    //ANYCHAT_VIDEOCLIPMODE_AUTO;    //0;默认模式，以最大比例进行裁剪，然后再整体拉伸，画面保持比例，但被裁剪画面较大
    //ANYCHAT_VIDEOCLIPMODE_OVERLAP; //1;重叠模式，只取最大有效部分，对边缘进行裁剪
    //ANYCHAT_VIDEOCLIPMODE_SHRINK;  //2;缩小模式，缩小到合适的比例，不进行裁剪
    //ANYCHAT_VIDEOCLIPMODE_STRETCH; //3;平铺模式，不进行裁剪，但可能导致画面不成比例
    //ANYCHAT_VIDEOCLIPMODE_DYNAMIC; //4;动态模式，由上层应用根据分辩率来调整显示表面，保持画面不变形

    // 录制视频的裁剪模式
    BRAC_SetSDKOption(BRAC_SO_RECORD_CLIPMODE, ANYCHAT_VIDEOCLIPMODE_DYNAMIC);

    // 自定义录制参数
    var json = {
        "recordlayout": 4,
        "layoutstyle": 0,
        // 多路串流 摄像头 屏幕 音频
        "streamlist": [{
            "userid": userId,
            "streamindex": 0,
            "recordindex": 0
        }, {
            "userid": userId,
            "streamindex": 1,
            "recordindex": 1
        }],
    }

    var jsonParam = JSON.stringify(json);

    console.log(jsonParam);
    // 0x1b37为自定义录像标志 满足系统特殊需求 请勿修改
    console.log("BRAC_StreamRecordCtrlEx： 0x1b37 " + userId + ">" + state);
    BRAC_StreamRecordCtrlEx(userId, state, 0x1b37, 0, jsonParam);

}

/**
 * 本机用户成功进入房间后触发一次 接收房间在线用户信息
 * @param dwUserCount 在线人数 包含本机
 * @param dwRoomId 房间ID
 */
function OnAnyChatRoomOnlineUser(dwUserCount, dwRoomId) {
    console.log("OnAnyChatRoomOnlineUser: " + dwUserCount);
    // 获取在线用户列表
    var user_id_list = BRAC_GetOnlineUser();
    $(".current-online-count").html(user_id_list.length + 1);
    // 循环在线用户列表
    for (var i = 0; i < user_id_list.length; i++) {
        // 获取用户编号
        var userId = user_id_list[i];
        // 评标页面开启屏幕共享但是不进行视频流显示
        // 请求摄像头视频流
        // 设置用户视频流信息
        BRAC_SetUserStreamInfo(userId, 1, BRAC_STREAMINFO_VIDEOCODECID, 0);
        // 请求用户音频数据
        BRAC_UserSpeakControl(userId, 1);

        // 最大化情况下 请求视频流
        if (windowState === WINDOW_MAX) {
            var userName = getUserName(BRAC_GetUserName(userId));

            if (SPECIAL_PEOPLE.indexOf(userName) === -1) {
                createRemoteUserDom(i, userId);
                // 设置视频显示位置 根据ID
                BRAC_SetVideoPosEx(userId, GetID("othervideo" + i), "ANYCHAT_VIDEO_REMOTE" + i, 0);
                // 请求用户视频数据
                BRAC_UserCameraControlEx(userId, 1, 0, 0, "");
            }
        }
    }
    changeMai()
}

/**
 * 用户进入(离开)房间时触发
 * @param dwUserId 用户ID
 * @param bEnterRoom 1:进入房间 0:离开房间
 */
function OnAnyChatUserAtRoom(dwUserId, bEnterRoom) {
    console.log("OnAnyChatUserAtRoom: " + dwUserId + ":" + bEnterRoom);
    if (bEnterRoom === 1) {
        // 进入房间
        if (windowState === WINDOW_MAX) {
            // 获取在线用户列表
            var user_id_list = BRAC_GetOnlineUser();
            $(".current-online-count").html(user_id_list.length + 1);
            destroyRemoteUserDom();

            // 循环在线用户列表
            for (var i = 0; i < user_id_list.length; i++) {
                // 获取用户编号
                var userId = user_id_list[i];

                var userName = getUserName(BRAC_GetUserName(userId));

                if (SPECIAL_PEOPLE.indexOf(userName) === -1) {
                    createRemoteUserDom(i, userId);
                    // 评标页面开启屏幕共享但是不进行视频流显示
                    // 请求摄像头视频流
                    // 设置用户视频流信息
                    BRAC_SetUserStreamInfo(userId, 0, BRAC_STREAMINFO_VIDEOCODECID, 0);
                    // 设置视频显示位置 根据ID
                    BRAC_SetVideoPosEx(userId, GetID("othervideo" + i), "ANYCHAT_VIDEO_REMOTE" + i, 0);
                    // 请求用户视频数据
                    BRAC_UserCameraControlEx(userId, 1, 0, 0, "");
                    // 请求用户音频数据
                    BRAC_UserSpeakControl(userId, 1);
                }
            }
        }
    } else {
        // 退出房间
        // 结束视频请求
        BRAC_UserCameraControl(dwUserId, 0);
        // 结束音频请求
        BRAC_UserSpeakControl(dwUserId, 0);

        console.log("重新排列画面");

        destroyRemoteUserDom();

        // 重新排列画面
        // 进入房间
        // 获取在线用户列表
        var user_id_list = BRAC_GetOnlineUser();
        $(".current-online-count").html(user_id_list.length + 1);
        // 循环在线用户列表
        for (var i = 0; i < user_id_list.length; i++) {
            // 获取用户编号
            var userId = user_id_list[i];

            var userName = getUserName(BRAC_GetUserName(userId));

            if (SPECIAL_PEOPLE.indexOf(userName) === -1) {
                createRemoteUserDom(i, userId);
                // 设置用户视频流信息
                BRAC_SetUserStreamInfo(userId, i, BRAC_STREAMINFO_VIDEOCODECID, i);
                // 设置视频显示位置 根据ID
                BRAC_SetVideoPosEx(userId, GetID("othervideo" + i), "ANYCHAT_VIDEO_REMOTE" + i, i);
                // 请求用户视频数据
                BRAC_UserCameraControlEx(userId, 1, i, 0, "");
                // 请求用户音频数据
                BRAC_UserSpeakControl(userId, 1);
            }
        }
    }
    changeMai()
}

/**
 * 客户端连接服务器成功后, 网络异常中断时触发
 * @param reason 连接断开的原因、
 * @param error_code 出错代码
 */
function OnAnyChatLinkClose(reason, error_code) {
    console.log("OnAnyChatLinkClose: " + reason);
    console.log("网络连接中断! 断线时间：" + new Date());
    console.log("重新连接start");
    window.location.href = window.location.href;
}

/**
 * 录像或拍照完成时 触发回调
 * @param dwUserId 用户编号
 * @param dwErrorCode 出错代码 可用于判断录像/拍照操作是否成功
 * @param lpFileName 表示保存路径 可能存在带有安装路径代替符号
 * @param dwElapse 录制时长 秒
 * @param dwFlags 附带标志 可用户判定是录像还是拍照
 * @param dwParam 自定义参数 整形
 * @param lpUserStr 自定义参数 字符串
 */
function OnAnyChatRecordSnapShotEx2(dwUserId, dwErrorCode, lpFileName, dwElapse, dwFlags, dwParam, lpUserStr) {
    console.log("保存路径:" + lpFileName);
}

/**
 * 根据ID获取文档元素
 */
function GetID(id) {
    console.log("GetID: " + id);
    if (document.getElementById) {
        return document.getElementById(id);
    } else if (window[id]) {
        return window[id];
    }
    return null;
}

/**
 * 窗口最大化
 */
function resizeMax() {
    hide_IWeb2018();
    console.log("resizeMax");
    $("#localvideo-normal").hide();
    $("#main").show();

    var height = document.documentElement.scrollHeight;
    parent.$("#layer-remote-eval-" + roomId).find("iframe:eq(0)").height(height);
    // 设置视频显示位置 根据ID
    BRAC_SetVideoPosEx(mSelfUserId, GetID("localvideo"), "ANYCHAT_VIDEO_LOCAL");

    // 获取在线人员信息
    var user_id_list = BRAC_GetOnlineUser();
    $(".current-online-count").html(user_id_list.length + 1);
    for (var i = 0; i < user_id_list.length; i++) {
        // 获取用户编号
        var userId = user_id_list[i];

        var userName = getUserName(BRAC_GetUserName(userId));
        if (SPECIAL_PEOPLE.indexOf(userName) === -1) {
            // 创建显示位置 DOM元素
            createRemoteUserDom(i, userId);
            // 评标页面开启屏幕共享但是不进行视频流显示
            // 请求摄像头视频流
            // 设置用户视频流信息
            BRAC_SetUserStreamInfo(userId, 0, BRAC_STREAMINFO_VIDEOCODECID, 0);
            // 设置视频显示位置 根据ID
            BRAC_SetVideoPosEx(userId, GetID("othervideo" + i), "ANYCHAT_VIDEO_REMOTE" + i, 0);
            // 请求用户视频数据
            BRAC_UserCameraControlEx(userId, 1, 0, 0, "");
            // 请求用户音频数据
            BRAC_UserSpeakControl(userId, 1);
        }
    }
    changeMai();
}

/**
 * 窗口标准化
 */
function resizeNormal() {
    console.log("resizeNormal");
    show_IWeb2018();
    destroyRemoteUserDom();

    $("#main").hide();
    $("#localvideo-normal").show();
    // 设置视频显示位置 根据ID
    BRAC_SetVideoPosEx(mSelfUserId, GetID("localvideo-normal"), "ANYCHAT_VIDEO_LOCAL_NORMAL");

    // 取消远程人员的串流
    var user_id_list = BRAC_GetOnlineUser();
    $(".current-online-count").html(user_id_list.length + 1);
    for (var i = 0; i < user_id_list.length; i++) {
        // 获取用户编号
        var userId = user_id_list[i];
        if (userId === mSelfUserId) {
            // 评标页面开启屏幕共享但是不进行视频流显示
            // 请求摄像头视频流
            // 设置用户视频流信息
            BRAC_SetUserStreamInfo(userId, 0, BRAC_STREAMINFO_VIDEOCODECID, 0);
            // 设置视频显示位置 根据ID
            BRAC_SetVideoPosEx(userId, GetID("localvideo"), "ANYCHAT_VIDEO_LOCAL", 0);
            // 请求用户视频数据
            BRAC_UserCameraControlEx(userId, 1, 0, 0, "");
            // 请求用户音频数据
            BRAC_UserSpeakControl(userId, 1);
        } else {
            // 结束视频串流
            BRAC_UserCameraControl(userId, 0);
            // 结束音频请求
            // BRAC_UserSpeakControl(userId, 0);
        }
    }
}

/**
 * 窗口最小化
 */
function resizeMin() {
    console.log("resizeMin");
    destroyRemoteUserDom();

    $("#main").hide();
    $("#localvideo-normal").hide();

    // 取消远程人员的串流
    var user_id_list = BRAC_GetOnlineUser();

    for (var i = 0; i < user_id_list.length; i++) {
        // 获取用户编号
        var userId = user_id_list[i];
        if (userId !== mSelfUserId) {
            // 结束视频串流
            BRAC_UserCameraControl(userId, 0);
            // 结束音频请求
            // BRAC_UserSpeakControl(userId, 0);
        }
    }
}

/**
 * 根据i创建用于显示远程视频流的DOM元素
 * @param i 代表第几个DOM
 * @param userid 用户id
 */
function createRemoteUserDom(i, userid) {
    var json = BRAC_GetUserName(userid);

    var domstr = "<li>"
        + "<div class='face' id='othervideo" + i + "'></div>"
        + "<div class='name'>"
        + getUserName(json)
        + "<span>";

    if (getChairMan(json) === '1') {
        domstr += "(评标组长)</span><i data-status='1' onclick=\"changeSingleAudio('" + userid + "', this)\"></i></div>"
    } else {
        domstr += "</span><i data-status='1' onclick=\"changeSingleAudio('" + userid + "', this)\"></i></div>"
    }
    console.log("createRemoteUserDom" + i + " : " + domstr);
    $("#expert-video").append(domstr);
}

/**
 * 销毁所有远程视频流的DOM元素
 */
function destroyRemoteUserDom() {
    console.log("destroyRemoteUserDom");
    $("div[id^='othervideo']").parents("li").remove();
}

/**
 * 收到文字消息时 触发回调
 * @param dwFromUserId
 * @param dwToUserId
 * @param bSecret
 * @param lpMsgBuf
 * @param dwLen
 */
function OnAnyChatTextMessage(dwFromUserId, dwToUserId, bSecret, lpMsgBuf, dwLen) {
    console.log("OnAnyChatTextMessage");
    showMsg(dwFromUserId, lpMsgBuf);
}

/**
 * 显示文字消息
 * @param userId
 * @param msg
 */
function showMsg(userId, msg) {
    console.log("showMsg——" + userId + " : " + msg);
    var msg = "<li>"
        + "<p>" + getUserName(BRAC_GetUserName(userId)) + "&emsp;" + GetTheTime() + "</p>"
        + "<span>" + msg + "</span>"
        + "</li>"

    $('#show-msg').append(msg);

    // 控制滚动条 自动滚动到最底部
    $('#show-msg').scrollTop($('#show-msg')[0].scrollHeight);
}

//获取当前时间  (00:00:00)
function GetTheTime() {
    var TheTime = new Date();
    return TheTime.toLocaleTimeString();
}

// 切换音频状态
function changeAudio(status) {
    audioState = status;
    console.log("change all Audio: " + audioState);
    // 改变音频标志和图标
    $("#expert-video li").each(function (index) {
        var $i = $(this).find("div[class='name'] i").eq(0);
        console.log(index + "onload:" + $i.data("status"));
        if (audioState === AUDIO_OPEN) {
            $i.data("status", AUDIO_CLOSE);
            $i.css("background-image", "url(/img/anychat/shutup.png)");
        } else {
            $i.data("status", AUDIO_OPEN);
            $i.css("background-image", "url(/img/anychat/openup.png)");
        }
    });

    // 调用anychat实现音频切换
    // 切换本地用户音频
    if (audioState === AUDIO_OPEN) {
        BRAC_UserSpeakControl(-1, 0);
        console.log("local user BRAC_UserSpeakControl: 0");
    } else {
        BRAC_UserSpeakControl(-1, 1);
        console.log("local user BRAC_UserSpeakControl: 1");
    }

    // 切换非本地用户音频
    var user_id_list = BRAC_GetOnlineUser();
    if (audioState === AUDIO_OPEN) {
        for (var i = 0; i < user_id_list.length; i++) {
            // 获取用户编号
            var userId = user_id_list[i];
            // 闭麦
            BRAC_UserSpeakControl(userId, 0);
            console.log(userId + "BRAC_UserSpeakControl: 0");
        }
        audioState = AUDIO_CLOSE;
    } else {
        for (var i = 0; i < user_id_list.length; i++) {
            // 获取用户编号
            var userId = user_id_list[i];
            // 开麦
            BRAC_UserSpeakControl(userId, 1);
            console.log(userId + "BRAC_UserSpeakControl: 1");
        }
        audioState = AUDIO_OPEN;
    }
}

// 切换音频状态
function changeSingleAudio(userId, e) {
    console.log("change single Audio: " + userId);
    var status = $(e).data("status") + "";
    if (status === AUDIO_OPEN) {
        $(e).data("status", AUDIO_CLOSE);
        $(e).css("background-image", "url(/img/anychat/shutup.png)")
        // 闭麦
        BRAC_UserSpeakControl(userId, 0);
        console.log(userId + "BRAC_UserSpeakControl: 0");
    } else {
        $(e).data("status", AUDIO_OPEN);
        $(e).css("background-image", "url(/img/anychat/openup.png)")
        // 闭麦
        BRAC_UserSpeakControl(userId, 1);
        console.log(userId + "BRAC_UserSpeakControl: 1");
    }
}

// 根据专家信息json 获取专家姓名
function getUserName(json) {
    var expert = JSON.parse(json);
    console.log("getUserName: " + expert.name);
    return expert.name;
}

// 根据专家信息json 获取专家是否为评标组长
function getChairMan(json) {
    var expert = JSON.parse(json);
    console.log("getChairMan: " + expert.chairMan);
    return expert.chairMan;
}

// 专家组长可以控制全员音频状态
function changeMai() {
    if (charMan === "1") {
        $("#expert-video li").each(function () {
            var $i = $(this).find("div[class='name'] i").eq(0);
            $i.addClass("mai");
        });
    }
}