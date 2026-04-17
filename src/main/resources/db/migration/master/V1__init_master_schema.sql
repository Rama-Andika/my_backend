SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE tenants (
                         tenant_id VARCHAR(50) PRIMARY KEY,
                         db_url VARCHAR(255) NOT NULL,
                         db_username VARCHAR(100) NOT NULL,
                         db_password VARCHAR(100) NOT NULL,
                         STATUS VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, SUSPENDED, INACTIVE',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `sysuser` (
                           `user_id` bigint(20) NOT NULL DEFAULT 0,
                           `login_id` varchar(20) DEFAULT NULL,
                           `password` varchar(32) DEFAULT NULL,
                           `full_name` varchar(64) DEFAULT NULL,
                           `email` varchar(64) DEFAULT NULL,
                           `description` varchar(120) DEFAULT NULL,
                           `reg_date` date DEFAULT NULL,
                           `update_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
                           `user_status` int(11) DEFAULT NULL,
                           `last_login_date` datetime DEFAULT NULL,
                           `last_login_ip` varchar(20) DEFAULT NULL,
                           `employee_id` bigint(20) DEFAULT NULL,
                           `employee_num` varchar(20) DEFAULT NULL,
                           `user_level` int(11) DEFAULT NULL,
                           `company_id` bigint(20) unsigned NOT NULL DEFAULT 0,
                           `last_login_date1` datetime DEFAULT NULL,
                           `segment1_id` bigint(20) DEFAULT NULL,
                           `segment2_id` bigint(20) DEFAULT NULL,
                           `segment3_id` bigint(20) DEFAULT NULL,
                           `segment4_id` bigint(20) DEFAULT NULL,
                           `segment5_id` bigint(20) DEFAULT NULL,
                           `segment6_id` bigint(20) DEFAULT NULL,
                           `segment7_id` bigint(20) DEFAULT NULL,
                           `segment8_id` bigint(20) DEFAULT NULL,
                           `segment9_id` bigint(20) DEFAULT NULL,
                           `segment10_id` bigint(20) DEFAULT NULL,
                           `segment11_id` bigint(20) DEFAULT NULL,
                           `segment12_id` bigint(20) DEFAULT NULL,
                           `segment13_id` bigint(20) DEFAULT NULL,
                           `segment14_id` bigint(20) DEFAULT NULL,
                           `segment15_id` bigint(20) DEFAULT NULL,
                           `user_key` varchar(145) DEFAULT NULL,
                           `reset_password` tinyint(4) DEFAULT 0,
                           `tipe_sales` int(11) NOT NULL DEFAULT 1,
                           `group_id` bigint(20) DEFAULT NULL,
                           PRIMARY KEY (`user_id`),
                           UNIQUE KEY `XPKsysuser` (`user_id`),
                           KEY `FKbgik636v4w9ml1p9i9gj382gh` (`group_id`),
                           CONSTRAINT `FKbgik636v4w9ml1p9i9gj382gh` FOREIGN KEY (`group_id`) REFERENCES `grp` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

insert into `sysuser` (`user_id`, `login_id`, `password`, `full_name`, `email`, `description`, `reg_date`, `update_date`, `user_status`, `last_login_date`, `last_login_ip`, `employee_id`, `employee_num`, `user_level`, `company_id`, `last_login_date1`, `segment1_id`, `segment2_id`, `segment3_id`, `segment4_id`, `segment5_id`, `segment6_id`, `segment7_id`, `segment8_id`, `segment9_id`, `segment10_id`, `segment11_id`, `segment12_id`, `segment13_id`, `segment14_id`, `segment15_id`, `user_key`, `reset_password`, `tipe_sales`, `group_id`) values('504404509311573860','superuser','oxyriverside','Superuser','','','2012-10-24','2026-04-01 10:33:23','2','2026-04-01 10:33:23','','504404509311499481','-','1','0',NULL,'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','d71520s5u7','0','0',null);

CREATE TABLE `api_logs` (
                            `api_log_id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `date` timestamp NULL DEFAULT current_timestamp(),
                            `req_name` varchar(255) NOT NULL,
                            `req_header` varchar(255) NOT NULL,
                            `req_body` text NOT NULL,
                            `res_body` text DEFAULT NULL,
                            `TIMESTAMP` datetime DEFAULT NULL,
                            PRIMARY KEY (`api_log_id`),
                            KEY `req_name` (`req_name`),
                            KEY `req_header` (`req_header`),
                            KEY `re_date` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=17755447379454643 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `grp` (
                       `group_id` bigint(20) NOT NULL DEFAULT 0,
                       `group_name` varchar(64) DEFAULT NULL,
                       `reg_date` datetime DEFAULT NULL,
                       `description` text DEFAULT NULL,
                       PRIMARY KEY (`group_id`),
                       UNIQUE KEY `XPKgrp` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `refresh_token` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `expiry_date` datetime(6) NOT NULL,
                                 `revoked` bit(1) NOT NULL,
                                 `token_hash` varchar(64) NOT NULL,
                                 `user_id` bigint(20) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `UK_kdj16cltjxdksuyiosdhliveg` (`token_hash`),
                                 KEY `FKajcmd839bavll21gf9pjlbt2f` (`user_id`),
                                 CONSTRAINT `FKajcmd839bavll21gf9pjlbt2f` FOREIGN KEY (`user_id`) REFERENCES `sysuser` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1354 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;