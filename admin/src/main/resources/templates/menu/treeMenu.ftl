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
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>

<script>
    function updateState(enabled, msg) {
        var $zTree = $.fn.zTree.getZTreeObj('menu-tree');
        var nodes = $zTree.getCheckedNodes();
        if (nodes.length > 0) {
            var ids = [];
            for (var i = 0; nodes.length > i; i++) {
                if (nodes[i].id !== -1) {
                    ids.push(nodes[i].id);
                }
            }
            $.ajax({
                url: "${ctx}/menu/updateMenuEnabled",
                data: {
                    ids: ids.toString(),
                    enabled: enabled
                },
                success: function (bool) {
                    if (bool) {
                        layer.msg(msg, {
                            icon: 1, end: function () {
                                initZtree();
                            }
                        });
                        initZtree();
                    }
                }
            })
        } else {
            window.top.layer.msg("请先选择一个菜单目录!", {icon: 2});
        }
    }

</script>


<script type="text/javascript" defer>
    // 定义ztree对象
    var $ztree;
    // 定义ztree 节点数组
    var zNodes = [];
    // 定义ztree设置
    var setting = {
        view: {
            selectedMulti: false,
            fontCss: getFont,
            showTitle: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: this.onClick,
            beforeDrop: zTreeBeforeDrop,
            onDrop: zTreeOnDrop,
            onRightClick: zTreeOnRightClick
        },
        key: {
            title: "左键添加，右键修改"
        },
        check: {
            enable: true,
            chkboxType: {"Y": "s", "N": "ps"}
        }
        ,
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
     * 定义树单击事件
     *
     * @param e 事件源参数
     * @param treeId 树ID
     * @param node 节点对象
     */
    function onClick(e, treeId, node) {
        parent.right.location = '${ctx}/menu/addMenuPage?id=' + node.id + '&menuName=' + encodeURI(node.name);
    }

    /**、
     * 鼠标右键事件
     *
     * @param e 事件源参数
     * @param treeId 树ID
     * @param node 节点对象
     */
    function zTreeOnRightClick(e, treeId, node) {
        if (node.id === -1) {
            window.top.layer.msg("无法修改根节点!", {icon: 2});
            return false;
        } else {
            parent.right.location = '${ctx}/menu/updateMenuPage?id=' + node.id;
        }
    };

    /**
     * 节点拖拽结束前 触发事件
     *
     * @param treeId 树ID
     * @param treeNodes 被拖拽的节点JSON数据集合
     * @param targetNode 目标节点JSON
     * @param moveType 移动到目标节点的相对位置
     * @param isCopy 操作类型是复制还是移动
     * @returns {boolean} 如果返回false则恢复节点并阻止zTreeOnDrag执行
     */
    function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
        // 不能移动为根节点或子节点
        if (targetNode.pId == null) {
            window.top.layer.msg("只允许同级间移动!", {icon: 2});
            return false;
        }
        if (moveType === 'inner') {
            window.top.layer.msg("不允许移动为子节点!", {icon: 2});
            return false;
        }
        return true;
    }

    /**
     * 拖拽菜单
     *
     * @param event 数据源
     * @param treeId 树ID
     * @param treeNodes 被拖拽的节点JSON数据集合
     * @param targetNode 目标节点JSON
     * @param moveType 移动到目标节点的相对位置
     * @param isCopy 操作类型是复制还是移动
     */
    function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
        var moveNode = treeNodes[0];
        if (moveType !== null) {
            var id = moveNode.id;
            var parentId;
            if (moveNode.pId == null) {
                parentId = -1
            } else {
                parentId = (moveNode.getParentNode()).id;
            }
            var orderNo = 1;
            console.log(moveNode.getPreNode())
            // 判断是否还存在上一个节点,默认假设不存在（即: orderNo= 1）
            if (moveNode.getPreNode() !== null) {
                // 设置序号为，移动节点序号减一
                orderNo = moveNode.orderNo - 1;
            }
            $.ajax({
                url: "${ctx}/menu/dragMenu",
                data: {
                    id: id,
                    parentId: parentId,
                    orderNo: orderNo
                },
                success: function (bool) {
                    if (!bool) {
                        window.top.layer.msg("拖拽失败!", {icon: 2});
                    } else {
                        window.top.layer.msg("拖拽成功!", {icon: 1});
                    }
                    initZtree();
                }
            })
        }
    }

    /**
     * 初始化树结构
     */
    function initZtree() {
        // 销毁所有的zTree
        $.fn.zTree.destroy();
        zNodes = [];
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
                        node.enabled = tree[i].enabled;
                        if (tree[i].enabled === 0) {
                            node.font = {
                                'color': 'red'
                            };
                        }
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
     * 新增菜单事件
     */
    function add() {
        var $zTree = $.fn.zTree.getZTreeObj('menu-tree');
        var nodes = $zTree.getCheckedNodes();
        if (nodes.length === 1) {
            parent.right.location = '${ctx}/menu/addMenuPage?id=' + nodes[0].id + '&menuName=' + encodeURI(nodes[0].name);
        } else if (nodes.length > 1) {
            window.top.layer.msg("只允许选择一个菜单!", {icon: 2})
        } else {
            parent.right.location.href = '${ctx}/menu/addMenuPage';
        }

    }

    /**
     * 启用菜单事件
     */
    function enabled() {
        updateState(1, "启用成功！")
    }

    /**
     * 禁用菜单事件
     */
    function dis() {
        updateState(0, "禁用成功！");
    }

    /**
     * 删除菜单事件
     */
    function del() {
        var $zTree = $.fn.zTree.getZTreeObj('menu-tree');
        var nodes = $zTree.getCheckedNodes();
        if (nodes.length > 0) {
            window.top.layer.confirm("确定要删除该菜单吗?", {icon: 3, title: '提示'}, function (index) {
                var ids = [];
                for (var i = 0; nodes.length > i; i++) {
                    var halfCheck = nodes[i].getCheckStatus();
                    if (!halfCheck.half) {
                        ids.push(nodes[i].id);
                    }
                }
                $.ajax({
                    url: "${ctx}/menu/delMenu",
                    data: {
                        ids: ids.toString()
                    },
                    success: function (bool) {
                        if (bool) {
                            window.top.layer.msg("删除成功!", {icon: 1});
                            initZtree();
                        }
                    }
                });

                window.top.layer.close(index);
            }, function (index) {
                window.top.layer.msg("已取消!", {icon: 1});
                window.top.layer.close(index);
            })
        } else {
            window.top.layer.msg("请先选择一个菜单目录!", {icon: 2});
        }
    }

    /**
     * 添加树节点
     *
     * @param parentId 父级ID
     * @param id ID
     * @param menuName 菜单名称
     * @param enabled 启用状态
     * @param orderNo 排序号
     */
    function addNode(parentId, id, menuName, enabled, orderNo) {
        var $zTree = $.fn.zTree.getZTreeObj('menu-tree');
        var parentNode = $zTree.getNodeByParam("id", parentId, null);
        $zTree.addNodes(parentNode, [{id: id, pId: parentId, name: menuName, enabled: enabled, orderNo: orderNo}]);
    }

    /**
     * 更新树节点
     *
     * @param id
     * @param menuName
     */
    function updateNode(id, menuName) {
        var $zTree = $.fn.zTree.getZTreeObj('menu-tree');
        var currentNode = $zTree.getNodeByParam("id", id, null);
        currentNode.name = menuName;
        $zTree.editName(currentNode);
        $zTree.cancelEditName();
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

    initZtree();
</script>
</body>
</html>
