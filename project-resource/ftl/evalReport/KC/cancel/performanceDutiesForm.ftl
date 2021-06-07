<head>
    <title>评审工作履职情况记录表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <#--一行td默认显示的个数-->
        <#assign rowTdNum = 9>
        <div class="panel pagination">
            <table width="100%" class="base-normal-table" name="dutyRecord">
                <thead>
                <tr>
                    <td colspan="12"><div class="base-header-mid first-title">评审工作履职情况记录表</div></td>
                </tr>
                <tr>
                    <td colspan="12">
                        <span>标段名称：${bidSection.bidSectionName }</span>
                    </td>
                </tr>
                <tr>
                    <td colspan="12">
                        <span>标段编号：${bidSection.bidSectionCode }</span>
                    </td>
                </tr>
                <tr>
                    <td colspan="12">招标单位：${tenderProject.tendererName}</td>
                </tr>
                <tr class="base-border" style="text-align: center">
                    <td width="4%"></td>
                    <td width="30%">履职情况\专家姓名</td>
                    <#list expertUsers as expert>
                        <td width="6%">
                            ${expert.expertName}
                        </td>
                    </#list>
                    <#if expertUsers?size gt 9>
                        <#--大于9个专家情况-->
                        <#assign rowTdNum = 11 />
                        <#if (11 - expertUsers?size) gt 0 >
                            <#list 0 .. (11 - expertUsers?size - 1) as i>
                                <td width="6%"></td>
                            </#list>
                        </#if>
                     <#else >
                         <#if (9 - expertUsers?size) gt 0 >
                             <#list 0 .. (9 - expertUsers?size - 1) as i>
                                 <td width="6%"></td>
                             </#list>
                         </#if>
                    </#if>
                    <td width="12%">备注</td>
                </tr>
                </thead>
                <tbody>
                <tr class="base-border">
                    <td rowspan="15" style="text-align: center">会<br><br>议<br><br>内<br><br>容</td>
                    <td>是否熟悉采购法律法规</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                    <td rowspan="15"></td>
                </tr>
                <tr class="base-border">
                    <td>是否认真阅读招投标文件</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否熟悉商务条款</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否熟悉技术规范</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否具备一定的谈判技巧（工程建设、政府采购公开类不填）</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否无原则迎合他人意见</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否有倾向性诱导性发言</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否按照评分标准计分</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否有计分错误现象</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否未按规定要求澄清、说明</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否严格遵守评标纪律</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否按规定上交通讯工具</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否迟到或提早离场</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否主动提出合理化建议</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                <tr class="base-border">
                    <td>是否故意拖延评审时间</td>
                    <#list 1..rowTdNum as p>
                        <td></td>
                    </#list>
                </tr>
                </tbody>
                <tfoot>
                <tr class="base-tfoot">
                    <td colspan="12" class="two-form">
                        <div class="col-6">招标人代表：</div>
                        <div style="width: 45%;float: right" align="right"><span>${date[0]}年</span><span>${date[1]}月</span><span>${date[2]}日</span></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="12">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="12">
                        记录内容的填写应在各栏目中单选一项，记录为是的打“√”，记录否的打“×”，需要说明的情况请在备注栏具体说明。
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</#escape>