-- 从学院表移除description字段
ALTER TABLE `college` DROP COLUMN `description`;

-- 从专业表移除description字段
ALTER TABLE `major` DROP COLUMN `description`;

-- 更新插入语句，不再包含description字段
-- 清空原有的学院表数据
TRUNCATE TABLE `college`;

-- 重新插入学院数据，不包含description
INSERT INTO `college` (`name`) VALUES 
('计算机与软件工程学院'),
('电气与电子工程学院'),
('机械工程学院'),
('管理工程学院'),
('艺术设计学院');

-- 清空原有的专业表数据
-- 注意：由于外键约束，需要先处理依赖表
-- 首先删除major_image表中的数据
TRUNCATE TABLE `major_image`;
-- 然后删除course表中的数据
TRUNCATE TABLE `course`;
-- 最后删除major表中的数据
TRUNCATE TABLE `major`;

-- 重新插入专业数据，不包含description
INSERT INTO `major` (`name`, `college_id`, `credit_requirements`, `main_courses`) VALUES 
('计算机科学与技术', 1, '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分', '程序设计基础、数据结构、计算机组成原理、操作系统、计算机网络、数据库系统、软件工程'),
('软件工程', 1, '总学分要求：158学分\n专业必修课：78学分\n专业选修课：40学分\n公共基础课：40学分', '程序设计、面向对象编程、软件工程导论、需求分析与设计、软件测试、软件体系结构、项目管理'),
('电气工程及其自动化', 2, '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分', '电路理论、电机学、电力电子技术、自动控制原理、电力系统分析、高电压技术、电气控制与PLC');

-- 重新插入课程数据
INSERT INTO `course` (`name`, `major_id`, `description`, `credits`, `category`, `prerequisites`) VALUES 
('程序设计基础', 1, '介绍C语言程序设计的基本概念和方法，培养学生的程序设计思维和能力。', 4, '必修', NULL),
('数据结构', 1, '研究数据的逻辑结构、存储结构及其基本操作的实现，是计算机专业的核心课程。', 4, '必修', '程序设计基础'),
('计算机组成原理', 1, '介绍计算机系统的基本组成和工作原理，包括CPU、存储器、输入输出设备等。', 4, '必修', NULL),
('软件工程导论', 2, '介绍软件工程的基本概念、原理和方法，培养学生的软件工程思想。', 3, '必修', NULL),
('需求分析与设计', 2, '介绍软件需求分析和设计的方法和技术，培养学生的系统分析和设计能力。', 4, '必修', '软件工程导论'),
('电路理论', 3, '研究电路的基本理论和分析方法，是电气工程专业的基础课程。', 5, '必修', NULL);

-- 重新插入专业图片数据
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