/************此数字证书相关信息操作基础 类**/

//BJCA驱动加载
window.onload = function () {
    init(function () {
        console.log("BJCA success");
    }, function () {
        console.log("BJCA fail");
    });
}

//获取锁的列表
function listUkeys(callback) {
    var res = SOF_GetUserList(function (data) {
        var keyList = data.retVal;
        var keyInfos = keyList.indexOf("&&&");

        if (keyInfos <= 0) {
            callback(ukeys);
            return;
        }
        keyInfos = keyList.split("&&&");
        var ukeys = [];
        for (var i = 0; i < keyInfos.length; i++) {
            var keyInfo = keyInfos[i].split("||");
            if (!isNull(keyInfo[0]) && "移动证书" != keyInfo[0]&&keyInfo[1].indexOf("mssp")==-1)  {
                var temp = {};
                temp.cert_no = keyInfo[1];
                temp.cert_name = keyInfo[0];
                ukeys.push(temp);
            }

        }
        callback(ukeys);
    });

}


//获取对应的certInfo
function getCertInfo(CERT_NO_INDEX, callback) {

    if (!CERT_NO_INDEX || CERT_NO_INDEX === "") {
        return;
    }
    var index_getCertInfo_loading = window.top.layer.msg('证书信息获取中...', {
        icon: 16,
        shade: [0.3, '#393D49'],
        time: 0
    });
    clearInterval(loading_caInfo_Interval);

    var data = {

// 证书所有者 Eg:兰州市市政房建测试有限公司
        CERT_OWNER: "",
// 证书有效期结束时间
        CERT_END_TIME: "",
// 密码卡序列号
        PWD_SERIAL_NUMBER: "",
// 唯一实体标识
        ONLY_ENTITY_IDENTIFIER: "",
//组织机构代码
        ORGANIZATION_CODE: "",
// UKEY序列号
        UKEY_SERIAL_NUMBER: "",
// 证书序列号
        CERT_SERIAL_NUMBER: "",
// 证书名称
        CERT_NAME: "",
// 证书颁发者
        CERT_ISSUER: "",
// 证书类型 (1: 企业 2:个人)
        CERT_TYPE: "",
// 证书颁发时间
        ISSUE_TIME: "",
// 证书有效期开始时间
        CERT_START_TIME: "",
// 证书签章图片base64
        SIGNAR_PIC_BASE: "",
        //标题
        TITLE: "",
        //
    };


    data.CERT_NO_INDEX = CERT_NO_INDEX;
    data.PWD_SERIAL_NUMBER = CERT_NO_INDEX.substring(0, CERT_NO_INDEX.indexOf("/"));
    data.UKEY_SERIAL_NUMBER = CERT_NO_INDEX.substring(CERT_NO_INDEX.indexOf("/") + 1);
    //-----------标题-------
    var title_index = 0;
    var title_c;
    var title_o;
    var title_ou;
    var title_st;
    var title_cn;
    var title_l;
    var title_e;
    //-------发证机构-------------
    var issuer = '';
    var issuer_index = 0;

    var issuer_c;
    var issuer_o;
    var issuer_ou;
    var issuer_st;
    var issuer_cn;
    var issuer_l;
    var issuer_e;

    var readCount = 0;
    SOF_ExportUserCert(CERT_NO_INDEX, function (base_cert) {

        //1
        readCount++;
        base_cert = base_cert.retVal;
        SOF_GetCertInfo(base_cert, 2, function (CERT_SERIAL_NUMBER) {
            //2
            readCount++;
            data.CERT_SERIAL_NUMBER = CERT_SERIAL_NUMBER.retVal;
        });
        SOF_GetCertEntity(base_cert, function (ONLY_ENTITY_IDENTIFIER) {
            //3
            readCount++;
            data.ONLY_ENTITY_IDENTIFIER = ONLY_ENTITY_IDENTIFIER.retVal;
            //该锁类型获取未必是正确的，todo 12 为企业 17为个人
            data.CERT_TYPE = (data.ONLY_ENTITY_IDENTIFIER.indexOf("JJ") != -1 ? "1" : "2");
            data.ORGANIZATION_CODE = "";
            if (data.ONLY_ENTITY_IDENTIFIER.indexOf("JJ") != -1) {
                data.ORGANIZATION_CODE = data.ONLY_ENTITY_IDENTIFIER.split("JJ")[1];
            } else if (data.ONLY_ENTITY_IDENTIFIER.indexOf("SF") != -1) {
                data.ORGANIZATION_CODE = data.ONLY_ENTITY_IDENTIFIER.split("SF")[1];
            }
        });

        //开始时间
        SOF_GetCertInfo(base_cert, 11, function (returnInfo) {
            //4
            readCount++;
            data.ISSUE_TIME = data.CERT_START_TIME = parse_time(returnInfo.retVal)
        });
        //结束时间
        SOF_GetCertInfo(base_cert, 12, function (returnInfo) {
            //5
            readCount++;
            data.CERT_END_TIME = parse_time(returnInfo.retVal);
        });


        SOF_GetCertInfo(base_cert, 4, function (returnInfo) {
            //6
            readCount++;
            issuer_c = returnInfo.retVal;
        });

        SOF_GetCertInfo(base_cert, 5, function (returnInfo) {
            //7
            readCount++;
            issuer_o = returnInfo.retVal;
        });
        SOF_GetCertInfo(base_cert, 6, function (returnInfo) {
            //8
            readCount++;
            issuer_ou = returnInfo.retVal;
        });
        SOF_GetCertInfo(base_cert, 7, function (returnInfo) {
            //9
            readCount++;
            issuer_cn = returnInfo.retVal;
        });
        SOF_GetCertInfo(base_cert, 8, function (returnInfo) {
            //10
            readCount++;
            issuer_st = returnInfo.retVal;
        });
        SOF_GetCertInfo(base_cert, 9, function (returnInfo) {
            //11
            readCount++;
            issuer_l = returnInfo.retVal;
        });
        SOF_GetCertInfo(base_cert, 10, function (returnInfo) {
            //12
            readCount++;
            issuer_e = returnInfo.retVal;
        });

        //end

        SOF_GetCertInfo(base_cert, 13, function (returnInfo) {
            //13
            readCount++;
            title_c = returnInfo.retVal;
        });
        //var title_o;
        SOF_GetCertInfo(base_cert, 14, function (returnInfo) {
            //14
            readCount++;
            title_o = data.CERT_OWNER = returnInfo.retVal;
        });

        SOF_GetCertInfo(base_cert, 15, function (returnInfo) {
            //15
            readCount++;
            title_ou = returnInfo.retVal;

        });

        SOF_GetCertInfo(base_cert, 16, function (returnInfo) {
            //16
            readCount++;
            title_st = returnInfo.retVal;
        });

        SOF_GetCertInfo(base_cert, 17, function (returnInfo) {
            //17
            readCount++;
            title_cn = data.CERT_NAME = returnInfo.retVal;
        });
        //

        SOF_GetCertInfo(base_cert, 18, function (returnInfo) {
            //18
            readCount++;
            title_l = returnInfo.retVal;
        });
        //
        SOF_GetCertInfo(base_cert, 19, function (returnInfo) {
            //19
            readCount++;
            title_e = returnInfo.returnVal;
        });


    });

    data.SIGNAR_PIC_BASE = "-";
    data.CERT_ISSUER = "BJCA";


    loading_caInfo_Interval = setInterval(function () {
        if (readCount != 19) {
            return;
        }
        readCount++;
        clearInterval(loading_caInfo_Interval);

        //标题获取了以后
        var cert_title = "";
        if (title_c) {
            cert_title += "C=" + title_c + ", ";
        }

        if (title_o) {
            cert_title += "O=" + title_o + ", ";
        }

        if (title_ou) {
            cert_title += "OU=" + title_ou + ", ";
        }

        if (title_st) {
            cert_title += "ST=" + title_st + ", ";
        }

        if (title_cn) {
            cert_title += "CN=" + title_cn + ", ";
        }

        if (title_l) {
            cert_title += "L=" + title_l + ", ";
        }

        if (title_e) {
            cert_title += "E=" + title_e + ", ";
        }
        data.TITLE = cert_title;
        //发证机构
        var issuer = "";
        if (issuer_c) {
            issuer += "C=" + issuer_c + ", ";
        }

        if (issuer_o) {
            issuer += "O=" + issuer_o + ", ";
        }

        if (issuer_ou) {
            issuer += "OU=" + issuer_ou + ", ";
        }

        if (issuer_st) {
            issuer += "ST=" + issuer_st + ", ";
        }

        if (issuer_cn) {
            issuer += "CN=" + issuer_cn + ", ";
        }

        if (issuer_l) {
            issuer += "L=" + issuer_l + ", ";
        }

        if (issuer_e) {
            issuer += "E=" + issuer_e + ", ";
        }
        data.CERT_ISSUER = issuer;


        window.top.layer.close(index_getCertInfo_loading);
        getQSKJCertInfo(CERT_NO_INDEX, function (QSKJ_INFO) {
            console.log("cert_read_callBack:"+new Date().getMilliseconds());
            callback(data, QSKJ_INFO);
        })
    }, 500)
    //return data;
}

var loading_caInfo_Interval;

function parse_time(time) {
    var year = time.substring(0, 4);
    var month = time.substring(4, 6);
    var day = time.substring(6, 8);
    var hour = time.substring(8, 10);
    var minutes = time.substring(10, 12);
    var seconds = time.substring(12);
    return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
}


//QSKJ Info
function getQSKJCertInfo(CERT_NO_INDEX, callback) {

    var QSKJ_INFO = {
        QSKJ_COMPANY_KEY: "",
        QSKJ_CERT_REMAINDER: "",
        QSKJ_CERT_ISSUED: "",
        QSKJ_FREE_KEY:""
    };
    var index_getQSKJCertInfo_loading = window.top.layer.msg('权限信息获取中...', {
        icon: 16,
        shade: [0.3, '#393D49'],
        time: 0
    });
    var index_load_key = 0;

    ReadFileEx(CERT_NO_INDEX, "bc520003b6e0c98511c805c4783fe5f7", function (returnInfo) {
        QSKJ_INFO.QSKJ_COMPANY_KEY = _Base64decode(returnInfo.retVal);
        index_load_key++;
    });
    ReadFileEx(CERT_NO_INDEX, "174b111fa0132969f10cbfc1d0985af4", function (returnInfo) {
        QSKJ_INFO.QSKJ_CERT_REMAINDER = _Base64decode(returnInfo.retVal);
        index_load_key++;
    });
    ReadFileEx(CERT_NO_INDEX, "1662fd9c1e8e81b26793313bf4df8236", function (returnInfo) {
        QSKJ_INFO.QSKJ_CERT_ISSUED = _Base64decode(returnInfo.retVal);
        index_load_key++;
    });
    ReadFileEx(CERT_NO_INDEX, "1d9137661ddcb9c20fe3fd3d806ebe94", function (returnInfo) {
        QSKJ_INFO.QSKJ_FREE_KEY = _Base64decode(returnInfo.retVal);
        index_load_key++;
    });

    if (loading_QSKJ_Info_Interval != null) {
        clearInterval(loading_QSKJ_Info_Interval);
    }

    loading_QSKJ_Info_Interval = setInterval(function () {
        if (index_load_key != 4) {
            return;
        }
        index_load_key++;
        clearInterval(loading_QSKJ_Info_Interval);
        QSKJ_INFO = JSON.stringify(QSKJ_INFO);
        if (!isNull(QSKJ_INFO)) {
            $.ajax({
                url: '/common/data/decrypt',
                type: 'post',
                cache: false,
                async: false,
                data: {
                    content: QSKJ_INFO,
                    keyNum: CERT_NO_INDEX,
                },
                success: function (data) {
                    QSKJ_INFO = {
                        QSKJ_COMPANY_KEY: "",
                        QSKJ_CERT_REMAINDER: "",
                        QSKJ_CERT_ISSUED: "",
                        QSKJ_FREE_KEY:""
                    };

                    if (data) {
                        QSKJ_INFO.QSKJ_COMPANY_KEY = data.QSKJ_COMPANY_KEY;
                        QSKJ_INFO.QSKJ_CERT_REMAINDER = data.QSKJ_CERT_REMAINDER;
                        QSKJ_INFO.QSKJ_CERT_ISSUED = data.QSKJ_CERT_ISSUED;
                        QSKJ_INFO.QSKJ_FREE_KEY= data.QSKJ_FREE_KEY;
                    }
                    window.top.layer.close(index_getQSKJCertInfo_loading);
                },
                error: function (data) {
                    console.error(data);
                    window.top.layer.close(index_getQSKJCertInfo_loading);
                    layer.msg("操作失败！")
                },
            });
        }
        callback(QSKJ_INFO);


    }, 100)


}

var loading_QSKJ_Info_Interval;

var loading_QSKJ_Info_Write_Interval;

//QSKJ Info writeInfo
function writeQSKJCertInfo(CERT_NO_INDEX, params, callback) {
    var index_writeQSKJCertInfo_loading = window.top.layer.msg('信息写入,请勿拔掉KEY...', {
        icon: 16,
        shade: [0.3, '#393D49'],
        time: 0
    });
    var index_load_key = 0;
    getQSKJCertInfo(CERT_NO_INDEX, function (QSKJ_INFO) {
        if (!isNull(params.QSKJ_COMPANY_KEY)) {
            QSKJ_INFO.QSKJ_COMPANY_KEY = params.QSKJ_COMPANY_KEY;
        }
        if (!isNull(params.QSKJ_CERT_REMAINDER)) {
            QSKJ_INFO.QSKJ_CERT_REMAINDER = params.QSKJ_CERT_REMAINDER;
        }
        if (!isNull(params.QSKJ_CERT_ISSUED)) {
            QSKJ_INFO.QSKJ_CERT_ISSUED = params.QSKJ_CERT_ISSUED;
        }
        if (!isNull(params.QSKJ_FREE_KEY)) {
            QSKJ_INFO.QSKJ_FREE_KEY = params.QSKJ_FREE_KEY;
        }
        QSKJ_INFO = JSON.stringify(QSKJ_INFO);
        $.ajax({
            url: '/common/data/encrypt',
            type: 'post',
            cache: false,
            async: false,
            data: {
                content: QSKJ_INFO,
                keyNum: CERT_NO_INDEX,
            },
            success: function (data) {
                console.log("即将写入的加密数据：" + data);

                //
                if(!isNull(data.QSKJ_CERT_REMAINDER)) {
                    console.log("即将写入的加密数据：QSKJ_CERT_REMAINDER->" + _Base64encode(data.QSKJ_CERT_REMAINDER));
                    WriteFileEx(CERT_NO_INDEX, "174b111fa0132969f10cbfc1d0985af4", _Base64encode(data.QSKJ_CERT_REMAINDER), function () {
                        index_load_key++;
                    });
                }else {
                    index_load_key++;
                }
                //
                if(!isNull(data.QSKJ_COMPANY_KEY)) {
                    console.log("即将写入的加密数据：QSKJ_CERT_REMAINDER->" + _Base64encode(data.QSKJ_COMPANY_KEY));
                    WriteFileEx(CERT_NO_INDEX, "bc520003b6e0c98511c805c4783fe5f7", _Base64encode(data.QSKJ_COMPANY_KEY), function () {
                        index_load_key++;
                    });
                }else {
                    index_load_key++;
                }
                //
                if(!isNull(data.QSKJ_CERT_ISSUED)) {
                    console.log("即将写入的加密数据：QSKJ_CERT_ISSUED->" + _Base64encode(data.QSKJ_CERT_ISSUED));
                    WriteFileEx(CERT_NO_INDEX, "1662fd9c1e8e81b26793313bf4df8236", _Base64encode(data.QSKJ_CERT_ISSUED), function () {
                        index_load_key++;
                    });
                }else {
                    index_load_key++;
                }

                if(!isNull(data.QSKJ_FREE_KEY)){
                    console.log("即将写入的加密数据：QSKJ_FREE_KEY->" + _Base64encode(data.QSKJ_FREE_KEY));
                    WriteFileEx(CERT_NO_INDEX, "1d9137661ddcb9c20fe3fd3d806ebe94", _Base64encode(data.QSKJ_FREE_KEY), function () {
                        index_load_key++;
                    });
                }else {
                    index_load_key++;
                }

                if (loading_QSKJ_Info_Write_Interval != null) {
                    clearInterval(loading_QSKJ_Info_Write_Interval);
                }

                loading_QSKJ_Info_Write_Interval = setInterval(function () {
                    if (index_load_key != 4) {
                        return;
                    } if (loading_QSKJ_Info_Write_Interval != null) {
                        clearInterval(loading_QSKJ_Info_Write_Interval);
                    }

                    index_load_key++;
                    window.top.layer.close(index_writeQSKJCertInfo_loading);
                    callback();
                }, 100);


            },
            error: function (data) {
                console.error(data);
                layer.msg("操作失败！")
            },
        });
    })
}

/**
 * 登录验证
 * **/
function caLogin(CERT_NO_INDEX, pwd, callback) {
    var index_caLogin = window.top.layer.msg('账号密码验证...', {
        icon: 16,
        shade: [0.3, '#393D49'],
        time: 0
    });
    SOF_Login(CERT_NO_INDEX, pwd, function (data) {
        if (data.retVal) {
            window.top.layer.close(index_caLogin);
            callback();
        } else {
            SOF_GetPinRetryCount(CERT_NO_INDEX, function (returnInfo) {
                var errorCount = returnInfo.retVal;

                window.top.layer.close(index_caLogin);
                if (errorCount > 0) {
                    window.parent.layer.alert("校验证书密码失败！您还有" + errorCount + "次机会重试！", {icon: 2});
                } else if (errorCount === 0) {
                    window.parent.layer.alert("您的证书密码已被锁死,请联系管理员进行解锁", {icon: 2});
                } else {
                    window.parent.layer.alert("登录失败!", {icon: 2});
                }

            });
        }
    });
}

