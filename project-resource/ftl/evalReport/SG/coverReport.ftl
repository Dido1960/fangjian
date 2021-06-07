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
                        <div class="well-box base-header-mid" style="text-align: center;line-height:50px"><span
                                    style="text-decoration: underline;;line-height: 2rem;">${bidSection.bidSectionName} </span>工程
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="well-box base-header first-title" style="text-align: center;">施工评标报告</div>
                    </td>
                </tr>
                <#--                <tr>-->
                <#--                    <td style="height: 600px;">-->
                <#--                        <div class="base-header" style="text-align: center; line-height: 150px; font-size: 60px;">评<br>标<br>报<br>告</div>-->
                <#--                    </td>-->
                <#--                </tr>-->
                <tr>
                    <td>
                        <div style="text-align: center;padding-top: 600px;">
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
                <tbody>
                <tr>
                    <td style="height: 50px; padding-top: 0">
                        <div class="well-box" style="text-align: center;line-height: 2rem;">
                            <u>${bidSection.bidSectionName}</u>工程招标
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
                                在招标文件规定的投标文件递交的截止时间内，共收到 <span
                                        style="text-underline: black">&emsp;&emsp;${allBidders?size}&emsp;&emsp;</span>
                                家投标人递交的投标文件。
                                <#if passFirstStepBidders>
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
                                </#if>
                                <#if noPassFirstStepBidders>
                                    ，共 <span style="text-underline: black">&emsp;&emsp;${noPassFirstStepBidders?size}&emsp;&emsp;</span> 家未通过评审。
                                <#else >
                                    。
                                </#if>
                            </li>
                            <li>
                                <#if noPassFirstStepBidders>
                                <#--                                    <u>-->
                                <#--                                        <#list noPassFirstStepBidders as bidder>-->
                                <#--                                            <#if bidder_index < noPassFirstStepBidders?size - 1>-->
                                <#--                                                ${bidder.bidderName}、-->
                                <#--                                            <#else >-->
                                <#--                                                ${bidder.bidderName}-->
                                <#--                                            </#if>-->
                                <#--                                        </#list>-->
                                <#--                                    </u>-->
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
                                通过对初步评审合格的投标文件的商务部分和技术部分进行详细评审，推荐中标候选人排序如下:
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
                                    <#if evalResultSgs??>
                                        <#list evalResultSgs as evalResult>
                                            <tr class="base-border">
                                                <td align="center">${evalResult.bidderName}</td>
                                                <#--投标报价-->
                                                <td align="center">
                                                    <#if evalResult.bidPrice??>
                                                        ${(evalResult.bidPrice?number)?string(",###.00")}
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
                                一、评标专家签到表
                            </li>
                            <li style="text-indent: 6em;">
                                二、开标记录表
                            </li>
                            <li style="text-indent: 6em;">
                                三、投标文件废标情况评审表
                            </li>
                            <li style="text-indent: 6em;">
                                四、施工能力、施工方案或施工组织设计扣分表
                            </li>
                            <li style="text-indent: 6em;">
                                五、安全质量事故扣分表
                            </li>
                            <li style="text-indent: 6em;">
                                六、建筑市场不良记录扣分表
                            </li>
                            <li style="text-indent: 6em;">
                                七、报价得分表
                            </li>
                            <#if tenderDoc.mutualSecurityStatus == 1>
                                <li style="text-indent: 6em;">
                                    八、互保共建加分表
                                </li>
                            </#if>
                            <li style="text-indent: 6em;">
                                <#if tenderDoc.mutualSecurityStatus == 1>
                                    九、
                                <#else >
                                    八、
                                </#if>
                                评标得分汇总表
                            </li>
                            <li style="text-indent: 6em;">
                                <#if tenderDoc.mutualSecurityStatus == 1>
                                    十、
                                <#else >
                                    九、
                                </#if>
                                评审意见汇总表
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
                    <td style="height: 100px; padding-top: 0;text-indent: 4rem">
                        <div>监标人：</div>
                    </td>
                </tr>
                <tr>
                    <td style="float: right; padding-top: 0">
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