<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
<!--[if lt IE 9]>
<script src=${ctx}"/js/html5shiv.min.js"></script>
<script src=${ctx}"/js/respond.min.js"></script>
<![endif]-->
<script src="${ctx}/js/common.js"></script>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
<script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<link rel="stylesheet" href="${ctx}/css/header.css">
<link rel="stylesheet" href="${ctx}/css/utils.css">
<link rel="stylesheet" href="${ctx}/css/pricePoints.css">
<section>
    <table class="layui-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>投标单位</th>
            <th>评标价（元）</th>
            <th>评标基准价（元）</th>
            <th>差值百分比（%）</th>
            <th>评价标得分</th>
        </tr>
        </thead>
        <tbody>
            <#list quoteScoreResultAppendixList as quoteScoreResultAppendix>
                <tr>
                    <td>${quoteScoreResultAppendix_index + 1}</td>
                    <td style="text-align: left">${quoteScoreResultAppendix.bidderName}</td>
                    <td>
                        <#if quoteScoreResultAppendix.bidPrice?? && quoteScoreResultAppendix.bidPrice != ''>
                            ${quoteScoreResultAppendix.bidPrice?number?string(",###.00")}
                        </#if>
                    </td>
                    <#if quoteScoreResultAppendix_index == 0>
                        <td rowspan="${quoteScoreResultAppendixList?size}">
                            <input type="text" style="width: 160px;height: 30px" name="updateBasePrice" onkeyup="clearNoNum(this)" onblur="updateCalcParam(this)" data-param-id="${calcScoreParam.id}"
                                   value="<#if calcScoreParam.updateBasePrice?? && calcScoreParam.updateBasePrice != "">${calcScoreParam.updateBasePrice}<#else>${calcScoreParam.basePrice}</#if>">
                        </td>
                    </#if>
                    <td>
                        <input type="text" style="width: 160px;height: 30px" name="bidPriceOffset" onkeyup="clearHaveFuNoNum(this)"
                               onblur="updateAppendix(this)" data-appendix-id="${quoteScoreResultAppendix.id}" value="${quoteScoreResultAppendix.bidPriceOffset}">
                    </td>
                    <td>
                        <input type="text" style="width: 160px;height: 30px" name="bidPriceScore" onkeyup="clearNoNum(this)"
                               onblur="updateAppendix(this)" data-appendix-id="${quoteScoreResultAppendix.id}" value="${quoteScoreResultAppendix.bidPriceScore}">
                    </td>
                </tr>
            </#list>
            <tr>
                <td>填写修正原因</td>
                <td colspan="5">
                    <textarea name="updateScoreReason" data-bidsection-id="${bidSection.id}" onblur="updateScoreReason(this)" maxlength="100" placeholder="请输入价格分修正原因" style="height: 100px; resize: none; width: 100%"></textarea>
                </td>
            </tr>
        </tbody>
    </table>
</section>
<script>
    function clearNoNum(obj) {
        //清除“数字”和“.”以外的字符
        obj.value = obj.value.replace(/[^\d.]/g, "");
        //验证第一个字符是数字而不是.
        obj.value = obj.value.replace(/^\./g, "");
        //只保留第一个. 清除多余的.
        obj.value = obj.value.replace(/\.{2,}/g, ".");
        obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        //防止用户输入01类型的整数
        if (obj.value !== '' && obj.value.indexOf('.') === -1) {
            obj.value = parseFloat(obj.value)
        }
    }

    function clearHaveFuNoNum(obj) {
        //清除“数字”和“.”以外的字符
        obj.value = obj.value.replace(/[^\d.-]/g, "");
        //验证第一个字符是数字而不是.
        obj.value = obj.value.replace(/^\./g, "");
        obj.value = obj.value.replace(/^\-\./g, "");
        //只保留第一个. 清除多余的.
        obj.value = obj.value.replace(/\.{2,}/g, ".");
        obj.value = obj.value.replace(/\-{2,}/g, "-");
        obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    }
    /**
     * 修改基准价
     * @param e
     */
    function updateCalcParam(e) {
        var paramId = $(e).data("param-id");
        var updateBasePrice = $(e).val();
        //防止用户输入0.数据
        if (updateBasePrice == "0."){
            updateBasePrice = 0;
        }
        if (isNull(updateBasePrice)){
            return false;
        }
        if (updateBasePrice.toString().split(".").length > 1 && updateBasePrice.toString().split(".")[1].length > 2){
            layer.tips("输入的【"+updateBasePrice+"】位数大于两位，请重新输入！",  $(e), {
                tips: [3, 'rgba(59, 135, 103, 1)'],
                time: 4000
            });
            $(e).val("");
            return false;
        }
        $.ajax({
            url: "${ctx}/expert/epcBidEval/updateCalcParam",
            type: "POST",
            cache: false,
            data: {
                id: paramId,
                updateBasePrice: updateBasePrice
            },
            success: function (result) {
                if (!result) {
                    layer.msg("保存失败！", {time: 3000, icon:2});
                }
            },
            error: function () {
                layer.msg("操作失败！", {time: 3000, icon:2});
            }
        });
    }

    /**
     * 修改投标人价格分附录
     * @param e
     */
    function updateAppendix(e) {
        var msg = "";
        var data = {};
        data.id = $(e).data("appendix-id");
        var val = $(e).val();
        //防止用户输入0.数据
        if (val == "0."){
            val = 0;
        }
        if (isNull(val)){
            return false;
        }
        var name = $(e).attr("name");
        if (val.toString().split(".").length > 1 && val.toString().split(".")[1].length > 2){
            msg = "输入的【"+val+"】位数大于两位，请重新输入！";
        }
        if (name === "bidPriceOffset") {
            data.bidPriceOffset = val;
        } else if (name === "bidPriceScore") {
            if (val > parseFloat('${calcScoreParam.totalScore}') || val < 0) {
                msg = "请输入0-${calcScoreParam.totalScore}的数值！"
            } else {
                data.bidPriceScore = val;
            }
        }

        if (!isNull(msg)) {
            layer.tips(msg,  $(e), {
                tips: [3, 'rgba(59, 135, 103, 1)'],
                time: 4000
            });
            $(e).val("");
            return
        }

        $.ajax({
            url: "${ctx}/expert/epcBidEval/updateAppendix",
            type: "POST",
            cache: false,
            data: data,
            success: function (result) {
                if (!result) {
                    layer.msg("保存失败！", {time: 3000, icon:2});
                }
            },
            error: function () {
                layer.msg("操作失败！", {time: 3000, icon:2});
            }
        });
    }

    /**
     * 更新报价分计算修正原因
     * @param e
     */
    function updateScoreReason(e) {
        var bidSectionId = $(e).data("bidsection-id");
        var updateScoreReason = $(e).val().trim();
        if (isNull(updateScoreReason)){
            layer.msg("理由不能为空哦!")
            return false;
        }
        if (updateScoreReason.length>100){
            layerAlert("输入修正原因过长，系统自动保存前100个字符");
            // 防止理由过长，导致异常
            updateScoreReason = updateScoreReason.substr(0,100);
        }
        var data = {}
        data.id = bidSectionId;
        data.updateScoreReason = updateScoreReason;
        data.updateScoreStatus = 1;
        updateBidSection(data);
    }

    /**
     * 更新报价分计算修正
     * @param data 更新的数据封装
     */
    function updateBidSection(data) {
        $.ajax({
            url: "${ctx}/expert/evalPlan/updateBidSection",
            type: "POST",
            cache: false,
            data: data,
            success: function (result) {
                if (!result) {
                    layer.msg("保存失败！", {time: 3000, icon:2});
                }
            },
            error: function () {
                layer.msg("操作失败！", {time: 3000, icon:2});
            }
        });
    }
</script>