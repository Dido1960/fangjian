<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
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
    <link rel="stylesheet" href="${ctx}/css/clarifyFile.css">
</head>
<body>
<div class="cont">
    <div class="cont-left">
        <h3>澄清文件</h3>
        <ul class="clarify-file-ul">
            <#if clarifyAnswers?? && clarifyAnswers?size gt 0>
                <#list clarifyAnswers as clarifyAnswer>
                    <li title="${clarifyAnswer_index + 1}.${clarifyAnswer.fdfs.name?replace(".pdf", "")?replace(".PDF", "")},上传时间：${clarifyAnswer.insertTime}" data-clarifyurl="${clarifyAnswer.fdfs.url}" <#if clarifyAnswer_index == 0>class="sele"</#if>>${clarifyAnswer_index + 1}.${clarifyAnswer.fdfs.name?replace(".pdf", "")?replace(".PDF", "")}</li>
                </#list>
            <#else>
                <li data-clarifyurl="" class="sele">未上传澄清文件</li>
            </#if>
        </ul>
    </div>
    <div id="showPdfView" class="cont-right">
        <script type="text/javascript" src="${ctx}/js/iWebPDF2018.js"></script>
        <script defer="defer">
            $(function () {
                hideBtns();
                $(".clarify-file-ul li").bind("click", function(){
                    $(this).addClass("sele").siblings().removeClass("sele");
                    var clarifyurl = $(this).data("clarifyurl");
                    if (!isNull(clarifyurl)) {
                        iWebPDF2018.Documents.OpenFromURL(clarifyurl)
                    }
                });
                $(".clarify-file-ul li[class='sele']").trigger("click");
            })
            /**
             * 禁用按钮
             */
            function hideBtns() {
                for (var i = 0; i < 21; i++) {
                    iWebPDF2018.CommandBars.Item(i).Visible = false;
                }
                // 设置验签功能可用
                iWebPDF2018.CommandBars.Item(20).Visible = true;
                var addins = iWebPDF2018.iWebPDFFun;
                if (addins != null) {
                    addins.ShowTools = 0;
                    addins.ShowMenus = 0;
                    addins.CommentWindow(1);
                    addins.EnableHandWriter = false;
                    addins.PrintRight = 0;

                    //开始页禁用
                    addins.StartPageVisible = false;
                    //不显示标签页
                    addins.ShowTabBarVisible = false;
                }
            }
        </script>
    </div>
</div>
</body>