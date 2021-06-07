<link rel="stylesheet" href="${ctx}/css/candidatesDetailed.css">
<style>
    .right .right-document ol li {
        width: 16.66%;
    }

    .right .right-table table tr td {
        width: 16.66%;
    }
</style>
<h3>推荐候选人</h3>
<div class="right-document">
    <ol>
        <li class="long">投标单位</li>
        <li>第一名</li>
        <li>第二名</li>
        <li>第三名</li>
    </ol>
    <div class="right-table">
        <table cellpadding=0 cellpadding=0>
            <#list bidders as bidder>
                <tr>
                    <td class="long">${bidder.bidderName}</td>
                    <td>${bidder.voteNums[0]}</td>
                    <td>${bidder.voteNums[1]}</td>
                    <td>${bidder.voteNums[2]}</td>
                </tr>
            </#list>
        </table>
    </div>
</div>