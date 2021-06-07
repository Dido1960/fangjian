<meta charset="utf-8">
<title></title>
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
<link rel="stylesheet" href="${ctx}/css/colorBase.css">
<link rel="stylesheet" href="${ctx}/css/pointSrc.css">
<link type="text/css" rel="stylesheet" href="${ctx}/css/qualifications.css"/>
<body style="overflow: hidden">
    <table class="layui-hide"  id="listExpertUserTable"></table>
    <p style="position: fixed;bottom: 0;margin-left: 15px;"><span class="layui-badge">提示:</span> 不选择专家，系统会合成之前签名数据</p>
</body>
<script>
    layui.use('table', function(){
        var table = layui.table;

        table.render({
            elem: '#listExpertUserTable'
            ,url:'${ctx}/signature/pageExpertUser'
            ,cols: [[
                {type:'checkbox'}
                ,{field:'expertName', width:"30%",align:"center", title: '专家姓名'}
                ,{field:'phoneNumber',width: "20%" , title: '手机号', }
                ,{field:'idCard', width: "30%", title: '证件号码', }
                ,{field:'signar',align: "center" , title: '签名状态',templet: function (d) {
                    if (d.signar === 1){
                        return "已签名"
                    } else {
                        return "未签名"
                    }
                    }
            }
            ]]
            ,page: true
        });
    });

    /**
     * 选择专家
     */
    function chooseExpertUser() {
        var data = layui.table.checkStatus('listExpertUserTable').data;
        var ids = '';
        if (data.length !== 0) {
            for (var i = 0; i < data.length; i++) {
                ids += '/' + data[i].id;
            }
            data[0].id = ids;
            return data[0];
        } else {
            return null;
        }
    }
</script>
