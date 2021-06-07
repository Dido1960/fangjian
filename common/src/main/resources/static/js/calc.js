/*******此JS用于 JS精确计算 防止精度丢失************/

/*******author Kevin 2020-09-24****************/

/**
 * 加法运算
 *
 * @param a a
 * @param b b
 * @private
 */
function _add(a, b) {
    var c, d, e;
    try {
        c = a.toString().split(".")[1].length;
    } catch (ex) {
        c = 0;
    }

    try {
        d = b.toString().split(".")[1].length;
    } catch (ex) {
        d = 0;
    }

    return e = Math.pow(10, Math.max(c, d)), (_mul(a, e) + _mul(b, e)) / e;
}

/**
 * 减法运算
 *
 * @param a 被减数
 * @param b 减数
 * @private
 */
function _sub(a, b) {
    var c, d, e;
    try {
        c = a.toString().split(".")[1].length;
    } catch (ex) {
        c = 0;
    }

    try {
        d = b.toString().split(".")[1].length;
    } catch (ex) {
        d = 0;
    }

    return e = Math.pow(10, Math.max(c, d)), (_mul(a, e) - _mul(b, e)) / e;
}

/**
 * 乘法运算
 *
 * @param a a
 * @param b b
 * @private
 */
function _mul(a, b) {
    var c = 0, d = a.toString(), e = b.toString();

    try {
        c += d.split(".")[1].length;
        c += e.split(".")[1].length;
    } catch (ex) {

    }

    return Number(d.replace(".", "")) * Number(e.replace(".", "")) / Math.pow(10, c);
}

/**
 * 除法运算
 *
 * @param a 被除数
 * @param b 除数
 * @private
 */
function _div(a, b) {
    var c = 0,
        d = 0,
        e = 0,
        f = 0;

    try {
        e = a.toString().split(".")[1].length;
        f = b.toString().split(".")[1].length;
    } catch (ex) {

    }

    return c = Number(a.toString().replace(".", "")), d = Number(b.toString().replace(".", "")), _mul(c / d, Math.pow(10, f - e));
}

/**
 * 载入保留小数点位数的方法 toFixed
 */
Number.prototype.toFixed = function (n) {
    if (n > 20 || n < 0) {
        throw new RangeError("toFixed method digits argument must be between 0 and 20");
    }

    var number = this;
    if (isNaN(number) || number >= Math.pow(10, 21)) {
        return number.toString();
    }

    if (typeof (n) == 'undefined' || n === 0) {
        return (Math.round(number)).toString();
    }

    var result = number.toString();
    var arr = result.split(".");

    if (arr.length < 2) {
        result += ".";
        for (var i = 0; i < n; i++) {
            result += "0";
        }
        return result;
    }

    var integer = arr[0];
    var decimal = arr[1];
    if (decimal.length === n) {
        return result;
    }

    if (decimal.length < 0) {
        for (var k = 0; k < n - decimal.length; k++) {
            result += "0";
        }
        return result;
    }

    result = integer + "." + decimal.substr(0, n);
    var last = decimal.substr(n, 1);

    if (parseInt(last, 10) >= 5) {
        var x = Math.pow(10, n);
        result = (Math.round(parseFloat(result) * x) + 1) / x;
        result = result.toFixed(n);
    }

    return result;
}
