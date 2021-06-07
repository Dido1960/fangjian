<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
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
    <script src="${ctx}/js/calc.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/colorBase.css">
    <link rel="stylesheet" href="${ctx}/css/otherCandidateSelect.css">
    <link rel="stylesheet" href="${ctx}/css/pointSrc.css">

</head>
<body>
<div class="cont-left">
    <form class="layui-form" action="#">
        <div class="layui-form-item">
            <label class="layui-form-label">当前投标单位</label>
            <div class="layui-input-block">
                <select id="select-box" class="bidders-list" lay-filter="changeBidder">
                    <#list bidders as bidder>
                        <option value="${bidder.id }" data-index="${bidder_index}"
                                data-docid="${bidder.bidDocId}">${bidder_index +1} 、${bidder.bidderName}
                            <#if bidder.expertReviewStatusForBidder>(已评审)</#if></option>
                    </#list>
                </select>
            </div>
        </div>
    </form>
    <div class="score">
        <div class="top">
            <#if !isPersonalSelectionEnd>
                <span class="green-b" onclick="recommendCandidate()">推荐候选人</span>
                <span class="yellow-b" id="personEnd" onclick="personalReviewEnd()" style="width: 116px">个人评审结束</span>
            <#else>
                <#if !currentGrade.groupEnd?? || (currentGrade.groupEnd?? && currentGrade.groupEnd != 1)>
                    <#if expert.isChairman == '1'>
                        <span class="yellow-b" onclick="endGroupReview()" style="width: 116px">小组评审结束</span>
                    </#if>
                </#if>
                <div class="blove-b" onclick="showExpertEval()">评审结果</div>
            </#if>
        </div>
        <div id="grade-content" class="bottom">
            <#list grades as grade>
                <ol>
                    <li class="long">${grade.name}</li>
                    <li>页码</li>
                </ol>
                <table cellpadding=0 cellspacing=0>
                    <#list grade.gradeItems as gradeItem>
                        <tr>
                            <td class="long">
                                ${gradeItem_index+1}、${gradeItem.itemContent}
                            </td>
                            <td id="point_${gradeItem.id}"></td>
                        </tr>
                    </#list>
                </table>
            </#list>
        </div>

        <form id="review-form" action="${ctx}/expert/otherBidEval/personalReviewEnd" method="post"
              style="display: none">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="hidden" name="gradeType" value="${currentGrade.gradeType}"/>
            <input type="hidden" name="bidSectionId" value="${bidSection.id}"/>
            <input type="hidden" name="evalProcess" value="${evalProcess}"/>
        </form>
    </div>
</div>

<div class="cont-right">
    <ol class="tit pdf-file-list" id="file-type-li">
        <li data-filetype="1" fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/TempConvert/temp.pdf">
            招标文件
        </li>
        <li data-pdf-index="3" class="choice" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">
            商务标
        </li>
        <li data-pdf-index="2" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">
            技术标
        </li>
        <li data-pdf-index="1" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">
            资格证明
        </li>
    </ol>
    <div id="view-pdf-div" class="cont-text">
        <#--是否帮助按钮-->
        <#assign showHelpBtn="true"/>
        <#--是否启用本地缓存机制-->
        <#assign localCache="true"/>
        <#--是否开启另存为按钮-->
        <#assign showSaveAs="false"/>
        <#--是否开启全屏按钮-->
        <#assign fullScreen="false"/>
        <#--1.必须存在class 为(pdf-file-list文件)-->
        <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
        <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
        <#include "${ctx}/common/showPDFView.ftl"/>
    </div>
</div>

<script>
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
            form.on('select(changeBidder)', function (data) {
                getBidderData(data.value);
                changBidderView();
            });
        });
        getBidderData('${bidders[0].id}');
    });

    /**
     * 获取投标人数据
     * @param bidderId
     */
    function getBidderData(bidderId) {
        $.ajax({
            url: "${ctx}/expert/otherBidEval/getBidderData",
            data: {
                "gradeId": '${currentGrade.id}',
                "bidderId": bidderId
            },
            type: "POST",
            cache: false,
            success: function (data) {
                //回显评审点
                echoPoint(data.pointList);
            },
            error: function () {
                layerAlert("数据获取失败！");
            }
        });
    }

    /**
     *回显评审点
     */
    function echoPoint(data) {
        if (isNull(data)) {
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var point = data[i];
            var $pointDiv = $('#point_' + point.gradeItemId);
            $pointDiv.empty();
            var htmlstr = "";
            if (!isNull(point.quReviewPointPages)) {
                for (var j = 0; j < point.quReviewPointPages.length; j++) {
                    var page = point.quReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 1)'>" + page + "页<img class='qualifications_img'></span>"
                }
            }

            if (!isNull(point.teReviewPointPages)) {
                for (var j = 0; j < point.teReviewPointPages.length; j++) {
                    var page = point.teReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 2)'>" + page + "页<img class='technology_img'></span>"
                }
            }

            if (!isNull(point.bsReviewPointPages)) {
                for (var j = 0; j < point.bsReviewPointPages.length; j++) {
                    var page = point.bsReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 3)'>" + page + "页<img class='business_img'></span>"
                }
            }

            $pointDiv.append(htmlstr);
        }
    }

    /**
     * 个人评审结束
     */
    function personalReviewEnd() {
        layerConfirm("确定结束个人评审吗？结束后将无法修改评审结果！", function () {
            $.ajax({
                url: "${ctx}/expert/otherBidEval/personalReviewEnd",
                data: {
                    "bidSectionId": '${bidSection.id}'
                },
                type: "POST",
                cache: false,
                success: function (result) {
                    if (result.code == "1") {
                        window.location.reload();
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
     * 勘察、设计详细评审，展示评审结果
     */
    function showExpertEval() {
        hide_IWeb2018();
        window.top.layer.open({
            type: 2,
            offset: 'c',
            title: '评审结果',
            shadeClose: true,
            area: ['70%', '70%'],
            content: "${ctx}/expert/otherBidEval/summaryVotesPage",
            end: function (index) {
                layer.close(index);
                show_IWeb2018();
            }
        });

    }

    /**
     * 推荐候选人
     */
    function recommendCandidate() {
        hide_IWeb2018();
        window.top.layer.open({
            type: 2,
            offset: 'c',
            title: '推荐候选人',
            shadeClose: true,
            area: ['1200px', '680px'],
            content: "${ctx}/expert/otherBidEval/recommendCandidatePage",
            end: function (index) {
                layer.close(index);
                show_IWeb2018();
            }
        });
    }

    /**
     * 小组评审结束
     */
    function endGroupReview() {
        $.ajax({
            url: "${ctx}/expert/otherBidEval/validGroupReview",
            type: "POST",
            data: {
                "evalProcess": ${evalProcess},
                "bidSectionId": ${bidSection.id}
            },
            cache: false,
            success: function (data) {
                if (data.isFreeBackApplying){
                    layerAlert("评审回退尚未审核，操作失败!");
                    return false;
                }
                if (!data.allEvalStatus) {
                    layerAlert(data.noEndExpertNames);
                    return;
                }
                if (!data.isHasResult) {
                    layerConfirm("评标委员会无法确认候选人，由专家组长进行候选人确认和完善候选人推荐信息!", function () {
                        setTimeout(function () {
                            hide_IWeb2018();
                            window.top.layer.open({
                                type: 2,
                                offset: 'c',
                                title: '推荐候选人',
                                shadeClose: true,
                                area: ['1200px', '680px'],
                                content: "${ctx}/expert/otherBidEval/leaderCandidatePage",
                                end: function (index) {
                                    layer.close(index);
                                    show_IWeb2018();
                                }
                            });
                        }, 400);

                    });
                    return;
                }
                // 结束小组评审
                submitResult();
            },
            error: function (data) {
                layerAlert("操作失败!");
            }
        })
    }

    function submitResult() {
        layerConfirm("确定结束小组评审吗?", function (index) {
            layerLoading('数据提交中, 请稍候...', null, 0);
            $.ajax({
                url: "${ctx}/expert/otherBidEval/endGroupReview",
                type: "POST",
                data: {
                    "evalProcess": ${evalProcess},
                    "bidSectionId": ${bidSection.id}
                },
                success: function (data) {
                    setTimeout(function () {
                        loadComplete();
                        window.location.href = window.location.href;
                    }, 400);
                }
            });
        });
    }
</script>
</body>
</html>