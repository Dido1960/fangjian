/*
SQLyog Ultimate v12.14 (64 bit)
MySQL - 5.7.17-log : Database - bid_con_lz_common
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bid_con_lz_common` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `bid_con_lz_common`;

/*Table structure for table `api_auth` */

DROP TABLE IF EXISTS `api_auth`;

CREATE TABLE `api_auth` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `API_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '接口名称',
  `PLATFORM` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '平台授权码',
  `API_KEY` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'API授权码',
  `REMARK` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '授权说明',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='API接口认证信息';

/*Data for the table `api_auth` */

insert  into `api_auth`(`ID`,`INSERT_TIME`,`API_NAME`,`PLATFORM`,`API_KEY`,`REMARK`,`ENABLED`) values
(1,'2020-06-30 10:10:43','servicePlatformDock','02510a79-784d-4880-8df8-3e917900e22d','cd033f02-5a36-480e-838a-8055a8063701','国泰新点授权码测试',0),
(3,'2021-01-16 11:23:50','servicePlatformDock','106b804b-25ea-4c62-8392-e60829c38831','da270849-e3c6-4d4b-a9b5-cfb248fb26d0','国泰新点授权码正式',1);

/*Table structure for table `back_push_status` */

DROP TABLE IF EXISTS `back_push_status`;

CREATE TABLE `back_push_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BACK_ID` int(11) DEFAULT NULL COMMENT '回退申请ID',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '专家ID',
  `PUSH_RESULT` int(11) DEFAULT '0' COMMENT '推送结果(0:未接收 1:已接收)',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='回退申请推送状态';

/*Data for the table `back_push_status` */

/*Table structure for table `bid_apply` */

DROP TABLE IF EXISTS `bid_apply`;

CREATE TABLE `bid_apply` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `CHAIR_MAN` int(11) DEFAULT NULL COMMENT '专家组长主键ID',
  `VOTE_COUNT` int(11) DEFAULT NULL COMMENT '投票轮次',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='评标申请记录';

/*Data for the table `bid_apply` */

/*Table structure for table `bid_section` */

DROP TABLE IF EXISTS `bid_section`;

CREATE TABLE `bid_section` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `TENDER_PROJECT_ID` int(11) DEFAULT NULL COMMENT '招标项目主键ID',
  `BID_SECTION_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '标段编号',
  `BID_SECTION_NAME` varchar(300) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '标段名称',
  `BID_SECTION_CONTENT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '标段内容',
  `BID_CLASSIFY_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '标段分类代码 参见字典表',
  `CONTRACT_RECKON_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '合同估算价(单位：元)',
  `BIDDER_QUALIFICATION` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标人资格条件',
  `PLAN_START_DATE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '计划开工日期',
  `LIMITE_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '工期',
  `BID_OPEN_STATUS` int(11) DEFAULT '0' COMMENT '开标状态 参见数据字典status',
  `EVAL_STATUS` int(11) DEFAULT '0' COMMENT '评标状态 参见数据字典status',
  `PRICE_RECORD_STATUS` int(11) DEFAULT '0' COMMENT '报价得分计算状态 参见数据字典status',
  `REG_ID` int(11) DEFAULT NULL COMMENT '行政区划主键ID',
  `DATA_FROM` int(11) DEFAULT '0' COMMENT '数据来源 参见数据字典dataSource',
  `DOCK_TENDER_DECRY_STATUS` int(11) DEFAULT '0' COMMENT '招标文件解密状态 0：失败  1：成功',
  `BID_OPEN_ONLINE` int(11) DEFAULT '0' COMMENT '是否网上开标 1：网上开标，0：现场开标',
  `REMOTE_EVALUATION` int(11) DEFAULT '0' COMMENT '是否远程异地评标 1：远程评标，0：现场评标',
  `BID_OPEN_END_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '开标结束时间',
  `EVAL_START_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标开始时间',
  `EVAL_END_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标结束时间',
  `CANCEL_STATUS` int(11) DEFAULT '0' COMMENT '流标状态 0:未做流标状态修改 1：流标 2：不流标',
  `CANCEL_REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '流标原因',
  `SCRAP_STATUS` int(11) DEFAULT '0' COMMENT '废标状态 1：废标',
  `SCRAP_REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '废标原因',
  `BID_PAUSE_STATUS` int(11) DEFAULT '0' COMMENT '项目进行状态 1:暂停',
  `UKEY_SERIAL_NUM` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT 'UKey硬件序列号',
  `EVAL_PDF_GENERATE_STATUS` int(11) DEFAULT '0' COMMENT '评标报告生成状态 1：已生成',
  `REEVAL_FLAG` int(11) DEFAULT '0' COMMENT '项目复议状态 1:复议',
  `EVAL_REVIEW_STATUS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT '0' COMMENT '评标审核状态 0：未申请 1：申请中 2：通过',
  `CHECK_STATUS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '委托人身份检查状态 1:结束',
  `PAPER_EVAL` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '是否纸质标 1:纸质标',
  `LIVE_ROOM` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '不见面直播房间号',
  `SIGN_IN_START_TIME_LEFT` int(11) DEFAULT '1440' COMMENT '设置签到开标时间前面多少分钟，默认1440分钟(一天)',
  `RESUME_STATUS` int(11) DEFAULT '0' COMMENT '复会状态 参见数据字典status',
  `RESUME_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '复会时间',
  `MANAGER_ID` int(11) DEFAULT NULL COMMENT '监管主键ID(即GOV_USER主键ID)',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否删除',
  `UPDATE_SCORE_STATUS` int(11) DEFAULT '0' COMMENT '报价得分修改状态 参见数据字典status',
  `UPDATE_SCORE_REASON` varchar(1000) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '报价得分修改原因',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='标段信息';

/*Data for the table `bid_section` */

/*Table structure for table `bid_section_relate` */

DROP TABLE IF EXISTS `bid_section_relate`;

CREATE TABLE `bid_section_relate` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `REG_ID` int(11) DEFAULT NULL COMMENT '主场行政区划主键ID',
  `AWAY_REG_ID` int(11) DEFAULT NULL COMMENT '客场行政区划主键ID',
  `HOME_OPEN_SITE` int(11) DEFAULT NULL COMMENT '主场开标室 关联Site主键ID',
  `HOME_EVAL_SITE` int(11) DEFAULT NULL COMMENT '主场评标室 关联Site主键ID',
  `AWAY_EVAL_SITE` int(11) DEFAULT NULL COMMENT '客场评标室 关联Site主键ID',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否删除',
  `EVALUATION_REPORT_ID` int(11) DEFAULT NULL COMMENT '评标报告ID 关联fdfs主键ID',
  `START_CLEAR_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '清标开始时间',
  `CLEAR_TOTAL_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '清标用时',
  `CLEAR_ANALYSIS_UID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '清标阶段的整体服务参数序列号 清标 经济标的视图参数（开标结束后，进入评标的投标人）',
  `CALC_PRICE_UID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标阶段的整体服务参数序列号 报价的视图参数(初步评审后，参与报价得分计算的投标人)',
  `PRICE_SCORE_UID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '报价得分计算服务序列号 用于获取报价计算的结果JSON（初步评审后，参与报价得分计算的投标人）',
  `RESUMPTION_REPORT_ID` int(11) DEFAULT NULL COMMENT '复会报告ID 关联fdfs主键ID',
  `PRE_RELATED_ID` int(11) DEFAULT NULL COMMENT '非预审项目，管理预审项目 bidSectionId',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='标段关联信息';

/*Data for the table `bid_section_relate` */

/*Table structure for table `bid_vote` */

DROP TABLE IF EXISTS `bid_vote`;

CREATE TABLE `bid_vote` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_APPLY_ID` int(11) DEFAULT NULL COMMENT '评标申请记录主键ID',
  `BID_EXPERT_ID` int(11) DEFAULT NULL COMMENT '专家组长主键ID',
  `COUNT` int(11) DEFAULT NULL COMMENT '得票数',
  `VOTE_PERSON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投票人(多个投标人用逗号隔开)',
  `VOTE_ROUND` int(11) DEFAULT NULL COMMENT '投票轮次',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='专家推选评标组长投票信息';

/*Data for the table `bid_vote` */

/*Table structure for table `bidder` */

DROP TABLE IF EXISTS `bidder`;

CREATE TABLE `bidder` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `BIDDER_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标人名称',
  `BIDDER_CODE_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标人类别',
  `BIDDER_ORG_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标人统一社会信用代码',
  `BID_DOC_ID` int(11) DEFAULT NULL COMMENT '投标文件附件主键ID（关联UPLOAD_FILE主键ID）',
  `BID_DOC_TYPE` int(11) DEFAULT NULL COMMENT '投标文件附件类型 0:gef或tjy  1:sgef或etjy',
  `IS_PASS_BID_OPEN` int(11) DEFAULT '1' COMMENT '是否通过开标（即能够进入评标系统）',
  `DATA_FROM` int(11) DEFAULT '0' COMMENT '数据来源  参见数据字典dataSource',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否删除',
  `BID_MANAGER` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标单位项目负责人(用于对接数据接收)',
  `LEGAL_PERSON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '法人或授权委托人(用于对接数据接收)',
  `PHONE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '联系电话(用于对接数据接收)',
  `DELETE_REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '删除原因',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='投标人信息';

/*Data for the table `bidder` */

/*Table structure for table `bidder_exception` */

DROP TABLE IF EXISTS `bidder_exception`;

CREATE TABLE `bidder_exception` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人主键ID',
  `EXCEPTION_TYPE` int(11) DEFAULT NULL COMMENT '异常类型',
  `EXCEPTION_REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '异常原因',
  `OPERATOR_ID` int(11) DEFAULT NULL COMMENT '操作人id',
  `OPERATOR_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '操作人名称',
  `OPERAT_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '操作时间',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='投标人异常信息';

/*Data for the table `bidder_exception` */

/*Table structure for table `bidder_file_info` */

DROP TABLE IF EXISTS `bidder_file_info`;

CREATE TABLE `bidder_file_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人息主键ID',
  `RECEIPT_ID` int(11) DEFAULT NULL COMMENT '投标文件核验成功后上传的回执单ID,对应fdfs表主键',
  `CERT_ID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '加密锁的ID',
  `CIPHER` text COLLATE utf8_general_mysql500_ci COMMENT '加密因子',
  `PCID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标文件硬件码',
  `DISKID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标文件硬盘码',
  `MACID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标文件网卡MAC',
  `GEF_ID` int(11) DEFAULT NULL COMMENT '投标人上传的GEF源文件ID,对应uploadFile主键',
  `GEF_HASH` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT 'GEF投标文件Hash值',
  `XML_UID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标工程量清单文件标识码',
  `SGEF_ID` int(11) DEFAULT NULL COMMENT '投标人上传的SGEF源文件ID,对应uploadFile主键',
  `SGEF_HASH` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT 'SGEF加密投标文件Hash值',
  `CZR_ID` int(11) DEFAULT NULL COMMENT '存根文件上传ID,对应uploadFile主键',
  `CZR_HASH` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '存根文件Hash值',
  `BUSINESS_STATUS` int(11) DEFAULT '0' COMMENT '商务文件 上传状态 （0：未上传 1：成功 2：失败）/纸质标代表 纸质标投标文件PDF',
  `TECHNICAL_STATUS` int(11) DEFAULT '0' COMMENT '技术文件 上传状态 （0：未上传 1：成功 2：失败）',
  `QUALIFICATIONS_STATUS` int(11) DEFAULT '0' COMMENT '资格证明 上传状态 （0：未上传 1：成功 2：失败）',
  `CHECKLIST_STATUS` int(11) DEFAULT '0' COMMENT '工程量清单PDF（施工类特有） 上传状态 （0：未上传 1：成功 2：失败）',
  `CHECKLIST_XML_STATUS` int(11) DEFAULT '0' COMMENT '工程量清单XML（施工类特有） 上传状态 （0：未上传 1：成功 2：失败）',
  `OTHER_STATUS` int(11) DEFAULT '0' COMMENT '其他文件 上传状态 （0：未上传 1：成功 2：失败）',
  `ALL_FILE_STATUS` int(11) DEFAULT '0' COMMENT '所有投标文件 上传状态 （0：未上传 1：成功 2：失败）',
  `FILE_UNZIP_PATH` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标文件解密路径',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='投标文件信息';

/*Data for the table `bidder_file_info` */

/*Table structure for table `bidder_open_info` */

DROP TABLE IF EXISTS `bidder_open_info`;

CREATE TABLE `bidder_open_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人息主键ID',
  `GEF_HASH` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标文件Hash值',
  `UPFILE_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '文件上传时间',
  `CLIENT_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '法人或授权委托人姓名',
  `CLIENT_IDCARD` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '法人或授权委托人身份证号',
  `CLIENT_PHONE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '电话号码',
  `TICKET_NO` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '身份验证订单号',
  `QR_PIC_URL` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '二维码地址',
  `SQR_PIC_URL` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '头像地址',
  `AUTHENTICATION` int(11) DEFAULT NULL COMMENT '验证结果 1:验证通过，其他均为失败验证结果',
  `AUTH_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '验证时间',
  `REQUEST_ID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '验证ID',
  `SQWTS_FILE_ID` int(11) DEFAULT NULL COMMENT '授权委托书上传id',
  `SQWTS_FILE_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '授权委托书名称',
  `URGENT_SIGIN` int(11) DEFAULT NULL COMMENT '紧急签到',
  `SQWTS_PNG_FILE_ID` int(11) DEFAULT NULL COMMENT '法人或授权委托书人照片上传id',
  `SIGNIN_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '签到时间',
  `TENDER_CASN` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT 'CA序列号(招标人CA)',
  `BID_CASN` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT 'CA序列号(投标人CA)',
  `RATE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '费率',
  `QUALITY` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '工程质量(施工)',
  `NOT_CHECKIN` int(11) DEFAULT NULL COMMENT '未签到 1:迟到 2:弃标 9:其它',
  `NOT_CHECKIN_REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '未签到原因(notCheckin=9时存在)',
  `DECRYPT_STATUS` int(11) DEFAULT '0' COMMENT '投标文件解密状态(0:未解密;1:已解密;2:解密失败)',
  `TENDER_DECRYPT_STATUS` int(11) DEFAULT '0' COMMENT '招标解密状态(0:未解密;1:已解密;2:解密失败)',
  `MARGIN_PAY_STATUS` int(11) DEFAULT '0' COMMENT '投标保证金缴纳状态(1:已缴纳;0:未缴纳)',
  `BIDDER_IDENTITY_STATUS` int(11) DEFAULT NULL COMMENT '投标人身份检查 1:符合 0:不符合',
  `SEAL_STATUS` int(11) DEFAULT NULL COMMENT '密封性检查 1:密封 0:破损',
  `TENDER_REJECTION` int(11) DEFAULT NULL COMMENT '标书拒绝 1:拒绝',
  `TENDER_REJECTION_REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '标书拒绝理由',
  `PRICE_DETERMINE` int(11) DEFAULT NULL COMMENT '投标报价确定 1:已确定',
  `DOC_DETERMINE` int(11) DEFAULT NULL COMMENT '开标一览表确定 1:已确定',
  `RESUME_DETERMINE` int(11) DEFAULT '0' COMMENT '复会确定 1:无异议 2：有异议 0: 未做选择',
  `IS_CLIENT_CHECK` int(11) DEFAULT '0' COMMENT '是否完成身份检查，1：是：0否',
  `DOC_DETERMINE_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '开标一览表确定时间',
  `BID_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '报价 单位：元',
  `TIME_LIMIT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '工期',
  `DECRYPT_START_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '解密时间开始时间',
  `DECRYPT_END_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '解密时间结束时间',
  `KICK_STATUS` int(11) DEFAULT '0' COMMENT '是否提出  0:默认  1: 剔除',
  `SGEF_HASH` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '加密投标文件Hash值',
  `DISSENT_STATUS` int(11) DEFAULT '0' COMMENT '质询异议状态 1:无异议 2：有异议 0: 未做选择',
  `PRICE_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '报价类型（总价、上浮、下浮）',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='投标人开标信息';

/*Data for the table `bidder_open_info` */

/*Table structure for table `bidder_quantity` */

DROP TABLE IF EXISTS `bidder_quantity`;

CREATE TABLE `bidder_quantity` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `TENDER_XML_UID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标工程量清单文件标示码',
  `BID_XML_UID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标工程量清单文件标示码',
  `STRUCTURE_ANALYSIS_FLAG` int(11) DEFAULT NULL COMMENT '错漏项分析服务标示: 0:未请求服务 1:已请求服务',
  `STRUCTURE_ANALYSIS_SERIAL_NUMBER` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '错漏项分析服务序列号',
  `STRUCTURE_ANALYSIS_STATE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '错漏项分析服务状态',
  `PRICE_ANALYSIS_FLAG` int(11) DEFAULT NULL COMMENT '零负报价分析服务标示: 0:未请求服务 1:已请求服务',
  `PRICE_ANALYSIS_SERIAL_NUMBER` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '零负报价分析服务序列号',
  `PRICE_ANALYSIS_STATE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '零负报价分析服务状态',
  `ARITHMETIC_ANALYSIS_FLAG` int(11) DEFAULT NULL COMMENT '算术性分析服务标示: 0:未请求服务 1:已请求服务',
  `ARITHMETIC_ANALYSIS_SERIAL_NUMBER` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '算术性分析服务序列号',
  `ARITHMETIC_ANALYSIS_STATE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '算术性分析服务状态',
  `RULE_ANALYSIS_FLAG` int(11) DEFAULT NULL COMMENT '取费基础分析服务标示: 0:未请求服务 1:已请求服务',
  `RULE_ANALYSIS_SERIAL_NUMBER` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '取费基础分析服务序列号',
  `RULE_ANALYSIS_STATE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '取费基础分析服务状态',
  `STRUCTURE_ANALYSIS_PROCESS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '错漏项分析服务进度',
  `PRICE_ANALYSIS_PROCESS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '零负报价分析服务进度',
  `ARITHMETIC_ANALYSIS_PROCESS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '算术性分析服务进度',
  `RULE_ANALYSIS_PROCESS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '取费基础分析服务进度',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='投标人工程量清单服务信息';

/*Data for the table `bidder_quantity` */

/*Table structure for table `bidder_quantity_score` */

DROP TABLE IF EXISTS `bidder_quantity_score`;

CREATE TABLE `bidder_quantity_score` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人id',
  `BIDDER_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标人名称',
  `SCORE_A` decimal(14,2) DEFAULT NULL COMMENT '分部分项工程量清单报价A 得分',
  `SCORE_B1` decimal(14,2) DEFAULT NULL COMMENT '措施项目清单报价B1 得分',
  `SCORE_B2` decimal(14,2) DEFAULT NULL COMMENT '措施项目清单报价B2 得分',
  `SCORE_C` decimal(14,2) DEFAULT NULL COMMENT '其它项目清单C(总承包服务费) 得分',
  `SCORE_D` decimal(14,2) DEFAULT NULL COMMENT '规费清单报价D 得分',
  `SCORE_E` decimal(14,2) DEFAULT NULL COMMENT '税金清单报价E 得分',
  `SCORE_F` decimal(14,2) DEFAULT NULL COMMENT '综合单价F 得分',
  `SCORE_G` decimal(14,2) DEFAULT NULL COMMENT '主要材料设备单价G 得分',
  `TOTAL_SCORE` decimal(14,2) DEFAULT NULL COMMENT '总得分',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='投标人工程量清单报价得分结果';

/*Data for the table `bidder_quantity_score` */

/*Table structure for table `bidder_review_result` */

DROP TABLE IF EXISTS `bidder_review_result`;

CREATE TABLE `bidder_review_result` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `RESULT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审汇总的结果 0/空:不合格 1:合格',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='企业评审grade单项的评审结果表 合格制';

/*Data for the table `bidder_review_result` */

/*Table structure for table `bidder_review_result_deduct` */

DROP TABLE IF EXISTS `bidder_review_result_deduct`;

CREATE TABLE `bidder_review_result_deduct` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `DEDUCT_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审扣减的总分 0/空:不扣分 非0:扣减的分值',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='企业评审grade单项的评审结果表 扣分制';

/*Data for the table `bidder_review_result_deduct` */

/*Table structure for table `bidder_review_result_score` */

DROP TABLE IF EXISTS `bidder_review_result_score`;

CREATE TABLE `bidder_review_result_score` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `ADD_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审得分的总分',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='企业评审单项的评审结果表 打分制';

/*Data for the table `bidder_review_result_score` */

/*Table structure for table `bsn_chain_info` */

DROP TABLE IF EXISTS `bsn_chain_info`;

CREATE TABLE `bsn_chain_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人息主键ID',
  `TYPE` int(11) DEFAULT NULL COMMENT '区块数据类型（1：招标文件存根  2：开标记录表  3投标文件解密记录）',
  `TX_ID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '区块数据id',
  `STATUS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '返回数据状态码',
  `BASE_KEY` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '返回key值',
  `QUERY_ADDRESS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '数据请求地址',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='区块信息表';

/*Data for the table `bsn_chain_info` */

/*Table structure for table `calc_score_param` */

DROP TABLE IF EXISTS `calc_score_param`;

CREATE TABLE `calc_score_param` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段id',
  `EVAL_PRICE_SCORE_METHOD` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标价得分计算方式',
  `EVAL_PRICE_SCORE_METHOD_DESC` varchar(2000) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标价得分计算方式详细描述',
  `TOTAL_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '总分',
  `BASE_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标基准价',
  `OFFSET_RATE_PRECISION` int(11) DEFAULT '2' COMMENT '偏差率保留小数位数',
  `GT_BASE_PRICE_DEDUCTION` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标价每高于评标基准价一个百分点的扣分值',
  `LT_BASE_PRICE_DEDUCTION` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标价每低于评标基准价一个百分点的扣分值',
  `UPDATE_BASE_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '修改后的评标基准价',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='计算报价得分参数';

/*Data for the table `calc_score_param` */

/*Table structure for table `candidate_results` */

DROP TABLE IF EXISTS `candidate_results`;

CREATE TABLE `candidate_results` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '专家ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人ID',
  `RANKING` int(11) DEFAULT NULL COMMENT '推荐名次',
  `REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '推荐理由',
  `IS_END` int(11) DEFAULT NULL COMMENT '个人环节是否结束 1：结束',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='推荐候选人结果表';

/*Data for the table `candidate_results` */

/*Table structure for table `candidate_success` */

DROP TABLE IF EXISTS `candidate_success`;

CREATE TABLE `candidate_success` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人ID',
  `RANKING` int(11) DEFAULT NULL COMMENT '推荐名次',
  `REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '推荐理由',
  `BIDDER_FROM` int(11) DEFAULT NULL COMMENT '1：小组投票产生 2：组长推选产生',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='中标候选人表';

/*Data for the table `candidate_success` */

/*Table structure for table `clarify_answer` */

DROP TABLE IF EXISTS `clarify_answer`;

CREATE TABLE `clarify_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段主键ID',
  `BID_SECTION_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '标段编号',
  `BID_CLASSIFY_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '标段分类代码',
  `REGION_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '行政区划',
  `FILE_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '文件类型（1：招标文件  2：澄清文件）',
  `UPFILES_ID` int(11) DEFAULT NULL COMMENT '关联upfiles主键',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='澄清答疑';

/*Data for the table `clarify_answer` */

/*Table structure for table `company_user` */

DROP TABLE IF EXISTS `company_user`;

CREATE TABLE `company_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '姓名',
  `LOGIN_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录名',
  `PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录密码',
  `ALL_SPELLING` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '拼音 全拼',
  `FIRST_SPELLING` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '拼音 首字母',
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业统一信用代码',
  `ADDRESS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '单位地址',
  `LINK_MAN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人',
  `PHONE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `ENABLED` int(11) DEFAULT '1' COMMENT '启用状态',
  `LOCAL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业所在地代码',
  `LOCAL_DETAIL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业所在地',
  `PHOTO_ID` int(11) DEFAULT NULL COMMENT '头像id',
  `FILE_IDS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上传文件id',
  `LICENSE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营业执照扫描件执照',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='企业用户';

/*Data for the table `company_user` */

/*Table structure for table `dep` */

DROP TABLE IF EXISTS `dep`;

CREATE TABLE `dep` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `REG_ID` int(11) DEFAULT NULL COMMENT '所属行政区划Id',
  `DEP_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '机构名称',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '排序',
  `ALL_SPELLING` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '拼音 全拼',
  `FIRST_SPELLING` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '拼音 首字母',
  `PARENT_ID` int(11) DEFAULT NULL COMMENT '父级部门Id',
  `CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '统一社会信用代码',
  `PHONE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '部门电话',
  `REMARK` varchar(1000) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '部门职责',
  `GOV_DEP_TYPE` int(11) DEFAULT NULL COMMENT '部门类别',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='部门';

/*Data for the table `dep` */

/*Table structure for table `elect_bidder` */

DROP TABLE IF EXISTS `elect_bidder`;

CREATE TABLE `elect_bidder` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '被推选企业ID',
  `EVAL_EXPERT_ID` int(11) DEFAULT NULL COMMENT '推选专家ID',
  `REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '推荐理由',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '推选的名次：1：第一中标候选人 2：第二候选人 3：第三中标候选人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='企业推选记录';

/*Data for the table `elect_bidder` */

/*Table structure for table `elect_bidder_result` */

DROP TABLE IF EXISTS `elect_bidder_result`;

CREATE TABLE `elect_bidder_result` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '被推选企业ID',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '推选的名次：1：第一中标候选人 2：第二候选人 3：第三中标候选人',
  `REASON` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '理由',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='企业推选结果';

/*Data for the table `elect_bidder_result` */

/*Table structure for table `eval_result_epc` */

DROP TABLE IF EXISTS `eval_result_epc`;

CREATE TABLE `eval_result_epc` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '企业ID',
  `BID_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标报价',
  `BUSINESS_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '报价得分',
  `DETAILED_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '详细评审得分分数',
  `TOTAL_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '最终得分',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '排名',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='施工总承包项目简要评标结果';

/*Data for the table `eval_result_epc` */

/*Table structure for table `eval_result_jl` */

DROP TABLE IF EXISTS `eval_result_jl`;

CREATE TABLE `eval_result_jl` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '企业ID',
  `BID_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标报价',
  `BUSINESS_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '商务标得分',
  `TECHNICAL_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '技术标得分',
  `TOTAL_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '最终得分',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '排名',
  `VIOLATION_DEDUCT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '违章行为扣分',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='监理项目简要评标结果';

/*Data for the table `eval_result_jl` */

/*Table structure for table `eval_result_sg` */

DROP TABLE IF EXISTS `eval_result_sg`;

CREATE TABLE `eval_result_sg` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '企业ID',
  `BID_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标报价',
  `BUSINESS_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '报价得分',
  `ABILITY` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '施工能力扣分分数',
  `ORGANIZE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '施工组织设计扣分分数',
  `QUALITY` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '安全质量事故扣分分数',
  `MUTUAL_SECURITY` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '互保共建加分分数',
  `TOTAL_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '最终得分',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '排名',
  `BAD_RECORD` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '不良行为扣分分数',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='施工项目简要评标结果';

/*Data for the table `eval_result_sg` */

/*Table structure for table `expert_review` */

DROP TABLE IF EXISTS `expert_review`;

CREATE TABLE `expert_review` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '评审专家',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `START_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审开始时间',
  `END_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审结束时间',
  `INIT_STATUS` int(11) DEFAULT NULL COMMENT '初始化状态 参见数据字典bool',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='专家评审表';

/*Data for the table `expert_review` */

/*Table structure for table `expert_review_mutual` */

DROP TABLE IF EXISTS `expert_review_mutual`;

CREATE TABLE `expert_review_mutual` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '评审专家主键ID',
  `EXPERT_REVIEW_ID` int(11) DEFAULT NULL COMMENT '专家评审表ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `MUTUAL_RESULT` int(11) DEFAULT NULL COMMENT '对应选择的gradeItemId',
  `EVAL_RESULT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '最终加分结果',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `IDX_PKLAKXCE` (`EXPERT_ID`),
  KEY `IDX_O62EDHCH` (`EXPERT_REVIEW_ID`),
  KEY `IDX_BSVQYJ65` (`BIDDER_ID`),
  KEY `IDX_2NONIXPM` (`GRADE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='专家对企业评审单项评审结果 互保共建';

/*Data for the table `expert_review_mutual` */

/*Table structure for table `expert_review_single_item` */

DROP TABLE IF EXISTS `expert_review_single_item`;

CREATE TABLE `expert_review_single_item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '评审专家主键ID',
  `EXPERT_REVIEW_ID` int(11) DEFAULT NULL COMMENT '专家评审表ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `GRADE_ITEM_ID` int(11) DEFAULT NULL COMMENT '评审项ID',
  `GRADE_TYPE` int(11) DEFAULT NULL COMMENT '评审类别',
  `EVAL_RESULT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审结果(0:不合格, 1:合格)',
  `EVAL_COMMENTS` varchar(2000) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审意见',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `IDX_89NCAH23` (`EXPERT_ID`),
  KEY `IDX_QX6MF8D8` (`EXPERT_REVIEW_ID`),
  KEY `IDX_GGIAWQ6W` (`BIDDER_ID`),
  KEY `IDX_9HATH37T` (`GRADE_ID`),
  KEY `IDX_DDNLDTGG` (`GRADE_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='专家对企业评审单项评审结果 合格制';

/*Data for the table `expert_review_single_item` */

/*Table structure for table `expert_review_single_item_deduct` */

DROP TABLE IF EXISTS `expert_review_single_item_deduct`;

CREATE TABLE `expert_review_single_item_deduct` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '评审专家主键ID',
  `EXPERT_REVIEW_ID` int(11) DEFAULT NULL COMMENT '专家评审表ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `GRADE_ITEM_ID` int(11) DEFAULT NULL COMMENT '评审项ID',
  `EVAL_RESULT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审结果(0:扣分, 1:不扣分)',
  `DEDUCT_COMMENTS` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '扣分意见',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `IDX_Q6EJSU6A` (`EXPERT_ID`),
  KEY `IDX_GCGPC9AN` (`EXPERT_REVIEW_ID`),
  KEY `IDX_VWY7AFYC` (`BIDDER_ID`),
  KEY `IDX_OIYATAKJ` (`GRADE_ID`),
  KEY `IDX_G5VD8BO7` (`GRADE_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='专家对企业评审单项评审结果 扣分制';

/*Data for the table `expert_review_single_item_deduct` */

/*Table structure for table `expert_review_single_item_deduct_score` */

DROP TABLE IF EXISTS `expert_review_single_item_deduct_score`;

CREATE TABLE `expert_review_single_item_deduct_score` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '评审专家主键ID',
  `EXPERT_REVIEW_ID` int(11) DEFAULT NULL COMMENT '专家评审表ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `GRADE_ITEM_ID` int(11) DEFAULT NULL COMMENT '评审项ID',
  `EVAL_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审分数',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `IDX_L7QNMDNB` (`EXPERT_ID`),
  KEY `IDX_STJ5OSPW` (`EXPERT_REVIEW_ID`),
  KEY `IDX_TRSSPUHQ` (`BIDDER_ID`),
  KEY `IDX_ZMJ0LS89` (`GRADE_ID`),
  KEY `IDX_F27D6BCE` (`GRADE_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='专家对企业评审单项评审结果 扣分打分制';

/*Data for the table `expert_review_single_item_deduct_score` */

/*Table structure for table `expert_review_single_item_score` */

DROP TABLE IF EXISTS `expert_review_single_item_score`;

CREATE TABLE `expert_review_single_item_score` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `EXPERT_ID` int(11) DEFAULT NULL COMMENT '评审专家主键ID',
  `EXPERT_REVIEW_ID` int(11) DEFAULT NULL COMMENT '专家评审表ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标企业ID',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `GRADE_ITEM_ID` int(11) DEFAULT NULL COMMENT '评审项ID',
  `GRADE_TYPE` int(11) DEFAULT NULL COMMENT '评审类别',
  `EVAL_SCORE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评审分数',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `IDX_MS7HAQDG` (`EXPERT_ID`),
  KEY `IDX_223ND5YS` (`EXPERT_REVIEW_ID`),
  KEY `IDX_JUPEJLCF` (`BIDDER_ID`),
  KEY `IDX_BFYV76NE` (`GRADE_ID`),
  KEY `IDX_3UFRK5PH` (`GRADE_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='专家对企业评审单项评审结果 打分制';

/*Data for the table `expert_review_single_item_score` */

/*Table structure for table `expert_user` */

DROP TABLE IF EXISTS `expert_user`;

CREATE TABLE `expert_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `ALL_SPELLING` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '拼音 全拼',
  `FIRST_SPELLING` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '拼音 首字母',
  `REG_ID` int(11) DEFAULT NULL COMMENT '行政区划主键(用于区分远程异地评标主客场)',
  `ID_CARD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '证件号码',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `BID_APPLY_ID` int(11) DEFAULT NULL COMMENT '评标申请记录主键ID',
  `EXPERT_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '专家名称',
  `PHONE_NUMBER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '手机号码',
  `COMPANY` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '专家单位',
  `LEADER_STATUS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '候选资格状态，用于组长推选 0:失去候选资格)',
  `PWD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '随机密码',
  `CHECKIN_TIME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '专家签到时间',
  `IS_CHAIRMAN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否为评标组长',
  `CATEGORY` int(11) DEFAULT NULL COMMENT '专家类别 参见数据字典expertCategory',
  `PASS_WORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码明文',
  `PROFESSIONAL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '职称',
  `AVOID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '专家状态（包含主动回避和签到  null: 未签到 0:已确认 1:已回避 2:已签到）',
  `REASON` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '回避原因',
  `SIGNAR` int(11) DEFAULT NULL COMMENT '是否签名完成，1代表签名结束',
  `DATA_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据类型（1:正常推送专家， 2：补抽专家）',
  `UNAVAILABLE_REASON` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '未启用原因',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='评标专家';

/*Data for the table `expert_user` */

/*Table structure for table `fdfs` */

DROP TABLE IF EXISTS `fdfs`;

CREATE TABLE `fdfs` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `MARK` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件标记',
  `DFS_GROUP` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件组名',
  `DFS_ADDRESS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件地址',
  `URL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件访问地址',
  `SUFFIX` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '后缀名',
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件名',
  `BYTE_SIZE` int(11) DEFAULT NULL COMMENT '字节大小',
  `READ_SIZE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '易读性大小(自动转换单位)',
  `WAV_DURATION` int(11) DEFAULT NULL COMMENT 'wav音频文件的总时长',
  `FILE_HASH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件Hash值',
  `DELETE_FLAG` int(11) DEFAULT NULL COMMENT '删除标识 1是删除',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `UQ_BRPFM5PD` (`DFS_ADDRESS`),
  KEY `IDX_JIMZUEBH` (`DFS_GROUP`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='FastDFS文件';

/*Data for the table `fdfs` */

insert  into `fdfs`(`ID`,`INSERT_TIME`,`MARK`,`DFS_GROUP`,`DFS_ADDRESS`,`URL`,`SUFFIX`,`NAME`,`BYTE_SIZE`,`READ_SIZE`,`WAV_DURATION`,`FILE_HASH`,`DELETE_FLAG`) values
(1,'2021-01-25 18:50:17','/uploads/2021/01/25/fbc339dd-7ee6-4785-aa34-2dc88d4e4771.gef','mygroup','M03/00/A8/rBUBSWAOMeqAUrGcAEp83kZpMR8855.gef','http://61.178.200.57:18090/mygroup/M03/00/A8/rBUBSWAOMeqAUrGcAEp83kZpMR8855.gef','gef','9269aa53-0c8d-4c6c-b9b1-6888079295e1.gef',4881630,'4.66MB',NULL,'42DA087F4EE93DC3B4E253771DD31930',NULL);

/*Table structure for table `free_back_apply` */

DROP TABLE IF EXISTS `free_back_apply`;

CREATE TABLE `free_back_apply` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `STEP` int(11) DEFAULT NULL COMMENT '申请回退的评审环节 对应evalFlow code',
  `STEP_NOW` int(11) DEFAULT NULL COMMENT '回退申请前的评审环节 对应evalFlow code',
  `CHECK_STATUS` int(11) DEFAULT '0' COMMENT '审核状态(对应BackStatus 0:未审核 1:审核通过 2:审核未通过)',
  `REASON` varchar(500) DEFAULT NULL COMMENT '回退理由',
  `APPLY_TIME` varchar(255) DEFAULT NULL COMMENT '申请时间',
  `CHECK_TIME` varchar(255) DEFAULT NULL COMMENT '审核时间',
  `APPLY_USER` int(11) DEFAULT NULL COMMENT '申请人ID',
  `CHECK_USER` int(11) DEFAULT NULL COMMENT '审核人ID',
  `FREE_BACK_ANNEX_ID` int(11) DEFAULT NULL COMMENT '回退审核通过后，各个环节专家的个人评审表附件ID(对应fdfs主键ID)',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自由回退申请';

/*Data for the table `free_back_apply` */

/*Table structure for table `gov_user` */

DROP TABLE IF EXISTS `gov_user`;

CREATE TABLE `gov_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `NAME` varchar(255) DEFAULT NULL COMMENT '姓名',
  `LOGIN_NAME` varchar(255) DEFAULT NULL COMMENT '登录名',
  `PASSWORD` varchar(255) DEFAULT NULL COMMENT '登录密码',
  `ALL_SPELLING` varchar(255) DEFAULT NULL COMMENT '拼音 全拼',
  `FIRST_SPELLING` varchar(255) DEFAULT NULL COMMENT '拼音 首字母',
  `DEP_ID` int(11) DEFAULT NULL COMMENT '用户所在部门',
  `PHONE` varchar(255) DEFAULT NULL COMMENT '用户电话',
  `LEADER` int(11) DEFAULT '0' COMMENT '是否绑定最高权限（1：绑定，  0：不绑定）',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='政府用户';

/*Data for the table `gov_user` */

/*Table structure for table `grade` */

DROP TABLE IF EXISTS `grade`;

CREATE TABLE `grade` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `NAME` varchar(255) DEFAULT NULL COMMENT '评分标准名称',
  `GRADE_TYPE` varchar(255) DEFAULT NULL COMMENT '评分标准类型',
  `TYPE` int(11) DEFAULT NULL COMMENT '评审形式(0:打分;1:合格制;2扣分制)',
  `CALC_TYPE` int(11) DEFAULT NULL COMMENT '1:加分 2:扣分',
  `SCORE` varchar(255) DEFAULT NULL COMMENT '总分',
  `HAS_ITEM` int(11) DEFAULT NULL COMMENT '是否含有评分子项GradeChildItem',
  `GROUP_END` int(11) DEFAULT NULL COMMENT '小组评审是否结束 1:评审结束',
  `USER_ID` int(11) DEFAULT NULL COMMENT '创建人ID',
  `REMARK` varchar(1000) DEFAULT NULL COMMENT '备注说明',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否移除评分标准',
  `REVIEW_PROCESS` int(11) DEFAULT NULL COMMENT '评审环节(1:初步评审; 2:详细评审)',
  `REVIEW_TYPE` int(11) DEFAULT NULL COMMENT '评审类型  参考ReviewType',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分标准表';

/*Data for the table `grade` */

/*Table structure for table `grade_child_item` */

DROP TABLE IF EXISTS `grade_child_item`;

CREATE TABLE `grade_child_item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `GRADE_ITEM_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `ITEM_CONTENT` varchar(2000) DEFAULT NULL COMMENT '评分项内容',
  `SCORE` varchar(255) DEFAULT NULL COMMENT '评分项分数',
  `RULE_TRADE_CODE` varchar(255) DEFAULT NULL COMMENT '引用后台配置好的RuleTradeRule规则',
  `REMARK` varchar(1000) DEFAULT NULL COMMENT '备注说明',
  `SCORE_RANGE` varchar(255) DEFAULT NULL COMMENT '打分范围',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分项子项信息';

/*Data for the table `grade_child_item` */

/*Table structure for table `grade_item` */

DROP TABLE IF EXISTS `grade_item`;

CREATE TABLE `grade_item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `GRADE_ID` int(11) DEFAULT NULL COMMENT '评分标准ID',
  `ITEM_CONTENT` varchar(2000) DEFAULT NULL COMMENT '评分项内容',
  `SCORE` varchar(255) DEFAULT NULL COMMENT '评分项分数',
  `SCORE_TYPE` varchar(255) DEFAULT NULL COMMENT '分数类型（拓展字段：酌情打分  固定分值）',
  `SCORE_RANGE` varchar(255) DEFAULT NULL COMMENT '分数范围',
  `REMARK` varchar(1000) DEFAULT NULL COMMENT '备注说明',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分项信息';

/*Data for the table `grade_item` */

/*Table structure for table `lati_longitude` */

DROP TABLE IF EXISTS `lati_longitude`;

CREATE TABLE `lati_longitude` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BIDDER_NAME` varchar(255) DEFAULT NULL COMMENT '投标人名称',
  `BID_SECTION_ID` varchar(255) DEFAULT NULL COMMENT '标段id',
  `LATI_LONGITUDE` varchar(255) DEFAULT NULL COMMENT '经纬度',
  `TYPE` int(11) DEFAULT NULL COMMENT '身份：0：投标人，1：游客',
  `REMAKE` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投标人/游客经纬度';

/*Data for the table `lati_longitude` */

/*Table structure for table `like_count` */

DROP TABLE IF EXISTS `like_count`;

CREATE TABLE `like_count` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段id',
  `COUNT` int(11) DEFAULT NULL COMMENT '点赞数',
  `REMAKE` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞统计';

/*Data for the table `like_count` */

/*Table structure for table `line_msg` */

DROP TABLE IF EXISTS `line_msg`;

CREATE TABLE `line_msg` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人主键ID',
  `SEND_NAME` varchar(255) DEFAULT NULL COMMENT '发送人名称',
  `ROLE_TYPE` int(11) DEFAULT NULL COMMENT '消息类型 0：系统消息 1：投标人消息 2：代理机构消息',
  `QUESTION` int(11) DEFAULT NULL COMMENT '是否质疑信息 1：质疑信息',
  `MESSAGE` varchar(1000) DEFAULT NULL COMMENT '内容信息',
  `BACK_NAME` varchar(255) DEFAULT NULL COMMENT '回复人',
  `BACK_MSG` varchar(1000) DEFAULT NULL COMMENT '回复消息',
  `RESUME` int(11) DEFAULT NULL COMMENT '对复会是否有异议 1：有异议',
  `OBJECTION_FILE_ID` int(11) DEFAULT NULL COMMENT '有异议时，上传的附件id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网上开标消息';

/*Data for the table `line_msg` */

/*Table structure for table `line_msg_read` */

DROP TABLE IF EXISTS `line_msg_read`;

CREATE TABLE `line_msg_read` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `LINE_MSG_ID` int(11) DEFAULT NULL COMMENT '网上开标消息主键ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID（投标人主键Id或代理机构主键ID）',
  `USER_TYPE` int(11) DEFAULT NULL COMMENT '用户类型（投标人：1投标人，2代理）',
  `READ_SITUATION` int(11) DEFAULT '0' COMMENT '阅读情况(0：未读，1:已读；默认情况未读)',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网上开标消息阅读情况';

/*Data for the table `line_msg_read` */

/*Table structure for table `line_status` */

DROP TABLE IF EXISTS `line_status`;

CREATE TABLE `line_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `UPLOAD_STATUS` int(11) DEFAULT NULL COMMENT '文件上传状态 1:开始文件上传 2:文件上传结束',
  `SIGNIN_STATUS` int(11) DEFAULT NULL COMMENT '签到状态 1:开始签到 2:签到结束',
  `PUBLISH_BIDDER_STATUS` varchar(255) DEFAULT NULL COMMENT '公布投标人环节进行情况 1:结束',
  `BIDDER_CHECK_STATUS` int(11) DEFAULT NULL COMMENT '投标人身份检查状态 1:投标人身份校验 2:投标人身份校验结束',
  `DECRYPTION_STATUS` int(11) DEFAULT NULL COMMENT '解密状态 1:开始解密 2:解密结束',
  `DECRYPTION_TIME` int(11) DEFAULT NULL COMMENT '解密累计用时（秒）',
  `DECRYPTION_PERIODS` varchar(1000) DEFAULT NULL COMMENT '解密时间段json字符串',
  `QUESTION_STATUS` int(11) DEFAULT NULL COMMENT '质询状态 1：开始质询 2：结束质询',
  `RESUME_STATUS` int(11) DEFAULT NULL COMMENT '复会状态 1:开始复会 2:结束复会',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网上开标状态';

/*Data for the table `line_status` */

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `PARENT_ID` int(11) DEFAULT NULL COMMENT '父级菜单Id',
  `MENU_NAME` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `URL` varchar(255) DEFAULT NULL COMMENT '菜单地址',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '菜单排序号',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  `ICON_FONT` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

/*Data for the table `menu` */

insert  into `menu`(`ID`,`INSERT_TIME`,`PARENT_ID`,`MENU_NAME`,`URL`,`ORDER_NO`,`ENABLED`,`ICON_FONT`) values
(1,'2021-01-25 12:36:44',-1,'后台维护','',9,1,'layui-icon-username'),
(2,'2021-01-25 12:36:44',-1,'日志管理','',8,1,'layui-icon-read'),
(3,'2021-01-25 12:36:44',2,'用户日志','/log/userLogPage',1,1,NULL),
(4,'2021-01-25 12:36:44',2,'系统访问日志','/log/networkLogPage',3,1,NULL),
(5,'2021-01-25 12:36:44',2,'接口日志','/log/apiLogPage',4,1,NULL),
(6,'2021-01-25 12:36:44',2,'运行日志','/log/runtimeLogPage',5,1,NULL),
(7,'2021-01-25 12:36:44',10,'菜单管理','/menu/frameMenuPage',3,1,NULL),
(8,'2021-01-25 12:36:44',1,'行政区划','/reg/frameRegPage',2,1,NULL),
(9,'2021-01-25 12:36:44',10,'角色管理','/role/frameRolePage',1,1,NULL),
(10,'2021-01-25 12:36:44',-1,'开发人员','',1,1,'layui-icon-spread-left'),
(11,'2021-01-25 12:36:44',10,'接口授权','/apiAuth/apiAuthPage',4,1,NULL),
(12,'2021-01-25 12:36:44',-1,'用户管理',NULL,7,1,'layui-icon-user'),
(13,'2021-01-25 12:36:44',12,'政府用户','/govUser/frameUserPage',2,1,''),
(14,'2021-01-25 12:36:44',12,'人员管理','/user/frameUserPage',3,1,''),
(15,'2021-01-25 12:36:45',1,'场地管理','/site/frameSitePage',3,1,NULL),
(16,'2021-01-25 12:36:45',1,'监控管理','/monitor/frameMonitorPage',4,1,NULL),
(17,'2021-01-25 12:36:45',1,'定时器维护','/quartzJob/quartzJobPage',5,1,NULL),
(18,'2021-01-25 12:36:45',-1,'CA维护',NULL,10,1,'layui-icon-password'),
(19,'2021-01-25 12:36:45',18,'离线审核权限写入','/certInfo/writeReviewAuthority',1,1,NULL);

/*Table structure for table `monitor` */

DROP TABLE IF EXISTS `monitor`;

CREATE TABLE `monitor` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `REG_ID` int(11) DEFAULT NULL COMMENT '行政区划主键',
  `REG_NAME` varchar(255) DEFAULT NULL COMMENT '行政区划名称',
  `REG_CODE` varchar(255) DEFAULT NULL COMMENT '行政区划代码',
  `IP` varchar(255) DEFAULT NULL COMMENT '大华摄像头IP',
  `PORT` varchar(255) DEFAULT NULL COMMENT '大华摄像头端口',
  `USER_NAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `PWD` varchar(255) DEFAULT NULL COMMENT '密码',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监控信息';

/*Data for the table `monitor` */

/*Table structure for table `online_info` */

DROP TABLE IF EXISTS `online_info`;

CREATE TABLE `online_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `SESSION_ID` varchar(255) DEFAULT NULL COMMENT '登录ID',
  `NAME` varchar(255) DEFAULT NULL COMMENT '名字',
  `USER_ID` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `MODULE` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `IP_INFO` varchar(255) DEFAULT NULL COMMENT '登录IP',
  `ADDRESS_INFO` varchar(255) DEFAULT NULL COMMENT '地址信息',
  PRIMARY KEY (`ID`),
  KEY `IDX_MAENTFMX` (`MODULE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在线用户信息表';

/*Data for the table `online_info` */

/*Table structure for table `project` */

DROP TABLE IF EXISTS `project`;

CREATE TABLE `project` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `PROJECT_CODE` varchar(255) DEFAULT NULL COMMENT '项目编号',
  `PROJECT_NAME` varchar(300) DEFAULT NULL COMMENT '项目名称',
  `REGION_CODE` varchar(255) DEFAULT NULL COMMENT '项目所在行政区域代码',
  `INVEST_PROJECT_CODE` varchar(255) DEFAULT NULL COMMENT '投资项目统一代码',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '项目地址',
  `LEGAL_PERSON` varchar(255) DEFAULT NULL COMMENT '项目法人',
  `INDUSTRIES_TYPE` varchar(255) DEFAULT NULL COMMENT '项目行业分类',
  `SOURCE_FUND` varchar(255) DEFAULT NULL COMMENT '资金来源',
  `CONTRIBUTION_SCALE` varchar(255) DEFAULT NULL COMMENT '出资比例',
  `PROJECT_SCALE` varchar(255) DEFAULT NULL COMMENT '项目规模',
  `CONTACT_PERSON` varchar(255) DEFAULT NULL COMMENT '联系人',
  `CONTACT_INFORMATION` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `APPROVAL_NAME` varchar(255) DEFAULT NULL COMMENT '项目审批文件名称',
  `APPROVAL_NUMBER` varchar(255) DEFAULT NULL COMMENT '项目审批文号',
  `APPROVAL_AUTHORITY` varchar(255) DEFAULT NULL COMMENT '项目审批单位',
  `CREATE_TIME` varchar(255) DEFAULT NULL COMMENT '项目建立时间',
  `REG_ID` int(11) DEFAULT NULL COMMENT '行政区划主键ID',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目信息';

/*Data for the table `project` */

/*Table structure for table `project_pause` */

DROP TABLE IF EXISTS `project_pause`;

CREATE TABLE `project_pause` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `OPERATOR_ID` int(11) DEFAULT NULL COMMENT '操作人员ID',
  `OPERATOR_NAME` varchar(255) DEFAULT NULL COMMENT '操作人员名称',
  `PAUSE_REASON` varchar(255) DEFAULT NULL COMMENT '暂停原因',
  `PAUSE_STATUS` int(11) DEFAULT '1' COMMENT '暂停状态(0:暂停 1:未暂停)',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目暂停';

/*Data for the table `project_pause` */

/*Table structure for table `quartz_job` */

DROP TABLE IF EXISTS `quartz_job`;

CREATE TABLE `quartz_job` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `NAME` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `JOB_GROUP` varchar(255) DEFAULT NULL COMMENT '任务组',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `JOB_CLASS_NAME` varchar(255) DEFAULT NULL COMMENT '任务执行类',
  `CRON_EXPRESSION` varchar(255) DEFAULT NULL COMMENT '任务触发规则',
  `TRIGGER_STATE` varchar(255) DEFAULT NULL COMMENT '触发器状态',
  `OLD_NAME` varchar(255) DEFAULT NULL COMMENT '旧的任务名称',
  `OLD_JOB_GROUP` varchar(255) DEFAULT NULL COMMENT '旧的任务组',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`),
  KEY `IDX_BXNRSGY4` (`JOB_GROUP`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Quartz定义任务表';

/*Data for the table `quartz_job` */

/*Table structure for table `quote_score_result` */

DROP TABLE IF EXISTS `quote_score_result`;

CREATE TABLE `quote_score_result` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人id',
  `BID_PRICE` varchar(255) DEFAULT NULL COMMENT '投标人评标价',
  `BID_PRICE_OFFSET` varchar(255) DEFAULT NULL COMMENT '对应报价偏差率',
  `BID_PRICE_SCORE` varchar(255) DEFAULT NULL COMMENT '对应报价得分',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投标人报价得分结果';

/*Data for the table `quote_score_result` */

/*Table structure for table `quote_score_result_appendix` */

DROP TABLE IF EXISTS `quote_score_result_appendix`;

CREATE TABLE `quote_score_result_appendix` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `BIDDER_ID` int(11) DEFAULT NULL COMMENT '投标人id',
  `BID_PRICE` varchar(255) DEFAULT NULL COMMENT '投标人评标价',
  `BID_PRICE_OFFSET` varchar(255) DEFAULT NULL COMMENT '对应报价偏差率',
  `BID_PRICE_SCORE` varchar(255) DEFAULT NULL COMMENT '对应报价得分',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投标人报价得分结果附录';

/*Data for the table `quote_score_result_appendix` */

/*Table structure for table `reeval_log` */

DROP TABLE IF EXISTS `reeval_log`;

CREATE TABLE `reeval_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段主键ID',
  `USER_ID` int(11) DEFAULT NULL COMMENT '操作人主键',
  `REASON` varchar(1000) DEFAULT NULL COMMENT '复议原因',
  `SUBMIT_TIME` varchar(255) DEFAULT NULL COMMENT '提交时间',
  `RE_EVAL_ANNEX_ID` int(11) DEFAULT NULL COMMENT '项目复议后，评标报告附件ID(对应fdfs主键ID)',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='复议日志';

/*Data for the table `reeval_log` */

/*Table structure for table `reg` */

DROP TABLE IF EXISTS `reg`;

CREATE TABLE `reg` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `PARENT_ID` int(11) DEFAULT NULL COMMENT '父级区划Id',
  `REG_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '区划名称',
  `ORDER_NO` int(11) DEFAULT NULL COMMENT '排序',
  `ALL_SPELLING` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '拼音 全拼',
  `FIRST_SPELLING` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '拼音 首字母',
  `REG_NO` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '行政区划代码',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='行政区划';

/*Data for the table `reg` */

insert  into `reg`(`ID`,`INSERT_TIME`,`PARENT_ID`,`REG_NAME`,`ORDER_NO`,`ALL_SPELLING`,`FIRST_SPELLING`,`REG_NO`,`ENABLED`) values
(1,'2021-01-12 14:33:41',-1,'甘肃省',1,'gansusheng','gss','620000',1),
(2,'2021-01-12 14:33:41',1,'兰州市',1,'lanzhoushi','lzs','620101',1),
(3,'2021-01-12 14:33:42',2,'城关区',2,'chengguanqu','cgq','620102',1),
(4,'2021-01-12 14:33:53',2,'七里河区',2,'qilihequ','qlhq','620103',1),
(5,'2021-01-12 14:34:07',1,'酒泉市',2,'jiuquanshi','jqs','620901',1),
(6,'2021-01-12 14:34:07',5,'肃州区',1,'suzhouqu','szq','620902',1),
(7,'2021-01-12 14:34:25',5,'金塔县',2,'jintaxian','jtx','620921',1),
(8,'2021-01-12 14:34:41',5,'瓜州县',3,'guazhouxian','gzx','620922',1),
(9,'2021-01-12 14:34:57',5,'肃北蒙古族自治县',4,'subeimengguzuzizhixian','sbmgzzzx','620923',1),
(10,'2021-01-12 14:35:10',5,'阿克塞哈萨克族自治县',5,'akesaihasakezuzizhixian','akshskzzzx','620924',1),
(11,'2021-01-12 14:35:26',5,'玉门市',6,'yumenshi','yms','620981',1),
(12,'2021-01-12 14:35:38',5,'敦煌市',7,'dunhuangshi','dhs','620982',1),
(13,'2021-01-12 14:35:50',2,'西固区',3,'xiguqu','xgq','620104',1),
(14,'2021-01-12 14:36:02',2,'安宁区',4,'anningqu','anq','620105',1),
(15,'2021-01-12 14:37:10',2,'红古区',5,'hongguqu','hgq','620111',1),
(16,'2021-01-12 14:37:24',2,'永登县',6,'yongdengxian','ydx','620121',1),
(17,'2021-01-12 14:37:36',2,'皋兰县',7,'gaolanxian','glx','620122',1),
(18,'2021-01-12 14:37:52',2,'榆中县',8,'yuzhongxian','yzx','620123',1),
(19,'2021-01-12 14:38:08',2,'兰州新区',9,'lanzhouxinqu','lzxq','1000665',1);

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `ROLE_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '角色名称',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态 参见数据字典enabled',
  `REMARK` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '备注说明',
  `ORDER_NO` int(11) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='角色';

/*Data for the table `role` */

insert  into `role`(`ID`,`INSERT_TIME`,`ROLE_NAME`,`ENABLED`,`REMARK`,`ORDER_NO`) values
(1,'2021-01-12 11:14:49','开发人员',1,'',0),
(2,'2021-01-12 11:14:49','运维最高权限人员',1,'运维最高权限人员相应权限',0);

/*Table structure for table `role_menu` */

DROP TABLE IF EXISTS `role_menu`;

CREATE TABLE `role_menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色id',
  `MENU_ID` int(11) DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关系表';

/*Data for the table `role_menu` */

insert  into `role_menu`(`ID`,`INSERT_TIME`,`ROLE_ID`,`MENU_ID`) values
(1,'2021-01-25 12:46:23',1,1),
(2,'2021-01-25 12:46:23',1,2),
(3,'2021-01-25 12:46:23',1,3),
(4,'2021-01-25 12:46:23',1,4),
(5,'2021-01-25 12:46:23',1,5),
(6,'2021-01-25 12:46:23',1,6),
(7,'2021-01-25 12:46:23',1,7),
(8,'2021-01-25 12:46:23',1,8),
(9,'2021-01-25 12:46:23',1,9),
(10,'2021-01-25 12:46:23',1,10),
(11,'2021-01-25 12:46:23',1,11),
(12,'2021-01-25 12:46:23',1,12),
(13,'2021-01-25 12:46:23',1,13),
(14,'2021-01-25 12:46:23',1,14),
(15,'2021-01-25 12:46:23',1,15),
(16,'2021-01-25 12:46:23',1,16),
(17,'2021-01-25 12:46:23',1,17),
(18,'2021-01-25 12:46:23',1,18),
(19,'2021-01-25 12:46:23',1,19),
(30,'2021-01-25 12:57:25',2,10),
(31,'2021-01-25 12:57:25',2,9),
(32,'2021-01-25 12:57:25',2,12),
(33,'2021-01-25 12:57:25',2,13),
(34,'2021-01-25 12:57:25',2,14),
(35,'2021-01-25 12:57:25',2,1),
(36,'2021-01-25 12:57:25',2,8),
(37,'2021-01-25 12:57:25',2,15),
(38,'2021-01-25 12:57:25',2,16),
(39,'2021-01-25 12:57:25',2,17),
(40,'2021-01-25 12:57:25',2,18),
(41,'2021-01-25 12:57:25',2,19);

/*Table structure for table `role_user` */

DROP TABLE IF EXISTS `role_user`;

CREATE TABLE `role_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色id',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='角色用户关系表';

/*Data for the table `role_user` */

insert  into `role_user`(`ID`,`INSERT_TIME`,`ROLE_ID`,`USER_ID`) values
(1,'2021-01-25 12:48:04',1,1),
(2,'2021-01-25 12:48:04',2,2),
(3,'2021-01-25 12:48:04',2,3);

/*Table structure for table `site` */

DROP TABLE IF EXISTS `site`;

CREATE TABLE `site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '场地名称',
  `TYPE` int(11) DEFAULT NULL COMMENT '场地类型',
  `REG_ID` int(11) DEFAULT NULL COMMENT '行政区划主键',
  `REMARK` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='场地信息';

/*Data for the table `site` */

/*Table structure for table `tender_doc` */

DROP TABLE IF EXISTS `tender_doc`;

CREATE TABLE `tender_doc` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段信息主键ID',
  `DOC_NO` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '文件编号',
  `BIDDER_QUALIFICATION` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标资格',
  `VALID_PERIOD` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标有效期',
  `BID_DOC_PEFER_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标文件递交方法',
  `TENDER_DOC_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标文件费金额(单位：元)',
  `MARGIN_AMOUNT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标保证金金额(单位：元)',
  `CONTROL_PRICE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '控制价(最高限价 单位：元)',
  `EVALUATION_METHOD` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '评标办法',
  `BID_DOC_REFER_END_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '投标文件递交截止时间',
  `BID_OPEN_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '开标时间',
  `BID_OPEN_PLACE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '开标地点',
  `BID_OPEN_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '开标方式',
  `QUAL_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '资格审查方式',
  `SUBMIT_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '递交时间',
  `DOC_FILE_ID` int(11) DEFAULT NULL COMMENT '招标文件ID（关联UPLOAD_FILE主键ID）',
  `GRADE_ID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '关联评标办法grade,多个评标办法间用逗号隔开',
  `FLOAT_POINT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '甘肃评标方法中的浮动点(取值范围1.5%、1.25%、1%、0.75%、0.5%、0.25%)',
  `TAXES_FACTOR` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '税金系数',
  `STRUCTURE_STATUS` int(11) DEFAULT NULL COMMENT '结构性分析启用状态 参见数据字典bool',
  `PRICE_STATUS` int(11) DEFAULT NULL COMMENT '零负报价分析启用状态 参见数据字典bool',
  `SIMILAR_ITEM_STATUS` int(11) DEFAULT NULL COMMENT '雷同项分析启用状态 参见数据字典bool',
  `FUND_BASIS_STATUS` int(11) DEFAULT NULL COMMENT '取费基础分析启用状态 参见数据字典bool',
  `MUTUAL_SECURITY_STATUS` int(11) DEFAULT NULL COMMENT '互保共建启用状态 参见数据字典bool',
  `CREDIT_STATUS` int(11) DEFAULT NULL COMMENT '信用报告评审启用状态 参见数据字典bool',
  `EXPERT_COUNT` int(11) DEFAULT NULL COMMENT '专家人数',
  `REPRESENTATIVE_COUNT` int(11) DEFAULT NULL COMMENT '业主代表人数',
  `OPEN_BID_RECORD_DES` varchar(2000) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '开标记录表备注',
  `VERSION` int(11) NOT NULL DEFAULT '1' COMMENT '招标文件版本',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否删除',
  `CERT_NUM` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标CA序列号',
  `XML_UID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '工程量清单文件标识码',
  `OTHER_GRADE_ID` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '其他评审ID，关联评标办法grade,多个评标办法间用逗号隔开',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='招标文件信息';

/*Data for the table `tender_doc` */

/*Table structure for table `tender_project` */

DROP TABLE IF EXISTS `tender_project`;

CREATE TABLE `tender_project` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `PROJECT_ID` int(11) DEFAULT NULL COMMENT '项目主键ID',
  `TENDER_PROJECT_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标项目编号',
  `TENDER_PROJECT_NAME` varchar(300) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标项目名称',
  `TENDER_PROJECT_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标项目类型',
  `REGION_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标项目所在行政区域代码',
  `REG_ID` int(11) DEFAULT NULL COMMENT '行政区划主键ID',
  `TENDER_CONTENT` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标内容与范围及招标方案说明',
  `OWNER_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '项目业主名称',
  `TENDERER_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标人名称',
  `TENDERER_CODE_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标人类别',
  `TENDERER_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标人代码',
  `TENDERER_ROLE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标人角色',
  `TENDER_AGENCY_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标代理机构名称',
  `TENDER_AGENCY_PHONE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标代理机构电话',
  `TENDER_AGENCY_CODE_TYPE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标代理机构类别',
  `TENDER_AGENCY_ROLE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标代理机构角色',
  `TENDER_MODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标方式',
  `TENDER_ORGANIZE_FORM` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标组织形式',
  `CREATE_TIME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标项目建立时间',
  `SUPERVISE_DEPT_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '监督部门名称',
  `SUPERVISE_DEPT_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '监督部门代码',
  `APPROVE_DEPT_NAME` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '审核部门名称',
  `APPROVE_DEPT_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '审核部门代码',
  `USER_ID` int(11) DEFAULT NULL COMMENT '招标项目创建人(单点用户ID)',
  `TENDER_AGENCY_CODE` varchar(255) COLLATE utf8_general_mysql500_ci DEFAULT NULL COMMENT '招标代理机构统一社会信用代码',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci ROW_FORMAT=DYNAMIC COMMENT='招标项目信息';

/*Data for the table `tender_project` */

/*Table structure for table `upload_file` */

DROP TABLE IF EXISTS `upload_file`;

CREATE TABLE `upload_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件名',
  `SUFFIX` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '后缀名',
  `PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路径',
  `BYTE_SIZE` int(11) DEFAULT NULL COMMENT '字节大小',
  `READ_SIZE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '易读性大小(自动转换单位)',
  `CALL_COUNT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件被读取次数',
  `FILE_UID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件UUID',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='附件';

/*Data for the table `upload_file` */

/*Table structure for table `user_cert` */

DROP TABLE IF EXISTS `user_cert`;

CREATE TABLE `user_cert` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID',
  `UKEY_NUM` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '锁号',
  `USER_TYPE` int(11) DEFAULT NULL COMMENT '用户类型',
  `CA_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '锁的名称',
  `LOGIN_FLAG` int(11) DEFAULT '0' COMMENT '是否允许登录',
  `DELETE_FLAG` int(11) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户和CA关系表';

/*Data for the table `user_cert` */

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `LOGIN_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录名',
  `PASSWORD` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录密码',
  `REG_ID` int(11) DEFAULT NULL COMMENT '用户所在地区',
  `ALL_SPELLING` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼音 全拼',
  `FIRST_SPELLING` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼音 首字母',
  `ENABLED` int(11) DEFAULT NULL COMMENT '启用状态  参见数据字典enabled',
  `PHONE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户电话',
  `USER_FILE_ID` int(11) DEFAULT NULL COMMENT '用户的头像',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

/*Data for the table `user_info` */

insert  into `user_info`(`ID`,`INSERT_TIME`,`NAME`,`LOGIN_NAME`,`PASSWORD`,`REG_ID`,`ALL_SPELLING`,`FIRST_SPELLING`,`ENABLED`,`PHONE`,`USER_FILE_ID`) values
(1,'2021-01-25 12:49:19','admin','admin','0439a8617e300f936d55c0979a387bb37e9029405ad9a2bfed0c63dfd8051976d248fb1bac1d5b591c08179c11a241c3ec855896e6d200531091b994df5425c526421abbf07198662c56d5cefd30c25411a8f4318952dbfb1fe97bfca414313d2f114cbc88da1d064a',1,'admin','admin',1,'123456789',24),
(2,'2021-01-25 12:49:19','兰州测试','lzcs','0477fc2fdefd790e6c351760d4aadf35e16b67056c63294f6effa618625c1f7734f885c9c46bf35c2f857c94bf2885d5077b871ac84550fbddd0bb0000ab155b6439d3a6bfee6e4d9d67bf7f61f66ce9e1b05cd2d6055dd4a1c2cbdee59a010bfe2a3a51d95ad9021f758fa667',2,'lanzhouceshi','lzcs',1,'13322054102',NULL),
(3,'2021-01-25 12:49:19','李德洲','ldz','0466e592739dbabd0d9a4d8d8c78b5829b4d6dfea78443e27c3f2852d2ae8e89f721cb356c29c939042a9842b2531013877fbea5953e0b952cb561039eb67124d322e5c4e4b6d4c55d9f6c7fd64b6a846104297d1809590c34d6a8e68f268510f04fcf7bf892b08c21de',5,'lidezhou','ldz',1,'18054121682',NULL);

/*Table structure for table `user_reg` */

DROP TABLE IF EXISTS `user_reg`;

CREATE TABLE `user_reg` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户id',
  `REG_ID` int(11) DEFAULT NULL COMMENT '区划id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户区划关联表';

/*Data for the table `user_reg` */

/*Table structure for table `user_style` */

DROP TABLE IF EXISTS `user_style`;

CREATE TABLE `user_style` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `THEME` varchar(255) DEFAULT NULL COMMENT '界面风格',
  `NOTE` text COMMENT '便签',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户界面';

/*Data for the table `user_style` */

/*Table structure for table `wining_bid_file` */

DROP TABLE IF EXISTS `wining_bid_file`;

CREATE TABLE `wining_bid_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BID_SECTION_ID` int(11) DEFAULT NULL COMMENT '标段ID',
  `WINING_BID_FILE_ID` int(11) DEFAULT NULL COMMENT '中标通知书pdf对应的uploadFile主键id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='中标通知书信息';

/*Data for the table `wining_bid_file` */

/*Table structure for table `wordbook` */

DROP TABLE IF EXISTS `wordbook`;

CREATE TABLE `wordbook` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `BOOK_TOP_KEY` varchar(255) DEFAULT NULL COMMENT 'TOP KEY',
  `BOOK_PARENT_KEY` varchar(255) DEFAULT NULL COMMENT 'PARENT KEY',
  `BOOK_KEY` varchar(255) DEFAULT NULL COMMENT 'KEY',
  `BOOK_VALUE` varchar(255) DEFAULT NULL COMMENT 'VALUE',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

/*Data for the table `wordbook` */

insert  into `wordbook`(`ID`,`INSERT_TIME`,`BOOK_TOP_KEY`,`BOOK_PARENT_KEY`,`BOOK_KEY`,`BOOK_VALUE`) values
(1,'2021-01-25 17:53:00','bool','bool','0','否'),
(2,'2021-01-25 17:53:00','bool','bool','1','是'),
(3,'2021-01-25 17:53:00','enabled','enabled','0','禁用'),
(4,'2021-01-25 17:53:00','enabled','enabled','1','启用'),
(5,'2021-01-25 17:53:00','dmlType','dmlType','1','新增'),
(6,'2021-01-25 17:53:00','dmlType','dmlType','2','删除'),
(7,'2021-01-25 17:53:00','dmlType','dmlType','3','更新'),
(8,'2021-01-25 17:53:01','dmlType','dmlType','4','查询'),
(9,'2021-01-25 17:53:01','dmlType','dmlType','5','未知'),
(10,'2021-01-25 17:53:01','apiName','apiName','sso','单点登录接口'),
(11,'2021-01-25 17:53:01','apiName','apiName','clientUpdate','客户端更新包获取'),
(12,'2021-01-25 17:53:01','logLevel','logLevel','DEBUG','DEBUG'),
(13,'2021-01-25 17:53:01','logLevel','logLevel','INFO','INFO'),
(14,'2021-01-25 17:53:01','logLevel','logLevel','WARN','WARN'),
(15,'2021-01-25 17:53:01','userType','userType','0','管理员'),
(16,'2021-01-25 17:53:01','userType','userType','1','主管部门'),
(17,'2021-01-25 17:53:02','userType','userType','2','代理机构'),
(18,'2021-01-25 17:53:02','userType','userType','3','招标人'),
(19,'2021-01-25 17:53:02','siteType','siteType','1','开标室'),
(20,'2021-01-25 17:53:02','siteType','siteType','2','评标室'),
(21,'2021-01-25 17:53:02','status','status','0','未开始'),
(22,'2021-01-25 17:53:02','status','status','1','进行中'),
(23,'2021-01-25 17:53:02','status','status','2','结束'),
(24,'2021-01-25 17:53:02','bidClassifyCode','bidClassifyCode','A03','勘察'),
(25,'2021-01-25 17:53:02','bidClassifyCode','bidClassifyCode','A04','设计'),
(26,'2021-01-25 17:53:02','bidClassifyCode','bidClassifyCode','A05','监理'),
(27,'2021-01-25 17:53:02','bidClassifyCode','bidClassifyCode','A08','施工'),
(28,'2021-01-25 17:53:02','bidClassifyCode','bidClassifyCode','A10','资格预审'),
(29,'2021-01-25 17:53:02','bidClassifyCode','bidClassifyCode','A11','电梯安装与采购'),
(30,'2021-01-25 17:53:02','bidClassifyCode','bidClassifyCode','A12','施工总承包'),
(31,'2021-01-25 17:53:02','dataSource','dataSource','0','本系统数据'),
(32,'2021-01-25 17:53:02','dataSource','dataSource','1','对接数据'),
(33,'2021-01-25 17:53:03','expertCategory','expertCategory','1','经济标专家'),
(34,'2021-01-25 17:53:03','expertCategory','expertCategory','2','技术标专家'),
(35,'2021-01-25 17:53:03','expertCategory','expertCategory','3','业主代表'),
(36,'2021-01-25 17:53:03','authType','authType','1','信id'),
(37,'2021-01-25 17:53:03','authType','authType','2','支付宝'),
(38,'2021-01-25 17:53:03','govDepType','govDepType','1','招标办'),
(39,'2021-01-25 17:53:03','govDepType','govDepType','2','交易中心（专家录入）'),
(40,'2021-01-25 17:53:03','govDepType','govDepType','3','交易中心（工程处）');

/* Trigger structure for table `api_auth` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_API_AUTH` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_API_AUTH` BEFORE INSERT ON `api_auth` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `back_push_status` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BACK_PUSH_STATUS` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BACK_PUSH_STATUS` BEFORE INSERT ON `back_push_status` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bid_apply` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BID_APPLY` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BID_APPLY` BEFORE INSERT ON `bid_apply` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bid_section` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BID_SECTION` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BID_SECTION` BEFORE INSERT ON `bid_section` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bid_section_relate` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BID_SECTION_RELATE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BID_SECTION_RELATE` BEFORE INSERT ON `bid_section_relate` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bid_vote` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BID_VOTE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BID_VOTE` BEFORE INSERT ON `bid_vote` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER` BEFORE INSERT ON `bidder` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_exception` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_EXCEPTION` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_EXCEPTION` BEFORE INSERT ON `bidder_exception` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_file_info` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_FILE_INFO` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_FILE_INFO` BEFORE INSERT ON `bidder_file_info` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_open_info` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_OPEN_INFO` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_OPEN_INFO` BEFORE INSERT ON `bidder_open_info` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_quantity` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_QUANTITY` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_QUANTITY` BEFORE INSERT ON `bidder_quantity` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_quantity_score` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_QUANTITY_SCORE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_QUANTITY_SCORE` BEFORE INSERT ON `bidder_quantity_score` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_review_result` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_REVIEW_RESULT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_REVIEW_RESULT` BEFORE INSERT ON `bidder_review_result` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_review_result_deduct` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_REVIEW_RESULT_DEDUCT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_REVIEW_RESULT_DEDUCT` BEFORE INSERT ON `bidder_review_result_deduct` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bidder_review_result_score` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BIDDER_REVIEW_RESULT_SCORE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BIDDER_REVIEW_RESULT_SCORE` BEFORE INSERT ON `bidder_review_result_score` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `bsn_chain_info` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_BSN_CHAIN_INFO` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_BSN_CHAIN_INFO` BEFORE INSERT ON `bsn_chain_info` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `calc_score_param` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_CALC_SCORE_PARAM` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_CALC_SCORE_PARAM` BEFORE INSERT ON `calc_score_param` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `candidate_results` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_CANDIDATE_RESULTS` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_CANDIDATE_RESULTS` BEFORE INSERT ON `candidate_results` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `candidate_success` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_CANDIDATE_SUCCESS` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_CANDIDATE_SUCCESS` BEFORE INSERT ON `candidate_success` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `clarify_answer` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_CLARIFY_ANSWER` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_CLARIFY_ANSWER` BEFORE INSERT ON `clarify_answer` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `company_user` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_COMPANY_USER` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_COMPANY_USER` BEFORE INSERT ON `company_user` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `dep` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_DEP` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_DEP` BEFORE INSERT ON `dep` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `elect_bidder` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_ELECT_BIDDER` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_ELECT_BIDDER` BEFORE INSERT ON `elect_bidder` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `elect_bidder_result` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_ELECT_BIDDER_RESULT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_ELECT_BIDDER_RESULT` BEFORE INSERT ON `elect_bidder_result` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `eval_result_epc` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EVAL_RESULT_EPC` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EVAL_RESULT_EPC` BEFORE INSERT ON `eval_result_epc` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `eval_result_jl` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EVAL_RESULT_JL` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EVAL_RESULT_JL` BEFORE INSERT ON `eval_result_jl` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `eval_result_sg` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EVAL_RESULT_SG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EVAL_RESULT_SG` BEFORE INSERT ON `eval_result_sg` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `expert_review` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EXPERT_REVIEW` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EXPERT_REVIEW` BEFORE INSERT ON `expert_review` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `expert_review_mutual` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EXPERT_REVIEW_MUTUAL` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EXPERT_REVIEW_MUTUAL` BEFORE INSERT ON `expert_review_mutual` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `expert_review_single_item` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EXPERT_REVIEW_SINGLE_ITEM` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EXPERT_REVIEW_SINGLE_ITEM` BEFORE INSERT ON `expert_review_single_item` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `expert_review_single_item_deduct` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EXPERT_REVIEW_SINGLE_ITEM_DEDUCT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EXPERT_REVIEW_SINGLE_ITEM_DEDUCT` BEFORE INSERT ON `expert_review_single_item_deduct` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `expert_review_single_item_deduct_score` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EXPERT_REVIEW_SINGLE_ITEM_DEDUCT_SCORE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EXPERT_REVIEW_SINGLE_ITEM_DEDUCT_SCORE` BEFORE INSERT ON `expert_review_single_item_deduct_score` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `expert_review_single_item_score` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EXPERT_REVIEW_SINGLE_ITEM_SCORE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EXPERT_REVIEW_SINGLE_ITEM_SCORE` BEFORE INSERT ON `expert_review_single_item_score` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `expert_user` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_EXPERT_USER` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_EXPERT_USER` BEFORE INSERT ON `expert_user` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `fdfs` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_FDFS` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_FDFS` BEFORE INSERT ON `fdfs` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `free_back_apply` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_FREE_BACK_APPLY` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_FREE_BACK_APPLY` BEFORE INSERT ON `free_back_apply` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `gov_user` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_GOV_USER` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_GOV_USER` BEFORE INSERT ON `gov_user` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `grade` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_GRADE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_GRADE` BEFORE INSERT ON `grade` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `grade_child_item` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_GRADE_CHILD_ITEM` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_GRADE_CHILD_ITEM` BEFORE INSERT ON `grade_child_item` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `grade_item` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_GRADE_ITEM` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_GRADE_ITEM` BEFORE INSERT ON `grade_item` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `lati_longitude` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_LATI_LONGITUDE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_LATI_LONGITUDE` BEFORE INSERT ON `lati_longitude` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `like_count` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_LIKE_COUNT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_LIKE_COUNT` BEFORE INSERT ON `like_count` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `line_msg` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_LINE_MSG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_LINE_MSG` BEFORE INSERT ON `line_msg` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `line_msg_read` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_LINE_MSG_READ` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_LINE_MSG_READ` BEFORE INSERT ON `line_msg_read` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `line_status` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_LINE_STATUS` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_LINE_STATUS` BEFORE INSERT ON `line_status` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `menu` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_MENU` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_MENU` BEFORE INSERT ON `menu` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `monitor` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_MONITOR` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_MONITOR` BEFORE INSERT ON `monitor` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `online_info` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_ONLINE_INFO` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_ONLINE_INFO` BEFORE INSERT ON `online_info` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `project` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_PROJECT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_PROJECT` BEFORE INSERT ON `project` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `project_pause` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_PROJECT_PAUSE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_PROJECT_PAUSE` BEFORE INSERT ON `project_pause` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `quartz_job` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_QUARTZ_JOB` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_QUARTZ_JOB` BEFORE INSERT ON `quartz_job` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `quote_score_result` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_QUOTE_SCORE_RESULT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_QUOTE_SCORE_RESULT` BEFORE INSERT ON `quote_score_result` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `quote_score_result_appendix` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_QUOTE_SCORE_RESULT_APPENDIX` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_QUOTE_SCORE_RESULT_APPENDIX` BEFORE INSERT ON `quote_score_result_appendix` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `reeval_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_REEVAL_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_REEVAL_LOG` BEFORE INSERT ON `reeval_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `reg` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_REG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_REG` BEFORE INSERT ON `reg` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `role` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_ROLE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_ROLE` BEFORE INSERT ON `role` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `role_menu` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_ROLE_MENU` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_ROLE_MENU` BEFORE INSERT ON `role_menu` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `role_user` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_ROLE_USER` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_ROLE_USER` BEFORE INSERT ON `role_user` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `site` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_SITE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_SITE` BEFORE INSERT ON `site` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `tender_doc` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_TENDER_DOC` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_TENDER_DOC` BEFORE INSERT ON `tender_doc` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `tender_project` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_TENDER_PROJECT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_TENDER_PROJECT` BEFORE INSERT ON `tender_project` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `upload_file` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_UPLOAD_FILE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_UPLOAD_FILE` BEFORE INSERT ON `upload_file` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `user_cert` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_USER_CERT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_USER_CERT` BEFORE INSERT ON `user_cert` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `user_info` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_USER_INFO` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_USER_INFO` BEFORE INSERT ON `user_info` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `user_reg` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_USER_REG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_USER_REG` BEFORE INSERT ON `user_reg` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `user_style` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_USER_STYLE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_USER_STYLE` BEFORE INSERT ON `user_style` FOR EACH ROW SET new.INSERT_TIME = now()
; */$$


DELIMITER ;

/* Trigger structure for table `wining_bid_file` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_WINING_BID_FILE` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_WINING_BID_FILE` BEFORE INSERT ON `wining_bid_file` FOR EACH ROW SET new.INSERT_TIME = now()
; */$$


DELIMITER ;

/* Trigger structure for table `wordbook` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_WORDBOOK` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_WORDBOOK` BEFORE INSERT ON `wordbook` FOR EACH ROW SET new.INSERT_TIME = now()
; */$$


DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
