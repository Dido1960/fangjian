<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>甘肃省公路工程电子评标辅助系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
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
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/pricePoints.css">
</head>
<body>
<#include "${ctx}/include/header.ftl">
<section>
    <div class="process">
        <h3>
            <span onclick="returnUpFlow()">返回</span>
            <p title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</p>
        </h3>
        <ul>
            <li class="sele">
                <a href="javascript:void(0)">
                    <div class="flow-num">1
                    </div>
                    <p>报价评审</p>
                </a>
            </li>
        </ul>
    </div>
    <div class="cont">
        <h3>1、投标报价评标基准价计算方法：</h3>
        <p style="text-indent: 2em">通过资格审查和初步 评审后的投标人的报价为有效报价。有效报价去掉一个最高价、去掉一个最低价后的算术平均值为评标基准价；当有效报价为3家时，则3家有效报价的算术平均值为评标基准价。</p>
        <h3>2、投标报价的偏差率计算公式：</h3>
        <p style="text-indent: 2em">偏差率=100%×（投标人报价-评标基准价）/评标基准价</p>
        <h3>3、投标报价评分标准(${calcScoreParam.totalScore } 分)：</h3>
        <p style="text-indent: 2em">${calcScoreParam.evalPriceScoreMethodDesc}</p>
        <p style="text-indent: 2em">注：偏离不足1%的，按照插入法计算得分，结果四舍五入保留2位小数。</p>
        <p></p>
        <div class="cont-btn">
            <#if !currentGrade.groupEnd?? || (currentGrade.groupEnd?? && currentGrade.groupEnd != 1)>
                <#if expert.isChairman == "1">
                    <span class="green-b" onclick="updatePriceScore()">价格分修改</span>
                </#if>
                <#if !expertReview.enabled?? || (expertReview.enabled?? && expertReview.enabled != 1)>
                    <span class="yellow-b" onclick="personalReviewEnd()" style="width: 116px;">个人评审结束</span>
                <#else>
                    <#if expert.isChairman == "1">
                        <span class="yellow-b" onclick="validDetailedGroupEnd()" style="width: 116px;">小组评审结束</span>
                    </#if>
                </#if>
            </#if>
        </div>
        <div class="document">
            <ol>
                <li class="small">序号</li>
                <li class="long">投标单位</li>
                <li>评标价（元）</li>
                <li>评标基准价（元)</li>
                <li>差值百分比（%)</li>
                <li>评标价得分</li>
            </ol>
            <div class="cont-table">
                <table cellpadding=0 cellspacing=0>
                    <#if bidSection.priceRecordStatus?? && bidSection.priceRecordStatus == 1>
                        <#list bidders as bidder>
                            <tr>
                                <td class="small">${bidder_index + 1}</td>
                                <td class="long">${bidder.bidderName}</td>
                                <td>
                                    <#if bidder.quoteScoreResult.bidPrice?? && bidder.quoteScoreResult.bidPrice != ''>
                                        ${bidder.quoteScoreResult.bidPrice?number?string(",###.00")}
                                    </#if>
                                </td>
                                <#if bidder_index == 0>
                                    <td class="average" rowspan="${bidders?size}">
                                        <#if calcScoreParam.basePrice?? && calcScoreParam.basePrice != ''>
                                            ${calcScoreParam.basePrice?number?string(",###.00")}
                                        </#if>
                                    </td>
                                </#if>
                                <td>${bidder.quoteScoreResult.bidPriceOffset}</td>
                                <td>${bidder.quoteScoreResult.bidPriceScore}</td>
                            </tr>
                        </#list>
                    <#else>
                        <#list bidders as bidder>
                            <tr>
                                <td  class="small">${bidder_index + 1}</td>
                                <td class="long">${bidder.bidderName}</td>
                                <td>
                                    <#if bidder.quoteScoreResult.bidPrice?? && bidder.quoteScoreResult.bidPrice != ''>
                                        ${bidder.bidderOpenInfo.bidPrice?number?string(",###.##")}
                                    </#if>
                                </td>
                                <#if bidder_index == 0>
                                    <td class="average" rowspan="${bidders?size}"></td>
                                </#if>
                                <td></td>
                                <td></td>
                            </tr>
                        </#list>
                    </#if>
                </table>
            </div>
        </div>
        <#if bidSection.updateScoreStatus?? && bidSection.updateScoreStatus == 1>
            <div class="examples">
                <h2>修正后报价得分</h2>
            </div>
            <div class="document">
                <ol>
                    <li class="small">序号</li>
                    <li class="long">投标单位</li>
                    <li>评标价（元）</li>
                    <li>评标基准价（元)</li>
                    <li>差值百分比（%)</li>
                    <li>评标价得分</li>
                </ol>
                <div class="cont-table">
                    <table cellpadding=0 cellspacing=0>
                        <#list bidders as bidder>
                            <tr>
                                <td class="small">${bidder_index + 1}</td>
                                <td class="long">${bidder.bidderName}</td>
                                <td>
                                    <#if bidder.quoteScoreResultAppendix.bidPrice?? && bidder.quoteScoreResultAppendix.bidPrice != ''>
                                        ${bidder.quoteScoreResultAppendix.bidPrice?number?string(",###.00")}
                                    </#if>
                                </td>
                                <#if bidder_index == 0>
                                    <td class="average" rowspan="${bidders?size}">
                                        <#if calcScoreParam.updateBasePrice?? && calcScoreParam.updateBasePrice != "">
                                            ${calcScoreParam.updateBasePrice?number?string(",###.00")}
                                        <#else>${calcScoreParam.basePrice?number?string(",###.00")}
                                        </#if>
                                    </td>
                                </#if>
                                <td>${bidder.quoteScoreResultAppendix.bidPriceOffset}</td>
                                <td>${bidder.quoteScoreResultAppendix.bidPriceScore}</td>
                            </tr>
                        </#list>
                        <#if bidSection.updateScoreReason?? && bidSection.updateScoreReason != "">
                            <tr>
                                <td class="small">修正原因</td>
                                <td colspan="5" style="text-align: left;">${bidSection.updateScoreReason}</td>
                            </tr>
                        </#if>
                    </table>
                </div>
            </div>
        </#if>
    </div>
</section>
<script>
    /**
     * 个人结束
     * */
    function personalReviewEnd() {
        layerConfirm("确认要结束个人评审吗？", function () {
            $.ajax({
                url: "${ctx}/expert/epcBidEval/epcPricePersonalEnd",
                type: "POST",
                cache: false,
                success: function (result) {
                    if (result.code == "1") {
                        layerSuccess("个人评审结束成功！",function () {
                            window.location.reload();
                        });
                    } else {
                        layerAlert(result.msg);
                    }
                },
                error: function () {
                    layerAlert("操作失败！");
                }
            });
        });
    }

    /**
     * 小组评审结束
     */
    function validDetailedGroupEnd() {
        $.ajax({
            url: "${ctx}/expert/epcBidEval/checkEpcPriceGroupEnd",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code === "2") {
                    layerAlert(data.msg, 2, 2);
                }else {
                    priceGroupEnd();
                }
            },
            error: function (data) {
                layerLoading("操作失败！", 2, 2);
            }
        })
    }

    /**
     * 小组结束
     **/
    function priceGroupEnd() {
        layerConfirm("确认要结束小组评审吗？结束后将不能再进行价格分修改！", function () {
            $.ajax({
                url: "${ctx}/expert/epcBidEval/epcPriceGroupEnd",
                type: "POST",
                cache: false,
                success: function (result) {
                    if (result) {
                        layerSuccess("小组评审结束成功！",function () {
                            window.location.reload();
                        });
                    } else {
                        layerAlert("小组评审结束失败！");
                    }
                },
                error: function () {
                    layerAlert("操作失败！");
                }
            });
        });
    }

    /**
     * 修改价格分
     **/
    function updatePriceScore() {
        layer.open({
            type: 2,
            offset: "c",
            title: ['价格分修改', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            content: "${ctx}/expert/epcBidEval/updatePriceScorePage",
            area: ['80%', '80%'],
            btn: ['确认', '取消'],
            btnAlign: 'c',
            shade: 0.3,
            btn1: function (index, layero) {
                // 点击确认的回调函数
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                var data = {
                    id: '${bidSection.id}',
                    updateScoreStatus: 1
                }
                iframeWin.updateBidSection(data);
                layer.close(index);
                window.location.reload();
            },
            btn2: function (index) {
                // 点击取消的回调函数
                // layer.msg("已取消");
                window.location.reload();
                layer.close(index);
            }
        });
    }
</script>
</body>
</html>