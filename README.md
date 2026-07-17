# 基于区块链的健康档案管理系统 (EMR)

## 项目简介

一个基于区块链技术的健康档案管理系统，采用前后端分离架构。

## 技术栈

### 后端 (Backend)
- **框架**: Spring Boot 3.2.0
- **语言**: Java 17
- **ORM**: MyBatis-Plus 3.5.5
- **安全**: Spring Security + JWT
- **数据库**: MySQL
- **构建工具**: Maven

### 前端 (Frontend)
- **框架**: Vue 2.6
- **UI 组件**: Element UI
- **状态管理**: Vuex
- **路由**: Vue Router
- **HTTP 客户端**: Axios
- **构建工具**: Vue CLI

## 项目结构

`
vue01/
├── backend/          # Spring Boot 后端服务
│   ├── src/main/java/com/erm/backend/
│   │   ├── controller/     # REST API 控制器
│   │   ├── service/        # 业务逻辑层
│   │   ├── entity/         # 数据实体
│   │   ├── mapper/         # MyBatis Mapper
│   │   ├── configuration/  # 配置类
│   │   ├── security/       # JWT 认证
│   │   └── VO/             # 视图对象
│   └── src/main/resources/
│       ├── application.yml # 应用配置
│       └── schema.sql      # 数据库初始化脚本
├── erm/              # Vue 2 前端应用
│   ├── src/
│   │   ├── views/          # 页面组件
│   │   │   ├── patient/    # 患者端页面
│   │   │   └── doctor/     # 医生端页面
│   │   ├── router/         # 路由配置
│   │   ├── store/          # Vuex 状态管理
│   │   ├── plugins/        # Axios/Element UI 插件
│   │   └── App.vue
│   └── public/
└── README.md
`

## 功能模块

- **患者端**: 注册登录、健康档案管理、预约管理、授权管理
- **医生端**: 病历查看、患者授权审核、数据管理
- **系统管理**: 用户认证、JWT Token 管理

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+

### 后端启动
`ash
cd backend
./mvnw spring-boot:run
# 服务运行在 http://localhost:8181
`

### 前端启动
`ash
cd erm
npm install
npm run serve
# 访问 http://localhost:8080
`

### 数据库初始化
项目启动时会自动执行 schema.sql 初始化数据库表结构和基础数据。

## 配置说明

后端数据库连接配置在 ackend/src/main/resources/application.yml:
- 默认地址: jdbc:mysql://localhost:3306/emr
- 默认账号: oot
- 默认密码: 123456

> **注意**: 生产环境请使用环境变量覆盖敏感配置，不要提交真实密码到代码仓库。

## License

MIT

## Development Guidelines

This project follows the [CLAUDE.md](CLAUDE.md) behavioral guidelines for coding best practices.


