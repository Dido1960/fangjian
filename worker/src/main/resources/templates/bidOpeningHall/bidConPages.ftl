<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>开标大厅</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/css/bidConPages.css">
</head>

<body>
    <div class="box">
          <h3 class="caption">
        <#if active=="test">
            测试服
        <#elseif active=="dev">
            开发
        <#else>
        </#if>
    </h3>

        <ul class="center">
            <li onclick="goToPage('${bidConAgency}')">
                <div class="top">
                    <img src="../img/change_1.png" alt="">
                </div>
                <div class="bottom">
                    <span class="touch">开标人员</span>
                </div>
            </li>
            <li onclick="goToPage('${bidConBidder}')">
                <div class="top">
                    <img src="${ctx}/img/change_2.png" alt="">
                </div>
                <div class="bottom">
                    <span class="touch">投标人员</span>
                </div>
            </li>
            <li onclick="goToPage('${bidConWorker}')">
                <div class="top">
                    <img src="${ctx}/img/change_3.png" alt="">
                </div>
                <div class="bottom">
                    <span class="touch">专家录入</span>
                </div>
            </li>
            <li onclick="goToPage('${bidConExpert}')">
                <div class="top">
                    <img src="${ctx}/img/change_4.png" alt="">
                </div>
                <div class="bottom">
                    <span class="touch">评标专家</span>
                </div>
            </li>
            <li onclick="goToPage('${bidConSupervise}')">
                <div class="top">
                    <img src="${ctx}/img/change_5.png" alt="">
                </div>
                <div class="bottom">
                    <span class="touch">主管部门</span>
                </div>
            </li>
            <li onclick="goToPage('${admin}')">
                <div class="top">
                    <img src="../img/change_1.png" alt="">
                </div>
                <div class="bottom">
                    <span class="touch">系统维护</span>
                </div>
            </li>
        </ul>
        <div class="foot">copyright © 2020</div>
    </div>
</body>
<script>
    function showtime() {
        var nowtime = new Date()
        var hours = nowtime.getHours()
        var min = nowtime.getMinutes()
        var sen = nowtime.getSeconds()
        var date = nowtime.getDate();
        var hour = document.querySelector('.hours')
        var mins = document.querySelector(".min")
        var senc = document.querySelector(".sen")
        hour.innerHTML = hours
        mins.innerHTML = min
        senc.innerHTML = sen
        // console.log(nowtime.getHours())
        // console.log(nowtime.getMinutes())
        // console.log(nowtime.getSeconds())
    }
    setInterval("showtime()", 1000)
    // $('.center').on('click', 'li .touch', function () {
    //     $(this).parent('.bottom').parent('li').addClass('tot').siblings().removeClass('tot')
    // })

    function goToPage(url) {
        if (!isNull(url)) {
            window.top.location.href = url;
        }else {
            layer.msg("敬请期待!", {icon: 1});
        }
    }
</script>

</html>