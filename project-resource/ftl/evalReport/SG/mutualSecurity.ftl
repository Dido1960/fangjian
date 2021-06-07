<head>
    <title>互保共建加分表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<div class="show">
    <div class="panel">
        <table width="100%" class="base-normal-table">
            <thead>
            <tr>
                <td colspan="5">评标报告附件八</td>
            </tr>
            <tr>
                <td colspan="5">
                    <div class="base-header-mid first-title">互保共建加分表</div>
                </td>
            </tr>
            <tr>
                <td colspan="5">标段名称：${bidSection.bidSectionName}</td>
            </tr>
            <tr>
                <td colspan="5">标段编号：${bidSection.bidSectionCode}</td>
            </tr>
            <tr>
                <td colspan="5">
                    <div style="width: 60%;float: left">招标单位：${tenderProject.tendererName}</div>
                    <div style="width: 39%;float: right">日期：<span>${date[0]}年</span><span>${date[1]}月</span><span>${date[2]}日</span></div>
                </td>
            </tr>
            <tr class="base-border" style="text-align: center">
                <td width="15%">评审内容</td>
                <td width="85%" style="text-align: left" colspan="4">
                    （一）互保共建（投标人在投标文件所附《互保共建互为市场承诺书》（复印件）中承诺凡采购注册在本市行政区域范围内企业原材料占到所竞标工程总材料的30%（含30%）以上，50%以下的加1分，占到50%（含50%）以上加1.5分）
                </td>
            </tr>
            <tr class="base-border" style="text-align: center">
                <td width="15%">投标人名称 </td>
                <td width="25%">有</td>
                <td width="25%">无</td>
                <td width="25%">加分</td>
                <td width="10%">分数合计</td>
            </tr>
            </thead>
            <tbody>
            <#if evalResultSgs>
                <#list evalResultSgs as evalResultSg>
                    <tr class="base-border" style="text-align: center">
                        <td style="text-align: left">${evalResultSg.bidderName}</td>
                        <#if evalResultSg.mutualSecurity == 0>
                            <td></td>
                            <td>√</td>
                            <td></td>
                        <#else>
                            <td>√</td>
                            <td></td>
                            <td>${evalResultSg.mutualSecurity}</td>
                        </#if>
                        <td>${evalResultSg.mutualSecurity}</td>
                    </tr>
                </#list>
            <#else>
                <#list 0..5 as i>
                    <tr class="base-border" style="text-align: center">
                        <td style="text-align: left"></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </#list>
            </#if>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="5">说明：在“评审结果”栏“有”、“无”选项下面画“√”表示选定该项，评定为“有”时加设定分值，评定为“无”时不扣分。</td>
                </tr>
                <tr class="base-tfoot">
                    <td colspan="5">评标委员会成员签字：</td>
                </tr>
            </tfoot>
        </table>
    </div>
</div>