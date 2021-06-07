<ul>
    <li>序号</li>
    <li class="long">投标单位</li>
    <li>投标人身份检查</li>
    <li>缴纳状态</li>
    <li class="long">检查结果</li>
</ul>
<div class="identity-table">
    <table cellpadding=0 cellspacing=0>
        <#list bidders as bidder>
            <tr>
                <td>${bidder?counter}</td>
                <td class="long">${bidder.bidderName}</td>
                <td>
                    <#if bidder.bidderOpenInfo.bidderIdentityStatus?? >
                        ${(bidder.bidderOpenInfo.bidderIdentityStatus == '0')?string('不符合','符合')}
                    <#else >
                        未检查
                    </#if>
                </td>
                <td>
                    <#if (bidder.bidderOpenInfo)?? >
                        <#if bidder.bidderOpenInfo.marginPayStatus == 0>
                            未缴纳
                        <#elseif bidder.bidderOpenInfo.marginPayStatus == 1>
                            已缴纳
                        <#elseif bidder.bidderOpenInfo.marginPayStatus == 2>
                            保函
                        </#if>
                    <#else >
                        未检查
                    </#if>
                </td>

                <#if bidder.bidderOpenInfo.notCheckin == '' || bidder.bidderOpenInfo.notCheckin != 3><!-- 未上传投标文件 -->
                    <td class="long" title="未上传投标文件">未通过</td>
                <#elseif bidder.bidderOpenInfo.bidderIdentityStatus == '0'>
                    <td class="long" title="身份检查不符合">未通过</td>
                <#elseif bidder.bidderOpenInfo.notCheckin == '2'><!-- 弃标 -->
                    <td class="long" title="弃标">未通过</td>
                <#elseif bidder.bidderOpenInfo.notCheckin == '9'><!-- 其他原因 -->
                    <td class="long" title="${bidder.bidderOpenInfo.notCheckinReason}">未通过</td>
                <#elseif bidder.bidderOpenInfo.tenderRejection == '1'> <!-- 标书被拒绝 -->
                    <td class="long" title="${bidder.bidderOpenInfo.tenderRejectionReason}">未通过</td>
                <#else>
                    <#--非资格预审项目检查，投标保证金缴纳状态-->
                    <#if bidSection.bidClassifyCode != 'A10'>
                        <#if !bidder.bidderOpenInfo.marginPayStatus || bidder.bidderOpenInfo.marginPayStatus == 0>
                            <td class="long" title="投标保证金未缴纳">未通过</td>
                        <#else >
                            <td class="long">通过</td>
                        </#if>
                    <#else >
                        <td class="long">通过</td>
                    </#if>
                </#if>
            </tr>
        </#list>
    </table>
</div>