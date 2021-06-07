<!DOCTYPE html>
<html>
<head>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <style>
        .cont {
            width: 100%;
            min-height: 700px;
            background-color: #fff;
            margin-top: 20px;
            border-radius: 14px;
            padding: 0 20%;
            box-sizing: border-box;
            position: relative;
        }
        .cont .blove-b {
            display: block;
            width: 85px;
            height: 40px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 40px;
            text-align: center;
            cursor: pointer;
            position: absolute;
            right: 20px;
            top: 25px;
            border-radius: 6px;
        }
        .cont .blove-b:hover {
            opacity: 0.7;
        }
        .print h2 {
            width: 100%;
            height: 84px;
            text-align: center;
            font-size: 18px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 84px;
            color: #1a2539;
            letter-spacing: 2px;
        }
        .print h3 {
            width: 100%;
            height: 49px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 49px;
            color: #1a2539;
        }
        .print div {
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 24px;
            color: #1a2539;
        }
        .print p {
            width: 100%;
            height: 72px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 72px;
            color: #1a2539;
            position: absolute;
            bottom: 0;
            left: 0;
            padding: 0 20%;
            margin-top: 50px;
            box-sizing: border-box;
        }
        .print p span {
            float: right;
        }
    </style>
</head>
<body>
<div class="cont">
    <div class="print" id="print">
        <h2>专家评标诚信廉政承诺书</h2>
        <h3>作为评标专家,我郑重承诺:</h3>
        <div>一、有下列情形之一的，主动提出回避:<br>
            （一）与投标人的法定代表人或者负责人有夫妻、直系血亲、三代以内旁系血亲或者近姻亲关系；<br>
            （二）参加采购活动前3年内与投标人存在劳动关系；<br>
            （三）参加采购活动前3年内担任投标人的董事、监事；<br>
            （四）参加采购活动前3年内是投标人的控股股东或者实际控制人；<br>
            （五）项目主管部门或者行政监督部门的人员；<br>
            （六）与投标人有利害关系，可能影响对投标公正评审的；<br>
            （七）曾因在招标、评标以及其他与招标投标有关活动中从事违法行为而受过行政处罚或刑事处罚的；<br>
            （八）法律法规规定的其他情形。<br>
            二、严格执行有关招标投标的法律法规和部门规章，依照招标文件和评标办法，公正客观、独立自主
            地进行评审，对自己的评审意见负责。不对外透露对投标文件的评审和比较、中标候选人的推荐情况
            以及与评标有关的其他情况。<br>
            三、在评标过程中，严格遵守评标工作纪律，不与任何投标人或者与招标结果有利害关系的人进行私
            下接触，不收受投标人、中介人、其他利害关系人的财物或者其他好处，不向招标人征询其确定中标
            人的意向，不接受任何单位或者个人明示或者暗示提出的倾向或者排斥特定投标人的要求，不做其他
            不客观、不公正履行职务的行为。
            我将严守以上承诺，自觉接受审计机关、纪检监察机关以及其他有关部门依法实施的监督，并承担相应的法律责任。
        </div>
        <p>承诺人：<br><span>日期：${now.year}年${now.monthValue}月${now.dayOfMonth}日</span></p>
    </div>
</div>

<script>

</script>
</body>
</html>