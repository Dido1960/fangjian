/*--------------------------------------------------------------------------  
 *
 * BJCA Adaptive Javascript
 * This script support bjca pdfseal client version 4.4.0.
 * Author:BJCA-zc
 *--------------------------------------------------------------------------*/

// 隐藏工具栏
function hide_toolbar() {
    var ret = PdfSealObj.Hide_ToolBar();
    if (ret != 0)
        alert("隐藏工具栏失败，错误码：" + ret);
}

// 显示工具栏
function show_toolbar() {
    var ret = PdfSealObj.Show_ToolBar();
    if (ret != 0)
        alert("显示工具栏失败，错误码：" + ret);
}

// 打开本地或在线pdf
function open_url_pdf() {
    var pageNum = document.getElementById('url').value;
    var ret = PdfSealObj.Pdf_Open_path(pageNum);
    if (ret != 0)
        alert("打开本地或在线pdf失败，错误码：" + ret);
}

// 选择文件打开pdf
function open_pdf() {
    var ret = PdfSealObj.Pdf_Open();
    if (ret != 0)
        alert("选择文件打开pdf失败，错误码：" + ret);
}

// 设置水印数据
function SetWaterMarkInfo() {
    var arr1 = {
        "IsShowWaterMark": true,
        "IsShowTime": true,
        "MarkRotate": 15,
        "FontSize": 30,
        "ShowData": "北京数宇认证股份有限公司",
        //"ShowTime": "2019年11月11日"
        "ShowTime": ""
    };

    var thresholdKeyJson = {
        "version": "1.0",
        "watermark": arr1
    };

    var rv = PdfSealObj.Pdf_SetWaterMarkInfo(JSON.stringify(thresholdKeyJson));
    if (rv != 0) {
        alert("设置水印数据失败，错误码：" + rv);
        return rv;
    }
}

// 保存文档到指定路径
function save_pdf() {
    var savePath = document.getElementById('savePath').value;
    var ret = PdfSealObj.Pdf_Save_Path(savePath);
    if (ret == 0)
        alert("保存成功");
    else
        alert("保存文档失败，错误码：" + ret);
}

// 另存为
function save_as() {
    var ret = PdfSealObj.Pdf_SaveAs();
    if (ret == 0)
        alert("保存成功");
    else
        alert("另存文档失败，错误码：" + ret);
}

// 保存pdf
function select_Path_Save() {
    var ret = PdfSealObj.Pdf_Save();
    if (ret == -1021)
		alert("文档未改变");
	else if (ret != 0)
        alert("保存pdf失败，错误码：" + ret);
}

// 放大pdf
function pdf_zoomin() {
    var ret = PdfSealObj.Pdf_ZoomIn();
    if (ret != 0)
        alert("放大pdf失败，错误码：" + ret);
}

// 缩小pdf
function pdf_zoomout() {
    var ret = PdfSealObj.Pdf_ZoomOut();
    if (ret != 0)
        alert("缩小pdf失败，错误码：" + ret);
}

// 上翻页
function pdf_pageup() {
    var ret = PdfSealObj.Pdf_PageUp();
    if (ret != 0)
        alert("上翻页失败，错误码：" + ret);
}

// 下翻页
function pdf_pagedown() {
    var ret = PdfSealObj.Pdf_PageDown();
    if (ret != 0)
        alert("下翻页失败，错误码：" + ret);
}

// 适配
function pdf_page_onsize() {
    var ret = PdfSealObj.Pdf_OnSize();
    if (ret != 0)
        alert("适配失败，错误码：" + ret);
}

// 跳页
function pdf_goto_page() {
    var ret = PdfSealObj.Pdf_PageOn(document.getElementById('pagenum').value);
    if (ret != 0)
        alert("跳页失败，错误码：" + ret);
}

// 签名
function pdf_sign() {
    var ret = PdfSealObj.Pdf_Sign();
    if (ret != 0)
        alert("签名失败，错误码：" + ret);
}

// 切换印章图片
function pdf_ChangeSeal() {
    var ret = PdfSealObj.Pdf_ChangeSeal();
	if (ret != 0)
        alert("切换印章图片：" + ret);
}

// 获取云印章图片列表
function pdf_GetXBYSealPicList() {
    var picList = PdfSealObj.Pdf_GetXBYSealPicList();
	//alert('picList->' + picList);	
	$('#cert_text').val(picList);
	var certList = picList.split('&&&');
	//alert(certList.length);
	$(".seal_img").empty();
	for (i=0; i<certList.length; i++) 
	{
		var imgList = certList[i].split('||');
		var imgBase64 = imgList[1];
		if(imgBase64 != '' && imgBase64 != undefined){
			_certid = imgList[3];
			_msspid = imgList[4];
			var img = "<img title='鼠标左键点击一下选择此印章' class='doQianzhang' style='width: 80px;cursor: pointer;' src='data:image/jpg;base64," + imgList[1] + "' imgBase64='" + imgList[1] + "' certid='" + _certid + "' msspid='" + _msspid + "'>";
			$(".seal_img").append(img);
		}
	}
}

// 获取key印章图片列表
function pdf_GetKeySealPicList() {
    var picList = PdfSealObj.Pdf_GetKeySealPicList();
	//alert(picList);	
	$('#cert_text').val(picList);
	var certList = picList.split('&&&');
	//alert(certList.length);
	$(".seal_img").empty();
	for (i=0; i<certList.length; i++) 
	{
		var imgList = certList[i].split('||');
		var imgBase64 = imgList[1];
		if(imgBase64 != '' && imgBase64 != undefined){
			_certid = imgList[3];
			_msspid = '';
			var img = "<img title='鼠标左键点击一下选择此印章' class='doQianzhang' style='width: 80px;cursor: pointer;' src='data:image/jpg;base64," + imgList[1] + "' imgBase64='" + imgList[1] + "' certid='" + _certid + "' msspid='" + _msspid + "'>";
			$(".seal_img").append(img);
		}
	}
}

// 重置当前选择的证书id
function pdf_ResetSelectUserCertId() {
	var ret = PdfSealObj.Pdf_ResetSelectUserCertId();
}

// 退出信步云登录
function pdf_XBYLogout() {
	var ret = PdfSealObj.Pdf_XBYLogout();
}

// 设置印章图片
function pdf_SetStampPic() {
	var pic = document.getElementById("image_base64").value;
    var ret = PdfSealObj.Pdf_SetStampPic(pic);
	if (ret != 0)
        alert("设置印章图片错误：" + ret);
	
	var certId = document.getElementById("image_certid").value;
	var ret = PdfSealObj.Pdf_SetCertId(certId);
	if (ret != 0)
        alert("设置证书id错误：" + ret);
}

// 证书登陆
function pdf_LoginByCertId() {
	var certId = document.getElementById("image_certid").value;
	var ret = PdfSealObj.Pdf_LoginByCertId(certId);
}

// 验证证书是否登陆
function pdf_IsLogin() {
	var certId = document.getElementById("image_certid").value;
	var ret = PdfSealObj.Pdf_IsLogin(certId);
	if (ret == 0) {
		alert("已登陆");
	} else {
		alert("未登陆");
	}
}

// 验签
function pdf_verify() {
    var ret = PdfSealObj.Pdf_Verify();
    if (ret == 0) //验证成功
        alert("验证成功");
    else
        alert("验签失败，错误码：" + ret);
}

// 获取文档页数
function pdf_getPageCount() {
    var count = PdfSealObj.Pdf_GetPageCount();
	if (count > 0) //验证成功
        alert("文档页数：" + count);
    else
        alert("获取文档页数失败，错误码：" + count);
}

// 获取当前页码
function pdf_getPageNum() {
    var num = PdfSealObj.Pdf_GetPageNum();
    alert("当前页码：" + num);
}

// 获取当前文档名
function pdf_getFileName() {
    var name = PdfSealObj.Pdf_GetFileName();
    alert("当前文档名：" + name);
}

// 获取文档路径
function GetPdfPath() {
    var localPath = PdfSealObj.Pdf_GetFilePath();
    alert("文档路径：" + localPath);
}

// 获取文件修改状态
function pdf_getFileChange() {
    var ret = PdfSealObj.GetFileChange();
    alert("文件修改状态：" + ret);
}

// 批量签章
function pdf_batch_sign() {
    var ret = PdfSealObj.Pdf_BatchSign();
	if (ret == -2024 || ret == -3004) {
		alert("取消签章：" + ret);
	} else if (ret == 17) {
		alert("尚未登陆：" + ret);
	} else if (ret != 0)
        alert("批量签章：" + ret);
}

// 获取当前打开pdf的base64数据
function GetPdfBase64() {
    var pdfBase64 = PdfSealObj.Pdf_GetFileBase64();
	if (pdfBase64 == "") {
		alert("获取base64数据失败");
	} else {
		alert("当前打开pdf的base64数据：" + pdfBase64);
	}
}

// 撤销签章
function pdf_remove_sign() {
    var ret = PdfSealObj.Pdf_RemoveSign();
	if (ret == -2010)
		alert("文档无签章！");
    else if (ret != 0)
        alert("撤销签章错误：" + ret);
}

// 获取签章个数
function GetSealCount() {
    var ret = PdfSealObj.Pdf_GetSealCount(); //0-无签章，>0-签章个数，<0-错误
    if (ret == 0)
		alert("无签章");
	else if (ret > 0)
		alert("签章个数：" + ret);
	else if (ret < 0)
		alert("错误：" + ret);
}

// 控制工具栏按钮
function EnableToolBtn() {
    var toolnames = document.getElementById("toolnames").value;
    var type = document.getElementById("type").value;
    var ret = PdfSealObj.Pdf_EnableToolBtn(toolnames, type); //0-禁用，1-显示，2-隐藏
}

// 文档是否打开
function pdf_isOpen() {
    var ret = PdfSealObj.Pdf_IsOpen();
	if (ret == 1)
		alert("文档已打开");
	else if (ret == 0)
		alert("文档未打开");
}

// 关闭文档
function ClosePDF() {
    var ret = PdfSealObj.Pdf_Close();
}

// 搜索文本
function pdf_searchText() {
    var searchText = document.getElementById('searchText').value;
    var ret = PdfSealObj.Pdf_SearchText(searchText);
	if (ret == "")
        alert("搜索文本错误：" + ret);
	else
		alert("搜索文本返回值：" + ret);
}

// 跳转位置
function pdf_goPosition() {
    var goPageNum = document.getElementById('goPageNum').value;
    var goPosition = document.getElementById('goPosition').value;
    PdfSealObj.Pdf_Go_Position(goPageNum, goPosition);
}

// 打开base64数据
function pdf_openBase64() {
    var base64PdfData = document.getElementById('base64PdfData').value;
    var ret = PdfSealObj.Pdf_OpenBase64(base64PdfData);
    alert("base64数据：" + ret);
}

// 打印
function pdf_print() {
    var ret = PdfSealObj.Pdf_Print();
	if (ret != 0)
        alert("打印错误：" + ret);
}

// 获取页面坐标
function pdf_getPosParam() {
    var ret = PdfSealObj.Pdf_GetPosParam();
    alert("页面坐标：" + ret);
}

// 获取pdf页面的宽高
function GetPageSize() {
    var width = 0;
    var height = 0;

    // 0-宽；1-高
    var width = PdfSealObj.Pdf_GetPageSize(1, 0);
	var height = PdfSealObj.Pdf_GetPageSize(1, 1);
    alert("宽：" + width + "  高：" + height);
}

// 批量签章（不展示）
function BatchSealByCoordinateFromFile() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var pageCount = PdfSealObj.FileGetPageCount(inFilePathBase64);
	if (pageCount > 0) {
		var rv = PdfSealObj.FileBatchSealByCoordinate(inFilePathBase64, outFilePathBase64, 1, pageCount, 1, 200, 200, 0);
		if (rv != 0) {
			alert("批签错误，错误码：" + rv);
		} else {
			alert("批签成功");
		}
	} else {
		alert("获取文档页数错误，错误码：" + pageCount);
	}
}

function startRequest() {
    var oShell = new ActiveXObject("WScript.Shell");
    var sRegVal = 'HKEY_CURRENT_USER\\Software\\Microsoft\\Windows NT\\CurrentVersion\\Windows\\Device';
    var sDefault = oShell.RegRead(sRegVal);
    return sDefault;
}

// 打印外传pdf
function PrintPDF() {
    var startPage = 1;
    var endPage = 1;
    var printDirection = 2; // 1是纵向打印，2是横向打印
    var printMode = 1; // 1是按照物理大小打印，2是适配页面大小进行缩放打印

    //var inFilePath = "d:\\SignPdf\\HmDybfBydfjg.pdf";
    var inFilePath = document.getElementById('inputFilePath').value;
    var inFilePathBase64 = Base64.encode(inFilePath);

    var cpname = startRequest();
    //alert(cpname);
    var cpnameBase64 = Base64.encode(cpname);

    var pageCount = PdfSealObj.FileGetPageCount(inFilePathBase64);
	if (pageCount > 0) {
		endPage = pageCount;
		var rv = PdfSealObj.FilePrintPDF(inFilePathBase64, cpnameBase64, startPage, endPage, printDirection, printMode);
		//var rv = PdfSealObj.FilePrintPDF(inFilePathBase64, cpnameBase64, startPage, endPage, 2, printMode);
		//var rv = PdfSealObj.FilePrintPDF(inFilePathBase64, cpnameBase64, startPage, endPage, printDirection, 2);
		//var rv = PdfSealObj.FilePrintPDF(inFilePathBase64, cpnameBase64, startPage, endPage, 2, 2);
		if (rv != 0) {
			alert("打印错误，错误码：" + rv);
		} else {
			alert("打印成功");
		}
	} else {
		alert("获取PDF页数错误，错误码：" + pageCount);
	}
}

// 获取文档页数（不展示）
function fileGetPageCount() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var inFilePathBase64 = Base64.encode(inFilePath);
    var ret = PdfSealObj.FileGetPageCount(inFilePathBase64);
	if (ret > 0) {
		alert("PDF页数：" + ret);
	} else {
		alert("获取PDF页数错误，错误码：" + ret);
	}
}

// 获取签章个数（不展示）
function fileGetSealCount() {
    var inFilePath = document.getElementById('outFilePath').value;
    var inFilePathBase64 = Base64.encode(inFilePath);
    var ret = PdfSealObj.FileGetSealCount(inFilePathBase64);
    if (ret >= 0) {
		alert("签章个数：" + ret);
	} else {
		alert("获取签章个数错误，错误码：" + ret);
	}
}

// 验证详细信息
function pdf_verifyDetail() {
    var ret = PdfSealObj.Pdf_VerifyDetail();
	alert("详细信息：" + ret);
}

// 坐标签章（不展示）
function fileSealByCoordinate() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var lPageNo = 1; // 签章所在页
    var fPdfXPos = 100; // 横坐标
    var fPdfYPos = 100; // 纵坐标
    var bizNum = ""; // 外传签章标识（如业务编号，为空内部自动生成）
    var rv = PdfSealObj.FileSealByCoordinate(inFilePathBase64, outFilePathBase64, lPageNo, fPdfXPos, fPdfYPos, bizNum);
    if (rv != 0) {
        alert("坐标签章错误，错误码：" + rv);
    } else {
        alert("坐标签章成功");
    }
}

// 关键字签章（不展示）
function fileSealByKeywords() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var strKeyword = document.getElementById('searchText').value;
    var strKeywordBase64 = Base64.encode(strKeyword);

    var lSearchType = 1; // 搜索顺序（以页为单位）1：正序搜索（默认）-1：逆序搜索
    var lMultiSeal = 0; // 0：单签章，其他参数暂不支持
    var rv = PdfSealObj.FileSealByKeywords(inFilePathBase64, outFilePathBase64, strKeywordBase64, lSearchType, lMultiSeal);
    if (rv != 0) {
        alert("关键字签章错误，错误码：" + rv);
    } else {
        alert("关键字签章成功");
    }
}

// 验证（不展示）
function fileVerify() {
    var inFilePath = document.getElementById('outFilePath').value;
    var inFilePathBase64 = Base64.encode(inFilePath);

    var lVerifyAll = 1; // 0-验证指定签名，1-验证全部签名
    var sigNum = 0; // 0-验证全部，其他验证指定签名
    var ret = PdfSealObj.FileVerify(inFilePathBase64, lVerifyAll, sigNum);
	if (ret != 0) {
		alert("验证错误，错误码：" + ret);
	} else {
		alert("验证信息：" + ret);
	}
}

// 骑缝（不展示）
function filePagingSeal() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var count = PdfSealObj.FileGetPageCount(inFilePathBase64);
	if (count) {
		var lStartShare = 50; // 起始页占比（1-99）
		var lVertPos = 60; // 骑缝纵向占页比例
		var lOffset = 0; // 印章与页面边缘距离
		var rv = PdfSealObj.FilePagingSeal(inFilePathBase64, outFilePathBase64, 1, count, lStartShare, lVertPos, lOffset);
		if (rv != 0) {
			alert("骑缝签章错误，错误码：" + rv);
		} else {
			alert("骑缝签章成功");
		}
	} else {
		alert("获取PDF页数错误，错误码：" + count);
	}
}

// 关于
function pdf_about() {
    PdfSealObj.Pdf_About();
}

// 逆时针旋转
function pdf_rotateMinus() {
    PdfSealObj.Pdf_RotateMinus();
}

// 顺时针旋转
function pdf_rotateAdd() {
    PdfSealObj.Pdf_RotateAdd();
}

// 骑缝章
function pdf_pagingSign() {
    var ret = PdfSealObj.Pdf_PagingSign();
    alert(ret);
}

// 显示或隐藏书签
function showOutline() {
    var ret = PdfSealObj.Pdf_Show_Outline(true);
}

function hideOutline() {
    var ret = PdfSealObj.Pdf_Show_Outline(false);
}

// 显示隐藏左边工具栏
function showLeftBar() {
    var ret = PdfSealObj.Pdf_ShowLeftBar(true);
}

function hideLeftBar() {
    var ret = PdfSealObj.Pdf_ShowLeftBar(false);
}

function showStateBar() {
    var ret = PdfSealObj.Pdf_ShowStateBar(true);
}

function hideStateBar() {
    var ret = PdfSealObj.Pdf_ShowStateBar(false);
}

// 获取版本号
function pdf_getVersion() {
    var ret = PdfSealObj.Pdf_GetVersion();
    document.getElementById('pdfSignOcxVersion').value = ret;
}

function OnPdfSignedEvent(result)
{
	//if (result == 0)
	//{
	//	alert("签名成功事件");
	//}
	//else
	//{
	//	alert("签名失败事件");
	//}
}

function OnPdfRemovedEvent(result)
{
	//if (result == 0)
	//{
	//	alert("删除成功事件");
	//}
	//else
	//{
	//	alert("删除失败事件");
	//}
}
 
function OnPdfVerifyEvent(result)
{
	//if (result == 0)
	//{
	//	alert("验证成功事件");
	//}
	//else
	//{
	//	alert("验证失败事件");
	//}
}

function $checkBrowserISIE() 
{
	return (!!window.ActiveXObject || 'ActiveXObject' in window) ? true : false;
}

//load a control
function LoadControl(CLSID, ctlName, testFuncName) 
{
	var pluginDiv = document.getElementById("pluginDiv" + ctlName);
	if (pluginDiv) {
		return true;
	}
	pluginDiv = document.createElement("div");
	pluginDiv.id = "pluginDiv" + ctlName;
	//document.body.appendChild(pluginDiv);
	var div = document.getElementById("PdfSealOcxContainer");
	div.appendChild(pluginDiv);

	try {
		if ($checkBrowserISIE()) {	// IE
			pluginDiv.innerHTML = '<object id="' + ctlName + '" classid="CLSID:' + CLSID + '" style="HEIGHT:740px; WIDTH:940px"></object>';
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
		}
		
		if (testFuncName != null && testFuncName != "" && eval(ctlName + "." + testFuncName) == undefined) {
            document.body.removeChild(pluginDiv);
            pluginDiv.innerHTML = "";
            pluginDiv = null;
            return false; 
		}
		
		setTimeout("pdf_about()", 10);
		
		return true;
	} catch (e) {
		div.removeChild(pluginDiv);
		pluginDiv.innerHTML = "";
		pluginDiv = null;
		return false;
	}
}

/*
 (function() {	
	 var bOK = $LoadControl("1A5F8EA9-5A10-47EF-81E7-DF7AD23955BC", "PdfSealObj", "Pdf_ZoomIn");
	 if (!bOK) {
		 alert("电子签章控件未加载!");
		 return;
	 }
 })();
*/
