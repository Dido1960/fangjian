/** 本文件为anychat.jsp 页面 音视频逻辑JS **/

// 检查插件是否安装完成定时器
var mRefreshPluginTimer = -1;
// 本机用户编号
var mSelfUserId = -1;
// 房间号
var roomId = 1;
// 服务器 IP 地址或网站域名（URL）地址
var ip = "117.156.94.24";
// 服务器端口号（默认端口号为：8906）
var port = "23158";
// 登录信息
var loginJson = "";
// 特殊人员列表 例如监控室 询标室人员等
var SPECIAL_PEOPLE = [
    // 监控室
    "SPECIAL_JK"
];

/**
 * anychat插件启动函数
 */
function onloadAnyChat() {
    console.log("start onload AnyChat");
    if (navigator.plugins && navigator.plugins.length) {
        window.navigator.plugins.refresh(false);
    }

    // 检查插件安装情况
    // 定义业务层需要的AnyChat API Level，最低 API 版本号，默认为“0”
    var API_LEVEL = "0";
    // 初始化插件
    var error_code = BRAC_InitSDK(API_LEVEL);
    console.log("BRAC_InitSDK: " + error_code);

    if (error_code === GV_ERR_SUCCESS) {
        // 已安装AnyChat插件
        if (mRefreshPluginTimer !== -1) {
            // 清除插件安装检测定时器
            clearInterval(mRefreshPluginTimer);
        }

        // 延迟等待保证IE浏览器下 有足够的时间初始化SDK
        setTimeout(function () {
            // AnyChat 登录
            login_BRAC();
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
        if (mRefreshPluginTimer === -1) {
            mRefreshPluginTimer = setInterval(onloadAnyChat, 1000);
        }
    }
}

/**
 * AnyChat登录
 */
function login_BRAC() {
    // 连接服务器
    console.log("connect " + ip + " : " + port);
    var error_code = BRAC_Connect(ip, port);
    console.log("connect: " + error_code);
}

/**
 * BRAC_Connect回调函数 调用后触发一次
 * @param b_success 是否连接成功 0:成功
 * @param error_code 出错代码
 */
function OnAnyChatConnect(b_success, error_code) {
    console.log("OnAnyChatConnect: " + error_code);
    if (error_code === 0) {
        // 服务器连接成功 进行用户登录
        error_code = BRAC_Login(loginJson, "", 0);
        console.log("login: " + error_code);
    }
}

/**
 * BRAC_Login回调函数 调用后触发一次
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
}

/**
 * 本机用户成功进入房间后触发一次 接收房间在线用户信息
 * @param dwUserCount 在线人数 包含本机
 * @param dwRoomId 房间ID
 */
function OnAnyChatRoomOnlineUser(dwUserCount, dwRoomId) {
    console.log("OnAnyChatRoomOnlineUser: " + dwUserCount);
    // 获取在线用户列表
    // 进入房间
    // 获取在线用户列表
    var user_id_list = BRAC_GetOnlineUser();

    // 循环在线用户列表
    for (var i = 0; i < user_id_list.length; i++) {
        // 获取用户编号
        var userId = user_id_list[i];

        createRemoteUserDom(i, userId);

        for (var j = 0; j < 2; j++) {
            // 评标页面开启屏幕共享但是不进行视频流显示
            // 请求摄像头视频流
            // 设置用户视频流信息
            BRAC_SetUserStreamInfo(userId, j, BRAC_STREAMINFO_VIDEOCODECID, j);
            // 设置视频显示位置 根据ID
            BRAC_SetVideoPosEx(userId, GetID("othervideo" + i + j), "ANYCHAT_VIDEO_REMOTE" + i + j, j);
            // 请求用户视频数据
            BRAC_UserCameraControlEx(userId, 1, j, 0, "");
            // 请求用户音频数据
            BRAC_UserSpeakControl(userId, 1);
        }
    }
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
        // 获取在线用户列表
        var user_id_list = BRAC_GetOnlineUser();

        destroyRemoteUserDom();

        // 循环在线用户列表
        for (var i = 0; i < user_id_list.length; i++) {
            // 获取用户编号
            var userId = user_id_list[i];

            createRemoteUserDom(i, userId);

            for (var j = 0; j < 2; j++) {
                // 评标页面开启屏幕共享但是不进行视频流显示
                // 请求摄像头视频流
                // 设置用户视频流信息
                BRAC_SetUserStreamInfo(userId, j, BRAC_STREAMINFO_VIDEOCODECID, j);
                // 设置视频显示位置 根据ID
                BRAC_SetVideoPosEx(userId, GetID("othervideo" + i + j), "ANYCHAT_VIDEO_REMOTE" + i + j, j);
                // 请求用户视频数据
                BRAC_UserCameraControlEx(userId, 1, j, 0, "");
                // 请求用户音频数据
                BRAC_UserSpeakControl(userId, 1);
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
        var useridlist = BRAC_GetOnlineUser();

        // 循环在线用户列表
        for (var i = 0; i < useridlist.length; i++) {
            // 获取用户编号
            var userId = useridlist[i];

            createRemoteUserDom(i, userId);

            for (var j = 0; j < 2; j++) {
                // 评标页面开启屏幕共享但是不进行视频流显示
                // 请求摄像头视频流
                // 设置用户视频流信息
                BRAC_SetUserStreamInfo(userId, j, BRAC_STREAMINFO_VIDEOCODECID, j);
                // 设置视频显示位置 根据ID
                BRAC_SetVideoPosEx(userId, GetID("othervideo" + i + j), "ANYCHAT_VIDEO_REMOTE" + i + j, j);
                // 请求用户视频数据
                BRAC_UserCameraControlEx(userId, 1, j, 0, "");
                // 请求用户音频数据
                BRAC_UserSpeakControl(userId, 1);
            }
        }
    }
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
 * 根据i创建用于显示远程视频流的DOM元素
 * @param i 代表第几个DOM
 * @param user_id 用户
 */
function createRemoteUserDom(i, user_id) {
    var json = BRAC_GetUserName(user_id);

    var userName = getUserName(json);
    if (SPECIAL_PEOPLE.indexOf(userName) === -1) {
        var domstr = "<li><p>" + getUserName(json);

        if (getChairMan(json) === '1') {
            domstr += "<span>组长</span>";
        }

        domstr += "<b>" + (i+1) + "</b></p>"
            + "<div class='face'>"
            + "<div id='othervideo" + i + "0'></div>"
            + "<div id='othervideo" + i + "1'></div>"
            + "</div></li>";

        console.log("createRemoteUserDom" + i + " : " + domstr);
        $("#expert-video").append(domstr);
    }
}

/**
 * 销毁所有远程视频流的DOM元素
 */
function destroyRemoteUserDom() {
    console.log("destroyRemoteUserDom");
    $("div[id^='othervideo']").parents("li").remove();
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

// 下载AnyChat SDK插件
function downloadAnyChatPlugin() {
    var anychat_plugin_url = 'http://anychat.oss-cn-hangzhou.aliyuncs.com/AnyChatWebSetup.exe';
    var anychat_plugin_x64_url = 'http://anychat.oss-cn-hangzhou.aliyuncs.com/AnyChatWebSetup_x64.exe';
    if (window.navigator.platform == 'Win64'){
        // 64位插件
        window.location.href = anychat_plugin_x64_url;
    } else {
        window.location.href = anychat_plugin_url;
    }
}