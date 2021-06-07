var CryptoAgent;


function OnLoad() {
    try {
        var eDiv = document.createElement("div");
        var indexOpr = navigator.appVersion.indexOf("OPR");
        var chromestr = "Chrome/";
        var index = navigator.appVersion.indexOf(chromestr);
        if (index >= 0 && indexOpr < 0) {
            alert("Please use ie!")
        } else {
            CryptoAgent = new Cryptokit();
        }
        CryptoAgent.init();
    } catch (e) {
        alert(e);

    }
}

function OnUnLoad() {
    try {
        CryptoAgent.uninit();
    } catch (e) {
        alert(e);

    }
}


function GetLastErrorCallback(result) {
    alert(result.value);
}

function GetVersionOnClick() {
    CryptoAgent.GetVersion(
        function callback(result) {
            if (0 === result.error) {
                alert(result.value);
            } else {
                CryptoAgent.GetLastErrorDesc(GetLastErrorCallback);
            }
        });
}


/**
 * 信封解密
 * @param EncryptedData 待解密数据
 * @param targetId 输出的目标
 * @constructor
 */
function MsgEnvelopeDecryptOnClick(EncryptedData, targetId,resultBack) {
    var SM2ProviderNameList = "Longmai GM3000 for CFCA CSP V1.1";
    CryptoAgent.decryptMsgCMSEnvelope(EncryptedData, SM2ProviderNameList,
        function callback(result) {
            if (0 === result.error) {
                // 返回解密数据
                $(targetId).val(result.value);
                resultBack(result);
            } else {
                CryptoAgent.GetLastErrorDesc(GetLastErrorCallback);
                resultBack(result);
            }
        });
}

/**
 * 设置加密证书
 * @param qsCertVal 权晟证书
 * @constructor
 */
function SetBase64EncryptCertButtonOnClick(qsCertVal) {
    CryptoAgent.setEncryptCert(qsCertVal,
        function callback(result) {
            if (0 === result.error) {
            } else {
                CryptoAgent.GetLastErrorDesc(GetLastErrorCallback);
            }
        });
}

/**
 * 数字信封加密
 * @param sourceData 加密原始数据
 * @param targetId 输出的目标
 * @constructor
 */
function EncryptMsgEnvelopeOnClick(sourceData, targetId) {

    CryptoAgent.encryptMsgCMSEnvelope(sourceData, "SM4",
        function callback(result) {
            if (0 === result.error) {
                $(targetId).val(result.value)
            } else {
                CryptoAgent.GetLastErrorDesc(GetLastErrorCallback);
            }
        });
}