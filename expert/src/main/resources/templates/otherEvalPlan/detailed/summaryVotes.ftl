<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/otherSummaryVotes.css">

</head>
<body>
<div class="choice">
    <ul class="select">
        <li class="sele" data-group="personResult">个人评审结果</li>
        <li data-group="groupResult">小组评审结果</li>
        <li data-group="candidateSuccess">中标候选人</li>
    </ul>
</div>
<div class="box personResult">
    <table cellpadding=0 cellspacing=0>
        <thead>
        <tr>
            <th>名次</th>
            <th>投标单位</th>
            <th>推荐理由</th>
        </tr>
        </thead>
        <tbody>
        <#list candidates as candidate>
            <tr>
                <td>第${candidate_index+1}名</td>
                <td>${candidate.bidderName}</td>
                <td>${candidate.reason}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
<div class="box groupResult" style="display: none">
    <table cellpadding=0 cellspacing=0>
        <thead>
        <tr>
            <th>投标单位</th>
            <th>第一名</th>
            <th>第二名</th>
            <th>第三名</th>
        </tr>
        </thead>
        <tbody>
        <#if bidders??>
            <#assign isGroupEnd = 1/>
            <#list bidders as bidder>
                <tr>
                    <td>${bidder.bidderName}</td>
                    <#list bidder.voteNums as voteNum>
                        <td>${voteNum}票</td>
                    </#list>
                </tr>
            </#list>
        <#else >
            <#assign isGroupEnd = 0/>
        </#if>
        </tbody>
    </table>
</div>
<div class="box candidateSuccess" style="display: none">
    <table cellpadding=0 cellspacing=0>
        <thead>
        <tr>
            <th>名次</th>
            <th>投标单位</th>
            <th>推荐理由</th>
        </tr>
        </thead>
        <tbody>
        <#list successBidder as sb>
            <tr>
                <td>第${sb_index+1}名</td>
                <td>${sb.bidderName}</td>
                <td>${sb.candidateSuccess.reason}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
<script>
    $(".select").on('click', 'li', function () {
        if (!$(this).is($(".select").eq(0)) && '${isGroupEnd}' == "0") {
            layerWarning("小组评审尚未结束");
            return;
        }
        $(this).addClass("sele").siblings().removeClass("sele");
        $(".box").hide();
        var group = $(this).attr("data-group");
        $(".box." + group).show();
    });
</script>

</html>


