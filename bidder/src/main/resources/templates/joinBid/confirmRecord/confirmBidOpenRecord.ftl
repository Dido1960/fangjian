<link rel="stylesheet" href="${ctx}/css/confirmBidOpenRecord.css">
<script type="text/javascript" src="${ctx}/js/utf.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-qrcode.js"></script>
<#assign isHide=data["isHide"] >
<#assign currentBidder=data["currentBidder"] >
<#assign otherBidders=data["otherBidders"] >
<div class="right-center" style="display: none">
    <ul>
        <#if !isHide>
            <li>投标人</li>
            <li>投标报价</li>
            <li>保证金</li>
            <li>授权委托人</li>
        <#else >
            <li style="width: 49%">申请人</li>
            <li style="width: 50%">授权委托人</li>
        </#if>
    </ul>
    <div class="contentBox">
        <#--  当前投标人  -->
        <#if currentBidder != null>
            <div class="content">
                <#if !isHide>
                    <div id="bidder-name">${currentBidder.bidderName!""}</div>
                    <div>
                        <#if !currentBidder.bidderOpenInfo.bidPriceType?? || currentBidder.bidderOpenInfo.bidPriceType == "总价">
                            <#if currentBidder.bidderOpenInfo.bidPrice?? && currentBidder.bidderOpenInfo.bidPrice !=''>
                                ${((currentBidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                            </#if>
                        <#else>
                            ${currentBidder.bidderOpenInfo.bidPriceType}${currentBidder.bidderOpenInfo.bidPrice}
                        </#if>
                    </div>
                    <#if currentBidder.bidderOpenInfo.marginPayStatus==0>
                        <div>未缴纳</div>
                    <#else >
                        <div>已缴纳</div>
                    </#if>
                    <div>${bidder.bidderOpenInfo.clientName}</div>
                <#else >
                    <div style="width: 49%">${currentBidder.bidderName}</div>
                    <div style="width: 50%">${bidder.bidderOpenInfo.clientName}</div>
                </#if>
            </div>
        </#if>

        <#--  其它投标人  -->
        <#if otherBidders??>
            <#list otherBidders as bidder>
                <div class="content">
                    <#if !isHide>
                        <div>${bidder.bidderName}</div>
                        <div>
                            <#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
                                <#if bidder.bidderOpenInfo.bidPrice?? && bidder.bidderOpenInfo.bidPrice !=''>
                                    ${((bidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                                </#if>
                            <#else>
                                ${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}
                            </#if>
                        </div>
                        <#if bidder.bidderOpenInfo.marginPayStatus==0>
                            <div>未缴纳</div>
                        <#else >
                            <div>已缴纳</div>
                        </#if>
                        <div>${bidder.bidderOpenInfo.clientName}</div>
                    <#else >
                        <div style="width: 49%">******</div>
                        <div style="width: 50%">******</div>
                    </#if>
                </div>
            </#list>
        </#if>

    </div>
</div>

<div class="right-refuse decryption-no" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>解密阶段未结束</p>
</div>
<div class="right-refuse tender-reject" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>标书已被拒绝</p>
    <p class="rejectReason">拒绝理由：${bidder.bidderOpenInfo.tenderRejectionReason}</p>
</div>
<div class="right-msg decryptFile-no" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>解密未成功</p>
</div>
<div class="foot" style="display: none">
    <h4>对本次开标会议有无异议，请确认开标一览表</h4>
    <div class="btns site-demo-button" id="layerDemo">
        <#if bidder.bidderOpenInfo?? && bidder.bidderOpenInfo.dissentStatus != 2>
            <span class="layui-btn dissent" data-method="offset" data-type="auto" onclick="dissentWindows(this)">有异议</span>
        </#if>
        <span class="unDissent" onclick="unDissent()">无异议</span>
    </div>
</div>
<#if isUpChain>
    <div class="QRcode footer">
        <div class="qr-cen">
            <div class="qr-left">
                <p>扫一扫</p>
                <p>查看区块信息</p>
            </div>
            <div class="qr-right">
            </div>
        </div>
    </div>
</#if>
<input type="hidden" id="bidderId" value="${bidder.id!}">
<input type="hidden" id="bidSectionId" value="${bidder.bidSectionId!}">
<input type="hidden" id="tenderRejection" value="${bidder.bidderOpenInfo.tenderRejection!}">
<input type="hidden" id="docDetermine" value="${bidder.bidderOpenInfo.docDetermine!}">
<input type="hidden" id="questionStatus" value="${questionStatus!}">
<input type="hidden" id="decryptionStatus" value="${decryptionStatus!}">
<script>
    $(function () {
        //初始化页面数据
        initPageData();
        if ('${isUpChain}') {
            $(".qr-right").qrcode({
                width: 175,
                height: 175,
                text: '${lastBsnChainInfo.queryAddress}',
                render: "canvas",
                src: '${ctx}/img/qrLogo.png'             //二维码中间的图片
            });
        }
    });

    /**
     *  页面初始化
     */
    function initPageData() {
        var tenderRejection = $("#tenderRejection").val();
        var questionStatus = $("#questionStatus").val();
        var docDetermine = $("#docDetermine").val();
        var decryptionStatus = $("#decryptionStatus").val();
        if (isDissentOrNot()) {
            $(".foot").show().find("h4").text("已确认开标一览表");
            $(".foot #layerDemo").remove();
        }
        if (tenderRejection == 1) {
            $(".tender-reject").show();
        } else {
            if (decryptionStatus != 2){
                $(".decryption-no").show();
            }else {
                if ('${bidder.bidderOpenInfo.decryptStatus}' != 1){//解密失败
                    $(".decryptFile-no").show();
                }else {
                    $(".right-center").show();
                }
            }
        }
        if (questionStatus == 1) {
            $(".foot").show();
        }
    }

    /**
     *  是否点过无异议
     */
    function isDissentOrNot() {
        var flag = false;
        //判断当前用户是否 无异议
        $.ajax({
            url: '${ctx}/bidderModel/isDissentOrNot',
            type: 'POST',
            cache: false,
            async: false,//必须同步
            data: {
                bidderId: $("#bidderId").val(),
                bidSectionId: $("#bidSectionId").val(),
            },
            success: function (data) {
                flag = data;
            },
            error: function (data) {
                console.error(data);
            },
        });
        return flag;
    }

    /**
     * 有异议
     */
    function dissentWindows(e) {
        $.ajax({
            url: '${ctx}/bidderModel/updateBidderOpenInfo',
            type: 'post',
            cache: false,
            data: {
                'bidSectionId': $("#bidSectionId").val(),
                'bidderId': $("#bidderId").val(),
                'dissentStatus': '2'
            },
            success: function (data) {
                if (data){
                    layerAlert('请前往消息盒子发表异议消息！');
                    // 解除消息盒子禁言
                    postMessageFun({forbiddenData: {memberId: '${bidderIdPre}_${bidder.id}', type: 1}});
                    $(e).remove();
                }else {
                    layerWarning('操作失败');
                }

            },
            error: function (data) {
                layerAlert('请前往消息盒子发表异议消息！');
                loadComplete();
                console.error(data);
            },
        });
        <#--var bidderId = $("#bidderId").val();-->
        <#--layer.open({-->
        <#--    type: 2,-->
        <#--    title: ['填写异议信息', 'text-align:center;'],-->
        <#--    shadeClose: true,-->
        <#--    area: ['600px', '400px'],-->
        <#--    btn: ['确定', '取消'],-->
        <#--    btnAlign: 'c',//按钮居中-->
        <#--    resize: false,-->
        <#--    content: "${ctx}/bidderModel/dissentPage?id=" + bidderId,-->
        <#--    btn1: function (index, layero) {-->
        <#--        var body = window.layer.getChildFrame('body', index);-->
        <#--        var iframeWin = window[layero.find('iframe')[0]['name']];-->
        <#--        iframeWin.addDissent(function () {-->
        <#--            $(".footer").show();-->
        <#--            layer.closeAll();-->
        <#--            layer.msg("操作成功！", {icon: 1, time: 2000})-->
        <#--        });-->
        <#--    },-->
        <#--    btn2: function (index) {-->
        <#--        layer.close(index);-->
        <#--    }-->
        <#--});-->
    }

    /**
     * 无异议
     */
    function unDissent() {
        layer.confirm("确认无异议？",
            {icon: 3, title: '操作提示'},
            function () {
                doLoading();
                $.ajax({
                    url: '${ctx}/bidderModel/updateBidderOpenInfo',
                    type: 'post',
                    cache: false,
                    data: {
                        'bidSectionId': $("#bidSectionId").val(),
                        'bidderId': $("#bidderId").val(),
                        'dissentStatus': '1'
                    },
                    success: function (data) {
                        loadComplete();
                        if (data) {
                            // 消息盒子禁言
                            postMessageFun({forbiddenData: {memberId: '${bidderIdPre}_${bidder.id}', type: 0}});
                            // 添加公告
                            postMessageFun({linkType: '${bidder.bidderName}对本次开标无异议'});
                            $("#layerDemo").prev().text("已确认开标一览表");
                            $("#layerDemo").find("span").remove();
                            $(".foot,.footer").show();
                            layer.msg("操作成功！", {icon: 1})
                        } else {
                            layer.msg("操作失败！", {icon: 5})
                        }
                    },
                    error: function (data) {
                        loadComplete();
                        console.error(data);
                        layer.msg("操作失败！", {icon: 5})
                    },
                });
            });
    }
</script>