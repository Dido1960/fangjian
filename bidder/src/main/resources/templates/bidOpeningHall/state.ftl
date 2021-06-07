<ul>
    <li>序号</li>
    <li>投标单位</li>
    <li>报价（元）</li>
    <li>解密情况</li>
    <li>解密时间</li>
</ul>
<div class="state-table">
    <table cellpadding=0 cellspacing=0>
        <#list bidders as bidder>
            <tr>
                <td>${bidder?counter}</td>
                <td>${bidder.bidderName}</td>
                <td>
                    <#if bidder.bidderOpenInfo.bidPrice?? && bidder.bidderOpenInfo.bidPrice != ''>
                        <#if bidder.bidderOpenInfo.bidPrice?contains('%')>
                            ${bidder.bidderOpenInfo.bidPrice}
                        <#else >
                            ${((bidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                        </#if>
                    <#else >
                        /
                    </#if>
                </td>
                <td>
                    <#if (bidder.bidderOpenInfo)??>
                        <#if bidder.bidderOpenInfo.decryptStatus == 0>
                            未解密
                        <#elseif   bidder.bidderOpenInfo.decryptStatus == 1>
                            解密成功
                        <#elseif   bidder.bidderOpenInfo.decryptStatus == 2>
                            解密失败
                        </#if>
                    <#else >
                        /
                    </#if>
                </td>
                <td>
                    <#if bidder.decryptStatus == 2>
                        ${bidder.decryptTimeMinute}分${bidder.decryptTimeSecond}秒
                    <#else >
                        /
                    </#if>
                </td>
            </tr>
        </#list>

    </table>
</div>