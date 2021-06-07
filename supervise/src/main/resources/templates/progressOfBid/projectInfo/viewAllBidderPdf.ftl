<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>甘肃省电子开标评标平台</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/viewAllBidderPdf.css">
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
<body style="background: #fff">
<div class="box">
    <div class="left">
        <div class="cont" data-envelope="1">
            <h3>投标单位</h3>
            <ul>
                <#list bidders as bidder>
                    <li <#if bidder_index == 0>class="sele"</#if>
                        data-fileid="${bidder.bidFirstDocId}">${bidder_index + 1}.${bidder.bidderName }</li>
                </#list>
            </ul>
        </div>
    </div>
    <div class="right">
        <div class="title" data-envelope="1">
            <span data-filepath="${bidFile}" data-fileclass="1" data-fileid="${tenderDoc.docFileId}">招标文件</span>
            <span data-filepath="${tenderFile}" data-fileclass="2" class="election">投标文件</span>
        </div>
        <div class="title" style="display: none;" data-envelope="2">
            <span data-filepath="${bidFile}" data-fileclass="1" data-fileid="${tenderDoc.docFileId}">招标文件</span>
            <span data-filepath="${tenderFile}" data-fileclass="2" class="election">投标文件</span>
        </div>
        <div id="view-pdf-div" class="document"></div>
    </div>
</div>
</body>
<script>
    $(function () {
        setTimeout(function () {
            loadViewEvalPdf('${bidders[0].id}', '1');
        }, 200)
    })

    /**
     * 加载评标的招投标文件
     * @param bidderId 投标人id
     * @param envelope 信封类型
     */
    function loadViewEvalPdf(bidderId, envelope) {
        $("#view-pdf-div").load("${ctx}/supervise/loadViewEvalPdf",
            {
                'envelope': envelope,
                'bidSectionId': '${bidSection.id}',
                'bidderId': bidderId,
            }, function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
            });
    }
</script>
</html>