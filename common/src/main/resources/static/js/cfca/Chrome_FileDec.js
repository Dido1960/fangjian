function BrowserInfo() {
    var res = {
        name: "",
        version: "",
    };
    var reg;
    var userAgent = self.navigator.userAgent;

    if (reg = /edge\/([\d\.]+)/i.exec(userAgent)) {
        res.name = "Edge";
        res.version = reg[1];
    } else if (/msie/i.test(userAgent)) {
        res.name = "Internet Explorer";
        res.version = /msie ([\d\.]+)/i.exec(userAgent)[1];
    } else if (/Trident/i.test(userAgent)) {
        res.name = "Internet Explorer";
        res.version = /rv:([\d\.]+)/i.exec(userAgent)[1];
    } else if (/chrome/i.test(userAgent)) {
        res.name = "Chrome";
        res.version = /chrome\/([\d\.]+)/i.exec(userAgent)[1];
    } else if (/safari/i.test(userAgent)) {
        res.name = "Safari";
        res.version = /version\/([\d\.]+)/i.exec(userAgent)[1];
    } else if (/firefox/i.test(userAgent)) {
        res.name = "Firefox";
        res.version = /firefox\/([\d\.]+)/i.exec(userAgent)[1];
    }
    return res;
}

var CryptoKit;

function LoadObj() {
    try {
        var browser = BrowserInfo();
        if (("Chrome" == browser.name && parseInt(browser.version) > 41)) {

            CryptoKit = new nmCryptokit(browser.name);

            CryptoKit.init()
                .then(function () {
                })
                .catch(function (response) {
                    alert(response.result);
                    installExtension();
                });
        } else {
            alert("Unsupported browser!");
        }
    } catch (e) {
        alert(e);
        return;
    }
}

function installExtension() {
    var browser = BrowserInfo();
    if ("Chrome" == browser.name) {
        alert("Please install extension for Chrome!");
    } else {
        alert("Unsupported browser!");
    }
}

function OnLoad() {
    // Waiting for extension loading
    document.getElementById("CryptoKitExtension").style.display = "none";
    setTimeout('LoadObj()', 500);
}

function OnUnLoad() {
    try {
        CryptoKit.uninit().then(function () {
            EnableButton(false);
        });
    } catch (e) {
        alert(e);
        return;
    }
}

function SelectCertificateOnClick() {
    document.getElementById("SelectCertResult").value = "false";

    var subjectDNFilter = document.getElementById("SubjectDNFilter").value;
    var issuerDNFilter = document.getElementById("IssuerDNFilter").value;
    var serialNumFilter = document.getElementById("SerialNumFilter").value;
    var SM2ProviderNameList = document.getElementById("SM2ProviderNameList").value;

    CryptoKit.selectSignCert(subjectDNFilter, issuerDNFilter, serialNumFilter, SM2ProviderNameList)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("SelectCertResult").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function SelectEncryptCertOnClick() {
    document.getElementById("SelectCertResult").value = "false";

    var subjectDNFilter = document.getElementById("SubjectDNFilter").value;
    var issuerDNFilter = document.getElementById("IssuerDNFilter").value;
    var serialNumFilter = document.getElementById("SerialNumFilter").value;
    var SM2ProviderNameList = document.getElementById("SM2ProviderNameList").value;

    CryptoKit.selectEncryptCert(subjectDNFilter, issuerDNFilter, serialNumFilter, SM2ProviderNameList)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("SelectCertResult").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function GetCertInfoOnClick() {
    document.getElementById("CertInfoContent").value = "";

    var InfoTypeID = GetSelectedItemValue("InfoTypeID");

    CryptoKit.getSignCertInfo(InfoTypeID)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("CertInfoContent").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function GetEncryptCertInfoOnClick() {
    document.getElementById("CertInfoContent").value = "";

    var InfoTypeID = GetSelectedItemValue("InfoTypeID");

    CryptoKit.getEncryptCertInfo(InfoTypeID)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("CertInfoContent").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}


function SignMessageP7DetachedOnClick() {
    document.getElementById("MessageSignature").value = "";

    var source = document.getElementById("Signsourcemessage").value;
    var selectedAlg = GetSelectedItemValue("msgalgorithm");
    var IsWithSource = false;

    CryptoKit.signMsgPKCS7(source, selectedAlg, IsWithSource)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("MessageSignature").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function VerifyMessageP7DetachedOnClick() {

    var signature = document.getElementById("MessageSignature").value;
    var source = document.getElementById("Signsourcemessage").value;

    CryptoKit.verifyMsgSignaturePKCS7Detached(signature, source)
        .then(function (response) {
            EnableButton(true);
            alert("验签成功！");
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function SignFileP7DetachedOnClick() {
    document.getElementById("FileSignature").value = "";

    var SourceFilePath = document.getElementById("SignFilePath").value;

    CryptoKit.signFilePKCS7Detached(SourceFilePath, "SHA-1")
        .then(function (response) {
            EnableButton(true);
            document.getElementById("FileSignature").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function VerifyFileP7DetachedOnClick() {

    var signature = document.getElementById("FileSignature").value;
    var source = document.getElementById("SignFilePath").value;

    CryptoKit.verifyFileSignaturePKCS7Detached(signature, source)
        .then(function (response) {
            EnableButton(true);
            alert("验签成功！");
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

/**
 * 设置加密证书
 * @param qsCertVal 权晟证书
 * @constructor
 */
function SetBase64EncryptCertButtonOnClick(qsCertVal) {
    CryptoKit.setEncryptCert(qsCertVal)
        .then(function (response) {
        })
        .catch(function (response) {
            ShowErrorInfo(response);
        });
}

/**
 * 数字信封加密
 * @param sourceData 加密原始数据
 * @param targetId 输出的目标
 * @constructor
 */
function EncryptMsgEnvelopeOnClick(sourceData, targetId) {
    CryptoKit.encryptMsgCMSEnvelope(sourceData, "SM4")
        .then(function (response) {
            $(targetId).val(response.result);
        })
        .catch(function (response) {
            ShowErrorInfo(response);
        });
}

/**
 * 数字信封解密
 * @param EncryptedData 待解密数据
 * @param targetId 输出的目标
 * @constructor
 */
function MsgEnvelopeDecryptOnClick(EncryptedData, targetId,resultBack) {
    var SM2ProviderNameList = "Longmai GM3000 for CFCA CSP V1.1";
    CryptoKit.decryptMsgCMSEnvelope(EncryptedData, SM2ProviderNameList)
        .then(function (response) {
            // 返回解密数据
            $(targetId).val(response.result);
            resultBack(response);
        })
        .catch(function (response) {
            ShowErrorInfo(response);
            resultBack(response);
        });
}

function GenSymKeyButtonOnClick() {
    document.getElementById("SymKey").value = "";

    var SymAlg = GetSelectedItemValue("SymAlg");

    CryptoKit.genSymKey(SymAlg)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("SymKey").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function SymEncryptMsgOnClick() {
    document.getElementById("SymEncryptMessage").value = "";

    var SourceMsg = document.getElementById("SymEncSourcemassage").value;
    var SymAlg = GetSelectedItemValue("SymAlg");
    var SymKey = document.getElementById("SymKey").value;

    CryptoKit.symEncryptMessage(SourceMsg, SymAlg, SymKey)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("SymEncryptMessage").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function SymMsgDecryptButtonOnClick() {
    document.getElementById("MsgSymDecrypt").value = "";

    var EncryptedMsg = document.getElementById("SymEncryptMessage").value;
    var SymAlg = GetSelectedItemValue("SymAlg");
    var SymKey = document.getElementById("SymKey").value;

    CryptoKit.symDecryptMessage(EncryptedMsg, SymAlg, SymKey)
        .then(function (response) {
            EnableButton(true);
            document.getElementById("MsgSymDecrypt").value = response.result;
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function SymEncryptFileOnClick() {

    var SourceFile = document.getElementById("SymEncSourceFile").value;
    var SymAlg = GetSelectedItemValue("SymAlg");
    var SymKey = document.getElementById("SymKey").value;
    var EncryptedFile = document.getElementById("SymEncryptFile").value;

    CryptoKit.symEncryptFile(SourceFile, SymAlg, SymKey, EncryptedFile)
        .then(function (response) {
            EnableButton(true);
            alert("文件对称加密成功！");
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}

function SymFileDecryptButtonOnClick() {

    var EncryptedFile = document.getElementById("SymEncryptFile").value;
    var SymAlg = GetSelectedItemValue("SymAlg");
    var SymKey = document.getElementById("SymKey").value;
    var DecryptedFile = document.getElementById("SymDecryptFile").value;

    CryptoKit.symDecryptFile(EncryptedFile, SymAlg, SymKey, DecryptedFile)
        .then(function (response) {
            EnableButton(true);
            alert("文件对称解密成功！");
        })
        .catch(function (response) {
            EnableButton(true);
            ShowErrorInfo(response);
        });
    EnableButton(false);
}


function ShowErrorInfo(response) {
    if (null == response) {
        alert("Extension has been remove!");
    } else if (1 == response.errorcode) { // connection error
        alert(response.result);
    } else { // host error
        CryptoKit.getLastErrorDesc()
            .then(function (response) {
                alert(response.result);
            });
    }
}

function GetSelectedItemValue(itemName) {
    var ele = document.getElementsByName(itemName);
    for (i = 0; i < ele.length; i++) {
        if (ele[i].checked) {
            return ele[i].value;
        }
    }
}

