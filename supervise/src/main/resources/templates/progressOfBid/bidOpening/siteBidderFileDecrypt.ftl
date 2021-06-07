<link rel="stylesheet" href="${ctx}/css/bidderFileDecrypt.css">
<div class="right-document">
    <ol>
        <li style="width: 10%">序号</li>
        <li style="width: 30%" class="long">投标人名称</li>
        <li style="width: 20%">标书状态</li>
        <li style="width: 20%">文件类型</li>
        <li style="width: 20%">操作</li>
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
        <td style="width: 10%">{{=startNum + index+1}}</td>
        <td style="width: 30%" class="long">
            <p>{{bidder.bidderName}}</p>
        </td>
        <td style="width: 20%" class="red-f">&emsp;</td>
        <td style="width: 20%">&emsp;</td>
        <td style="width: 20%; color: rgba(255, 71, 71, 1);">已被标书拒绝</td>
    </tr>
    {{# }else { }}
    <tr>
        <td style="width: 10%">{{=startNum + index+1}}</td>
        <td style="width: 30%" class="long">{{bidder.bidderName||""}}</td>
        {{# if(decryptStatus == 0){ }}
        <td style="width: 20%">未解密</td>
        {{# }else if(decryptStatus == 1) { }}
        <td style="width: 20%" class="passSign">解密成功</td>
        {{# }else { }}
        <td style="width: 20%" class="unSign">解密失败</td>
        {{# } }}

        {{# if(isNull(docType)){ }}
        <td style="width: 20%">未知类型</td>
        {{# }else if(docType == 1) { }}
        <td style="width: 20%">投标文件</td>
        {{# }else { }}
        <td style="width: 20%">备用文件</td>
        {{# } }}
        <td style="width: 20%">
            <span onclick="downloadFile('{{bidder.id}}','{{bidder.bidDocId}}')">标书下载</span>
        </td>
    </tr>

    {{# } }}
    {{# }); }}
    <li></li>
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
            url: '${ctx}/gov/bidOpen/pageSiteTenderDecryptBidders',
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
     * @param bidderId 文件ID
     * @param bidDocId
     */
    function downloadFile(bidderId,bidDocId) {
        if (isNull(bidDocId)){
            window.top.layer.msg("文件不存在!",{icon:2})
            return
        }
        window.open("${ctx}/gov/bidSection/bidderTenderDownloadPage?bidderId="+bidderId);
    }


</script>

