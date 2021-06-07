/**
 * 转换金钱大写
 * @param number 金额
 * @param currency 单位 元 或者万元
 * @returns {string}
 */
var chineseUnit = "元=拾=佰=仟=万=拾=佰=仟=亿=拾=佰=仟=万";
var chineseValue = "零壹贰叁肆伍陆柒捌玖";
function convertMoney(number, currency) {
    if (!number) {
        return "";
    }

    var money = number.replace(/ /g, "");

    if (currency && currency.indexOf("万元") !== -1) {
        if (money.indexOf(".") !== -1) {
            var temp = money.split(".");

            if (temp[1].length <= 4) {
                money = temp[0] + temp[1];
                for (var i = 0; i < 4 - temp[1].length; i++) {
                    money = money + "0";
                }
            } else {
                money = temp[0] + temp[1].substring(0, 4) + "." + temp[1].substring(4);
            }
        } else {
            money = money + "0000";
        }
    }

    number = money.trim();

    if (number.indexOf(".") === 0) {
        return "";
    }

    var chinese = "";
    var decimalsChinese = "";
    var intNumber = "";
    var decimals = "";

    // 判断所传过来的值，是整数还是小数
    var srcNumber = number;

    if (number.indexOf(".") === -1) {
        srcNumber = srcNumber + ".00";
        intNumber = srcNumber.substring(0, srcNumber.indexOf("."));
        decimals = "0";
    } else {
        intNumber = srcNumber.substring(0, srcNumber.indexOf("."));
        decimals = srcNumber.substring(srcNumber.indexOf(".") + 1, srcNumber.length);
        if (decimals.length < 2) {
            decimals = decimals + "0";
        }
    }

    var chineseUnit1 = chineseUnit.split("=");
    // 转换整数部分
    for (var j = 0; j < intNumber.length; j++) {
        chinese += chineseValue.charAt(srcNumber.charAt(j)) + chineseUnit1[intNumber.length - 1 - j];
    }

    var isZero = decimals.substring(0, 1);
    // 这个地方的判断主要是因为，零钱有小数部分，小数部分的默认值是0.0,默认的有小数部分。所以需要判断
    // 小数部分那个小数是否为0，如果为0，就需要转换小数部分了。
    // 计算小数部分
    if ((parseFloat(decimals) !== 0)) {
        decimalsChinese += chineseValue.charAt(decimals.substring(0, 1)) + "角" + chineseValue.charAt(decimals.substring(1, 2)) + "分";
    } else {
        decimalsChinese = "零元整";
    }
    chinese += decimalsChinese;
    while (chinese.indexOf("零零") !== -1 || chinese.indexOf("零万") !== -1
    || chinese.indexOf("零亿") !== -1 || chinese.indexOf("亿万") !== -1
    || chinese.indexOf("零佰") !== -1 || chinese.indexOf("零元") !== -1
    || chinese.indexOf("零拾") !== -1 || chinese.indexOf("零仟") !== -1
    || chinese.indexOf("零角") !== -1 || chinese.indexOf("零分") !== -1) {
        chinese = chinese.replace(/零零/g, "零");
        chinese = chinese.replace(/零拾/g, "零");
        chinese = chinese.replace(/零万/g, "万");
        chinese = chinese.replace(/零亿/g, "亿");
        chinese = chinese.replace(/零元/g, "元");
        chinese = chinese.replace(/亿万/g, "亿零");
        chinese = chinese.replace(/零佰/g, "零");
        chinese = chinese.replace(/零仟/g, "零");
        chinese = chinese.replace(/零角/g, "零");
        chinese = chinese.replace(/零[十佰仟]/g, "零");
        chinese = chinese.replace(/零分/g, "");
    }
    chinese = chinese.replace(/元元/g, "元");
    while (chinese.
    indexOf("零") === 0 || chinese.indexOf("万") === 0 || chinese.indexOf("元") === 0 || chinese.indexOf("亿") === 0) {
        chinese = chinese.substring(1);

    }
    if (chinese.indexOf("undefine") !== -1) {
        chinese = "零元" + currency;
    }
    return chinese;

}