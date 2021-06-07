<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>预审关联</title>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

    <link rel="stylesheet" href="${ctx}/css/bidRelate.css">
</head>
<body>
<div class="cont">
    <div class="cont-top">
        <h3>标段信息</h3>
        <form action="">
            <div class="check">
                <label for="">标段名称</label>
                <input type="text" value="${bidSection.bidSectionName}" disabled>
            </div>
            <div class="check">
                <label for="">标段编号</label>
                <input type="text" value="${bidSection.bidSectionCode}" disabled>
            </div>
            <div class="check">
                <label for="">标段类型</label>
                <input type="text" value="${bidType}" disabled>
            </div>
            <div class="check">
                <label for="">开标时间</label>
                <input type="text" value="${tenderDoc.bidOpenTime}" disabled>
            </div>
        </form>
    </div>
    <div class="document">
        <div class="document-left">
            <ol>
                <li>序号</li>
                <li class="long">投标单位</li>
            </ol>
            <ul class="bidderUl">
                <input type="hidden" class="bidders-list" value="">
                <#list bidders as bidder>
                    <li  onclick="selectThis(this,'${bidder.id}')">
                        <div>${bidder_index + 1}</div>
                        <div class="long" title="${bidder.bidderName}">${bidder.bidderName}</div>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="document-right">
            <ol class="pdf-file-list" id="file-type-li">
                <li class="choice" data-filetype="1" fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/TempConvert/temp.pdf">招标文件</li>
                <li data-pdf-index="3" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">商务文件</li>
                <li data-pdf-index="2" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">技术文件</li>
                <li data-pdf-index="1" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">资格证明文件</li>
            </ol>
            <div id="view-pdf-div" class="cont-text">
                <#--是否帮助按钮-->
                <#assign showHelpBtn="true"/>
                <#--是否启用本地缓存机制-->
                <#assign localCache="true"/>
                <#--是否开启另存为按钮-->
                <#assign showSaveAs="false"/>
                <#--是否开启另存为按钮-->
                <#assign fullScreen="false"/>
                <#--1.必须存在class 为(pdf-file-list文件)-->
                <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
                <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
                <#include "${ctx}/common/showPDFView.ftl"/>
            </div>
        </div>
    </div>
</div>

<script>

    $(function () {
        selectThis(null, null);
    });

    function selectThis(obj, bidderId) {
        if (isNull(obj)){
            obj = $(".bidderUl li").eq(0);
        }
        if (isNull(bidderId)){
            bidderId = '${bidders[0].id}'
        }

        $(obj).siblings().removeClass("sele");
        $(obj).addClass("sele");
        $(".bidders-list").val(bidderId);
        changBidderView();
    }
</script>
</body>
</html>