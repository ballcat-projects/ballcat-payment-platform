ALTER TABLE project
    ADD COLUMN `mode` varchar(20) DEFAULT 'ALLOW' COMMENT '模式';

ALTER TABLE virtual_address
    ADD COLUMN `mode`        varchar(20) DEFAULT 'EXCLUDE' COMMENT '模式',
    ADD COLUMN `project_ids` json COMMENT '项目ids'
;

