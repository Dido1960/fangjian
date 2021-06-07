<script type="text/javascript" src="${ctx}/js/base64.js"></script>
<div id="showPdfView" style="height: 850px; width: 100%">
    <script type="text/javascript" src="${ctx}/js/iWebPDF2018.js"></script>
</div>
<script defer="defer">
    $(function () {
        $("#file-type-li li").bind("click", function(){
            $(this).addClass("choice").siblings().removeClass("choice");
            var fileType = $(this).data("filetype");
            var filePath = $(this).data("filepath");
            var fileId = $(this).data("fileid");
            getEvalFileUrl(fileId, filePath, fileType)
        });
        $("#file-type-li li[data-filetype='2'][data-fileclass='1']").trigger("click");
    })
    /**
     * 利用插件打开需要查看的文件
     * @param fileUrl 文件访问地址
     */
    function viewEvalPdf(fileUrl) {
        for (var i = 0; i < 21; i++) {
            iWebPDF2018.CommandBars.Item(i).Visible = false;
        }
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
        if (addins != null) {
            if (!isNull(fileUrl)) {
                var base64encode_str = _Base64encode(fileUrl);
                var md5_encode_url = substring16str(base64encode_str);
                localStorage.setItem("fileUrl", md5_encode_url);
                var open_status = iWebPDF2018.iWebPDFFun.WebOpenLocalFile("C:\\" + md5_encode_url   );
                if (open_status == 0) {
                    console.log("网络文件")
                    iWebPDF2018.Documents.OpenFromURL(fileUrl);
                } else {
                    console.log("本地文件")
                }
            }else{
                layerError("文件路径不存在！");
                console.log("文件路径不存在open")
            }
        }
    }

    /**
     * 获取PDF对应页码
     */
    function getPageNum() {
        return iWebPDF2018.iWebPDFFun.CurPage;
    }

    /**
     * 通过页面跳转到pdf指定页码
     * @param pageNo 页码
     */
    function gotoPageNum(pageNo) {
        //检测当前查看的对象是否是招标文件，不是提示切换。
        var nowType = $(".tit").children(".choice").attr("data-filetype");
        if (nowType == 1){
            // layerConfirm("当前查看的文件不是投标文件，是否切换为投标文件？",gotoPage(pageNo),null);
            hide_IWeb2018();
            layer.confirm('当前查看的文件不是投标文件，是否切换为投标文件？',{
                btn: ['确定','取消'],
                end:function () {
                    show_IWeb2018();
                }
            }, function(){
                gotoPage(pageNo);
                layer.closeAll();
            }, function(){
                show_IWeb2018();
                layer.closeAll();
            });
        }else {
            iWebPDF2018.iWebPDFFun.CurPage = pageNo;
        }
    }

    function gotoPage(pageNo) {
        show_IWeb2018();
        $(".tit").children("#tenderFile").click();
        iWebPDF2018.iWebPDFFun.CurPage = pageNo;
    }

    /**
     * 获取地址查询其文件服务器访问地址，然后通过金格插件打开
     * @param fileId 文件id
     * @param mark 文件地址
     * @param fileType 文件类型（招标or投标）
     */
    function getEvalFileUrl(fileId, mark, fileType) {
        iWebPDF2018.iWebPDFFun.WebClose();
        if (isNull(fileId) || isNull(mark) || isNull(fileType)) {
            layerError("文件路径不存在！");
            console.log("文件路径不存在in");
            return;
        }
        $.ajax({
            url: "${ctx}/viewPdf/getEvalFileUrl",
            type: "POST",
            cache: false,
            data: {
                "fileId": fileId,
                "mark": mark,
                "fileType": fileType
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrf_header, csrf_token);
            },
            success: function (url) {
                console.log("文件地址：" + url);
                if (isNull(url)) {
                    layerError("文件路径不存在！");
                    console.log("文件路径不存在success");
                } else {
                    viewEvalPdf(url);
                }
            },
            error: function (e) {
                console.error(e);
                layerError("文件打开失败！");
                console.log("文件路径不存在error");
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }
    /**
     * 获取字符串的后16位字符串，不足16位直接返回源字符串
     * @param str
     * @returns {string|*}
     */
    function substring16str(str) {
        var str_length = str.length;
        if (str_length <= 32) {
            return str;
        } else {
            return str.substring(str_length - 32, str_length);
        }
    }
</script>

<script language="javascript" for=iWebPDF2018 event="OnToolsClick(vIndex,vCaption)">
    try {
        console.log('IE编号:' + vIndex + '\n\r' + '条目:' + vCaption + '\n\r' + '请根据这些信息编写按钮具体功能');
    } catch (e) {

    }
</script>
<script language="javascript" for=iWebPDF2018 event="OnDocumentOpen()">
    var encode_url = localStorage.getItem("fileUrl");
    var saveFlag = iWebPDF2018.iWebPDFFun.WebSaveLocalFile("c:\\" + encode_url)
    if (saveFlag == 0) {
        console.log("本地缓存文件" + "c:\\" + encode_url);
    }
</script>
<script>
    //谷歌文件加载完成执行
    function OnDocumentOpen(){
        var encode_url = localStorage.getItem("fileUrl");
        var saveFlag = iWebPDF2018.iWebPDFFun.WebSaveLocalFile("c:\\" + encode_url)
        if (saveFlag == 0) {
            console.log("本地缓存文件" + "c:\\" + encode_url);
        }
    }
</script>
