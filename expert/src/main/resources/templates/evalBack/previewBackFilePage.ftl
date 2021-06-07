<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
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
    <script src="${ctx}/js/LodopFuncs.js"></script>
    <style>
        body .cont-div {
            width: 98%;
            height: 100%;
        }
        .cont-head {
            width: 100%;
            height: 60px;
            line-height: 60px;
            text-align: center;
            font-size: 26px;
            font-family: '微软雅黑';
        }
        .cont-div table {
            width: 100%;
            border-right: 1px solid #000;
            border-bottom: 1px solid #000;
            margin: 10px 10px;
            font-size: 14px;
            line-height: 30px;
            text-indent: 0.5em;
            font-family: '微软雅黑';
        }
        .cont-div table tr td {
            border-left: 1px solid #000;
            border-top: 1px solid #000;
        }
        .cont-div table tr td:nth-child(1) {
            text-align: center;
        }
        .signature {
            position: relative;
            left: 0;
            top: -50px;
            width: 150px;
        }
        .signature div:nth-child(2) {
            position: relative;
            left: 353px;
            top: 120px;
            width: 150px;
        }
    </style>
</head>
<body>
<div class="cont-div">
    <div class="cont-head">房建市政电子评标回退申请表</div>
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td width="20%">项目名称</td>
            <td colspan="3">${tenderProject.tenderProjectName}</td>
        </tr>
        <tr>
            <td>标段名称</td>
            <td colspan="3">${bidSection.bidSectionName}</td>
        </tr>
        <tr>
            <td>标段编号</td>
            <td colspan="3">${bidSection.bidSectionCode}</td>
        </tr>
        <tr>
            <td>评标进展环节</td>
            <td>${freeBackApply.beforeStepName}</td>
            <td>申请回退环节</td>
            <td>${freeBackApply.stepName}</td>
        </tr>
        <tr>
            <td>回退申请人</td>
            <td>${freeBackApply.applyUserName}</td>
            <td>回退时间</td>
            <td>${freeBackApply.applyTime}</td>
        </tr>
        <tr height="200px;">
            <td style="min-height:200px;">回退原因</td>
            <td colspan="3" style="min-height:200px; text-align: left; text-indent: 2em;padding: 0 5px;">${freeBackApply.reason}</td>
        </tr>
        <tr height="200px;">
            <td height="200px;">评标委员会组长确认</td>
            <td colspan="3" height="200px;">
                <div class="signature">
                    <div>签字:</div>
                    <div>
                        <div>${now.year}年${now.monthValue}月${now.dayOfMonth}日</div>
                    </div>
                </div>
            </td>
        </tr>
        <tr height="200px;">
            <td height="200px;">评标委员会成员确认</td>
            <td colspan="3" height="200px;">
                <div class="signature">
                    <div>
                        <div>签字:</div>
                    </div>
                    <div>
                        <div>${now.year}年${now.monthValue}月${now.dayOfMonth}日</div>
                    </div>
                </div>
            </td>
        </tr>
    </table>
</div>
<script>
    function printBackApplyFile() {
        var LODOP = getLodop();
        LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.documentElement.innerHTML);
        LODOP.PREVIEW();
        parent.layer.closeAll();
    }
</script>
</body>