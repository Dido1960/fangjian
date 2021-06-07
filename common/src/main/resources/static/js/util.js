/**
 * 下划线转换驼峰
 * @param name
 * @returns {string}
 */
function toCamelCase(name) {
    return name.replace(/_(\w)/g, function (all, letter) {
        return letter.toUpperCase();
    });
}

/**
 * 驼峰转换下划线
 * @param name
 * @returns {string}
 */
function toUnderline(name) {
    return name.replace(/([A-Z])/g, "_$1").toLowerCase();
}

/**
 * 在弹出的layer弹框中关闭窗口
 */
function closeInOpenedLayer() {
    if (parent.layer) {
        //获取窗口索引
        var index = parent.layer.getFrameIndex(window.name);
        //关闭窗口
        parent.layer.close(index);
    }
}

/**
 * 构建分页对象
 * @param data
 * @param extra
 * @returns {{size: *, current: number}}
 */
function buildPageModel(data, extra) {
    var page = {
        draw: data.draw,
        size: data.length,
        current: data.start / data.length + 1
    };
    //排序条件
    if (!_.isEmpty(data.order)) {
        _.each(data.order, function (element, index) {
            page['orders[' + index + '].asc'] = element.dir === 'asc';
            page['orders[' + index + '].column'] = toUnderline(data.columns[element.column].data);
        });
    }
    if (extra) {
        page = $.extend({}, page, extra);
    }
    return page;
}

/**
 * 转换分页对象
 * @param data
 * @param type
 * @returns {string}
 */
function convertPageModel(data, type) {
    var json = $.parseJSON(data);
    return JSON.stringify({
        draw: json.draw,
        recordsTotal: json.total,
        recordsFiltered: json.total,
        data: json.records
    });
}

/**
 * null转为空字符串
 * @param str
 * @returns {string}
 */
function null2empty(str) {
    if (str === null) {
        return "";
    }
    return str;
}

/**
 * 时间戳转换为时间
 * @param timestamp
 * @returns {string}
 */
function timestampToDateTime(timestamp) {
    var date = new Date(timestamp);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    var d = date.getDate();
    d = d < 10 ? ("0" + d) : d;
    var h = date.getHours();
    h = h < 10 ? ("0" + h) : h;
    var M = date.getMinutes();
    M = M < 10 ? ("0" + M) : M;
    var s = date.getSeconds();
    s = s < 10 ? ("0" + s) : s;
    var str = y + "-" + m + "-" + d + " " + h + ":" + M + ":" + s;
    return str;
}
