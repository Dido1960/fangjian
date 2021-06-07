/** layuiAdmin.std-v2020.1.24 LPPL License By https://www.layui.com/admin/ */
;layui.define(function (e) {
    var i = (layui.$, layui.layer, layui.laytpl, layui.setter, layui.view, layui.admin);
    i.events.logout = function () {
        i.req({
            url: "/logout",
            type: "get",
            data: {},
            success: function (e) {
                i.exit(function () {
                    location.href = "/login.html"
                })
            }
        })
    }, e("common", {})
});