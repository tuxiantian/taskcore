/*
SQLyog Ultimate v8.61 
MySQL - 5.5.15-log : Database - drp_yqoc_test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `qrtz_blob_triggers` */

DROP TABLE IF EXISTS `qrtz_blob_triggers`;

CREATE TABLE `qrtz_blob_triggers` (
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `FK_Reference_25` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_calendars` */

DROP TABLE IF EXISTS `qrtz_calendars`;

CREATE TABLE `qrtz_calendars` (
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_cron_triggers` */

DROP TABLE IF EXISTS `qrtz_cron_triggers`;

CREATE TABLE `qrtz_cron_triggers` (
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '定时名称(必须唯一)( qrtz_triggers.trigger_name) ',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '定时组( qrtz_triggers.trigger_group) ',
  `CRON_EXPRESSION` varchar(200) NOT NULL COMMENT 'Cron格式的时间表达式',
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `FK_Reference_24` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_fired_triggers` */

DROP TABLE IF EXISTS `qrtz_fired_triggers`;

CREATE TABLE `qrtz_fired_triggers` (
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_STATEFUL` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_job_details` */

DROP TABLE IF EXISTS `qrtz_job_details`;

CREATE TABLE `qrtz_job_details` (
  `JOB_NAME` varchar(200) NOT NULL COMMENT '定时任务名称(必须唯一) ',
  `JOB_GROUP` varchar(200) NOT NULL COMMENT '定时任务组',
  `DESCRIPTION` varchar(250) DEFAULT NULL COMMENT '描述',
  `JOB_CLASS_NAME` varchar(250) NOT NULL COMMENT '定时执行目标类名',
  `IS_DURABLE` varchar(1) NOT NULL COMMENT '保存',
  `IS_VOLATILE` varchar(1) NOT NULL,
  `IS_STATEFUL` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL COMMENT '可恢复',
  `JOB_DATA` blob COMMENT '执行任务需要的参数',
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_job_listeners` */

DROP TABLE IF EXISTS `qrtz_job_listeners`;

CREATE TABLE `qrtz_job_listeners` (
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `JOB_LISTENER` varchar(200) NOT NULL,
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`,`JOB_LISTENER`),
  CONSTRAINT `FK_Reference_13` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_locks` */

DROP TABLE IF EXISTS `qrtz_locks`;

create table `qrtz_locks` (
	`LOCK_NAME` varchar (120)
); 
insert into `qrtz_locks` (`LOCK_NAME`) values('TRIGGER_ACCESS');

/*Table structure for table `qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;

CREATE TABLE `qrtz_paused_trigger_grps` (
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_scheduler_state` */

DROP TABLE IF EXISTS `qrtz_scheduler_state`;

CREATE TABLE `qrtz_scheduler_state` (
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_simple_triggers` */

DROP TABLE IF EXISTS `qrtz_simple_triggers`;

CREATE TABLE `qrtz_simple_triggers` (
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '定时名称(必须唯一) ( qrtz_triggers.trigger_name)',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '定时组( qrtz_triggers.trigger_group)',
  `REPEAT_COUNT` bigint(7) NOT NULL COMMENT '执行次数',
  `REPEAT_INTERVAL` bigint(12) NOT NULL COMMENT '间隔时间',
  `TIMES_TRIGGERED` bigint(7) NOT NULL COMMENT '时间',
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `FK_Reference_23` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_trigger_listeners` */

DROP TABLE IF EXISTS `qrtz_trigger_listeners`;

CREATE TABLE `qrtz_trigger_listeners` (
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `TRIGGER_LISTENER` varchar(200) NOT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_LISTENER`),
  CONSTRAINT `FK_Reference_26` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `qrtz_triggers` */

DROP TABLE IF EXISTS `qrtz_triggers`;

CREATE TABLE `qrtz_triggers` (
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '定时名称(必须唯一) ',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '定时组',
  `JOB_NAME` varchar(200) NOT NULL COMMENT '定时任务名称(必须唯一)( qrtz_job_details.job_name)',
  `JOB_GROUP` varchar(200) NOT NULL COMMENT '定时任务组( qrtz_job_details.jjob_group) ',
  `IS_VOLATILE` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL COMMENT '描述 ',
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL COMMENT '下次执行时间',
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL COMMENT '上次执行时间',
  `PRIORITY` int(11) NOT NULL DEFAULT '1',
  `TRIGGER_STATE` varchar(16) NOT NULL COMMENT '运行状态',
  `TRIGGER_TYPE` varchar(8) NOT NULL COMMENT '时间类型 ',
  `START_TIME` bigint(13) NOT NULL COMMENT '开始时间',
  `END_TIME` bigint(13) DEFAULT NULL COMMENT '结束时间',
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob COMMENT '执行任务需要的参数 ',
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `FK_Reference_22` (`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `FK_Reference_22` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

INSERT INTO QRTZ_LOCKS values('TRIGGER_ACCESS'); 
 INSERT INTO QRTZ_LOCKS values('JOB_ACCESS'); 
 INSERT INTO QRTZ_LOCKS values('CALENDAR_ACCESS'); 
 INSERT INTO QRTZ_LOCKS values('STATE_ACCESS'); 
 INSERT INTO QRTZ_LOCKS values('MISFIRE_ACCESS'); 