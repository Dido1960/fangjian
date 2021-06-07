<#if bidSection.resumeStatus == 0>
<#--未开始复会-->
    <div class="center-head">
        <span id="text-msg">代理未开始复会，请耐心等待...</span>
    </div>
<#elseif bidSection.resumeStatus == 1>
<#--复会状态：1（进行中）-->
    <div class="center-head">
        <#if bidder.bidderOpenInfo.resumeDetermine == 2>
            <span id="text-msg">请投标人通过直播查看评标结果</span>
            <div class="btns">
                <span class="blove-b" id="noBtn" onclick="unDissent()">无异议</span>
            </div>
        <#elseif bidder.bidderOpenInfo.resumeDetermine == 1>
            <span id="text-msg">请投标人通过直播查看评标结果</span>
            <div class="btns">
            </div>
        <#else >
            <span id="text-msg">请投标人通过直播查看评标结果</span>
            <div class="btns">
                <span class="oragen-b" id="haveBtn" onclick="dissentWindows()">有异议</span>
                <span class="blove-b" id="noBtn" onclick="unDissent()">无异议</span>
            </div>
        </#if>
    </div>
<#else>
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
</script>