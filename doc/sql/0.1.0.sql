CREATE TABLE `lingting_payment_config`
(
    `id`                 int(11) PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
    `mark`               varchar(50) NOT NULL COMMENT '标志',
    `third_part`         VARCHAR(10) NOT NULL COMMENT '第三方',
    `ali_app_id`         varchar(32)   DEFAULT '' COMMENT 'ali_app_id',
    `ali_private_key`    varchar(3248) DEFAULT '' COMMENT 'ali_private_key',
    `ali_pay_public_key` VARCHAR(786)  DEFAULT '' COMMENT 'ali_pay_public_key',
    `ali_format`         VARCHAR(10)   DEFAULT 'json' COMMENT 'ali_format',
    `ali_charset`        VARCHAR(10)   DEFAULT 'utf-8' COMMENT 'ali_charset',
    `ali_sign_type`      varchar(10)   DEFAULT 'RSA2' COMMENT 'ali_sign_type',
    `wx_app_id`          VARCHAR(36)   DEFAULT '' COMMENT 'wx_app_id',
    `wx_mch_id`          VARCHAR(20)   DEFAULT '' COMMENT 'wx_mch_id',
    `wx_mch_key`         VARCHAR(64)   DEFAULT '' COMMENT 'wx_mch_key',
    `deleted`            bigint(20)    DEFAULT 0 COMMENT '逻辑删除标识，未删除为 0，已删除为删除时间',
    KEY `idx_third` (`third_part`) USING BTREE,
    -- 同一标志下, 同一第三方仅允许一个支付配置
    UNIQUE KEY `uk_mark_third` (`mark`, `third_part`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='第三方支付配置';

RENAME TABLE `notify` TO `lingting_payment_notify`;
RENAME TABLE `notify_log` TO `lingting_payment_notify_log`;
RENAME TABLE `pay` TO `lingting_payment_pay`;
RENAME TABLE `project` TO `lingting_payment_project`;
RENAME TABLE `project_history` TO `lingting_payment_project_history`;
RENAME TABLE `virtual_address` TO `lingting_payment_virtual_address`;

ALTER TABLE `lingting_payment_project`
    DROP COLUMN `mode`;
ALTER TABLE `lingting_payment_virtual_address`
    DROP COLUMN `mode`;

UPDATE lingting_payment_virtual_address
SET project_ids = (SELECT CONCAT('[', GROUP_CONCAT(p.id), ']') FROM lingting_payment_project p);