CREATE DATABASE `delay_job` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
use `delay_job`;

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`account` varchar(32) NOT NULL,
`passwd` varchar(32) NOT NULL,
`modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `global_rec`;
CREATE TABLE `global_rec` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`out_job_no` varchar(32) NOT NULL COMMENT '外部任务编号',
`job_shard_id` tinyint(3) unsigned NOT NULL COMMENT '任务分片ID',
`job_id` bigint(20) NOT NULL COMMENT '内部任务ID',
`trigger_time` bigint(21) NOT NULL COMMENT '触发时间',
`gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`),
UNIQUE KEY `idx_global_out_job_no` (`out_job_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `job_seg_trigger` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`job_shard_id` tinyint(3) unsigned NOT NULL COMMENT '任务分片ID',
`trigger_time_start` bigint(21) NOT NULL COMMENT '触发段起始时间',
`trigger_time_end` bigint(21) NOT NULL COMMENT '触发段截止时间',
`gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
PRIMARY KEY (`id`),
KEY `idx_st_jshard_id` (`job_shard_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `job_seg_trigger_flow` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`job_shard_id` tinyint(3) unsigned NOT NULL COMMENT '任务分片ID',
`trigger_time_start` bigint(20) NOT NULL COMMENT '任务段触发起始时间',
`trigger_time_end` bigint(20) NOT NULL COMMENT '任务段截止时间',
`state` tinyint(2) NOT NULL COMMENT '任务段处理状态:20-执行中 30-补偿中 40-完成',
`retry_times` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '重试次数',
`gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`),
KEY `idx_sgf_shard_trigger_time` (`job_shard_id`,`trigger_time_start`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `job_shard`;
CREATE TABLE `job_shard` (
`id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT COMMENT '分片ID',
`cur_server` int(11) NOT NULL COMMENT '当前分片持有者',
`req_server` int(11) NOT NULL DEFAULT '0' COMMENT '分片申请者',
`state` tinyint(3) unsigned NOT NULL COMMENT '5-正常，10-任务转移中，15-停用',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `scheduler_info`;
CREATE TABLE `scheduler_info` (
`id` int(9) unsigned NOT NULL AUTO_INCREMENT,
`uuid` varchar(32) NOT NULL COMMENT '调度器ID',
`last_heartbeat_time` bigint(22) DEFAULT NULL COMMENT '最后心跳时间',
`register` varchar(5) NOT NULL COMMENT '注册信息',
`ip` varchar(25) DEFAULT NULL COMMENT '调度器ip',
PRIMARY KEY (`id`),
KEY `idx_scheduler_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `sequence_key`;
CREATE TABLE `sequence_key` (
`id` int(11) NOT NULL,
`key` varchar(25) NOT NULL COMMENT 'ID种子键',
`start_with` bigint(20) DEFAULT '1' COMMENT '流水号起始',
`inc_span` int(11) DEFAULT '1' COMMENT '增长步骤',
`description` varchar(64) DEFAULT NULL COMMENT '说明',
PRIMARY KEY (`id`),
UNIQUE KEY `idx_sequence_key` (`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci;

INSERT INTO `sequence_key` (`id`, `key`, `start_with`, `inc_span`, `description`) VALUES (1000, 'JOB_ID', 100000000, 500, 'Job_ID生成种子');
INSERT INTO `account`(`id`, `account`, `passwd`, `modify_time`) VALUES (1, 'delay', '7243f8be75253afbadf7477867021f8b', '2022-07-29 00:43:51');