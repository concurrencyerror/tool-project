# Tool Project

## 项目概述

这是一个以 `tool-backend` 为核心的 Spring Boot 后端项目，目标是实现提醒配置、工作日判断（第三方节假日 API）和多租户基础能力。

## 技术栈

- Java 25
- Spring Boot 4.0.0
- Spring Data JPA / Security / Redis
- MariaDB Driver
- Docker Compose

## 项目结构

```text
tool-project/
├─ README.md
└─ tool-backend/
   ├─ .dockerignore
   ├─ .env.example
   ├─ pom.xml
   ├─ compose.yaml
   ├─ Dockerfile
   ├─ sql/
   └─ src/
      ├─ main/
      └─ test/
```

## 现状分析（2026-03-17）

通过代码与配置审查，当前项目存在以下关键问题：

### P0（阻塞级）

1. `JacksonUtil` 代码存在明显编译/运行风险（已于 2026-03-18 修复）  
   文件：`tool-backend/src/main/java/com/horace/toolbackend/util/JacksonUtil.java`  
   原问题：导包为 `tools.jackson.*`，且序列化相关调用未见异常处理。当前已改为标准 `com.fasterxml.jackson.*` 导包，并统一封装 JSON 转换异常。

2. 敏感信息明文入库（已于 2026-03-18 修复）  
   文件：
   - `tool-backend/src/main/resources/application-dev.yaml`
   - `tool-backend/src/main/resources/application-pro.yaml`  
     原问题：数据库与 Redis 密码直接写在仓库配置中。当前已切换为环境变量注入，并新增 `tool-backend/.env.example` 作为示例。

### P1（高优先级）

1. Compose 数据库配置不一致（已于 2026-03-18 修复）  
   文件：`tool-backend/compose.yaml:3,8`  
   原问题：镜像是 `mysql:latest`，却使用 `MARIADB_ROOT_PASSWORD` 环境变量。当前已统一改为 `mariadb` 镜像与对应环境变量。

2. Redis 配置挂载路径可疑（已于 2026-03-18 修复）  
   文件：`tool-backend/compose.yaml:22,25`  
   原问题：挂载和启动参数指向 `/usr/local/etc/redis/redis/conf`，路径像目录而不是标准配置文件。当前已改为直接使用容器命令行参数配置 Redis 密码和持久化。

3. 时间类型设计不统一  
   文件：
   - `tool-backend/src/main/java/com/horace/toolbackend/entity/RemindEntity.java`
   - `tool-backend/src/main/java/com/horace/toolbackend/repository/RemindRepository.java`  
     问题：实体使用 `Date`，查询参数使用 `LocalDateTime`，容易出现时区与参数转换问题。

4. 实体访问器不完整  
   文件：`tool-backend/src/main/java/com/horace/toolbackend/entity/RemindEntity.java`  
   问题：`user` 字段缺少 getter/setter。

### P2（中优先级）

1. 当前未发现对外业务入口（Controller/Scheduler/Runner）。
2. `log-back.xml` 含硬编码 Linux 路径及乱码字符，跨环境可移植性弱。
3. 项目多处注释/文档编码异常，影响协作维护。
4. `main` 方法当前是包可见（`static void main`），在 JDK 25 语义下通常可启动，但若考虑旧版本 JDK 或部分工具链兼容，建议改为
   `public static void main`。

## 建议修复顺序

1. 修复启动与编译阻塞问题（优先处理 `JacksonUtil`）。
2. 迁移密钥到环境变量或密钥管理服务，清理仓库中的明文密码。
3. 修正 Compose 的 DB/Redis 配置，确保一键启动可用。
4. 统一时间模型（建议全链路 `LocalDateTime` 或 `Instant` + 时区策略）。
5. 增加 Controller 或任务调度入口，并补充端到端测试。
6. 统一编码为 UTF-8，清理乱码注释和文档。

## 本地运行（建议）

1. 安装 JDK 25、Maven 3.9+。
2. 配置数据库和 Redis 连接信息，建议通过环境变量注入。
3. 进入目录：`tool-backend`
4. 执行：`mvn clean test`、`mvn spring-boot:run`

可选环境变量示例：

```env
DB_URL=jdbc:mariadb://127.0.0.1:3306/tools-dev
DB_USERNAME=root
DB_PASSWORD=change-me
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=change-me
```

## Docker 部署

当前仓库中的 `tool-backend/Dockerfile` 只负责构建和运行后端服务，前端如果存在，应单独部署。

1. 进入目录：`tool-backend`
2. 复制环境文件：将 `.env.example` 复制为 `.env`
3. 按实际环境修改 `.env` 中的数据库、Redis 和端口配置
4. 启动服务：`docker compose up --build -d`
5. 查看状态：`docker compose ps`
6. 查看日志：`docker compose logs -f tool-backend`

默认会启动以下服务：

- `tool-backend`：Spring Boot 后端，默认映射端口 `6677`
- `tool-db`：MariaDB 11.8
- `tool-redis`：Redis 7.4

停止服务：

```bash
docker compose down
```

如果需要连同数据卷一并清理：

```bash
docker compose down -v
```

## 备注

- 本次分析基于当前仓库静态审查完成。
- 当前环境未安装 Maven，未能在此环境内复跑 `mvn` 相关命令。
