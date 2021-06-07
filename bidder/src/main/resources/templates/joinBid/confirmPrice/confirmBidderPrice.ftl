<link rel="stylesheet" href="${ctx}/css/confirmPrice.css">
<div class="right-center" style="display: none">
    <form class="layui-form" id="myForm">
        <div class="fill">
            <label for="">企业名称</label>
            <input type="text" name="bidderName" placeholder="请输入企业名称" value="${bidder.bidderName!}" readonly>
        </div>
        <div class="fill">
            <label for="">委托人姓名</label>
            <input type="text" name="bidderOpenInfo.clientName" placeholder="请输入委托人姓名"
                   value="${bidder.bidderOpenInfo.clientName!}" class="clientName" readonly>
        </div>
        <div class="fill">
            <label for="">身份证号码</label>
            <input type="text" name="bidderOpenInfo.clientIdcard" placeholder="请输入身份证号码"
                   value="${bidder.bidderOpenInfo.clientIdcard!}" class="clientIdcard" readonly>
        </div>
        <#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
            <div class="fill">
                <label for="">投标报价</label>
                <input type="text" name="bidPrice" placeholder="请输入投标报价"
                        <#if bidder.bidderOpenInfo.bidPrice??>
                            value="${((bidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}(元)"
                        </#if>
                       readonly>
            </div>
            <div class="fill">
                <label for="">报价大写</label>
                <input type="text" name="bidPriceChinese" placeholder="请输入报价大写" value="" readonly>
            </div>
        <#else>
            <div class="fill">
                <label for="">投标报价</label>
                <input type="text" name="bidPrice" placeholder="请输入投标报价"
                        <#if bidder.bidderOpenInfo.bidPrice??>
                            value="${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}"
                        </#if>readonly>
            </div>
        </#if>
        <div>
            <input type="hidden" name="bidSectionId" value="${bidder.bidderOpenInfo.bidSectionId!}">
            <input type="hidden" name="id" value="${bidder.id!}">
            <input type="hidden" name="bidderOpenInfo.id" value="${bidder.bidderOpenInfo.id!}">
            <input type="hidden" name="bidderOpenInfo.priceDetermine" class="priceDetermine"
                   value="${bidder.bidderOpenInfo.priceDetermine!}">
            <input type="hidden" class="decryptStatus" value="${lineStatus.decryptionStatus!}">
            <input type="hidden" id="tenderRejection" value="${bidder.bidderOpenInfo.tenderRejection!}">
        </div>

        <div lay-submit lay-filter="update" class="submit">确认</div>
    </form>
</div>

<div class="right-refuse right-center2" style="display: none">
    <p>暂未到报价确认环节，请耐心等待</p>
</div>
<div class="right-msg tender-reject" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>标书已被拒绝</p>
    <p class="rejectReason">拒绝理由：${bidder.bidderOpenInfo.tenderRejectionReason}</p>
</div>
<div class="right-msg decryptFile-no" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>解密未成功</p>
</div>

<script>
    $(function () {
        initPages();
    });

    /**
     * 初始化页面
     */
    function initPages() {
        var priceDetermine = $(".priceDetermine").val();
        var decryptStatus = $(".decryptStatus").val();
        var bidPrice = '${bidder.bidderOpenInfo.bidPrice}';
        var tenderRejection = $("#tenderRejection").val();

        //根据 开标状态 和 解密情况 展示相应模板
        if (tenderRejection == 1) {
            //被标书拒绝
            $(".tender-reject").show();
            return false;
        }
        if (decryptStatus == "2") { //解密阶段结束
            if ('${bidder.bidderOpenInfo.decryptStatus}' != 1){//解密失败
                $(".decryptFile-no").show();
            }else {
                $(".right-center").show();
                if (!isNull(bidPrice.trim())) {
                    var bidPriceChinese = convertMoney(bidPrice.trim(), "元");
                    bidPriceChinese = bidPriceChinese === "整" ? "零元整" : bidPriceChinese;
                    $("input[name=bidPriceChinese]").val(bidPriceChinese);
                }
                if (priceDetermine == "1") {
                    $(".submit").text("已确认").removeAttr("lay-filter").css("opacity", "0.7");
                }
            }
        } else {
            //解密阶段未结束
            $(".right-center2").show();
        }
    }

    /**
     * 初始化layui
     */
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;

        form.on("submit(update)", function () {
            if ($(".priceDetermine").val() == "1") {
                return false;
            } else {
                layer.confirm("确认报价吗？",
                    {icon: 3, title: '操作提示'},
                    function () {
                        doLoading();
                        $(".priceDetermine").val(1);
                        $.ajax({
                            url: "${ctx}/bidderModel/confirmPrice",
                            type: "POST",
                            cache: false,
                            data: $("#myForm").serialize(),
                            success: function (data) {
                                loadComplete();
                                if (data) {
                                    $(".submit").text("已确认").removeAttr("lay-filter").css("opacity", "0.7");
                                    layer.msg("确认成功", {icon: 1});
                                } else {
                                    layer.msg("确认失败", {icon: 5});
                                }
                            },
                            error: function (e) {
                                loadComplete();
                                console.error(e);
                                layer.msg("确认失败", {icon: 5,end:function () {
                                        if(e.status == 403){
                                            console.warn("用户登录失效！！！")
                                            window.top.location.href = "/login.html";
                                        }
                                    }});
                            }
                        });
                    });

            }


        });

    });

</script>