ALTER TABLE project
    ADD COLUMN `mode` varchar(20) DEFAULT 'ALLOW' COMMENT '模式';

ALTER TABLE virtual_address
    ADD COLUMN `mode`        varchar(20) DEFAULT 'EXCLUDE' COMMENT '模式',
    ADD COLUMN `project_ids` json COMMENT '项目ids'
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

ALTER TABLE virtual_address
    ADD COLUMN `usdt_amount` decimal(24, 4) DEFAULT 0 COMMENT 'USDT 余额';
