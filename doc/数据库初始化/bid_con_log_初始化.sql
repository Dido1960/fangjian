/*
SQLyog Ultimate v12.14 (64 bit)
MySQL - 5.7.17-log : Database - bid_con_lz_log
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bid_con_lz_log` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `bid_con_lz_log`;

/*Table structure for table `api_log` */

DROP TABLE IF EXISTS `api_log`;

CREATE TABLE `api_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `API_NAME` varchar(255) DEFAULT NULL COMMENT '请求接口名称',
  `METHOD_NAME` varchar(255) DEFAULT NULL COMMENT '请求方法名称',
  `PARAMS` text COMMENT '请求参数内容',
  `PLATFORM` varchar(255) DEFAULT NULL COMMENT '平台授权码',
  `API_KEY` varchar(255) DEFAULT NULL COMMENT 'API授权码',
  `CREATE_API_TIME` datetime DEFAULT NULL COMMENT '日志创建时间',
  `RESPONSE` text COMMENT '响应结果',
  `RESPONSE_TIME` datetime DEFAULT NULL COMMENT '响应时间',
  `RESPONSE_TIME_CONSUME` bigint(20) DEFAULT NULL COMMENT '响应耗时',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API日志';

/*Data for the table `api_log` */

/*Table structure for table `api_push_log` */

DROP TABLE IF EXISTS `api_push_log`;

CREATE TABLE `api_push_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `API_URI` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `API_PARAMS` text COMMENT '请求参数',
  `API_REMARK` varchar(255) DEFAULT NULL COMMENT '请求说明',
  `CREATE_API_TIME` datetime DEFAULT NULL COMMENT '日志创建时间',
  `RESPONSE_CODE` int(11) DEFAULT NULL COMMENT '响应状态值',
  `RESPONSE_CONTENT` text COMMENT '响应返回值',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API推送日志';

/*Data for the table `api_push_log` */

/*Table structure for table `dock_push_log` */

DROP TABLE IF EXISTS `dock_push_log`;

CREATE TABLE `dock_push_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `API_URI` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `API_PARAMS` text COMMENT '请求参数',
  `API_PARAMS_LAWS` text COMMENT '请求参数(明文)',
  `API_REMARK` varchar(255) DEFAULT NULL COMMENT '请求说明',
  `CREATE_API_TIME` datetime DEFAULT NULL COMMENT '日志创建时间',
  `RESPONSE_CODE` int(11) DEFAULT NULL COMMENT '响应状态值',
  `RESPONSE_CONTENT` text COMMENT '响应返回值',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对接推送日志';

/*Data for the table `dock_push_log` */

/*Table structure for table `logging_event` */

DROP TABLE IF EXISTS `logging_event`;

CREATE TABLE `logging_event` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `TIMESTMP` varchar(255) DEFAULT NULL COMMENT '日志事件的创建时间',
  `FORMATTED_MESSAGE` varchar(255) DEFAULT NULL COMMENT '格式化后的消息',
  `LOGGER_NAME` varchar(255) DEFAULT NULL COMMENT '日志的 logger 名',
  `LEVEL_STRING` varchar(255) DEFAULT NULL COMMENT '日志事件的级别',
  `THREAD_NAME` varchar(255) DEFAULT NULL COMMENT '发出日志请求所在的线程名',
  `REFERENCE_FLAG` varchar(255) DEFAULT NULL COMMENT '用来表示是否是异常或者与 MDC 属性相关联。它的值通过 ch.qos.logback.classic.db.DBHelper 计算得到。日志时间包含 MDC 或者 Context 时，它的值为 1。包含异常时，它的值为 2。包含两者，则值为 3。',
  `ARG0` varchar(255) DEFAULT NULL COMMENT '参数1',
  `ARG1` varchar(255) DEFAULT NULL COMMENT '参数2',
  `ARG2` varchar(255) DEFAULT NULL COMMENT '参数3',
  `ARG3` varchar(255) DEFAULT NULL COMMENT '参数4',
  `CALLER_FILENAME` varchar(255) DEFAULT NULL COMMENT '发出日志请求的文件名',
  `CALLER_CLASS` varchar(255) DEFAULT NULL COMMENT '发出日志请求的类',
  `CALLER_METHOD` varchar(255) DEFAULT NULL COMMENT '发出日志请求的方法',
  `CALLER_LINE` varchar(255) DEFAULT NULL COMMENT '发出日志请求所在的行',
  `EVENT_ID` varchar(255) DEFAULT NULL COMMENT '主键ID',
  PRIMARY KEY (`ID`),
  KEY `IDX_FLZU2COQ` (`LEVEL_STRING`),
  KEY `IDX_TKHIL6GK` (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志事件表';

/*Data for the table `logging_event` */

/*Table structure for table `network_log` */

DROP TABLE IF EXISTS `network_log`;

CREATE TABLE `network_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户主键',
  `USER_NAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `REQUEST_URI` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `REQUEST_METHOD` varchar(255) DEFAULT NULL COMMENT '请求方法',
  `REQUEST_PARAMS` text COMMENT '请求参数',
  `REMOTE_ADDRESS` varchar(255) DEFAULT NULL COMMENT '来源地址',
  `USER_AGENT` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `DEVICE_NAME` varchar(255) DEFAULT NULL COMMENT '设备名称',
  `BROWSER_NAME` varchar(255) DEFAULT NULL COMMENT '浏览器名称',
  `BROWSER_VERSION` varchar(255) DEFAULT NULL COMMENT '浏览器版本',
  `PROCESSING_TIME` bigint(20) DEFAULT NULL COMMENT '处理耗时',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网络日志';

/*Data for the table `network_log` */

/*Table structure for table `quartz_job_log` */

DROP TABLE IF EXISTS `quartz_job_log`;

CREATE TABLE `quartz_job_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `JOB_NAME` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `JOB_GROUP` varchar(255) DEFAULT NULL COMMENT '任务组',
  `JOB_ID` int(11) DEFAULT NULL COMMENT '任务主键',
  `JOB_TIME` datetime DEFAULT NULL COMMENT '任务执行时间',
  `CLASS_NAME` varchar(255) DEFAULT NULL COMMENT '任务执行类',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Quartz定时任务执行日志';

/*Data for the table `quartz_job_log` */

/*Table structure for table `review_authority_log` */

DROP TABLE IF EXISTS `review_authority_log`;

CREATE TABLE `review_authority_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `USER_ID` int(11) DEFAULT NULL COMMENT '操作人id',
  `USER_NAME` varchar(255) DEFAULT NULL COMMENT '操作人名称',
  `CERT_NAME` varchar(255) DEFAULT NULL COMMENT '证书名称',
  `KEY_NO` varchar(255) DEFAULT NULL COMMENT '锁唯一序列号',
  `OPERATE_TYPE` int(11) DEFAULT NULL COMMENT '操作方式（1：写入权限， 0：取消权限）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='离线审核权限写入日志';

/*Data for the table `review_authority_log` */

/*Table structure for table `user_log` */

DROP TABLE IF EXISTS `user_log`;

CREATE TABLE `user_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSERT_TIME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据时间戳',
  `USER_ID` int(11) DEFAULT NULL COMMENT '用户ID',
  `USERNAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `DML_TYPE` varchar(255) DEFAULT NULL COMMENT '数据库操纵类型',
  `CONTENT` varchar(5000) DEFAULT NULL COMMENT '日志内容',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='用户日志';

/*Data for the table `user_log` */

insert  into `user_log`(`ID`,`INSERT_TIME`,`USER_ID`,`USERNAME`,`DML_TYPE`,`CONTENT`,`CREATE_TIME`) values
(1,'2021-01-25 12:51:55',1,'admin','更新','菜单权限设置：roleId=2,ids= 12,13,14,1,8,15,16,17,18,19','2021-01-25 12:51:55'),
(2,'2021-01-25 12:53:26',1,'admin','查询','查询区划下部门信息: dep=Dep(id=null, insertTime=null, regId=2, depName=null, enabled=null, orderNo=null, allSpelling=null, firstSpelling=null, parentId=null, govDepType=null, phone=null, remark=null, code=null, regName=null, status=)','2021-01-25 12:53:26'),
(3,'2021-01-25 12:53:29',1,'admin','查询','查询区划下部门信息: dep=Dep(id=null, insertTime=null, regId=3, depName=null, enabled=null, orderNo=null, allSpelling=null, firstSpelling=null, parentId=null, govDepType=null, phone=null, remark=null, code=null, regName=null, status=)','2021-01-25 12:53:29'),
(4,'2021-01-25 12:53:31',1,'admin','查询','查询区划下部门信息: dep=Dep(id=null, insertTime=null, regId=4, depName=null, enabled=null, orderNo=null, allSpelling=null, firstSpelling=null, parentId=null, govDepType=null, phone=null, remark=null, code=null, regName=null, status=)','2021-01-25 12:53:31'),
(5,'2021-01-25 12:57:02',1,'admin','更新','更新角色信息: role=Role(id=2, insertTime=null, roleName=运维最高权限人员, enabled=1, remark=运维最高权限人员相应权限, orderNo=0, status=启用, menuId=null)','2021-01-25 12:57:02'),
(6,'2021-01-25 12:57:25',1,'admin','更新','菜单权限设置：roleId=2,ids= 10,9,12,13,14,1,8,15,16,17,18,19','2021-01-25 12:57:25'),
(7,'2021-01-25 15:08:17',11565,'游客_11565','未知','游客登录','2021-01-25 15:08:17'),
(8,'2021-01-25 16:35:24',NULL,NULL,'查询','CA数据解密（密文）：{&quot;QSKJ_COMPANY_KEY&quot;:&quot;&quot;,&quot;QSKJ_CERT_REMAINDER&quot;:&quot;&quot;,&quot;QSKJ_CERT_ISSUED&quot;:&quot;&quot;,&quot;QSKJ_FREE_KEY&quot;:&quot;&quot;}加密因子：102080006658592/5303201810037213','2021-01-25 16:35:24');

/* Trigger structure for table `api_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_API_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_API_LOG` BEFORE INSERT ON `api_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `api_push_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_API_PUSH_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_API_PUSH_LOG` BEFORE INSERT ON `api_push_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `dock_push_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_DOCK_PUSH_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_DOCK_PUSH_LOG` BEFORE INSERT ON `dock_push_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `logging_event` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_LOGGING_EVENT` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_LOGGING_EVENT` BEFORE INSERT ON `logging_event` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `network_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_NETWORK_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_NETWORK_LOG` BEFORE INSERT ON `network_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `quartz_job_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_QUARTZ_JOB_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_QUARTZ_JOB_LOG` BEFORE INSERT ON `quartz_job_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `review_authority_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_REVIEW_AUTHORITY_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_REVIEW_AUTHORITY_LOG` BEFORE INSERT ON `review_authority_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/* Trigger structure for table `user_log` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `TRG_USER_LOG` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `TRG_USER_LOG` BEFORE INSERT ON `user_log` FOR EACH ROW SET new.INSERT_TIME = now() */$$


DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
