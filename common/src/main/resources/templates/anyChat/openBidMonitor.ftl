<html>
<head>
    <meta charset="utf-8">
    <title>开标音视频</title>
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
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatsdk.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatevent.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/advanceset.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/js/anychat/bigScreenMoniter.js"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/js/dpsdk/dpconf.js"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/js/dpsdk/dpocxfun.js"></script>
    <link type="text/css" href="${ctx}/css/anychat/bigScreenMoniter.css" rel="stylesheet"/>
</head>
<body>
<div class="cenetr-box" id="main">
    <div class="header">
        <img src="/img/anychat/logo.png" class="logo">
        <div class="header-title">
            <p>甘肃交易通电子评标</p>
            <p>远程异地开标室</p>
        </div>
        <div class="header-lit-title">
            <p>标段名称: ${bidSection.bidSectionName}</p>
            <p>标段编号：${bidSection.bidSectionCode}</p>
            <p>主场：${homeReg.regName}公共资源交易中心${homeOpenSite.name}</p>
        </div>
    </div>
    <div class="bottom-box">
        <object id="DPSDK" classid="CLSID:D3E383B6-765D-448D-9476-DFD8B499926D" style="width: 100%; height: 100%" codebase="DpsdkOcx.cab#version=1.0.0.0"></object>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        // 加载主场开标室监控
        initDPSDK("DPSDK", "${homeReg.regNo}", "${homeOpenSite.name}");
    })
</script>
</body>
</html>
