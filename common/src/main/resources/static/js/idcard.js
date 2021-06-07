/*根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
    地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
    出生日期码表示编码对象出生的年、月、日，其中年份用四位数字表示，年、月、日之间不用分隔符。
    顺序码表示同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性，偶数分给女性。
    校验码是根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。

出生日期计算方法。
    15位的身份证编码首先把出生年扩展为4位，简单的就是增加一个19或18,这样就包含了所有1800-1999年出生的人;
2000年后出生的肯定都是18位的了没有这个烦恼，至于1800年前出生的,那啥那时应该还没身份证号这个东东，⊙﹏⊙b汗...
下面是正则表达式:
    出生日期1800-2099  (18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])
身份证正则表达式 /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i
15位校验规则 6位地址编码+6位出生日期+3位顺序号
18位校验规则 6位地址编码+8位出生日期+3位顺序号+1位校验位

校验位规则     公式:∑(ai×Wi)(mod 11)……………………………………(1)
公式(1)中：
                i----表示号码字符从由至左包括校验码在内的位置序号；
                ai----表示第i位置上的号码字符值；
                Wi----示第i位置上的加权因子，其数值依据公式Wi=2^(n-1）(mod 11)计算得出。
                i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1
Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 1*/
function isValidCard(id_card) {
    const city = {
        11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古",
        21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏",
        33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南",
        42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆",
        51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃",
        63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外"
    };
    // 是否为空
    if (id_card === '') {
        return false;
    }
    // 校验长度，类型
    if (isCardNo(id_card) === false) {
        return false;
    }
    // 检查省份
    if (checkProvince(id_card, city) === false) {
        return false;
    }
    // 校验生日
    if (checkBirthday(id_card) === false) {
        return false;
    }

    // 检验位的检测
    return checkParity(id_card) !== false;
}

function isCardNo(id_card) {
    // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
    const reg = /(^\d{15}$)|(^\d{17}(\d|X|x)$)/;
    return reg.test(id_card) !== false;
}

function checkProvince(id_card, city) {
    const province = id_card.substr(0, 2);
    return city[province] !== undefined;
}

function checkBirthday(id_card) {
    const len = id_card.length;
    // 身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
    if (len === 15) {
        const re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
        const arr_data = id_card.match(re_fifteen);
        const year = arr_data[2];
        const month = arr_data[3];
        const day = arr_data[4];
        const birthday = new Date('19' + year + '/' + month + '/' + day);
        return verifyBirthday('19' + year, month, day, birthday);
    }
    // 身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
    if (len === 18) {
        const re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X|x)$/;
        const arr_data = id_card.match(re_eighteen);
        const year = arr_data[2];
        const month = arr_data[3];
        const day = arr_data[4];
        const birthday = new Date(year + '/' + month + '/' + day);
        return verifyBirthday(year, month, day, birthday);
    }
    return false;
}

function verifyBirthday(year, month, day, birthday) {
    const now = new Date();
    const now_year = now.getFullYear();
    // 年月日是否合理
    if (birthday.getFullYear() === year && (birthday.getMonth() + 1) === month && birthday.getDate() === day) {
        // 判断年份的范围（0岁到100岁之间)
        const time = now_year - year;
        return time >= 0 && time <= 100;
    }
    return false;
}

function checkParity(id_card) {
    // 15位转18位
    id_card = convert15To18(id_card);
    const len = id_card.length;
    if (len === 18) {
        const arr_int = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
        const arr_char = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
        var card_temp = 0, i, val_num;
        for (i = 0; i < 17; i++) {
            card_temp += id_card.substr(i, 1) * arr_int[i];
        }
        val_num = arr_char[card_temp % 11];
        return val_num === id_card.substr(17, 1).toLocaleUpperCase();
    }
    return false;
}

function convert15To18(id_card) {
    if (id_card.length === 15) {
        const arr_int = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
        const arr_char = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
        var card_temp = 0, i;
        id_card = id_card.substr(0, 6) + '19' + id_card.substr(6, id_card.length - 6);
        for (i = 0; i < 17; i++) {
            card_temp += id_card.substr(i, 1) * arr_int[i];
        }
        id_card += arr_char[card_temp % 11];
        return id_card;
    }
    return id_card;
}