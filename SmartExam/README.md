# SmartExam Backend

SmartExam 考试题库系统后端基础骨架，技术栈：

- Maven
- Spring Boot 3
- MyBatis-Plus
- MySQL

## 快速开始

1. 执行数据库脚本：`sql/schema.sql`
2. 修改 `src/main/resources/application.yml` 中数据库账号密码
3. 启动项目

```bash
mvn spring-boot:run
```

## 当前模块结构

- `common`：通用响应结构
- `config`：MyBatis-Plus 配置
- `module/system`：系统基础接口（健康检查）
- `module/question`：题库模块（题目查询示例）
- `module/exam`：试卷域基础实体与 Mapper

## 示例接口

- `GET /api/system/health`
- `GET /api/questions`
