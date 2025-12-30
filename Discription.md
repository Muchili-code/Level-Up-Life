# README

## 项目结构树

```txt
Life-RPG/
├── pom.xml                         # Maven 依赖管理 (Spring Boot, JPA, H2, Thymeleaf)
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── liferpg/        
│       │           ├── Application.java          # 项目启动入口类
│       │           ├── models/                   # 数据模型层 (Entity & Repository)
│       │           │   ├── User.java             # 账号实体，级联关联 Player, Quest 与 PurchaseLog
│       │           │   ├── UserRepository.java   # 用户数据库接口
│       │           │   ├── Player.java           # 勇者属性实体 (等级、称号、属性、金币)
│       │           │   ├── PlayerRepository.java # 勇者属性数据库接口
│       │           │   ├── Quest.java            # 任务实体 (含 isDeleted 回收站标记)
│       │           │   ├── QuestRepository.java  # 任务数据库接口 (支持关键词与用户过滤)
│       │           │   ├── Reward.java           # 商店物品实体
│       │           │   ├── RewardRepository.java # 商店物品数据库接口
│       │           │   ├── PurchaseLog.java      # [新增] 购买记录实体 (物品、金额、时间)
│       │           │   └── PurchaseLogRepository.java # [新增] 购买记录数据库接口
│       │           ├── controllers/              # 业务控制层 (MVC)
│       │           │   ├── AuthController.java   # 处理注册、登录、注销、账户删除
│       │           │   ├── PlayerController.java # 处理 Dashboard、数据重置、荣誉殿堂(称号)展示
│       │           │   ├── QuestController.java  # 处理任务 CRUD、完成结算、回收站还原与彻底删除
│       │           │   └── ShopController.java   # 处理商店浏览、金币购买与购买记录生成
│       │           └── utils/
│       │               └── GameMath.java         # 核心公式类 (动态经验阈值、属性结算公式)
│       └── resources/              
│           ├── application.properties      # 配置文件 (H2 数据库持久化路径、控制台开启)
│           ├── data.sql                    # 数据库预设初始数据 (预置商店物品)
│           ├── static/                     # 静态资源
│           │   ├── css/
│           │   │   └── rpg-theme.css       # RPG 深色主题自定样式
│           │   ├── js/
│           │   │   └── game-logic.js       # 前端交互逻辑
│           │   └── images/
│           │       └── avatars/            # 角色头像资源
│           └── templates/                  # Thymeleaf 视图模板
│               ├── base.html               # 公共布局模板 (含导航栏、全局提示)
│               ├── login.html              # 登录页面
│               ├── register.html           # 注册页面
│               ├── dashboard.html          # 个人主页 (含属性雷达图、等级进度条)
│               ├── quest_board.html        # 任务公告栏 (当前有效任务列表)
│               ├── quest_form.html         # 任务发布/编辑表单
│               ├── quest_trash.html        # [新增] 任务回收站 (含还原与永久删除)
│               ├── shop.html               # 奖励商店 (含商品浏览与购买历史清单)
│               └── titles_board.html       # [新增] 荣誉殿堂 (头衔解锁进度展示)
├── data/                           # [自动生成] H2 数据库本地文件存储目录
├── target/                         # Maven 编译输出目录
├── Discription.md                  # 项目核心描述文档 (即本文档)
└── README.md                       # 项目简介与安装说明
```

## 项目运行方式

- 运行代码，清理

```powershell
mvn clean package -DskipTests
```



## 流程

太棒了！选择 **Life RPG (人生 RPG)** 是最容易做出彩、且开发过程最有趣的题目。你不是在写代码，你是在设计一款“为自己服务的游戏”。

为了助你拿到高分，我将不仅提供代码结构，还会把**游戏策划（Game Design）**的要素融入到你的开发大纲中。

------

<span style="font-size:1.1em; color:#CC0000;">**🎮 项目代号：Life RPG (Level Up Your Life)**</span>

### 核心概念设计 (Game Mechanics)

这个应用的核心逻辑不仅仅是“完成任务”，而是**“投入产出比”**的即时反馈。

- **玩家 (User/Player):** 拥有等级 (Level)、经验值 (XP)、金币 (Gold)、以及属性 (如：力量、智力)。
- **任务 (Quest):** 即待办事项。分为“主线任务”（重要紧急）和“日常任务”（每日打卡）。
- **奖励 (Loot/Shop):** 用金币购买现实生活中的奖励（如：看一集动漫 = 消耗 50 金币）。

------

### 数据库设计 (Database Schema)

这是项目的地基。你需要至少 3 张表。

**表 1：勇者状态表 (`player_status`)**

- 这其实就是 User 表的扩展。
- 字段：`id`, `username`, `level` (当前等级), `current_xp` (当前经验), `gold` (金币数), `str` (力量值), `int` (智力值).

**表 2：任务布告栏 (`quests`)**

- 这就是 To-Do 表。
- 字段：`id`, `title` (任务名), `description`, `difficulty` (难度: 1-5), `reward_xp` (完成给多少经验), `reward_gold` (给多少钱), `attribute_type` (加力量还是智力?), `status` (待领取/进行中/已完成), `deadline`.

**表 3：奖励商店 (`rewards`)**

- 商品表。
- 字段：`id`, `item_name` (奖励名, 如"奶茶"), `cost` (价格), `stock` (库存/或是无限).

------

### 项目目录结构树 (Project Structure)

假设你使用 **Python (Flask)** 或 **Java (Spring Boot)**，结构大同小异。为了高分，我们要体现**分层架构 (MVC)** 的思想。

```
Life-RPG/
├── database/                   # 存放数据库文件或SQL脚本
│   └── init_db.sql             # 初始化建表语句
├── static/                     # 静态资源 (前端)
│   ├── css/
│   │   └── rpg-theme.css       # 核心样式 (建议找个像素风或深色主题)
│   ├── js/
│   │   └── game-logic.js       # 处理点击特效、进度条动画
│   └── images/
│       ├── icons/              # 剑、书、金币的图标
│       └── avatars/            # 勇者头像
├── templates/ (或 views/)      # HTML 页面
│   ├── base.html               # 骨架页面 (导航栏包含：HP条, XP条, 金币数)
│   ├── dashboard.html          # 主控台 (显示人物属性雷达图)
│   ├── quest_board.html        # 任务列表 (CRUD 的核心页面)
│   └── shop.html               # 商店页面 (消费金币的地方)
├── src/ (后端代码)
│   ├── app.py (或 Application.java)  # 启动入口
│   ├── models/                 # 数据模型 (对应数据库表)
│   │   ├── Player.py
│   │   └── Quest.py
│   ├── controllers/ (或 routes/) # 业务逻辑
│   │   ├── quest_controller.py   # 处理任务的新增、完成
│   │   ├── shop_controller.py    # 处理购买逻辑
│   │   └── player_controller.py  # 处理升级逻辑
│   └── utils/
│       └── game_math.py        # (高分点) 专门计算升级所需经验的公式
└── README.md                   # 项目说明文档
```

------

### 功能开发路线图 (你的作业步骤)

这里我把 CRUD 和游戏逻辑结合起来，分为三个阶段：

**阶段一：青铜时代 (基础 CRUD)**

- **目标：** 能添加任务，能看到任务列表。
- **后端：** 实现 `add_quest` 和 `get_all_quests` 接口。
- **前端：** 做一个简单的表格显示任务。
- *此时它还只是个普通的 To-Do List。*

**阶段二：白银时代 (核心游戏循环)**

- **目标：** 完成任务能升级。这是**最关键**的一步。

- 逻辑 (The "Magic" Function)：

  编写一个 complete_quest(quest_id) 函数：

  1. 在数据库中把该任务标记为 `DONE`。
  2. 读取该任务的 `reward_xp` 和 `reward_gold`。
  3. 读取当前 `Player` 的数据。
  4. **计算：** `Player.xp += reward_xp`, `Player.gold += reward_gold`。
  5. **判断升级：** 如果 `Player.xp` > `升级阈值`，则 `Player.level += 1`。
  6. 保存回数据库。

- **前端：** 点击“完成”按钮，弹窗提示：“恭喜！获得 50 经验，金币 +10！”

**阶段三：黄金时代 (视觉与消费)**

- **目标：** 花钱，以及好看的界面。
- **商店功能：** 编写 `buy_item(item_id)`。逻辑：判断 `Player.gold >= Item.cost`。如果够，扣钱，前端显示“购买成功，去享受吧！”。
- **可视化 (高分必杀技)：**
  - 在网页顶部做一个长长的 **XP 进度条**。每次完成任务，进度条自动上涨（用简单的 CSS width 属性控制）。
  - 如果时间允许，引入 **ECharts.js**，在首页画一个五边形雷达图（力量、智力、敏捷、耐力、幸运），完成不同类型的任务增加不同的属性。

------

### 高分答辩话术 (Storytelling)

当老师问你：“这个项目有什么难点/亮点？”

你可以这样回答：

> “普通的管理系统缺乏激励机制，用户很难坚持使用。我的项目创新点在于将‘行为经济学’与 Web 开发相结合。
>
> 技术上，我设计了一个后端事务逻辑，保证任务完成和属性结算的原子性（要么都成功，要么都不变）。
>
> 此外，前端实现了可视化的动态反馈，把枯燥的数据库 CRUD 操作转化为了即时的视觉奖励。”





