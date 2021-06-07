<link rel="stylesheet" href="${ctx}/css/bidderFileDecrypt.css">
<div class="right-document">
    <ol>
        <li>序号</li>
        <li class="long">投标人名称</li>
        <li>标书状态</li>
        <li>解密时间</li>
        <li>解密用时</li>
        <li>文件类型</li>
        <li>操作</li>
    </ol>
    <div class="document-table">
        <table cellpadding=0 cellspacing=0 id="biddersContent">

        </table>
    </div>
    <div class="foot" id="footPager" style="position: relative">
        <#--设置自定义分页-->
        <!--保存总记录数-->
        <input type="hidden" id="count">
        <div class="foot" id="page-temp" style="position: relative;"></div>
    </div>
    <input id="curPage" type="hidden" value="1"/>
    <input id="pageSize" type="hidden" value="5"/>
</div>

<#--模板。 -->
<script id="biddersTemplate" type="text/html">
    {{# var startNum= ($("#curPage").val()-1)*($("#pageSize").val())}}
    {{#  layui.each(d, function(index, bidder){
    var decryptStatus = bidder.bidderOpenInfo.decryptStatus;
    var docType = bidder.bidDocType;
    }}
    {{# if(bidder.bidderOpenInfo.tenderRejection == 1 && bidder.isPassFirstOpen != 1){ }}
    <tr>
        <td>{{=startNum + index+1}}</td>
        <td class="long">
            <p>{{bidder.bidderName}}</p>
        </td>
        <td class="red-f">&emsp;</td>
        <td>&emsp;</td>
        <td>&emsp;</td>
        <td>&emsp;</td>
        <td style="color: rgba(255, 71, 71, 1);">已被标书拒绝</td>
    </tr>
    {{# }else { }}
    <tr>
        <td>{{=startNum + index+1}}</td>
        <td class="long">{{bidder.bidderName||""}}</td>
        {{# if(decryptStatus == 0){ }}
        <td>未解密</td>
        {{# }else if(decryptStatus == 1) { }}
        <td class="passSign">解密成功</td>
        {{# }else { }}
        <td class="unSign">解密失败</td>
        {{# } }}

        <td>{{bidder.bidderOpenInfo.decryptStartTime||"-"}}</td>
        <td>{{bidder.decryptTimeMinute||0}}分{{bidder.decryptTimeSecond||0}}秒</td>

        {{# if(isNull(docType)){ }}
        <td>未知类型</td>
        {{# }else if(docType == 1) { }}
        <td>投标文件</td>
        {{# }else { }}
        <td>备用文件</td>
        {{# } }}

        <td>
            <span onclick="downloadFile('{{bidder.id}}','{{bidder.bidDocId}}')">标书下载</span>
        </td>
    </tr>

    {{# } }}
    {{# }); }}
</script>

<script>
    $(function () {
        //分页展示
        pageShowBidders();
    });

    /**
     *   数据分页
     */
    function pageShowBidders() {
        laypage.render({
            elem: 'page-temp',
            curr: 1,//当前页
            limit: 5,//页大小
            count: ${biddersCount},
            prev: "<i class='layui-icon layui-icon-left'></i>",//上一页图标
            next: "<i class='layui-icon layui-icon-right'></i>",//上一页图标
            limits: [5, 10, 15, 20],
            groups: 5,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
            page: true,
            jump: function (obj, first) {
                var curr = obj.curr;
                var pageSize = obj.limit;
                $("#curPage").val(curr);
                $("#pageSize").val(pageSize);
                // 展示当前页数据
                getBiddersList(curr, pageSize);
                //首次不执行
                if (!first) {
                    // 页面加载时，显示全部
                }
            }
        });
    }

    /**
     * 获取数据并渲染(条件查询)
     * @param curr 页码
     * @param pageSize
     */
    function getBiddersList(curr, pageSize) {
        var indexLoad = window.top.layer.load();
        $.ajax({
            url: '${ctx}/gov/bidOpen/pageTenderDecryptBidders',
            type: 'post',
            cache: false,
            async: true,
            data: {
                page: curr,
                limit: pageSize
            },
            success: function (data) {
                window.top.layer.close(indexLoad)
                if (!isNull(data)) {
                    $(".layui-laypage-count").text(" 共 ${biddersCount} 条 ");
                    // 获取模版
                    var getTpl = biddersTemplate.innerHTML;
                    var view = document.getElementById("biddersContent");
                    // 渲染模版
                    laytpl(getTpl).render(data, function (html) {
                        view.innerHTML = html;
                    });
                }
            },
            error: function (data) {
                console.error(data);
                window.top.layer.close(indexLoad)
            },
        });

    }

    /**
     * 标书下载
     * @param bidderId 投标人id
     * @param bidDocId
     */
    function downloadFile(bidderId, bidDocId) {
        if (isNull(bidDocId)) {
            window.top.layer.msg("文件不存在!", {icon: 2})
            return;
        }
        window.open("${ctx}/gov/bidSection/bidderTenderDownloadPage?bidderId=" + bidderId);


        <#--var indexLoad = layer.load();-->
        <#--//文件命名-->
        <#--var name = "tenderFile" + bidderId + ".pdf";-->
        <#--request = new XMLHttpRequest();-->
        <#--request.open("GET", "${ctx}/gov/downloadTender?bidderId=" + bidderId);-->
        <#--request.responseType = "blob";-->

        <#--request.onload = function () {-->
        <#--    if (request.status === 200) {-->
        <#--        layer.close(indexLoad);-->
        <#--        var blob = this.response;-->
        <#--        //判断是否是IE浏览器(兼容IE)-->
        <#--        if (window.navigator.msSaveBlob) {-->
        <#--            try {-->
        <#--                window.navigator.msSaveBlob(blob, name)-->
        <#--            } catch (e) {-->
        <#--                console.log(e);-->
        <#--            }-->
        <#--        } else {-->
        <#--            var url = window.URL.createObjectURL(blob);-->
        <#--            console.log(url);-->
        <#--            var a = document.createElement("a");-->
        <#--            document.body.appendChild(a);-->
        <#--            a.href = url;-->
        <#--            a.download = name;-->
        <#--            a.click();-->
        <#--        }-->
        <#--    } else if (request.status === 500) {-->
        <#--        layer.msg("文件服务器异常，请联系管理员", {icon: 2})-->
        <#--        layer.close(indexLoad);-->
        <#--    } else {-->
        <#--        layer.close(indexLoad);-->
        <#--        layer.msg("下载失败！", {icon: 5});-->
        <#--    }-->

        <#--}-->
        <#--request.send();-->

    }

    /**
     * 文件是否上传完成
     * @param bidderId 投标人id
     */
    function fileUploadComplete(bidderId) {
        var isAllFileUploadOk = false;
        $.ajax({
            url: '${ctx}/gov/bidSection/getAllFileStatus',
            type: 'post',
            cache: false,
            async: false,
            data: {bidderId: bidderId},
            success: function (data) {
                isAllFileUploadOk = data;
            }
        });
        return isAllFileUploadOk;
    }


</script>

