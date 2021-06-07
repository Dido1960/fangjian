<link rel="stylesheet" href="${ctx}/css/sign.css">
<script type="text/javascript" src="${ctx}/js/utf.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-qrcode.js"></script>
<div class="right-center" style="display: none">
    <h2>填写授权委托书信息</h2>
    <form id="msgForm" class="layui-form">
        <div>
            <input type="hidden" name="id" value="${bidderOpenInfo.id}">
        </div>
        <div class="fill">
            <label for="">企业名称</label>
            <input type="text" readonly value="${bidder.bidderName}">
        </div>
        <div class="fill">
            <label for="">委托人姓名</label>
            <input type="text" name="clientName" placeholder="请输入委托人姓名" lay-verType="tips" lay-verify="required"
                   value="${bidderOpenInfo.clientName}">
        </div>
        <div class="fill">
            <label for="">身份证号码</label>
            <input type="text" id="clientIdcard" name="clientIdcard" placeholder="请输入身份证号码" lay-verType="tips"
                   lay-verify="required|isIdCard" value="${bidderOpenInfo.clientIdcard}">
        </div>
        <div class="fill">
            <label for="">联系电话</label>
            <input type="text" name="clientPhone" placeholder="请输入联系电话" lay-verType="tips" lay-verify="required|phone"
                   value="${bidderOpenInfo.clientPhone}">
        </div>
        <div class="upload">
            <label for="">授权委托书</label>
            <div class="still">
                <@UploadOneTag id="sqwtsFileId" name="sqwtsFileId" allowFileSize="20M" readonly="readonly" isBindInput="false" allowType="*.pdf;" value="${bidderOpenInfo.sqwtsFileId!}"
                autocomplete="off" placeholder="请选择PDF格式上传" class="fileInput layui-input" verify="fileNotNull"></@UploadOneTag>
                <div class="btn up" onclick="upLoadThis(this)" >上传文件</div>
                <span class="fileShow watch btn" <#if !bidderOpenInfo.sqwtsFileId??>style="display: none" </#if>
                  onclick="fileShowFun(this)" >预览</span>
            </div>
        </div>
        <div class="submit">
            <#if bidderOpenInfo.clientName?? && bidderOpenInfo.clientName!=''>
                <input type="button" lay-submit lay-filter="save" id="formBtnSubmit" class="layui-btn" style="
                background: linear-gradient( 270deg, rgba(255, 145, 0, 1) 0%, rgba(255, 179, 81, 1) 100% );
                filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#ff9100, endColorstr=#ffb351);color: #fff;"
                       value="重新提交"/>
            <#else >
                <input type="button" lay-submit lay-filter="save" id="formBtnSubmit" class="layui-btn" style="
                    background: linear-gradient(270deg,rgba(19, 97, 254, 1) 0%, rgba(78, 138, 255, 1) 100%);
filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#1361fe, endColorstr=#4e8aff);"
                       value="提交"/>
            </#if>
        </div>
    </form>
</div>
<div class="right-down2 right-down-other" style="display: none">
    <p>签到截止时间已到</p>
</div>
<div class="right-down3 right-down-other" style="display: none">
    <p>签到时间未到</p>
    <h2 style="text-align: center">签到开始时间：${times.startTime}</h2>
</div>
<div class="fileNo right-down-other" style="display: none">
    <p>投标文件尚未进行上传，不能进行签到</p>
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
    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;

        form.render();

        form.verify({
            //验证身份证
            isIdCard: function (value) {
                return isIdCard(value);
            },
            //委托书不可为空
            fileNotNull: function (value) {
                return fileNotNull(value);
            }
        });

        form.on("submit(save)", function () {
            var idCard = $("#clientIdcard").val();
            var fileId = $("#sqwtsFileId").val();

            if (isNull(fileId) || !reg.test(idCard)) {
                return false;
            }
            if (lock1) {
                return false;
            }
            lock1 = true;
            doLoading();
            $.ajax({
                url: "${ctx}/identityAuth/saveMsg",
                type: "POST",
                cache: false,
                data: $("#msgForm").serialize(),
                success: function (data) {
                    lock1 = false;
                    loadComplete();
                    if (data) {
                        goToUrl('${ctx}/identityAuth/identityAuthPage', $(".identityAuthLi"));
                    } else {
                        layer.msg("提交失败", {icon: 2, time: 2000});
                    }
                    return false
                },
                error: function (e) {
                    lock1 = false;
                    loadComplete();
                    console.error(e);
                    if (e.status == 403) {
                        console.warn("用户登录失效！！！")
                        window.top.location.href = "/login.html";
                    }
                    return false;
                }
            });
        });
    });

    var lock1 = false;

    $(function () {
        //修改上传插件的部分功能
        $(".fileInput").attr("lay-verType", "tips");
        $(".fileInput").attr("name", "sqwtsFileName");
        $("#msgForm").find(".layui-icon-upload-drag").hide();
        $("#msgForm").find(".haveValueShow").remove();

        //判断签到的进行状态
        siginNow();

        if (isNull('${bidderOpenInfo.sqwtsFileId}')){
            $("#sqwtsFileId").css("width","calc(100% - 90px)");
        }
    });
</script>

<script>

    var fileInterval = null;

    //文件上传按钮绑定
    function upLoadThis(obj) {
        $(obj).parent().find(".layui-icon-upload-drag").click();
        setInterval(function () {
            fileIsUpload(obj);
        }, 1000);
    }

    //判断文件是否上传完毕
    function fileIsUpload(obj) {
        if (!isNull($(obj).parent().find(".fileInput").val())) {
            $("#sqwtsFileId").css("width","calc(100% - 180px)");
            setTimeout(function () {
                $(".fileShow").show();
            }, 400);
            if (!isNull(fileInterval)) {
                clearInterval(fileInterval);
            }
        }else {
            $("#sqwtsFileId").css("width","calc(100% - 90px)");
            $(".fileShow").hide();
        }
    }

    //文件预览按钮绑定
    function fileShowFun(obj) {
        $(obj).parent().find(".showUploadFile").click();
    }

    //身份证校验
    function isIdCard(value) {
        if (reg.test(value) === false) {
            return '身份证输入有误，请重新输入';
        }
    }

    //委托书不可为空
    function fileNotNull(value) {
        if (value == "" || value == null) {
            return '请上传委托书';
        }
    }

    //判断签到的进行状态
    function siginNow() {
        //判断文件递交时间外不可签到
        var timeStatus = timePass('${times.startTime}', '${times.endTime}', '${times.nowTime}');
        if (isNull('${bidderFileInfo.receiptId}')) {
            $(".fileNo").css("display", "block");
            return false;
        }
        if (timeStatus == 2) {
            $(".right-down3").css("display", "block");
            return false;
        }
        //判断是否在签到时间内或时间外
        if ('${lineStatus.signinStatus}' == 1) {
            if ('${bidderOpenInfo.urgentSigin}' == 1) {
                goToUrl('${ctx}/identityAuth/urgentSiginEndPage', $(".identityAuthLi"));
            } else if ('${bidderOpenInfo.authentication}' == 1) {
                goToUrl('${ctx}/identityAuth/identityAuthEnd', $(".identityAuthLi"));
            } else {
                if ('${bidderOpenInfo.ticketNo}' != null && '${bidderOpenInfo.ticketNo}' != "") {
                    goToUrl('${ctx}/identityAuth/identityAuthPage', $(".identityAuthLi"));
                } else {
                    $(".right-center").css("display", "block");
                }
            }

        } else if ('${lineStatus.signinStatus}' == 0) {
            $(".right-down3").css("display", "block");
        } else {
            $(".right-down2").css("display", "block");
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
        }
    }

    /*
  判断当前时间是否在时间范围内
  1 范围内
  2 未到时间
  3 超过时间
   */
    function timePass(startTime, endTime, nowTime) {
        var t1 = dateStrToMils(startTime);
        var t2 = dateStrToMils(endTime);
        //当前时间戳
        var timestamp = dateStrToMils(nowTime);
        ;
        console.log(t1, t2, timestamp);
        if (timestamp >= t1 && timestamp <= t2) {
            return 1;
        } else if (timestamp < t1) {
            return 2;
        } else {
            return 3;
        }
    }

    //时间字符串转换为时间戳
    function dateStrToMils(dateStr) {
        var date = dateStr;
        date = date.substring(0, 19);
        date = date.replace(/-/g, '/');

        var timestamp = new Date(date).getTime();
        return timestamp;
    }
</script>