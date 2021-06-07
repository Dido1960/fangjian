<link rel="stylesheet" href="${ctx}/css/bidEvalBase.css">
<div class="cont">
    <div class="left">
        <ul>
            <#list processList as process>
                <#if processStatus[process.processName] == 2 >
                    <li id="${process.processName}" class="choice complete"
                        onclick="loadEvalProcess('${ctx}/gov/bidEval/${process.processName}Page', this)"
                        processStatus="${processStatus[process.processName]}">
                        <a href="javascript:void(0)">
                            <img src="${ctx}/img/icon_${process.processName}_sele.png" alt="">${process.remake}
                            <i></i>
                        </a>
                    </li>
                <#elseif processStatus[process.processName] == 1>
                    <li id="${process.processName}" class="choice current"
                        onclick="loadEvalProcess('${ctx}/gov/bidEval/${process.processName}Page', this)"
                        processStatus="${processStatus[process.processName]}">
                        <a href="javascript:void(0)">
                            <img src="${ctx}/img/icon_${process.processName}_sele.png" alt="">${process.remake}
                            <i></i>
                        </a>
                    </li>
                <#else >
                    <li id="${process.processName}"
                        onclick="loadEvalProcess('${ctx}/gov/bidEval/${process.processName}Page', this)"
                        processStatus="${processStatus[process.processName]}">
                        <a href="javascript:void(0)">
                            <img src="${ctx}/img/icon_${process.processName}.png" alt="">${process.remake}
                            <i></i>
                        </a>
                    </li>
                </#if>
            </#list>
        </ul>
    </div>
    <div class="right">

    </div>
</div>
<script>
    $(function () {
        var objId = localStorage.getItem("evalProcess_flow_${bidSection.id}");
        setTimeout(function () {
            if (isNull(objId)) {
                $(".left ul li").eq(0).click();
            } else {
                $("#" + objId).click();
            }
        }, 400);
    })

    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param obj
     */
    function loadEvalProcess(url, obj) {
        var processStatus = $(obj).attr("processStatus")
        if (processStatus == 0) {
            layerWarning("该环节还未开始！");
            return false;
        }
        if (processStatus == 1) {
            url = "${ctx}/gov/bidEval/linkInProgressPage"
        }
        //设置默认加载项
        localStorage.setItem("evalProcess_flow_${bidSection.id}", $(obj).attr('id'));
        // objAddClass(obj);
        var $content_box = $(".right");
        $content_box.load(url, function () {
            // 添加跨域参数
            $.ajaxSetup({
                type: "POST",
                cache: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrf_header, csrf_token);
                }
            });
        });
    }

    function objAddClass(obj) {
        $(obj).siblings().removeClass("choice");
        $(obj).addClass("choice");
    }
</script>