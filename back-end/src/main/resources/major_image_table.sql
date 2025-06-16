-- 创建专业图片表
CREATE TABLE IF NOT EXISTS `major_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `major_id` int(11) NOT NULL COMMENT '所属专业ID',
  `image_type` varchar(50) NOT NULL COMMENT '图片类型',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序顺序',
  `description` varchar(255) DEFAULT NULL COMMENT '图片描述',
  PRIMARY KEY (`id`),
  KEY `idx_major_image_major_id` (`major_id`),
  KEY `idx_major_image_type` (`image_type`),
  CONSTRAINT `fk_major_image_major` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业图片表';

-- 插入样例数据
INSERT INTO `major_image` (`major_id`, `image_type`, `image_url`, `sort_order`, `description`) VALUES
(1, 'CREDIT_REQUIREMENT', 'uploads/course_structure/cr_cs_1.jpg', 1, '计算机科学与技术学分要求图1'),
(1, 'CREDIT_REQUIREMENT', 'uploads/course_structure/cr_cs_2.jpg', 2, '计算机科学与技术学分要求图2'),
(1, 'MAIN_COURSE', 'uploads/course_structure/mc_cs_1.jpg', 1, '计算机科学与技术主要课程图1'),
(1, 'STRUCTURE', 'uploads/course_structure/st_cs_1.jpg', 1, '计算机科学与技术课程关系图1'),
(2, 'CREDIT_REQUIREMENT', 'uploads/course_structure/cr_se_1.jpg', 1, '软件工程学分要求图1'),
(2, 'MAIN_COURSE', 'uploads/course_structure/mc_se_1.jpg', 1, '软件工程主要课程图1'),
(2, 'STRUCTURE', 'uploads/course_structure/st_se_1.jpg', 1, '软件工程课程关系图1'),
(3, 'CREDIT_REQUIREMENT', 'uploads/course_structure/cr_ee_1.jpg', 1, '电气工程学分要求图1'),
(3, 'MAIN_COURSE', 'uploads/course_structure/mc_ee_1.jpg', 1, '电气工程主要课程图1'),
(3, 'STRUCTURE', 'uploads/course_structure/st_ee_1.jpg', 1, '电气工程课程关系图1');

-- 修改major表，移除structure_image字段
ALTER TABLE `major` DROP COLUMN `structure_image`; 