# StuBeacon 学生与AI交互平台

## AI聊天功能

项目新增了与AI进行对话的功能，利用讯飞星火大模型API实现智能问答。

### 功能特点

- 支持多轮对话记忆
- 对话历史管理（创建、查询、删除）
- 实时响应
- 用户权限控制
- 前后端分离架构

### 后端接口设计

#### 对话管理

| 接口 | 方法 | 描述 |
| --- | --- | --- |
| `/api/chat/conversations` | GET | 获取用户的所有对话列表 |
| `/api/chat/conversations` | POST | 创建新对话 |
| `/api/chat/conversations/{id}` | DELETE | 删除对话 |

#### 消息管理

| 接口 | 方法 | 描述 |
| --- | --- | --- |
| `/api/chat/conversations/{id}/messages` | GET | 获取对话中的所有消息 |
| `/api/chat/conversations/{id}/messages` | POST | 发送消息并获取AI回复 |

### 数据库设计

#### conversation表

| 字段 | 类型 | 描述 |
| --- | --- | --- |
| id | INT | 主键 |
| title | VARCHAR(255) | 对话标题 |
| user_id | INT | 用户ID |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| last_message | VARCHAR(255) | 最后一条消息预览 |

#### message表

| 字段 | 类型 | 描述 |
| --- | --- | --- |
| id | INT | 主键 |
| conversation_id | INT | 对话ID |
| role | VARCHAR(50) | 角色（user或assistant） |
| content | TEXT | 消息内容 |
| timestamp | TIMESTAMP | 时间戳 |

### 使用的AI模型

- 讯飞星火大模型 V3.5
- API版本：wss://spark-api.xf-yun.com/v3.5/chat

## 部署说明

1. 确保MySQL数据库已创建所需表（见schema.sql）
2. 配置application-dev.yml中的数据库连接信息
3. 启动后端SpringBoot应用
4. 启动前端Vue应用

## 技术栈

### 后端
- SpringBoot
- MySQL
- WebSocket (讯飞星火API)
- JWT认证

### 前端
- Vue.js
- Axios
- Tailwind CSS
