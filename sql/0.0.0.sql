CREATE DATABASE IF NOT EXISTS `payment` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

DROP TABLE IF EXISTS `project`;
CREATE TABLE IF NOT EXISTS `project`
(
    `id`           int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name`         varchar(50)         NOT NULL COMMENT '项目名',
    `disabled`     tinyint(1) DEFAULT 1 COMMENT '是否禁用',
    `api_key`      char(16)   DEFAULT '',
    `api_security` char(32)   DEFAULT '',
    `create_time`  datetime(4),
    KEY `idx_key` (api_key) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='项目';

DROP TABLE IF EXISTS `project_history`;
CREATE TABLE IF NOT EXISTS `project_history`
(
    `id`           int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `user_id`      int(11)             NOT NULL COMMENT '操作人',
    `project_id`   int(11)             NOT NULL COMMENT '项目ID',
    `name`         varchar(50)         NOT NULL COMMENT '项目名',
    `disabled`     tinyint(1) DEFAULT 1 COMMENT '是否禁用',
    `api_key`      char(16)   DEFAULT '',
    `api_security` char(32)   DEFAULT '',
    `create_time`  datetime(4),
    KEY `idx_user_project` (`user_id`, `project_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='项目修改记录';

DROP TABLE IF EXISTS `pay`;
CREATE TABLE IF NOT EXISTS `pay`
(
    `trade_no`            char(21)     NOT NULL PRIMARY KEY COMMENT '交易号',
    `project_id`          int(11)      NOT NULL COMMENT '项目ID',
    `project_trade_no`    varchar(50)  NOT NULL COMMENT '项目交易号',
    `third_part_trade_no` varchar(120)            DEFAULT '' COMMENT '第三方交易号',
    `status`              varchar(10)  NOT NULL COMMENT '支付状态',
    `amount`              decimal(21, 4) UNSIGNED DEFAULT 0 COMMENT '支付金额',
    `currency`            varchar(10)  NOT NULL COMMENT '支付货币',
    `chain`               varchar(10)             DEFAULT '' COMMENT '链',
    `address`             varchar(120)            DEFAULT '' COMMENT '收款地址',
    `retry_end_time`      datetime(4)             DEFAULT NULL COMMENT '重试截止时间',
    `third_part`          varchar(10)             DEFAULT '' COMMENT '第三方',
    `mode`                varchar(10)             DEFAULT '' COMMENT '支付模式',
    `desc`                varchar(255)            DEFAULT '' COMMENT '描述',
    `notify_url`          varchar(200) NOT NULL COMMENT '通知地址',
    `notify_status`       varchar(10)             DEFAULT 'WAIT' COMMENT '通知状态',
    `complete_time`       datetime(4),
    `create_time`         datetime(4),
    KEY `idx_project` (`project_id`) USING BTREE,
    KEY `idx_project_trade` (`project_trade_no`) USING BTREE,
    KEY `idx_third_trade` (`third_part_trade_no`) USING BTREE,
    KEY `idx_status` (`status`) USING BTREE,
    KEY `idx_currency` (`currency`) USING BTREE,
    KEY `idx_notify_status` (`notify_status`) USING BTREE,
    KEY `idx_address` (`address`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='支付信息';

DROP TABLE IF EXISTS `virtual_address`;
CREATE TABLE IF NOT EXISTS `virtual_address`
(
    `id`          int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `chain`       varchar(10)  DEFAULT '' COMMENT '链',
    `address`     varchar(120) DEFAULT '' COMMENT '地址',
    `disabled`    tinyint(1)   DEFAULT 1 COMMENT '是否禁用',
    `using`       tinyint(1)   DEFAULT 0 COMMENT '是否使用中',
    `create_time` datetime(4)         NOT NULL,
    KEY `idx_chain` (`chain`) USING BTREE,
    UNIQUE KEY `uk_address` (`address`) USING BTREE,
    KEY `idx_disabled` (`disabled`) USING BTREE,
    KEY `idx_using` (`using`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='虚拟货币地址';

ALTER TABLE `notify` ADD COLUMN `rate`        decimal(12, 4) DEFAULT NULL COMMENT '汇率';
DROP TABLE IF EXISTS `notify`;
CREATE TABLE IF NOT EXISTS `notify`
(
    `id`          bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `project_id`  int(11)                NOT NULL COMMENT '项目ID',
    `trade_no`    char(21)               NOT NULL COMMENT '交易号',
    `notify_url`  varchar(200)           NOT NULL COMMENT '通知地址',
    `status`      varchar(10)    DEFAULT 'WAIT' COMMENT '通知状态',
    `next_time`   datetime(4)            NOT NULL COMMENT '下次通知时间',
    `count`       int(11)        DEFAULT 0 COMMENT '通知次数',
    `rate`        decimal(12, 4) DEFAULT NULL COMMENT '汇率',
    `create_time` datetime(4)            NOT NULL,
    KEY `idx_project` (`project_id`) USING BTREE,
    KEY `idx_trade_no` (`trade_no`) USING BTREE,
    KEY `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='通知';

DROP TABLE IF EXISTS `notify_log`;
CREATE TABLE IF NOT EXISTS `notify_log`
(
    `id`          bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `notify_id`   int(11)                NOT NULL COMMENT '通知ID',
    `project_id`  int(11)                NOT NULL COMMENT '项目ID',
    `trade_no`    char(21)               NOT NULL COMMENT '交易号',
    `notify_url`  varchar(200)           NOT NULL COMMENT '通知地址',
    `status`      varchar(10) DEFAULT 'WAIT' COMMENT '通知状态',
    `params`      text                   NOT NULL COMMENT '请求参数',
    `http_status` int(11)     DEFAULT 0 COMMENT '请求状态',
    `res`         text                   NOT NULL COMMENT '请求返回值',
    `create_time` datetime(4)            NOT NULL COMMENT '通知时间',
    KEY `idx_notify` (`notify_id`) USING BTREE,
    KEY `idx_project` (`project_id`) USING BTREE,
    KEY `idx_trade_no` (`trade_no`) USING BTREE,
    KEY `idx_http` (`http_status`) USING BTREE,
    KEY `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='通知日志';
