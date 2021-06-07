<link rel="stylesheet" href="${ctx}/css/waiting.css">
<script type="text/javascript" src="${ctx}/js/utf.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-qrcode.js"></script>
<div class="right-center">
    <img src="../img/correct.png" alt="">
    <div class="pass">请等待核验身份</div>
    <p>签到时间：${bidderOpenInfo.signinTime}</p>
    <div class="submit red-b" onclick="cancelUrgentSigin()">撤销签到</div>
</div>
<div class="QRcode foot" style="display: none">
    <div class="qr-cen">
        <div class="qr-left">
            <p>扫一扫</p>
            <p>查看区块信息</p>
        </div>
        <div class="qr-right">

        </div>
    </div>
</div>
<script>
    $(function () {
        if ('${isUpChain}'){
            $(".qr-right").qrcode({
                width: 175,
                height: 175,
                text: '${lastBsnChainInfo.queryAddress}',
                render: "canvas",
                src: '${ctx}/img/qrLogo.png'             //二维码中间的图片
            });
            $(".foot").show();
        }
    })

    //撤销签到
    function cancelUrgentSigin() {
        $.ajax({
            url: "${ctx}/identityAuth/cancelUrgentSigin",
            type: "POST",
            cache: false,
            async: false,
            data: {
                id: '${bidderOpenInfo.id}'
            },
            success: function (data) {
                if (data) {
                    layer.msg("撤销成功", {icon: 1, time: 2000});
                }
            },
            error: function (e) {
                layer.msg("撤销失败", {icon: 2, time: 2000});
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
        goToUrl('${ctx}/identityAuth/msgInputPage', $(".identityAuthLi"));
    }
</script>