CREATE TABLE `kt_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码邮箱',
  `is_deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

CREATE TABLE `kt_project` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'key',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目名',
  `is_deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '更新人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目';

CREATE TABLE `kt_file_repo` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) unsigned NOT NULL,
  `file_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `file_type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件类型',
  `is_deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '更新人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件资源';

CREATE TABLE `kt_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `repo_id` bigint(20) NOT NULL COMMENT '资源ID',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '运行状态',
  `last_start_time` timestamp NULL DEFAULT NULL COMMENT '最后一次开始时间',
  `last_end_time` timestamp NULL DEFAULT NULL COMMENT '最后一次结束时间',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `log_level` int(11) NOT NULL DEFAULT '3' COMMENT '运行日志等级。0:Nothing,1:Error,2:Minimal,3:Basic,4:Detailed,5:Debug,6:Rowlevel',
  `is_deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '更新人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务';

CREATE TABLE `kt_runner` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL COMMENT '任务ID',
  `trigger` int(1) NOT NULL DEFAULT '1' COMMENT '触发方式。1：手动，2：定时',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '运行状态',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '最后一次开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '最后一次结束时间',
  `create_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运行记录';

create table `qrtz_calendars` (
                                  `sched_name` varchar(120) not null,
                                  `calendar_name` varchar(120) not null,
                                  `calendar` blob not null,
                                  primary key (`sched_name`,`calendar_name`) using btree
) engine=innodb default charset=utf8mb4;

create table `qrtz_fired_triggers` (
                                       `sched_name` varchar(120) not null,
                                       `entry_id` varchar(95) not null,
                                       `trigger_name` varchar(120) not null,
                                       `trigger_group` varchar(120) not null,
                                       `instance_name` varchar(120) not null,
                                       `fired_time` bigint(13) not null,
                                       `sched_time` bigint(13) not null,
                                       `priority` int(11) not null,
                                       `state` varchar(16) not null,
                                       `job_name` varchar(120) default null,
                                       `job_group` varchar(120) default null,
                                       `is_nonconcurrent` varchar(1) default null,
                                       `requests_recovery` varchar(1) default null,
                                       primary key (`sched_name`,`entry_id`) using btree,
                                       key `idx_qrtz_ft_trig_inst_name` (`sched_name`,`instance_name`) using btree,
                                       key `idx_qrtz_ft_inst_job_req_rcvry` (`sched_name`,`instance_name`,`requests_recovery`) using btree,
                                       key `idx_qrtz_ft_j_g` (`sched_name`,`job_name`,`job_group`) using btree,
                                       key `idx_qrtz_ft_jg` (`sched_name`,`job_group`) using btree,
                                       key `idx_qrtz_ft_t_g` (`sched_name`,`trigger_name`,`trigger_group`) using btree,
                                       key `idx_qrtz_ft_tg` (`sched_name`,`trigger_group`) using btree
) engine=innodb default charset=utf8mb4;

create table `qrtz_job_details` (
                                    `sched_name` varchar(120) not null,
                                    `job_name` varchar(120) not null,
                                    `job_group` varchar(120) not null,
                                    `description` varchar(120) default null,
                                    `job_class_name` varchar(120) not null,
                                    `is_durable` varchar(1) not null,
                                    `is_nonconcurrent` varchar(1) not null,
                                    `is_update_data` varchar(1) not null,
                                    `requests_recovery` varchar(1) not null,
                                    `job_data` blob,
                                    primary key (`sched_name`,`job_name`,`job_group`) using btree,
                                    key `idx_qrtz_j_req_recovery` (`sched_name`,`requests_recovery`) using btree,
                                    key `idx_qrtz_j_grp` (`sched_name`,`job_group`) using btree
) engine=innodb default charset=utf8mb4;

create table `qrtz_locks` (
                              `sched_name` varchar(120) not null,
                              `lock_name` varchar(40) not null,
                              primary key (`sched_name`,`lock_name`) using btree
) engine=innodb default charset=utf8mb4;

create table `qrtz_paused_trigger_grps` (
                                            `sched_name` varchar(120) not null,
                                            `trigger_group` varchar(120) not null,
                                            primary key (`sched_name`,`trigger_group`) using btree
) engine=innodb default charset=utf8mb4;

create table `qrtz_scheduler_state` (
                                        `sched_name` varchar(120) not null,
                                        `instance_name` varchar(120) not null,
                                        `last_checkin_time` bigint(13) not null,
                                        `checkin_interval` bigint(13) not null,
                                        primary key (`sched_name`,`instance_name`) using btree
) engine=innodb default charset=utf8mb4;


create table `qrtz_triggers` (
                                 `sched_name` varchar(120) not null,
                                 `trigger_name` varchar(120) not null,
                                 `trigger_group` varchar(120) not null,
                                 `job_name` varchar(120) not null,
                                 `job_group` varchar(120) not null,
                                 `description` varchar(120) default null,
                                 `next_fire_time` bigint(13) default null,
                                 `prev_fire_time` bigint(13) default null,
                                 `priority` int(11) default null,
                                 `trigger_state` varchar(16) not null,
                                 `trigger_type` varchar(8) not null,
                                 `start_time` bigint(13) not null,
                                 `end_time` bigint(13) default null,
                                 `calendar_name` varchar(120) default null,
                                 `misfire_instr` smallint(2) default null,
                                 `job_data` blob,
                                 primary key (`sched_name`,`trigger_name`,`trigger_group`) using btree,
                                 key `idx_qrtz_t_j` (`sched_name`,`job_name`,`job_group`) using btree,
                                 key `idx_qrtz_t_jg` (`sched_name`,`job_group`) using btree,
                                 key `idx_qrtz_t_c` (`sched_name`,`calendar_name`) using btree,
                                 key `idx_qrtz_t_g` (`sched_name`,`trigger_group`) using btree,
                                 key `idx_qrtz_t_state` (`sched_name`,`trigger_state`) using btree,
                                 key `idx_qrtz_t_n_state` (`sched_name`,`trigger_name`,`trigger_group`,`trigger_state`) using btree,
                                 key `idx_qrtz_t_n_g_state` (`sched_name`,`trigger_group`,`trigger_state`) using btree,
                                 key `idx_qrtz_t_next_fire_time` (`sched_name`,`next_fire_time`) using btree,
                                 key `idx_qrtz_t_nft_st` (`sched_name`,`trigger_state`,`next_fire_time`) using btree,
                                 key `idx_qrtz_t_nft_misfire` (`sched_name`,`misfire_instr`,`next_fire_time`) using btree,
                                 key `idx_qrtz_t_nft_st_misfire` (`sched_name`,`misfire_instr`,`next_fire_time`,`trigger_state`) using btree,
                                 key `idx_qrtz_t_nft_st_misfire_grp` (`sched_name`,`misfire_instr`,`next_fire_time`,`trigger_group`,`trigger_state`) using btree,
                                 constraint `qrtz_triggers_ibfk_1` foreign key (`sched_name`, `job_name`, `job_group`) references `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) engine=innodb default charset=utf8mb4;




create table `qrtz_blob_triggers` (
                                      `sched_name` varchar(120) not null,
                                      `trigger_name` varchar(120) not null,
                                      `trigger_group` varchar(120) not null,
                                      `blob_data` blob,
                                      primary key (`sched_name`,`trigger_name`,`trigger_group`) using btree,
                                      key `sched_name` (`sched_name`,`trigger_name`,`trigger_group`) using btree,
                                      constraint `qrtz_blob_triggers_ibfk_1` foreign key (`sched_name`, `trigger_name`, `trigger_group`) references `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) engine=innodb default charset=utf8mb4;



create table `qrtz_cron_triggers` (
                                      `sched_name` varchar(120) not null,
                                      `trigger_name` varchar(120) not null,
                                      `trigger_group` varchar(120) not null,
                                      `cron_expression` varchar(120) not null,
                                      `time_zone_id` varchar(80) default null,
                                      primary key (`sched_name`,`trigger_name`,`trigger_group`) using btree,
                                      constraint `qrtz_cron_triggers_ibfk_1` foreign key (`sched_name`, `trigger_name`, `trigger_group`) references `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) engine=innodb default charset=utf8mb4;


create table `qrtz_simple_triggers` (
                                        `sched_name` varchar(120) not null,
                                        `trigger_name` varchar(120) not null,
                                        `trigger_group` varchar(120) not null,
                                        `repeat_count` bigint(7) not null,
                                        `repeat_interval` bigint(12) not null,
                                        `times_triggered` bigint(10) not null,
                                        primary key (`sched_name`,`trigger_name`,`trigger_group`) using btree,
                                        constraint `qrtz_simple_triggers_ibfk_1` foreign key (`sched_name`, `trigger_name`, `trigger_group`) references `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) engine=innodb default charset=utf8mb4;


create table `qrtz_simprop_triggers` (
                                         `sched_name` varchar(120) not null,
                                         `trigger_name` varchar(120) not null,
                                         `trigger_group` varchar(120) not null,
                                         `str_prop_1` varchar(120) default null,
                                         `str_prop_2` varchar(120) default null,
                                         `str_prop_3` varchar(120) default null,
                                         `int_prop_1` int(11) default null,
                                         `int_prop_2` int(11) default null,
                                         `long_prop_1` bigint(20) default null,
                                         `long_prop_2` bigint(20) default null,
                                         `dec_prop_1` decimal(13,4) default null,
                                         `dec_prop_2` decimal(13,4) default null,
                                         `bool_prop_1` varchar(1) default null,
                                         `bool_prop_2` varchar(1) default null,
                                         primary key (`sched_name`,`trigger_name`,`trigger_group`) using btree,
                                         constraint `qrtz_simprop_triggers_ibfk_1` foreign key (`sched_name`, `trigger_name`, `trigger_group`) references `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) engine=innodb default charset=utf8mb4;
