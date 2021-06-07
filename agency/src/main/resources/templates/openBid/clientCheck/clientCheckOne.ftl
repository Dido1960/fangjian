<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>身份检查</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/clientCheckOne.css">
    <style>

        /*去除disable默认样式*/
        .layui-radio-disbaled > i{
            color: rgb(19, 97, 254) !important;
        }
    </style>
</head>
<body>
<div class="sussTan">
    <div class="cons">
        <div class="left">
            <h4 id="checkNum"></h4>
            <div class="zuo">
                <span id="identityStatus" class="doubt"></span>
                <input type="hidden" id="boiId">
                <p id="bidderName"></p>
                <p id="bidderIdCard"></p>
                <img id="bidderPhoto" src="">
            </div>

            <div class="inut layui-form">
                <div class="inut-l" <#if clientCheckOne.isA10Bid>style="display: none"</#if>>
                    <label for="">保证金/保函状态</label>
                    <p><label for="">保证金：</label>
                        <input class="layui-form-radio" type="radio" <#if bidderOpenInfo.marginPayStatus == 1 >checked</#if> name="marginPay" value="1" id="marginPay_1" <#if clientCheckOne.isShow>disabled</#if>>
                        <span>已缴纳</span>
                        <input class="layui-form-radio" type="radio"  <#if bidderOpenInfo.marginPayStatus == 0 >checked</#if> name="marginPay" value="0" id="marginPay_0" <#if clientCheckOne.isShow>disabled</#if>>
                        <span>未缴纳</span>
                    </p>
                    <p><label for="">保函：</label>
                        <input class="layui-form-radio" type="radio"  <#if bidderOpenInfo.marginPayStatus == 2 >checked</#if>   name="marginPay" value="2" id="marginPay_2" <#if clientCheckOne.isShow>disabled</#if>>
                        <span>已递交</span>
                    </p>
                    <input type="hidden" id="marginPayInput" value="">
                </div>
                <div class="<#if clientCheckOne.isA10Bid>inut-center<#else >inut-r</#if>">
                    <label for="">投标人身份检查</label>
                    <input id="switchIdentity" type="checkbox" lay-filter="identity" class="swithCheck" lay-skin="switch"
                           checked="checked"
                           <#if clientCheckOne.isShow>disabled</#if> lay-text="符合|不符合">
                    <input type="hidden" id="identityInput" value="">
                </div>
            </div>
            <form action="">
                <label>填写原因<span>(身份检查)</span></label>
                <textarea name="" id="identityReasonInput"
                          class="<#if clientCheckOne.isA10Bid>checkOne<#else>checkTwo</#if>"
                          <#if clientCheckOne.isShow>disabled</#if> ></textarea>
                <input id="identityReasonHidden" type="hidden" value="">

                <div id="marginPayReason" <#if clientCheckOne.isA10Bid>style="display: none"</#if>>
                    <label>填写原因<span>(保证金检查)</span></label>
                    <textarea name="" id="marginPayReasonInput" class="checkTwo"
                              <#if clientCheckOne.isShow>disabled</#if>></textarea>
                    <input id="marginPayReasonHidden" type="hidden" value="">
                </div>
                <div class="submit" onclick="saveReason()" <#if clientCheckOne.isShow>style="display: none" </#if>>保存
                </div>
            </form>
        </div>
        <div class="you">
            <h3 id="bidderBigName"></h3>
            <div class="pdf-file-list" style="display: none">
                <div class="choice" fdfsMark=""></div>
            </div>
            <div class="xin conts pdf">
                <#--是否帮助按钮-->
                <#assign showHelpBtn="true"/>
                <#--是否启用本地缓存机制-->
                <#assign localCache="false"/>
                <#--是否开启另存为按钮-->
                <#assign showSaveAs="true"/>
                <#--是否开启另存为按钮-->
                <#assign fullScreen="true"/>
                <#--1.必须存在class 为(pdf-file-list文件)-->
                <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
                <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
                <#include "/common/showPDFView.ftl"/>
            </div>
            <div class="foot">
                <span id="lastBoi" class="back" onclick="goToOther(this)"></span>
                <span id="nextBoi" class="go" onclick="goToOther(this)"></span>
            </div>
        </div>
    </div>
</div>

<script>
    function goToOther(obj) {
        //数据获取
        var goToBidder = $(obj).attr("data-value");
        if (isNull(goToBidder)) {
            return false;
        }
        getInitData(goToBidder);
    }

    //监听switch
    layui.use(['form'], function () {
        var form = layui.form;
        form.on('switch(identity)', function (data) {
            clientCheck("identity", data);
        });
        form.on('radio', function (data) {
            clientCheck("marginPay", data);
        });
        form.render('checkbox');
        form.render('radio');
    });

    function formRender() {
        layui.use(['form'], function () {
            var form = layui.form;
            form.render('radio');
        });
    }

    //swich点击修改数据
    function clientCheck(checkType, data) {
        var boiId = $("#boiId").val();
        var passType;
        if (checkType === "marginPay") {
            passType = $(data.elem).val();
        } else {
            passType = 0;
            if (data.elem.checked) {
                passType = 1;
            }
        }

        $.ajax({
            url: "${ctx}/clientCheck/clientCheckThis",
            type: "POST",
            cache: false,
            async: true,
            data: {
                "boiId": boiId,
                "checkType": checkType,
                "passType": passType
            },
            success: function (data) {
                if (data) {

                }
            }
        });
        if (checkType == "identity") {
            var $identityReasonInput = $("#identityReasonInput");
            var $identityReasonHidden = $("#identityReasonHidden");
            var $identityInput = $("#identityInput");
            if (!data.elem.checked) {
                $identityReasonInput.removeAttr("disabled").focus().val($identityReasonHidden.val());
                $identityInput.val(0);
            } else {
                $identityReasonInput.attr("disabled", "disabled");
                $identityReasonHidden.val($identityReasonInput.val());
                $identityReasonInput.val("");
                $identityInput.val(1);
            }
        } else {
            var $marginPayReasonInput = $("#marginPayReasonInput");
            var $marginPayReasonHidden = $("#marginPayReasonHidden");
            var $marginPayInput = $("#marginPayInput");
            $marginPayInput.val(passType);
            if (passType == 0) {
                $marginPayReasonInput.removeAttr("disabled").focus().val($marginPayReasonHidden.val());
            } else {
                $marginPayReasonInput.attr("disabled", "disabled");
                $marginPayReasonHidden.val($marginPayReasonInput.val());
                $marginPayReasonInput.val("");
            }
        }
    }

    //保存理由
    function saveReason() {
        var $identityInput = $("#identityInput");
        var $marginPayInput = $("#marginPayInput");
        console.log($marginPayInput.val());
        if ($identityInput.val() == 0 || $marginPayInput.val() == 0) {
            layerLoading("保存成功！", 1, 2);
        }
    }

    var lock1 = false;

    function sendReasonDate(id, eType, reason, boiId, whoDo) {
        if (lock1) {
            return false
        }
        lock1 = true;
        $.ajax({
            url: "${ctx}/clientCheck/saveReason",
            type: "POST",
            cache: false,
            async: true,
            data: {
                "bidSectionId": '${clientCheckOne.bidSectionId}',
                "BidderOpenInfoId": boiId,
                "exceptionType": eType,
                "exceptionReason": reason,
                "id": id
            },
            success: function (data) {
                lock1 = false;
                if (whoDo == "click") {
                    if (data) {
                        layerLoading("保存成功！", 1, 2);
                    } else {
                        layerLoading("保存失败！", 2, 2);
                    }
                }
            },
            error: function (e) {
                lock1 = false;
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }

    //理由失焦事件绑定
    $("#identityReasonInput").blur(function () {
        if ($("#identityInput").val() == 0 && $("#identityReasonInput").val() != "") {
            sendReasonDate('${clientCheckOne.identityBidderException.id}', 1, $("#identityReasonInput").val(), $("#boiId").val(), "blur");
        }
    });
    $("#marginPayReasonInput").blur(function () {
        if ($("#marginPayInput").val() == 0 && $("#marginPayReasonInput").val() != "") {
            sendReasonDate('${clientCheckOne.marginPayBidderException.id}', 2, $("#marginPayReasonInput").val(), $("#boiId").val(), "blur");
        }
    });

</script>

<script>

    $(function () {
        getInitData('${clientCheckOne.bidderIdTo}');
    });

    function getInitData(bidderIdTo) {
        $.ajax({
            url: "${ctx}/clientCheck/getInitData",
            type: "POST",
            cache: false,
            data: {
                "bidderIdTo": bidderIdTo,
                "bidSectionId": '${clientCheckOne.bidSectionId}'
            },
            success: function (data) {
                if (data) {
                    pageInit(data);
                }
            },
            error: function (e) {
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
                console.error(e);
            }
        });
    }

    function pageInit(data) {
        //检查人数更新
        $("#checkNum").text("已检查" + data.isCheck + "家投标人，还剩" + data.notCheck + "家投标人");
        //认证状态
        if (data.bidderOpenInfo.urgentSigin == 1) {
            $("#identityStatus").removeClass().addClass("doubt").text("身份存疑")
        } else if (data.bidderOpenInfo.authentication == 1) {
            $("#identityStatus").removeClass().addClass("piao").text("实名成功")
        } else {
            $("#identityStatus").removeClass().addClass("doubt").text("实名失败")
        }
        if (!isNull(data.bidderOpenInfo.clientName)){
            //投标人信息
            $("#bidderName").text("姓名：" + data.bidderOpenInfo.clientName);
        }else {
            $("#bidderName").text("姓名：");
        }

        if (!isNull(data.bidderOpenInfo.clientIdcard)){
            $("#bidderIdCard").text("身份证号：" + data.bidderOpenInfo.clientIdcard);
        }else {
            $("#bidderIdCard").text("身份证号：");
        }

        var bidderPhoto;
        if (data.bidderOpenInfo.urgentSigin == 1) {
            bidderPhoto = data.bidderOpenInfo.photoUrl;
        } else {
            bidderPhoto = isNull(data.bidderOpenInfo.sqrPicUrl) ? "${ctx}/img/identity_default.png" : data.bidderOpenInfo.sqrPicUrl;
        }

        $("#bidderPhoto").attr("src",bidderPhoto);
        $("#boiId").val(data.bidderOpenInfo.id);
        //检查情况
        if (data.bidderOpenInfo.bidderIdentityStatus == 0) {
            $("#switchIdentity").removeAttr("checked");
            $("#switchIdentity").next().removeClass('layui-form-onswitch');
            $("#switchIdentity").next().find("em").text("不符合");
        } else {
            $("#switchIdentity").attr("checked", "checked");
            $("#switchIdentity").next().addClass('layui-form-onswitch');
            $("#switchIdentity").next().find("em").text("符合");
        }

        $("#identityInput").val(data.bidderOpenInfo.bidderIdentityStatus == 0 ? 0 : 1);
        //原因
        $("#identityReasonInput").val("");
        $("#identityReasonHidden").val("");
        $("#identityReasonInput").attr("disabled", data.bidderOpenInfo.bidderIdentityStatus == 0 ? false : "disabled");
        if ('${clientCheckOne.isShow}' == "true") {
            $("#identityReasonInput").attr("readonly", "readonly");
        }
        if (!isNull(data.identityBidderException)) {
            $("#identityReasonInput").val(data.bidderOpenInfo.bidderIdentityStatus == 0 ? data.identityBidderException.exceptionReason : "");
            $("#identityReasonHidden").val(data.identityBidderException.exceptionReason);
        }

        if (!data.isA10Bid) {
            var mStatus = 0;
            if (!isNull(data.bidderOpenInfo.marginPayStatus)){
                mStatus = data.bidderOpenInfo.marginPayStatus;
            }
            $("#marginPay_"+mStatus).prop('checked', 'checked')

            $("#marginPayInput").val(mStatus);
            //原因
            $("#marginPayReasonInput").val("");
            $("#marginPayReasonHidden").val("");
            $("#marginPayReasonInput").attr("disabled", mStatus == 0 ? false : "disabled");
            if ('${clientCheckOne.isShow}' == "true") {
                $("#marginPayReasonInput").attr("readonly", "readonly");
            }
            if (!isNull(data.marginPayBidderException)) {
                $("#marginPayReasonInput").val(mStatus == 0 ? data.marginPayBidderException.exceptionReason : "");
                $("#marginPayReasonHidden").val(data.marginPayBidderException.exceptionReason);
            }
        }
        formRender();

        $("#bidderBigName").text("投标人：" + data.bidderOpenInfo.bidderName).attr("title", data.bidderOpenInfo.bidderName);
        if (!isNull(data.lastBoi)) {
            $("#lastBoi").text("上一家：" + data.lastBoi.bidderName).attr("data-value", data.lastBoi.id).attr("title", data.lastBoi.bidderName);
        } else {
            $("#lastBoi").text("上一家：已经是第一家了").attr("data-value", null);
        }

        if (!isNull(data.nextBoi)) {
            $("#nextBoi").text("下一家：" + data.nextBoi.bidderName).attr("data-value", data.nextBoi.id).attr("title", data.nextBoi.bidderName);
        } else {
            $("#nextBoi").text("下一家：已经是最后一家了").attr("data-value", null);
        }
        loadPdf(data.bidderOpenInfo.sqwtsMark);
    }
</script>
</body>
</html>