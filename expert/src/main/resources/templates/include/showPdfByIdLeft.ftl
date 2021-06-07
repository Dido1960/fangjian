<body style="margin: 0px;border: 0px">
<div id="showPdfView" style="width: 100%;height:100vh;margin: 0px;padding: 0px;">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/js/iWebPDF2018.js"></script>
</div>
<script defer="defer">
    $(function () {
        for (var i = 0; i < 21; i++) {
            iWebPDF2018.CommandBars.Item(i).Visible = false;
        }
        var addins = iWebPDF2018.iWebPDFFun;
        if (addins != null) {
            addins.ShowTabBarVisible = false;
            addins.ShowTools = 0;
        }

        //fdfs 表
        if (isNull("${fdfs.id}")) {
            addins.alert("文件的唯一索引,未定义");
            return;
        }
        if (isNull("${user.username}")) {
            addins.alert("用户未登陆");
            return;
        }
        if (addins != null) {
            if (!isNull("${fdfs.url}")) {
                iWebPDF2018.Documents.OpenFromURL("${fdfs.url}");
            } else {
                addins.alert("文件路径不存在！");
            }
        }
    });

    function openLeftPdfFromURL(url) {
        for (var i = 0; i < 21; i++) {
            iWebPDF2018.CommandBars.Item(i).Visible = false;
        }
        var addins = iWebPDF2018.iWebPDFFun;
        if (addins != null) {
            addins.ShowTabBarVisible = false;
            addins.ShowTools = 0;
        }
        if (addins != null) {
            if (!isNull(url)) {
                iWebPDF2018.Documents.OpenFromURL(url);
            } else {
                addins.alert("文件路径不存在！");
            }
        }
    }
</script>
</body>
