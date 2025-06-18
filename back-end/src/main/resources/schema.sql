-- AI对话表
CREATE TABLE IF NOT EXISTS `tb_conversations` (
    `id` int NOT NULL AUTO_INCREMENT,
    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话标题',
    `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '对话总结/摘要',
    `user_id` int NOT NULL COMMENT '用户ID',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `last_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '最后一条消息预览',
    `total_messages` int DEFAULT 0 COMMENT '消息总数',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE,
    CONSTRAINT `fk_conversation_user` FOREIGN KEY (`user_id`) REFERENCES `tb_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI对话表';

-- AI对话消息表
CREATE TABLE IF NOT EXISTS `tb_messages` (
    `id` int NOT NULL AUTO_INCREMENT,
    `conversation_id` int NOT NULL COMMENT '对话ID',
    `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色（user或assistant）',
    `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
    `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    `token_count` int DEFAULT 0 COMMENT 'token数量（可用于计费）',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_conversation_id` (`conversation_id`) USING BTREE,
    CONSTRAINT `fk_message_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `tb_conversations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI对话消息表';

-- AI对话标签表（用于分类和搜索对话）
CREATE TABLE IF NOT EXISTS `tb_conversation_tags` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
    `user_id` int NOT NULL COMMENT '创建用户ID',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_name_user` (`name`, `user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI对话标签表';

-- AI对话-标签关联表
CREATE TABLE IF NOT EXISTS `tb_conversation_tag_relations` (
    `id` int NOT NULL AUTO_INCREMENT,
    `conversation_id` int NOT NULL COMMENT '对话ID',
    `tag_id` int NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_conversation_tag` (`conversation_id`, `tag_id`) USING BTREE,
    CONSTRAINT `fk_tag_relation_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `tb_conversations` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_tag_relation_tag` FOREIGN KEY (`tag_id`) REFERENCES `tb_conversation_tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI对话-标签关联表'; 