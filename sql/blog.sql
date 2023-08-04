/*
Navicat MySQL Data Transfer

Source Server         : LZX
Source Server Version : 80030
Source Host           : localhost:3306
Source Database       : blog

Target Server Type    : MYSQL
Target Server Version : 80030
File Encoding         : 65001

Date: 2023-07-30 19:08:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(256) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '文章内容',
  `summary` varchar(1024) DEFAULT NULL COMMENT '文章摘要',
  `category_id` bigint DEFAULT NULL COMMENT '所属分类id',
  `thumbnail` varchar(256) DEFAULT NULL COMMENT '缩略图',
  `is_top` char(1) DEFAULT '0' COMMENT '是否置顶（0否，1是）',
  `status` char(1) DEFAULT '1' COMMENT '状态（0已发布，1草稿）',
  `view_count` bigint DEFAULT '0' COMMENT '访问量',
  `is_comment` char(1) DEFAULT '1' COMMENT '是否允许评论 1是，0否',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表';

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag` (
  `article_id` bigint NOT NULL COMMENT '文章id',
  `tag_id` bigint NOT NULL DEFAULT '0' COMMENT '标签id',
  PRIMARY KEY (`article_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章标签关联表';

-- ----------------------------
-- Records of article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '分类名',
  `pid` bigint DEFAULT '-1' COMMENT '父分类id，如果没有父分类为-1',
  `description` varchar(512) DEFAULT NULL COMMENT '描述',
  `status` char(1) DEFAULT '0' COMMENT '状态0:正常,1禁用',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类表';

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', 'JAVA', '-1', 'Java语言', '0', null, null, null, null, '0');
INSERT INTO `category` VALUES ('2', 'PHP', '-1', 'wsd', '0', null, null, null, null, '0');
INSERT INTO `category` VALUES ('15', 'Mybatis', '-1', '学习', '0', '1', '2023-07-29 13:50:41', '1', '2023-07-29 14:05:57', '0');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` char(1) DEFAULT '0' COMMENT '评论类型（0代表文章评论，1代表友链评论）',
  `article_id` bigint DEFAULT NULL COMMENT '文章id',
  `root_id` bigint DEFAULT '-1' COMMENT '根评论id',
  `content` varchar(512) DEFAULT NULL COMMENT '评论内容',
  `to_comment_user_id` bigint DEFAULT '-1' COMMENT '所回复的目标评论的userid',
  `to_comment_id` bigint DEFAULT '-1' COMMENT '回复目标评论id',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `logo` varchar(256) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL COMMENT '网站地址',
  `status` char(1) DEFAULT '2' COMMENT '审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='友链';

-- ----------------------------
-- Records of link
-- ----------------------------
INSERT INTO `link` VALUES ('4', 'bilibili', 'https://www.ivean.com/wp-content/uploads/2020/10/1.png', 'B站哟', 'https://www.bilibili.com/', '0', '1', '2023-07-29 22:42:07', '1', '2023-07-29 22:55:50', '0');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2035 DEFAULT CHARSET=utf8mb3 COMMENT='菜单权限表';

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '系统管理', '0', '1', 'system', null, '1', 'M', '0', '0', '', 'system', '0', '2021-11-12 10:46:19', '0', null, '系统管理目录', '0');
INSERT INTO `menu` VALUES ('100', '用户管理', '1', '1', 'user', 'system/user/index', '1', 'C', '0', '0', 'system:user:list', 'user', '0', '2021-11-12 10:46:19', '1', '2022-07-31 15:47:58', '用户管理菜单', '0');
INSERT INTO `menu` VALUES ('101', '角色管理', '1', '2', 'role', 'system/role/index', '1', 'C', '0', '0', 'system:role:list', 'peoples', '0', '2021-11-12 10:46:19', '0', null, '角色管理菜单', '0');
INSERT INTO `menu` VALUES ('102', '菜单管理', '1', '3', 'menu', 'system/menu/index', '1', 'C', '0', '0', 'system:menu:list', 'tree-table', '0', '2021-11-12 10:46:19', '0', null, '菜单管理菜单', '0');
INSERT INTO `menu` VALUES ('1001', '用户查询', '100', '1', '', '', '1', 'F', '0', '0', 'system:user:query', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1002', '用户新增', '100', '2', '', '', '1', 'F', '0', '0', 'system:user:add', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1003', '用户修改', '100', '3', '', '', '1', 'F', '0', '0', 'system:user:edit', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1004', '用户删除', '100', '4', '', '', '1', 'F', '0', '0', 'system:user:remove', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1005', '用户导出', '100', '5', '', '', '1', 'F', '0', '0', 'system:user:export', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1006', '用户导入', '100', '6', '', '', '1', 'F', '0', '0', 'system:user:import', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1007', '重置密码', '100', '7', '', '', '1', 'F', '0', '0', 'system:user:resetPwd', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1008', '角色查询', '101', '1', '', '', '1', 'F', '0', '0', 'system:role:query', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1009', '角色新增', '101', '2', '', '', '1', 'F', '0', '0', 'system:role:add', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1010', '角色修改', '101', '3', '', '', '1', 'F', '0', '0', 'system:role:edit', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1011', '角色删除', '101', '4', '', '', '1', 'F', '0', '0', 'system:role:remove', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1012', '角色导出', '101', '5', '', '', '1', 'F', '0', '0', 'system:role:export', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1013', '菜单查询', '102', '1', '', '', '1', 'F', '0', '0', 'system:menu:query', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1014', '菜单新增', '102', '2', '', '', '1', 'F', '0', '0', 'system:menu:add', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1015', '菜单修改', '102', '3', '', '', '1', 'F', '0', '0', 'system:menu:edit', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('1016', '菜单删除', '102', '4', '', '', '1', 'F', '0', '0', 'system:menu:remove', '#', '0', '2021-11-12 10:46:19', '0', null, '', '0');
INSERT INTO `menu` VALUES ('2017', '内容管理', '0', '4', 'content', null, '1', 'M', '0', '0', null, 'table', null, '2022-01-08 02:44:38', '1', '2022-07-31 12:34:23', '', '0');
INSERT INTO `menu` VALUES ('2018', '分类管理', '2017', '1', 'category', 'content/category/index', '1', 'C', '0', '0', 'content:category:list', 'example', null, '2022-01-08 02:51:45', null, '2022-01-08 02:51:45', '', '0');
INSERT INTO `menu` VALUES ('2019', '文章管理', '2017', '0', 'article', 'content/article/index', '1', 'C', '0', '0', 'content:article:list', 'build', null, '2022-01-08 02:53:10', null, '2022-01-08 02:53:10', '', '0');
INSERT INTO `menu` VALUES ('2021', '标签管理', '2017', '6', 'tag', 'content/tag/index', '1', 'C', '0', '0', 'content:tag:index', 'button', null, '2022-01-08 02:55:37', null, '2022-01-08 02:55:50', '', '0');
INSERT INTO `menu` VALUES ('2022', '友链管理', '2017', '4', 'link', 'content/link/index', '1', 'C', '0', '0', 'content:link:list', '404', null, '2022-01-08 02:56:50', null, '2022-01-08 02:56:50', '', '0');
INSERT INTO `menu` VALUES ('2023', '写博文', '0', '0', 'write', 'content/article/write/index', '1', 'C', '0', '0', 'content:article:writer', 'build', null, '2022-01-08 03:39:58', '1', '2022-07-31 22:07:05', '', '0');
INSERT INTO `menu` VALUES ('2024', '友链新增', '2022', '0', '', null, '1', 'F', '0', '0', 'content:link:add', '#', null, '2022-01-16 07:59:17', null, '2022-01-16 07:59:17', '', '0');
INSERT INTO `menu` VALUES ('2025', '友链修改', '2022', '1', '', null, '1', 'F', '0', '0', 'content:link:edit', '#', null, '2022-01-16 07:59:44', null, '2022-01-16 07:59:44', '', '0');
INSERT INTO `menu` VALUES ('2026', '友链删除', '2022', '1', '', null, '1', 'F', '0', '0', 'content:link:remove', '#', null, '2022-01-16 08:00:05', null, '2022-01-16 08:00:05', '', '0');
INSERT INTO `menu` VALUES ('2027', '友链查询', '2022', '2', '', null, '1', 'F', '0', '0', 'content:link:query', '#', null, '2022-01-16 08:04:09', null, '2022-01-16 08:04:09', '', '0');
INSERT INTO `menu` VALUES ('2028', '导出分类', '2018', '1', '', null, '1', 'F', '0', '0', 'content:category:export', '#', null, '2022-01-21 07:06:59', null, '2022-01-21 07:06:59', '', '0');
INSERT INTO `menu` VALUES ('2034', '测试按钮', '2023', '2', '', null, '1', 'F', '0', '0', 'test:okok', '#', '1', '2023-07-28 11:04:02', '1', '2023-07-28 11:58:26', '', '1');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 COMMENT='角色信息表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员', 'admin', '1', '0', '0', '0', '2021-11-12 10:46:19', '0', null, '超级管理员');
INSERT INTO `role` VALUES ('2', '普通角色', 'common', '2', '0', '0', '0', '2021-11-12 10:46:19', '0', '2022-01-01 22:32:58', '普通角色');
INSERT INTO `role` VALUES ('12', '友链审核员', 'link', '1', '0', '0', null, '2022-01-16 06:49:30', null, '2022-01-16 08:05:09', null);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色和菜单关联表';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES ('0', '0');
INSERT INTO `role_menu` VALUES ('2', '1');
INSERT INTO `role_menu` VALUES ('2', '102');
INSERT INTO `role_menu` VALUES ('2', '1013');
INSERT INTO `role_menu` VALUES ('2', '1014');
INSERT INTO `role_menu` VALUES ('2', '1015');
INSERT INTO `role_menu` VALUES ('2', '1016');
INSERT INTO `role_menu` VALUES ('2', '2000');
INSERT INTO `role_menu` VALUES ('3', '2');
INSERT INTO `role_menu` VALUES ('3', '3');
INSERT INTO `role_menu` VALUES ('3', '4');
INSERT INTO `role_menu` VALUES ('3', '100');
INSERT INTO `role_menu` VALUES ('3', '101');
INSERT INTO `role_menu` VALUES ('3', '103');
INSERT INTO `role_menu` VALUES ('3', '104');
INSERT INTO `role_menu` VALUES ('3', '105');
INSERT INTO `role_menu` VALUES ('3', '106');
INSERT INTO `role_menu` VALUES ('3', '107');
INSERT INTO `role_menu` VALUES ('3', '108');
INSERT INTO `role_menu` VALUES ('3', '109');
INSERT INTO `role_menu` VALUES ('3', '110');
INSERT INTO `role_menu` VALUES ('3', '111');
INSERT INTO `role_menu` VALUES ('3', '112');
INSERT INTO `role_menu` VALUES ('3', '113');
INSERT INTO `role_menu` VALUES ('3', '114');
INSERT INTO `role_menu` VALUES ('3', '115');
INSERT INTO `role_menu` VALUES ('3', '116');
INSERT INTO `role_menu` VALUES ('3', '500');
INSERT INTO `role_menu` VALUES ('3', '501');
INSERT INTO `role_menu` VALUES ('3', '1001');
INSERT INTO `role_menu` VALUES ('3', '1002');
INSERT INTO `role_menu` VALUES ('3', '1003');
INSERT INTO `role_menu` VALUES ('3', '1004');
INSERT INTO `role_menu` VALUES ('3', '1005');
INSERT INTO `role_menu` VALUES ('3', '1006');
INSERT INTO `role_menu` VALUES ('3', '1007');
INSERT INTO `role_menu` VALUES ('3', '1008');
INSERT INTO `role_menu` VALUES ('3', '1009');
INSERT INTO `role_menu` VALUES ('3', '1010');
INSERT INTO `role_menu` VALUES ('3', '1011');
INSERT INTO `role_menu` VALUES ('3', '1012');
INSERT INTO `role_menu` VALUES ('3', '1017');
INSERT INTO `role_menu` VALUES ('3', '1018');
INSERT INTO `role_menu` VALUES ('3', '1019');
INSERT INTO `role_menu` VALUES ('3', '1020');
INSERT INTO `role_menu` VALUES ('3', '1021');
INSERT INTO `role_menu` VALUES ('3', '1022');
INSERT INTO `role_menu` VALUES ('3', '1023');
INSERT INTO `role_menu` VALUES ('3', '1024');
INSERT INTO `role_menu` VALUES ('3', '1025');
INSERT INTO `role_menu` VALUES ('3', '1026');
INSERT INTO `role_menu` VALUES ('3', '1027');
INSERT INTO `role_menu` VALUES ('3', '1028');
INSERT INTO `role_menu` VALUES ('3', '1029');
INSERT INTO `role_menu` VALUES ('3', '1030');
INSERT INTO `role_menu` VALUES ('3', '1031');
INSERT INTO `role_menu` VALUES ('3', '1032');
INSERT INTO `role_menu` VALUES ('3', '1033');
INSERT INTO `role_menu` VALUES ('3', '1034');
INSERT INTO `role_menu` VALUES ('3', '1035');
INSERT INTO `role_menu` VALUES ('3', '1036');
INSERT INTO `role_menu` VALUES ('3', '1037');
INSERT INTO `role_menu` VALUES ('3', '1038');
INSERT INTO `role_menu` VALUES ('3', '1039');
INSERT INTO `role_menu` VALUES ('3', '1040');
INSERT INTO `role_menu` VALUES ('3', '1041');
INSERT INTO `role_menu` VALUES ('3', '1042');
INSERT INTO `role_menu` VALUES ('3', '1043');
INSERT INTO `role_menu` VALUES ('3', '1044');
INSERT INTO `role_menu` VALUES ('3', '1045');
INSERT INTO `role_menu` VALUES ('3', '1046');
INSERT INTO `role_menu` VALUES ('3', '1047');
INSERT INTO `role_menu` VALUES ('3', '1048');
INSERT INTO `role_menu` VALUES ('3', '1049');
INSERT INTO `role_menu` VALUES ('3', '1050');
INSERT INTO `role_menu` VALUES ('3', '1051');
INSERT INTO `role_menu` VALUES ('3', '1052');
INSERT INTO `role_menu` VALUES ('3', '1053');
INSERT INTO `role_menu` VALUES ('3', '1054');
INSERT INTO `role_menu` VALUES ('3', '1055');
INSERT INTO `role_menu` VALUES ('3', '1056');
INSERT INTO `role_menu` VALUES ('3', '1057');
INSERT INTO `role_menu` VALUES ('3', '1058');
INSERT INTO `role_menu` VALUES ('3', '1059');
INSERT INTO `role_menu` VALUES ('3', '1060');
INSERT INTO `role_menu` VALUES ('3', '2000');
INSERT INTO `role_menu` VALUES ('11', '1');
INSERT INTO `role_menu` VALUES ('11', '100');
INSERT INTO `role_menu` VALUES ('11', '101');
INSERT INTO `role_menu` VALUES ('11', '102');
INSERT INTO `role_menu` VALUES ('11', '103');
INSERT INTO `role_menu` VALUES ('11', '104');
INSERT INTO `role_menu` VALUES ('11', '105');
INSERT INTO `role_menu` VALUES ('11', '106');
INSERT INTO `role_menu` VALUES ('11', '107');
INSERT INTO `role_menu` VALUES ('11', '108');
INSERT INTO `role_menu` VALUES ('11', '500');
INSERT INTO `role_menu` VALUES ('11', '501');
INSERT INTO `role_menu` VALUES ('11', '1001');
INSERT INTO `role_menu` VALUES ('11', '1002');
INSERT INTO `role_menu` VALUES ('11', '1003');
INSERT INTO `role_menu` VALUES ('11', '1004');
INSERT INTO `role_menu` VALUES ('11', '1005');
INSERT INTO `role_menu` VALUES ('11', '1006');
INSERT INTO `role_menu` VALUES ('11', '1007');
INSERT INTO `role_menu` VALUES ('11', '1008');
INSERT INTO `role_menu` VALUES ('11', '1009');
INSERT INTO `role_menu` VALUES ('11', '1010');
INSERT INTO `role_menu` VALUES ('11', '1011');
INSERT INTO `role_menu` VALUES ('11', '1012');
INSERT INTO `role_menu` VALUES ('11', '1013');
INSERT INTO `role_menu` VALUES ('11', '1014');
INSERT INTO `role_menu` VALUES ('11', '1015');
INSERT INTO `role_menu` VALUES ('11', '1016');
INSERT INTO `role_menu` VALUES ('11', '1017');
INSERT INTO `role_menu` VALUES ('11', '1018');
INSERT INTO `role_menu` VALUES ('11', '1019');
INSERT INTO `role_menu` VALUES ('11', '1020');
INSERT INTO `role_menu` VALUES ('11', '1021');
INSERT INTO `role_menu` VALUES ('11', '1022');
INSERT INTO `role_menu` VALUES ('11', '1023');
INSERT INTO `role_menu` VALUES ('11', '1024');
INSERT INTO `role_menu` VALUES ('11', '1025');
INSERT INTO `role_menu` VALUES ('11', '1026');
INSERT INTO `role_menu` VALUES ('11', '1027');
INSERT INTO `role_menu` VALUES ('11', '1028');
INSERT INTO `role_menu` VALUES ('11', '1029');
INSERT INTO `role_menu` VALUES ('11', '1030');
INSERT INTO `role_menu` VALUES ('11', '1031');
INSERT INTO `role_menu` VALUES ('11', '1032');
INSERT INTO `role_menu` VALUES ('11', '1033');
INSERT INTO `role_menu` VALUES ('11', '1034');
INSERT INTO `role_menu` VALUES ('11', '1035');
INSERT INTO `role_menu` VALUES ('11', '1036');
INSERT INTO `role_menu` VALUES ('11', '1037');
INSERT INTO `role_menu` VALUES ('11', '1038');
INSERT INTO `role_menu` VALUES ('11', '1039');
INSERT INTO `role_menu` VALUES ('11', '1040');
INSERT INTO `role_menu` VALUES ('11', '1041');
INSERT INTO `role_menu` VALUES ('11', '1042');
INSERT INTO `role_menu` VALUES ('11', '1043');
INSERT INTO `role_menu` VALUES ('11', '1044');
INSERT INTO `role_menu` VALUES ('11', '1045');
INSERT INTO `role_menu` VALUES ('11', '2000');
INSERT INTO `role_menu` VALUES ('11', '2003');
INSERT INTO `role_menu` VALUES ('11', '2004');
INSERT INTO `role_menu` VALUES ('11', '2005');
INSERT INTO `role_menu` VALUES ('11', '2006');
INSERT INTO `role_menu` VALUES ('11', '2007');
INSERT INTO `role_menu` VALUES ('11', '2008');
INSERT INTO `role_menu` VALUES ('11', '2009');
INSERT INTO `role_menu` VALUES ('11', '2010');
INSERT INTO `role_menu` VALUES ('11', '2011');
INSERT INTO `role_menu` VALUES ('11', '2012');
INSERT INTO `role_menu` VALUES ('11', '2013');
INSERT INTO `role_menu` VALUES ('11', '2014');
INSERT INTO `role_menu` VALUES ('12', '2017');
INSERT INTO `role_menu` VALUES ('12', '2022');
INSERT INTO `role_menu` VALUES ('12', '2024');
INSERT INTO `role_menu` VALUES ('12', '2025');
INSERT INTO `role_menu` VALUES ('12', '2026');
INSERT INTO `role_menu` VALUES ('12', '2027');
INSERT INTO `role_menu` VALUES ('13', '1');
INSERT INTO `role_menu` VALUES ('13', '100');
INSERT INTO `role_menu` VALUES ('13', '101');
INSERT INTO `role_menu` VALUES ('13', '1001');
INSERT INTO `role_menu` VALUES ('13', '1008');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '标签名',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签';

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `type` char(1) DEFAULT '0' COMMENT '用户类型：0代表普通用户，1代表管理员',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `phonenumber` varchar(32) DEFAULT NULL COMMENT '手机号',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
  `create_by` bigint DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14787164048667 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'violet', '小卡比', '$2a$10$5kjxQkejOJkQrvLBwApKYODhYumucXBwfnQ4TtIpf.s5Fpj/DYDH6', '1', '0', '1317095643@qq.com', '17620811317', '1', 'http://ry89du3in.hn-bkt.clouddn.com/2023/07/23/f61f3169656b429391778c0848733a8a.jpg', null, '2022-01-05 09:01:56', '1', '2023-07-24 11:18:29', '0');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
