<#list downBidderFileDTOs as downbidderfiledto>
    <#if downbidderfiledto>
        <div>
            <h3>${downbidderfiledto.bidderName}</h3>
            <ul>
                <#if downbidderfiledto.paperBidderPdf.url>
                    <li>
                        <img src="${ctx}/img/pdf.png" title="点击图标下载" alt=""
                             onclick="downloadFile('${downbidderfiledto.paperBidderPdf.url}','投标文件.pdf')">
                        <p>投标文件</p>
                        <div class="tender-btn" onclick="showPdf('${downbidderfiledto.paperBidderPdf.url}')">
                            <img src="${ctx}/img/see.png" alt="">预览
                        </div>
                    </li>
                </#if>
                <#if downbidderfiledto.businessFilePdf.url>
                    <li>
                        <img src="${ctx}/img/pdf.png" title="点击图标下载" alt=""
                             onclick="downloadFile('${downbidderfiledto.businessFilePdf.url}','商务标.pdf')">
                        <p>商务标</p>
                        <div class="tender-btn" onclick="showPdf('${downbidderfiledto.businessFilePdf.url}')">
                            <img src="${ctx}/img/see.png" alt="">预览
                        </div>
                    </li>
                </#if>
                <#if downbidderfiledto.technicalFilePdf.url>
                    <li>
                        <img src="${ctx}/img/pdf.png" title="点击图标下载" alt=""
                             onclick="downloadFile('${downbidderfiledto.technicalFilePdf.url}','技术标.pdf')">
                        <p>技术标</p>
                        <div class="tender-btn" onclick="showPdf('${downbidderfiledto.technicalFilePdf.url}')">
                            <img src="${ctx}/img/see.png" alt="">预览
                        </div>
                    </li>
                </#if>
                <#if downbidderfiledto.qualificationFilePdf.url>
                    <li>
                        <img src="${ctx}/img/pdf.png" title="点击图标下载" alt=""
                             onclick="downloadFile('${downbidderfiledto.qualificationFilePdf.url}','资格证明.pdf')">
                        <p>资格证明</p>
                        <div class="tender-btn" onclick="showPdf('${downbidderfiledto.qualificationFilePdf.url}')">
                            <img src="${ctx}/img/see.png" alt="">预览
                        </div>
                    </li>
                </#if>
                <#if bidSection.bidClassifyCode?? && bidSection.bidClassifyCode == "A08">
                    <#if downbidderfiledto.engineerQuantityListPdf.url>
                        <li>
                            <img src="${ctx}/img/pdf.png" title="点击图标下载" alt=""
                                 onclick="downloadFile('${downbidderfiledto.engineerQuantityListPdf.url}','工程量清单.pdf')">
                            <p>工程量清单</p>
                            <div class="tender-btn"
                                 onclick="showPdf('${downbidderfiledto.engineerQuantityListPdf.url}')">
                                <img src="${ctx}/img/see.png" alt="">预览
                            </div>
                        </li>
                    </#if>
                    <#if downbidderfiledto.engineerQuantityListXML.url>
                        <li>
                            <img src="${ctx}/img/xml.png" title="点击图标下载" alt=""
                                 onclick="downloadFile('${downbidderfiledto.engineerQuantityListXML.url}', '工程量清单.xml')">
                            <p>工程量清单</p>
                            <#--<div class="tender-btn"
                                 onclick="showPdf('${downbidderfiledto.engineerQuantityListXML.url}')">
                                <img src="${ctx}/img/see.png" alt="">预览
                            </div>-->
                        </li>
                    </#if>
                </#if>
                <#if bidSection.paperEval?? && bidSection.paperEval == "1">
                    <#if downbidderfiledto.gefFdfs.url>
                        <li>
                            <img src="${ctx}/img/yck.png" title="点击图标下载" alt=""
                                 onclick="downloadFile('${downbidderfiledto.gefFdfs.url}','投标文件.yck')">
                            <p>投标文件</p>
                            <span></span>
                        </li>
                    </#if>
                    <#if downbidderfiledto.sgefFdfs.url>
                        <li>
                            <img src="${ctx}/img/syck.png" title="点击图标下载" alt=""
                                 onclick="downloadFile('${downbidderfiledto.sgefFdfs.url}','投标文件.syck')">
                            <p>投标文件</p>
                            <span></span>
                        </li>
                    </#if>
                <#else>
                    <#if downbidderfiledto.gefFdfs.url>
                        <li>
                            <img src="${ctx}/img/gef.png" title="点击图标下载" alt=""
                                 onclick="downloadFile('${downbidderfiledto.gefFdfs.url}','投标文件.gef')">
                            <p>投标文件</p>
                            <span></span>
                        </li>
                    </#if>
                    <#if downbidderfiledto.sgefFdfs.url>
                        <li>
                            <img src="${ctx}/img/sgef.png" title="点击图标下载" alt=""
                                 onclick="downloadFile('${downbidderfiledto.sgefFdfs.url}','备用文件.sgef')">
                            <p>投标文件</p>
                            <span></span>
                        </li>
                    </#if>
                </#if>

                <#if downbidderfiledto.czrFdfs.url>
                    <li>
                        <img src="${ctx}/img/czr.png" title="点击图标下载" alt=""
                             onclick="downloadFile('${downbidderfiledto.czrFdfs.url}','存证文件.czr')">
                        <p>存证文件</p>
                        <span></span>
                    </li>
                </#if>
            </ul>
        </div>
    </#if>
</#list>