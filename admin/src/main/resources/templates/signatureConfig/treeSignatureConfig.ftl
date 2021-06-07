<!DOCTYPE html>
<html lang="zh" style="height: 100%; overflow: hidden">
<head>
    <meta charset="utf-8">
    <title>区划树</title>
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
<ul id="reg-tree" class="ztree"></ul>
<script type="text/javascript" defer>
    var zTreeObj;
    //定义ztree设置
    var setting = {
        view: {
            selectedMulti: false,
            dblClickExpand: dblClickExpand
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: this.onClick,
        },
        check: {
            enable: true,
            chkboxType: {"Y": "s", "N": "ps"}
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
     * 根据不同区划id获取所属场地
     *
     * @param e 事件源参数
     * @param treeId 树ID
     * @param node 节点对象
     */
    function onClick(e, treeId, node) {
        parent.right.location = '${ctx}/signatureConfig/signatureConfigTablePage?regNo=' + node.regNo + '&regName=' + encodeURI(node.name);
    }

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

    var zNodes = [];

    /**
     * 初始化树
     */
    function initZtree() {
        //销毁所有的zTree
        $.fn.zTree.destroy();
        zNodes = [];
        //通过ajax获取行政区划树
        $.post('${ctx}/reg/listReg', {}, function (tree) {
            if (tree != null) {
                for (var i = 0; tree.length > i; i++) {
                    var node = {};
                    node.id = tree[i].id;
                    if (tree[i].parentId != null) {
                        node.pId = tree[i].parentId;
                    }
                    node.name = tree[i].regName;
                    node.regNo = tree[i].regNo;
                    if (tree[i].parentId === '-1') {
                        node.open = false;
                    } else {
                        node.open = true;
                    }
                    node.enabled = tree[i].enabled;
                    if (tree[i].enabled === 0) {
                        node.font = {
                            'color': 'red'
                        };
                    }
                    zNodes.push(node);
                }
            }
            zTreeObj = $.fn.zTree.init($('#reg-tree'), setting, zNodes);
        }, "json");
    }

    $(function () {
        initZtree();
    });
</script>
</body>
</html>
