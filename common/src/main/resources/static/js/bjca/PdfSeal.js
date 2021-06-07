/*--------------------------------------------------------------------------  
 *
 * BJCA Adaptive Javascript
 * This script support bjca pdfseal client version 4.4.0.
 * Author:BJCA-zc
 *--------------------------------------------------------------------------*/

// ���ع�����
function hide_toolbar() {
    var ret = PdfSealObj.Hide_ToolBar();
    if (ret != 0)
        alert("���ع�����ʧ�ܣ������룺" + ret);
}

// ��ʾ������
function show_toolbar() {
    var ret = PdfSealObj.Show_ToolBar();
    if (ret != 0)
        alert("��ʾ������ʧ�ܣ������룺" + ret);
}

// �򿪱��ػ�����pdf
function open_url_pdf() {
    var pageNum = document.getElementById('url').value;
    var ret = PdfSealObj.Pdf_Open_path(pageNum);
    if (ret != 0)
        alert("�򿪱��ػ�����pdfʧ�ܣ������룺" + ret);
}

// ѡ���ļ���pdf
function open_pdf() {
    var ret = PdfSealObj.Pdf_Open();
    if (ret != 0)
        alert("ѡ���ļ���pdfʧ�ܣ������룺" + ret);
}

// ����ˮӡ����
function SetWaterMarkInfo() {
    var arr1 = {
        "IsShowWaterMark": true,
        "IsShowTime": true,
        "MarkRotate": 15,
        "FontSize": 30,
        "ShowData": "����������֤�ɷ����޹�˾",
        //"ShowTime": "2019��11��11��"
        "ShowTime": ""
    };

    var thresholdKeyJson = {
        "version": "1.0",
        "watermark": arr1
    };

    var rv = PdfSealObj.Pdf_SetWaterMarkInfo(JSON.stringify(thresholdKeyJson));
    if (rv != 0) {
        alert("����ˮӡ����ʧ�ܣ������룺" + rv);
        return rv;
    }
}

// �����ĵ���ָ��·��
function save_pdf() {
    var savePath = document.getElementById('savePath').value;
    var ret = PdfSealObj.Pdf_Save_Path(savePath);
    if (ret == 0)
        alert("����ɹ�");
    else
        alert("�����ĵ�ʧ�ܣ������룺" + ret);
}

// ���Ϊ
function save_as() {
    var ret = PdfSealObj.Pdf_SaveAs();
    if (ret == 0)
        alert("����ɹ�");
    else
        alert("����ĵ�ʧ�ܣ������룺" + ret);
}

// ����pdf
function select_Path_Save() {
    var ret = PdfSealObj.Pdf_Save();
    if (ret == -1021)
		alert("�ĵ�δ�ı�");
	else if (ret != 0)
        alert("����pdfʧ�ܣ������룺" + ret);
}

// �Ŵ�pdf
function pdf_zoomin() {
    var ret = PdfSealObj.Pdf_ZoomIn();
    if (ret != 0)
        alert("�Ŵ�pdfʧ�ܣ������룺" + ret);
}

// ��Сpdf
function pdf_zoomout() {
    var ret = PdfSealObj.Pdf_ZoomOut();
    if (ret != 0)
        alert("��Сpdfʧ�ܣ������룺" + ret);
}

// �Ϸ�ҳ
function pdf_pageup() {
    var ret = PdfSealObj.Pdf_PageUp();
    if (ret != 0)
        alert("�Ϸ�ҳʧ�ܣ������룺" + ret);
}

// �·�ҳ
function pdf_pagedown() {
    var ret = PdfSealObj.Pdf_PageDown();
    if (ret != 0)
        alert("�·�ҳʧ�ܣ������룺" + ret);
}

// ����
function pdf_page_onsize() {
    var ret = PdfSealObj.Pdf_OnSize();
    if (ret != 0)
        alert("����ʧ�ܣ������룺" + ret);
}

// ��ҳ
function pdf_goto_page() {
    var ret = PdfSealObj.Pdf_PageOn(document.getElementById('pagenum').value);
    if (ret != 0)
        alert("��ҳʧ�ܣ������룺" + ret);
}

// ǩ��
function pdf_sign() {
    var ret = PdfSealObj.Pdf_Sign();
    if (ret != 0)
        alert("ǩ��ʧ�ܣ������룺" + ret);
}

// �л�ӡ��ͼƬ
function pdf_ChangeSeal() {
    var ret = PdfSealObj.Pdf_ChangeSeal();
	if (ret != 0)
        alert("�л�ӡ��ͼƬ��" + ret);
}

// ��ȡ��ӡ��ͼƬ�б�
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
			var img = "<img title='���������һ��ѡ���ӡ��' class='doQianzhang' style='width: 80px;cursor: pointer;' src='data:image/jpg;base64," + imgList[1] + "' imgBase64='" + imgList[1] + "' certid='" + _certid + "' msspid='" + _msspid + "'>";
			$(".seal_img").append(img);
		}
	}
}

// ��ȡkeyӡ��ͼƬ�б�
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
			var img = "<img title='���������һ��ѡ���ӡ��' class='doQianzhang' style='width: 80px;cursor: pointer;' src='data:image/jpg;base64," + imgList[1] + "' imgBase64='" + imgList[1] + "' certid='" + _certid + "' msspid='" + _msspid + "'>";
			$(".seal_img").append(img);
		}
	}
}

// ���õ�ǰѡ���֤��id
function pdf_ResetSelectUserCertId() {
	var ret = PdfSealObj.Pdf_ResetSelectUserCertId();
}

// �˳��Ų��Ƶ�¼
function pdf_XBYLogout() {
	var ret = PdfSealObj.Pdf_XBYLogout();
}

// ����ӡ��ͼƬ
function pdf_SetStampPic() {
	var pic = document.getElementById("image_base64").value;
    var ret = PdfSealObj.Pdf_SetStampPic(pic);
	if (ret != 0)
        alert("����ӡ��ͼƬ����" + ret);
	
	var certId = document.getElementById("image_certid").value;
	var ret = PdfSealObj.Pdf_SetCertId(certId);
	if (ret != 0)
        alert("����֤��id����" + ret);
}

// ֤���½
function pdf_LoginByCertId() {
	var certId = document.getElementById("image_certid").value;
	var ret = PdfSealObj.Pdf_LoginByCertId(certId);
}

// ��֤֤���Ƿ��½
function pdf_IsLogin() {
	var certId = document.getElementById("image_certid").value;
	var ret = PdfSealObj.Pdf_IsLogin(certId);
	if (ret == 0) {
		alert("�ѵ�½");
	} else {
		alert("δ��½");
	}
}

// ��ǩ
function pdf_verify() {
    var ret = PdfSealObj.Pdf_Verify();
    if (ret == 0) //��֤�ɹ�
        alert("��֤�ɹ�");
    else
        alert("��ǩʧ�ܣ������룺" + ret);
}

// ��ȡ�ĵ�ҳ��
function pdf_getPageCount() {
    var count = PdfSealObj.Pdf_GetPageCount();
	if (count > 0) //��֤�ɹ�
        alert("�ĵ�ҳ����" + count);
    else
        alert("��ȡ�ĵ�ҳ��ʧ�ܣ������룺" + count);
}

// ��ȡ��ǰҳ��
function pdf_getPageNum() {
    var num = PdfSealObj.Pdf_GetPageNum();
    alert("��ǰҳ�룺" + num);
}

// ��ȡ��ǰ�ĵ���
function pdf_getFileName() {
    var name = PdfSealObj.Pdf_GetFileName();
    alert("��ǰ�ĵ�����" + name);
}

// ��ȡ�ĵ�·��
function GetPdfPath() {
    var localPath = PdfSealObj.Pdf_GetFilePath();
    alert("�ĵ�·����" + localPath);
}

// ��ȡ�ļ��޸�״̬
function pdf_getFileChange() {
    var ret = PdfSealObj.GetFileChange();
    alert("�ļ��޸�״̬��" + ret);
}

// ����ǩ��
function pdf_batch_sign() {
    var ret = PdfSealObj.Pdf_BatchSign();
	if (ret == -2024 || ret == -3004) {
		alert("ȡ��ǩ�£�" + ret);
	} else if (ret == 17) {
		alert("��δ��½��" + ret);
	} else if (ret != 0)
        alert("����ǩ�£�" + ret);
}

// ��ȡ��ǰ��pdf��base64����
function GetPdfBase64() {
    var pdfBase64 = PdfSealObj.Pdf_GetFileBase64();
	if (pdfBase64 == "") {
		alert("��ȡbase64����ʧ��");
	} else {
		alert("��ǰ��pdf��base64���ݣ�" + pdfBase64);
	}
}

// ����ǩ��
function pdf_remove_sign() {
    var ret = PdfSealObj.Pdf_RemoveSign();
	if (ret == -2010)
		alert("�ĵ���ǩ�£�");
    else if (ret != 0)
        alert("����ǩ�´���" + ret);
}

// ��ȡǩ�¸���
function GetSealCount() {
    var ret = PdfSealObj.Pdf_GetSealCount(); //0-��ǩ�£�>0-ǩ�¸�����<0-����
    if (ret == 0)
		alert("��ǩ��");
	else if (ret > 0)
		alert("ǩ�¸�����" + ret);
	else if (ret < 0)
		alert("����" + ret);
}

// ���ƹ�������ť
function EnableToolBtn() {
    var toolnames = document.getElementById("toolnames").value;
    var type = document.getElementById("type").value;
    var ret = PdfSealObj.Pdf_EnableToolBtn(toolnames, type); //0-���ã�1-��ʾ��2-����
}

// �ĵ��Ƿ��
function pdf_isOpen() {
    var ret = PdfSealObj.Pdf_IsOpen();
	if (ret == 1)
		alert("�ĵ��Ѵ�");
	else if (ret == 0)
		alert("�ĵ�δ��");
}

// �ر��ĵ�
function ClosePDF() {
    var ret = PdfSealObj.Pdf_Close();
}

// �����ı�
function pdf_searchText() {
    var searchText = document.getElementById('searchText').value;
    var ret = PdfSealObj.Pdf_SearchText(searchText);
	if (ret == "")
        alert("�����ı�����" + ret);
	else
		alert("�����ı�����ֵ��" + ret);
}

// ��תλ��
function pdf_goPosition() {
    var goPageNum = document.getElementById('goPageNum').value;
    var goPosition = document.getElementById('goPosition').value;
    PdfSealObj.Pdf_Go_Position(goPageNum, goPosition);
}

// ��base64����
function pdf_openBase64() {
    var base64PdfData = document.getElementById('base64PdfData').value;
    var ret = PdfSealObj.Pdf_OpenBase64(base64PdfData);
    alert("base64���ݣ�" + ret);
}

// ��ӡ
function pdf_print() {
    var ret = PdfSealObj.Pdf_Print();
	if (ret != 0)
        alert("��ӡ����" + ret);
}

// ��ȡҳ������
function pdf_getPosParam() {
    var ret = PdfSealObj.Pdf_GetPosParam();
    alert("ҳ�����꣺" + ret);
}

// ��ȡpdfҳ��Ŀ��
function GetPageSize() {
    var width = 0;
    var height = 0;

    // 0-��1-��
    var width = PdfSealObj.Pdf_GetPageSize(1, 0);
	var height = PdfSealObj.Pdf_GetPageSize(1, 1);
    alert("��" + width + "  �ߣ�" + height);
}

// ����ǩ�£���չʾ��
function BatchSealByCoordinateFromFile() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var pageCount = PdfSealObj.FileGetPageCount(inFilePathBase64);
	if (pageCount > 0) {
		var rv = PdfSealObj.FileBatchSealByCoordinate(inFilePathBase64, outFilePathBase64, 1, pageCount, 1, 200, 200, 0);
		if (rv != 0) {
			alert("��ǩ���󣬴����룺" + rv);
		} else {
			alert("��ǩ�ɹ�");
		}
	} else {
		alert("��ȡ�ĵ�ҳ�����󣬴����룺" + pageCount);
	}
}

function startRequest() {
    var oShell = new ActiveXObject("WScript.Shell");
    var sRegVal = 'HKEY_CURRENT_USER\\Software\\Microsoft\\Windows NT\\CurrentVersion\\Windows\\Device';
    var sDefault = oShell.RegRead(sRegVal);
    return sDefault;
}

// ��ӡ�⴫pdf
function PrintPDF() {
    var startPage = 1;
    var endPage = 1;
    var printDirection = 2; // 1�������ӡ��2�Ǻ����ӡ
    var printMode = 1; // 1�ǰ��������С��ӡ��2������ҳ���С�������Ŵ�ӡ

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
			alert("��ӡ���󣬴����룺" + rv);
		} else {
			alert("��ӡ�ɹ�");
		}
	} else {
		alert("��ȡPDFҳ�����󣬴����룺" + pageCount);
	}
}

// ��ȡ�ĵ�ҳ������չʾ��
function fileGetPageCount() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var inFilePathBase64 = Base64.encode(inFilePath);
    var ret = PdfSealObj.FileGetPageCount(inFilePathBase64);
	if (ret > 0) {
		alert("PDFҳ����" + ret);
	} else {
		alert("��ȡPDFҳ�����󣬴����룺" + ret);
	}
}

// ��ȡǩ�¸�������չʾ��
function fileGetSealCount() {
    var inFilePath = document.getElementById('outFilePath').value;
    var inFilePathBase64 = Base64.encode(inFilePath);
    var ret = PdfSealObj.FileGetSealCount(inFilePathBase64);
    if (ret >= 0) {
		alert("ǩ�¸�����" + ret);
	} else {
		alert("��ȡǩ�¸������󣬴����룺" + ret);
	}
}

// ��֤��ϸ��Ϣ
function pdf_verifyDetail() {
    var ret = PdfSealObj.Pdf_VerifyDetail();
	alert("��ϸ��Ϣ��" + ret);
}

// ����ǩ�£���չʾ��
function fileSealByCoordinate() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var lPageNo = 1; // ǩ������ҳ
    var fPdfXPos = 100; // ������
    var fPdfYPos = 100; // ������
    var bizNum = ""; // �⴫ǩ�±�ʶ����ҵ���ţ�Ϊ���ڲ��Զ����ɣ�
    var rv = PdfSealObj.FileSealByCoordinate(inFilePathBase64, outFilePathBase64, lPageNo, fPdfXPos, fPdfYPos, bizNum);
    if (rv != 0) {
        alert("����ǩ�´��󣬴����룺" + rv);
    } else {
        alert("����ǩ�³ɹ�");
    }
}

// �ؼ���ǩ�£���չʾ��
function fileSealByKeywords() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var strKeyword = document.getElementById('searchText').value;
    var strKeywordBase64 = Base64.encode(strKeyword);

    var lSearchType = 1; // ����˳����ҳΪ��λ��1������������Ĭ�ϣ�-1����������
    var lMultiSeal = 0; // 0����ǩ�£����������ݲ�֧��
    var rv = PdfSealObj.FileSealByKeywords(inFilePathBase64, outFilePathBase64, strKeywordBase64, lSearchType, lMultiSeal);
    if (rv != 0) {
        alert("�ؼ���ǩ�´��󣬴����룺" + rv);
    } else {
        alert("�ؼ���ǩ�³ɹ�");
    }
}

// ��֤����չʾ��
function fileVerify() {
    var inFilePath = document.getElementById('outFilePath').value;
    var inFilePathBase64 = Base64.encode(inFilePath);

    var lVerifyAll = 1; // 0-��ָ֤��ǩ����1-��֤ȫ��ǩ��
    var sigNum = 0; // 0-��֤ȫ����������ָ֤��ǩ��
    var ret = PdfSealObj.FileVerify(inFilePathBase64, lVerifyAll, sigNum);
	if (ret != 0) {
		alert("��֤���󣬴����룺" + ret);
	} else {
		alert("��֤��Ϣ��" + ret);
	}
}

// ��죨��չʾ��
function filePagingSeal() {
    var inFilePath = document.getElementById('inputFilePath').value;
    var outFilePath = document.getElementById('outFilePath').value;

    var inFilePathBase64 = Base64.encode(inFilePath);
    var outFilePathBase64 = Base64.encode(outFilePath);

    var count = PdfSealObj.FileGetPageCount(inFilePathBase64);
	if (count) {
		var lStartShare = 50; // ��ʼҳռ�ȣ�1-99��
		var lVertPos = 60; // �������ռҳ����
		var lOffset = 0; // ӡ����ҳ���Ե����
		var rv = PdfSealObj.FilePagingSeal(inFilePathBase64, outFilePathBase64, 1, count, lStartShare, lVertPos, lOffset);
		if (rv != 0) {
			alert("���ǩ�´��󣬴����룺" + rv);
		} else {
			alert("���ǩ�³ɹ�");
		}
	} else {
		alert("��ȡPDFҳ�����󣬴����룺" + count);
	}
}

// ����
function pdf_about() {
    PdfSealObj.Pdf_About();
}

// ��ʱ����ת
function pdf_rotateMinus() {
    PdfSealObj.Pdf_RotateMinus();
}

// ˳ʱ����ת
function pdf_rotateAdd() {
    PdfSealObj.Pdf_RotateAdd();
}

// �����
function pdf_pagingSign() {
    var ret = PdfSealObj.Pdf_PagingSign();
    alert(ret);
}

// ��ʾ��������ǩ
function showOutline() {
    var ret = PdfSealObj.Pdf_Show_Outline(true);
}

function hideOutline() {
    var ret = PdfSealObj.Pdf_Show_Outline(false);
}

// ��ʾ������߹�����
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

// ��ȡ�汾��
function pdf_getVersion() {
    var ret = PdfSealObj.Pdf_GetVersion();
    document.getElementById('pdfSignOcxVersion').value = ret;
}

function OnPdfSignedEvent(result)
{
	//if (result == 0)
	//{
	//	alert("ǩ���ɹ��¼�");
	//}
	//else
	//{
	//	alert("ǩ��ʧ���¼�");
	//}
}

function OnPdfRemovedEvent(result)
{
	//if (result == 0)
	//{
	//	alert("ɾ���ɹ��¼�");
	//}
	//else
	//{
	//	alert("ɾ��ʧ���¼�");
	//}
}
 
function OnPdfVerifyEvent(result)
{
	//if (result == 0)
	//{
	//	alert("��֤�ɹ��¼�");
	//}
	//else
	//{
	//	alert("��֤ʧ���¼�");
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
		 alert("����ǩ�¿ؼ�δ����!");
		 return;
	 }
 })();
*/
