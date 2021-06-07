<@layout.extends name="layout/base.ftl">
<@layout.put block="body">
    <script src="${ctx}/js/common.js"></script>
    <style>
        .layadmin-tabsbody-item {
            background: #EEEEEE !important;
        }
    </style>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/layui-icon-extend/iconfont.css" media="all">
    <body class="layui-layout-body">
    <div id="LAY_app">
        <div class="layui-layout layui-layout-admin">
            <div class="layui-header">
                <ul class="layui-nav layui-layout-left">
                    <li class="layui-nav-item layadmin-flexible" lay-unselect>
                        <a href="javascript:void(0)" layadmin-event="flexible" title="侧边伸缩">
                            <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                        </a>
                    </li>
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:void(0)" layadmin-event="refresh" title="刷新">
                            <i class="layui-icon layui-icon-refresh-3"></i>
                        </a>
                    </li>
                </ul>
                <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">
                    <#-- <li class="layui-nav-item" lay-unselect>
                         <a lay-href="/layuiAdmin/views/app/message/index.html" layadmin-event="message" lay-text="消息中心">
                             <i class="layui-icon layui-icon-notice"></i>
                             <!-- 如果有新消息，则显示小圆点 &ndash;&gt;
                             <span class="layui-badge-dot"></span>
                         </a>
                     </li>-->
                    <li class="layui-nav-item layui-hide-xs" lay-unselect="">
                        <a href="javascript:;" layadmin-event="theme">
                            <i class="layui-icon layui-icon-theme"></i>
                        </a>
                    </li>

                    <li class="layui-nav-item layui-hide-xs" lay-unselect="">
                        <a href="javascript:;" layadmin-event="note">
                            <i class="layui-icon layui-icon-note"></i>
                        </a>
                    </li>
                    <li class="layui-nav-item layui-hide-xs" lay-unselect>
                        <a href="javascript:void(0)" layadmin-event="fullscreen">
                            <i class="layui-icon layui-icon-screen-full"></i>
                        </a>
                    </li>

                    <li class="layui-nav-item layui-hide-xs" lay-unselect>
                        <img id="userImg"
                             style="width: 35px; height: 35px;border-radius: 50%; border: 1px #aaaaaa solid"
                             onclick="show()"/>

                    </li>

                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:void(0)">
                            <#-- 用户名 -->
                            <cite class="user-name"></cite>
                        </a>
                        <dl class="layui-nav-child">
                            <dd><a href="javascript:update()">基本资料</a></dd>
                            <dd><a href="javascript:updatePass()">修改密码</a></dd>
                            <hr>
                            <dd layadmin-event="logout" style="text-align: center;"><a>退出</a></dd>
                        </dl>
                    </li>
                    <li class="layui-nav-item layui-hide-xs" lay-unselect>
                        <a href="javascript:void(0)" layadmin-event="about"><i
                                    class="layui-icon layui-icon-more-vertical"></i></a>
                    </li>
                    <li class="layui-nav-item layui-show-xs-inline-block layui-hide-sm" lay-unselect>
                        <a href="javascript:void(0)" layadmin-event="more"><i
                                    class="layui-icon layui-icon-more-vertical"></i></a>
                    </li>
                </ul>
            </div>
            <div class="layui-side layui-side-menu">
                <div class="layui-side-scroll">
                    <div class="layui-logo">
                        <span>后台管理</span>
                    </div>
                    <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu"
                        lay-filter="layadmin-system-side-menu">
                    </ul>

                </div>
            </div>
            <div class="layadmin-pagetabs" id="LAY_app_tabs">
                <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
                <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
                <div class="layui-icon layadmin-tabs-control layui-icon-down">
                    <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                        <li class="layui-nav-item" lay-unselect>
                            <a href="javascript:void(0)"></a>
                            <dl class="layui-nav-child layui-anim-fadein">
                                <dd layadmin-event="closeThisTabs"><a href="javascript:void(0)">关闭当前标签页</a></dd>
                                <dd layadmin-event="closeOtherTabs"><a href="javascript:void(0)">关闭其它标签页</a></dd>
                                <dd layadmin-event="closeAllTabs"><a href="javascript:void(0)">关闭全部标签页</a></dd>
                            </dl>
                        </li>
                    </ul>
                </div>
                <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                    <ul class="layui-tab-title" id="LAY_app_tabsheader">
                        <li lay-id="/layuiAdmin/views/home/console.html" lay-attr="/layuiAdmin/views/home/console.html"
                            class="layui-this"><i
                                    class="layui-icon layui-icon-home"></i>主页
                        </li>
                    </ul>
                </div>
            </div>
            <div class="layui-body" id="LAY_app_body">
                <div class="layadmin-tabsbody-item layui-show">
                    <iframe src="/layuiAdmin/views/home/console.html" frameborder="0" class="layadmin-iframe"></iframe>
                </div>
            </div>
            <!-- 辅助元素，一般用于移动设备下遮罩 -->
            <div class="layadmin-body-shade" layadmin-event="shade"></div>
        </div>
    </div>
    <script>
        $(function () {
            $.ajax({
                url: '${ctx}/listLeftMenu',
                cache: false,
                success: function (data) {
                    // 获取用户信息
                    $(".user-name").html(data.user.name);
                    var login_name = data.user.loginName;
                    if (!isNull(data.url)) {
                        $("#userImg").prop("src", data.url);
                    } else {
                        $("#userImg").hide();
                    }
                    var firstUrl="";

                    var menuList = data['menuList'];
                    var unInvoiceCount = data['unInvoiceCount'];
                    var isExistInvoice=false;

                    var liHtmlStr = ""
                    for (var i = 0; i < menuList.length; i++) {
                        liHtmlStr = '<li data-name="home" class="layui-nav-item">';
                        if (menuList[i].parentId === -1) {
                            var aHtmlStr = ' <a href="javascript:void(0)" lay-tips="' + menuList[i].menuName + '" lay-direction="2">' +
                                ' <i class="layui-icon ' + menuList[i].iconFont + '"></i>' +
                                ' <cite>' + menuList[i].menuName + '</cite>' +
                                ' </a>'
                            liHtmlStr += aHtmlStr;
                        }

                        for (var j = 0; j < menuList[i].subMenuList.length; j++) {
                            var dlHtmlStr = '<dl class="layui-nav-child">' +
                                ' <dd>' +
                                ' <a lay-href="' + menuList[i].subMenuList[j].url + '">' + menuList[i].subMenuList[j].menuName + '</a>' +
                                ' </dd>' +
                                ' </dl>';
                            liHtmlStr += dlHtmlStr;
                            if (menuList[i].subMenuList[j].url=='/admin/invoice/listChangePage'){
                                isExistInvoice=true;
                            }
                            if(isNull(firstUrl)){
                                firstUrl=menuList[i].subMenuList[j].url;
                            }
                        }
                        $("#LAY-system-side-menu").append(liHtmlStr);
                    }
                    if (isExistInvoice&&unInvoiceCount>0){
                        layer.open({
                            content:"你好，你有"+unInvoiceCount+"条有发票需要审核！",
                            offset:'rb',
                            anim: 2,
                            btn:[],
                            shade: 0,
                            //time:5000
                        });
                    }
                    menuList.forEach(function (menu) {
                        console.log(menu);
                        if (menu.url == '/admin/order/orderListPage'){
                            findUnprocessedOrder();
                        }
                    })
                    layui.use(['element', 'table'], function () {
                        var element = layui.element;
                        var table = layui.table;
                        //初始化动态元素，一些动态生成的元素如果不设置初始化，将不会有默认的动态效果
                        element.render();

                        table.on('tool(operate)', function (obj) {
                            var data = obj.data;
                            if (obj.event === "show") {
                                alert(1111)
                                show(data);
                            }
                        });
                    });

                    //initFristItem(firstUrl);
                }
            })

        })

        /**
         * 查找待处理订单数量
         */
        function findUnprocessedOrder () {

            $.ajax({
                url:'${ctx}/onlineRecharge/findUnprocessedOrder',
                dataType: 'json',
                //async: 'false',
                cache: false,
                success: function (data) {
                    //console.log(data)
                    if (data > 0){
                        var index = layer.open({
                            type: 1
                            ,title: false
                            ,offset: ['900px','1600px']
                            ,area: ['300px','160px']
                            ,content: '<div style="padding: 20px 80px;">你有<strong><big> '+data+' </big></strong>条线上支付信息需要你去处理</div>'
                            ,shade: 0
                            ,time: 5000
                            ,closeBtn: 0
                        });
                    }
                }
            })
        }

        function logout() {
            layer.confirm("是否退出登录？",
                {icon: 3, title: '操作提示'},
                function () {
                    layer.closeAll();
                    layer.load();
                    $.ajax({
                        url: '/logout',
                        type: 'get',
                        cache: false,
                        async: false,
                        data: {},
                        success: function (data) {
                            window.location.reload();
                        },
                        error: function (data) {
                            console.error(data);
                        },
                    });
                })
        }


        /**
         * 修改资料
         */

        function update() {
            window.layer.open({
                type: 2,
                title: '个人资料',
                shadeClose: true,
                area: ['30%', '60%'],
                btn: ['修改', '关闭'],
                content: '${ctx}/user/updateUserDataPage',
                btn1: function (index, layero) {
                    var body = window.layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.updateUser(function () {
                        window.layer.close(index);
                        window.location.reload();
                    })
                },
                btn2: function (index) {
                    window.layer.close(index);
                }
            })
        }

        /**
         * 修改密码
         */
        function updatePass() {
            window.layer.open({
                type: 2,
                title: '修改密码',
                shadeClose: true,
                area: ['30%', '60%'],
                btn: ['修改', '取消'],
                content: '${ctx}/user/updatePassPage',
                btn1: function (index, layero) {
                    var body = window.layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.updateUser(function () {
                        window.layer.close(index);
                    });
                },
                btn2: function (index) {
                    window.layer.close(index);
                }
            })
        }

        /**
         * 查看个人资料
         */
        function show() {
            window.layer.open({
                type: 2,
                title: '我的资料',
                shadeClose: true,
                area: ['30%', '60%'],
                btn: ['了解了'],
                content: '${ctx}/user/showUserData',
                btn1: function (index) {
                    window.layer.close(index);
                }
            })
        }


        /**
         * 加载第一个角色拥有的菜单
         * @return
         * @author lesgod
         * @date 2020-6-28 13:21
         */
        function  initFristItem(url) {
            /* $("#LAY_app_body iframe").attr("src",url);*/
        }
    </script>
    </@layout.put>
    <@layout.put block="js"></@layout.put>
</@layout.extends>