<#if bidSection.resumeStatus == 0>
<#--未开始复会-->
    <div class="center-head">
        <span id="text-msg">代理未开始复会，请耐心等待...</span>
    </div>
<#elseif bidSection.resumeStatus == 1 && relate.resumptionReportId??>
<#--复会状态：1（进行中）-->
    <div class="center-head">
        <#if bidder.bidderOpenInfo.resumeDetermine == 2>
            <span id="text-msg">对本次结果有无异议，请进行确认</span>

            <div class="btns">
                <span class="blove-b" id="noBtn" onclick="unDissent()">无异议</span>
                <span class="blove-b" id="meetingReport" onclick="showResumeReport()">复会报告</span>
            </div>
        <#elseif bidder.bidderOpenInfo.resumeDetermine == 1>
            <span id="text-msg">点击查看复会报告</span>
            <div class="btns">
                <span class="blove-b" id="meetingReport" onclick="showResumeReport()">复会报告</span>
            </div>
        <#else >
            <span id="text-msg">对本次结果有无异议，请进行确认</span>
            <div class="btns">
                <span class="oragen-b" id="haveBtn" onclick="dissentWindows()">有异议</span>
                <span class="blove-b" id="noBtn" onclick="unDissent()">无异议</span>
                <span class="blove-b" id="meetingReport" onclick="showResumeReport()">复会报告</span>
            </div>
        </#if>
    </div>

<#--显示复会报告-->
<#--    <div class="pdf-file-list" style="display: none">-->
<#--        <div class="choice" fdfsMark="${resumeReport.mark}"></div>-->
<#--    </div>-->
<#--    <div class="document">-->
<#--        &lt;#&ndash;是否帮助按钮&ndash;&gt;-->
<#--        <#assign showHelpBtn="false"/>-->
<#--        &lt;#&ndash;是否启用本地缓存机制&ndash;&gt;-->
<#--        <#assign localCache="true"/>-->
<#--        &lt;#&ndash;是否开启另存为按钮&ndash;&gt;-->
<#--        <#assign showSaveAs="false"/>-->
<#--        &lt;#&ndash;是否开启另存为按钮&ndash;&gt;-->
<#--        <#assign fullScreen="false"/>-->
<#--        &lt;#&ndash;1.必须存在class 为(pdf-file-list文件)&ndash;&gt;-->
<#--        &lt;#&ndash;2.如果存在投标人切换 请 调用 changBidderView()更新pdf&ndash;&gt;-->
<#--        &lt;#&ndash;3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页&ndash;&gt;-->
<#--        <#include "/common/showPDFView.ftl"/>-->
<#--    </div>-->
<#elseif !relate.resumptionReportId>
<#--复会时间到了，未结束评标-->
    <div class="center-head">
        <span id="text-msg">评标未结束，复会报告未生成</span>
        <div class="btns"></div>
    </div>
<#else>
<#--复会时间到了，未结束评标-->
    <div class="center-head">
        <span id="text-msg">当前复会已结束!</span>
        <div class="btns"></div>
    </div>
</#if>

<script>
    var meetingStatus = '${bidSection.resumeStatus}';
    var bidderStatus = '${bidder.bidderOpenInfo.resumeDetermine}';

    var timer = setInterval(function () {
        searchMeetingStatus();
    }, 1000);

    /**
     * 查询复会状态
     */
    function searchMeetingStatus() {
        $.ajax({
            url: '${ctx}/bidder/bidSection/resumptionStatus',
            type: 'post',
            cache: false,
            success: function (result) {
                // 获取标段信息
                if (result.data.resumeStatus != meetingStatus) {
                    meetingStatus = result.data.resumeStatus;
                    if (result.data.resumeStatus == 1) {
                        layerAlert("复会已开始！", function () {
                            window.location.reload();
                        }, null, 3);
                    } else if (result.data.resumeStatus == 2) {
                        // 关闭计时器
                        clearInterval(timer);
                        layerAlert("复会已结束！", function () {
                            window.location.reload();
                        }, null, 1);
                    }
                }
            },
            error: function (data) {
                console.error(data);
            },
        });
    }

    /**
     * 无异议
     */
    function unDissent() {
        layerConfirm("确认无异议？", function () {
            updateDissentStatus(1);
        });
    }


    /**
     * 有异议
     */
    function dissentWindows() {
        layerConfirm("如果有异议，请通过消息盒子提出异议！", function () {
            updateDissentStatus(2);
        })
    }

    function updateDissentStatus(status) {
        doLoading();
        $.ajax({
            url: "${ctx}/bidder/bidSection/updateDissentStatus",
            type: "POST",
            cache: false,
            data: {
                "bidSectionId": ${bidSection.id},
                "bidderId": ${bidder.id},
                "status": status
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    if (status == 1) {
                        if (bidderStatus == 2) {
                            postMessageFun({forbiddenData: {memberId: '${bidderIdPre}_${bidder.id}', type: 0}});
                        }
                        $("#haveBtn").remove();
                        $("#noBtn").remove();
                        $("#text-msg").text("点击查看复会报告");
                    } else if (status == 2) {
                        postMessageFun({forbiddenData: {memberId: '${bidderIdPre}_${bidder.id}', type: 1}});
                        $("#haveBtn").remove();
                    }
                    bidderStatus = status;
                }
            },
            error: function (e) {
                layer.close(layerLoadIndex);
            }
        });
    }

    /**
     * 查看复会报告
     */
    function showResumeReport() {
        var markq = '${resumeReport.mark}';
        if (isNull(markq)) {
            layer.msg("文件不存在");
            return;
        }
        window.top.layer.open({
            type: 2,
            title: '复会报告查看',
            shadeClose: true,
            scrollbar: false,
            area: ['1200px', '790px'],
            btn: ['取消'],
            offset: 'auto',
            content: "/bidFile/showPdfIframe?mark=" + encodeURIComponent(markq),
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
    }
</script>