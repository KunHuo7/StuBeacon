-- 创建学院表
CREATE TABLE IF NOT EXISTS `college` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学院ID',
  `name` varchar(100) NOT NULL COMMENT '学院名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院信息表';

-- 创建专业表
CREATE TABLE IF NOT EXISTS `major` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '专业ID',
  `name` varchar(100) NOT NULL COMMENT '专业名称',
  `college_id` int(11) NOT NULL COMMENT '所属学院ID',
  `credit_requirements` text COMMENT '学分要求',
  `main_courses` text COMMENT '主要课程',
  PRIMARY KEY (`id`),
  KEY `fk_major_college` (`college_id`),
  CONSTRAINT `fk_major_college` FOREIGN KEY (`college_id`) REFERENCES `college` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业信息表';

-- 创建课程表
CREATE TABLE IF NOT EXISTS `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `name` varchar(100) NOT NULL COMMENT '课程名称',
  `major_id` int(11) NOT NULL COMMENT '所属专业ID',
  `description` text COMMENT '课程描述',
  `credits` int(11) DEFAULT NULL COMMENT '学分',
  `category` varchar(50) DEFAULT NULL COMMENT '课程分类（必修/选修）',
  `prerequisites` text COMMENT '先修课程',
  PRIMARY KEY (`id`),
  KEY `fk_course_major` (`major_id`),
  CONSTRAINT `fk_course_major` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表';

-- 插入示例数据：学院
INSERT INTO `college` (`name`) VALUES 
('计算机与软件工程学院'),
('电气与电子工程学院'),
('机械工程学院'),
('管理工程学院'),
('艺术设计学院');

-- 插入示例数据：专业（部分示例）
INSERT INTO `major` (`name`, `college_id`, `credit_requirements`, `main_courses`) VALUES 
('计算机科学与技术', 1, '总学分要求：160学分\n专业必修课：80学分\n专业选修课：40学分\n公共基础课：40学分', '程序设计基础、数据结构、计算机组成原理、操作系统、计算机网络、数据库系统、软件工程'),
('软件工程', 1, '总学分要求：158学分\n专业必修课：78学分\n专业选修课：40学分\n公共基础课：40学分', '程序设计、面向对象编程、软件工程导论、需求分析与设计、软件测试、软件体系结构、项目管理'),
('电气工程及其自动化', 2, '总学分要求：165学分\n专业必修课：85学分\n专业选修课：40学分\n公共基础课：40学分', '电路理论、电机学、电力电子技术、自动控制原理、电力系统分析、高电压技术、电气控制与PLC');

-- 插入示例数据：课程（部分示例）
INSERT INTO `course` (`name`, `major_id`, `description`, `credits`, `category`, `prerequisites`) VALUES 
('程序设计基础', 1, '介绍C语言程序设计的基本概念和方法，培养学生的程序设计思维和能力。', 4, '必修', NULL),
('数据结构', 1, '研究数据的逻辑结构、存储结构及其基本操作的实现，是计算机专业的核心课程。', 4, '必修', '程序设计基础'),
('计算机组成原理', 1, '介绍计算机系统的基本组成和工作原理，包括CPU、存储器、输入输出设备等。', 4, '必修', NULL),
('软件工程导论', 2, '介绍软件工程的基本概念、原理和方法，培养学生的软件工程思想。', 3, '必修', NULL),
('需求分析与设计', 2, '介绍软件需求分析和设计的方法和技术，培养学生的系统分析和设计能力。', 4, '必修', '软件工程导论'),
('电路理论', 3, '研究电路的基本理论和分析方法，是电气工程专业的基础课程。', 5, '必修', NULL); 