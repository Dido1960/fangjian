<style>
    .currentConvertPdf{
        border-radius: 10px;
    }
</style>
<script>
    /**
     * 评标报告生成进度展示
     */
    function currentConvertPdf() {
        indexLoad = window.top.layer.open({
            type: 2,
            offset: 'c',
            shadeClose: false,
            scrollbar:false,
            area: ['55%', '750px'],
            skin: 'currentConvertPdf',
            btn: false,
            title: false,
            move: false,
            resize: false,
            content: '${ctx}/expert/showSysInfoPage',
            btn1: false
        });
    }


    /**
     * 用于展示pdf合成进度
     * @param targetUrl
     * @param e
     * @param msg
     */
    function goPdfToUrl(targetUrl, e, msg) {
        var iframe = $(".loadBox iframe")[0];
        // 添加load
        var indexLoad;
        if (msg != null) {
            try {
                hide_IWeb2018();
                iframe.contentWindow. hide_IWeb2018();
            }catch (e) {

            }
            // indexLoad=window.top.layer.msg(msg, {icon: 16, time: 0, shade: [0.3, '#393D49']});
            // 展示当前pdf生成进度
            currentConvertPdf();
        } else {
            hide_IWeb2018()
            indexLoad = layer.load();
        }
        var objId = $(e).parents("li").attr("id")
        baseCssChange(objId);


        iframe.src = targetUrl;
        if (iframe.attachEvent) {
            iframe.attachEvent("onload", function () {
                // layer.close(indexLoad);
                window.top.layer.closeAll();
            });
        } else {
            iframe.onload = function () {
                // layer.close(indexLoad);
                window.top.layer.closeAll();
            };
        }

        //ID存入缓存
        localStorage.setItem("endEvaluation_${bidSection.id}", objId);

        //显示当前进行的流程
        showProcessNow($(e).parents("li"));
    }

    /**
     * 重新生成评标报告
     * 不展示合成进度
     */
    function afreshReport() {
        indexLoad = window.top.layer.msg("清除签名中，请耐心等待...", {icon: 16, time: 0, shade: [0.3, '#393D49']});
        $.ajax({
            url: "${ctx}/expert/otherBidEval/reReport",
            type: "POST",
            cache: false,
            success: function (data) {
                window.location.reload();
            },
            error: function (e) {
                window.top.layer.closeAll();
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }

    /**
     * 清除签名的table
     */
    function restSign() {
        if ('${resignButton}'){
            hide_IWeb2018();
            layerConfirm("确定要重新签名吗?", function () {
                resetExpertSignStatus();
            });

            <#--var choose_expert_index = window.top.layer.open({-->
            <#--    type: 2,-->
            <#--    title: ["请选择要清除签名的专家", 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(0, 102, 204, 1);line-height:57px;padding:0;'],-->
            <#--    shadeClose: true,-->
            <#--    area: ['45%', '65%'],-->
            <#--    btn: ['确认', '取消'],-->
            <#--    content: '${ctx}/signature/chooseExpertUser',-->
            <#--    btn1: function (index, layero) {-->
            <#--        var body = window.top.layer.getChildFrame('body', index);-->
            <#--        var res = window.top[layero.find('iframe')[0]['name']].chooseExpertUser();-->
            <#--        if (res !== null && res !== undefined) {-->
            <#--            $("#expert_ids").val(res.id);-->
            <#--            resetExpertSignStatus();-->
            <#--            window.top.layer.close(choose_expert_index);-->
            <#--        } else {-->
            <#--            layerConfirm("您未选择清除签名专家，是否合成之前的签名?", function () {-->
            <#--                allSigarView();-->
            <#--            });-->
            <#--            window.top.layer.close(choose_expert_index);-->
            <#--        }-->
            <#--    },-->
            <#--    btn2: function (index) {-->
            <#--        window.top.layer.close(index);-->
            <#--        window.location.reload();-->
            <#--    },-->
            <#--});-->
        } else {
            hide_IWeb2018();
            layerAlert("有专家未签名，无法重新签名!",'','',2)
        }


    }

    /**
     * 重新签名
     */
    function afreshSignView() {
        hide_IWeb2018();
        var _index = window.top.layer.confirm('确定要重新签名吗？', {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            window.top.layer.close(_index)
            // 重置当前专家的签名状态
            resetExpertSignStatus();
        });
    }

    /**
     * 清除选择的专家签名状态
     */
    function resetExpertSignStatus() {
        var indexLoad = window.top.layer.load();
        $.ajax({
            url: '${ctx}/signature/afreshSign',
            type: 'post',
            cache: false,
            success: function (data) {
                window.top.layer.close(indexLoad)
                // 重新生成评标报告
                reGenerateReport();
            },
            error: function (data) {
                console.error(data);
                window.top.layer.close(indexLoad)
            },
        });
    }

    /**
     * 重新生成评标报告
     */
    function reGenerateReport() {
        hide_IWeb2018();
        var confirmWin = window.top.layer.confirm('确定重新生成吗？', {
                icon: 3,
                btn: ['确定', '取消'],
                cancel: function () {
                    show_IWeb2018();
                }
            }, function () {
                window.top.layer.close(confirmWin);
                var _index = window.top.layer.confirm('是否<span style="font-weight: bold;color: red">清除签名</span>信息？', {icon: 3,
                    btn: ['不清除','清除'] //按钮
                }, function(){
                    regenerateReportFunc(false)
                }, function(){
                    regenerateReportFunc(true)
                });
            }, function () {
            }
        );
    }

    /**
     * 电子签名
     */
    function eleSignView() {
        bidSectionId = "${bidSectionId}";
        expertIndex = 0;
        $.ajax({
            url: "/signature/getPdfSignInfo",
            type: "post",
            data: {
                "bidSectionId": bidSectionId,
                "index": expertIndex
            },
            success: function (data) {
                if (!isNull(data.errorMsg)){
                    hide_IWeb2018();
                    layerAlert(data.errorMsg)
                    return false;
                } else {
                    if (data.s == "y") {
                        //当前专家签名信息
                        signerJson = data.expertJson;
                        positionJson = data.positionJson;
                        wono = data.expertId;
                        nextExpertName = data.nextExpertName;
                        initSign();
                    } else {
                        allSigarView();
                    }
                }
            }
        })
    }

    // 合成用时
    var countNumLoadTimes = 0;

    /**
     * 所有专家签名结束，进行签名合成
     */
    function allSigarView() {
        countNumLoadTimes = 0;
        window.top.layer.closeAll();
        layerLoading("专家签名合成中，请耐心等待...（约3分钟）<span id='loadShowNowTime'></span>", null,7000);
        var wriInInterval = setInterval(function () {
            countNumLoadTimes++;
            $("#loadShowNowTime").html("已用时：<span class='red-f'>"+countNumLoadTimes+"s</span>");
        },1000);
        $.ajax({
            url: "/signature/signaturePdf",
            type: "POST",
            data: {
                "bidSectionId": "${bidSectionId}"
            },
            cache:false,
            success: function (data) {
                // 关闭layer弹窗
                loadComplete();
                if (eval(data)) {
                    layerSuccess("签名合成成功", function () {
                        window.location.reload();
                    })
                } else {
                    layerSuccess("请检查签名是否缺失！！");
                    clearInterval(wriInInterval);
                }
            },
            error: function () {
                layerSuccess("请检查签名是否缺失！！");
                clearInterval(wriInInterval);
            }
        });
    }


    /**
     * 电子签名提示信息
     */
    function showSignTip(){
        var str = "温馨提示:<br>";
        str+= "请在手写板中心区域签名<br>";
        showTips(".signa_tips",str);
        hide_IWeb2018();
    }

    /**
     * 重新生成报告
     */
    function regenerateReportFunc(delSignFlag){
        window.top.layer.closeAll();
        // 展示当前pdf生成进度
        currentConvertPdf();
        $.ajax({
            url: '${ctx}/expert/evalPlan/reReport',
            type: "POST",
            cache: false,
            data: {delExertSigarInfo : delSignFlag},
            success: function (data) {
                window.location.reload();
            },
            error: function (e) {
                window.top.layer.closeAll();
                console.error(e);
                if(e.status === 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }


    /**
     * 重新签名
     */
    function showAgainSignTip(){
        var str = "温馨提示:<br>";
        str+= "1、请在手写板中心区域签名<br>";
        str+= "2、签错名时,请点击重新签名<br>";
        str+= "3、报告重新生成,请点重新签名<br>";
        showTips(".signa_agin_tips",str);
    }

    /**
     * 展示tips
     * @param id 目标元素
     * @param msg 提示信息
     */
    function showTips(id,msg){
        layer.tips(msg, id, {
            tips: [2, '#5FB878'], //还可配置颜色
            time:0
        });
    }
</script>