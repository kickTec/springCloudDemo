/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.6.33 : Database - test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`test` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `test`;

/*Table structure for table `hibernate_sequence` */

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `hibernate_sequence` */

insert  into `hibernate_sequence`(`next_val`) values (1);

/*Table structure for table `message_tx` */

DROP TABLE IF EXISTS `message_tx`;

CREATE TABLE `message_tx` (
  `message_id` varchar(40) NOT NULL COMMENT '消息ID',
  `mq_msg_id` varchar(50) DEFAULT NULL COMMENT 'mq消息ID',
  `topics` varchar(30) DEFAULT NULL COMMENT '主题',
  `tags` varchar(30) DEFAULT NULL COMMENT '标签',
  `keys` varchar(30) DEFAULT NULL COMMENT '消息关键词',
  `message_body` varchar(200) DEFAULT NULL COMMENT '消息主体',
  `service_type` int(11) DEFAULT NULL COMMENT '业务类型',
  `service_id` varchar(40) DEFAULT NULL COMMENT '业务ID',
  `relate_id` varchar(40) DEFAULT NULL COMMENT '关联ID',
  `message_status` int(11) DEFAULT NULL COMMENT '消息状态（1、初始化 2、发送成功 3、本地异常（发送失败） 4、消费成功 5、消费失败 6、已死亡 7、已删除）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `message_tx` */

/*Table structure for table `t_logger` */

DROP TABLE IF EXISTS `t_logger`;

CREATE TABLE `t_logger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) NOT NULL,
  `unit_id` varchar(32) NOT NULL,
  `tag` varchar(50) NOT NULL,
  `content` varchar(1024) NOT NULL,
  `create_time` varchar(30) NOT NULL,
  `app_name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

/*Data for the table `t_logger` */

/*Table structure for table `t_tx_exception` */

DROP TABLE IF EXISTS `t_tx_exception`;

CREATE TABLE `t_tx_exception` (
  `id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `ex_state` smallint(6) NOT NULL,
  `group_id` varchar(60) DEFAULT NULL,
  `mod_id` varchar(100) DEFAULT NULL,
  `registrar` smallint(6) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `transaction_state` int(11) DEFAULT NULL,
  `unit_id` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `t_tx_exception` */

/*Table structure for table `tb_description` */

DROP TABLE IF EXISTS `tb_description`;

CREATE TABLE `tb_description` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Data for the table `tb_description` */

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `description_id` int(11) DEFAULT NULL COMMENT '关联详情id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户信息表';

/*Data for the table `tb_user` */

/*Table structure for table `transaction_message` */

DROP TABLE IF EXISTS `transaction_message`;

CREATE TABLE `transaction_message` (
  `id` bigint(64) NOT NULL,
  `message` varchar(1000) NOT NULL COMMENT '消息内容',
  `queue` varchar(50) NOT NULL COMMENT '队列名称',
  `send_system` varchar(20) NOT NULL COMMENT '发送消息的系统',
  `send_count` int(4) NOT NULL DEFAULT '0' COMMENT '重复发送消息次数',
  `c_date` datetime NOT NULL COMMENT '创建时间',
  `send_date` datetime DEFAULT NULL COMMENT '最近发送消息时间',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '状态：0等待消费  1已消费  2已死亡',
  `die_count` int(4) NOT NULL DEFAULT '0' COMMENT '死亡次数条件，由使用方决定，默认为发送10次还没被消费则标记死亡,人工介入',
  `customer_date` datetime DEFAULT NULL COMMENT '消费时间',
  `customer_system` varchar(50) DEFAULT NULL COMMENT '消费系统',
  `die_date` datetime DEFAULT NULL COMMENT '死亡时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `transaction_message` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` varchar(30) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

/* Function  structure for function  `функцийка` */

/*!50003 DROP FUNCTION IF EXISTS `функцийка` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `функцийка`( параметър_версия VARCHAR(25)) RETURNS varchar(25) CHARSET latin1
    DETERMINISTIC
RETURN параметър_версия */$$
DELIMITER ;

/* Procedure structure for procedure `p` */

/*!50003 DROP PROCEDURE IF EXISTS  `p` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p`()
BEGIN DECLARE cols INT; SELECT @numcols INTO cols; IF cols < 2 THEN SET @numcols = 2; SELECT cols AS "one"; ELSE SET @numcols = 1; SELECT 1 AS "one", cols AS "two"; END IF; END */$$
DELIMITER ;

/* Procedure structure for procedure `p123` */

/*!50003 DROP PROCEDURE IF EXISTS  `p123` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `p123`()
BEGIN SELECT id+12, CONCAT_WS('-',label,'ahoi') FROM test ORDER BY id LIMIT 1; SELECT id + 42, CONCAT_WS('---',label, label) FROM test ORDER BY id LIMIT 1; END */$$
DELIMITER ;

/* Procedure structure for procedure `процедурка` */

/*!50003 DROP PROCEDURE IF EXISTS  `процедурка` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `процедурка`(OUT версия VARCHAR(25))
BEGIN SELECT VERSION() INTO версия; END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
