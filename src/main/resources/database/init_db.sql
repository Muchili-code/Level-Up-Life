-- 1. 用户表 (新增)
-- 用于存储账号和密码，并关联到一个玩家角色
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    player_id BIGINT
);

-- 2. 勇者状态表 (修改)
-- 移除了原有的 username，因为用户名现在归 User 表管理
CREATE TABLE IF NOT EXISTS player_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255), 
    level INT DEFAULT 1,
    current_xp INT DEFAULT 0,
    gold INT DEFAULT 0,
    str INT DEFAULT 10,
    intel INT DEFAULT 10,
    title VARCHAR(255) DEFAULT '初级冒险者'
);

-- 3. 任务布告栏 (修改)
-- 新增 user_id 字段，实现“每个用户只能看到自己的任务”
CREATE TABLE IF NOT EXISTS quests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT, -- 关键：关联到 users 表的 id
    title VARCHAR(255) NOT NULL,
    description TEXT,
    difficulty INT DEFAULT 1,
    reward_xp INT DEFAULT 10,
    reward_gold INT DEFAULT 5,
    attribute_type VARCHAR(50),
    status VARCHAR(20) DEFAULT 'TODO',
    deadline DATE
);

-- 4. 奖励商店 (保持不变)
CREATE TABLE IF NOT EXISTS rewards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    cost INT NOT NULL,
    stock INT DEFAULT -1,
    description TEXT
);