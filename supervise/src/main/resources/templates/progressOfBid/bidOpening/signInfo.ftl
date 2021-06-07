<link rel="stylesheet" href="${ctx}/css/signInfo.css">
<div class="right-document">
    <ol>
        <li class="small">序号</li>
        <li>投标单位名称</li>
        <li>签到时间</li>
        <li>文件递交时间</li>
        <li class="small">文件上传凭证</li>
    </ol>
    <div class="document-table">
        <table cellpadding=0 cellspacing=0 id="biddersContent">

        </table>
    </div>
    <#--设置自定义分页-->
    <div class="foot" id="footPager" style="position: relative">
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
    {{#  layui.each(d, function(index, bidder){ }}
    <tr>
        <td class="small">{{=startNum+index+1}}</td>
        <td>{{bidder.bidderName}}</td>
        <td {{#if(isNull(bidder.bidderOpenInfo.signinTime)){ }} class="unSign" {{# } }}>
            {{bidder.bidderOpenInfo.signinTime||"未签到"}}
        </td>
        <td {{#if(isNull(bidder.bidderOpenInfo.upfileTime)){ }} class="unSign" {{# } }}>
            {{bidder.bidderOpenInfo.upfileTime||"未递交"}}
        </td>
        <td class="small">
            <span onclick="showReceipt('{{bidder.receiptFileId}}')">回执单</span>
        </td>
    </tr>
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
     * @param pageSize 每页展示的条数
     */
    function getBiddersList(curr, pageSize) {
        var indexLoad = window.top.layer.load();
        $.ajax({
            url: '${ctx}/gov/bidOpen/pageAllBidders',
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
     * 查看回执单
     */
    function showReceipt(id) {
        if (isNull(id)) {
            layer.msg("文件不存在", {icon: 5});
            return;
        }
        layer.open({
            type: 2,
            title: '回执单查看',
            shadeClose: true,
            area: ['40%', '100%'],
            offset: 'rb',
            //btn: ['确认'],
            content: '${ctx}/gov/bidOpen/reciptPage?id=' + id,
        });
    }

</script>