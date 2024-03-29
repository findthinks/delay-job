CREATE DATABASE `delay_job` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE `delay_job`;

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(32) NOT NULL,
  `passwd` varchar(32) NOT NULL,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO `account` (`id`, `account`, `passwd`, `modify_time`) VALUES (1, 'delay', '7243f8be75253afbadf7477867021f8b', '2021-01-29 00:43:51');

DROP TABLE IF EXISTS `global_rec`;
CREATE TABLE `global_rec` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `out_job_no` varchar(32) NOT NULL COMMENT '外部任务编号',
  `job_shard_id` tinyint unsigned NOT NULL COMMENT '任务分片ID',
  `job_id` bigint NOT NULL COMMENT '内部任务ID',
  `trigger_time` bigint NOT NULL COMMENT '触发时间',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_global_out_job_no` (`out_job_no`) USING BTREE,
  KEY `idx_global_trigger_time` (`trigger_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `job_1`;
CREATE TABLE `job_1` (
  `id` bigint unsigned NOT NULL,
  `job_shard_id` tinyint unsigned NOT NULL COMMENT '任务分片ID',
  `out_job_no` varchar(32) NOT NULL COMMENT '外部任务编号',
  `trigger_time` bigint NOT NULL COMMENT '计划触发时间',
  `pause_time` bigint DEFAULT NULL COMMENT '暂停计时时间点',
  `type` tinyint unsigned NOT NULL COMMENT '任务类型 5-普通任务，10-可暂停计时任务',
  `state` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '任务状态:10-提交 20-重试中 30-成功 40-失败 50-取消',
  `retry_times` tinyint(1) unsigned zerofill NOT NULL COMMENT '重试次数',
  `callback_protocol` tinyint(1) NOT NULL COMMENT '回调协议:0-HTTP 5-GRPC 10-KAFKA 20-LOG',
  `callback_endpoint` varchar(64) NOT NULL COMMENT '回调地址',
  `job_info` varchar(256) DEFAULT NULL COMMENT 'JOB附加参数',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_job1_out_job_no` (`out_job_no`) USING BTREE,
  KEY `idx_job1_trigger_time` (`trigger_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `job_2`;
CREATE TABLE `job_2` (
  `id` bigint unsigned NOT NULL,
  `job_shard_id` tinyint unsigned NOT NULL COMMENT '任务分片ID',
  `out_job_no` varchar(32) NOT NULL COMMENT '外部任务编号',
  `trigger_time` bigint NOT NULL COMMENT '计划触发时间',
  `pause_time` bigint DEFAULT NULL COMMENT '暂停计时时间点',
  `type` tinyint unsigned NOT NULL COMMENT '任务类型 5-普通任务，10-可暂停计时任务',
  `state` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '任务状态:10-提交 20-重试中 30-成功 40-失败 50-取消',
  `retry_times` tinyint(1) unsigned zerofill NOT NULL COMMENT '重试次数',
  `callback_protocol` tinyint(1) NOT NULL COMMENT '回调协议:0-HTTP 5-GRPC 10-KAFKA 20-LOG',
  `callback_endpoint` varchar(64) NOT NULL COMMENT '回调地址',
  `job_info` varchar(256) DEFAULT NULL COMMENT 'JOB附加参数',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_job2_out_job_no` (`out_job_no`) USING BTREE,
  KEY `idx_job2_trigger_time` (`trigger_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `job_3`;
CREATE TABLE `job_3` (
  `id` bigint unsigned NOT NULL,
  `job_shard_id` tinyint unsigned NOT NULL COMMENT '任务分片ID',
  `out_job_no` varchar(32) NOT NULL COMMENT '外部任务编号',
  `trigger_time` bigint NOT NULL COMMENT '计划触发时间',
  `pause_time` bigint DEFAULT NULL COMMENT '暂停计时时间点',
  `type` tinyint unsigned NOT NULL COMMENT '任务类型 5-普通任务，10-可暂停计时任务',
  `state` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '任务状态:10-提交 20-重试中 30-成功 40-失败 50-取消',
  `retry_times` tinyint(1) unsigned zerofill NOT NULL COMMENT '重试次数',
  `callback_protocol` tinyint(1) NOT NULL COMMENT '回调协议:0-HTTP 5-GRPC 10-KAFKA 20-LOG',
  `callback_endpoint` varchar(64) NOT NULL COMMENT '回调地址',
  `job_info` varchar(256) DEFAULT NULL COMMENT 'JOB附加参数',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_job3_out_job_no` (`out_job_no`) USING BTREE,
  KEY `idx_job3_trigger_time` (`trigger_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `job_seg_trigger`;
CREATE TABLE `job_seg_trigger` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `job_shard_id` tinyint unsigned NOT NULL COMMENT '任务分片ID',
  `trigger_time_start` bigint NOT NULL COMMENT '触发段起始时间',
  `trigger_time_end` bigint NOT NULL COMMENT '触发段截止时间',
  `gmt_modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_st_jshard_id` (`job_shard_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `job_seg_trigger_flow`;
CREATE TABLE `job_seg_trigger_flow` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `job_shard_id` tinyint unsigned NOT NULL COMMENT '任务分片ID',
  `trigger_time_start` bigint NOT NULL COMMENT '任务段触发起始时间',
  `trigger_time_end` bigint NOT NULL COMMENT '任务段截止时间',
  `state` tinyint NOT NULL COMMENT '任务段处理状态:20-执行中 30-补偿中 40-完成',
  `retry_times` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '重试次数',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_sgf_shard_trigger_time` (`job_shard_id`,`trigger_time_start`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `job_shard`;
CREATE TABLE `job_shard` (
  `id` tinyint unsigned NOT NULL AUTO_INCREMENT COMMENT '分片ID',
  `cur_server` int NOT NULL COMMENT '当前分片持有者',
  `req_server` int NOT NULL DEFAULT '0' COMMENT '分片申请者',
  `state` tinyint unsigned NOT NULL COMMENT '5-正常，10-任务转移中，15-停用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO `job_shard` (`id`, `cur_server`, `req_server`, `state`) VALUES (1, 1, -1, 5);
INSERT INTO `job_shard` (`id`, `cur_server`, `req_server`, `state`) VALUES (2, 1, -1, 5);
INSERT INTO `job_shard` (`id`, `cur_server`, `req_server`, `state`) VALUES (3, 1, -1, 5);

DROP TABLE IF EXISTS `scheduler_info`;
CREATE TABLE `scheduler_info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(32) NOT NULL COMMENT '调度器ID',
  `last_heartbeat_time` bigint DEFAULT NULL COMMENT '最后心跳时间',
  `register` varchar(5) NOT NULL COMMENT '注册信息',
  `ip` varchar(25) DEFAULT NULL COMMENT '调度器ip',
  PRIMARY KEY (`id`),
  KEY `idx_scheduler_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `sequence_key`;
CREATE TABLE `sequence_key` (
 `id` int NOT NULL,
 `key` varchar(25) NOT NULL COMMENT 'ID种子键',
 `start_with` bigint DEFAULT '1' COMMENT '流水号起始',
 `inc_span` int DEFAULT '1' COMMENT '增长步骤',
 `description` varchar(64) DEFAULT NULL COMMENT '说明',
 PRIMARY KEY (`id`),
 UNIQUE KEY `idx_sequence_key` (`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO `sequence_key` (`id`, `key`, `start_with`, `inc_span`, `description`) VALUES (1000, 'JOB_ID', 100000000, 1000, 'Job_ID生成种子');
COMMIT;