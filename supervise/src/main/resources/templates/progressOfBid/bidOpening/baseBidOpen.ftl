<link rel="stylesheet" href="${ctx}/css/bidOpenFlow.css">
<div class="cont">
    <div class="left">
        <ul>
            <#if bidSection.bidOpenOnline == 1>
               <li class="choice" data-flowname="signInfo" data-img="left_10"
                   onclick="showInfo('${ctx}/gov/bidOpen/signInfoPage', this)">
                   <a href="#">
                       <img src="${ctx}/img/sign_1_sele.png" alt="">签到信息
                       <i></i>
                   </a>
               </li>
               <li data-flowname="identityCheck" data-img="left_10"
                   onclick="showInfo('${ctx}/gov/bidOpen/identityCheckPage', this)">
                   <a href="#">
                       <img src="${ctx}/img/sign_2.png" alt="">身份检查
                       <i></i>
                   </a>
               </li>
            </#if>
            <#if bidSection.bidClassifyCode == 'A08'>
                <li data-flowname="bidControlPrice" data-img="left_3"
                    onclick="showInfo('${ctx}/gov/bidOpen/bidControlPricePage', this)">
                    <a href="#">
                        <img src="${ctx}/img/sign_5.png" alt="">招标控制价
                        <i></i>
                    </a>
                </li>
            </#if>
            <#if bidSection.bidClassifyCode == 'A12'>
                <li data-flowname="bidControlPrice" data-img="left_3"
                    onclick="showInfo('${ctx}/gov/bidOpen/bidControlPricePage', this)">
                    <a href="#">
                        <img src="${ctx}/img/sign_5.png" alt="">最高投标限价
                        <i></i>
                    </a>
                </li>
            </#if>
            <#if bidSection.bidClassifyCode == 'A08'>
                <li data-flowname="floatPoint" data-img="left_4"
                    onclick="showInfo('${ctx}/gov/bidOpen/floatPointPage', this)">
                    <a href="#">
                        <img src="${ctx}/img/sign_3.png" alt="">浮动点抽取
                        <i></i>
                    </a>
                </li>
            </#if>
            <#if bidSection.bidOpenOnline == 1>
                <li data-flowname="bidderFileDecrypt" data-img="left_6"
                    onclick="showInfo('${ctx}/gov/bidOpen/bidderFileDecryptPage', this)">
                    <a href="#">
                        <img src="${ctx}/img/sign_4.png" alt="">标书解密
                        <i></i>
                    </a>
                </li>
            <#else>
                <li data-flowname="bidderFileDecrypt" data-img="left_6"
                    onclick="showInfo('${ctx}/gov/bidOpen/siteBidderFileDecryptPage', this)">
                    <a href="#">
                        <img src="${ctx}/img/sign_4.png" alt="">标书解密
                        <i></i>
                    </a>
                </li>

            </#if>
            <li data-flowname="bidOpenRecord" data-img="left_10"
                onclick="showInfo('${ctx}/gov/bidOpen/bidOpenRecordPage', this)">
                <a href="#">
                    <img src="${ctx}/img/sign_6.png" alt="">开标记录表
                    <i></i>
                </a>
            </li>
        </ul>
    </div>
    <div class="right">

    </div>
</div>

<script>
    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param e
     */
    function showInfo(targetUrl, e) {
        if ('${bidSection.bidOpenOnline}' == 1) {
            if ($(e).hasClass("not_start")) {
                layerWarning("该环节还未开始！");
                return false;
            }
        }

        var indexLoad = layer.load();
        setTimeout(function () {
            var $content_right = $(".right");
            $content_right.hide();
            $(e).addClass("choice").siblings().removeClass("choice");
            $(".left ul li").each(function (index) {
                if ($(this).is($(e))) {
                    localStorage.setItem("bid_open_flow_${bidSection.id}", index);
                }
            });
            $content_right.load(targetUrl, function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
                layer.close(indexLoad);
                $content_right.fadeIn();
            });
        }, 200);

    }

    // 全局加载layui组件，无需在子页面单独引入
    var form, layer, laytpl, laypage, element;
    $(function () {
        layui.use(['form', 'layer', 'element', 'laytpl', 'laypage'], function () {
            form = layui.form;
            element = layui.element;
            laypage = layui.laypage;
            laytpl = layui.laytpl;
        });
        //给左侧菜单栏添加完成情况
        listBidOpenFlowSituation();

        //刷新时加载开标默认项
        var $left_ul_li = $(".left ul li");
        var $left_ul_li_index = localStorage.getItem("bid_open_flow_${bidSection.id}");
        if ($left_ul_li != null) {
            $left_ul_li.eq($left_ul_li_index).trigger("click");
        } else {
            $left_ul_li.eq(0).trigger("click");
        }

    });


    /**
     * 获取开标进行环节的完成情况
     */
    function listBidOpenFlowSituation() {
        $.ajax({
            type: "post",
            url: "${ctx}/gov/listBidOpenFlowSituation",
            cache: false,
            success: function (data) {
                if (!isNull(data) && data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        var $cur_li = $(".left ul li[data-flowname='" + data[i].flowName + "']");
                        if (data[i].flowStatus === 0) {
                            $cur_li.addClass("not_start");
                        }
                        if (data[i].flowStatus === 1) {
                            $cur_li.addClass("current");
                        }
                        if (data[i].flowStatus === 2) {
                            $cur_li.addClass("complete");
                        }
                    }
                }
            }
        });
    }

</script>