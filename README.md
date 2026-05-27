# 🏥 智慧护理平台 (Smart Nursing Platform)

基于 **Spring Boot 3 + Vue 3** 构建的智慧养老护理管理平台，提供在线预约、护理服务、居家病床、服务订单、AI 问诊等核心功能，支持管理端与用户端双角色分离。

---

## 📸 项目结果展示

### 核心功能模块

| 模块 | 描述 | 预期输出 |
|------|------|----------|
| **用户注册/登录** | 角色分离的登录体系，管理员与普通用户拥有不同菜单权限 | 管理员可见全部功能（信息/业务/系统管理），用户仅见预约/护理/AI功能 |
| **在线预约** | 选择服务类型→填写老人信息→选择时间→确认提交 | 生成待处理预约记录，侧栏实时展示预约列表 |
| **护理服务** | 按分类浏览护理项目，查看详情后一键跳转预约 | 服务卡片展示名称/价格/时长/标签，点击"立即预约"自动匹配服务类型 |
| **居家病床** | 新增/删除病床，安排入住/办理退房，护理记录查看 | 统计卡片实时更新总数/占用/空闲/待入住，状态筛选与搜索 |
| **服务订单** | 从预约确认自动生成订单，支持接单→服务中→已完成状态流转 | 订单编号自动生成，金额按服务类型自动计算，状态同步更新关联预约 |
| **AI 智能问诊** | 集成 DeepSeek 大模型，提供智能健康咨询问答 | 实时对话式交互，返回结构化健康建议 |

### 业务流程图

```
用户注册 → 登录 → 浏览护理服务 → 在线预约 → 管理员确认预约
                                                    ↓
                                          自动生成服务订单（含金额）
                                                    ↓
                                    待接单 → 接单 → 服务中 → 完成
                                    预约状态与订单状态双向同步
```

---

## 🚀 快速上手

### 环境依赖

| 环境 | 最低版本 | 说明 |
|------|----------|------|
| **JDK** | 17+ | 后端运行环境 |
| **Node.js** | 18+ | 前端构建工具链 |
| **MySQL** | 8.0+ | 数据库，需创建数据库 `shop` |
| **Maven** | 3.8+ | 后端构建（项目内置 Maven Wrapper） |

### 1. 克隆项目

```bash
git clone <your-repo-url>
cd smart-nursing-platform
```

### 2. 数据库配置

确保 MySQL 服务已启动，创建数据库并导入初始化脚本（如有）：

```sql
CREATE DATABASE IF NOT EXISTS shop DEFAULT CHARACTER SET utf8mb4;
```

修改 `backend/src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    username: root
    password: your-password          # 替换为你的 MySQL 密码
    url: jdbc:mysql://localhost:3306/shop?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
```

同时配置 AI 接口密钥（如需使用 AI 问诊功能）：

```yaml
spring:
  ai:
    openai:
      api-key: your-api-key          # 替换为你的 DeepSeek API Key
```

### 3. 启动后端

```bash
cd backend

# Windows
mvnw spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

后端默认启动在 **http://localhost:8080**。

### 4. 启动前端

```bash
cd frontend
npm install        # 首次运行需安装依赖
npm run dev
```

前端默认启动在 **http://localhost:5173**。

### 5. 访问系统

| 角色 | 地址 | 账号 | 密码 |
|------|------|------|------|
| 前端页面 | http://localhost:5173 | — | — |
| 管理员 | 登录后自动跳转 | `admin` | `12345` |
| 普通用户 | 点击注册创建 | 自行注册 | 自行设置 |

---

## 📁 项目结构

```
smart-nursing-platform/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/example/demo/
│   │   ├── controller/               # 接口控制器
│   │   │   ├── UserController.java           # 用户登录/注册
│   │   │   ├── AppointmentController.java    # 预约管理（含确认生成订单）
│   │   │   ├── OrderController.java          # 服务订单管理（状态同步）
│   │   │   ├── BedController.java            # 居家病床管理
│   │   │   ├── NursingServiceController.java # 护理服务列表
│   │   │   └── ...
│   │   ├── entity/                   # 数据实体
│   │   ├── service/                  # 业务逻辑层
│   │   ├── mapper/                   # MyBatis 映射接口
│   │   └── config/                   # 配置类
│   ├── src/main/resources/
│   │   ├── application.yml           # 主配置文件
│   │   └── mappers/                  # MyBatis XML 映射
│   └── pom.xml                       # Maven 依赖
│
├── frontend/                         # Vue 3 前端
│   ├── src/
│   │   ├── pages/                    # 页面组件
│   │   │   ├── LoginPage.vue         # 登录
│   │   │   ├── RegisterPage.vue      # 注册
│   │   │   ├── MainPage.vue          # 管理端布局
│   │   │   ├── UserHomePage.vue      # 用户端布局
│   │   │   ├── appointment/          # 在线预约
│   │   │   ├── nursing/              # 护理服务
│   │   │   ├── service/              # 居家病床
│   │   │   ├── order/                # 服务订单管理
│   │   │   ├── ai/                   # AI 问诊
│   │   │   └── ...
│   │   ├── router/index.js           # 路由配置（含角色守卫）
│   │   └── assets/                   # 静态资源
│   ├── package.json
│   └── vite.config.js                # Vite 配置（含代理）
│
└── README.md
```

---

## ⚙️ 配置说明

### 前端代理配置

`frontend/vite.config.js` 已配置代理，将 `/api` 请求转发至后端：

```js
server: {
  proxy: {
    '/': 'http://localhost:8080'
  }
}
```

### 路由与权限

- 使用 **Hash 模式**路由 (`createWebHashHistory`)
- 路由守卫根据 `localStorage.loginUser.role` 判断角色
- `role === 'admin'` → 只能访问 `/admin/*`
- 其他用户 → 只能访问 `/home/*`
- 未登录 → 只能访问 `/login` 和 `/register`

---

## 🔌 API 接口一览

| 接口 | 方法 | 说明 |
|------|------|------|
| `/user/login` | POST | 用户登录 |
| `/user/register` | POST | 用户注册 |
| `/appointment/list` | GET | 预约列表 |
| `/appointment` | POST | 创建预约 |
| `/appointment/{id}/confirm` | PUT | 确认预约（自动生成订单） |
| `/order/list` | GET | 订单列表（支持关键词/状态筛选） |
| `/order/{id}/status` | PUT | 更新订单状态（同步预约） |
| `/order/stats` | GET | 订单统计数据 |
| `/bed/list` | GET | 病床列表 |
| `/bed` | POST | 新增病床 |
| `/bed/stats` | GET | 病床统计数据 |
| `/nursing-service/list` | GET | 护理服务列表 |
| `/staff/list` | GET | 护理人员列表 |

---

## 🛠️ 技术栈

| 层级 | 技术 |
|------|------|
| **后端框架** | Spring Boot 3.5.13 |
| **ORM** | MyBatis Plus 3.5.7 |
| **数据库** | MySQL 8.0 |
| **连接池** | Druid 1.2.14 |
| **AI 集成** | Spring AI + DeepSeek |
| **前端框架** | Vue 3.5 + Vite 8 |
| **UI 组件库** | Element Plus 2.14 |
| **路由** | Vue Router 5 |
| **HTTP 客户端** | Axios |

---

## ❓ 常见问题

**Q: 启动后端报数据库连接失败？**
A: 检查 MySQL 是否启动，确认 `application.yml` 中数据库用户名、密码、URL 是否正确，数据库 `shop` 是否已创建。

**Q: 前端页面空白或报 404？**
A: 确认后端已启动（端口 8080），前端代理配置正确。尝试清除浏览器缓存后刷新。

**Q: AI 问诊无法使用？**
A: 确认已配置有效的 DeepSeek API Key，且网络可访问 `https://api.deepseek.com`。

**Q: 登录提示"用户名或密码错误"？**
A: 确保数据库中有对应用户记录。管理员默认账号 `admin/12345` 需在数据库中预先插入。

---

## 📄 License

本项目仅供学习交流使用。
