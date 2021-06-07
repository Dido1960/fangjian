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
    <link rel="stylesheet" href="${ctx}/css/publishBidder.css">

</head>
<body>
<div class="head">当前共有投标人： ${bidderCount}家
    <span onclick="window.top.switchPublishBidderStatus(1)">重新检查</span>
</div>
<div class="caption">
    <li style='width: 10%'>序号</li>
    <li style='width: 30%'>投标人名称</li>
    <li style='width: 30%'>文件上传情况</li>
    <li style='width: 30%'>未递交原因</li>
</div>
<div class="content-box">
    <ul class="sects">
        <#if allBidders??>
            <#list allBidders as bidder>
                <li>
                    <#--投标人主键id -->
                    <input type="hidden" name="biderId" value="${bidder.id!}">
                    <div class="index" style="width: 10%">
                        <p>${bidder_index + 1}</p>
                    </div>
                    <div class="name" style="width: 30%">
                        <p>${bidder.bidderName!}</p>
                    </div>
                    <div class="case" style="width: 30%">
                        <#if bidder.bidDocId?if_exists>
                            <span class="green"  >已上传</span>
                        <#else >
                            <span class="red"  >未上传</span>
                        </#if>
                    </div>

                    <div class="case" style="width: 30%">
                        <#assign notCheckin = bidder.bidderOpenInfo.notCheckin>
                        <#if notCheckin == 3>
                            <span class="green" style="border: none">已递交</span>
                        <#elseif notCheckin == 1>
                            <span class="red" style="border: none">迟到</span>
                        <#elseif notCheckin == 2>
                            <span class="red" style="border: none">弃标</span>
                        <#elseif notCheckin == 9>
                            <div class="red-f">
                                <p style="text-align: center">${bidder.bidderOpenInfo.notCheckinReason!}</p>
                            </div>
                        <#else >
                            <span class="yellow" style="border: none">未确认</span>
                        </#if>
                    </div>

                </li>
            </#list>

        </#if>

    </ul>
</div>


<!--保存总记录数-->
<input type="hidden" id="bidder_total">
<#--设置自定义分页-->
<div id="page-temp"></div>

<div class="kong"></div>
<div class="tan" style="display: none; z-index: 99999;">
    <form action="">
        <input type="radio" name="why" value="1">迟到
        <input type="radio" name="why" value="2">弃标
        <input type="radio" name="why" value="9" checked>其他
    </form>
    <!-- <div class="foot">
        <span class="yes">确定</span>
        <span class="no">取消</span>
    </div> -->
</div>
</body>
