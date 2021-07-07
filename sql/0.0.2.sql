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
