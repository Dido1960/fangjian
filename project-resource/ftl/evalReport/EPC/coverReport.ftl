<head>
    <title>评标报告封面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
    <style>
        .pageNext {
            page-break-before: always;
        }
    </style>
</head>
<#escape x as x!"">
    <div class="show">
        <div class="panel pageNext">
            <table class="base-normal-table">
                <thead></thead>
                <tbody>
                <tr>
                    <td style="height: 180px; padding-top: 100px">
                        <div class="well-box base-header-mid" style="text-align: center;line-height:50px"><span style="text-decoration: underline;">${bidSection.bidSectionName} </span>工程</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="well-box base-header first-title" style="text-align: center;">总承包评标报告</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div style="text-align: center;padding-top: 500px;">
                            <span class="my-date">${date[0]}年</span><span class="my-date">${date[1]}月</span><span
                                    class="my-date">${date[2]}日</span>
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
                        <div class="well-box" style="text-align: center;"><u>${bidSection.bidSectionName}</u>工程招标
                        </div>
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
                            <li class="my-two-indent">五、推荐中标候选人顺序</li>
                            <li style="text-indent: 2em;">
                                通过对初步评审合格的投标文件的商务部分和技术部分进行详细评审，推荐中标候选人
                                排序如下：
                            </li>
                            <li style="text-indent: 2em;">
                                <table height="100%" class="base-normal-table base-write">
                                    <thead>
                                    <tr class="base-border base-bold" style="text-align: center">
                                        <td align="center" width="40%">投标单位名称</td>
                                        <td align="center" width="20%">投标报价(元)</td>
                                        <td align="center" width="20%">总得分</td>
                                        <td align="center" width="20%">名次</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#if evalResultEpcs??>
                                        <#list evalResultEpcs as evalResult>
                                            <tr class="base-border">
                                                <td align="center">${evalResult.bidderName}</td>
                                                <#--投标报价-->
                                                <td align="center">
                                                    <#if evalResult.bidPrice??>
                                                        ${(evalResult.bidPrice?number)?string(",###.##")}
                                                    </#if>
                                                </td>
                                                <td align="center">
                                                    ${evalResult.totalScore}
                                                </td>
                                                <td align="center">${evalResult.orderNo}</td>
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
                                3、资格审查表
                            </li>
                            <li style="text-indent: 6em;">
                                4、初步评审表
                            </li>
                            <li style="text-indent: 6em;">
                                5、详细评审明细表
                            </li>
                            <li style="text-indent: 6em;">
                                6、报价得分表
                            </li>
                            <li style="text-indent: 6em;">
                                7、评标得分汇总表
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
                    <td style="height: 50px; padding-top: 0;text-indent: 4rem">
                        <div>监标人：</div>
                    </td>
                </tr>
                <tr>
                    <td style="float: right; padding-top: 0">
                        <div>${date[0]}年${date[1]}月${date[2]}日</div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
<#--    <div class="show">-->
<#--        <div class="panel pageNext">-->
<#--            <table class="base-normal-table">-->
<#--                <thead></thead>-->
<#--                <tbody>-->
<#--                <tr>-->
<#--                    <td style="height: 180px; padding-top: 0">-->
<#--                        <div class="well-box base-header-mid" style="text-align: center;"><span style="text-decoration: underline;">${bidSection.bidSectionName} </span>工程</div>-->
<#--                    </td>-->
<#--                </tr>-->
<#--                <tr>-->
<#--                    <td>-->
<#--                        <div class="well-box base-header first-title" style="text-align: center;">总承包评标报告</div>-->
<#--                    </td>-->
<#--                </tr>-->
<#--                <tr class="base-tfoot">-->
<#--                    <td>-->
<#--&lt;#&ndash;                        <div style="text-align: left;margin-left: 150px;padding-top: 300px;font-size: 20px;text-indent: 2rem">&emsp;&emsp;标段编号：<span class="base-underline">${bidSection.bidSectionCode}</span></div>&ndash;&gt;-->
<#--&lt;#&ndash;                        <div style="text-align: left;margin-left: 150px;padding-top: 60px;font-size: 20px;text-indent: 2rem">标段名称：<span class="base-underline">${bidSection.bidSectionName}</span></div>&ndash;&gt;-->
<#--&lt;#&ndash;                        <div style="text-align: left;margin-left: 150px;padding-top: 60px;font-size: 20px;">招标代理机构：<span class="base-underline">${tenderProject.tenderAgencyName}</span></div>&ndash;&gt;-->
<#--                        <div style="text-align: center;padding-top: 600px;font-size: 20px;">-->
<#--                            <span class="my-date">${date[0]}年</span><span class="my-date">${date[1]}月</span><span class="my-date">${date[2]}日</span>-->
<#--                        </div>-->
<#--                    </td>-->
<#--                </tr>-->
<#--                </tbody>-->
<#--            </table>-->
<#--        </div>-->

<#--        <div class="panel pageNext">-->
<#--            <table class="base-normal-table">-->
<#--                <thead></thead>-->
<#--                <tbody>-->
<#--                <tr>-->
<#--                    <td>-->
<#--                        <ul class="base-ul" style="font-size: 16px">-->
<#--                            <li style="text-indent: 0"> ${tenderProject.tendererName}:</li>-->
<#--                            <li style="text-indent: 2em;">-->
<#--                                <span>${bidSection.bidSectionName}</span>，于<span>${openTime.year}</span>年<span>${openTime.month}</span>月<span>${openTime.day}</span>日<span>${openTime.minutesAndSeconds}</span>时开标。-->
<#--                                评标委员会按照《中华人民共和国招标投标法》及本项目招标文件和评标办法，对投标人-->
<#--                                <u>-->
<#--                                    <#list passFirstStepBidders as bidder>-->
<#--                                        <#if bidder_index < passFirstStepBidders?size - 1>-->
<#--                                            ${bidder.bidderName}、-->
<#--                                        <#else>-->
<#--                                            ${bidder.bidderName}-->
<#--                                        </#if>-->
<#--                                    </#list>-->
<#--                                </u>-->
<#--                                提交的投标文件进行了初步评审，<u>${passFirstStepBidders?size}</u> 家通过了初步评审。对 <u>${passFirstStepBidders?size}</u> 家符合招标文件要求进入详细评审的投标文件进行了详细、认真的评审，而且对每个有效的投标文件进行了比较评审，最终向你推荐中标候选人顺序如下：-->
<#--                            </li>-->
<#--                            <li style="text-indent: 2em;">-->
<#--                                <table height="100%" class="base-normal-table base-write">-->
<#--                                    <thead>-->
<#--                                    <tr class="base-border base-bold" style="text-align: center">-->
<#--                                        <td align="center" width="10%">名次</td>-->
<#--                                        <td align="center" >投标人名称</td>-->
<#--                                        <td align="center" width="15%">总得分</td>-->
<#--                                    </tr>-->
<#--                                    </thead>-->
<#--                                    <tbody>-->
<#--                                        <#if evalResultEpcs??>-->
<#--                                            <#list evalResultEpcs as evalResult>-->
<#--                                                <tr class="base-border">-->
<#--                                                    <td align="center">${evalResult.orderNo}</td>-->
<#--                                                    <td align="center">${evalResult.bidderName}</td>-->
<#--                                                    <td align="center">${evalResult.totalScore}</td>-->
<#--                                                </tr>-->
<#--                                            </#list>-->
<#--                                        <#else>-->
<#--                                            <tr class="base-border">-->
<#--                                                <td></td>-->
<#--                                                <td></td>-->
<#--                                                <td></td>-->
<#--                                            </tr>-->
<#--                                        </#if>-->
<#--                                    </tbody>-->
<#--                                </table>-->
<#--                            </li>-->
<#--                            <li style="text-indent: 2em;">依据《中华人民共和国招标投标法》及国家、省、市的有关规定确定中标人，并办理有关确认手续。</li>-->
<#--                            <li>附件：</li>-->
<#--                            <li></li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                1、开标记录表-->
<#--                            </li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                2、评标专家签到表-->
<#--                            </li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                3、资格审查表-->
<#--                            </li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                4、初步评审表-->
<#--                            </li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                5、详细评审明细表-->
<#--                            </li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                6、报价得分表-->
<#--                            </li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                7、评标得分汇总表-->
<#--                            </li>-->
<#--                            <li style="text-indent: 6em;">-->
<#--                                8、评审意见汇总表-->
<#--                            </li>-->
<#--                        </ul>-->
<#--                    </td>-->
<#--                </tr>-->
<#--                </tbody>-->
<#--                <tfoot>-->
<#--                    <tr>-->
<#--                        <td style="height: 50px; padding-top: 0;text-indent: 4rem">-->
<#--                            <div>评标委员会成员签字:</div>-->
<#--                        </td>-->
<#--                    </tr>-->
<#--                    <tr>-->
<#--                        <td style="height: 100px; padding-top: 0;text-indent: 4rem">-->
<#--                            <div>监标人：</div>-->
<#--                        </td>-->
<#--                    </tr>-->
<#--                    <tr>-->
<#--                        <td style="float: right;padding-top: 0">-->
<#--                            <div>${date[0]}年${date[1]}月${date[2]}日</div>-->
<#--                        </td>-->
<#--                    </tr>-->
<#--                </tfoot>-->
<#--            </table>-->

<#--        </div>-->
<#--    </div>-->


</#escape>