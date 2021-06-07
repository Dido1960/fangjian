<link rel="stylesheet" href="${ctx}/css/identityCheck.css">
<style>
    <#if bidSection.bidClassifyCode == 'A10'>
    .right-document ol li ,.document-table table td{
        width: 15%;
    }

    </#if>
</style>
<div class="right-document">
    <ol>
        <li>序号</li>
        <li class="long">投标人名称</li>
        <li>文件递交</li>
        <li>身份检查</li>
        <#if bidSection.bidClassifyCode != 'A10'>
            <li>保证金缴纳</li>
        </#if>
        <li>检查结果</li>
        <li>授权委托书</li>
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
    var marginPayStatus = bidder.bidderOpenInfo.marginPayStatus;
    var checkResult = true;
    }}
    <tr>
        <td>{{=startNum+index+1}}</td>
        <td class="long">{{bidder.bidderName}}</td>

        {{# if(!(isNull(bidder.bidderOpenInfo.notCheckin) || bidder.bidderOpenInfo.notCheckin == 3)){
        checkResult = false;
        }}
        <td class="unSign">未递交</td>
        {{# }else { }}
        <td>已递交</td>
        {{# } }}

        {{# if(bidder.bidderOpenInfo.bidderIdentityStatus != 0){ }}
        <td>符合</td>
        {{# }else {
        checkResult = false;
        }}
        <td class="unSign">不符合</td>
        {{# } }}

        <#if bidSection.bidClassifyCode != 'A10'>
            {{# if(isNull(marginPayStatus) || marginPayStatus == 0){
            checkResult = false;
            }}
            <td class="unSign">未缴纳</td>
            {{# }else { }}
            <td>{{bidder.bidderOpenInfo.marginPay}}</td>
            {{# } }}
        </#if>

        {{# if(checkResult){ }}
        <td>检测通过</td>
        {{# }else { }}
        <td class="unSign">检测未通过</td>
        {{# } }}

        <td>
            <span onclick="clientCheckOnePage('{{bidder.bidderOpenInfo.id}}',true)">查看</span>
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
     * @param pageSize
     */
    function getBiddersList(curr, pageSize) {
        var indexLoad = window.top.layer.load();
        $.ajax({
            url: '${ctx}/gov/bidOpen/pageAllBidders',
            type: 'post',
            cache: false,
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
     * 开始检查
     * 跳转检查页面
     */
    function clientCheckOnePage(id, isShow) {

        $.ajax({
            url: "${ctx}/gov/bidOpen/clientCheckShowPage",
            type: "POST",
            cache: false,
            async: true,
            data: {
                "bidderIdTo": id,
                "isShow": isShow
            },
            dataType: "html",
            success: function (data) {
                lock = false;
                if (data)
                    layer.open({
                        type: 1,
                        offset: 'auto',
                        id: "client001",
                        title: ['授权委托书查看', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                        content: data,
                        area: ['1000px', '860px'],
                        shade: 0.3, //不显示遮罩
                        resize: false, //不允许拖拽
                        move: false, //不允许移动
                        end: function (index, layero) {
                            layer.close(index);
                            if (!isShow) {
                                goToUrl('${ctx}/bidOpening/identityCheckPage', $("#checkLi"))
                            }
                        }
                    });
            },
            error: function (e) {
                lock = false;
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });

    }
</script>