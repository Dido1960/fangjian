<div class="decrypt-left">
    <ul><#if bidders?size gt 0>
            <#list bidders as bidder>
                <li>
                    <p>${bidder.bidderName}</p>
                    <span>
                    <#if bidder.bidderOpenInfo.decryptStatus == 0>
                        未解密
                    <#elseif bidder.bidderOpenInfo.decryptStatus == 1>
                        解密成功
                    <#elseif bidder.bidderOpenInfo.decryptStatus == 2>
                        解密失败
                    </#if>
                </span>
                </li>
            </#list>
        <#else >
            <li>
                <p>解密未开始</p>
            </li>
        </#if>
    </ul>
</div>
<div class="decrypt-right">
    <div class="decrypt-word">
        <img src="../img/decrypt-right.png" alt="">
        <span class="percentage">${progress?int}%</span>
        <div class="all">解密总进度</div>
        <div class="decrypt-complete">
            <p>${decNumb}</p>
            <span>已解密 <i>${decrypted}</i></span>
        </div>
        <div class="decrypt-wait">
            <p>${wait}</p>
            <span>等待人数</span>
        </div>
        <div class="decrypt-conduct">
            <p>${unDecryptCount}</p>
            <span>未解密</span>
        </div>
    </div>
</div>