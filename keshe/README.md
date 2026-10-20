# 非粮生物柴油植物知识查找与匹配系统

课程设计项目：把《中国非粮生物柴油植物》中的植物档案、含油率及脂肪酸化学组分整理成结构化数据，
提供**组合查询、结果字段自选、植物详情、收藏、后台维护**等功能。

项目采用传统 Java Web 的“服务端渲染”思路，没有使用前后端分离，页面直接由 Thymeleaf 渲染，
数据访问保留 JdbcTemplate + 原生 SQL，便于对照课程中的 JDBC、Servlet/Filter、JavaBean 等技术点。

## 一、技术栈

| 层次 | 技术 |
| --- | --- |
| 后端框架 | Spring Boot 3.5、Spring MVC |
| 页面模板 | Thymeleaf（服务端渲染） |
| 数据访问 | JdbcTemplate + 原生 SQL |
| 数据库 | MySQL 8.x 或 MariaDB 10.6+ |
| 登录与权限 | HttpSession + 自定义 Filter + BCrypt 密码哈希 |
| 前端 | HTML5、CSS3、JavaScript、Bootstrap 5（本地静态资源，无需 npm） |
| 构建与部署 | Maven、可执行 jar、内置 Tomcat |
| JDK | Java 21 |

## 二、功能概览

### 前台（游客可用）

- 首页：数据库统计、科属分类导航、含油率示例植物。
- 知识查询 `/search`：关键词 + 科 + 属 + 含油率范围 + 是否有含油数据 + 排序方式的组合查询。
- 结果字段选择：中文名、英文名、拉丁学名、别名、科、属、特征、分布、栽培、用途、含油率可自由勾选。
- 分页：每页 5 / 10 / 20 / 50 条。
- 植物详情 `/crop/{id}`：完整档案、全部含油率记录、同科植物、外部知识链接。
- 关于项目 `/about`：数据来源与技术说明。

### 登录用户

- 注册 `/register`、登录 `/login`、退出 `/logout`。
- 收藏植物 `/favorites`，在详情页可收藏 / 取消收藏。

### 管理员

- 后台概览 `/admin`。
- 作物维护 `/admin/crops`：新增、编辑、删除、搜索、分页。
- 含油率记录维护 `/admin/oils`：按作物、产地、采集/测试单位筛选，新增、编辑、删除。
- 用户管理 `/admin/users`：角色调整、启用/停用、删除；系统会校验“至少保留一个管理员”和“不能操作自己”。

### 演示账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 普通用户 | `user` | `user123` |

## 三、环境准备

1. 安装 JDK 21。
2. 安装 Maven 3.9+。
3. 安装 MySQL 8 或 MariaDB 10.6+。
4. 创建数据库（数据库名默认 `crop_knowledge`）：

```sql
CREATE DATABASE crop_knowledge CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'keshe'@'localhost' IDENTIFIED BY 'keshe123';
GRANT ALL PRIVILEGES ON crop_knowledge.* TO 'keshe'@'localhost';
FLUSH PRIVILEGES;
```

> 表结构和演示数据由程序自动初始化：
> `db/schema.sql` 每次启动执行 `CREATE TABLE IF NOT EXISTS`；
> `data.sql` 只在 `crop` 表为空时导入，避免管理员修改数据后重启被覆盖。

## 四、配置数据库连接

默认配置在 `src/main/resources/application.yml`，也可以通过环境变量覆盖：

| 环境变量 | 默认值 | 说明 |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/crop_knowledge?...` | JDBC 连接地址 |
| `DB_USERNAME` | `root` | 数据库用户名 |
| `DB_PASSWORD` | 空 | 数据库密码 |
| `SERVER_PORT` | `8080` | 服务端口 |

使用 MariaDB 时把地址改为：

```bash
export DB_URL='jdbc:mariadb://localhost:3306/crop_knowledge?useUnicode=true&characterEncoding=UTF-8&useSSL=false'
```

## 五、启动与打包

### 开发模式启动

```bash
mvn spring-boot:run
```

带数据库环境变量启动：

```bash
DB_URL='jdbc:mariadb://localhost:3306/crop_knowledge?useUnicode=true&characterEncoding=UTF-8&useSSL=false' \
DB_USERNAME=keshe \
DB_PASSWORD=keshe123 \
mvn spring-boot:run
```

浏览器访问 <http://localhost:8080>。

### 打包为可执行 jar

```bash
mvn clean package
java -jar target/crop-knowledge-1.0.0.jar
```

也可以把数据库参数写在启动命令前：

```bash
DB_USERNAME=keshe DB_PASSWORD=keshe123 \
java -jar target/crop-knowledge-1.0.0.jar
```

### 运行测试

```bash
mvn test
```

## 六、数据库设计

| 表 | 说明 | 关键字段 |
| --- | --- | --- |
| `app_user` | 系统用户 | `username`、`password_hash`、`role`、`enabled` |
| `crop` | 植物档案 | `chinese_name`、`latin_name`、`family`、`genus`、特征/分布/栽培/用途、图片、参考资料 |
| `oil_record` | 含油率及化学组分 | `crop_id`、采集/测试单位、部位、产地、含油率、碘值、酸值、皂化值、C12~C20:1、备注 |
| `favorite` | 用户收藏 | `user_id` + `crop_id` 联合主键 |

- `oil_record.crop_id` 外键关联 `crop(id)`，删除作物时级联删除含油率记录。
- `favorite` 同时级联关联用户和作物。
- 角色使用 `ADMIN` / `USER`；密码使用 BCrypt 哈希，不保存明文。

完整建表语句见 `src/main/resources/db/schema.sql`，演示数据见 `src/main/resources/data.sql`。

## 七、项目结构

```text
src/main/java/com/keshe/crop
├── config          # BCrypt 配置、首次启动演示数据初始化
├── controller      # 页面控制器：首页、查询、详情、登录、后台
├── filter          # AuthFilter：登录校验与管理员权限校验
├── model           # JavaBean、表单对象、分页对象、枚举
├── repository      # JdbcTemplate 数据访问层
└── service         # 业务层

src/main/resources
├── application.yml
├── db/schema.sql              # 建表脚本
├── data.sql                   # 演示数据（33 种植物、60 条含油率记录）
├── static                     # Bootstrap、自定义 CSS/JS、植物 SVG 图片
└── templates                  # Thymeleaf 页面
    ├── admin/                 # 后台页面
    ├── auth/                  # 登录、注册
    ├── crop/                  # 植物详情
    ├── error/                 # 403/404/500
    └── fragments/             # 公共导航、页脚、消息提示
```

## 八、评分点对应

| 评分点 | 实现位置 |
| --- | --- |
| 前端页面 | `templates/`、`static/css/app.css`、Bootstrap 5 响应式布局 |
| 表单验证 | HTML5 属性 + `static/js/app.js` + Jakarta Validation 注解 |
| Java Web 业务 | Spring MVC、JavaBean、`AuthFilter`、JdbcTemplate |
| 数据库设计 | `db/schema.sql` 四张表、主外键、唯一约束、CHECK 约束 |
| 特定功能 | 组合查询、结果字段自选、分页、详情、收藏、后台 CRUD |
| 权限控制 | Session 登录态 + Filter 拦截 `/admin/**` 与 `/favorites` |
| 部署 | Maven 打包可执行 jar，内置 Tomcat |

## 九、常见问题

**1. 启动时报“Access denied for user”**

检查 `DB_USERNAME`、`DB_PASSWORD` 是否正确，或使用 `application.yml` 中的默认值连接本地数据库。

**2. 启动时报“Unknown database 'crop_knowledge'”**

先执行第三节中的 `CREATE DATABASE` 语句。

**3. 演示账号登录失败**

`data.sql` 只在作物表为空时导入。如果之前手动改过用户表，可以删除数据库后重新启动，
或用 BCrypt 重新生成密码哈希后更新 `app_user`。

**4. 中文显示乱码**

确认数据库使用 `utf8mb4`，JDBC URL 中带 `characterEncoding=UTF-8`。

**5. 图片显示为占位图**

课程资料 PDF 中没有单独的植物图片，项目使用生成的 SVG 占位图。
管理员可以在“作物维护 → 编辑”中把图片地址改成实际图片链接。
