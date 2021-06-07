<style>
    .right-center {
        width: 100%;
        height: 240px;
        background: rgba(255, 255, 255, 1);
        opacity: 1;
        font-size: 36px;
        font-family: "Microsoft YaHei";
        font-weight: bold;
        line-height: 240px;
        text-align: center;
        color: rgba(34, 49, 101, 1);
        margin-top: 20px;
        display: none;
    }
</style>
<div class="right-center">当前尚未开标</div>
<div class="right-center">正在开标中</div>
<div class="right-center">开标已结束</div>

<script>
    var isOpenBid = true;
    var isOpenBidEnd = true;
    <#if !isOpenBid>
    isOpenBid = false;
    </#if>
    <#if !isOpenBidEnd>
    isOpenBidEnd = false;
    </#if>
    $(function () {
        if (isOpenBid && isOpenBidEnd) {
            $(".right-center").eq(1).show();
        } else if (!isOpenBidEnd) {
            $(".right-center").eq(2).show();
        } else {
            $(".right-center").eq(0).show();
        }
    })
</script>