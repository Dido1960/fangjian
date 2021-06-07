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
    <link rel="stylesheet" href="${ctx}/css/downloadFiles.css">
</head>

<body>
<div class="cont">
    <ol class="download">
        <li class="sele" id="project">项目文件</li>
        <li>投标文件</li>
    </ol>
    <div class="project">
        <#include "${ctx}/progressOfBid/projectInfo/downloadProjectFiles.ftl"/>
    </div>
    <div class="tender" style="display: none;">
        <#include "${ctx}/progressOfBid/projectInfo/downloadBidFiles.ftl"/>
    </div>
</div>
</body>
<script>
    // 项目列表页面文件下载弹窗投标和项目得切换
    $('.download').on('click','li',function(){
        $(this).addClass('sele').siblings().removeClass('sele')
        if($(this).attr('id') === 'project'){
            $('.project').show()
            $('.tender').hide()
        }else{
            $('.project').hide()
            $('.tender').show()
        }
    })

    /**
     * 预览pdf
     * @param url pdf的外网地址
     */
    function showPdf(url) {
        window.top.layer.open({
            type: 2,
            title: 'PDF文件预览',
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: 0,
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/gov/bidSection/previewPDFPage?url="+url,
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 文件下载
     * @param url 文件的外网地址
     */
    function downloadFile(url,name){
        var newUrl = url + "?filename=" + encodeURI(name);
        window.location.href = encodeURI(newUrl, "utf8");
    }

</script>
</html>