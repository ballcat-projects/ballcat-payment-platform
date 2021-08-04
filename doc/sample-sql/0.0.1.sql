INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `permission`, `target_type`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`)
VALUES (200202, 200200, '支付信息强制重试', 'pay:forcibly:retry', 1, 2, 0, 0, 2, 0, NOW(), NULL)
     , (200203, 200200, '支付信息强制失败', 'pay:forcibly:fail', 1, 3, 0, 0, 2, 0, NOW(), NULL)
;

INSERT INTO `sys_role_menu` (`role_code`, `menu_id`)
VALUES ('ROLE_ADMIN', 200202)
     , ('ROLE_ADMIN', 200203)
;

CREATE TABLE `api_access_log`
(
    `id`          bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `trace_id`    char(24)      DEFAULT NULL COMMENT '追踪ID',
    `project_id`  bigint(16)    DEFAULT 0 COMMENT '项目ID',
    `key`         char(16)      DEFAULT '' COMMENT 'API KEY',
    `ip`          varchar(64)   DEFAULT NULL COMMENT '访问IP地址',
    `ua`          varchar(1000) DEFAULT '' COMMENT 'ua',
    `uri`         varchar(255)  DEFAULT '' COMMENT '请求URI',
    `method`      varchar(10)   DEFAULT '' COMMENT '操作方式',
    `req_params`  text COMMENT '请求参数',
    `req_body`    text COMMENT '请求body',
    `http_status` int(5)        DEFAULT NULL COMMENT '响应状态码',
    `result`      text COMMENT '响应信息',
    `error_msg`   text COMMENT '错误消息',
    `time`        bigint(64)    DEFAULT NULL COMMENT '执行时长',
    `create_time` datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_trace` (`trace_id`) USING BTREE,
    KEY `idx_project` (`project_id`) USING BTREE,
    KEY `idx_key` (`key`) USING BTREE,
    KEY `uri` (`uri`) USING BTREE,
    KEY `httpStatus` (`http_status`) USING BTREE,
    KEY `create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='API 请求日志';

INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `icon`, `path`, `target_type`, `uri`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`)
VALUES (200400, 200000, 'API请求日志', 'bars', 'api/log', 1, 'log/api-access/ApiAccessLogPage', 4, 0, 0, 1, 0, NOW(), NULL);

INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `permission`, `target_type`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`)
VALUES (200401, 200400, 'API请求日志查询', 'api:access:log:read', 1, 2, 0, 0, 2, 0, NOW(), NULL)
;

INSERT INTO `sys_role_menu` (`role_code`, `menu_id`)
VALUES ('ROLE_ADMIN', 200400)
     , ('ROLE_ADMIN', 200401)
;