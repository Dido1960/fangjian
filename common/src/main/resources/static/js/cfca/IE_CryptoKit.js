function Cryptokit() {
};


Cryptokit.prototype.init = function () {
    var eDiv = document.createElement("div");
    if (navigator.appName.indexOf("Internet") >= 0 || navigator.appVersion.indexOf("Trident") >= 0) {
        if (window.navigator.cpuClass == "x86") {
            eDiv.innerHTML = "<object id=\"cryptokitCtrl\" codebase=\"CryptoKit.SCQSKJ.x86.cab\" classid=\"clsid:2E17057B-C00D-4053-8E39-1AFCFA9BCA94\" ></object>";
        }
        else {
            eDiv.innerHTML = "<object id=\"cryptokitCtrl\" codebase=\"CryptoKit.SCQSKJ.x64.cab\" classid=\"clsid:2FCFC764-7108-4af0-AE4E-C0C62E186D34\" ></object>";
        }
    }
    else {
        alert("Please use IE!");
    }
    document.body.appendChild(eDiv);
}


Cryptokit.prototype.uninit = function () {
}


Cryptokit.prototype.GetVersion = function (callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").GetVersion();
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.selectSignCert = function (subjectDNFilter, issuerDNFilter, serialNumFilter, SM2ProviderNameListFilter, callback) {
    var result = new Object() ;
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SelectSignCert(subjectDNFilter, issuerDNFilter, serialNumFilter, SM2ProviderNameListFilter);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.selectEncryptCert = function (subjectDNFilter, issuerDNFilter, serialNumFilter, SM2ProviderNameListFilter, callback) {
    var result = new Object() ;
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SelectEncryptCert(subjectDNFilter, issuerDNFilter, serialNumFilter, SM2ProviderNameListFilter);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.getSignCertInfo = function (InfoTypeID, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").GetSignCertInfo(InfoTypeID);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.getEncryptCertInfo = function (InfoTypeID, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").GetEncryptCertInfo(InfoTypeID);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.validatePin = function (KeyPin, callback) {
    var result = new Object() ;
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").ValidatePin(KeyPin);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.signMsgPKCS7 = function (source, selectedAlg, IsWithSource, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SignMsgPKCS7(source, selectedAlg, IsWithSource);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.verifyMsgSignaturePKCS7Detached = function (signature, source, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").VerifyMsgSignaturePKCS7Detached(signature, source);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.signFilePKCS7Detached = function (sourcefile, selectedAlg, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SignFilePKCS7Detached (sourcefile, selectedAlg);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.verifyFileSignaturePKCS7Detached = function (Signature, sourcefile, callback) {
    var result = new Object() ;
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").VerifyFileSignaturePKCS7Detached(Signature, sourcefile);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.setEncryptCert = function (Base64CertData, callback) {
    var result = new Object() ;
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SetEncryptCert(Base64CertData);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.encryptMsgCMSEnvelope = function (EncryptSourceData, SysAlg, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").EncryptMsgCMSEnvelope(EncryptSourceData, SysAlg);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.decryptMsgCMSEnvelope = function (EncryptData, SM2ProviderNameList, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").DecryptMsgCMSEnvelope(EncryptData, SM2ProviderNameList);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.genSymKey = function (SymAlg, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").GenSymKey(SymAlg);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.symEncryptMessage = function (SourceMsg, SymAlg, SymKey, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SymEncryptMessage(SourceMsg, SymAlg, SymKey);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.symDecryptMessage = function (EncryptedMsg, SymAlg, SymKey, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SymDecryptMessage(EncryptedMsg, SymAlg, SymKey);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.symEncryptFile = function (SourceFile, SymAlg, SymKey, EncryptedFile, callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SymEncryptFile(SourceFile, SymAlg, SymKey, EncryptedFile);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.symDecryptFile = function (EncryptedFile, SymAlg, SymKey, DecryptedFile, callback) {
    var result = new Object() ;
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").SymDecryptFile(EncryptedFile, SymAlg, SymKey, DecryptedFile);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.getSealImage = function (P11Name, callback) {
    var result = new Object() ;
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").GetSealImage(P11Name);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
    callback(result);
}


Cryptokit.prototype.GetLastErrorDesc = function (callback) {
    var result = new Object();
    try {
        result.error = 0;
        result.value = document.getElementById("cryptokitCtrl").GetLastErrorDesc();
        callback(result);
    }
    catch (e) {
        result.error = -1;
        result.value = e.message;
    }
}
