# SmartExam
考试题库系统(Question bank system)
# SmartExam 考试题库系统

SmartExam 是一款基于前后端分离架构开发的考试题库管理系统，旨在提供高效的题目管理、智能组卷及数据可视化分析功能。
### 🚀 快速开始

#### 1. 准备工作
* 确保本地已安装 **JDK 17+** 和 **Node.js**。
* 准备好 **MySQL** 数据库环境。

#### 2. 后端启动
1. 进入 `SmartExam` 目录。
2. 执行 `sql/schema.sql` 脚本初始化数据库表结构。
3. 修改 `src/main/resources/application.yml` 中的数据库账号和密码。
4. 运行以下命令启动服务（默认端口 8080）：
   ```bash
   mvn spring-boot:run

## 🌟 核心功能

* **课程与章节管理**：支持多级课程关联，实现“课程-章节”的精细化组织。
* **全功能题库**：支持单选、多选、判断等多种题型，实现题目与章节的级联绑定。
* **智能随机组卷**：基于随机算法，根据用户设定的课程、章节及题型比例快速生成试卷。
* **数据可视化看板**：利用 ECharts 展示题型占比及课程题量分布，直观掌握题库现状。
* **试卷导出**：支持组卷结果的预览与导出。

## 🛠️ 技术选型

### 后端 (SmartExam)
* **框架**：Spring Boot 3
* **持久层**：MyBatis-Plus
* **数据库**：MySQL 8.0
* **其他**：Lombok, Maven

### 前端 (smart-exam-web)
* **框架**：Vue 3 (Composition API)
* **UI 组件库**：Element Plus
* **图表库**：ECharts
* **网络请求**：Axios

## 📂 项目结构

```text
SmartExam/
├── SmartExam/            # 后端 Spring Boot 项目
│   ├── sql/              # 数据库初始化脚本
│   └── src/              # 后端源代码
├── smart-exam-web/       # 前端 Vue 项目
│   └── src/              # 前端源代码
└── README.md             # 项目说明文档
