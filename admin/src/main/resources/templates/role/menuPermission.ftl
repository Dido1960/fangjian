<!DOCTYPE html>
<html lang="zh" style="height: 100%; overflow: hidden">
<head>
    <meta charset="utf-8">
    <title>菜单树</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <!--layer-->
    <script type="text/javascript" src="${ctx}/plugin/layer/layer.js"></script>
    <!--layuiamdin -->
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/style/admin.css" media="all">
    <!--ztree-->
    <script type="text/javascript" src="${ctx}/plugin/ztree/js/jquery.ztree.all.min.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/ztree/css/metroStyle/metroStyle.css">

    <script type="text/javascript" src="${ctx}/js/common.js"></script>

    <style type="text/css">
        .ztree {
            height: 100%;
        }
    </style>
</head>
<body style="background-color: #fff; height: 100%;">
<ul id="menu-tree" class="ztree"></ul>
<script type="text/javascript" defer>
    var id = ${role.id};
    // 定义ztree对象
    var $ztree;
    // 定义ztree 节点数组
    var zNodes = [];
    // 定义ztree设置
    var setting = {
        view: {
            selectedMulti: false,
            fontCss: getFont,
            dblClickExpand: dblClickExpand
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: this.onClick,
            beforeDrop: this.beforeDrop,
            onDrop: this.onDrop
        },
        check: {
            enable: true,
            chkboxType: {"Y": "ps", "N": "ps"}
        },
        edit: {
            enable: true,
            showRemoveBtn: false,
            showRenameBtn: false,
            drag: {
                isCopy: false
            }
        }
    };

    /**
     * 确定是否自动展开父节点
     *
     * @param treeId 数ID
     * @param node 树节点
     * @returns {boolean} 是否自动展开父节点
     */
    function dblClickExpand(treeId, node) {
        return node.level > 0;
    }

    /**
     * 初始化树结构
     */
    function initZtree(menuId) {
        // 销毁所有的zTree
        $.fn.zTree.destroy();
        zNodes = [];
        var arr = [];
        arr = menuId.split("/");
        // 通过ajax获取菜单树
        $.ajax({
            url: "${ctx}/menu/listTreeMenu",
            success: function (tree) {
                if (tree.length !== 0) {
                    for (var i = 0; tree.length > i; i++) {
                        var node = {};
                        node.id = tree[i].id;
                        if (tree[i].parentId !== null) {
                            node.pId = tree[i].parentId;
                        }
                        node.name = tree[i].menuName;
                        node.open = tree[i].parentId !== -1;
                        node.checked = arr.indexOf(tree[i].id.toString()) !== -1;
                        node.enabled = tree[i].enabled;
                        node.orderNo = tree[i].orderNo;
                        zNodes.push(node);
                    }
                }
                $ztree = $.fn.zTree.init($('#menu-tree'), setting, zNodes);
                //展开全部节点
                var treeObj = $.fn.zTree.getZTreeObj("menu-tree");
                treeObj.expandAll(true);
            }
        })
    }

    /**
     * 获取节点字体信息
     *
     * @param treeId 树ID
     * @param node 节点信息
     * @returns {{}} 节点字体信息
     */
    function getFont(treeId, node) {
        return node.font ? node.font : {};
    }

    $(function () {
        var menuId = '${role.menuId!}';
        initZtree(menuId);
    });

    /**
     * 菜单权限确认
     */
    function menuPermission() {
        var $zTree = $.fn.zTree.getZTreeObj('menu-tree');
        var nodes = $zTree.getCheckedNodes();
        var arr = [];
        if (nodes.length > 0) {
            for (var i = 0; i < nodes.length; i++) {
                arr.push(nodes[i].id);
            }
            console.log(arr)
            $.ajax({
                url: '${ctx}/role/menuPermission',
                async: false,
                traditional: "true",
                data: {
                    roleId: id,
                    ids: arr
                },
                success: function (data) {
                    data = JSON.parse(data);
                    if (data) {
                        window.top.layer.closeAll();
                        window.top.layer.msg("设置成功！", {icon: 1})
                    } else {
                        window.top.layer.msg("设置失败！", {icon: 2})
                    }
                }
            })
        } else {
            window.top.layer.msg("请至少选择一个菜单！", {icon: 2});
        }
    }
</script>
</body>
</html>
