var csrf_token = $("meta[name='_csrf']").prop("content");
var csrf_header = $("meta[name='_csrf_header']").prop("content");

// Spring-Security CSRF
// 页面发起Ajax请求时自动设置CSRF Header
if (csrf_token && csrf_header) {
    // 设置全局AJAX请求
    $.ajaxSetup({
        type: "POST",
        cache: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrf_header, csrf_token);
        },
        error:function (xhr, textStatus, error){
            if(xhr.status == 403){
                console.warn("用户登录失效！！！")
                window.top.location.href = "/login.html";
            }
        }
    })
} else {
    console.error('页面未设置跨域参数 <meta name="_csrf"  />\n' +
        '    <meta name="_csrf_header" />');
}

$(function () {
    $.ajax({
        url: '/common/systemMessage/getNewMessage',
        type: 'post',
        cache: false,
        success: function (data) {
            if (!isNull(data)) {
                var type = data.type;
                var dialog = data.dialog;
                var content = data.msg;
                // 判定弹窗方式
                if (dialog === "MSG") {
                    // 判定消息类型
                    if (type === "INFO") {
                        layerSuccess(content, function () {
                            removeMessage()
                        });
                    }

                    if (type === "ERROR") {
                        layerError(content, function () {
                            removeMessage()
                        });
                    }
                }

                if (dialog === "ALERT") {
                    if (type === "INFO") {
                        layerAlert(content, function () {
                            removeMessage()
                        });
                    }

                    if (type === "ERROR") {
                        layerAlert(content, function () {
                            removeMessage()
                        });
                    }
                }
            }
        },
        error: function (data) {
            console.log(data);
        },
    });
})

function removeMessage() {
    $.ajax({
        url: '/common/systemMessage/removeMessage',
        type: 'post',
        cache: false,
        success: function (data) {

        },
        error: function (data) {
            console.log(data);
        }
    });
}

/**
 *
 * @param obj
 *
 * @return boolean
 * @author lesgod
 * @date 2020/5/7 14:38
 */
function isNull(obj) {
    var flg = false;
    if (typeof (obj) === "string") {
        obj = obj.replace(/^\s+|\s+$/gm, '');
    }
    if (obj === null || obj === "" || obj == undefined || obj === "undefined" || obj === "null" || obj === "NULL") {
        flg = true;
    }
    return flg;
}

/*将表单序列化成一个对象*/
function serializeObject(form) {
    var o = {};
    $.each($(form).serializeArray(), function (index) {
        if (o[this['name']]) {
            o[this['name']] = o[this['name']] + "," + this['value'];
        } else {
            if (!isNull(this['value'])) {
                o[this['name']] = this['value'] + "";
            }

        }
    });
    return o;
}

/**
 * 转换为金额格式化
 * @param num
 * @returns {string}
 */
function formatCurrency(num) {
    num = num.toString().replace(/\$|\,/g, '');
    if (isNaN(num))
        num = "0";
    var sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    var cents = num % 100;
    num = Math.floor(num / 100).toString();
    if (cents < 10)
        cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
        num = num.substring(0, num.length - (4 * i + 3)) + ',' +
            num.substring(num.length - (4 * i + 3));
    return (((sign) ? '' : '-') + num + '.' + cents);
}

var sleep = function (time) {
    var startTime = new Date().getTime() + parseInt(time, 10);
    while (new Date().getTime() < startTime) {
    }
};

/**
 * 对时间只有一位的进行0补齐
 * @param str
 * @returns {string}
 */
function timeAdd0(str) {
    if (str.length <= 1) {
        str = '0' + str;
    }
    return str
}

/**
 * 退出登录
 */
function exitSystem() {
    hide_IWeb2018();
    layer.confirm("确认要退出系统？", {
        icon: 3,
        end: function() {
            show_IWeb2018();
        }
    }, function (index) {
        show_IWeb2018();
        layer.close(index);
        $.ajax({
            url: '/logout',
            type: 'post',
            cache: false,
            async: false,
            success: function () {
                window.location.href = "/login.html";
            }
        });
    }, function (index) {
        // 取消的回调函数
        layer.msg("已取消!", {icon: 1});
        layer.close(index);
    });
}

var having_layer_count = 0;
var loadingindex;
/**
 *
 * @param msg 提示消息
 * @param success 成功执行函数
 * @param end 结束执行
 * @param time 遮罩层时间
 * @param icon 图标
 * @param shade 遮罩透明度
 * @returns {*}
 */
function layerLoading(msg, icon, time, shade, success, end) {
    var showtime;
    if (isNull(time)) {
        showtime = 30 * 1000
    } else {
        showtime = time * 1000;
    }
    if (isNaN(showtime)) {
        showtime = 30 * 1000;
    }

    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        if (msg) {
            loadingindex = window.top.layer.msg(msg, {
                id: 'systemMessage',
                icon: isNull(icon) ? 16 : icon,
                shade: isNull(shade) ? [0.3, '#393D49'] : shade,
                time: showtime,
                success: function (layero) {
                    if (success && typeof (success) == "function") {
                        success(layero);
                    }
                }, end: function (layero) {
                    if (isSelfWindow) {
                        show_IWeb2018();
                    }
                    if (end && typeof (end) == "function") {
                        end(layero);
                    }
                }
            });
        } else {
            loadingindex = window.top.layer.msg("加载中，请稍等...", {
                id: 'systemMessage',
                icon: isNull(icon) ? 16 : icon,
                shade: isNull(shade) ? [0.3, '#393D49'] : shade,
                time: showtime,
                success: function (layero) {
                    if (success && typeof (success) == "function") {
                        success(layero);
                    }
                }, end: function (layero) {
                    if (isSelfWindow) {
                        show_IWeb2018();
                    }
                    if (end && typeof (end) == "function") {
                        end(layero);
                    }
                }
            });
        }
    }, 400);
}

/**
 * 加载层  无关闭PDF插件  无延时
 * @param msg 提示消息
 * @param success 成功执行函数
 * @param end 结束执行
 * @param time 遮罩层时间
 * @param icon 图标
 * @param shade 遮罩透明度
 * @returns {*}
 */
function doLoading(msg, icon, time, shade, success, end) {
    var showtime;
    if (isNull(time)) {
        showtime = 5 * 1000
    } else {
        showtime = time * 1000;
    }
    if (isNaN(showtime)) {
        showtime = 30 * 1000;
    }

    if(isNull(msg)){
        msg = "加载中，请稍等...";
    }

    loadingindex = window.top.layer.msg(msg, {
        id: 'systemMessage',
        icon: isNull(icon) ? 16 : icon,
        shade: isNull(shade) ? [0.3, '#393D49'] : shade,
        time: showtime,
        success: function (layero) {
            if (success && typeof (success) == "function") {
                success(layero);
            }
        }, end: function (layero) {
            if (end && typeof (end) == "function") {
                end(layero);
            }
        }
    });
    return loadingindex;
}

/**
 * 加载完成
 */
function loadComplete() {
    if (!isNull(loadingindex)) {
        window.top.layer.close(loadingindex);
        layer.close(loadingindex);
    }
}


/**
 * 错误信息弹窗
 * @param msg 提示消息
 * @param callback 回调函数
 * @param time 消失时间
 */
function layerError(msg, callback, time) {
    if (having_layer_count > 0) {
        layer.close(loadingindex);
        having_layer_count = 0;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    layer.close(loadingindex);
    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        systemMessage(1, msg, true, function () {
            if (isSelfWindow) {
                show_IWeb2018();
            }
            having_layer_count--;
            if (callback && typeof (callback) == "function") {
                callback();
            }
        }, time)
    }, 400);
}

/**
 * 消息弹出窗提示
 * @param msg 消息
 * @param callback 回调函数
 * @param successCallback 成功回调执行函数
 */
function layerAlert(msg, callback, successCallback, icon) {
    if (having_layer_count > 0) {
        layer.close(loadingindex);
        having_layer_count = 0;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    layer.close(loadingindex);
    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        window.top.layer.alert(msg, {
            icon: isNull(icon) ? 2 : icon,
            id: 'layerAlert',
            yes: function (index, layero) {
                window.top.layer.close(index)
                if (callback && typeof (callback) == "function") {
                    callback();
                }
            }, end: function () {
                if (isSelfWindow) {
                    show_IWeb2018();
                }
                having_layer_count--;
            }, success: function () {
                if (successCallback && typeof (successCallback) == "function") {
                    successCallback();
                }
            }
        });
    }, 400);
}

/**
 * 消息弹出窗提示 包含end方式
 * @param msg 消息
 * @param callback 回调函数
 * @param endBack
 * @param successCallback 成功回调执行函数
 * @param icon 图标
 */
function layerAlertAndEnd(msg, callback, successCallback, endBack,icon) {
    if (having_layer_count > 0) {
        layer.close(loadingindex);
        having_layer_count = 0;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    layer.close(loadingindex);
    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        window.top.layer.alert(msg, {
            icon: isNull(icon)? 1 : icon, id: 'other1', yes: function (index, layero) {
                window.top.layer.close(index)
                if (callback && typeof (callback) == "function") {
                    callback();
                }
            }, end: function () {
                having_layer_count--;
                if (isSelfWindow) {
                    show_IWeb2018();
                }
                if (endBack && typeof (endBack) == "function") {
                    endBack();
                }
            }, success: function () {
                if (successCallback && typeof (successCallback) == "function") {
                    successCallback();
                }
            }
        });
    },400);
}

/**
 * 弹窗显示警告消息
 * @param msg 消息
 * @param callback 回调函数
 */
function layerWarning(msg, callback) {
    if (having_layer_count > 0) {
        layer.close(loadingindex);
        having_layer_count = 0;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    layer.close(loadingindex);
    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        systemMessage(-1, msg, true, function () {
            if (isSelfWindow) {
                show_IWeb2018();
            }
            if (callback && typeof (callback) == "function") {
                callback();
            }
        });
    }, 400)
}

/**
 * layer 弹窗显示成功消息
 * @param msg 消息
 * @param callback 回调事件
 */
function layerSuccess(msg, callback) {
    if (having_layer_count > 0) {
        layer.close(loadingindex);
        having_layer_count = 0;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    layer.close(loadingindex);
    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        systemMessage(0, msg, true, function () {
            having_layer_count--;
            if (isSelfWindow) {
                show_IWeb2018();
            }
            if (callback && typeof (callback) == "function") {
                callback();
            }
        }, 2500)
    }, 400);
}


/**
 * layer 弹窗显示成功消息
 * @param msg
 * @param callback 回调事件
 */
function layerConfirm(msg, callback) {
    if (having_layer_count > 0) {
        return false;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    layer.close(loadingindex);
    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        window.top.layer.confirm(msg, {
                icon: 3,
                end: function f() {
                    having_layer_count--;
                    if (isSelfWindow) {
                        show_IWeb2018();
                    }
                }
            }, function (index) {
                window.top.layer.close(index);
                if (callback && typeof (callback) == "function") {
                    callback();
                }
            }
        );
    }, 400);
}

/**
 * layer 抉择弹窗
 * @param msg
 * @param success 回调事件
 * @param callback 回调事件
 */
function openConfirm(msg, success, callback) {
    if (having_layer_count > 0) {
        return false;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    layer.close(loadingindex);
    setTimeout(function () {
        var isSelfWindow = false;
        if (getIWeb2018HideStatus()) {
            hide_IWeb2018();
            isSelfWindow = isSelfWindow || true;
        }
        window.top.layer.confirm(msg, {
                icon: 3,
                end: function f() {
                    having_layer_count--;
                    if (isSelfWindow) {
                        show_IWeb2018();
                    }
                },
                success : function f() {
                    if (success && typeof (success) == "function") {
                        success();
                    }
                },
            }, function (index) {
                window.top.layer.close(index);
                if (callback && typeof (callback) == "function") {
                    callback();
                }
            }
        );
    }, 400);
}

/**
 * 系统通知
 * @param icon 图标
 * @param content 内容
 * @param needShade 透明度
 * @param end 结束回调函数
 * @param time 消失时间
 */
function systemMessage(icon, content, needShade, end, time) {
    var showTime = 2000;
    if (!isNull(time)) {
        showTime = time;
    }
    if (needShade) {
        layer.open({
            title: false,
            content: content,
            shadeClose: false,
            closeBtn: false,
            btn: false,
            icon: icon + 1,
            shade: [0.3, '#393D49'],
            time: showTime,
            end: function () {
                if (end && typeof (end) == "function") {
                    end();
                }
            },
        });
    } else {
        layer.msg(content, {
            icon: icon + 1,
            time: showTime,
            end: function () {
                if (end && typeof (end) == "function") {
                    end();
                }
            },
        });
    }
}

/**
 * 获取IE版本号
 *
 * @returns {*}
 */
function getIEVersion() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if (isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if (fIEVersion === 7) {
            return 7;
        } else if (fIEVersion === 8) {
            return 8;
        } else if (fIEVersion === 9) {
            return 9;
        } else if (fIEVersion === 10) {
            return 10;
        } else {
            return 6;//IE版本<=7
        }
    } else if (isEdge) {
        return 'edge';//edge
    } else if (isIE11) {
        return 11; //IE11
    } else {
        return -1;//不是ie浏览器
    }
}

/**
 * 隐藏IWeb2018
 */
function hide_IWeb2018() {
    try {
        var iWebPDF2018 = window.iWebPDF2018;
        // 如果直接通过组件获取插件信息获取不到，则通过外层iframe的id获取（注意id必须为showPdfIframe，iframe的id）
        if (isNull(iWebPDF2018)) {
            iWebPDF2018 = document.getElementById("showPdfIframe").contentWindow.iWebPDF2018;
        }
        var addins = iWebPDF2018.iWebPDFFun;
        if (getIEVersion() >= 9) {
            addins.hide = false;
        } else {
            iWebPDF2018.HidePlugin(false);
        }
    } catch (e) {
        console.log("隐藏IWeb2018 error!")
    }
}

/**
 * 显示IWeb2018
 */
function show_IWeb2018() {
    try {
        var iWebPDF2018 = window.iWebPDF2018;
        // 如果直接通过组件获取插件信息获取不到，则通过外层iframe的id获取（注意id必须为showPdfIframe，iframe的id）
        if (isNull(iWebPDF2018)) {
            iWebPDF2018 = document.getElementById("showPdfIframe").contentWindow.iWebPDF2018;
        }
        var addins = iWebPDF2018.iWebPDFFun;

        if (getIEVersion() >= 9) {
            addins.hide = true;
        } else {
            iWebPDF2018.HidePlugin(true);
        }
    } catch (e) {
        console.log("显示IWeb2018 error!");
    }
}

/**
 * 获取iweb2018的隐藏状态
 * @returns {boolean|*} true:未隐藏  false: 隐藏或插件初始化失败
 */
function getIWeb2018HideStatus() {
    try {
        var iWebPDF2018 = window.iWebPDF2018;
        // 如果直接通过组件获取插件信息获取不到，则通过外层iframe的id获取（注意id必须为showPdfView，iframe的id）
        if (isNull(iWebPDF2018)) {
            var iframe = document.getElementById("showPdfView");
            if (!isNull(iframe)){
                iWebPDF2018 = iframe.contentWindow.iWebPDF2018;
            }
        }

        /**
         * 如果直接获取插件iframe获取不到 再穿入一层进行获取
         */
        if (isNull(iWebPDF2018)){
            var iframe = document.getElementsByTagName("iframe")[0].document.getElementById("showPdfView");
            if (!isNull(iframe)){
                iWebPDF2018 = iframe.contentWindow.iWebPDF2018;
            }
        }

        if (isNull(iWebPDF2018)) {
            return false;
        }
        var addins = iWebPDF2018.iWebPDFFun;

        if (getIEVersion() >= 9) {
            return addins.hide;
        } else {
            return addins.Hide;
        }
    } catch (e) {
        console.log("显示IWeb2018 error!");
        return false;
    }

}

/**
 * 两位数数字转换为汉字
 * 提供中文数字
 * @param number
 * @returns {string}
 */
var cnum = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'];

function numberToChinese(number) {
    if (isNull(number)) {
        return "";
    }
    var chinese = "";
    number = number + ""; // 数字转为字符串
    for (var i = 0; i < number.length; i++) {
        chinese += cnum[parseInt(number.charAt(i))];
    }
    if (chinese.length == 2) {
        // 两位数的时候
        // 如果个位数是0的时候，令改成十
        if (chinese.charAt(1) == cnum[0]) {
            chinese = chinese.charAt(0) + cnum[10];
            // 如果是一十改成十
            if (chinese == cnum[1] + cnum[10]) {
                chinese = cnum[10]
            }
        } else if (chinese.charAt(0) == cnum[1]) {
            // 如果十位数是一的话改成十
            chinese = cnum[10] + chinese.charAt(1);
        }
    }
    return chinese;
}

function realTimeMessage(url, bidderId, message) {
    $.ajax({
        type: "post",
        url: url,
        data: {
            bidderId: bidderId
        },
        success:function(data){
            if(isNull(data)){
                return;
            }
            if (message && typeof (message) == "function") {
                message(data);
            }
        }
    });
}


function deleteMessage(url, bidderId) {
    $.ajax({
        type: "post",
        url: url,
        data: {
            bidderId: bidderId
        },
        success:function(data){

        }
    });
}

/***
 * 查看消息盒子图片
 * 图片太大时，根据电脑屏幕等比例缩放
 */

function viewPicture(src) {
    if (isNull(src)) {
        parent.layer.msg("没有发现图片！", {icon: 5});
        return;
    }
    var img = new Image();
    img.onload = function () {//避免图片还未加载完成无法获取到图片的大小。
        //避免图片太大，导致弹出展示超出了网页显示访问，所以图片大于浏览器时下窗口可视区域时，进行等比例缩小。
        var max_height = $(window).height() - 100;
        var max_width = $(window).width();

        //rate1，rate2，rate3 三个比例中取最小的。
        var rate1 = max_height / img.height;
        var rate2 = max_width / img.width;
        var rate3 = 1;
        var rate = Math.min(rate1, rate2, rate3);
        //等比例缩放
        var imgHeight = img.height * rate; //获取图片高度
        var imgWidth = img.width * rate; //获取图片宽度

        var imgHtml = "<img src='" + src + "' width='" + imgWidth + "px' height='" + imgHeight + "px'/>";
        //弹出层
        parent.layer.open({
            type: 1,
            title: false,//不显示标题
            closeBtn: 0,
            area: ['auto', 'auto'],
            skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content: imgHtml
        });
    }
    img.src = src;
}

/**
 * 复选框全选
 *
 * @param ckBoxElement
 * @param ckBoxName
 * @return
 */
function checkAll(ckBoxElement, ckBoxName) {
    var checkStatus = $(ckBoxElement).is(':checked');
    if (checkStatus) {
        $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").prop('checked', "checked");
    } else {
        $(":checkbox[name='" + ckBoxName + "'][disabled!='disabled']").removeAttr('checked', false);
    }
    $(ckBoxElement).attr("value", "-1");
}

/**
 * 检查复选框是否至少有一个被选中
 *
 * @param ckBoxName
 * @return
 */
function checkCkBoxStatus(ckBoxName) {
    var ckFlag = false;
    var ckBoxs = $(":checkbox[name='" + ckBoxName + "']");
    if (ckBoxs != null) {
        for (var i = 0; i < ckBoxs.length; i++) {
            if ($(ckBoxs[i]).prop('checked')) {
                ckFlag = true;
                break;
            }
        }
    }
    return ckFlag;
}

/**
 * layui 动态表格合并相同的row名称
 * @return
 * @author lesgod
 * @date 2020-7-6 10:42
 */
var layuiRowspan = function (fieldNameTmp, index, flag) {
    var fieldName = [];
    if (typeof fieldNameTmp == "string") {
        fieldName.push(fieldNameTmp);
    } else {
        fieldName = fieldName.concat(fieldNameTmp);
    }
    for (var i = 0; i < fieldName.length; i++) {
        execRowspan(fieldName[i], index, flag);
    }
};

var execRowspan = function (fieldName, index, flag) {
    // 1为不冻结的情况，左侧列为冻结的情况
    var fixedNode = index == "1" ? $(".layui-table-body")[index - 1] : (index == "3" ? $(".layui-table-fixed-r") : $(".layui-table-fixed-l"));
    // 左侧导航栏不冻结的情况
    var child = $(fixedNode).find("td");
    var childFilterArr = [];
    // 获取data-field属性为fieldName的td
    for (var i = 0; i < child.length; i++) {
        if (child[i].getAttribute("data-field") == fieldName) {
            childFilterArr.push(child[i]);
        }
    }
    // 获取td的个数和种类
    var childFilterTextObj = {};
    for (var i = 0; i < childFilterArr.length; i++) {
        var childText = flag ? childFilterArr[i].innerHTML : childFilterArr[i].textContent;
        if (childFilterTextObj[childText] == undefined) {
            childFilterTextObj[childText] = 1;
        } else {
            var num = childFilterTextObj[childText];
            childFilterTextObj[childText] = num * 1 + 1;
        }
    }
    var canRowspan = true;
    var maxNum;//以前列单元格为基础获取的最大合并数
    var finalNextIndex;//获取其下第一个不合并单元格的index
    var finalNextKey;//获取其下第一个不合并单元格的值
    for (var i = 0; i < childFilterArr.length; i++) {
        (maxNum > 9000 || !maxNum) && (maxNum = $(childFilterArr[i]).prev().attr("rowspan") && fieldName != "8" ? $(childFilterArr[i]).prev().attr("rowspan") : 9999);
        var key = flag ? childFilterArr[i].innerHTML : childFilterArr[i].textContent;//获取下一个单元格的值
        var nextIndex = i + 1;
        var tdNum = childFilterTextObj[key];
        var curNum = maxNum < tdNum ? maxNum : tdNum;
        if (canRowspan) {
            for (var j = 1; j <= curNum && (i + j < childFilterArr.length);) {//循环获取最终合并数及finalNext的index和key
                finalNextKey = flag ? childFilterArr[i + j].innerHTML : childFilterArr[i + j].textContent;
                finalNextIndex = i + j;
                if ((key != finalNextKey && curNum > 1) || maxNum == j) {
                    canRowspan = true;
                    curNum = j;
                    break;
                }
                j++;
                if ((i + j) == childFilterArr.length) {
                    finalNextKey = undefined;
                    finalNextIndex = i + j;
                    break;
                }
            }
            childFilterArr[i].setAttribute("rowspan", curNum);
            if ($(childFilterArr[i]).find("div.rowspan").length > 0) {//设置td内的div.rowspan高度适应合并后的高度
                $(childFilterArr[i]).find("div.rowspan").parent("div.layui-table-cell").addClass("rowspanParent");
                $(childFilterArr[i]).find("div.layui-table-cell")[0].style.height = curNum * 38 - 10 + "px";
            }
            canRowspan = false;
        } else {
            childFilterArr[i].style.display = "none";
        }
        if (--childFilterTextObj[key] == 0 | --maxNum == 0 | --curNum == 0 | (finalNextKey != undefined && nextIndex == finalNextIndex)) {//||(finalNextKey!=undefined&&key!=finalNextKey)
            canRowspan = true;
        }
    }
};

/**
 * 评标页面单独loading层
 */
function layerLoadingForExpert(msg, icon, time, shade, success, end){
    var showtime;
    if (isNull(time)) {
        showtime = 30 * 1000
    } else {
        showtime = time * 1000;
    }
    if (isNaN(showtime)) {
        showtime = 30 * 1000;
    }

    setTimeout(function () {
        single_double_iframe_hide_pdf();
        if (msg) {
            loadingindex = window.top.layer.msg(msg, {
                id: 'systemMessage',
                icon: isNull(icon) ? 16 : icon,
                shade: isNull(shade) ? [0.3, '#393D49'] : shade,
                time: showtime,
                success: function (layero) {
                    if (success && typeof (success) == "function") {
                        success(layero);
                    }
                }, end: function (layero) {
                    single_double_iframe_show_pdf();
                    if (end && typeof (end) == "function") {
                        end(layero);
                    }
                }
            });
        } else {
            loadingindex = window.top.layer.msg("加载中，请稍等...", {
                id: 'systemMessage',
                icon: isNull(icon) ? 16 : icon,
                shade: isNull(shade) ? [0.3, '#393D49'] : shade,
                time: showtime,
                success: function (layero) {
                    if (success && typeof (success) == "function") {
                        success(layero);
                    }
                }, end: function (layero) {
                    single_double_iframe_show_pdf();
                    if (end && typeof (end) == "function") {
                        end(layero);
                    }
                }
            });
        }
    }, 400);
}

/**
 * 消息弹出窗提示 包含end方式
 * @param msg 消息
 * @param callback 回调函数
 * @param endBack
 * @param successCallback 成功回调执行函数
 * @param icon 图标
 */
function layerAlertAndEndForExpert(msg, callback, successCallback, endBack,icon) {
    hide_IWeb2018();
    if (having_layer_count > 0) {
        layer.close(loadingindex);
        having_layer_count = 0;
    }
    having_layer_count++;
    // 关闭其他显示，防止影响layer的插件
    loadComplete();
    setTimeout(function () {
        single_double_iframe_hide_pdf();
        loadingindex = window.top.layer.alert(msg, {
            icon: isNull(icon)? 1 : icon, id: 'other1', yes: function (index, layero) {
                window.top.layer.close(index)
                if (callback && typeof (callback) == "function") {
                    callback();
                }
            }, end: function () {
                having_layer_count--;
                single_double_iframe_show_pdf();
                if (endBack && typeof (endBack) == "function") {
                    endBack();
                }
            }, success: function () {
                if (successCallback && typeof (successCallback) == "function") {
                    successCallback();
                }
            }
        });
    },400);
}



/**
 * 单、双层iframe隐藏pdf插件
 */
function single_double_iframe_hide_pdf() {
    try {
        var $firstIframe = document.getElementsByTagName("iframe")[0];
        if (!isNull($firstIframe) && $firstIframe.id != 'showPdfView') {
            $firstIframe.contentWindow.hide_IWeb2018();
        } else {
            hide_IWeb2018();
        }
    }catch (e) {
        console.log("隐藏IWeb2018 error!");
    }
}

/**
 * 单、双层iframe显示pdf插件
 */
function single_double_iframe_show_pdf() {
    try {
        var $firstIframe = document.getElementsByTagName("iframe")[0];
        if (!isNull($firstIframe) && $firstIframe.id != 'showPdfView') {
            $firstIframe.contentWindow.show_IWeb2018();
        } else {
            show_IWeb2018();
        }
    }catch (e) {
        console.log("显示IWeb2018 error!");
    }
}
