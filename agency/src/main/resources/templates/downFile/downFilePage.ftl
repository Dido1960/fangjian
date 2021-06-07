<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>甘肃省公路工程电子开标辅助系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/downFiles.css">
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
    <script src="${ctx}/js/base64.js"></script>
</head>

<body>
<#include "${ctx}/common/baseTitile.ftl"/>
<section>
    <div class="head"><p>${bidSection.bidSectionName}</p></div>
    <ol class="select">
        <li id="project" class="sele">项目文件</li>
        <li>投标文件</li>
    </ol>
    <div class="project">
        <div data-downurl="${tenderPdf.url}" data-filename="招标文件.${tenderPdf.suffix}">
            <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
            <p>招标文件</p>
            <span> <img src="${ctx}/img/down_see.png" alt="">预览</span>
        </div>
<#--        <#if !(bidSection.paperEval?? && bidSection.paperEval == "1")>-->
<#--            <div data-downurl="${tenderZjf.url}" data-filename="招标文件.${tenderZjf.suffix}">-->
<#--                <img src="${ctx}/img/down_gef.png" title="点击图标下载" alt="">-->
<#--                <p>招标文件</p>-->
<#--            </div>-->
<#--        </#if>-->
        <#if clarifyAnswer??>
            <div data-downurl="${clarifyAnswer.fdfs.url}" data-filename="澄清文件.${clarifyAnswer.fdfs.suffix}">
                <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
                <p>澄清文件</p>
                <span><img src="${ctx}/img/down_see.png" alt="">预览</span>
            </div>
        </#if>
        <#if recordTable??>
            <div data-downurl="${recordTable.url}" data-filename="开标记录表.${recordTable.suffix}">
                <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
                <p>开标记录表</p>
                <span> <img src="${ctx}/img/down_see.png" alt="">预览</span>
            </div>
        </#if>
    </div>
    <div class="tender" style="display: none;">
        <#list downBidderFileDTOS as downBidderFileDTO>
            <div>
                <h3>${downBidderFileDTO_index +1}、${downBidderFileDTO.bidderName}</h3>
                <ul>
                    <#if bidSection.paperEval?? && bidSection.paperEval == "1">
                        <#if downBidderFileDTO.paperBidderPdf??>
                            <li data-downurl="${downBidderFileDTO.paperBidderPdf.url}"
                                data-filename="投标文件.${downBidderFileDTO.paperBidderPdf.suffix}">
                                <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
                                <p>投标文件</p>
                                <div class="tender-btn">
                                    <img src="${ctx}/img/down_see.png" alt="">预览
                                </div>
                            </li>
                        </#if>
                    <#else>
                        <#if downBidderFileDTO.businessFilePdf??>
                            <li data-downurl="${downBidderFileDTO.businessFilePdf.url}"
                                data-filename="商务标文件.${downBidderFileDTO.businessFilePdf.suffix}">
                                <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
                                <p>商务标文件</p>
                                <div class="tender-btn">
                                    <img src="${ctx}/img/down_see.png" alt="">预览
                                </div>
                            </li>
                        </#if>
                        <#if downBidderFileDTO.technicalFilePdf??>
                            <li data-downurl="${downBidderFileDTO.technicalFilePdf.url}"
                                data-filename="技术标文件.${downBidderFileDTO.technicalFilePdf.suffix}">
                                <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
                                <p>技术标文件</p>
                                <div class="tender-btn">
                                    <img src="${ctx}/img/down_see.png" alt="">预览
                                </div>
                            </li>
                        </#if>
                        <#if downBidderFileDTO.qualificationFilePdf??>
                            <li data-downurl="${downBidderFileDTO.qualificationFilePdf.url}"
                                data-filename="资格证明文件.${downBidderFileDTO.qualificationFilePdf.suffix}">
                                <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
                                <p>资格证明文件</p>
                                <div class="tender-btn">
                                    <img src="${ctx}/img/down_see.png" alt="">预览
                                </div>
                            </li>
                        </#if>
                        <#if downBidderFileDTO.engineerQuantityListPdf??>
                            <li data-downurl="${downBidderFileDTO.engineerQuantityListPdf.url}"
                                data-filename="工程量清单文件.${downBidderFileDTO.engineerQuantityListPdf.suffix}">
                                <img src="${ctx}/img/down_pdf.png" title="点击图标下载" alt="">
                                <p>投标工程量清单</p>
                                <div class="tender-btn">
                                    <img src="${ctx}/img/down_see.png" alt="">预览
                                </div>
                            </li>
                        </#if>
                    </#if>

<#--                    <#if bidSection.paperEval?? && bidSection.paperEval == "1">-->
<#--                        <#if downBidderFileDTO.gefFdfs??>-->
<#--                            <li data-downurl="${downBidderFileDTO.gefFdfs.url}" data-filename="${downBidderFileDTO.gefFdfs.name}">-->
<#--                                <img src="${ctx}/img/down_yck.png" title="点击图标下载" alt="">-->
<#--                                <p>投标文件</p>-->
<#--                                <span></span>-->
<#--                            </li>-->
<#--                        </#if>-->
<#--                        <#if downBidderFileDTO.sgefFdfs??>-->
<#--                            <li data-downurl="${downBidderFileDTO.sgefFdfs.url}" data-filename="${downBidderFileDTO.sgefFdfs.name}">-->
<#--                                <img src="${ctx}/img/down_syck.png" title="点击图标下载" alt="">-->
<#--                                <p>备用文件</p>-->
<#--                                <span></span>-->
<#--                            </li>-->
<#--                        </#if>-->
<#--                    <#else>-->
<#--                        <#if downBidderFileDTO.gefFdfs??>-->
<#--                            <li data-downurl="${downBidderFileDTO.gefFdfs.url}" data-filename="${downBidderFileDTO.gefFdfs.name}">-->
<#--                                <img src="${ctx}/img/down_gef.png" title="点击图标下载" alt="">-->
<#--                                <p>投标文件</p>-->
<#--                                <span></span>-->
<#--                            </li>-->
<#--                        </#if>-->
<#--                        <#if downBidderFileDTO.sgefFdfs??>-->
<#--                            <li data-downurl="${downBidderFileDTO.sgefFdfs.url}" data-filename="${downBidderFileDTO.sgefFdfs.name}">-->
<#--                                <img src="${ctx}/img/down_sgef.png" title="点击图标下载" alt="">-->
<#--                                <p>投标文件</p>-->
<#--                                <span></span>-->
<#--                            </li>-->
<#--                        </#if>-->
<#--                    </#if>-->

<#--                    <#if downBidderFileDTO.czrFdfs??>-->
<#--                        <li data-downurl="${downBidderFileDTO.czrFdfs.url}" data-filename="${downBidderFileDTO.czrFdfs.name}">-->
<#--                            <img src="${ctx}/img/down_czr.png" title="点击图标下载" alt="">-->
<#--                            <p>存证文件</p>-->
<#--                            <span></span>-->
<#--                        </li>-->
<#--                    </#if>-->
                </ul>
            </div>
        </#list>
    </div>
</section>
</body>
<script>
    $('.select').on('click', 'li', function () {
        $(this).addClass('sele').siblings().removeClass('sele')
        if ($(this).attr('id') == 'project') {
            $('.project').show()
            $('.tender').hide()
        } else {
            $('.project').hide()
            $('.tender').show()
        }
    })

    /**
     * 给投标文件绑定下载事件
     */
    $(".tender ul li img").bind("click", function (uri) {
        var url = $(this).parent().data("downurl");
        var downName = $(this).parent().data("filename");
        var newUrl =url+"?filename="+encodeURI(downName);
        window.location.href = newUrl;
    })

    /**
     * 给投标文件（pdf）绑定预览事件
     */
    $(".tender ul li div[class='tender-btn']").bind("click", function () {
        var url = $(this).parent().data("downurl");
        window.location.href = "${fileViewAddress}?url=" + encodeURI(url);
    })

    /**
     * 给项目文件绑定下载事件
     */
    $(".project div img").bind("click", function (uri) {
        var url = $(this).parent().data("downurl");
        var downName = $(this).parent().data("filename");
        var newUrl =url+"?filename="+encodeURI(downName);
        window.location.href = newUrl;
    })

    /**
     * 给项目文件（pdf）绑定预览事件
     */
    $(".project div span").bind("click", function () {
        var url = $(this).parent().data("downurl");
        window.location.href = "${fileViewAddress}?url=" + encodeURI(url);
    })

</script>
</html>