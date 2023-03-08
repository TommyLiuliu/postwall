/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : postwall-blog

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 22/02/2023 11:35:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_article
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '封面图片地址',
  `year` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发表年份',
  `month` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发表月份',
  `day` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发表日期',
  `publish_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '发表时间（yyyy-MM-dd）',
  `is_publish` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否发表',
  `is_comment` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否允许评论',
  `is_top` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶',
  `category_id` int(20) NOT NULL DEFAULT 0 COMMENT '分类 id',
  `link` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章链接',
  `read_num` int(11) NOT NULL DEFAULT 0 COMMENT '阅读数',
  `praise_num` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_num` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',
  `top_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '置顶时间',
  `is_delete` tinyint(4) NULL DEFAULT NULL COMMENT '是否删除',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_title`(`title`) USING BTREE,
  INDEX `idx_link`(`link`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_article
-- ----------------------------

-- ----------------------------
-- Table structure for blog_article_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_comment`;
CREATE TABLE `blog_article_comment`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` int(10) NOT NULL DEFAULT 0 COMMENT '文章 id',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父级评论 id',
  `banner_id` int(11) NOT NULL DEFAULT 0 COMMENT '面板 id',
  `user_id` int(10) NOT NULL DEFAULT 0 COMMENT '评论用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `is_delete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `praise_num` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_banner_id`(`banner_id`) USING BTREE,
  INDEX `idx_create_time`(`comment_time`) USING BTREE,
  INDEX `idx_p_id`(`parent_id`) USING BTREE,
  INDEX `idx_post_id`(`article_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_article_comment
-- ----------------------------

-- ----------------------------
-- Table structure for blog_article_praise
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_praise`;
CREATE TABLE `blog_article_praise`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `praise_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_article_praise
-- ----------------------------

-- ----------------------------
-- Table structure for blog_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `article_id` int(10) NOT NULL DEFAULT 0 COMMENT '文章 id',
  `tag_id` int(10) NOT NULL DEFAULT 0 COMMENT '标签 id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post_id`(`article_id`) USING BTREE,
  INDEX `idx_tag_id`(`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章标签关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for blog_category
-- ----------------------------
DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '封面地址',
  `state` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 1：可用 0：禁用',
  `sort` tinyint(4) NOT NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `is_delete` tinyint(4) NULL DEFAULT NULL COMMENT '是否删除',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_category
-- ----------------------------

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `is_delete` tinyint(4) NULL DEFAULT NULL COMMENT '是否删除',
  `create_user` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_tag
-- ----------------------------

-- ----------------------------
-- Table structure for blog_user
-- ----------------------------
DROP TABLE IF EXISTS `blog_user`;
CREATE TABLE `blog_user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `nickname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邮箱地址',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `is_delete` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否删除',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '2' COMMENT '角色 admin = 管理员  user = 用户',
  `last_time_login` datetime NULL DEFAULT NULL COMMENT '最近登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_user
-- ----------------------------
INSERT INTO `blog_user` VALUES (4, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '775957025@qq.com', '17776254919', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', 0, 'sysadmin', '2023-02-22 09:23:48', '2023-01-01 14:23:35', '2023-01-01 14:28:19');

-- ----------------------------
-- Table structure for param_config
-- ----------------------------
DROP TABLE IF EXISTS `param_config`;
CREATE TABLE `param_config`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数名',
  `config_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '参数值',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_config_key`(`config_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of param_config
-- ----------------------------
INSERT INTO `param_config` VALUES (1, 'USER_DEFAULT_AVATAR', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', '用户默认头像', '2022-12-11 02:34:13', '2022-12-11 02:34:13');
INSERT INTO `param_config` VALUES (2, 'IMAGES_PATH', 'D:\\我的项目\\POSTWALL-BLOG\\images', '图片存放地址', '2022-12-11 11:57:57', '2022-12-11 11:57:57');
INSERT INTO `param_config` VALUES (3, 'CATEGORY_DEFAULT_COVER', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', '分类默认地址', '2022-12-11 12:18:38', '2022-12-11 12:18:38');

SET FOREIGN_KEY_CHECKS = 1;
