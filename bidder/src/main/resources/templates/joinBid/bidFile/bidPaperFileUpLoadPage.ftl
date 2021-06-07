<link rel="stylesheet" href="${ctx}/css/bid.css">
<div class="right-cen vers">
    <h3>重要提示:</h3>
    <p>1、请投标人错峰提前上传投标文件，避免在高峰时段上传投标文件时，导致上传时间过长，影响本次投标。 </p>
    <p>2、投标文件上传并提交成功后，在开标时间前
        <#if bidSection.signInStartTimeLeft?? && bidSection.signInStartTimeLeft != "">
            <#if hours ==0 && minutes == 0>${days}天<#else >${days}天${hours}小时${minutes}分钟</#if>
        <#else >
            1天
        </#if>
        内点击签到，填写真实身份信息并上传授权委托书，确认并提交信息后，系统提供人脸识别二维<br/>码，请用手机具有扫一扫功能软件，扫描本系统提供的二维码，并按照系统要求录制人脸信息，直至提示身份识别成功，签到完成。
    </p>
    <#--<p>3、YCK文件为非必传文件，具体上传格式以招标文件要求为准，请各投标人在保证文件内容安全的情况下，自行斟酌使用。</p>-->
    <div class="kong"></div>
</div>
<div class="right-down" style="display: none">
    <hr/>
    <form class="layui-form" id="file-form">
        <div style="display: none">
            <input type="hidden" name="bidSectionId" value="${bidderOpenInfo.bidSectionId}">
            <input type="hidden" name="bidderId" value="${bidderOpenInfo.bidderId}">
            <input type="hidden" name="id" value="${bidderOpenInfo.id}">
            <input type="hidden" name="bidderFileInfo.id" value="${bidderFileInfo.id}">
        </div>

        <div class="yckDiv">
            <label for="">投标文件（yck）</label>
            <@UploadOneTag name="bidderFileInfo.gefId" allowFileSize="1024M"  readonly = "readonly" allowType="*.yck;"
            autocomplete="off" placeholder="请选择yck格式投标文件上传" class="fileInput layui-input" isLocal="true" style="" disabled="disabled">
            </@UploadOneTag>
            <div class="btn" onclick="upLoadThis(this)">上传文件</div>
        </div>

        <div class="czrDiv">
            <label for="">存证文件（czr）</label>
            <@UploadOneTag name="bidderFileInfo.czrId" allowFileSize="1M" readonly = "readonly" allowType="*.czr;"
            autocomplete="off" placeholder="请选择czr格式投标文件上传" class="fileInput layui-input" isLocal="true" style="" disabled="disabled">
            </@UploadOneTag>
            <div class="btn" onclick="upLoadThis(this)">上传文件</div>
        </div>

        <div class="syckDiv">
            <label for="">投标文件（syck）</label>
            <@UploadOneTag name="bidderFileInfo.sgefId" allowFileSize="1024M" readonly = "readonly" allowType="*.syck;"
            autocomplete="off" placeholder="请选择syck格式投标文件上传" class="fileInput layui-input" isLocal="true" style="" disabled="disabled">
            </@UploadOneTag>
            <div class="btn" onclick="upLoadThis(this)">上传文件</div>
        </div>
    </form>
</div>
<div class="foot again" style="display: none;width: 250px">
    <span onclick="reUpload()" style="width: 106px">重新上传</span>
    <#if receiptId??&& receiptId!="">
        <span type="button" class="blove-b layui-btn receiptLookOne"
              style="width: 106px;background-color:rgba(13, 65, 157, 1);" onclick="showReceipt()">
            查看回执单
        </span>
    </#if>
</div>
<div class="foot onlyReceipt" style="display: none;width: 250px">
    <#if receiptId??&& receiptId!="">
        <span type="button" class="blove-b layui-btn receiptLookOne"
              style="width: 106px;background-color:rgba(13, 65, 157, 1);" onclick="showReceipt()">
            查看回执单
        </span>
    </#if>
</div>
<div class="right-down2 right-down-other" style="display: none">
    <p>投标文件上传截止时间已到</p>
</div>
<div class="right-down3 right-down-other" style="display: none">
    <p>投标文件上传时间未到</p>
    <h2 style="text-align: center">文件上传开始时间：${times.startTime}</h2>
</div>
<div class="right-down4 right-down-other" style="display: none">
    <p>投标文件上传成功</p>
</div>

<div class="submit" style="display: none" lay-submit lay-filter="save" id="formBtnSubmit">
    提交
</div>
<div class="foot reSubmit" style="display: none">
    <span class="yellow-b" lay-submit lay-filter="save" id="formBtnSubmit">重新提交</span>
    <#--<#if receiptId??&& receiptId!="">
        <span type="button" class="blove-b layui-btn receiptLookOne" onclick="showReceipt()">
            回执单查看
        </span>
    </#if>-->
</div>

<script type="text/javascript">
    function upLoadThis(othis) {
        $(othis).parent().find(".fileInput").click();
    }

    /*
    判断当前时间是否在时间范围内
    1 范围内
    2 未到时间
    3 超过时间
     */
    function timePass(startTime, endTime,nowTime) {
        var t1 = dateStrToMils(startTime);
        var t2 = dateStrToMils(endTime);
        //当前时间戳
        var timestamp = dateStrToMils(nowTime);
        console.log(t1, t2, timestamp);
        if (timestamp >= t1 && timestamp <= t2) {
            return 1;
        } else if (timestamp < t1) {
            return 2;
        } else {
            return 3;
        }
    }

    //当前服务器时间
    var serverTime;

    function getServerTime() {
        var xhr = new XMLHttpRequest();
        if (!xhr) {
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xhr.open("HEAD", location.href, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                this.currentytime = xhr.getResponseHeader("Date");

                serverTime = new Date(this.currentytime);
            }
        }
        xhr.send(null);
    }

    //时间字符串转换为时间戳
    function dateStrToMils(dateStr) {
        var date = dateStr;
        date = date.substring(0, 19);
        date = date.replace(/-/g, '/');
        return new Date(date).getTime();
    }

    //时间范围内开放文件上传，时间范围外禁用
    function timeShow() {
        var timeStatus = timePass('${times.startTime}', '${times.endTime}', '${times.nowTime}');
        if (timeStatus == 1) {
            //上传时间内
            //判断是否已近上传文件
            if ('${bidderFileInfo.receiptId}' != '') {
                $(".again").css("display", "block");
                $(".right-cen").css("display", "none");
                // $(".right-down4").css("display", "block");
                $(".watch").css("display", "block");
            } else {
                //显示文件上传
                $(".right-down").css("display", "block");
                isUpload();
            }
        } else if (timeStatus == 2) {
            //没到上传时间
            $(".right-down3").css("display", "block");
        } else {
            //超过上传时间
            //判断是否已近上传文件
            if (!isNull('${bidderFileInfo.receiptId}')) {
                $(".vers").css("display", "none");
                $(".onlyReceipt").css("display", "block");
            } else {
                $(".right-down2").css("display", "block");
            }
        }
    }

    function reUpload() {
        //判断是否已近上传文件
        $(".again").css("display", "none");
        $(".watch").css("display", "none");
        $(".right-down4").css("display", "none");
        $(".vers").css("display", "block");
        //显示文件上传
        $(".right-down").css("display", "block");
        isUpload();
    }

    //判断是否已经上传文件,是则显示数据
    function isUpload() {
        var $yckDiv = $(".yckDiv");
        var $syckDiv = $(".syckDiv");
        var $czrDiv = $(".czrDiv");
        if ('${bidderFileInfo.receiptId}' != '') {
            if ('${yckFile.id}' != null) {
                $yckDiv.find(".fileInput").val('${yckFile.name}');
                $yckDiv.find("input[name = 'bidderFileInfo.gefId']").val('${yckFile.id}');
            }
            if ('${syckFile.id}' != null) {
                $syckDiv.find(".fileInput").val('${syckFile.name}');
                $syckDiv.find("input[name = 'bidderFileInfo.sgefId']").val('${syckFile.id}');
            }
            if ('${czrFile.id}' != null) {
                $czrDiv.find(".fileInput").val('${czrFile.name}');
                $czrDiv.find("input[name = 'bidderFileInfo.czrId']").val('${czrFile.id}');
            }
            $(".reSubmit").css("display", "block");
        } else {
            $(".submit").css("display", "block");
        }
    }

    //表单非空校验
    function formNotNull() {
        var $syckDiv = $(".syckDiv");
        var $yckDiv = $(".yckDiv");
        var $czrDiv = $(".czrDiv");
        var syck = $syckDiv.find("input[name = 'bidderFileInfo.sgefId']").val();
        var yck = $syckDiv.find("input[name = 'bidderFileInfo.gefId']").val();
        var czr = $czrDiv.find("input[name = 'bidderFileInfo.czrId']").val();
        if (!isNull(czr) && !isNull(syck) && !isNull(yck)) {
            return true;
        } else {
            if (isNull(czr)) {
                $czrDiv.find(".fileInput").css("border", "1px solid rgb(230, 17, 17)");
            }
            if (isNull(syck)) {
                $syckDiv.find(".fileInput").css("border", "1px solid rgb(230, 17, 17)");
            }
            if (isNull(yck)) {
                $yckDiv.find(".fileInput").css("border", "1px solid rgb(230, 17, 17)");
            }
            layer.msg("请上传必需文件!", {icon: 2, time: 2000});
            return false;
        }
    }

</script>
<script>
    var indexLoad = -1;
    var lock = false;
    $(function () {
        timeShow();
        $("#file-form").find(".layui-icon-upload-drag").hide();
        $("#file-form").find(".haveValueShow").remove();
    });

    /**
     * 初始化layui
     */
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;

        form.on("submit(save)", function () {
            //表单校验
            if (formNotNull()) {
                if (lock) {
                    return false;
                }
                lock = true;
                var index = layer.msg("文件上传中，请稍等...", {
                    icon: 16,
                    time: 0,
                    shade: [0.1, '#fff']
                });
                $.ajax({
                    url: "${ctx}/bidFile/savePaperFile",
                    type: "POST",
                    cache: false,
                    data: $("#file-form").serialize(),
                    success: function (data) {
                        layer.close(index);
                        window.top.layer.close(indexLoad);
                        if (data.code === "1") {
                            layer.msg("文件上传成功", {
                                icon: 1, 'time': 2000, 'end': function () {
                                    goToUrl('${ctx}/bidFile/bidPaperFileUpLoadPage', $(".bidFileLi"));
                                }
                            });
                        } else if (data.code === "2" || data.code === "0") {
                            layer.msg(data.msg, {
                                icon: data.code, 'time': 2000, 'end': function () {
                                    lock = false;
                                    return false;
                                }
                            });
                        } else {
                            layer.msg("文件上传失败", {
                                icon: 2, 'time': 2000, 'end': function () {
                                    lock = false;
                                    return false;
                                }
                            });
                        }
                        // lock = false;
                    },
                    error: function (e) {
                        lock = false;
                        console.error(e);
                        layer.close(index);
                        window.top.layer.close(indexLoad);
                        if (e.status == 403) {
                            console.warn("用户登录失效！！！")
                            window.top.location.href = "/login.html";
                        }
                        return false;
                    }
                });

                // 显示校验进度
                currentSchedule();
            } else {
                return false;
            }
        });
        form.render();
    });

    function currentSchedule() {
        indexLoad = window.top.layer.open({
            type: 2,
            offset: 'auto',
            title: false,
            shadeClose: false,
            area: ['550px', '400px'],
            skin: 'curruent',
            btn: false,
            move: false,
            resize: false,
            content: '${ctx}/bidFile/currentSchedulePage?bidSectionId=${bidSection.id}&bidderId=${bidderOpenInfo.bidderId}',
            btn1: false
        });
    }

    //查看回执单
    function showReceipt() {
        var markq = '${receiptFile.mark}';
        if (isNull(markq)) {
            doLoading("文件未提交", 0, 2);
            return;
        }
        window.top.layer.open({
            type: 2,
            title: '回执单查看',
            shadeClose: true,
            scrollbar: false,
            area: ['1200px', '790px'],
            btn: ['取消'],
            offset: 'auto',
            // content: $("#pdf"),
            content: "/bidFile/showPdfIframe?mark=" + encodeURIComponent("${receiptFile.mark}"),
            // content:'www.baidu.com',
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
        <#--loadPdf('${receiptFile.mark}');-->
    }
</script>

