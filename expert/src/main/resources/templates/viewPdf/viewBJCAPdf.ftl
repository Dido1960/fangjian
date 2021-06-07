<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bjca/base64.js"></script>
<script type="text/javascript" src="${ctx}/js/bjca/PdfSeal.js"></script>
<script type="text/javascript" src="${ctx}/js/json2.js"></script>
<OBJECT id="PdfSealObj" name="PdfSealObj" classid="clsid:1A5F8EA9-5A10-47EF-81E7-DF7AD23955BC" width="100%" height="100%"></OBJECT>
<SCRIPT type="text/javascript" FOR="PdfSealObj" EVENT="PdfSignedEvent(result)" >
    OnPdfSignedEvent(result);
</SCRIPT>

<SCRIPT type="text/javascript" FOR="PdfSealObj" EVENT="PdfRemovedEvent(result)" >
    OnPdfRemovedEvent(result);
</SCRIPT>

<SCRIPT type="text/javascript" FOR="PdfSealObj" EVENT="PdfVerifyEvent(result)" >
    OnPdfVerifyEvent(result);
</SCRIPT>
<script>
    $(function () {
        PdfSealObj.Hide_ToolBar();
    })
    var bidder_tenderDoc = {};
    {
        <#if bidders??>
            <#list bidders as bidderTemp>
            bidder_tenderDoc["${bidderTemp.id}"] = "${bidderTemp.bidDocId}";
            </#list>
        </#if>
    }

    /**
     *
     * 加载pdf
     * ***/
    var now_load_pdf_path_bjca = "";
    function loadBjcaPdf(mark) {
        if (isNull(mark)) {
            console.warn("mark is not allow NULL!!!!!!!");
            return;
        }

        var bidderId = $(".left-list .check").attr("data-bidderid");
        if (mark.indexOf("{bidDocId}") != -1) {
            mark = mark.replace("{bidDocId}", bidder_tenderDoc[bidderId]);
        }
        console.log("mark:" + mark);
        if (now_load_pdf_path_bjca == mark) {
            console.info("路径加载一致，无需更新iweb加载" + now_load_pdf_path_bjca);
            return;
        }

        var url;
        $.ajax({
            url: '${ctx}/fdfs/getMarkUrl',
            type: 'post',
            cache: false,
            data: {
                markUrl: mark
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrf_header, csrf_token);
            },
            success: function (data) {
                url = data;
                console.log("获取地址：" + url)
                try {
                    PdfSealObj.Pdf_Close();
                } catch (e) {
                    console.warn("文件关闭失败");
                }
                now_load_pdf_path_bjca = url;
                if (!isNull(url)) {
                    try {
                        var ret;
                        if ("${localCache}" === "true") {
                            var base64encode_str = _Base64encode(now_load_pdf_path_bjca);
                            var encode_url = "C:\\" + substring16str(base64encode_str);
                            console.log(encode_url);
                            var open_status = PdfSealObj.Pdf_Save_Path(encode_url);
                            if (open_status == 0) {
                                console.log("本地文件")
                                ret = PdfSealObj.Pdf_Open_path(encode_url);
                                if (ret != 0){
                                    alert("打开本地pdf失败，错误码：" + ret);
                                }
                            } else {
                                console.log("网络文件")
                                ret = PdfSealObj.Pdf_Open_path(url);
                                if (ret != 0){
                                    alert("打开在线pdf失败，错误码：" + ret);
                                }
                            }
                        } else {
                            console.log("网络文件")
                            ret = PdfSealObj.Pdf_Open_path(url);
                            if (ret != 0){
                                alert("打开在线pdf失败，错误码：" + ret);
                            }
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
</script>
