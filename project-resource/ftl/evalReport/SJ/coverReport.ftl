<head>
    <title>评标报告封面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
    <style>
        .pageNext{page-break-before: always;}
    </style>
</head>
<#escape x as x!"">
    <div class="show">
        <div class="panel pageNext">
            <table class="base-normal-table">
                <thead></thead>
                <tbody>
                <tr>
                    <td style="height: 180px; padding-top: 0">
                        <div class="well-box base-header-mid" style="text-align: center;line-height:50px"><span style="text-decoration: underline;">${bidSection.bidSectionName} </span>工程</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="well-box base-header first-title" style="text-align: center;">设计评标报告</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div style="text-align: center;padding-top: 500px;">
                            <span class="my-date">${date[0]}年</span><span class="my-date">${date[1]}月</span><span class="my-date">${date[2]}日</span>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="panel pageNext">
            <table class="base-normal-table">
                <thead></thead>
                <tbody>
                <tr>
                    <td style="height: 50px; padding-top: 0">
                        <div class="well-box" style="text-align: center;"><u>${bidSection.bidSectionName}</u>工程招标</div>
                    </td>
                </tr>
                <tr>
                    <td style="height: 50px; padding-top: 0">
                        <div class="well-box base-header-mid" style="text-align: center;">评标报告</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <ul class="base-ul" style="font-size: 16px">
                            <li style="text-indent: 0"> ${tenderProject.tendererName}:</li>
                            <li style="text-indent: 2em;">
                                你单位<span>${bidSection.bidSectionName}</span>工程招标的开标工作，于<span>${openTime.year}</span>年<span>${openTime.month}</span>月<span>${openTime.day}</span>日<span>${openTime.minutesAndSeconds}</span>（北京时间）在<span>${reg.regName}</span>公共资源交易中心如期进行，依法组建的评标委员会，按照招标文件的要求和法定程序对投标文件进行了认真负责的评审，现评标工作结束，评标情况报告如下：
                            </li>
                            <li style="text-indent: 2em;">一、评标委员会组成</li>
                            <li style="text-indent: 2em;">
                                评标委员会<span>${tenderDoc.expertCount}</span>人，其中：招标人代表<span>${tenderDoc.representativeCount}</span>人，从《甘肃省建设工程评标专家库》中随机抽取的评标专家<span>${tenderDoc.expertCount - tenderDoc.representativeCount}</span>人。评标委员会推荐<span>${expertChairman.expertName}</span>为评标委员会主任。
                            </li>
                            <li style="text-indent: 2em;">二、评标原则</li>
                            <li style="text-indent: 2em;">
                                严格遵守“公开、公平、公正、诚实信用”的原则。
                            </li>
                            <li style="text-indent: 2em;">三、评标的依据</li>
                            <li style="text-indent: 2em;">
                                招标文件规定的评标标准和方法。
                            </li>
                            <li style="text-indent: 2em;">四、评标过程</li>
                            <li style="text-indent: 2em;">
                                在招标文件规定的投标文件递交的截止时间内，共收到 <span style="text-underline: black">&emsp;&emsp;${allBidders?size}&emsp;&emsp;</span> 家投标人递交的投标文件。
                                经过初步评审，
                                <u>
                                    <#list passFirstStepBidders as bidder>
                                        <#if bidder_index < passFirstStepBidders?size - 1>
                                            ${bidder.bidderName}、
                                        <#else >
                                            ${bidder.bidderName}
                                        </#if>
                                    </#list>
                                </u>
                                通过
                                <#if noPassFirstStepBidders>
                                    ，共 <span style="text-underline: black">&emsp;&emsp;${noPassFirstStepBidders?size}&emsp;&emsp;</span> 家未通过评审。
                                <#else >
                                    。
                                </#if>
                            </li>
                            <li>
                                <#if noPassFirstStepBidders>
                                    <p>（未通过原因详见《评审意见表》）。</p>
                                <#else >
                                    <p style="text-indent: 0"><u>无投标单位</u> 废标。</p>
                                </#if>
                            </li>
                        </ul>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="panel pageNext">
            <table class="base-normal-table">
                <thead></thead>
                <tbody>
                <tr>
                    <td>
                        <ul class="base-ul" style="font-size: 16px">
                            <li class="my-two-indent">五、评审结果</li>
                            <li style="text-indent: 2em;">
                                <table height="100%" class="base-normal-table base-write">
                                    <thead>
                                        <tr class="base-border base-bold" style="text-align: center">
                                            <td align="center" width="60%">投标单位名称</td>
                                            <td align="center" width="20%">投标报价</td>
                                            <td align="center" width="20%">名次</td>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <#if listCandidateSuccesses??>
                                        <#list listCandidateSuccesses as result>
                                            <tr class="base-border">
                                                <td align="center">${result.bidderName}</td>
                                                <td align="center">
                                                    <#if !result.bidderPriceType?? || result.bidderPriceType == "总价">
                                                        <#if result.bidderPrice?? && result.bidderPrice != ''>
                                                            ${((result.bidderPrice)?number)?string(",###.00")}
                                                        </#if>
                                                    <#else>
                                                        ${result.bidderPriceType}${result.bidderPrice}
                                                    </#if>
                                                </td>
                                                <td align="center">${result.ranking}</td>
                                            </tr>
                                        </#list>
                                    </#if>
                                    </tbody>
                                </table>
                            </li>
                            <li style="text-indent: 2em;">附件：</li>
                            <li></li>
                            <li style="text-indent: 6em;">
                                1、评标专家签到表
                            </li>
                            <li style="text-indent: 6em;">
                                2、开标记录表
                            </li>
                            <li style="text-indent: 6em;">
                                3、初步评审表
                            </li>
                            <li style="text-indent: 6em;">
                                4、合格投标人名单
                            </li>
                            <li style="text-indent: 6em;">
                                5、推荐中标候选人名单
                            </li>
                            <li style="text-indent: 6em;">
                                6、评委推荐表
                            </li>
                            <li style="text-indent: 6em;">
                                7、评委投票统计表
                            </li>
                            <li style="text-indent: 6em;">
                                8、评审意见汇总表
                            </li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td style="height: 50px; padding-top: 0;text-indent: 4rem">
                        <div>评标委员会成员签字:</div>
                    </td>
                </tr>
                <tr>
                    <td style="height: 100px; text-indent: 4rem">
                        <div>监标人：</div>
                    </td>
                </tr>
                <tr>
                    <td style="float: right;padding-top: 0">
                        <div>${date[0]}年${date[1]}月${date[2]}日</div>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                </tfoot>
            </table>
        </div>
    </div>
</#escape>