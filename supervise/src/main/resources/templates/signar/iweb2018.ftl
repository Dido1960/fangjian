<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
</head>
<script language="javascript" for=iWebPDF2018 event="OnToolsClick(vIndex,vCaption)">
    console.log('IE编号:' + vIndex + '\n\r' + '条目:' + vCaption + '\n\r' + '请根据这些信息编写按钮具体功能');
    if (vIndex == 10) {
        parent.web_save();
    } else if (vIndex == 11) {
        parent.fullScreen();
    }else if (vIndex == 12) {
        parent.downloadPdf();
    }

</script>
<script language="javascript" for=iWebPDF2018 event="OnMenuClick(vIndex,vCaption)">

    console.log('IE编号:' + vIndex + '\n\r' + '条目:' + vCaption + '\n\r' + '请根据这些信息编写菜单具体功能');
    if (vIndex == 10) {
        alert("自定义按钮事件！");
    } else if (vIndex == 11) {

    }else if (vIndex == 12) {
        parent.downloadPdf();
    }

</script>
<script type="text/javascript">

    //
    function OnToolsClick(vIndex, vCaption) {
        //alert('编号:'+vIndex+'\n\r'+'条目:'+vCaption+'\n\r'+'请根据这些信息编写按钮具体功能');
        if (vIndex == 10) {
            /* addins.Alert("测试按钮一事件！");*/
            parent.web_save();
        } else if (vIndex == 11) {
            parent.fullScreen();
        }else if (vIndex == 12) {
            parent.downloadPdf();
        }
    }




    /*var tempFile = iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.CreateTempFileName();

    iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("DBSTEP","DBSTEP");
    iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("OPTION","LOADFILE");
    iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("FILETYPE","PDF");
    iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("USERNAME","演示人");
    iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("RECORDID",mRecordID);
    iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("FILENAME","1385716767003.pdf");
    var ret=iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.PostDBPacket(false);
    iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.MsgFileSave(tempFile);
    iWebPDF2018.Documents.Open(tempFile);*/

</script>
<script type="text/javascript" src="/js/iWebPDf2018-1.2.js"></script>

<body style="overflow:hidden;margin: 0px;height:   100vh">
<div bgcolor="#ffffff" topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" style="height: 100vh;overflow: hidden" id="IWEBPDF2018_LOAD_PDF_CONETENT">

</div>


</body>
<script>

</script>

<script language="javascript" for=iWebPDF2018 event="OnDocumentOpen()">
    parent.localStorageSave();
</script>
<script>
    //谷歌文件加载完成执行
    function OnDocumentOpen(){
        parent.localStorageSave();
    }
</script>

</html>