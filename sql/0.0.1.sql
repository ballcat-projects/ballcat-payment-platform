INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `permission`, `target_type`, `sort`, `keep_alive`, `hidden`, `type`, `deleted`, `create_time`, `update_time`)
VALUES (200202, 200200, '支付信息强制重试', 'pay:forcibly:retry', 1, 2, 0, 0, 2, 0, NOW(), NULL)
     , (200203, 200200, '支付信息强制失败', 'pay:forcibly:fail', 1, 3, 0, 0, 2, 0, NOW(), NULL)
;

INSERT INTO `sys_role_menu` (`role_code`, `menu_id`)
VALUES ('ROLE_ADMIN', 200202)
     , ('ROLE_ADMIN', 200203)
;

