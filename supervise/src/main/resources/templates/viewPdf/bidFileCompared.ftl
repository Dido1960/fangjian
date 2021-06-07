<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>文件对比</title>
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
</head>
<body>
<div class="box">
    <div class="left topDiv" >
        <iframe src="${ctx}/gov/viewPdf/bidViewPage" frameborder="0" style="border: 0px;height: 100%;width: 100%"></iframe>
    </div>
    <div class="right topDiv">
        <iframe src="${ctx}/gov/viewPdf/bidViewPage" frameborder="0" style="border: 0px;height: 100%;width: 100%"></iframe>
    </div>
</div>

</body>
</html>