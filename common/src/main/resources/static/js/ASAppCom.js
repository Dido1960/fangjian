/*--------------------------------------------------------------------------  
 *
 * BJCA Adaptive Javascript, Version SAB(Support All Browsers :))
 * This script support bjca xss client version 2.0 and dcds1.9.3、dsps2.6.1.
 * Author:BJCA-xf
 *--------------------------------------------------------------------------*/

/* globals var */
var $_$ASAlert = null;     // alert custom function
var $_$ASAppObj = null;    	// ASAppCom class Object
var $_$DCDSObj = null;    	// DCDSCom class Object
var $_$DSPSObj = null;      // DSPSCom class Object
//var $_$ASWebSocketObj = null; // WebSocket class Object for future 
var $_$ASCurrentObj = null;   // Current use class Object
var $_$AS_PenOperationCallBackFunc = null; //custom AS_PenOperation callback function


// set custom alert function
function SetAlertFunction(custom_alert) 
{
	$_$ASAlert = custom_alert;
}

function $checkBrowserISIE() 
{
	return (!!window.ActiveXObject || 'ActiveXObject' in window) ? true : false;
}

// set custom AS_PenOperation callback
function AS_SetPenOperationCallBack(callback) 
{
	$_$AS_PenOperationCallBackFunc = callback;
}

//缺省笔事件操作函数
function DefaultGetPenOperation(opType, xpos, ypos)
{
	var tStrMsg = "";
	switch(opType){
		case 1:	// 点击操作
		
			tStrMsg = "点击" + "<" + xpos + "," + ypos + ">";
			var btns = document.getElementsByTagName("button");

			for(var i = 0; i < btns.length; i++){
				var btn_width = btns[i].offsetWidth;
				var btn_height = btns[i].offsetHeight;
				var btn_left = getAbsoluteLeft(btns[i]) + window.screenLeft;
				var btn_top = getAbsoluteTop(btns[i]) + window.screenTop - document.documentElement.scrollTop;
				//alert("top : " + getAbsoluteTop(btns[i].id) + "; ypos: " + ypos + "; scrollTop :" +document.documentElement.scrollTop  + "; btn_top : " +btn_top+ ";  screenTop: " + window.screenTop);

				if (btn_left < xpos &
					xpos < btn_left + btn_width &
					btn_top < ypos &
					ypos < btn_top + btn_height)
				{
					btns[i].onclick();
				}
			}
			break;
		case 2:	// 向上滑动
			tStrMsg = "上滑" + "<" + xpos + "," + ypos + ">";
			for(var i = 0; i < ypos; i += 10){
				window.scrollBy(0,10);
			}
			break;
		case 3:
			tStrMsg = "下滑" + "<" + xpos + "," + ypos + ">";
			for(var i = 0; i < ypos; i += 10){
				window.scrollBy(0, -10);
			}
			break;
		default:
			tStrMsg = "未知操作：" + opType;
			break;
	}
	
	return tStrMsg;
}
		
//AS_PenOperation(LONG opType, LONG xpos, LONG ypos)
function $AS_PenOperation(opType, xpos, ypos) 
{
	if (typeof $_$AS_PenOperationCallBackFunc == 'function') {
		return $_$AS_PenOperationCallBackFunc(opType, xpos, ypos);
	} else {
		return DefaultGetPenOperation(opType, xpos, ypos);		
	}
}

// IE11 attach event
function $AttachIE11AS_PenOperationEvent(strObjName) 
{
	var handler = document.createElement("script");
	handler.setAttribute("for", strObjName);
	handler.setAttribute("event", "AS_PenOperation(opType, xpos, ypos)");
	handler.appendChild(document.createTextNode("$AS_PenOperation(opType, xpos, ypos)"));
	document.body.appendChild(handler);
}

//load a control
function $LoadControl(CLSID, ctlName, testFuncName, addEvent) 
{
	var pluginDiv = document.getElementById("pluginDiv" + ctlName);
	if (pluginDiv) {
		return true;
	}
	pluginDiv = document.createElement("div");
	pluginDiv.id = "pluginDiv" + ctlName;
	document.body.appendChild(pluginDiv);
	
	try {	
		if ($checkBrowserISIE()) {	// IE
			pluginDiv.innerHTML = '<object id="' + ctlName + '" classid="CLSID:' + CLSID + '" style="HEIGHT:0px; WIDTH:0px"></object>';

		} else {
            var chromeVersion = window.navigator.userAgent.match(/Chrome\/(\d+)\./);
            if (chromeVersion && chromeVersion[1]) {
                if (parseInt(chromeVersion[1], 10) >= 42) { // not support npapi return false
                    document.body.removeChild(pluginDiv);
                    pluginDiv.innerHTML = "";
                    pluginDiv = null;
                    return false;
                }
            }

			if (addEvent) {
                pluginDiv.innerHTML = '<embed id=' + ctlName + ' type=application/x-xtx-axhost clsid={' + CLSID + '} event_AS_PenOperation=$AS_PenOperation width=0 height=0 />' ;
			} else {
				pluginDiv.innerHTML = '<embed id=' + ctlName + ' type=application/x-xtx-axhost clsid={' + CLSID + '} width=0 height=0 />' ;
			}	
		}
		
		if (testFuncName != null && testFuncName != "" && eval(ctlName + "." + testFuncName) == undefined) {
            document.body.removeChild(pluginDiv);
            pluginDiv.innerHTML = "";
            pluginDiv = null;
            return false; 
		}
		return true;
	} catch (e) {
		document.body.removeChild(pluginDiv);
		pluginDiv.innerHTML = "";
		pluginDiv = null;
		return false;
	}
}

function $ASAlert(strMsg) {
	if (typeof $_$ASAlert == 'function') {
		$_$ASAlert(strMsg);
	} else {
		alert(strMsg);
	}	
}

function $myOKRtnFunc(retVal, cb, ctx)
{
    if (typeof cb == 'function') {
        var retObj = {retVal:retVal, ctx:ctx};
        cb(retObj);
    } 
    return retVal;
}

//ASAppCom class
function CreateASAppObject() {	
	var bOK = $LoadControl("BA878047-4D57-40FA-BB43-C20BA6AB4175", "ASAppCom", "AS_GetVersion()", true);
	if (!bOK) {
		return null;
	}	
	
	var o = new Object();
	
	//AS_InitSign
	o.AS_InitSign = function(signType, cb, ctx) {
		var ret = ASAppCom.AS_InitSign(signType)
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_SetBusinessParam
	o.AS_SetBusinessParam = function(ParamType, Param, cb, ctx) {
		var ret = ASAppCom.AS_SetBusinessParam(ParamType, Param);
		return $myOKRtnFunc(ret, cb, ctx);
	};
    
	//AS_SetSignerInfo	
	o.AS_SetSignerInfo = function(name, IDType, ID, cb, ctx) {
		var ret = ASAppCom.AS_SetSignerInfo(name, IDType, ID);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_SetSignEvidenceData
	o.AS_SetSignEvidenceData = function(dataJson, cb, ctx) {
		var ret = ASAppCom.AS_SetSignEvidenceData(dataJson);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_AddSignEvidenceData
	o.AS_AddSignEvidenceData = function(cb, ctx) {
		var ret = ASAppCom.AS_AddSignEvidenceData();
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_TakePhoto
	o.AS_TakePhoto = function(cb, ctx) {
		var ret = ASAppCom.AS_TakePhoto();
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_GetSignGUID
	o.AS_GetSignGUID = function(cb, ctx) {
		var ret = ASAppCom.AS_GetSignGUID();
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_AddMulWord(BSTR content, LONG megerNum)
	o.AS_AddMulWord = function(content, megerNum, cb, ctx) {
		var ret = ASAppCom.AS_AddMulWord(content, megerNum);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_GetSignEvidenceData(LONG dataType)
	o.AS_GetSignEvidenceData = function(dataType, cb, ctx) {
		var ret = ASAppCom.AS_GetSignEvidenceData(dataType);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_DeleteSign(BSTR SignGUID)
	o.AS_DeleteSign = function(SignGUID, cb, ctx) {
		var ret = ASAppCom.AS_DeleteSign(SignGUID);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_ReusedSign(BSTR SignGUID)
	o.AS_ReusedSign = function(SignGUID, cb, ctx) {
		var ret = ASAppCom.AS_ReusedSign(SignGUID);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_SetTimeout(LONG nTimeout)
	o.AS_SetTimeout = function(nTimeout, cb, ctx) {
		var ret = ASAppCom.AS_SetTimeout(nTimeout);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_SetDlgPos(LONG left, LONG top)
	o.AS_SetDlgPos = function(left, top, cb, ctx) {
		var ret = ASAppCom.AS_SetDlgPos(left, top);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_SetDlgSize(LONG width, LONG height)
	o.AS_SetDlgSize = function(width, height, cb, ctx) {
		var ret = ASAppCom.AS_SetDlgSize(width, height);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_AddEvidenceHash(BSTR evidenceDataBase64, BSTR mimeType, BSTR bioType)
	o.AS_AddEvidenceHash = function(evidenceDataBase64, mimeType, bioType, cb, ctx) {
		var ret = ASAppCom.AS_AddEvidenceHash(evidenceDataBase64, mimeType, bioType);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_GetBusinessString()
	o.AS_GetBusinessString = function(cb, ctx) {
		var ret = ASAppCom.AS_GetBusinessString();
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_GetSignPackage(BSTR businessString)
	o.AS_GetSignPackage = function(businessString, cb, ctx) {
		var ret = ASAppCom.AS_GetSignPackage(businessString);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_SetPdfGenerateData(LONG type, BSTR data, BSTR dataID)
	o.AS_SetPdfGenerateData = function(type, data, dataID, cb, ctx) {
		var ret = ASAppCom.AS_SetPdfGenerateData(type, data, dataID);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_SetPosKW(BSTR kw, LONG kwIndex, LONG XOffset, LONG YOffset, LONG width, LONG height);
	o.AS_SetPosKW = function(kw, kwIndex, XOffset, YOffset, width, height, cb, ctx) {
		var ret = ASAppCom.AS_SetPosKW(kw, kwIndex, XOffset, YOffset, width, height);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_SetPosXY (LONG left, LONG bottom, LONG right, LONG top, LONG pageNo);
	o.AS_SetPosXY = function(left, bottom, right, top, pageNo, cb, ctx) {
		var ret = ASAppCom.AS_SetPosXY(left, bottom, right, top, pageNo);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_SetPosRule (BSTR ruleNo);
	o.AS_SetPosRule = function(ruleNo, cb, ctx) {
		var ret = ASAppCom.AS_SetPosRule(ruleNo);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_AddUnitSign(BSTR imageBase, BSTR appName)
	o.AS_AddUnitSign = function(imageBase, appName, cb, ctx) {
		var ret = ASAppCom.AS_AddUnitSign(imageBase, appName);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_SetSignPlain(BSTR plainBase);
	o.AS_SetSignPlain = function(plainBase, cb, ctx) {
		var ret = ASAppCom.AS_SetSignPlain(plainBase);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_GetSignatureCount(BSTR signPackage)
	o.AS_GetSignatureCount = function(signPackage, cb, ctx) {
		var ret = ASAppCom.AS_GetSignatureCount(signPackage);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_GetIndexSignature(BSTR signPackage, LONG index)
	o.AS_GetIndexSignature = function(signPackage, index, cb, ctx) {
		var ret = ASAppCom.AS_GetIndexSignature(signPackage, index);
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_VerifySign(BSTR signValue, BSTR plainBase64)
	o.AS_VerifySign = function(signValue, plainBase64, cb, ctx) {
		var ret = ASAppCom.AS_VerifySign(signValue, plainBase64);
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_VerifyTimeStamp(BSTR tsValue, BSTR tsPlainData)
	o.AS_VerifyTimeStamp = function(tsValue, tsPlainData, cb, ctx) {
		var ret = ASAppCom.AS_VerifyTimeStamp(tsValue, tsPlainData);
		return $myOKRtnFunc(ret, cb, ctx);
	};		

	//AS_GetDataFromSignValue(BSTR signValue, BSTR plainBase64, LONG dataType)
	o.AS_GetDataFromSignValue = function(signValue, plainBase64, dataType, cb, ctx) {
		var ret = ASAppCom.AS_GetDataFromSignValue(signValue, plainBase64, dataType);
		return $myOKRtnFunc(ret, cb, ctx);
	};		

	//AS_ConnectDev
	o.AS_ConnectDev = function(cb, ctx) {
		var ret = ASAppCom.AS_ConnectDev();
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_DisConnectDev();
	o.AS_DisConnectDev = function(cb, ctx) {
		var ret = ASAppCom.AS_DisConnectDev();
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_GetExtendScreenPos();
	o.AS_GetExtendScreenPos = function(cb, ctx) {
		var ret = ASAppCom.AS_GetExtendScreenPos();
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_ReadFile(LPTSTR base64FileName)
	o.AS_ReadFile = function(base64FileName, cb, ctx) {
		var ret = ASAppCom.AS_ReadFile(base64FileName);
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_SaveFile(BSTR base64FileName, BSTR base64FileContent)
	o.AS_SaveFile = function(base64FileName, base64FileContent, cb, ctx) {
		var ret = ASAppCom.AS_SaveFile(base64FileName, base64FileContent);
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_HashFile(LPTSTR hashFileName, LONG hashAlg)
	o.AS_HashFile = function(hashFileName, hashAlg, cb, ctx) {
		var ret = ASAppCom.AS_HashFile(hashFileName, hashAlg);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_HashData(LPTSTR plainData, LONG hashAlg)
	o.AS_HashData = function(plainData, hashAlg, cb, ctx) {
		var ret = ASAppCom.AS_HashData(plainData, hashAlg);
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_Base64Encode(LPCTSTR strData, LPCTSTR strType)
	o.AS_Base64Encode = function(strData, strType, cb, ctx) {
		var ret = ASAppCom.AS_Base64Encode(strData, strType);
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_Base64Decode(LPCTSTR strBase64)
	o.AS_Base64Decode = function(strBase64, cb, ctx) {
		var ret = ASAppCom.AS_Base64Decode(strBase64);
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_GetVersion
	o.AS_GetVersion = function(cb, ctx) {
		var ret = ASAppCom.AS_GetVersion();
		return $myOKRtnFunc(ret, cb, ctx);
	};	

	o.AS_ResizeImage = function(strBase64,imgW,imgType,cb, ctx) {
		var ret = ASAppCom.AS_ResizeImage(strBase64,imgW,imgType);
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_GetLastError();
	o.AS_GetLastError = function(cb, ctx) {
		var ret = ASAppCom.AS_GetLastError();
		return $myOKRtnFunc(ret, cb, ctx);
	};		
	
	//AS_MoveTo(LONG dlg_left, LONG dlg_top, LONG dlg_width, LONG dlg_height)
	o.AS_MoveTo = function(dlg_left, dlg_top, dlg_width, dlg_height, cb, ctx) {
		var ret = ASAppCom.AS_MoveTo(dlg_left, dlg_top, dlg_width, dlg_height);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	o.AS_MoveByTitle = function(title, left, top, width, height, cb, ctx) {
		var ret = ASAppCom.AS_MoveByTitle(title, left, top, width, height);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	o.AS_MoveToMonitorByTitle = function(title, monitorId, cb, ctx) {
		var ret = ASAppCom.AS_MoveToMonitorByTitle(title, monitorId);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_SetConfig(BSTR strKey, BSTR strValue)
	o.AS_SetConfig = function(strKey, strValue, cb, ctx) {
		var ret = ASAppCom.AS_SetConfig(strKey, strValue);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_ShowFile(BSTR fileData, LONG fileType)
	o.AS_ShowFile = function(fileData, fileType, cb, ctx) {
		var ret = ASAppCom.AS_ShowFile(fileData, fileType);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_EndShowFile()
	o.AS_EndShowFile = function(cb, ctx) {
		var ret = ASAppCom.AS_EndShowFile();
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_ShowHtml(BSTR htmlSrc, BSTR jsSrc)
	o.AS_ShowHtml = function(htmlSrc, jsSrc, cb, ctx) {
		var ret = ASAppCom.AS_ShowHtml(htmlSrc, jsSrc);
		return $myOKRtnFunc(ret, cb, ctx);
	};

	//AS_GetIDCardInfo()
	o.AS_GetIDCardInfo = function(cb, ctx) {
		var ret = ASAppCom.AS_GetIDCardInfo();
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_PdfSign()
	o.AS_PdfSign = function(cb, ctx) {
		var ret = ASAppCom.AS_PdfSign();
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	//AS_SetServerIP(BSTR mainIP,LONG mainPort,BSTR spareIP,LONG sparePort,LONG timeout)
	o.AS_SetServerIP = function(mainIP,mainPort,spareIP,sparePort,timeout,cb,ctx){
		var ret = ASAppCom.AS_SetServerIP(mainIP,mainPort,spareIP,sparePort,timeout);
		return $myOKRtnFunc(ret, cb, ctx);
	};
			
	//AS_SetHWRModel(LONG RecognizeNum)
	o.AS_SetHWRModel = function(RecognizeNum,cb,ctx){
		var ret = ASAppCom.AS_SetHWRModel(RecognizeNum);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_GetHWRResult()
	o.AS_GetHWRResult = function(cb, ctx) {
		var ret = ASAppCom.AS_GetHWRResult();
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//LONG AS_SetSignAlg(LONG SignAlg, BOOL IsTS)
	o.AS_SetSignAlg = function(SignAlg,IsTS,cb,ctx){
		var ret = ASAppCom.AS_SetSignAlg(SignAlg,IsTS);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//LONG AS_SetPenAttr(LONG Type, LONG Color, LONG Width)
	o.AS_SetPenAttr = function(Type,Color,Width,cb,ctx){
		var ret = ASAppCom.AS_SetPenAttr(Type,Color,Width);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//LONG AS_SetEvidenceCollectionModel(LONG Modal)
	o.AS_SetEvidenceCollectionModel = function(Modal,cb,ctx){
		var ret = ASAppCom.AS_SetEvidenceCollectionModel(Modal);
		return $myOKRtnFunc(ret, cb, ctx);
	};
	
	//AS_OpenIDCardDlg()
	o.AS_OpenIDCardDlg = function(cb, ctx) {
		var ret = ASAppCom.AS_OpenIDCardDlg();
		return $myOKRtnFunc(ret, cb, ctx);
	};	
	
	return o;
}

//DCDSCom class
function CreateDCDSComObject() {	
	var bOK = $LoadControl("D3C5BDC4-CE65-48D8-8DE0-C3DB1DF84962", "DCDSCom", "get_version()", false);
	if (!bOK) {
		return null;
	}	
	
	var o = new Object();
	
    
	return o;
}

//DSPSCom class
function CreateDSPSComObject() {
	var bOK = $LoadControl("AE1F34E2-6389-4AB4-B298-B7BB3CF68010", "DSPSCom", "get_version()", false);
	if (!bOK) {
		return null;
	}	
		
	var o = new Object();
	
	
	
	return o;
}

function consoleLog(str){
	var time = new Date();  
	var t = time.toTimeString() + "---" + time.getMilliseconds();
	console.log( t + " [" + str + "]");
}

//webSocket client class
function CreateASWebSocketObject(){
	
	try{
		var o = new Object();
		
		var config = {};
		var config_index = 0;
		//var socket;
		consoleLog("webSocket start");
		o.socket = new WebSocket("ws://127.0.0.1:22001/anysign2");
		consoleLog("webSocket end");
		// websocket exception is async, so output error to a function;
		// some error could not get error message,see this:https://www.w3.org/TR/websockets/#concept-websocket-close-fail
		o.socket.onerror = function(e) {
			// 低版本IE不支持日志，需要调试时自行打开
			console.error(e);
			console.log("create websocket failed, error = " + e.data)
		};
		
		o.socket.onclose = function(event) {
			 // See http://tools.ietf.org/html/rfc6455#section-7.4.1
			if (event.code == 1000)
				reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
			else if(event.code == 1001)
				reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
			else if(event.code == 1002)
				reason = "An endpoint is terminating the connection due to a protocol error";
			else if(event.code == 1003)
				reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
			else if(event.code == 1004)
				reason = "Reserved. The specific meaning might be defined in the future.";
			else if(event.code == 1005)
				reason = "No status code was actually present.";
			else if(event.code == 1006)
			   reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
			else if(event.code == 1007)
				reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
			else if(event.code == 1008)
				reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
			else if(event.code == 1009)
			   reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
			else if(event.code == 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
				reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: " + event.reason;
			else if(event.code == 1011)
				reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
			else if(event.code == 1015)
				reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
			else
				reason = "Unknown reason";
			// 低版本IE不支持日志，需要调试时自行打开
			console.log('websocket disconnect: ' + event.code + ' ' + reason + ' ' + event.wasClean);
			console.log('websocket disconnect: reason = ' + reason );
		};

		o.socket.onopen=function(){
			// 如果需要websocket连接后就立即对websocket进行的调用，可以放到这里。不然可能会出现已经调用，但是websocket还未创建成功的情况
			consoleLog('websocket opened')
		}

		o.socket.onmessage = function(evt) { 
		
			try{
				if(evt.data == "undefined" || !evt.data )
					return;
				var res = JSON.parse(evt.data);  
				// 低版本IE不支持日志，需要调试时自行打开
				console.log('server return data = ' + res);
				var call_cmd_id = res['call_cmd_id'];
				if(!call_cmd_id)
				{
				   return;
				}
			
				var callback = config[call_cmd_id];
				if(callback){
					callback(res);
				}
			
				delete config[call_cmd_id];
			}
			catch(err){
				console.log("onmessage ,error = " + err.message + ", json = " + evt.data);
			}
		}

		o.callMethod = function(strMethodName,callback,argsArray) {
			config_index++;
			if(callback){
				config[config_index]=callback;
			}

			var sendArray = {};
			sendArray['xtx_func_name'] = strMethodName;
			sendArray['call_cmd_id'] = config_index;

			if (arguments.length > 1) {
				sendArray["param"] = argsArray;
			}
			
			//添加状态判断，当为OPEN时，发送消息
			//CONNECTING：值为0，表示正在连接；
			//OPEN：值为1，表示连接成功，可以通信了；
			//CLOSING：值为2，表示连接正在关闭；
			//CLOSED：值为3，表示连接已经关闭，或者打开连接失败。
			if (o.socket.readyState===1) {
				o.socket.send(JSON.stringify(sendArray))
			}else{
				consoleLog("websocket stat is : " + o.socket.readyState)
			}
		}

		//AS_InitSign(LONG signType);
		o.AS_InitSign = function(signTyp, callback){
			consoleLog("AS_InitSign");
			o.callMethod("AS_InitSign",callback,[signTyp,"LONG"]);
		}

		//AS_SetBusinessParam(LONG ParamType, BSTR Param);
		o.AS_SetBusinessParam = function(ParamType, Param, callback) {
			o.callMethod("AS_SetBusinessParam",callback,[ParamType,"LONG",Param,"BSTR"]);
		};
		
		//AS_SetSignerInfo(BSTR name, BSTR IDType, BSTR ID);
		o.AS_SetSignerInfo = function(name, IDType, ID, callback) {
			o.callMethod("AS_SetSignerInfo",callback,[name,"BSTR",IDType,"BSTR",ID,"BSTR"]);
		};
		
		//AS_SetSignEvidenceData(BSTR dataJson);
		o.AS_SetSignEvidenceData = function(dataJson, callback) {
			o.callMethod("AS_SetSignEvidenceData",callback,[dataJson,"BSTR"]);
		};
		
		//AS_AddSignEvidenceData();
		o.AS_AddSignEvidenceData = function(callback) {
			o.callMethod("AS_AddSignEvidenceData",callback);
		};
		
		//AS_TakePhoto();
		o.AS_TakePhoto = function(callback) {
			o.callMethod("AS_TakePhoto",callback);
		};
		
		//AS_GetSignGUID();
		o.AS_GetSignGUID = function(callback) {
			o.callMethod("AS_GetSignGUID",callback);
		};
		
		//AS_AddMulWord(BSTR content, LONG megerNum)
		o.AS_AddMulWord = function(content, megerNum, callback) {
			o.callMethod("AS_AddMulWord",callback,[content,"BSTR",megerNum,"LONG"]);
		};
		
		//AS_GetSignEvidenceData(LONG dataType)
		o.AS_GetSignEvidenceData = function(dataType, callback) {
			o.callMethod("AS_GetSignEvidenceData",callback,[dataType,"LONG"]);
		};
		
		//AS_DeleteSign(BSTR SignGUID)
		o.AS_DeleteSign = function(SignGUID, callback) {
			o.callMethod("AS_DeleteSign",callback,[SignGUID,"BSTR"]);
		};

		//AS_ReusedSign(BSTR SignGUID)
		o.AS_ReusedSign = function(SignGUID, callback) {
			o.callMethod("AS_ReusedSign",callback,[SignGUID,"BSTR"]);
		};
		
		//AS_SetTimeout(LONG nTimeout)
		o.AS_SetTimeout = function(nTimeout, callback) {
			o.callMethod("AS_SetTimeout",callback,[nTimeout,"LONG"]);
		};

		//AS_SetDlgPos(LONG left, LONG top)
		o.AS_SetDlgPos = function(left, top, callback) {
			o.callMethod("AS_SetDlgPos",callback,[left,"LONG",top,"LONG"]);
		};
		
		//AS_SetDlgSize(LONG width, LONG height)
		o.AS_SetDlgSize = function(width, height, callback) {
			o.callMethod("AS_SetDlgSize",callback,[width,"LONG",height,"LONG"]);
		};

		//AS_AddEvidenceHash(BSTR evidenceDataBase64, BSTR mimeType, BSTR bioType)
		o.AS_AddEvidenceHash = function(evidenceDataBase64, mimeType, bioType, callback) {
			o.callMethod("AS_AddEvidenceHash",callback,[evidenceDataBase64,"BSTR",mimeType,"BSTR",bioType,"BSTR"]);
		};
		
		//AS_GetBusinessString()
		o.AS_GetBusinessString = function(callback) {
			o.callMethod("AS_GetBusinessString",callback);
		};
		
		//AS_GetSignPackage(BSTR businessString)
		o.AS_GetSignPackage = function(businessString, callback) {
			o.callMethod("AS_GetSignPackage",callback,[businessString,"BSTR"]);
		};

		//AS_SetPdfGenerateData(LONG type, BSTR data, BSTR dataID)
		o.AS_SetPdfGenerateData = function(type, data, dataID, callback) {
			o.callMethod("AS_SetPdfGenerateData",callback,[type,"LONG",data,"BSTR",dataID,"BSTR"]);
		};	
		
		//AS_SetPosKW(BSTR kw, LONG kwIndex, LONG XOffset, LONG YOffset, LONG width, LONG height);
		o.AS_SetPosKW = function(kw, kwIndex, XOffset, YOffset, width, height, callback) {
			o.callMethod("AS_SetPosKW",callback,[kw,"BSTR",kwIndex,"LONG",XOffset,"LONG",YOffset,"LONG",width,"LONG",height,"LONG"]);
		};	
		
		//AS_SetPosXY (LONG left, LONG bottom, LONG right, LONG top, LONG pageNo);
		o.AS_SetPosXY = function(left, bottom, right, top, pageNo, callback) {
			o.callMethod("AS_SetPosXY",callback,[left,"LONG",bottom,"LONG",right,"LONG",top,"LONG",pageNo,"LONG"]);
		};	
		
		//AS_SetPosRule (BSTR ruleNo);
		o.AS_SetPosRule = function(ruleNo, callback) {
			o.callMethod("AS_SetPosRule",callback,[ruleNo,"BSTR"]);
		};	
		
		//AS_AddUnitSign(BSTR imageBase, BSTR appName)
		o.AS_AddUnitSign = function(imageBase, appName, callback) {
			o.callMethod("AS_AddUnitSign",callback,[imageBase,"BSTR",appName,"BSTR"]);
		};	
		
		//AS_SetSignPlain(BSTR plainBase);
		o.AS_SetSignPlain = function(plainBase, callback) {
			o.callMethod("AS_SetSignPlain",callback,[plainBase,"BSTR"]);
		};	
		
		//AS_GetSignatureCount(BSTR signPackage)
		o.AS_GetSignatureCount = function(signPackage, callback) {
			o.callMethod("AS_GetSignatureCount",callback,[signPackage,"BSTR"]);
		};	
		
		//AS_GetIndexSignature(BSTR signPackage, LONG index)
		o.AS_GetIndexSignature = function(signPackage, index, callback) {
			o.callMethod("AS_GetIndexSignature",callback,[signPackage,"BSTR",index,"LONG"]);
		};		
		
		//AS_VerifySign(BSTR signValue, BSTR plainBase64)
		o.AS_VerifySign = function(signValue, plainBase64, callback) {
			o.callMethod("AS_VerifySign",callback,[signValue,"BSTR",plainBase64,"BSTR"]);
		};		
		
		//AS_VerifyTimeStamp(BSTR tsValue, BSTR tsPlainData)
		o.AS_VerifyTimeStamp = function(tsValue, tsPlainData, callback) {
			o.callMethod("AS_VerifyTimeStamp",callback,[tsValue,"BSTR",tsPlainData,"BSTR"]);
		};

		//AS_GetDataFromSignValue(BSTR signValue, BSTR plainBase64, LONG dataType)
		o.AS_GetDataFromSignValue = function(signValue, plainBase64, dataType, callback) {
			o.callMethod("AS_GetDataFromSignValue",callback,[signValue,"BSTR",plainBase64,"BSTR",dataType,"LONG"]);
		};		

		//AS_ConnectDev
		o.AS_ConnectDev = function(callback) {
			o.callMethod("AS_ConnectDev",callback);
		};	
		
		//AS_DisConnectDev();
		o.AS_DisConnectDev = function(callback) {
			o.callMethod("AS_DisConnectDev",callback);
		};	
		
		//AS_GetExtendScreenPos();
		o.AS_GetExtendScreenPos = function(callback) {
			o.callMethod("AS_GetExtendScreenPos",callback);
		};		
		
		//AS_ReadFile(BSTR base64FileName);
		o.AS_ReadFile = function(base64FileName, callback) {
			o.callMethod("AS_ReadFile",callback,[base64FileName,"BSTR"]);
		};		
		
		//AS_SaveFile(BSTR base64FileName, BSTR base64FileContent)
		o.AS_SaveFile = function(base64FileName, base64FileContent, callback) {
			o.callMethod("AS_SaveFile",callback,[base64FileName,"BSTR",base64FileContent,"BSTR"]);
		};		
		
		//AS_HashFile(BSTR hashFileName, LONG hashAlg)
		o.AS_HashFile = function(hashFileName, hashAlg, callback) {
			o.callMethod("AS_HashFile",callback,[hashFileName,"BSTR",hashAlg,"LONG"]);
		};

		//AS_HashData(BSTR plainData, LONG hashAlg)
		o.AS_HashData = function(plainData, hashAlg, callback) {
			o.callMethod("AS_HashData",callback,[plainData,"BSTR",hashAlg,"LONG"]);
		};	
		
		//AS_Base64Encode(BSTR strData, BSTR strType)
		o.AS_Base64Encode = function(strData, strType, callback) {
			o.callMethod("AS_Base64Encode",callback,[strData,"BSTR",strType,"BSTR"]);
		};		
		
		//AS_Base64Decode(BSTR strBase64)
		o.AS_Base64Decode = function(strBase64, callback) {
			o.callMethod("AS_Base64Decode",callback,[strBase64,"BSTR"]);
		};		
		
		//AS_ResizeImage
		o.AS_ResizeImage = function(strBase64, imgW, imgType, callback) {
			o.callMethod("AS_ResizeImage",callback,[strBase64,"BSTR",imgW,"LONG",imgType,"LONG"]);
		};		
		
		o.AS_GetVersion = function(callback) {
			o.callMethod("AS_GetVersion",callback);
		};	
		
		//AS_GetLastError();
		o.AS_GetLastError = function(callback) {
			o.callMethod("AS_GetLastError",callback);
		};		
		
		//AS_MoveTo(LONG dlg_left, LONG dlg_top, LONG dlg_width, LONG dlg_height)
		o.AS_MoveTo = function(dlg_left, dlg_top, dlg_width, dlg_height, callback) {
			o.callMethod("AS_MoveTo",callback,[dlg_left,"LONG",dlg_top,"LONG",dlg_width,"LONG",dlg_height,"LONG"]);
		};	

	    o.AS_MoveByTitle = function(title, left, top, width, height, callback) {
	    	o.callMethod("AS_MoveByTitle",callback,[title, "BSTR", left,"LONG",top,"LONG",width,"LONG",height,"LONG"]);
	    };	
	    
	    o.AS_MoveToMonitorByTitle = function(title, monitorId, callback) {
	    	o.callMethod("AS_MoveToMonitorByTitle",callback,[title, "BSTR", monitorId,"LONG"]);
	    };			
		
		//AS_SetConfig(BSTR strKey, BSTR strValue)
		o.AS_SetConfig = function(strKey, strValue, callback) {
			o.callMethod("AS_SetConfig",callback,[strKey,"BSTR",strValue,"BSTR"]);
		};
		
		//AS_ShowFile(BSTR fileData, LONG fileType)
		o.AS_ShowFile = function(fileData, fileType, callback) {
			o.callMethod("AS_ShowFile",callback,[fileData,"BSTR",fileType,"LONG"]);
		};
		
		//AS_EndShowFile()
		o.AS_EndShowFile = function(callback) {
			o.callMethod("AS_EndShowFile",callback);
		};
		
		//AS_ShowHtml(BSTR htmlSrc, BSTR jsSrc)
		o.AS_ShowHtml = function(htmlSrc, jsSrc, callback) {
			o.callMethod("AS_ShowHtml",callback,[htmlSrc,"BSTR",jsSrc,"BSTR"]);
		};
		
		//AS_GetIDCardInfo()
		o.AS_GetIDCardInfo = function(callback) {
			o.callMethod("AS_GetIDCardInfo",callback);
		};
		
		//AS_PdfSign()
		o.AS_PdfSign = function(callback) {
			o.callMethod("AS_PdfSign",callback);
		};
		
		//AS_SetServerIP(BSTR mainIP,LONG mainPort,BSTR spareIP,LONG sparePort,LONG timeout)
		o.AS_SetServerIP = function(mainIP,mainPort,spareIP,sparePort,timeout,callback)
		{
			o.callMethod("AS_SetServerIP",callback,[mainIP,"BSTR",mainPort,"LONG",spareIP,"BSTR",sparePort,"LONG",timeout,"LONG"]);
		};
		
		//AS_SetHWRModel(LONG RecognizeNum)
		o.AS_SetHWRModel = function(RecognizeNum,callback)
		{
			o.callMethod("AS_SetHWRModel",callback,[RecognizeNum,"LONG"]);
		};
		
		//AS_GetHWRResult()
		o.AS_GetHWRResult = function(callback) {
			o.callMethod("AS_GetHWRResult",callback);
		};
		
		//AS_SetSignAlg(LONG SignAlg, BOOL IsTS)
		o.AS_SetSignAlg = function(SignAlg, IsTS, callback) {
			o.callMethod("AS_SetSignAlg",callback,[SignAlg,"LONG",IsTS,"BOOL"]);
		};
		
		//AS_SetPenAttr(LONG Type, LONG Color, LONG Width)
		o.AS_SetPenAttr = function(Type, Color, Width, callback) {
			o.callMethod("AS_SetPenAttr",callback,[Type,"LONG",Color,"LONG",Width,"LONG"]);
		};
		
		
		//AS_SetEvidenceCollectionModel(LONG Modal)
		o.AS_SetEvidenceCollectionModel = function(Modal, callback) {
			o.callMethod("AS_SetEvidenceCollectionModel",callback,[Modal,"LONG"]);
		};
		
		//AS_OpenIDCardDlg()
		o.AS_OpenIDCardDlg = function(callback) {
			o.callMethod("AS_OpenIDCardDlg",callback);
		};
		//AS_SetWindowHWND(LONG hWnd);
		//o.AS_SetWindowHWND = function(hWnd, callback) {
		//	o.callMethod("AS_SetWindowHWND",callback,[hWnd,"LONG"]);
		//};
	}
	catch(err){
		console.log("create websocket failed ,error = " + err.message);
	}
	return o;
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////// 对外提供的接口

function $myErrorRtnFunc(retVal, cb, ctx)
{
    if (typeof cb == 'function') {
        var retObj = {retVal:retVal, ctx:ctx};
        cb(retObj);
    }
    
    return retVal;
}

//AS_InitSign
function AS_InitSign(signType, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_InitSign(signType, cb, ctx);
	} else {
        return $myErrorRtnFunc("-1", cb, ctx);
	}
}

//AS_SetBusinessParam
function AS_SetBusinessParam(ParamType, Param, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetBusinessParam(ParamType, Param, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetSignerInfo
function AS_SetSignerInfo(name, IDType, ID, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetSignerInfo(name, IDType, ID, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetSignEvidenceData
function AS_SetSignEvidenceData(dataJson, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetSignEvidenceData(dataJson, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_AddSignEvidenceData();
function AS_AddSignEvidenceData(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_AddSignEvidenceData(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_TakePhoto();
function AS_TakePhoto(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_TakePhoto(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetSignGUID();
function AS_GetSignGUID(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetSignGUID(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_AddMulWord(BSTR content, LONG megerNum);
function AS_AddMulWord(content, megerNum, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_AddMulWord(content, megerNum, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetSignEvidenceData(LONG dataType);
function AS_GetSignEvidenceData(dataType, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetSignEvidenceData(dataType, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_DeleteSign(BSTR SignGUID)
function AS_DeleteSign(SignGUID, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_DeleteSign(SignGUID, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_ReusedSign(BSTR SignGUID);
function AS_ReusedSign(SignGUID, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_ReusedSign(SignGUID, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetTimeout(LONG nTimeout);
function AS_SetTimeout(nTimeout, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetTimeout(nTimeout, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetDlgPos(LONG left, LONG top);
function AS_SetDlgPos(left, top1 , cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetDlgPos(left, top1, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetDlgSize(LONG width, LONG height);
function AS_SetDlgSize(width, height , cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetDlgSize(width, height, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_AddEvidenceHash(BSTR evidenceDataBase64, BSTR mimeType, BSTR bioType)
function AS_AddEvidenceHash(evidenceDataBase64, mimeType,  bioType, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_AddEvidenceHash(evidenceDataBase64, mimeType,  bioType, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetBusinessString();
function AS_GetBusinessString(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetBusinessString(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetSignPackage(BSTR);
function AS_GetSignPackage(businessString, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetSignPackage(businessString, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetPdfGenerateData(LONG type, BSTR data, BSTR dataID)
function AS_SetPdfGenerateData(type, data, dataID, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetPdfGenerateData(type, data, dataID, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetPosKW(BSTR kw, LONG kwIndex, LONG XOffset, LONG YOffset, LONG width, LONG height)
function AS_SetPosKW(kw, kwIndex, XOffset, YOffset, width, height, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetPosKW(kw, kwIndex, XOffset, YOffset, width, height, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetPosXY (LONG left, LONG bottom, LONG right, LONG top, LONG pageNo);
function AS_SetPosXY(left, bottom, right, top, pageNo, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetPosXY(left, bottom, right, top, pageNo, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetPosRule (BSTR ruleNo)
function AS_SetPosRule(ruleNo, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetPosRule(ruleNo, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_AddUnitSign(BSTR imageBase, BSTR appName)
function AS_AddUnitSign(imageBase, appName, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_AddUnitSign(imageBase, appName, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetSignPlain(BSTR plainBase)
function AS_SetSignPlain(plainBase, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetSignPlain(plainBase, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetSignatureCount(BSTR signPackage)
function AS_GetSignatureCount(signPackage, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetSignatureCount(signPackage, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetIndexSignature(BSTR signPackage, LONG index)
function AS_GetIndexSignature(signPackage, index, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetIndexSignature(signPackage, index, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_VerifySign(BSTR signValue, BSTR plainBase64)
function AS_VerifySign(signValue, plainBase64, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_VerifySign(signValue, plainBase64, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_VerifyTimeStamp(BSTR tsValue, BSTR tsPlainData)
function AS_VerifyTimeStamp(tsValue, tsPlainData, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_VerifyTimeStamp(tsValue, tsPlainData, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetDataFromSignValue(BSTR signValue, BSTR plainBase64, LONG dataType)
function AS_GetDataFromSignValue(signValue, plainBase64, dataType, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetDataFromSignValue(signValue, plainBase64, dataType, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_ConnectDev();
function AS_ConnectDev(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_ConnectDev(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_DisConnectDev();
function AS_DisConnectDev(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_DisConnectDev(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetExtendScreenPos();
function AS_GetExtendScreenPos(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetExtendScreenPos(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_ReadFile(LPTSTR base64FileName)
function AS_ReadFile(base64FileName, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_ReadFile(base64FileName, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SaveFile(BSTR base64FileName, BSTR base64FileContent);
function AS_SaveFile(base64FileName, base64FileContent, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SaveFile(base64FileName, base64FileContent, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_HashFile(LPTSTR hashFileName, LONG hashAlg)
function AS_HashFile(hashFileName, hashAlg, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_HashFile(hashFileName, hashAlg, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_HashData(LPTSTR plainData, LONG hashAlg)
function AS_HashData(plainData, hashAlg, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_HashData(plainData, hashAlg, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_Base64Encode(LPCTSTR strData, LPCTSTR strType)
function AS_Base64Encode(strData, strType, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_Base64Encode(strData, strType, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_Base64Decode(LPCTSTR strBase64)
function AS_Base64Decode(strBase64, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_Base64Decode(strBase64, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_ResizeImage(strBase64,imgW,imgType)
function AS_ResizeImage(strBase64,imgW,imgType,cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_ResizeImage(strBase64,imgW,imgType,cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetVersion()
function AS_GetVersion(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetVersion(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetLastError();
function AS_GetLastError(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetLastError(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_MoveTo(LONG dlg_left, LONG dlg_top, LONG dlg_width, LONG dlg_height)
function AS_MoveTo(dlg_left, dlg_top, dlg_width, dlg_height, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_MoveTo(dlg_left, dlg_top, dlg_width, dlg_height, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

function AS_MoveByTitle(title, left, top, width, height, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_MoveByTitle(title, left, top, width, height, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

function AS_MoveToMonitorByTitle(title, monitorId, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_MoveToMonitorByTitle(title, monitorId, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetConfig(BSTR strKey, BSTR strValue)
function AS_SetConfig(strKey, strValue, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_SetConfig(strKey, strValue, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_ShowFile(BSTR fileData, LONG fileType)
function AS_ShowFile(fileData, fileType, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_ShowFile(fileData, fileType, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_EndShowFile()
function AS_EndShowFile(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_EndShowFile(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_ShowHtml(BSTR htmlSrc, BSTR jsSrc)
function AS_ShowHtml(htmlSrc, jsSrc, cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_ShowHtml(htmlSrc, jsSrc, cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_GetIDCardInfo()
function AS_GetIDCardInfo(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_GetIDCardInfo(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_PdfSign()
function AS_PdfSign(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_PdfSign(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

//AS_SetServerIP(BSTR mainIP,LONG mainPort,BSTR spareIP,LONG sparePort,LONG timeout)
function AS_SetServerIP(mainIP,mainPort,spareIP,sparePort,timeout,cb,ctx)
{
	if($_$ASCurrentObj != null){
		return $_$ASCurrentObj.AS_SetServerIP(mainIP,mainPort,spareIP,sparePort,timeout,cb,ctx);
	}else {
		return $myErrorRtnFunc("",cb,ctx);
	}
}

//AS_SetHWRModel(LONG RecognizeNum)
function AS_SetHWRModel(RecognizeNum,cb,ctx)
{
	if($_$ASCurrentObj != null){
		return $_$ASCurrentObj.AS_SetHWRModel(RecognizeNum,cb,ctx);
	}else {
		return $myErrorRtnFunc("",cb,ctx);
	}
}

//AS_SetSignAlg(LONG SignAlg, BOOL IsTS)
function AS_SetSignAlg(SignAlg,IsTS,cb,ctx)
{
	if($_$ASCurrentObj != null){
		return $_$ASCurrentObj.AS_SetSignAlg(SignAlg,IsTS,cb,ctx);
	}else {
		return $myErrorRtnFunc("",cb,ctx);
	}
}

//AS_GetHWRResult()
function AS_GetHWRResult(cb,ctx)
{
	if($_$ASCurrentObj != null){
		return $_$ASCurrentObj.AS_GetHWRResult(cb,ctx);
	}else {
		return $myErrorRtnFunc("",cb,ctx);
	}
}

//AS_SetPenAttr(LONG Type, LONG Color, LONG Width)
function AS_SetPenAttr(Type,Color,Width,cb,ctx)
{
	if($_$ASCurrentObj != null){
		return $_$ASCurrentObj.AS_SetPenAttr(Type,Color,Width,cb,ctx);
	}else {
		return $myErrorRtnFunc("",cb,ctx);
	}
}

//AS_SetEvidenceCollectionModel(LONG Modal)
function AS_SetEvidenceCollectionModel(Modal,cb,ctx)
{
	if($_$ASCurrentObj != null){
		return $_$ASCurrentObj.AS_SetEvidenceCollectionModel(Modal,cb,ctx);
	}else {
		return $myErrorRtnFunc("",cb,ctx);
	}
}

//AS_OpenIDCardDlg()
function AS_OpenIDCardDlg(cb, ctx) {
	if ($_$ASCurrentObj != null) {
		return $_$ASCurrentObj.AS_OpenIDCardDlg(cb, ctx);
	} else {
        return $myErrorRtnFunc("", cb, ctx);
	}
}

	function GetErrorMessage(ErrorCode)
    {
		var errcode;
		var Message = "";
		if(typeof(ErrorCode) == 'string')
		{
			errcode = parseInt(ErrorCode);	
		}else
			errcode = ErrorCode;	
		
		switch (errcode)
		{
			case 0:
				Message = "成功";
				break;
			case 16777217:
				Message = "传入的参数为空值";
				break;
			case 16777218:
				Message = "生成guid出错";
				break;
			case 16777219:
				Message = "找不到guid";
				break;
			case 16777220:
				Message = "暂时还没有成功的签名，guid为空";
				break;
			case 16777221:
				Message = "没有设置工单号";
				break;
			case 16777222:
				Message = "没有设置渠道号";
				break;
			case 16777223:
				Message = "没有设置生成pdf的数据";
				break;
			case 16777224:
				Message = "外设签名信息，数据包中没有签名信息。";
				break;
			case 16777225:
				Message = "外设签名信息，数据包中签名信息太多，超过了一张。";
				break;
			case 16777226:
				Message = "json对象不是string类型";
				break;
			case 16777227:
				Message = "签名类型出错，0为pdf签名，1位数据签名，用户输入了错误的类型";
				break;
			case 16777228:
				Message = "签名规则为空";
				break;
			case 16777229:
				Message = "设置业务类型出错";
				break;
			case 16777230:
				Message = "设置超时时长小于等于0";
				break;
			case 16777231:
				Message = "设置签名坐标错误";
				break;
			case 16777232:
				Message = "签名证据数据为空";
				break;
			case 16777233:
				Message = "输入的证据哈希为空";
				break;
			case 16777234:
				Message = "输入的证据哈希对应的原文mime类型为空";
				break;
			case 16777235:
				Message = "输入的证据哈希对应的原文mime类型不支持";
				break;
			case 16777236:
				Message = "输入的证据类型为空";
				break;
			case 16777237:
				Message = "手写图片格式不对";
				break;
			case 16777238:
				Message = "手写签名图片内容为空";
				break;
			case 16777239:
				Message = "批注图片格式不对";
				break;
			case 16777240:
				Message = "批注图片错误";
				break;
			case 16777241:
				Message = "指纹图片格式错误";
				break;
			case 16777242:
				Message = "指纹图片内容为空";
				break;
			case 16777243:
				Message = "拍照图片格式错误";
				break;
			case 16777244:
				Message = "照片为空";
				break;
			case 16777245:
				Message = "视频为空";
				break;
			case 16777246:
				Message = "音频为空";
				break;
			case 16777247:
				Message = "解压缩手写轨迹时失败";
				break;
			case 16777248:
				Message = "获取手写签名图片失败";
				break;
			case 16777249:
				Message = "获取指纹图片失败";
				break;
			case 16777250:
				Message = "获取手写和指纹相关信息失败";
				break;
			case 16777251:
				Message = "获取照片证据失败";
				break;
			case 16777252:
				Message = "获取音频证据失败";
				break;
			case 16777253:
				Message = "获取视频证据失败";
				break;
			case 16777254:
				Message = "手写签名轨迹数据为空";
				break;
			case 16777255:
				Message = "批注内容为空";
				break;
			case 16777256:
				Message = "批注合并数目错误";
				break;
			case 16777257:
				Message = "批注图片为空";
				break;
			case 16777258:
				Message = "设置批注坐标错误";
				break;
			case 16777259:
				Message = "获取采集到的批注图片失败";
				break;
			case 16777260:
				Message = "没有添加手写签名";
				break;
			case 16777261:
				Message = "加密包为空";
				break;
			case 16777262:
				Message = "签名值中的数据类型出错";
				break;
			case 16777263:
				Message = "签名值为空";
				break;
			case 16777264:
				Message = "压缩数据为空";
				break;
			case 16777265:
				Message = "内部证据数据为空";
				break;
			case 16777266:
				Message = "用户取消签名";
				break;
			case 16777267:
				Message = "显示签名对话框失败";
				break;
			case 16777268:
				Message = "获取加密包数据失败";
				break;
			case 16777269:
				Message = "输入文件路径错误";
				break;
			case 16777270:
				Message = "base64解码失败";
				break;
			case 16777271:
				Message = "读取文件失败";
				break;
			case 16777272:
				Message = "输入的数据内容为空";
				break;
			case 16777273:
				Message = "输入的数据长度为0";
				break;
			case 16777274:
				Message = "没有输入签名人姓名";
				break;
			case 16777275:
				Message = "没有输入签名人的证件类型";
				break;
			case 16777276:
				Message = "没有输入签名人的证件号码";
				break;
			case 16777277:
				Message = "输入的证据MimeType不支持";
				break;
			case 16777278:
				Message = "没有输入证据数据";
				break;
			case 16777279:
				Message = "用户操作超时";
				break;
			case 16777280:
				Message = "获取扩展屏坐标失败";
				break;
			case 16777281:
				Message = "渠道号长度超过限制，最多20个字符";
				break;
			case 16777282:
				Message = "渠道号必须由数字组成";
				break;
			case 16777283:
				Message = "签名控件窗口未能创建，非IE浏览器不支持此接口";
				break;
			case 16777284:
				Message = "UTF-8转换到Ansi失败";
				break;
			case 16777285:
				Message = "不支持的编码类型";
				break;
			case 16777286:
				Message = "不支持的哈希算法";
				break;
			case 16777287:
				Message = "输入签名文件名为空";
				break;
			case 16777288:
				Message = "输入文件内容为空";
				break;
			case 16777289:
				Message = "输入文件内容长度为0";
				break;
			case 16777290:
				Message = "输入的路径名不合法";
				break;
			case 16777291:
				Message = "输入的路径不存在";
				break;
			case 16777292:
				Message = "写文件出现错误";
				break;
			case 16777293:
				Message = "目录不具有写权限";
				break;
			case 16777294:
				Message = "打开摄像头失败";
				break;
			case 16777295:
				Message = "拍照失败";
				break;
			case 16777296:
				Message = "访问照片存储路径失败";
				break;
			case 16777297:
				Message = "访问音频存储路径失败";
				break;
			case 16777298:
				Message = "访问视频存储路径失败";
				break;
			case 16777299:
				Message = "打开Mic失败，请查看设备是否正常连接";
				break;
			case 16777300:
				Message = "没有安装相应的视屏编码器";
				break;
			case 16777301:
				Message = "输入的工单号为空";
				break;
			case 16777302:
				Message = "输入的工单号信息无内容";
				break;
			case 16777303:
				Message = "输入的渠道号为空";
				break;
			case 16777304:
				Message = "输入的渠道号信息无内容";
				break;
			case 16777305:
				Message = "输入的关键字为空";
				break;
			case 16777306:
				Message = "输入的关键字信息无内容";
				break;
			case 16777307:
				Message = "输入了错误的PDF数据类型";
				break;
			case 16777308:
				Message = "输入的PDF数据为空";
				break;
			case 16777309:
				Message = "签名笔迹轨迹为空";
				break;
			case 16777310:
				Message = "输入的服务器应用名为空";
				break;
			case 16777311:
				Message = "输入的单位签章图片为空";
				break;
			case 16777312:
				Message = "设置的对话框窗口无效，已超出屏幕范围";
				break;
			case 16777313:
				Message = "加载对话框界面出现错误";
				break;
			case 16777314:
				Message = "关键字长度超出限制";
				break;	
			case 16777315:
				Message = "拍照路径不可写";
				break;	
			case 16777316:
				Message = "录音路径不可写";
				break;
			case 16777317:
				Message = "录像路径不可写";
				break;
			case 16777318:
				Message = "设置的配置项参数为空";
				break;
			case 16777319:
				Message = "不支持的修改的配置项";
				break;
			case 16777320:
				Message = "获取操作系统信息失败";
				break;
			case 16777321:
				Message = "计算哈希值失败";
				break;
			case 16777322:
				Message = "释放签名或批注窗口失败";
				break;
			case 16777323:
				Message = "连接手写签名设备失败";
				break;
			case 16777324:
				Message = "断开手写签名设备失败";
				break;	
			case 16777325:
				Message = "获取手写签名设备状态失败";
				break;	
			case 16777326:
				Message = "获取指纹仪设备状态失败";
				break;
			case 16777327:
				Message = "设置窗口句柄失败";
				break;
			case 16777328:
				Message = "数据的json为空";
				break;
			case 16777329:
				Message = "输入的json解析失败";
				break;
			case 16777330:
				Message = "获取签名值失败";
				break;
			case 16777331:
				Message = "申请证书失败";
				break;
			case 16777332:
				Message = "解析BioFeature失败";
				break;
			case 16777333:
				Message = "输入的签名包为空";
				break;
			case 16777334:
				Message = "输入的原文为空";
				break;	
			case 16777335:
				Message = "解析输入的签名包数据格式错误";
				break;	
			case 16777336:
				Message = "签名值验证失败";
				break;
			case 16777337:
				Message = "获取证书内容数据失败";
				break;
			case 16777338:
				Message = "签名包中EventCert为空";
				break;
			case 16777339:
				Message = "签名包中TSValue为空";
				break;
			case 16777340:
				Message = "输入的签名算法不是有效参数";
				break;
			case 16777341:
				Message = "获取签名包数据中指纹图片为空";
				break;
			case 16777342:
				Message = "获取签名包手写图片为空";
				break;
			case 16777343:
				Message = "Biofeatur哈希值为空";
				break;
			case 16777344:
				Message = "获取证书扩展项失败";
				break;	
			case 16777345:
				Message = "比较证书BIO_HASH失败";
				break;	
			case 16777346:
				Message = "获取证书签名人失败";
				break;
			case 16777347:
				Message = "证件类型不正确";
				break;
			case 16777348:
				Message = "解析后的json不是json object";
				break;
			case 16777349:
				Message = "输入的文件太大，无法签名或者验证。";
				break;
			case 16777350:
				Message = "输入的时间戳签名值为空";
				break;
			case 16777351:
				Message = "输入的时间戳原文为空";
				break;
			case 16777352:
				Message = "格式化时间戳时间失败";
				break;
			case 16777353:
				Message = "验证时间戳签名失败";
				break;
			case 16777354:
				Message = "验证证书有效性失败（三级根配置不正确也会导致此错误）";
				break;	
			case 16777355:
				Message = "获取时间戳原文为空";
				break;	
			case 16777356:
				Message = "解析证书中的哈希值失败";
				break;
			case 16777357:
				Message = "比较证书中的哈希与手写笔迹哈希失败";
				break;
			case 16777358:
				Message = "指纹和照片必须存在一种";
				break;
			case 16777359:
				Message = "设置任意内容签字时，初始界面显示行数错误，取值范围在1~10之间 ";
				break;
			case 16777360:
				Message = "获取签名包数据中拍照图片为空";
				break;
			case 16777361:
				Message = "手写签名设备已连接，不能重复连接";
				break;
			case 16777362:
				Message = "对图像进行缩放发生错误";
				break;
			case 16777363:
				Message = "对图像进行格式转换发生错误";
				break;
			case 16777364:
				Message = "获取签名包数据中批注数据为空";
				break;
			case 16777365:
				Message = "获取证书ID失败";
				break;
			case 16777366:
				Message = "本次签名证据hash信息不正确";
				break;
			case 16777367:
				Message = "外设手写轨迹格式错误";
				break;
			case 16777368:
				Message = "不支持的外设证据类型";
				break;
			case 16777369:
				Message = "签名图片宽高设置错误";
				break;
			case 16777370:
				Message = "输入的索引号错误，签名包中找不到对应的签名值";
				break;	
			case 16777371:
			    Message = "输入的加密包格式错误";
				break;	
			case 16777372:
				Message = "加密包中的签名超过数量";
				break;	
			case 16777373:
				Message = "加密包中的批注超过数量";
				break;	
			case 16777374:
			    Message = "身份证信息为空";
				break;	
			case 16777375:
				Message = "答题信息为空";
				break;	
			case 16777376:
			    Message = "设置签名框大小";	
				break;	
			case 16777377:
				Message = "工单号超过最大长度";
				break;	
			case 16777378:
			    Message = "签名人姓名超过最大长度";	
				break;	
			case 16777379:
				Message = "证件号码超过最大长度";
				break;	
			case 16777380:
			    Message = "外设签名证据数据超过最大长度";
				break;					
			case 16777381:
				Message = "批注内容数据超过最大长度";
				break;	
			case 16777382:
			    Message = "获取签名证据类型错误";	
				break;	
			case 16777383:
				Message = "guid超过最大长度";
				break;	
			case 16777384:
			    Message = "添加的证据哈希超过最大长度";	
				break;	
			case 16777385:
				Message = "添加的证据哈希MIMETYPE超过最大长度";
				break;	
			case 16777386:
			    Message = "添加的证据哈希BIOTYPE超过最大长度";
				break;					
			case 16777387:
				Message = "加密包超过最大长度";
				break;	
			case 16777388:
			    Message = "PDF生成数据的模板号超过最大长度";
				break;					
			case 16777389:
				Message = "单位签章图片超过最大长度";
				break;	
			case 16777390:
			    Message = "服务器应用名超过最大长度";	
				break;	
			case 16777391:
				Message = "签名包超过最大长度";
				break;	
			case 16777392:
			    Message = "时间戳签名值超过最大长度";	
				break;	
			case 16777393:
				Message = "时间戳原文超过最大长度";
				break;	
			case 16777394:
			    Message = "输入文件路径超过最大长度";
				break;	
			case 16777395:
				Message = "设置窗口大小错误";
				break;	
			case 16777396:
			    Message = "配置项数据超过最大长度";
				break;	
			case 16777397:
				Message = "展示文件类型错误";
				break;	
			case 16777398:
			    Message = "图片宽度设置错误，要大于0";				
				break;
			case 16777399:
				Message = "设置服务器地址失败";				
				break;
			case 16777400:
				Message = "创建pdfcom组件失败";				
				break;
			case 16777401:
				Message = "获取P7数据失败";				
				break;
			case 16777402:
				Message = "pdf签名合章失败";				
				break;
			case 16777403:
				Message = "pdf签名获取hash失败";				
				break;
			case 16777404:
				Message = "写临时文件失败";				
				break;
			case 16777405:
				Message = "pdf签名获取hash失败";				
				break;
			case 16777406:
				Message = "手写轨迹ocr识别模式设置失败";				
				break;
			case 16777407:
				Message = "获取手写轨迹文字结果失败";				
				break;
			case 16777408:
				Message = "启用ocr识别失败";				
				break;
			case 16777409:
				Message = "未找到要移动的窗口";				
				break;
			case 16777410:
				Message = "算法参数错误";				
				break;
			case 16777411:
				Message = "笔类型参数错误";				
				break;
			case 16777412:
				Message = "证据采集模式参数错误";				
				break;
			case 16777413:
				Message = "合规检查功能调用错误";				
				break;
			case 16777414:
				Message = "入参越界";				
				break;
			case 16777415:
				Message = "用户未同意签名前告知条款";				
				break;
			case 16785417:
				Message = "批注对话框超时";				
				break;
			case 16789764:
			    Message = "用户取消了签名";				
				break;
			case 16789774:
				Message = "ocr请求发送失败 ";				
				break;
			case 16789775:
				Message = "未启用ocr识别";	
				break;
			case 16789776:
				Message = "客户端输入签名人姓名与服务器返回的识别文字匹配失败";				
				break;
			case 16789777:
				Message = "ocr服务器返回错误";				
				break;
			case 16789778:
				Message = "服务器返回的业务号与客户端匹配失败";				
				break;	
            case 16789765:
				Message = "对话框超时";				
				break;				
			case 16789779:
				Message = "ocr识别次数设置错误";				
				break;
			case 16789780:
				Message = "获取到的识别文字为空";				
				break;
			case 16793601:
				Message = "没有检测到设备";				
				break;
			default:
				Message = "未知错误：";
				break;
		}
		return Message + " 错误码：" + ErrorCode;
    }

(function() {
	$_$ASAppObj = CreateASAppObject();
	if ($_$ASAppObj != null) {
		$_$ASCurrentObj = $_$ASAppObj;
		return;
	}
	
    $_$WebSocketObj = CreateASWebSocketObject();
    if ($_$WebSocketObj != null) {
		$_$ASCurrentObj = $_$WebSocketObj;
		return;
	}
    /*
	$_$DCDSObj = CreateDCDSComObject();
	if ($_$DCDSObj != null) {
		//$_$ASCurrentObj = $_$SecXV2Obj;
		return;
	}
	
	$_$DSPSObj = CreateDSPSComObject();
	if ($_$DSPSObj != null) {
		//$_$ASCurrentObj = $_$SecXObj;
		return;
	}
    */
    $_$ASCurrentObj = null;
	
	$ASAlert("检查信手书控件出错!");
	return;
})();

 