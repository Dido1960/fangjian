<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7">
<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/js/base64.js"></script>
<iframe id="showPdfView" style="width: 100%;height:100%;overflow:hidden;border: 0px" frameborder="0">
</iframe>

<script>
    if (getIEVersion() == 9) {
        console.warn($("#showPdfView").parent().parent().height())
        $("#showPdfView").height($("#showPdfView").parent().parent().height());
        setTimeout(function () {
            initIframe();
        }, 200)
    } else {
        initIframe();
    }

    var initFlag = 0;
    var iWebPDF2018;

    function initIframe() {
        //处理页面F5刷新相关问题
        var iframe = document.getElementById("showPdfView");
        //把缓存给iframe
        iframe.src = "/iweb/iweb2018Page";
        //iframe加载完成后保存iframe的状态值
        iframe.onload = iframe.onreadystatechange = function () {};

        var interval = setInterval(function () {
            if (initFlag != 0) {
                clearInterval(interval);
            } else {
                initFlag++;
                console.log("初始化金格插件开始，10次未初始化成功将不再执行");
                init();
            }
        }, 300)
    }

    var bidder_tenderDoc = {};
    {
        <#if bidders??>
        <#list bidders as bidderTemp>

        bidder_tenderDoc["${bidderTemp.id}"] = "${bidderTemp.bidDocId}";

        </#list>
        </#if>

    }
    var init_count = 0;
    function init() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        if (isNull(iWebPDF2018)) {
            if (init_count < 10) {
                console.log("初始化次数：" + init_count)
                init_count++;
                setTimeout(init,100);
            }
            return;
        }
        init_count = 0;
        var addins = iWebPDF2018.iWebPDFFun;

        if (isNull(addins)) {
            return;
        }

        hide_IWeb2018();
        setTimeout(function () {
            hiddeBtn();
            show_IWeb2018();
        }, 100);
        var mark = $(".pdf-file-list").find(".choice").attr("fdfsMark");
        setTimeout(function(){
            loadPdf(mark)
        },100);
    }

    var nowLoadPdfPath_iweb2018 = "";

    /**
     * 文件保存
     * */
    function downloadPdf() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        var ret = iWebPDF2018.iWebPDFFun.WebSaveLocal();
        if (!ret) {
            addins.Alert("下载失败！");
        } else {
            addins.Alert("下载成功！");
        }
    }

    /**
     *
     * 加载pdf
     * ***/
    function loadPdf(mark) {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        if (isNull(mark)) {
            console.warn("mark is not allow NULL!!!!!!!");
            try {
                iWebPDF2018.Documents.CloseAll(true);
            } catch (e) {
                console.warn("文件关闭失败");
            }
            return;
        }
        var bidderId = $(".bidders-list").val();

        if (mark.indexOf("{bidDocId}") != -1) {
            mark = mark.replace("{bidDocId}", bidder_tenderDoc[bidderId]);
        }


        if (nowLoadPdfPath_iweb2018 == mark) {
            console.info("路径加载一致，无需更新iweb加载" + nowLoadPdfPath_iweb2018);
            return;
        }
        var url;
        $.ajax({
            url: '/fdfs/getMarkUrl',
            type: 'post',
            cache: false,
            data: {
                markUrl: mark
            },
            success: function (data) {
                url = data;
                console.log("获取地址：" + url)

                try {
                    iWebPDF2018.Documents.CloseAll(true);
                } catch (e) {
                    console.warn("文件关闭失败");
                }
                nowLoadPdfPath_iweb2018 = url;
                if (!isNull(url)) {
                    try {
                        if ("${localCache}" === "true") {
                            var base64encode_str = _Base64encode(nowLoadPdfPath_iweb2018);
                            var encode_url = "C:\\" + substring16str(base64encode_str);
                            console.log(encode_url);
                            var open_status = iWebPDF2018.iWebPDFFun.WebOpenLocalFile(encode_url);
                            if (open_status == 0) {
                                console.log("网络文件")
                                iWebPDF2018.Documents.OpenFromURL(url);
                            } else {
                                console.log("本地文件")
                            }
                        } else {
                            console.log("网络文件")
                            iWebPDF2018.Documents.OpenFromURL(url);
                        }

                    } catch (e) {
                        console.warn(e);
                    }
                } else {
                    console.warn("文件路径不存在");
                    return;
                }
            },
            error: function (data) {
                console.warn(data);
                layer.msg("操作失败！")
            },
        });

    }

    /**
     * 全屏查看
     *
     * */
    function fullScreen() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        iWebPDF2018.iWebPDFFun.FullScreen = true
    }


    /**
     * 获取PDF对应页码
     * */
    function getPageNum() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        return iWebPDF2018.iWebPDFFun.CurPage;
    }

    /***
     * 跳转到指定的页码
     * */
    function gotoPageNum(pageNo) {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        iWebPDF2018.iWebPDFFun.CurPage = pageNo;
    }

    /**
     * 隐藏不必要的按钮
     * */
    function hiddeBtn() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        var addins = iWebPDF2018.iWebPDFFun;
        iWebPDF2018.iWebPDFFun.ShowTools = 1;
        iWebPDF2018.iWebPDFFun.ShowMenus = 0;
        iWebPDF2018.iWebPDFFun.CommentWindow(1);
        iWebPDF2018.iWebPDFFun.EnableHandWriter = false;

        //开始页禁用
        iWebPDF2018.iWebPDFFun.StartPageVisible = false;
        //不显示标签页
        iWebPDF2018.iWebPDFFun.ShowTabBarVisible = false;

        //手写签名等
        addins.ShowPostil = false;

        var nClick = 1;
        var nCount = iWebPDF2018.CommandBars.Count;
        nClick++;
        for (var i = 0; i < nCount; i++) {
            if (0 == (nClick % 2)) {
                iWebPDF2018.CommandBars.Item(i).Visible = false;
            } else {
                iWebPDF2018.CommandBars.Item(i).Visible = true;
            }
        }
        iWebPDF2018.CommandBars.Item(20).Visible = true;
        <#if showHelpBtn??&&showHelpBtn="true">
        //菜单栏
        //iWebPDF2018.CommandBars.Item(0).Visible = false;
        //保存 、打印、另存为
        //iWebPDF2018.CommandBars.Item(1).Visible = false;
        //旋转
        iWebPDF2018.CommandBars.Item(2).Visible = true;
        //放大缩小工具
        iWebPDF2018.CommandBars.Item(3).Visible = true;
        // //拖动工具 ，指针
        // iWebPDF2018.CommandBars.Item(4).Visible = true;
        // //查找
        // iWebPDF2018.CommandBars.Item(5).Visible = true;
        //
        // //标记区
        // iWebPDF2018.CommandBars.Item(12).Visible = true;

        {
            // //无用
            // iWebPDF2018.CommandBars.Item(6).Visible = true;
            // //无用
            // iWebPDF2018.CommandBars.Item(7).Visible = true;
            // iWebPDF2018.CommandBars.Item(8).Visible = true;
            // iWebPDF2018.CommandBars.Item(9).Visible = true;
            // iWebPDF2018.CommandBars.Item(10).Visible = true;
            // iWebPDF2018.CommandBars.Item(11).Visible = true;


            // iWebPDF2018.CommandBars.Item(13).Visible = true;
            //iWebPDF2018.CommandBars.Item(14).Visible = true;
            //iWebPDF2018.CommandBars.Item(15).Visible = true;
            // iWebPDF2018.CommandBars.Item(16).Visible = false;

            // iWebPDF2018.CommandBars.Item(17).Visible = true;
            // iWebPDF2018.CommandBars.Item(18).Visible = true;
            //  iWebPDF2018.CommandBars.Item(19).Visible = true;
            //
            //  iWebPDF2018.CommandBars.Item(20).Visible = true;
        }
        </#if>

        if ('${showSaveAs}' == 'true') {
            addins.AppendTools("12", "下载", 1);
        }

        if ('${fullScreen}' == 'true') {
            addins.AppendTools("11", "全屏查看", 15);
        }
        if ('${printPdf}' == 'true') {
            addins.AppendTools("13", "打印", 3);
        }
        if ('${signatureBtn}' == 'true') {
            iWebPDF2018.CommandBars.Item(18).Visible = true;
            addins.AppendTools("10", "保存", 2);
        }
    }

    /**
     * 隐藏签章按钮
     * */
    function hiddeBtn_Sigar() {
        var iWebPDF2018 = document.getElementById("showPdfView")[0].contentWindow.iWebPDF2018;
        iWebPDF2018.CommandBars.Item(21).Visible = false;
    }

    /**
     * 隐藏信息
     * * */
    function hide_IWeb2018() {
        try {
            var iWebPDF2018 = window.iWebPDF2018;
            // 如果直接通过组件获取插件信息获取不到，则通过外层iframe的id获取（注意id必须为showPdfIframe，iframe的id）
            if (isNull(iWebPDF2018)) {
                iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
            }
            var addins = iWebPDF2018.iWebPDFFun;

            if (getIEVersion() >= 9) {
                addins.hide = false;
            } else {
                iWebPDF2018.HidePlugin(false);
            }
        } catch (e) {
            console.log(e)
        }
    }


    /**
     * 显示信息
     * * */
    function show_IWeb2018() {
        try {
            var iWebPDF2018 = window.iWebPDF2018;
            // 如果直接通过组件获取插件信息获取不到，则通过外层iframe的id获取（注意id必须为showPdfIframe，iframe的id）
            if (isNull(iWebPDF2018)) {
                iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
            }
            var addins = iWebPDF2018.iWebPDFFun;
            console.log("hide_IWeb2018" + addins.hide)
            if (getIEVersion() >= 9) {
                addins.hide = true;
            } else {
                iWebPDF2018.HidePlugin(true);
            }

        } catch (e) {
            console.log(e)
        }
    }

    /*
    * 本地文件 读取成功后缓存文件
    * **/
    function localStorageSave() {
        if ("${localCache}" === "true") {
            var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
            var base64encode_str = _Base64encode(nowLoadPdfPath_iweb2018);
            var encode_url = substring16str(base64encode_str);
            var saveFlag = iWebPDF2018.iWebPDFFun.WebSaveLocalFile("c:\\" + encode_url)
            if (saveFlag == 0) {
                console.warn("本地缓存文件(失败)" + nowLoadPdfPath_iweb2018);
            } else {
                console.log("本地缓存文件(成功)" + "c:\\" + encode_url);
                loadPdf(nowLoadPdfPath_iweb2018);
            }
        }
    }

    /***绑定文件加载文件事件**/
    $(function () {
        $(".pdf-file-list li").bind("click", function () {
            $(".pdf-file-list li").removeClass("choice");
            $(this).addClass("choice");
            loadPdf($(this).attr("fdfsMark"));
        });
    })

    //改变投标信息
    function changBidderView() {
        var mark = $(".pdf-file-list").find(".choice").attr("fdfsMark");
        loadPdf(mark);
    }

    /***
     * 跳转至某页
     * @param 跳转至某页
     * ***/
    function goToPage(pageNum, index) {

        if ($(".pdf-file-list").eq(index).hasClass("choice")) {
            var iWebPDF2018 = window.iWebPDF2018;
            iWebPDF2018.iWebPDFFun.CurPage = pageNo;
        } else {
            layerAlertInfo("请将文件切换至【" + $(".pdf-file-list").eq(index).html() + "】视图后跳转");
        }

    }

    //自定义按钮要走
    function OnToolsClick(vIndex, vCaption) {
        //alert('编号:'+vIndex+'\n\r'+'条目:'+vCaption+'\n\r'+'请根据这些信息编写按钮具体功能');
        if (vIndex == 10) {
            /* addins.Alert("测试按钮一事件！");*/
            web_save();
        } else if (vIndex == 11) {
            fullScreen();
        } else if (vIndex == 12) {
            downloadPdf();
        }
        try {
            customOnToolsClick(vIndex, vCaption);
        } catch (e) {
            console.log(e);
        }

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

    /**
     * 文件保存
     * */
    function web_save() {
        var iWebPDF2018 = document.getElementById("showPdfView").contentWindow.iWebPDF2018;
        var record_id = "${fdfs.id}";
        var addins = iWebPDF2018.iWebPDFFun;
        // 必须上传的文件ID
        if (addins != null && !isNull("${fdfs.url}")) {
            addins.WebUrl = "http://" + window.location.host + "/sigar/uploadPdfSignar";
            addins.RECORDID = record_id;
            addins.FILENAME = "${fdfs.name}";
            addins.FILETYPE = ".pdf";
            addins.USERNAME = "${user.username}";
            var ret = addins.WebSave();
            if (!ret) {
                addins.Alert("文档保存失败！");
            } else {
                addins.Alert("保存成功！");
                loadPdf('${fdfs.mark}');
            }
        } else {
            addins.Alert("文档不存在信息！");
        }
    }
</script>



