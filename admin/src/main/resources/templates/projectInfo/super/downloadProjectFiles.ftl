<div>
    <ul>
        <li>
            <img src="${ctx}/img/pdf.png" title="点击图标下载" alt="" onclick="downloadFile('${tenderPdf.url}','招标文件.pdf')">
            <p>招标文件</p>
            <div class="project-btn" onclick="showPdf('${tenderPdf.url}')">
                <img src="${ctx}/img/see.png">预览
            </div>
        </li>
        <#if !(bidSection.paperEval?? && bidSection.paperEval == "1")>
            <#if tenderGef.url>
                <li>
                    <img src="${ctx}/img/gef.png" title="点击图标下载" alt="" onclick="downloadFile('${tenderGef.url}','招标文件.gef')">
                    <p>招标文件</p>
                </li>
            </#if>
        </#if>
        <#if bidSection.bidClassifyCode?? && bidSection.bidClassifyCode == "A08">
            <#if quantityPdf.url>
                <li>
                    <img src="${ctx}/img/pdf.png" title="点击图标下载" alt="" onclick="downloadFile('${quantityPdf.url}','招标工程量清单.pdf')">
                    <p>工程量清单</p>
                    <div class="project-btn" onclick="showPdf('${quantityPdf.url}')">
                        <img src="${ctx}/img/see.png">预览
                    </div>
                </li>
            </#if>
            <#if quantityXml.url>
                <li>
                    <img src="${ctx}/img/xml.png" title="点击图标下载" alt="" onclick="downloadFile('${quantityXml.url}','招标工程量清单.xml')">
                    <p>工程量清单</p>
                    <#--<div class="project-btn" onclick="showPdf('${quantityXml.url}')">
                        <img src="${ctx}/img/see.png">预览
                    </div>-->
                </li>
            </#if>
        </#if>
        <#if clarifyAnswer.fdfs.url>
            <li>
                <img src="${ctx}/img/pdf.png" title="点击图标下载" alt="" onclick="downloadFile('${clarifyAnswer.fdfs.url}','澄清文件.pdf')">
                <p>澄清文件</p>
                <div class="project-btn" onclick="showPdf('${clarifyAnswer.fdfs.url}')">
                    <img src="${ctx}/img/see.png" alt="">预览
                </div>
            </li>
        </#if>

        <#if openBidTablePdf.url>
            <li>
                <img src="${ctx}/img/pdf.png" title="点击图标下载" alt="" onclick="downloadFile('${openBidTablePdf.url}','开标记录表.pdf')">
                <p>开标记录表</p>
                <div class="project-btn" onclick="showPdf('${openBidTablePdf.url}')">
                    <img src="${ctx}/img/see.png" alt="" onclick="showPdf('${openBidTablePdf.url}')">预览
                </div>
            </li>
        </#if>

        <#if bidEvaluationPdf.url>
            <li>
                <img src="${ctx}/img/pdf.png" title="点击图标下载" alt="" onclick="downloadFile('${bidEvaluationPdf.url}','评标报告.pdf')">
                <p>评标报告</p>
                <div class="project-btn"  onclick="showPdf('${bidEvaluationPdf.url}')">
                    <img src="${ctx}/img/see.png" alt="">预览
                </div>
            </li>
        </#if>
    </ul>
</div>
<script>
    var layer;
    layui.use(['element', 'layer'], function () {
        var element = layui.element;
        layer = layui.layer;
    });

</script>