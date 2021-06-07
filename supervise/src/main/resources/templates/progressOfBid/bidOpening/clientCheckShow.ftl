<link rel="stylesheet" href="${ctx}/css/clientCheckShow.css">
<div class="loadClientCheck">
    <div class="sussTan">
        <div class="cons">
            <div class="left">
                <h4>已检查${clientCheckOne.isCheck}家投标人，还剩${clientCheckOne.notCheck}家投标人</h4>
                <div class="zuo">
                    <#if clientCheckOne.bidderOpenInfo.urgentSigin?? && clientCheckOne.bidderOpenInfo.urgentSigin == 1>
                        <span class="doubt">身份存疑</span>
                    <#else>
                        <#if clientCheckOne.bidderOpenInfo.authentication==1>
                            <span class="piao">实名成功</span>
                        <#else >
                            <span class="doubt">实名失败</span>
                        </#if>
                    </#if>
                    <p>姓名：${clientCheckOne.bidderOpenInfo.clientName}</p>
                    <p>身份证号：${clientCheckOne.bidderOpenInfo.clientIdcard} </p>
                    <#if clientCheckOne.bidderOpenInfo.urgentSigin?? && clientCheckOne.bidderOpenInfo.urgentSigin == "1">
                        <img src="${clientCheckOne.bidderOpenInfo.photoUrl}" style="border-radius: 5px;border: 1px solid #eee" alt=""/>
                    <#else>
                        <#if clientCheckOne.bidderOpenInfo.sqrPicUrl?? && clientCheckOne.bidderOpenInfo.sqrPicUrl != "">
                            <img src="${clientCheckOne.bidderOpenInfo.sqrPicUrl}" style="border-radius: 5px;border: 1px solid #eee" alt=""/>
                        <#else>
                            <img src="${ctx}/img/identity_default.png" style="border-radius: 5px;border: 1px solid #eee" alt=""/>
                        </#if>
                    </#if>
                </div>
            </div>
            <div class="you">
                <h3 title="${clientCheckOne.bidderOpenInfo.bidderName}">投标人：${clientCheckOne.bidderOpenInfo.bidderName}</h3>
                <div class="xin">

                </div>
                <div class="foot1">
                    <#if clientCheckOne.lastBoi??>
                        <span class="back" onclick="goToOther('${clientCheckOne.lastBoi.id}')" title="${clientCheckOne.lastBoi.bidderName}">
                             <span>上一家</span> ${clientCheckOne.lastBoi.bidderName}
                        </span>
                    <#else>
                        <span class="back"><span>上一家</span> 已经是第一家了</span>
                    </#if>

                    <#if clientCheckOne.nextBoi??>
                        <span class="go" onclick="goToOther('${clientCheckOne.nextBoi.id}')" title="${clientCheckOne.nextBoi.bidderName}">
                            <span>下一家</span> ${clientCheckOne.nextBoi.bidderName}
                        </span>
                    <#else>
                        <span class="go">
                            <span>下一家</span> 已经是最后一家了
                        </span>
                    </#if>
                </div>
            </div>

        </div>
    </div>
</div>
<script>
    /**
     * 切换投标人
     * @param id 开标信息id
     */
    function goToOther(id) {
        var indexLoad = layer.load();
        $(".loadClientCheck").parent().load("${ctx}/gov/bidOpen/clientCheckShowPage?bidderIdTo=" + id + "&isShow=${clientCheckOne.isShow}"
            , function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
                layer.close(indexLoad);
            });
    }

    //监听switch
    layui.use(['form'], function () {
        var form = layui.form;
        form.render('checkbox');
        form.render('radio');
        loadPdf('${clientCheckOne.bidderOpenInfo.sqwtsFdfsId}');

        form.on('switch(identity)', function (data) {
            clientCheck("identity", data);
        });
        form.on('radio(marginPay)', function (data) {
            clientCheck("marginPay", data);
        });
    });

    /**
     * swich点击修改数据
     */
    function clientCheck(checkType, data) {
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
            url: "${ctx}/bidOpening/clientCheckThis",
            type: "POST",
            cache: false,
            async: true,
            data: {
                "boiId": $(data.elem).attr("data-value"),
                "checkType": checkType,
                "passType": passType
            },
            success: function (data) {
                if (data) {
                    layer.msg('操作成功！', {icon: 1});
                } else {
                    layer.alert('操作失败', {
                        icon: 5,
                        btnAlign: 'c',
                        end: function () {
                            window.location.reload();
                        }
                    });
                }
            },
            error: function (e) {
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
        if (checkType == "identity") {
            if (!data.elem.checked) {
                $("#identityReasonInput").removeAttr("disabled");
                $("#identityReasonInput").focus();
                $("#identityReasonInput").val($("#identityReasonHidden").val());
                $("#identityReasonInput").removeAttr("readonly");
                $("#identityInput").val(0);
            } else {
                $("#identityReasonInput").attr("disabled", "disabled");
                $("#identityReasonHidden").val($("#identityReasonInput").val());
                $("#identityReasonInput").val("");
                $("#identityReasonInput").attr("readonly", "readonly");
                $("#identityInput").val(1);
            }
        } else {
            if ($(data.elem).val() == "0") {
                $("#marginPayReasonInput").removeAttr("disabled");
                $("#marginPayReasonInput").focus();
                $("#marginPayReasonInput").val($("#marginPayReasonHidden").val());
                $("#marginPayReasonInput").removeAttr("readonly");
                $("#marginPayInput").val(0);
            } else {
                $("#marginPayReasonInput").attr("disabled", "disabled");
                $("#marginPayReasonHidden").val($("#marginPayReasonInput").val());
                $("#marginPayReasonInput").val("");
                $("#marginPayReasonInput").attr("readonly", "readonly");
                $("#marginPayInput").val(1);
            }
        }
    }

    /**
     * 加载pdf
     */
    function loadPdf(id) {
        $(".xin").load("${ctx}/gov/bidOpen/showCheckPdfIframe?fileId=" + id);
    }

</script>
