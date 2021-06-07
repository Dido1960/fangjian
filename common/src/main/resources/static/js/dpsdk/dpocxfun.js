/***  大华SDK OCX 业务逻辑 JS ***/

/**
 * 实例信息数组对象
 * 
 * 每个对象 含有以下参数
 * mainWindowId 主窗口ID
 * login 登录状态
 */
var ins = [];

// 码流定义
// 主码流
const STREAM_MAIN = 1;
// 辅码流
const STREAM_SUP = 2;

// 音视频类型定义
// 视频
const MEDIA_VIDEO = 1;
// 音频
const MEDIA_AUDIO = 2;
// 视频 + 音频
const MEDIA_NORMAL = 3;

// 协议类型
const TRANS_TCP = 1;
const TRANS_UDP = 0;

/**
 * 初始化大华SDK
 * @param sdk_id OCX控件ID
 * @param reg_code 地区行政区划代码
 * @param room_name 评标室名称
 */
function initDPSDK(sdk_id, reg_code, room_name) {
    console.log("initDPSDK: " + sdk_id);

    // 定义实例信息对象 并保存
    var INS = ins[sdk_id] = {};
    INS.room_name = room_name;
    ins.push(INS);
    
    // 获取对象
    var SDK = document.getElementById(sdk_id);
    
    /**
     * 创建主窗口
     * @param nLeft 左坐标, 范围0~100
     * @param nTop 上坐标, 范围0~100
     * @param nRight 右坐标, 范围0~100
     * @param nBottom 下坐标, 范围0~100
     * @return 主窗口ID -1表示失败 通过DPSDK_GetLastError获取错误码
     */

    // WARNING 参数勿变动 特定参数
    var mainWindowId = SDK.DPSDK_CreateSmartWnd(0, 0, 100, 100);
    
    if(mainWindowId === -1) {
        console.log("DPSDK_CreateSmartWnd: ERROR; ERRORCODE= " + SDK.DPSDK_GetLastError());
        return;
    }
    
    // 记录主窗口ID
    INS.mainWindowId = mainWindowId;
    
    // 设置窗口数量 默认为1
    var ret = SDK.DPSDK_SetWndCount(mainWindowId, 1);
    showRetMsg(ret, "DPSDK_SetWndCount");

    // 选中窗口
    SDK.DPSDK_SetSelWnd(mainWindowId, 0);

   /* SDK.DPSDK_SetToolBtnVisible(1, false);
    SDK.DPSDK_SetToolBtnVisible(7, false);
    SDK.DPSDK_SetToolBtnVisible(9, false);
    SDK.DPSDK_SetControlButtonShowMode(1, 0);
    SDK.DPSDK_SetControlButtonShowMode(2, 0);
    */
    // 登录
    login_DPSDK(sdk_id, reg_code);
}

/**
 * 同步登录
 * @param sdk_id OCX控件ID
 * @param reg_code 地区行政区划代码
 */
function login_DPSDK(sdk_id, reg_code) {
    var SDK = document.getElementById(sdk_id);
    // 获取登录信息
    var login = getLoginInfo(reg_code);

    if(!login) {
        console.log("登录信息获取错误: " + reg_code);
        return;
    }
    
    // 登录
    console.log("IP: " + login.ip);
    console.log("PORT: " + login.port);
    console.log("USERNAME: " + login.userName);
    console.log("PWD: " + login.pwd);
    var ret = SDK.DPSDK_Login(login.ip, login.port, login.userName, login.pwd);
    
    showRetMsg(ret, '登录');
    
    if(ret === 0) {
        // 记录登录状态
        var INS = getInstance(sdk_id);
        INS.login = 1;
        
        loadOrgInfo(sdk_id);
    }
}

/**
 * 加载组织结构
 * @param sdk_id OCX控件ID
 */
function loadOrgInfo(sdk_id) {
    var SDK = document.getElementById(sdk_id);
    
    var ret = SDK.DPSDK_LoadDGroupInfo();
    
    showRetMsg(ret, "加载组织结构");
    
    if(ret === 0) {
        getOrgInfo(sdk_id);
    }
}

/**
 * 获取组织结构
 * @param sdk_id OCX控件ID
 */
function getOrgInfo(sdk_id) {
    var SDK = document.getElementById(sdk_id);
    var org_info = SDK.DPSDK_GetDGroupStr();

    var INS = getInstance(sdk_id);

    // 解析记录组织结构信息
    parseOrgXml(sdk_id, org_info, INS.room_name);
}

/**
 * 评标室名称 解析组织结构
 * @param sdk_id
 * @param org_info
 * @param room_name
 */
function parseOrgXml(sdk_id, org_info, room_name) {
    $.ajax({
        url: "/gov/parseDpSdkOrgXml",
        type: "POST",
        data: {
            "orgInfoXml": org_info,
            "roomName": room_name
        },
        success: function(data) {
            if(!data) {
                console.log("组织结构解析NPE!");
                return;
            }

            var orgMap = data;

            var channels = [];
            $.each(orgMap, function (key, value) {
                channel = {};
                channel.name = key;
                channel.id = value[0];
                channel.status = value[1];
                channels.push(channel);
            });

            var INS = getInstance(sdk_id);
            console.log("channels: " + JSON.stringify(channels));
            INS.channels = channels;
            var channelLen = INS.channels.length;
            console.log("channelLen: " + channelLen);
            // 查询在线状态的 第一个通道ID
            var channelId;
            for(var i = 0; i < channelLen; i++) {
                console.log("status: " + INS.channels[i].status);
                if(INS.channels[i].status === '1') {
                    channelId = INS.channels[i].id;
                    break;
                }
            }

            // 视频播放
            playVideo(sdk_id, channelId);
        }
    })
}


/**
 * 根据控件ID 和 视频通道ID 播放视频
 * @param sdk_id OCX控件ID
 * @param channelId 视频通道ID
 */
function playVideo(sdk_id, channelId) {
    console.log("playVideo: " + channelId);
    var SDK = document.getElementById(sdk_id);
    var INS = getInstance(sdk_id);

    var nWndNo = SDK.DPSDK_GetSelWnd(INS.mainWindowId);
    var ret = SDK.DPSDK_AsyncStartRealplayByWndNo(INS.mainWindowId, nWndNo, channelId, STREAM_MAIN, MEDIA_NORMAL, TRANS_TCP);
    
    showRetMsg(ret, "播放视频");

    /*if(ret == 0)
    {
        showRetMsg(SDK.DPSDK_SetIvsShowFlagByWndNo(INS.mainWindowId, nWndNo, 1, 1),"规则线显示");//打开规则线显示
        showRetMsg(SDK.DPSDK_SetIvsShowFlagByWndNo(INS.mainWindowId, nWndNo, 2, 1),"目标框显示");//打开目标框显示
        showRetMsg(SDK.DPSDK_SetIvsShowFlagByWndNo(INS.mainWindowId, nWndNo, 3, 1),"轨迹线显示");//打开轨迹线显示
    }*/

}

/**
 * 根据SDK 控件ID 获取实例信息
 * @param sdk_id OCX控件ID
 */
function getInstance(sdk_id) {
    console.log("getInstance: " + sdk_id);
    return ins[sdk_id];
}

/**
 * 打印错误日志
 * @param ret 错误码
 * @param str 步骤名称
 */
function showRetMsg(ret, str) {
    if(ret === 0) {
        console.log(str + ' SUCCESS!');
    } else {
        if(ret === 19 || ret === 20) {
            console.log(str + ' ERROR, 请先加载组织结构!');
        } else {
            console.log(str + ' ERROR, ERRORCODE: ' + ret);
        }

    }
}

/**
 * 根据行政区划代码获取平台登录信息
 * @param code
 * @returns {*}
 */
function getLoginInfo(code) {
    var loginMessage = null;
    $.ajax({
        url: "/gov/getDHLoginInfo",
        type: "POST",
        async: false,
        data: {
            "regCode": code
        },
        success: function(data) {
            if(!isNull(data)){
                loginMessage = data;
            }
        },
        error:function (data) {

        }
    });
    if(isNull(loginMessage)){
        layer.alert("配置文件为空!");
        return ;
    }
    return loginMessage;
}
