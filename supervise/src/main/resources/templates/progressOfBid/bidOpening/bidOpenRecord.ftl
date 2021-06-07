<link rel="stylesheet" href="${ctx}/css/bidOpenRecord.css">
<#if fdfs??>
    <div id="showPdfView" style="height: 700px">
        <script type="text/javascript" src="/js/iWebPDF2018.js"></script>
    </div>
</#if>
<script defer="defer">
    $(function () {
        for (var i = 0; i < 21; i++) {
            iWebPDF2018.CommandBars.Item(i).Visible = false;
        }
        iWebPDF2018.CommandBars.Item(18).Visible = true;
        var addins = iWebPDF2018.iWebPDFFun;
        if (addins != null) {
            addins.ShowTools = 0;
            //开始页禁用
            addins.StartPageVisible = false;
            //不显示标签页
            addins.ShowTabBarVisible = false;


        }

        if (addins != null) {
            if (!isNull("${fdfs.url}")) {
                addins.WebOpenUrlFile("${fdfs.url}");
            } else {
                addins.alert("文件路径不存在！");
            }
            setTimeout(function () {
                addins.AppendTools("10", "下载", 20);
                addins.AppendTools("11", "打印", 3);
                addins.AppendTools("12", "保存", 2);
            }, 200);
        }
        $('.right .load_url_content').removeAttr("style");
    });


    function OnToolsClick(vIndex, vCaption) {
        //alert('编号:'+vIndex+'\n\r'+'条目:'+vCaption+'\n\r'+'请根据这些信息编写按钮具体功能');
        if (vIndex == 10) {
            /* addins.Alert("测试按钮一事件！");*/
            downloadPdf();
        }

        if (vIndex == 11) {
            /* addins.Alert("测试按钮一事件！");*/
            printPdf();
        }
        if (vIndex == 12) {
            /* addins.Alert("测试按钮一事件！");*/
            web_save();
        }
    }

    /**
     * 文件保存
     * */
    function downloadPdf() {
        var ret = iWebPDF2018.iWebPDFFun.WebSaveLocal();
        if (!ret) {
            addins.Alert("下载失败！");
        } else {
            addins.Alert("下载成功！");
        }
    }

    /**
     * 文件打印
     * */
    function printPdf() {
        var ret = iWebPDF2018.iWebPDFFun.WebPrint(1, "", 0, 0, true);
        if (!ret) {
            addins.Alert("文件打印失败！");
        } else {
            addins.Alert("打印成功！");
        }
    }

    /**
     * 获取PDF对应页码
     * */
    function getPageNum() {
        return iWebPDF2018.iWebPDFFun.CurPage;
    }

    /***
     * 跳转到指定的页码
     * */
    function gotoPageNum(pageNo) {
        iWebPDF2018.iWebPDFFun.CurPage = pageNo;
    }

    /**
     * 文件保存
     * */
    function web_save() {
        var record_id = "${fdfs.id}";
        var addins = iWebPDF2018.iWebPDFFun;
        addins.Alert("检测到签章： " + addins.SignatureCount());
        if (addins.SignatureCount() == 0) {
            addins.Alert("未检测到签章");
            return;
        }

        // 必须上传的文件ID
        if (addins != null && !isNull("${fdfs.url}")) {
            addins.WebUrl = "http://" + window.location.host + "/sigar/uploadPdfSignar";
            addins.RECORDID = record_id;
            addins.FILENAME = "${fdfs.name}";
            addins.FILETYPE = ".pdf";
            addins.USERNAME = "${user.username}";
            var ret = addins.WebSave();
            if (!ret) {
                addins.Alert("文档保存失败！");
            } else {
                addins.Alert("保存成功！");
                loadPdf('${fdfs.mark}')
            }
        } else {
            addins.Alert("文档不存在信息！");
        }
    }
</script>

<script language="javascript" for=iWebPDF2018 event="OnToolsClick(vIndex,vCaption)">
    try {
        console.log('IE编号:' + vIndex + '\n\r' + '条目:' + vCaption + '\n\r' + '请根据这些信息编写按钮具体功能');
        if (vIndex == 10) {
            downloadPdf();
        }

        if (vIndex == 11) {
            printPdf();
        }

        if (vIndex == 12) {
            web_save();
        }
    } catch (e) {

    }
</script>
