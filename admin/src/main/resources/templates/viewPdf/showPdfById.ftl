<div id="showPdfView" style="width: 100%;height: 550px;">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/js/iWebPDF2018.js"></script>
</div>
<script defer="defer">
    $(function () {
        for (var i = 0; i < 21; i++) {
            iWebPDF2018.CommandBars.Item(i).Visible = false;
        }
        // 设置验签功能可用
        iWebPDF2018.CommandBars.Item(20).Visible = true;
        var addins = iWebPDF2018.iWebPDFFun;
        if (addins != null) {
            addins.ShowTabBarVisible = false;
            addins.ShowTools = 0;
            //开始页禁用
            addins.StartPageVisible = false;
            //不显示标签页
            addins.ShowTabBarVisible = false;
        }

        //fdfs 表
        if (isNull("${fdfs.id}")) {
            addins.alert("文件的唯一索引,未定义");
            return;
        }

        if (addins != null) {
            if (!isNull("${fdfs.url}")) {
                iWebPDF2018.Documents.OpenFromURL("${fdfs.url}");
            } else {
                addins.alert("文件路径不存在！");
            }
            setTimeout(function () {
                addins.AppendTools("10", "下载", 1);
                addins.AppendTools("11", "打印", 3);
            }, 200);
        }
        $('.right .load_url_content').removeAttr("style");
    });


    /**
     * 谷歌内核回调函数
     * **/
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
</script>

<#--IE 定义得脚本回调函数-->
<script language="javascript" for=iWebPDF2018 event="OnToolsClick(vIndex,vCaption)">
    try {
        console.log('IE编号:' + vIndex + '\n\r' + '条目:' + vCaption + '\n\r' + '请根据这些信息编写按钮具体功能');
        if (vIndex == 10) {
            var ret = iWebPDF2018.iWebPDFFun.WebSaveLocal();
            if (!ret) {
                addins.Alert("下载失败！");
            } else {
                addins.Alert("下载成功！");
            }
        }

        if (vIndex == 11) {
            var ret = iWebPDF2018.iWebPDFFun.WebPrint(1, "", 0, 0, true);
            if (!ret) {
                addins.Alert("文件打印失败！");
            } else {
                addins.Alert("打印成功！");
            }
        }
    } catch (e) {

    }
</script>
<#--IE 定义得脚本回调函数 --end-->