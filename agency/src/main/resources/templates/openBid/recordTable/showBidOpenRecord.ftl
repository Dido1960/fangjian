<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助开标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <script src="${ctx}/js/convertMoney.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <#include "${ctx}/openBid/recordTable/publicOpenRecord.ftl"/>
</head>
<body style="overflow:hidden;">
<ol class="tit pdf-file-list" style="display: none">
    <li class="choice" fdfsMark="${mark}" >开标一览表</li>
</ol>
<div class="cont-text" style="height: 100vh;">
    <#--是否帮助按钮-->
    <#assign showHelpBtn="false"/>
    <#--是否启用本地缓存机制-->
    <#assign localCache="false"/>
    <#--是否开启另存为按钮-->
    <#assign showSaveAs="true"/>
    <#--是否开启另存为按钮-->
    <#assign fullScreen="true"/>
    <#--是否开启电子签章按钮-->
    <#assign signatureBtn="true"/>
    <#--1.必须存在class 为(pdf-file-list文件)-->
    <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
    <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
    <#include "${ctx}/common/showPDFView.ftl"/>
</div>

<script>

    $(function () {
        setTimeout(function () {
            var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
            if (isNull(iWebPDF2018)) {
                return;
            }
            var addins = iWebPDF2018.iWebPDFFun;
            addins.AppendTools("1001", "打印", 3);
            addins.AppendTools("1002", "修改", 46);
        }, 2000);
    })
    /**
     * 谷歌自定义插件方法
     */
    function customOnToolsClick(vIndex, vCaption) {
        if (vIndex == 1000) {
            downloadPdf();
        }

        if (vIndex == 1001) {
            printPdf();
        }

        if (vIndex == 1002) {
            updateRecordPdf();
        }
    }
    /**
     * 文件保存
     * */
    function downloadPdf() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        var ret = iWebPDF2018.iWebPDFFun.WebSaveLocal();
        if (!ret) {
            addins.Alert("下载失败！");
        } else {
            addins.Alert("下载成功！");
        }
    }

    /**
     * 修改开标记录表
     * */
    function updateRecordPdf() {
        hide_IWeb2018();
        window.top.layer.confirm('确认修改开标记录表吗？', {
                icon: 3,
                btn: ['确定', '取消'],
                cancel: function () {
                    show_IWeb2018();
                }
            }, function () {
                window.top.layer.closeAll();
                window.top.goToUrl('${ctx}/staff/bidOpenRecordPage/2', parent.$(".left ul li[data-flowname='bidOpenRecord']"));
                // parent.window.location.reload();
            },function () {
                show_IWeb2018();
            }
        );
    }
    /**
     * 文件打印
     * */
    function printPdf() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        var ret = iWebPDF2018.iWebPDFFun.WebPrint(1, "", 0, 0, true);
        if (!ret) {
            addins.Alert("文件打印失败！");
        } else {
            addins.Alert("打印成功！");
        }
    }
</script>
</body>
