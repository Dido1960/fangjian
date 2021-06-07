<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>文件查看</title>
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
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/compared.css">
    <script>
        function showSelect(obj) {
            $(obj).siblings("ul").slideDown();
            hide_IWeb2018()
        }

        function cancelSelect(obj) {

            setTimeout(function () {
                $(obj).siblings("ul").fadeOut();
                show_IWeb2018();
            }, 200);
        }

        function selectBidderId(e) {
            $('.bidders-list').val($(e).attr("data-id"));
            $('.bidderName').val($(e).attr("title"));
            changBidderView();
            cancelSelect();
        }
    </script>
    <style>
        <#if bidSection.paperEval?? && bidSection.paperEval == "1">
        .head ol {
            width: 160px;
        }
        </#if>
    </style>
</head>
<body>
<div class="head">
    <ol class="tit pdf-file-list">
        <#if bidSection.paperEval?? && bidSection.paperEval == "1">
            <li class="choice" fdfsMark="${tenderMark}">招标文件</li>
            <li class=" tenderFile"  fdfsMark="/bidderFile/{bidDocId}/resources/BidFile.pdf">投标文件</li>
        <#else>
            <li class="choice" fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/TempConvert/temp.pdf">招标文件</li>
            <li class=" tenderFile"  fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">
                资格证明
            </li>
            <li class=" tenderFile" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">
                商务标
            </li>
            <li  class="tenderFile" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">
                技术标
            </li>
        </#if>
    </ol>
    <div class="layui-form inp">
        <label>投标单位</label>
        <div id="bidderLeft">
            <input class="bidders-list" value="${bidders[0].id}" type="hidden"/>
            <input class="bidderName" type="text" readonly onclick="showSelect(this)" value="1.${bidders[0].bidderName}" onblur="cancelSelect(this)">
            <ul class="list" id="leftUl" style="display: none">
                <#list bidders as bidder>
                    <li data-id="${bidder.id}" onclick="selectBidderId(this)"
                        title="${bidder_index+1}.${bidder.bidderName}">${bidder_index+1}.${bidder.bidderName}</li>
                </#list>
            </ul>
        </div>
    </div>
</div>
<div class="cont" id="loadPdfLeftDiv">
    <#--是否帮助按钮-->
    <#assign showHelpBtn="true"/>
    <#--是否启用本地缓存机制-->
    <#assign localCache="true"/>
    <#--是否开启另存为按钮-->
    <#assign showSaveAs="false"/>
    <#--是否开启另存为按钮-->
    <#assign fullScreen="true"/>
    <#--1.必须存在class 为(pdf-file-list文件)-->
    <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
    <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
    <#include "/viewPdf/showPDFView.ftl"/>
</div>
</body>
</html>