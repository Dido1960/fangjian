<link rel="stylesheet" href="${ctx}/css/fileDecrypt.css">
<script type="text/javascript" src="${ctx}/js/utf.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-qrcode.js"></script>
<div class="right-center" style="display: none">
    <h3>文件解密</h3>
    <div class="decryptContent">
        <#if bidderFileInfo.gefId??>
            <div class="space gefFile">
                <label for="">投标文件（gef）</label>
                <@UploadOneTag name="gefId" allowFileSize="1024M" allowType="*.gef;"
                autocomplete="off" class="fileInput layui-input" style="float:left;" disabled="disabled">
                </@UploadOneTag>
                <div onclick="decryptGef(${bidderFileInfo.gefId!})" class="decryptBtn">解密</div>
            </div>
        </#if>
        <div class="space sgefFile">
            <label for="">投标文件（sgef）</label>
            <@UploadOneTag name="sgefFileId" allowFileSize="1024M" allowType="*.sgef;"
            autocomplete="off" class="fileInput layui-input" style="float:left;" disabled="disabled">
            </@UploadOneTag>
            <div onclick="decryptSgef(${bidderFileInfo.sgefId!})" class="decryptBtn">解密</div>
        </div>
    </div>
</div>

<input type="hidden" id="tenderRejection" value="${bidderOpenInfo.tenderRejection!}">
<input type="hidden" id="decryptRedisStatus" value="${decryptRedisStatus}">
<input type="hidden" id="decryptStatus" value="${bidderOpenInfo.decryptStatus}">
<input type="hidden" id="notCheckin" value="${bidderOpenInfo.notCheckin}">
<input type="hidden" id="lineDecryptionStatus" value="${lineStatus.decryptionStatus}">
<input type="hidden" id="isUpChain" value="${isUpChain}">
<div class="right-refuse not-start" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>暂未开始解密，请耐心等待</p>
</div>
<div class="right-refuse tender-reject" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>标书已被拒绝</p>
    <p class="rejectReason">拒绝理由：${bidderOpenInfo.tenderRejectionReason}</p>
</div>
<div class="right-refuse decrypt-ok" style="display: none">
    <img src="${ctx}/img/samll_right.png" alt="">
    <p>投标文件已完成解密，请等待其他投标人解密</p>
    <div class="end witingCss" style="display: none"></div>
</div>
<div class="right-refuse decrypt-fail" style="display: none">
    <img src="${ctx}/img/mistake.png" alt="">
    <p>投标文件未递交</p>
</div>
<div class="right-refuse decrypt-ing" style="display: none">
    <img style="width: 80px;height: 80px" src="${ctx}/img/Lark20200923-130048.gif" alt="">
    <p class="waitingText">排队解密中，排队总人数${queueCount}人</p>
    <div class="witing witingCss" style="display: none"></div>
</div>

<div class="right-refuse stage-over" style="display: none">
    <img src="${ctx}/img/samll_right.png" alt="">
    <p>解密阶段已结束</p>
</div>
<#if isUpChain>
    <div class="QRcode foot">
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
<input id="phoneCertStr" name="phoneCertStr" value="" type="hidden"/>
<input id="successVerifyId" name="successToken" value="" type="hidden"/>
<script>
    $(function () {
        templatesShow();
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
     * 页面初始化
     */
    function templatesShow() {
        var tenderRejection = $("#tenderRejection").val();
        var decryptStatus = $("#decryptStatus").val();
        var decryptRedisStatus = $("#decryptRedisStatus").val();
        var lineDecryptionStatus = $("#lineDecryptionStatus").val();
        var notCheckin = $("#notCheckin").val();
        $(".layui-icon-upload-drag").hide();
        $(".haveValueShow").remove();
        //根据 开标状态 和 解密情况 展示相应模板
        switch (lineDecryptionStatus) {
            case "1":
                //解密开始
                if (tenderRejection == 1) {
                    //被标书拒绝
                    $(".tender-reject").show();
                } else {
                    //0:未解密;1:已解密;2:解密失败
                    switch (decryptStatus) {
                        case "1":
                            $(".decrypt-ok").show();
                            //计算解密用时
                            defaultTime = "${decryptTime}" - 1;
                            queueTime();
                            $(".end").text("解密用时：" + time);
                            $(".end").show();
                            break;
                        default:
                            if (isNull(decryptRedisStatus)) {
                                if (!isNull('${bidderFileInfo.sgefId}')) {
                                    $(".right-center").show();
                                    isUpload();
                                } else {//如果未递交文件则显示文件未递交
                                    $(".decrypt-fail").show();
                                }
                            } else {
                                $(".decrypt-ing").show();
                                //开启计时器
                                defaultTime = '${queueTime}'
                                queueTimeInterval = setInterval(queueTime, 1000);
                                decryptInterval = setInterval(decryptSiu, 3000);
                                $(".witing").show();
                                $(".decrypt-ing").show();
                            }
                            break;
                    }
                }
                break;
            case "2":
                //解密结束
                $(".stage-over").show();
                break;
            default:
                //其他情况未开始解密
                $(".not-start").show();
                break;
        }

    }

    var decryptInterval;
    //排队定时器
    var queueTimeInterval;
    function phoneDecrypt() {
        window.top.layer.closeAll();
        var phoneCertNo = $("#phoneCertStr").val();
        if (isNull(phoneCertNo)) {
            layer.msg("未检测证书", {icon: 5});
            return;
        }
        console.log("phoneCertNo:" + phoneCertNo)
        $.ajax({
            url: "${ctx}/bidderModel/phoneDecrypt",
            type: "POST",
            cache: false,
            data: {
                fileId: '${bidderFileInfo.sgefId!}',
                phoneCertNo: phoneCertNo,
                bidderId: '${bidderId}',
                bidSectionId: "${bidderOpenInfo.bidSectionId}"
            },
            success: function (data) {
                loadComplete();
                if (data.code == "1") {
                    layer.msg(data.msg, {icon: 1});
                    var successVerifyId = $("#successVerifyId").val();
                    qrFileDecryptSuccess(successVerifyId);
                } else {
                    layer.msg(data.msg, {icon: 5});
                }
                $(".right-center").hide();
                showDecryptIng(data.data);

                decryptInterval = setInterval(decryptSiu, 1000);
            },
            error: function (data) {
                loadComplete();
                console.error(data);
                layer.msg("解密失败", {icon: 5});
            }
        });
    }

    /**
     * 投标文件解密
     * @param fileId 文件id
     */
    function decryptSgef(fileId) {
        if ('${bidderFileInfo.caType}' === '${phoneCa}') {
            layer.open({
                type: 2,
                title: " ",
                skin: 'decrypt-box',
                closeBtn: 1,
                content: "${ctx}/mobilePhoneScanCode/decryptQrPage",
                area: ['400px', '400px'],
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                },
                error: function (index) {
                    // 点击取消的回调函数
                    layer.close(index);
                }
            });
        } else {
            var cipher = "${bidderFileInfo.cipher}";
            var url = "/common/data/selectCaInfoPage";
            layer.open({
                type: 2,
                title: "请选择CA",
                offset: 'c',
                content: url,
                area: ['400px', '300px'],
                btn: ['确定', '关闭'],
                btn1: function (index, layero) {
                    //信息内容，参考于 BJCA_CertInfo
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    if (isNull(cipher)) {
                        iframeWin.loginCa(function (certInfo, QSKJInfo, certStatus) {
                            layer.close(index);
                            if (!isNull(certStatus) && certStatus) {
                                decrypt(fileId, "1", "", "1")
                            } else {
                                layer.msg("非正确的加密锁，请插入正确的加密锁进行解密！", {icon: 5, time: 5000});
                            }
                        });
                    } else {
                        iframeWin.loginCa(function (cert_info, qskj_info, certStatus) {
                            iframeWin.SOF_DecryptData(cert_info.CERT_NO_INDEX, cipher, function (returnInfo) {
                                var privateKey = returnInfo.retVal;
                                layer.close(index);
                                if (!isNull(privateKey)) {
                                    decrypt(fileId, "1", privateKey, "0")
                                } else {
                                    layer.msg("非正确的加密锁，请插入正确的加密锁进行解密！", {icon: 5, time: 5000});
                                }
                            })
                        });
                    }
                },
                btn2: function (index) {
                    // 点击取消的回调函数
                    layer.close(index);
                }
            });
        }
    }

    /**
     * 投标文件解密
     * @param fileId 文件id
     */
    function decryptGef(fileId) {
        layer.confirm("确认解密文件吗？", {icon: 3, title: '操作提示'}, function () {
            decrypt(fileId, "0", "", "0")
        });
    }

    /**
     * 投标文件解密
     * @param fileId 文件id
     * @param type 文件类型
     * @param privateKey 私钥
     * @param isOtherCa 是否是互认加密
     */
    function decrypt(fileId, type, privateKey, isOtherCa) {
        doLoading();
        $.ajax({
            url: "${ctx}/bidderModel/decrypt",
            type: "POST",
            cache: false,
            data: {
                fileId: fileId,
                fileType: type,
                bidderId: '${bidderId}',
                bidSectionId: "${bidderOpenInfo.bidSectionId}",
                isOtherCa: isOtherCa,
                privateKey: privateKey
            },
            success: function (data) {
                loadComplete();
                if (data.code == "1") {
                    layer.msg(data.msg, {icon: 1});
                } else {
                    layer.msg(data.msg, {icon: 5});
                }
                $(".right-center").hide();
                showDecryptIng(data.data);

                decryptInterval = setInterval(decryptSiu, 1000);
            },
            error: function (data) {
                loadComplete();
                console.error(data);
                layer.msg("解密失败", {icon: 5});
            }
        });
    }

    function showDecryptIng(data) {
        queueTimeInterval = setInterval(queueTime, 1000);
        $(".waitingText").text("排队解密中，排队总人数" + data + "人");
        $(".witing").show();
        $(".decrypt-ing").show();
    }

    //设置计时器
    var defaultTime = 0;

    var timer = null;
    var time = "00分00秒";
    var hours, minutes, seconds, liftTime;

    function queueTime() {
        defaultTime++;

        hours = parseInt(defaultTime / (60 * 60));
        liftTime = defaultTime % (60 * 60);
        minutes = parseInt(liftTime / 60);
        seconds = parseInt(liftTime % 60);

        time = (minutes > 9 ? minutes : '0' + minutes) + '分'
            + (seconds > 9 ? seconds : '0' + seconds) + '秒';

        $(".witing").text("解密用时：" + time);
    }

    /**
     * 查询解密状态
     */
    var decryptSiuCount = 0;
    function decryptSiu() {
        if (decryptSiuCount != 0){
            return false;
        }
        decryptSiuCount++;
        $.ajax({
            url: "${ctx}/bidderModel/decryptSiu",
            type: "POST",
            cache: false,
            data: {
                bidderId: ${bidderId},
                bidSectionId: ${bidderOpenInfo.bidSectionId}
            },
            success: function (data) {
                if (data.code != 3) {//解密结束
                    //关闭解密中页面以及定时器
                    window.clearInterval(decryptInterval);
                    window.clearInterval(queueTimeInterval);
                    var msg = "解密失败，请重新选择文件解密！";
                    if (!isNull(data.msg)) {
                        msg = data.msg;
                    }
                    if (data.code == 1) {//解密成功 修改解密时间
                        layer.msg("解密完成！", {
                            icon: 1, time: 2000, end: function () {
                                window.top.location.reload();
                                $('.decryptLi').click();
                            }
                        });
                    } else {//解密失败，允许重新提交
                        layer.msg(msg, {
                            icon: 2, time: 2000, end: function () {
                                window.top.location.reload();
                                $('.decryptLi').click();
                            }
                        });
                    }
                } else {//修改排队人数
                    $(".waitingText").text("排队解密中，排队总人数" + data.data + "人");
                    $(".witing").show();
                    $(".decrypt-ing").show();
                }
                decryptSiuCount --;
            },
            error: function (data) {
                console.error(data);
                decryptSiuCount --;
            }
        });
    }

    //判断是否已经上传文件,是则显示数据
    function isUpload() {
        if (!isNull('${gefFile}') && '${gefFile.id}' != null) {
            $(".gefFile").find(".fileInput").val('${gefFile.name}');
            $(".gefFile").find("input[name = 'gefFileId']").val('${gefFile.id}');
        }
        if (!isNull('${sgefFile}') && '${sgefFile.id}' != null) {
            $(".sgefFile").find(".fileInput").val('${sgefFile.name}');
            $(".sgefFile").find("input[name = 'sgefFileId']").val('${sgefFile.id}');
        }
    }

    function qrFileDecryptSuccess(successVerifyId) {
        $.ajax({
            url: "${ctx}/mobilePhoneScanCode/qrFileDecryptSuccess",
            type: "POST",
            cache: false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrf_header, csrf_token);
            },
            data: {
                projectName: '${bidSection.bidSectionName}',
                projectCode: '${bidSection.bidSectionCode}',
                verifyId: successVerifyId
            },
            success: function (data) {

            },
            error: function (e) {
                loadComplete();
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }
</script>