<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/css/clarifyFile.css">
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
    <style>
        .cont table thead tr th {
            text-align: center;
        }
        .head p {
            white-space: pre-wrap;
            word-wrap: break-word;
            word-break: break-all;
            text-align: center;
            margin-bottom: 20px;
            font-size: 24px;
        }
    </style>
    <script>
        /**
         * 下载或预览
         */
        function previewFile(id) {
            window.top.layer.open({
                type: 2,
                title: ['回退申请表预览', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
                shadeClose: true,
                area: ['40%', '100%'],
                btn: ['打印','关闭'],
                resize: false,
                move: false,
                offset: 'r',
                content: "${ctx}/evalBack/previewBackFilePage?freeBackApplyId=" +id,
                btn1: function (index, layero) {
                    var body = window.top.layer.getChildFrame('body', index);
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.printBackApplyFile();
                },
                btn2: function (index) {
                    window.top.layer.close(index);
                }
            });
        }
    </script>
</head>
<body>
<div class="cont">
    <div class="head">
        <p title="${bidSection.bidSectionName}回退记录表">${bidSection.bidSectionName}回退记录表</p>
    </div>
    <table class="layui-table">
        <thead>
        <tr>
            <th width="7%">序号</th>
            <th width="10%">申请人</th>
            <th width="33%">回退原因</th>
            <th width="15%">申请时间</th>
            <th width="10%">审核状态</th>
            <th width="15%">审核时间</th>
            <th width="10%">回退申请表</th>
        </tr>
        </thead>
        <tbody>
        <#if freeBackApplies?? && freeBackApplies?size gt 0>
            <#list freeBackApplies as freeBackApplie>
                <tr style="text-align: center">
                    <td>${freeBackApplie_index+1}</td>
                    <td>${freeBackApplie.applyUserName}</td>
                    <td style="text-align: left">${freeBackApplie.reason}</td>
                    <td>${freeBackApplie.applyTime}</td>
                    <td>
                        <#if freeBackApplie.checkStatus?? && freeBackApplie.checkStatus == '0'>
                            <span>未审核</span>
                        <#elseif freeBackApplie.checkStatus == '1'>
                            <span class="green-f">通过</span>
                        <#elseif freeBackApplie.checkStatus == '2'>
                            <span class="red-f">驳回</span>
                        <#else>
                            <span>--</span>
                        </#if>
                    </td>
                    <td>${freeBackApplie.checkTime}</td>
                    <td>
                        <span class="layui-btn blove-b" onclick="previewFile(${freeBackApplie.id},1)">预览</span>
                    </td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td colspan="7" style="text-align: center; font-size: 24px; font-weight: bold;">暂无回退申请记录</td>
            </tr>
        </#if>
        </tbody>
    </table>
</div>
</body>