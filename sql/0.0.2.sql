ALTER TABLE project
    ADD COLUMN `mode` varchar(20) DEFAULT 'ALLOW' COMMENT '模式';

ALTER TABLE virtual_address
    ADD COLUMN `mode`        varchar(20) DEFAULT 'EXCLUDE' COMMENT '模式',
    ADD COLUMN `project_ids` json COMMENT '项目ids'
;

INSERT INTO `sys_dict`(code, title, remarks, editable, value_type, hash_code, deleted, create_time)
VALUES ('project_mode', '项目模式', '', 1, 2, '233', 0, NOW())
     , ('virtual_address_mode', '地址模式', '', 1, 2, '233', 0, NOW())
;

INSERT INTO `sys_dict_item`(dict_code, value, name, attributes, sort, remarks, deleted, create_time)
VALUES ('project_mode', 'ALLOW', '允许使用所有可用地址', '{}', 0, '', 0, NOW())
     , ('project_mode', 'INCLUDE', '仅能使用允许本项目使用的地址', '{}', 0, '', 0, NOW())
     , ('virtual_address_mode', 'EXCLUDE', '禁止所选项目使用', '{}', 0, '', 0, NOW())
     , ('virtual_address_mode', 'INCLUDE', '仅允许所选项目使用', '{}', 0, '', 0, NOW())
;

INSERT INTO `sys_lov` (`keyword`, `title`, `url`, `method`, `position`, `key`, `fixed_params`, `multiple`, `ret`, `ret_field`, `create_time`, `update_time`)
VALUES ('projects', '选择项目', '/project', 'GET', 'PARAMS', 'id', '{}', b'1', b'1', 'id', NOW(), NOW());

INSERT INTO `sys_lov_body` (`keyword`, `title`, `field`, `index`, `property`, `custom`, `html`, `create_time`)
VALUES ('projects', 'ID', 'id', 1, '{}', b'0', '', NOW())
     , ('projects', '项目名', 'name', 1, '{}', b'0', '', NOW())
     , ('projects', '创建时间', 'createTime', 1, '{}', b'0', '', NOW())
;

INSERT INTO `sys_config`
VALUES (NULL, '真实货币支付过期时间', 'real_expire_timeout', '1440', 'pay', '真实货币支付过期时间。单位: 分钟, 最小值为1, 小于1按120处理. 指定时间范围内未付款则本次交易失败!注: 支付模式为 TRANSFER 不受本值限制', 0, NOW(), NOW())
;

ALTER TABLE pay
    ADD INDEX `idx_chain` (`chain`),
    ADD INDEX `idx_third_part` (`third_part`),
    ADD INDEX `idx_mode` (`mode`);

ALTER TABLE project
    ADD COLUMN `mark` varchar(20) DEFAULT '' COMMENT '项目标志';

UPDATE project
SET project.mark=name
WHERE mark = '';

ALTER TABLE project
    ADD COLUMN `scope` json COMMENT '项目权限';

UPDATE project
SET project.scope='[
    "USDT"
]'
WHERE scope IS NULL
   OR project.scope = '[]';

INSERT INTO `sys_dict`(code, title, remarks, editable, value_type, hash_code, deleted, create_time)
VALUES ('project_scope', '项目权限', '', 1, 2, '233', 0, NOW())
;

INSERT INTO `sys_dict_item`(dict_code, value, name, attributes, sort, remarks, deleted, create_time)
VALUES ('project_scope', 'USDT', 'USDT', '{}', 0, '', 0, NOW())
     , ('project_scope', 'ALI', '支付宝', '{}', 0, '', 0, NOW())
     , ('project_scope', 'WX', '微信', '{}', 0, '', 0, NOW())
;

UPDATE sys_dict_item
SET attributes='{
    "tagColor": "red"
}'
WHERE dict_code = 'pay_status'
  AND `value` = 'FAIL';
UPDATE sys_dict_item
SET attributes='{
    "tagColor": "blue"
}'
WHERE dict_code = 'pay_status'
  AND `value` = 'SUCCESS';
UPDATE sys_dict_item
SET attributes='{
    "tagColor": "orange"
}'
WHERE dict_code = 'pay_status'
  AND `value` = 'RETRY';
UPDATE sys_dict_item
SET attributes='{
    "tagColor": "green"
}'
WHERE dict_code = 'pay_status'
  AND `value` = 'WAIT';
UPDATE sys_dict_item
SET attributes='{
    "tagColor": "blue"
}'
WHERE dict_code = 'notify_status'
  AND `value` = 'SUCCESS';

ALTER TABLE virtual_address
    ADD COLUMN `usdt_amount` decimal(24, 4) DEFAULT 0 COMMENT 'USDT 余额';