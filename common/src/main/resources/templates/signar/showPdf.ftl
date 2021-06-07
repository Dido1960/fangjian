<div  id="showPdfView">
    <script type="text/javascript" src="/js/iWebPDF2018.js"></script>
</div>

<script defer="defer">
    $(function () {
        for (var i = 0; i < 20; i++) {
            iWebPDF2018.CommandBars.Item(i).Visible = false;
        }
       var addins = iWebPDF2018.iWebPDFFun;
        if (addins != null) {
            addins.ShowTabBarVisible = false;
            addins.ShowTools = 0;
            //开始页禁用
            addins.StartPageVisible = false;
            //不显示标签页
            addins.ShowTabBarVisible = false;
        }
        //upload_file 表
        if (isNull("${uploadFile.id}")) {
            addins.alert("文件的唯一索引,未定义");
            return;
        }
        if (isNull("${user.username}")) {
            addins.alert("用户未登陆");
            return;
        }
        if (addins != null) {
            if (!isNull("${url}")) {
                addins.WebOpenUrlFile("${url}");
            }else{
                addins.alert("文件路径不存在！");
            }
            setTimeout(function () {
                addins.AppendTools("10", "保存文件", 20);
            }, 200);
        }
    })


    function OnToolsClick(vIndex, vCaption) {
        //alert('编号:'+vIndex+'\n\r'+'条目:'+vCaption+'\n\r'+'请根据这些信息编写按钮具体功能');
        if (vIndex == 10) {
            /* addins.Alert("测试按钮一事件！");*/
            web_save();
        }
    }

    /**
     * 文件保存
     * */
    function web_save() {
        var record_id = "${uploadFile.id}";
       var addins = iWebPDF2018.iWebPDFFun;

        // 必须上传的文件ID
       var param = "${_csrf.headerName}=${_csrf.token}";
        param = param + "&uploadFileId=374";
        if (addins != null) {
            addins.WebUrl = "${url}";
            addins.RECORDID = record_id;
            addins.FILENAME = "${file.name}";
            addins.FILETYPE = ".pdf";
            addins.USERNAME = "${user.username}";
            var ret = addins.WebSave();
            if (!ret) {
                addins.Alert("文档保存失败！");
            } else {
                addins.Alert("保存成功！");
            }
        }
    }


    /**
     * 获取PDF对应页码
     * */
    function getPageNum() {
      return  iWebPDF2018.iWebPDFFun.CurPage;
    }

    /***
     * 跳转到指定的页码
     * */
    function gotoPageNum(pageNo) {
        iWebPDF2018.iWebPDFFun.CurPage = pageNo;
    }
</script>

<script language="javascript" for=iWebPDF2018 event="OnToolsClick(vIndex,vCaption)">
    console.log('IE编号:' + vIndex + '\n\r' + '条目:' + vCaption + '\n\r' + '请根据这些信息编写按钮具体功能');
    if (vIndex == 10) {
        web_save();
    }
</script>
<script language="javascript" for=iWebPDF2018 event="OnMenuClick(vIndex,vCaption)">
    console.log('IE编号:' + vIndex + '\n\r' + '条目:' + vCaption + '\n\r' + '请根据这些信息编写菜单具体功能');
    if (vIndex == 10) {
        alert("自定义按钮事件！");
    }
</script>
