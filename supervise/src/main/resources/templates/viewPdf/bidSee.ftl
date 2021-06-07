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
    <script src=${ctx}"${ctx}/js/html5shiv.min.js"></script>
    <script src=${ctx}"${ctx}/js/respond.min.js"></script>
    <![endif]-->
<#--    <script src="${ctx}/js/common.js"></script>-->
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/bidSee.css">
    <script>

        $(function (){
            // 默认第一家投标人选中
            $(".bidder_list>li").each(function (i){
                if (i===0){
                    $(this).trigger("click");
                }
            })
        })


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

        /**
         * 选择投标人
         */
        function selectBidderId(e) {
            // 记录主键
            $("#bidderId").val( $(e).data("id"));

            // 添加点击样式
            $(e).siblings("li").removeClass();
            $(e).addClass("sele");


            // 投标文件附件主键ID
            var thisBidderFileId = $(e).data("uid");
            if (thisBidderFileId===""){
                $(".pdf-file-list > li").each(function (){
                    if ($(this).hasClass("choice") && $(this).hasClass("tenderFile")) {
                        var newMark = $(this).data("uid");
                        $(this).attr("fdfsMark", newMark);
                    }
                })
            } else {
                $(".pdf-file-list > li").each(function (){
                    if ($(this).hasClass("choice") && $(this).hasClass("tenderFile")) {
                        var newMark = $(this).attr("fdfsMark").replace("{bidDocId}", thisBidderFileId)
                        $(this).attr("fdfsMark", newMark);
                    }
                })
            }

            $('.bidders-list').val($(e).attr("data-id"));
            $('.bidderName').val($(e).attr("title"));
            changBidderView();
            cancelSelect();
        }


        /**
         * 选择当前投标人的标书
         */
        function slectBidder(){
            $(".bidder_list > li").each(function (){
                if ($(this).hasClass("sele")) {
                    $(this).click();
                }
            })
        }
    </script>
</head>
<body>

<div class="cont">
    <#assign bidDocFileId =340>
    <div class="left">
        <h3>投标单位</h3>
        <ul class="bidder_list">
            <#list bidders as bidder>
                <li data-id="${bidder.id}" onclick="selectBidderId(this)" data-uid="${bidder.bidDocId}" title="${bidder.bidderName}">
                    <p>${bidder_index+1}.${bidder.bidderName}</p>
                </li>
            </#list>
        </ul>
    </div>
    <div class="right">
        <ol class="pdf-file-list">
            <li class="choice" fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/TempConvert/temp.pdf">招标文件</li>
            <#--            <li class="choice" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BidContent.pdf">投标文件</li>-->
            <li onclick="slectBidder()" class="tenderFile" data-uid="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">
                资格证明
            </li>
            <li onclick="slectBidder()"  class="tenderFile" data-uid="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf"  fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">
                商务标
            </li>
            <li onclick="slectBidder()" class="tenderFile" data-uid="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">
                技术标
            </li>
        </ol>
        <div class="pdf">
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
    </div>
</div>
</body>
</html>