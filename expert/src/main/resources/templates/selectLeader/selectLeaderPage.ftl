<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <script src="${ctx}/js/convertMoney.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/selection.css">
</head>
<body>
<header>
    <div class="text">
        <div class="name">
            <img src="${ctx}/img/logo_blue.png" alt="">
            甘肃省房建市政电子辅助评标系统
        </div>
        <div class="bao">
            <div class="try" onclick="exitSystem()">
                <b class="username" title="${user.name!}">${user.name!}</b>
                <i ></i>
            </div>
        </div>
    </div>
</header>
<section>
    <div class="head">评委组长推选</div>
    <h4 style="display: none;text-align: center;color: red;padding: 10px;" class="lastVoted">
        <span class="last-vote-info"></span>
    </h4>
    <ul class="cont" id="expertsList">
    </ul>
</section>


<#--隐藏域-->
<input type="hidden" id="bidSectionId" value="${bidSectionId}">
<input type="hidden" class="voteOver" value="${voteOver}">
<input type="hidden" class="isVote" value="${isVote}">
<input type="hidden" class="isFirstVote" value="${isFirstVote}">
<input type="hidden" class="voteRound" value="${voteRound}">
<#--弹窗提示信息-->
<div style="padding: 20px 60px;display: none" class="round-over-tip">本轮投票结束，点击进入下一轮投票</div>
<div style="padding: 20px 80px;display: none" class="vote-over-tip">
    投票结束，评标组长为：<span style="color: red" class="leaderExpert"></span><br>
    点击进入等待评标页面
</div>

<script id="expertTemplate" type="text/html">
    {{#  layui.each(d, function(index, expert){ }}
    <li class="expert-item">
        <div class="top">
            <img src="${ctx}/img/face.png" alt="">
            <div class="right">
                <p title="{{expert.expertName}}">评委姓名:&nbsp;{{expert.expertName}}</p>
                <p title="{{expert.company}}">工作单位:&nbsp;{{expert.company||''}}</p>
                <p title="{{expert.categoryName}}">评委身份:&nbsp;{{expert.categoryName}}</p>
            </div>
        </div>
        <div class="bottom">
            <#-- 投票没结束展示按钮 -->
            {{# if('${voteOver}'){ }}
            <#-- 本轮投过票 -->
            {{# if('${isVote}'){ }}
            <span class="gray-b">推选</span>
            {{# }else{ }}
            <#-- 没有失去候选资格 -->
            {{# if(expert.leaderStatus != "0"){ }}
            <input type="hidden" value="{{expert.id}}">
            <span class="blove-b elect-btn" onclick="elect(this)">推选</span>
            {{# }else{ }}
            <span class="gray-b">推选</span>
            {{# } }}
            {{# } }}
            {{# }else{ }}
            <span class="gray-b">推选</span>
            {{# } }}
        </div>
        <#-- 本轮投过票 才显示票数 -->
        {{# if('${isVote}'){ }}
        <div class="tips vote-count expert_{{expert.id}}">{{expert.count}}票</div>
        {{# }else{ }}
        <div class="tips vote-count expert_{{expert.id}}" style="display: none">{{expert.count}}票</div>
        {{# } }}
    </li>

    {{#  }); }}
</script>

<script>
    var heartBeatInterval;
    var expertInterval;
    var laytpl;
    var layer;
    var bidSectionId = $('#bidSectionId').val();
    var isAllExpertEnter = false;
    //文件规定的专家个数
    var expertCount = 0;
    //目前没有回避的专家个数
    var totalCount = 0;

    var heartBeatVoteCount=0;

    layui.use(['form', 'layer'], function () {
        layer = layui.layer;
        laytpl = layui.laytpl;
        showAllExpert();
        initPages();
    });

    /**
     * 页面初始化
     */
    function initPages() {
        var voteOver = $('.voteOver').val();
        var isFirstVote = $('.isFirstVote').val();
        //投票是否结束（是否已选出组长）
        if (!voteOver) {
            $(".enter-bid-btn").show();
        }
        //是否是第一轮投票，或者第一轮是否已选出组长
        if (isFirstVote) {
            showLastVoteInfo();
        }
        expertInterval = window.setInterval(showAllExpert, 3000);
    }

    /**
     * 检查投票信息
     */
    function heartBeatVote() {
        if(heartBeatVoteCount!=0){
            return ;
        }
        heartBeatVoteCount++;
        var voteRound = $('.voteRound').val();
        $.ajax({
            url: '${ctx}/selectLeader/heartBeatVote',
            type: 'POST',
            cache: false,
            data: {
                round: voteRound,
                bidSectionId: bidSectionId
            },
            success: function (data) {
                //更新本轮投票票数
                var experts = data.listExperts;
                if (!isNull(experts)) {
                    for (var i = 0; i < experts.length; i++) {
                        $(".expert_" + experts[i].id).text(experts[i].count + "票");
                    }
                }
                //投票结束
                if (data.voteOver) {
                    //评标组长信息
                    var leaderExpertUser = data.leaderExpertUser;
                    $(".leaderExpert").text(leaderExpertUser.expertName)
                    voteOverOpera();
                }

                //投票无结果，进入下一轮投票
                if (data.roundChange) {
                    $('.voteRound').val(data.currentRound);
                    roundOverOpera();
                }
                heartBeatVoteCount--;
            },
            error: function (data) {
                console.error(data);
                heartBeatVoteCount--;
            },
        });

    }

    /**
     * 展示所有专家信息
     */
    function showAllExpert() {
        $.ajax({
            url: '${ctx}/selectLeader/listAllExperts',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId
            },
            success: function (result) {
                var expertItems = $("#expertsList").find(".expert-item");
                var data = result.data.experts;
                var showCount = parseInt(expertItems.length);
                totalCount = parseInt(result.data.countExperts);
                expertCount = parseInt(result.data.expertCount);
                var agreeCount = data.length;
                //有新入场的专家重新渲染
                if (agreeCount > showCount && agreeCount <= totalCount) {
                    //展示提示信息
                    $('.wait-tip').show();
                    var getTpl = expertTemplate.innerHTML
                        , view = document.getElementById('expertsList');
                    laytpl(getTpl).render(data, function (html) {
                        view.innerHTML = html;
                    });
                }
                //所有专家入场完毕
                if (agreeCount === totalCount) {
                    $('.wait-tip').hide();
                    isAllExpertEnter = true;
                }
                //所有专家入场完毕，并且达到要求的专家数目才开始投票
                if (agreeCount === totalCount && expertCount === totalCount) {
                    window.clearInterval(expertInterval);
                    layer.msg('投票进行中...', {icon: 1});
                    heartBeatInterval = setInterval(heartBeatVote, 3000);
                }
            },
            error: function (data) {
                console.error(data);
                layer.closeAll();
                layer.msg("投票失败", {icon: 5})
            },
        });

    }

    /**
     * 投票推选
     */
    function elect(obj) {
        if (expertCount > totalCount) {
            var lackExpertCount = expertCount - totalCount;
            layer.msg("目前还缺少" + lackExpertCount + "位专家，等待专家重新录入!", {icon: 5})
            return;
        }
        if (!isAllExpertEnter) {
            layer.msg("投票尚未开始,请等待..", {icon: 5})
            return;
        }
        var indexLoad = window.top.layer.load();
        var expertId = $(obj).prev().val();
        var voteBox = $(obj).parents(".expert-item").find(".vote-count");
        var voteInfo = voteBox.text();
        var voteNum = voteInfo.substring(0, voteInfo.length - 1);
        var newVoteNum = parseInt(voteNum) + 1;
        $.ajax({
            url: '${ctx}/selectLeader/chooseLeader',
            type: 'post',
            cache: false,
            data: {
                bidExpertId: expertId,
                bidSectionId: bidSectionId
            },
            success: function (data) {
                layer.close(indexLoad);
                if (data) {
                    //投票后展示票数
                    $(".vote-count").show();
                    //修改按钮样式
                    $(".elect-btn").removeClass("blove-b").addClass("gray-b").removeAttr("onclick");
                    //跟新票数
                    voteBox.text(newVoteNum + "票");
                    layer.msg("投票成功", {icon: 1});
                } else {
                    layer.msg("投票失败", {icon: 5})
                }
            },
            error: function (data) {
                console.error(data);
                layer.close(indexLoad)
                layer.msg("投票失败", {icon: 5})
            },
        });

    }

    /**
     * 展示上一次投票信息
     */
    function showLastVoteInfo() {
        $(".lastVoted").show();
        $.ajax({
            url: '${ctx}/selectLeader/showLastVote',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId
            },
            success: function (data) {
                if (data) {
                    var voteInfo = "上轮投票结果：";
                    voteInfo += data[0].expertName + ":" + data[0].count + "票";
                    for (var i = 1; i < data.length; i++) {
                        voteInfo += ";" + data[i].expertName + ":" + data[i].count + "票";
                    }
                    $(".last-vote-info").text(voteInfo);
                }
            },
            error: function (data) {
                console.error(data);
            },
        });

    }

    /**
     * 本轮投票结束提示信息
     */
    function roundOverOpera() {
        showLastVoteInfo();
        layer.open({
            type: 1,
            title: "提示信息"
            , content: $(".round-over-tip")
            , btn: '确定'
            , btnAlign: 'c' //按钮居中
            , shade: 0 //不显示遮罩
            , yes: function () {
                layer.closeAll();
                window.location.href = window.location.href;
            }
            , end: function () {
                window.location.href = window.location.href;
            }
        });
    }

    /**
     * 投票结束提示信息
     */
    function voteOverOpera() {
        window.clearInterval(heartBeatInterval);
        showLastVoteInfo();
        layer.open({
            type: 1,
            title: "提示信息"
            , content: $(".vote-over-tip")
            , btn: '确定'
            , btnAlign: 'c' //按钮居中
            , shade: 0 //不显示遮罩
            , yes: function () {
                layer.closeAll();
                window.location.href = "${ctx}/expert/confirmBidEvalPage"
            }, end: function () {
                showLastVoteInfo();
            }
        });
    }


</script>

</body>

</html>