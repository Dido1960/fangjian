var chromeExtension = "gpfcpmhdijljecigipjknmbglklajpag";
var productID = "com.cfca.cryptokit.scqskj";

var extensionName = productID + ".extension";
var reqEventName  = productID + ".request";
var respEventName = productID + ".response";

Browser = {
    IE:      "Internet Explorer",
    Edge:    "Edge",
    Chrome:  "Chrome",
    Safari:  "Safari",
    Firefox: "Firefox",
};


// Encapsulate Chrome sendMessage callback to Promise
function SendMessageforChrome(request) {
    return new Promise( function (resolve, reject) {
        chrome.runtime.sendMessage(chromeExtension, request, function (response) {
			if (response) {
				if(0 == response.errorcode){ 
                    resolve(response);                                    
				}
				else{
                    reject(response);
				}
			}
			else {
			    var result = new Object();
			    result.errorcode = 1;
			    result.result = chrome.runtime.lastError.message;
			    reject(result);
			}
		});
    });
}


// Encapsulate Edge&Firefox event to Promise
function SendMessagebyEvent(request) {
    var event = new CustomEvent(reqEventName, { detail: request });
    document.dispatchEvent(event);

    return new Promise( function (resolve, reject) {
	
	    var responseEventName = respEventName;
	    if(request.funcInfo != undefined && request.funcInfo.randomId != undefined)
			responseEventName += ("." + request.funcInfo.randomId);
			
        document.addEventListener(responseEventName, function CallBack(e) {
            document.removeEventListener(e.type, CallBack);
            var eJson = JSON.parse(e.detail);
            if (null != eJson && 0 == eJson.errorcode) {
                resolve(eJson);
            }
            else {
                reject(eJson);
            }
        }, false);
    });
}

function SendMessage(browser, requestJSON) {
    if (Browser.Chrome == browser) {
        return SendMessageforChrome(requestJSON);
    }
    else {
        return SendMessagebyEvent(requestJSON);
    }
}


function checkExtension (browser) {
    return new Promise(function (resolve, reject) {
        var result = new Object();
        if (Browser.Chrome == browser) {
            // chrome.runtime.sendMessage() could check extension  existence.
            resolve(browser);
        }
        else if (Browser.Edge == browser || Browser.Firefox == browser) {
            if (document.getElementById(extensionName)) {
                resolve(browser);
            }
            else {
                result.errorcode = 1;
                result.result = "Extension does not exist!";
                reject(result);
            }
        }
        else {
            result.errorcode = 1;
            result.result = "Only support Chrome/Edge/Firefox";
            reject(result);
        }
    });
}


function nmCryptokit(browser) {

    this.browser = browser;
};

function GenerateRandomId() {
    var charstring = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890';
    var maxPos = charstring.length;
    var randomId = '';
    for (i = 0; i < 10; i++) {
        randomId += charstring.charAt(Math.floor(Math.random() * maxPos));
    }
    return randomId;
}
	
nmCryptokit.prototype.init = function () {

    var browser = this.browser;

    return checkExtension(browser)
        .then(function (browser) {
            var request = new Object();
            request.action = "connect";
            request.host = productID;
            return SendMessage(browser, request);
        }).then(function () {
            var request = new Object();
            request.action = "checkHost";
            return SendMessage(browser, request);
        });
}


nmCryptokit.prototype.uninit = function () {

    var request = new Object();                   
    request.action = "disconnect";
    request.host = productID;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.selectSignCert = function (strSubjectDNFilter, strIssuerDNFilter, strSerialNo, strSM2ProviderName) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strSubjectDNFilter);
    paramArr.push(strIssuerDNFilter);
    paramArr.push(strSerialNo);
    paramArr.push(strSM2ProviderName);
       
    funcInfo.function = "SelectSignCert";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.selectEncryptCert = function (strSubjectDNFilter, strIssuerDNFilter, strSerialNo, strSM2ProviderName) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strSubjectDNFilter);
    paramArr.push(strIssuerDNFilter);
    paramArr.push(strSerialNo);
    paramArr.push(strSM2ProviderName);
       
    funcInfo.function = "SelectEncryptCert";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.getSignCertInfo = function (strInfoType) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();	
    
    paramArr.push(strInfoType);
       
    funcInfo.function = "GetSignCertInfo";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.getEncryptCertInfo = function (strInfoType) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();	
    
    paramArr.push(strInfoType);
       
    funcInfo.function = "GetEncryptCertInfo";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.signMsgPKCS7 = function (strsource, strselectedAlg, bAttached) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();	
    
    paramArr.push(strsource);
    paramArr.push(strselectedAlg);
    paramArr.push(bAttached);
       
    funcInfo.function = "SignMsgPKCS7";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.verifyMsgSignaturePKCS7Detached = function (strsignature, strsource) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strsignature);
    paramArr.push(strsource);
       
    funcInfo.function = "VerifyMsgSignaturePKCS7Detached";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.signFilePKCS7Detached = function (strsourcefile, strselectedAlg) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strsourcefile);
    paramArr.push(strselectedAlg);
       
    funcInfo.function = "SignFilePKCS7Detached";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.verifyFileSignaturePKCS7Detached = function (strsignature, strSourceFilePath) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strsignature);
    paramArr.push(strSourceFilePath);
       
    funcInfo.function = "VerifyFileSignaturePKCS7Detached";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.setEncryptCert = function (strBase64CertData) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strBase64CertData);
       
    funcInfo.function = "SetEncryptCert";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.encryptMsgCMSEnvelope = function (strEncryptmassage, strEncryptAlg) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strEncryptmassage);
    paramArr.push(strEncryptAlg);
       
    funcInfo.function = "EncryptMsgCMSEnvelope";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.decryptMsgCMSEnvelope = function (strEnvelope, strSm2ProviderNameList) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strEnvelope);
    paramArr.push(strSm2ProviderNameList);
       
    funcInfo.function = "DecryptMsgCMSEnvelope";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.genSymKey = function (strSymAlg) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strSymAlg);
       
    funcInfo.function = "GenSymKey";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.symEncryptMessage = function (strSourcemsg, strEncryptalg, strSymkey) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strSourcemsg);
    paramArr.push(strEncryptalg);
    paramArr.push(strSymkey);
       
    funcInfo.function = "SymEncryptMessage";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.symDecryptMessage = function (strEncryptedMsg, strEncryptalg, strSymkey) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strEncryptedMsg);
    paramArr.push(strEncryptalg);
    paramArr.push(strSymkey);
       
    funcInfo.function = "SymDecryptMessage";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
        
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.symEncryptFile = function (strSourceFile, strSymAlg, strSymkey, strEncryptedFile) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strSourceFile);
    paramArr.push(strSymAlg);
    paramArr.push(strSymkey);
    paramArr.push(strEncryptedFile);
       
    funcInfo.function = "SymEncryptFile";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.symDecryptFile = function (strEncryptedFile, strSymAlg, strSymkey, strDecryptedFile) {
          
    var request = new Object();
    var funcInfo = new Object();
    var paramArr = new Array();
    var randomId = GenerateRandomId();
    
    paramArr.push(strEncryptedFile);
    paramArr.push(strSymAlg);
    paramArr.push(strSymkey);
    paramArr.push(strDecryptedFile);
       
    funcInfo.function = "SymDecryptFile";
    funcInfo.params = paramArr;
    funcInfo.randomId = randomId;
       
    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}


nmCryptokit.prototype.getLastErrorDesc = function () {
          
    var request = new Object();
    var funcInfo = new Object();
    var randomId = GenerateRandomId();

    funcInfo.function = "GetLastErrorDesc";
    funcInfo.params = null;
    funcInfo.randomId = randomId;

    request.action = "invoke";
    request.funcInfo = funcInfo;

    return SendMessage(this.browser, request);
}
