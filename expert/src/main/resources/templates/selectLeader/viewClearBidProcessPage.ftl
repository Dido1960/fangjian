<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>开始评标</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <#--<script type="text/javascript" src="${ctx}/js/circleChart.min.js"></script>-->
    <link rel="stylesheet" href="${ctx}/css/utils.css"><#--
    <link rel="stylesheet" href="${ctx}/css/normalize.css">
    <link rel="stylesheet" href="${ctx}/css/percircle.css">-->
    <link rel="stylesheet" href="${ctx}/css/clearBidProcess.css">
</head>
<body>
<div class="clear-box">
    <div class="clear-box_top">
        <div class="label">清标总进度</div>
        <div class="tiao">
            <div class="tiao-active"></div>
        </div>
        <div class="num"><span>0</span>%</div>
    </div>
    <div class="clear-box_center">
        <div class="left">
            已完成
            <span id="complete_num">0</span>
            家
        </div>
        <div class="right">
            未完成
            <span id="no_complete_num">0</span>
            家
        </div>
    </div>
    <div class="clear-box_bottom">
        <div class="table">
            <ul class="table-head">
                <li>
                    <div>投标人名称</div>
                    <#if tenderDoc.structureStatus?? && tenderDoc.structureStatus = 1>
                        <div>错漏项分析</div>
                    </#if>
                    <div>算术性错误</div>
                    <#if tenderDoc.priceStatus?? && tenderDoc.priceStatus = 1>
                        <div>零负报价检查</div>
                    </#if>
                    <#if tenderDoc.fundBasisStatus?? && tenderDoc.fundBasisStatus = 1>
                        <div>取费基础分析</div>
                    </#if>
                    <div>总进度</div>
                </li>
            </ul>
            <ul class="table-body" id="bidder-clear-bid-details">
            </ul>
        </div>
    </div>
</div>
</body>
<script>
    var init_data_num = 0;
    var processInit = true;

    $(function () {
        layui.use('element', function () {
            var element = layui.element;
            element.render();
        });
        setInterval(initClearBidProcessData, 1000);
    });

    /**
     * 初始化清标流程数据
     */
    function initClearBidProcessData() {
        if (init_data_num !== 0) {
            return;
        }
        init_data_num++;
        $.ajax({
            url: "${ctx}/expert/conBidEval/initClearBidProcessData",
            type: "POST",
            cache: false,
            success: function (data) {
                initTotalPercentage(data.totalPercentage);
                initCompleteNum(data.completeNum);
                initNotCompleteNum(data.notCompleteNum);
                initBidderClearBidDetails(data.bidderQuantities);
                init_data_num--;
            },
            error: function () {
                layerAlert("数据获取失败！");
            }
        });
    }

    /**
     * 初始化清标总进度
     * @param totalPercentage 总进度
     */
    function initTotalPercentage(totalPercentage) {
        $(".clear-box .num span").text(totalPercentage);
        $(".clear-box .tiao-active").css("width", totalPercentage + "%");
        $(".clear-box .tiao-active").addClass("tiao-transition");
    }

    /**
     * 初始化清标已完成家数量
     * @param completeNum 已完成数量
     */
    function initCompleteNum(completeNum) {
        $("#complete_num").html(completeNum);
    }

    /**
     * 初始化清标未完成家数量
     * @param notCompleteNum 未完成
     */
    function initNotCompleteNum(notCompleteNum) {
        $("#no_complete_num").html(notCompleteNum);
    }

    /**
     * 初始化所有投标人清标详情数据
     * @param bidderQuantities 所有投标人清标详情
     */
    function initBidderClearBidDetails(bidderQuantities) {
        if (processInit){
            processInit = false;
            var bidder_clear_bid_details = "";
            for (var i = 0; i < bidderQuantities.length; i++) {
                bidder_clear_bid_details += "<li>\n" +
                    "     <div>" + (i+1) + "." + bidderQuantities[i].bidderName + "</div>\n";
                if (!isNull('${tenderDoc.structureStatus}') && '${tenderDoc.structureStatus}' == '1') {
                    bidder_clear_bid_details += "<div>\n" +
                        "                        <div class='container' id='bidder_s_"+ i +"'>\n" +
                        "                            <div class='wave green'></div>\n" +
                        "                            <div class='wave-mask wave-mask-active' style='bottom: " + bidderQuantities[i].structureAnalysisProcess + "%'></div>\n" +
                        "                            <div class='container-text'>\n" +
                        "                                <span>" + bidderQuantities[i].structureAnalysisProcess + "</span>%\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>";
                }
                bidder_clear_bid_details += "<div>\n" +
                    "                        <div class='container' id='bidder_a_"+ i +"'>\n" +
                    "                            <div class='wave green'></div>\n" +
                    "                            <div class='wave-mask wave-mask-active' style='bottom: " + bidderQuantities[i].arithmeticAnalysisProcess + "%'></div>\n" +
                    "                            <div class='container-text'>\n" +
                    "                                <span>" + bidderQuantities[i].arithmeticAnalysisProcess + "</span>%\n" +
                    "                            </div>\n" +
                    "                        </div>\n" +
                    "                    </div>";
                if (!isNull('${tenderDoc.priceStatus}') && '${tenderDoc.priceStatus}' == '1') {
                    bidder_clear_bid_details += "<div>\n" +
                        "                        <div class='container' id='bidder_p_"+ i +"'>\n" +
                        "                            <div class='wave green'></div>\n" +
                        "                            <div class='wave-mask wave-mask-active' style='bottom: " + bidderQuantities[i].priceAnalysisProcess + "%'></div>\n" +
                        "                            <div class='container-text'>\n" +
                        "                                <span>" + bidderQuantities[i].priceAnalysisProcess + "</span>%\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>";
                }

                if (!isNull('${tenderDoc.fundBasisStatus}') && '${tenderDoc.fundBasisStatus}' == '1') {
                    bidder_clear_bid_details += "<div>\n" +
                        "                        <div class='container' id='bidder_r_"+ i +"'>\n" +
                        "                            <div class='wave green'></div>\n" +
                        "                            <div class='wave-mask wave-mask-active' style='bottom: " + bidderQuantities[i].ruleAnalysisProcess + "%'></div>\n" +
                        "                            <div class='container-text'>\n" +
                        "                                <span>" + bidderQuantities[i].ruleAnalysisProcess + "</span>%\n" +
                        "                            </div>\n" +
                        "                        </div>\n" +
                        "                    </div>";
                }
                bidder_clear_bid_details += "<div>\n" +
                    "                        <div class='container' id='bidder_t_"+ i +"'>\n" +
                    "                            <div class='wave blue'></div>\n" +
                    "                            <div class='wave-mask wave-mask-active' style='bottom: " + bidderQuantities[i].bidderTotalPercentage + "%'></div>\n" +
                    "                            <div class='container-text'>\n" +
                    "                                <span>" + bidderQuantities[i].bidderTotalPercentage + "</span>%\n" +
                    "                            </div>\n" +
                    "                        </div>\n" +
                    "                    </div>\n";
                bidder_clear_bid_details += "</li>"
            }
            $("#bidder-clear-bid-details").html(bidder_clear_bid_details);
        }else {
            for (var i = 0; i < bidderQuantities.length; i++) {
                if (!isNull('${tenderDoc.structureStatus}') && '${tenderDoc.structureStatus}' == '1') {
                    var $bidder_s = $("#bidder_s_" + i);
                    $bidder_s.find(".wave-mask-active").css("bottom", bidderQuantities[i].structureAnalysisProcess+"%");
                    $bidder_s.find(".container-text span").text(bidderQuantities[i].structureAnalysisProcess);
                }

                var $bidder_a = $("#bidder_a_" + i);
                $bidder_a.find(".wave-mask-active").css("bottom", bidderQuantities[i].arithmeticAnalysisProcess+"%");
                $bidder_a.find(".container-text span").text(bidderQuantities[i].arithmeticAnalysisProcess);

                if (!isNull('${tenderDoc.priceStatus}') && '${tenderDoc.priceStatus}' == '1') {
                    var $bidder_p = $("#bidder_p_" + i);
                    $bidder_p.find(".wave-mask-active").css("bottom", bidderQuantities[i].priceAnalysisProcess+"%");
                    $bidder_p.find(".container-text span").text(bidderQuantities[i].priceAnalysisProcess);
                }

                if (!isNull('${tenderDoc.fundBasisStatus}') && '${tenderDoc.fundBasisStatus}' == '1') {
                    var $bidder_r = $("#bidder_r_" + i);
                    $bidder_r.find(".wave-mask-active").css("bottom", bidderQuantities[i].ruleAnalysisProcess+"%");
                    $bidder_r.find(".container-text span").text(bidderQuantities[i].ruleAnalysisProcess);
                }

                var $bidder_t = $("#bidder_t_" + i);
                $bidder_t.find(".wave-mask-active").css("bottom", bidderQuantities[i].bidderTotalPercentage+"%");
                $bidder_t.find(".container-text span").text(bidderQuantities[i].bidderTotalPercentage);

            }
        }
    }
</script>
</html>