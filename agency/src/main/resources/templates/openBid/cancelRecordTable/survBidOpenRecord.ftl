<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助开标系统</title>
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
    <script src="${ctx}/js/convertMoney.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/bidOpenRecord.css">
    <#include "${ctx}/openBid/cancelRecordTable/publicOpenRecord.ftl"/>
</head>
<body style="overflow-x: hidden;">
<div class="cen-btn" onclick="cancelOutBidOpenRecord()" style="margin-right: auto; margin-left: auto">生成开标记录表</div>
<div class="cen-set">
    <h3>开标记录表</h3>
    <form action="">
        <ul class="shang">
            <li>
                <label for="name">标段名称</label>
                <input type="text" id="name" class="layui-bg-gray" value="${(bidSection.bidSectionName)!""}" readonly>
            </li>
            <li>
                <label for="hum">标段编号</label>
                <input type="text" id="hum" class="layui-bg-gray" value="${(tenderProject.tenderProjectCode)!""}" readonly>
            </li>
            <li>
                <label for="hum">招标人</label>
                <input type="text" id="hum" class="layui-bg-gray" value="${(tenderProject.tendererName)!""}" readonly>
            </li>
            <li>
                <label for="test1">开标时间</label>
                <input id="test1" class="layui-bg-gray" value="${(tenderDoc.bidOpenTime)!""}" readonly>
            </li>
            <li style="width: 100%">
                <label for="bidOpenPlace">开标地点</label>
                <input type="text" id="bidOpenPlace" value="${(tenderDoc.bidOpenPlace)!""}" style="width: 84%" onblur="validData()">
            </li>
        </ul>
        <table class="layui-table bid-open-table">
            <thead>
            <tr>
                <th>序号</th>
                <th>投标人</th>
                <th>投标总价（元）</th>
                <th>投标工期（日历天）</th>
                <th>法定代表人<br>或委托代理人签字</th>
            </tr>
            </thead>
            <tbody>
            <#if bidders?? && bidders?size gt 0>
                <#list bidders as bidder>
                    <tr>
                        <td>${bidder_index+1}</td>
                        <td>${(bidder.bidderName)!""}</td>
                        <td>
                            <#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
                                <#if bidder.bidderOpenInfo.bidPrice?? && bidder.bidderOpenInfo.bidPrice !=''>
                                    ${((bidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                                </#if>
                            <#else>
                                ${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}
                            </#if>
                        </td>
                        <td>${(bidder.bidderOpenInfo.timeLimit)!""}</td>
                        <td>
                            <#if bidSection.bidOpenOnline != 0>
                                ${(bidder.bidderOpenInfo.clientName)!""}
                            </#if>
                        </td>
                    </tr>
                </#list>
            <#else>
                <tr>
                    <td>1</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </#if>
            </tbody>
            <tfoot>
            <tr class="bei" style="height: 100px;">
                <td>备注</td>
                <td colspan="4" style="padding: 0!important;">
                <textarea style="width: 100%; height: 100px; border: none;resize: none; white-space: pre-wrap" maxlength="200" onblur="updateDesc(this)"><#if tenderDoc.openBidRecordDes??>${tenderDoc.openBidRecordDes}<#else>
                        <#if biddersFail??>
                            <#list biddersFail as bidderFail>
                                <#if bidderFail.bidderOpenInfo??>
                                    ${bidderFail_index+1}.${bidderFail.bidderName}:
                                    <#if bidderFail.bidderOpenInfo.tenderRejection==1>
                                        标书拒绝,标书拒绝理由：${bidderFail.bidderOpenInfo.tenderRejectionReason} ;
                                    <#elseif bidderFail.bidderOpenInfo.notCheckin==1>
                                        未签到,未签到原因:迟到
                                    <#elseif bidderFail.bidderOpenInfo.notCheckin==2>
                                        未签到,未签到原因:弃标
                                    <#elseif bidderFail.bidderOpenInfo.notCheckin==9>
                                        未签到,未签到原因:${bidderFail.bidderOpenInfo.notCheckinReason}
                                    </#if>
                                    <br>
                                </#if>
                            </#list>
                        </#if>
                    </#if>
                </textarea>
                </td>
            </tr>
            <tr>
                <td class="more-all" style="border-right: #fff">监标人：</td>
                <td colspan="4" style="border-left: #fff"></td>
            </tr>
            </tfoot>
        </table>
    </form>
</div>
<script>
    /*$(function () {
        if (!isNull('${flag}') && '${flag}' == "2") {
            window.location.reload();
        }
    });*/
    // 修改开标地点
    function validData() {
        // 触发表单验证
        $("#submitForm").trigger("click");
        var open_place = $("#bidOpenPlace").val();
        if (open_place.trim().length <= 0) {
            return;
        }
        // 保存开标地点
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateTenderDoc',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: ${bidSection.id},
                bidOpenPlace: open_place
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    if (data) {
                        window.top.parent.removeSingingVioce();
                    }
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 点击生成pdf输出到页面
     */
    function cancelOutBidOpenRecord() {
        layerLoading("开标记录表生成中，请稍等...");
        $.ajax({
            url: '${ctx}/staff/createRecordTable',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId: ${bidSection.id},
                bidOpenPlace: $("#bidOpenPlace").val()
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    layer.msg('生成成功！', {icon: 1});
                    setTimeout(function () {
                        window.location.href = "${ctx}/staff/cancelBidReportPage/1";
                    }, 1500);
                } else {
                    layer.msg("生成失败！", {icon: 5});
                }
                // window.top.layer.closeAll();

                // 加载消息盒子
                showMinOnlinePage();
            },
            error: function (data) {
                layer.msg("生成失败！", {icon: 5});
                console.error(data);
                // window.top.layer.closeAll();

                // 加载消息盒子
                showMinOnlinePage();
            },
        });
    }
</script>
</body>
