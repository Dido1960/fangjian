/**
 * 本JS特供 流程 BJCA签名版 对评标报告 进行轮流签名 和 签名复用流程
 * 不可复用于其他流程
 * @author Make
 */
// 标段主键
var bidSectionId;
// 工单号
var wono;
// 渠道号
var channel = "40931586";
// 签名位置信息JSON example { keyword: 关键字; resued: 重复签名次数; width: 签名区域宽度}
var positionJson;
// 签名位置信息对象
var position;
// 签名人信息JSON example { name: XXX; idcard: XXXX}
var signerJson;
// 当前签名下标
var index = 0;
// 当前专家下标
var expertIndex = 0;
// 签名数据JSON
var dataJson;

//下一位签字专家名字
var nextExpertName;

/**
 * 初始化签名控件
 */
function initSign() {
    // 签名方式 默认为手写签名 0
    var signType = 0;
    index = 0;
    AS_InitSign(signType, function (ret) {
        var rv = ret.retVal;
        if (0 != rv) {
            layerAlert("手写签名控件初始化失败!");
            console.log("AS_InitSign ERROR : " + GetErrorMessage(rv));
            disConnect();
        } else {
            console.log("AS_InitSign SUCCESS!");
            setWono();
        }
    })
}

function setWono() {
    AS_SetBusinessParam(1, wono, function (ret) {
        var rv = ret.retVal;
        if (0 != rv) {
            layerAlert("工单号设置失败!");
            console.log("AS_SetBusinessParam 1 ERROR : " + GetErrorMessage(rv));
            disConnect();
        } else {
            console.log("AS_SetBusinessParam 1 SUCCESS!");
            setChannel();
        }
    })
}

function setChannel() {
    AS_SetBusinessParam(2, channel, function (ret) {
        var rv = ret.retVal;
        if (0 != rv) {
            layerAlert("渠道号设置失败!");
            console.log("AS_SetBusinessParam 2 ERROR : " + GetErrorMessage(rv));
            disConnect();
        } else {
            console.log("AS_SetBusinessParam 2 SUCCESS!");
            setSigner();
        }
    })
}

function setSigner() {
    var signer = JSON.parse(signerJson);

    var name = signer.name;
    var type = "1";
    var idcard = signer.idcard;


    AS_SetSignerInfo(name, type, idcard, function (ret) {
        rv = ret.retVal;
        if (0 != rv) {
            layerAlert("签名人信息设置失败!");
            console.log("set signer error : " + GetErrorMessage(rv));
            disConnect();
        } else {
            console.log("set signer success");
            setSignPosition();
        }
    })

}

function setSignPosition() {
    position = JSON.parse(positionJson);
    // position[index].x=-5000;
    // position[index].y=0;
    console.log("set position : 关键字 " + position[index].keyword + " index : " + position[index].index);
    // 关键字签名模式
    AS_SetPosKW(position[index].keyword, 1, position[index].x, position[index].y, position[index].width, position[index].height, function (ret) {
        rv = ret.retVal;
        if (0 != rv) {
            layerAlert("签名位置信息设置失败!");
            console.log("set sign position error : " + GetErrorMessage(rv));
            disConnect();
        } else {
            index++;
            console.log("set sign position success");
            connect();
        }
    })


}

function connect() {
    AS_ConnectDev(function (ret) {
        rv = ret.retVal;
        if (0 != rv) {
            layerAlert("签名设备连接失败!");
            console.log("connect error : " + GetErrorMessage(rv));
            disConnect();
        } else {
            console.log("connect success");
            addSignData();
        }
    })
}

/**
 * 新增签名数据
 */
function addSignData() {
    var signer = JSON.parse(signerJson);

    var name = signer.name;
    hide_IWeb2018();
    var layerindex = layerAlert('请' + name + "在手写板签名,并<span style='color: red'>录入指纹</span>信息!", null, function () {
        AS_AddSignEvidenceData(function (ret) {
            rv = ret.retVal;
            if (0 != rv) {
                layerAlert("签名数据获取失败!");
                console.log("add sign data error : " + GetErrorMessage(rv));
                disConnect();
            } else {
                console.log("add sign data success");
                getBusinessStr();
            }
        })
    }, 3);


}

/**
 * 签名复用
 */
function resuedSign() {
    AS_GetSignGUID(function (ret) {

        var guid = ret.retVal;
        position[index].x=9000;
        position[index].y=9000;

        console.log("set position : 关键字 " + position[index].keyword + " index : " + position[index].index);

        AS_SetPosKW(position[index].keyword, 1, position[index].x, position[index].y, position[index].width, position[index].height, function (ret) {
            rv = ret.retVal;

            if (0 != rv) {
                layerAlert("签名位置信息设置失败!");
                console.log("set sign position error : " + GetErrorMessage(rv));
                disConnect();
            } else {
                console.log("set sign position success");
                index++;
                AS_ReusedSign(guid, function (ret) {
                    rv = ret.retVal;

                    if (0 != rv) {
                        layerAlert("签名复用失败!");
                        console.log("reused sign error : " + GetErrorMessage(rv));
                        disConnect();
                    } else {
                        console.log("reused sign success");
                        getBusinessStr();
                    }
                });
            }
        })
    })
}

/**
 * 获取加密数据
 */
function getBusinessStr() {
    var str = "";
    AS_GetBusinessString(function (ret) {
        str = ret.retVal;

        if (str == "") {
            layerAlert("加密数据获取失败!");
            console.log("get business str error : " + GetErrorMessage(getLastError()));
        } else {
            console.log("get business str success : " + str);
            dataJson = str;
             AS_GetSignEvidenceData(0,function (ret) {
                 var baseImage64 = ret.retVal;
                 console.log("----------------当前签章imgBase64----------------\n "+baseImage64);
                 disConnect();
                 signPdf(dataJson,baseImage64);
            });

        }
    })

}

/**
 * 签名设备断开连接
 */
function disConnect() {
    AS_DisConnectDev(function (ret) {
        rv = ret.retVal;
        if (0 != rv) {
            layerAlert("签名设备断开连接失败!");
            console.log("disconnect error : " + GetErrorMessage(rv))
        } else {
            console.log("disconnect success");
        }
    });
}

function getLastError() {
    var errorCode = 0;
    AS_GetLastError(function (ret) {
        errorCode = ret.retVal;
    })

    return errorCode;
}

/**
 * 校验是否所有专家签名结束，同时保存当前专家签名后的签名数据
 * @param dataJson 当前签名专家签名数据
 * @param baseImage64 签名图片
 */
function signPdf(dataJson,  baseImage64) {
    layerLoading('签名保存中, 请稍后...', null, null, 0);
    $.ajax({
        url: "/signature/signReport",
        type: "POST",
        data: {
            "json": dataJson,
            "expertId": wono,
            "bidSectionId": bidSectionId,
            "expertSignatureImage":baseImage64
        },
        success: function (data) {
            //判断是否还有专家未签字
            if (isNull(nextExpertName)) {
                setTimeout(function () {
                    allSigarView();
                },500);
            } else {
                setTimeout(function () {
                    eleSignView();
                },400);

            }
        },
        error: function () {
            layerAlertAndEnd("签名失败!ERROR", null, null, null, 2);
        }
    })
}