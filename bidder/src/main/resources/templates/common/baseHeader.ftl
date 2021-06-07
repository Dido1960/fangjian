<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/common.js"></script>
<link rel="stylesheet" href="${ctx}/css/header.css">
<link rel="stylesheet" href="${ctx}/css/utils.css">
<script>
    $(function () {
        $.ajax({
            url: '${ctx}/login/getUser',
            type: 'post',
            cache: false,
            success: function (data) {
                if (data.name.substring(0, 3) == "游客_") {
                    $.ajax({
                        url: '${ctx}/logout',
                        type: 'post',
                        cache: false,
                        success: function () {
                            window.location.href = "${ctx}/login.html";
                        }, error: function () {
                            window.location.href = "${ctx}/login.html";
                        }
                    });
                    return false;
                }
                $('header .username').html(data.name);
                $('header .username').attr('title', data.name)
            }
        });
    });

    /**
     * 退出登录
     */
    function exitSystem() {
        hide_IWeb2018();
        window.top.layerConfirm("确认要退出系统？", function () {
            // 设置全局AJAX请求
            $.ajaxSetup({
                type: "POST",
                cache: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrf_header, csrf_token);
                }
            })
            $.ajax({
                url: '${ctx}/logout',
                type: 'post',
                cache: false,
                success: function () {
                    layerSuccess("已安全退出登录", function () {
                        window.location.href = "${ctx}/login.html";
                    })

                }, error: function () {
                    window.location.href = "${ctx}/login.html";
                }
            });
        }, null)
    }

</script>
<header>
    <div class="text">
        <div class="name"><img class="logo" src="/img/logo-white.png"/>甘肃省房建市政电子辅助开标系统</div>
        <div class="info">
            <#if  hideProjectBtn?? && hideProjectBtn == 1>
            <#else>
                <a href="/index" class="header-button button-hover back">返回项目管理</a>
            </#if>
            <div class="header-text" onclick="exitSystem()">
                <span class="username"></span>
                <img src="/img/off.png" alt=""/>
            </div>
        </div>
    </div>
</header>


