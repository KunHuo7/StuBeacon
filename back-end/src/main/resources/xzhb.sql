/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80038 (8.0.38)
 Source Host           : localhost:3306
 Source Schema         : xzhb

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 18/06/2025 18:30:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity_participant
-- ----------------------------
DROP TABLE IF EXISTS `activity_participant`;
CREATE TABLE `activity_participant`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `activity_id` int NOT NULL,
  `user_id` int NOT NULL,
  `join_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_participant`(`activity_id` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for campus_activity
-- ----------------------------
DROP TABLE IF EXISTS `campus_activity`;
CREATE TABLE `campus_activity`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organizer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `max_participants` int NOT NULL,
  `current_participants` int NULL DEFAULT 0,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `user_id` int NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '学院ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学院名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学院信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `major_id` int NOT NULL COMMENT '所属专业ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '课程描述',
  `credits` int NULL DEFAULT NULL COMMENT '学分',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程分类（必修/选修）',
  `prerequisites` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '先修课程',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_course_major`(`major_id` ASC) USING BTREE,
  CONSTRAINT `fk_course_major` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '专业ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业名称',
  `college_id` int NOT NULL COMMENT '所属学院ID',
  `credit_requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '学分要求',
  `main_courses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '主要课程',
  `professional_field` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '专业领域方向',
  `career_features` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '职业特征',
  `employment_direction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '就业方向',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_major_college`(`college_id` ASC) USING BTREE,
  CONSTRAINT `fk_major_college` FOREIGN KEY (`college_id`) REFERENCES `college` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for major_image
-- ----------------------------
DROP TABLE IF EXISTS `major_image`;
CREATE TABLE `major_image`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `major_id` int NOT NULL COMMENT '所属专业ID',
  `image_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片类型',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序顺序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片描述',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_major_image_major_id`(`major_id` ASC) USING BTREE,
  INDEX `idx_major_image_type`(`image_type` ASC) USING BTREE,
  CONSTRAINT `fk_major_image_major` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专业图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rp_like
-- ----------------------------
DROP TABLE IF EXISTS `rp_like`;
CREATE TABLE `rp_like`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `blogId` int NOT NULL,
  `likeTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `userId`, `blogId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rp_talk_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `rp_talk_comment_like`;
CREATE TABLE `rp_talk_comment_like`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL COMMENT '用户ID',
  `commentId` int NOT NULL COMMENT '评论ID',
  `likeTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_comment`(`userId` ASC, `commentId` ASC) USING BTREE,
  INDEX `idx_comment`(`commentId` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rp_talk_like
-- ----------------------------
DROP TABLE IF EXISTS `rp_talk_like`;
CREATE TABLE `rp_talk_like`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL COMMENT '用户ID',
  `talkId` int NOT NULL COMMENT '话题ID',
  `likeTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_talk`(`userId` ASC, `talkId` ASC) USING BTREE,
  INDEX `idx_talk`(`talkId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rp_talk_tag
-- ----------------------------
DROP TABLE IF EXISTS `rp_talk_tag`;
CREATE TABLE `rp_talk_tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `talkId` int NOT NULL COMMENT '话题ID',
  `tagId` int NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_talk_tag`(`talkId` ASC, `tagId` ASC) USING BTREE,
  INDEX `idx_tag`(`tagId` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rs_notification
-- ----------------------------
DROP TABLE IF EXISTS `rs_notification`;
CREATE TABLE `rs_notification`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `notiId` int NOT NULL,
  `isRead` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `userId`, `notiId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_activities
-- ----------------------------
DROP TABLE IF EXISTS `tb_activities`;
CREATE TABLE `tb_activities`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动标题',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动简介',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '活动详细内容',
  `activityTime` datetime NULL DEFAULT NULL COMMENT '活动时间',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动地点',
  `organizer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主办方',
  `coverImage` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `status` tinyint NULL DEFAULT 0 COMMENT '活动状态：0-未开始，1-进行中，2-已结束',
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_blogs
-- ----------------------------
DROP TABLE IF EXISTS `tb_blogs`;
CREATE TABLE `tb_blogs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `authorId` int NOT NULL,
  `updateDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `watches` int NULL DEFAULT 0,
  `favourites` int NULL DEFAULT 0,
  `likes` int NULL DEFAULT 0,
  `isPublic` tinyint NULL DEFAULT 0,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_conversation_tag_relations
-- ----------------------------
DROP TABLE IF EXISTS `tb_conversation_tag_relations`;
CREATE TABLE `tb_conversation_tag_relations`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `conversation_id` int NOT NULL COMMENT '对话ID',
  `tag_id` int NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_conversation_tag`(`conversation_id` ASC, `tag_id` ASC) USING BTREE,
  INDEX `fk_tag_relation_tag`(`tag_id` ASC) USING BTREE,
  CONSTRAINT `fk_tag_relation_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `tb_conversations` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_tag_relation_tag` FOREIGN KEY (`tag_id`) REFERENCES `tb_conversation_tags` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI对话-标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_conversation_tags
-- ----------------------------
DROP TABLE IF EXISTS `tb_conversation_tags`;
CREATE TABLE `tb_conversation_tags`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `user_id` int NOT NULL COMMENT '创建用户ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name_user`(`name` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI对话标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_conversations
-- ----------------------------
DROP TABLE IF EXISTS `tb_conversations`;
CREATE TABLE `tb_conversations`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话标题',
  `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对话总结/摘要',
  `user_id` int NOT NULL COMMENT '用户ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后一条消息预览',
  `total_messages` int NULL DEFAULT 0 COMMENT '消息总数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_conversation_user` FOREIGN KEY (`user_id`) REFERENCES `tb_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI对话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_courses
-- ----------------------------
DROP TABLE IF EXISTS `tb_courses`;
CREATE TABLE `tb_courses`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型：credit-学分要求，main-主要课程，relationship-课程关系图',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `imageUrl` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片URL（主要用于课程关系图）',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_messages
-- ----------------------------
DROP TABLE IF EXISTS `tb_messages`;
CREATE TABLE `tb_messages`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `conversation_id` int NOT NULL COMMENT '对话ID',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色（user或assistant）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `token_count` int NULL DEFAULT 0 COMMENT 'token数量（可用于计费）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_conversation_id`(`conversation_id` ASC) USING BTREE,
  CONSTRAINT `fk_message_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `tb_conversations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI对话消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_notification
-- ----------------------------
DROP TABLE IF EXISTS `tb_notification`;
CREATE TABLE `tb_notification`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `type` tinyint NULL DEFAULT NULL,
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_personal
-- ----------------------------
DROP TABLE IF EXISTS `tb_personal`;
CREATE TABLE `tb_personal`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sex` tinyint NULL DEFAULT NULL,
  `hobby` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `major` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` tinyint NULL DEFAULT NULL,
  `type` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入学日期',
  `userid` int NOT NULL,
  `introduction` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人简介',
  `contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_talk_comments
-- ----------------------------
DROP TABLE IF EXISTS `tb_talk_comments`;
CREATE TABLE `tb_talk_comments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `authorId` int NOT NULL COMMENT '评论作者ID',
  `talkId` int NOT NULL COMMENT '话题ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `parentId` int NULL DEFAULT NULL COMMENT '父评论ID，用于回复功能',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_talk`(`talkId` ASC) USING BTREE,
  INDEX `idx_author`(`authorId` ASC) USING BTREE,
  INDEX `idx_create_time`(`createTime` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_talk_favorites
-- ----------------------------
DROP TABLE IF EXISTS `tb_talk_favorites`;
CREATE TABLE `tb_talk_favorites`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `talkId` int NOT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_talk`(`userId` ASC, `talkId` ASC) USING BTREE,
  INDEX `idx_talk_favorites_userId`(`userId` ASC) USING BTREE,
  INDEX `idx_talk_favorites_talkId`(`talkId` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_talk_tags
-- ----------------------------
DROP TABLE IF EXISTS `tb_talk_tags`;
CREATE TABLE `tb_talk_tags`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `useCount` int NULL DEFAULT 0 COMMENT '使用次数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_talks
-- ----------------------------
DROP TABLE IF EXISTS `tb_talks`;
CREATE TABLE `tb_talks`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '话题标题',
  `authorId` int NOT NULL COMMENT '作者ID',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '话题内容',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `views` int NULL DEFAULT 0 COMMENT '浏览量',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `comments` int NULL DEFAULT 0 COMMENT '评论数',
  `tags` json NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-草稿，1-已发布，2-已删除',
  `favorites` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_author`(`authorId` ASC) USING BTREE,
  INDEX `idx_create_time`(`createTime` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tb_users
-- ----------------------------
DROP TABLE IF EXISTS `tb_users`;
CREATE TABLE `tb_users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mail` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `studentId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager` tinyint NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
