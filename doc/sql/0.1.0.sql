CREATE TABLE `lingting_payment_config`
(
    `id`                 int(11) PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
    `mark`               varchar(50) NOT NULL COMMENT '标识',
    `third_part`         VARCHAR(10) NOT NULL COMMENT '第三方',
    `disabled`           tinyint(1)    DEFAULT 0 COMMENT '是否禁用',
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
    `create_time`        datetime(4) NOT NULL COMMENT '创建时间',
    KEY `idx_third` (`third_part`) USING BTREE,
    KEY `idx_mark` (`mark`) USING BTREE,
    -- 同一标识下, 同一第三方仅允许一个支付配置
    UNIQUE KEY `uk_mark_third` (`mark`, `third_part`, `deleted`) USING BTREE
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

ALTER TABLE lingting_payment_pay
    ADD COLUMN `config_mark` varchar(50) DEFAULT '' COMMENT '支付配置标识',
    ADD INDEX `idx_config` (`config_mark`);

ALTER TABLE lingting_payment_virtual_address
    DROP COLUMN `project_ids`;

CREATE TABLE `lingting_payment_virtual_address_project`
(
    `id`         int(11) AUTO_INCREMENT PRIMARY KEY,
    `va_id`      int(11) NOT NULL,
    `project_id` int(11) NOT NULL,
    UNIQUE KEY `uk_va_project` (`va_id`, `project_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci COMMENT ='中间表:虚拟货币地址-项目';

# 如果存在存储过程则删除
DROP PROCEDURE IF EXISTS scan_va;
# 定义新的结束符
DELIMITER $$
# 创建存储过程
CREATE PROCEDURE scan_va()
# 开始
BEGIN
    ##定义判断变量
    DECLARE _flag varchar(50);

    ## 定义查询变量
    DECLARE _cur CURSOR FOR
        SELECT va.id FROM lingting_payment_virtual_address va;

    ### 循环赋初始值
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET _flag = NULL;
    ### 打开
    OPEN _cur;
    ## 赋值
    FETCH _cur INTO _flag;
    ###循环判断
    WHILE (_flag IS NOT NULL)
        DO
            CALL scan_p(_flag);
            ## 赋值下一个游标
            FETCH _cur INTO _flag;
        END WHILE;
    ## 关闭
    CLOSE _cur;
# 结束, 需指定结束符
END$$

# 如果存在存储过程则删除
DROP PROCEDURE IF EXISTS scan_p;
# 创建存储过程
CREATE PROCEDURE scan_p(IN va_id varchar(50))
# 开始
BEGIN
    # 定义sql 流
    DECLARE _sql varchar(500);
    ##定义判断变量
    DECLARE _p_flag varchar(50);

    DECLARE _p_cur CURSOR FOR SELECT p.id FROM lingting_payment_project p;

    ### 循环赋初始值
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET _p_flag = NULL;
    ### 打开
    OPEN _p_cur;
    ## 赋值
    FETCH _p_cur INTO _p_flag;
    ###循环判断
    WHILE (_p_flag IS NOT NULL)
        DO
            SET @sql = CONCAT('insert into lingting_payment_virtual_address_project(va_id,project_id) value(', va_id, ',', _p_flag, ');');
            # 转为流?
            PREPARE _sql FROM @sql;
            # 执行sql
            EXECUTE _sql;
            # 解除分配
            DEALLOCATE PREPARE _sql;
            ## 赋值下一个游标
            FETCH _p_cur INTO _p_flag;
        END WHILE;
    ## 关闭
    CLOSE _p_cur;
# 结束, 需指定结束符
END$$

# 恢复结束符
DELIMITER ;
# 调用存储过程, 可以传入参数
CALL scan_va();
DROP PROCEDURE scan_va;
DROP PROCEDURE scan_p;

ALTER TABLE `lingting_payment_config`
    ADD COLUMN `create_by` varchar(50) DEFAULT '' COMMENT '创建人信息',
    ADD COLUMN `update_by` varchar(50) DEFAULT '' COMMENT '更新人信息';

ALTER TABLE `lingting_payment_project`
    ADD COLUMN `create_by` varchar(50) DEFAULT '' COMMENT '创建人信息',
    ADD COLUMN `update_by` varchar(50) DEFAULT '' COMMENT '更新人信息';

ALTER TABLE `lingting_payment_config`
    ADD COLUMN `bank_card` varchar(25) DEFAULT '' COMMENT '银行卡号';