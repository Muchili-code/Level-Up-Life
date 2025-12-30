--('🎮 游戏特权', 100, -1, '现实奖励：兑换后可无压力玩游戏1小时'),
--('☕ 续命咖啡', 50, 10, '现实奖励：今天可以去买一杯喜欢的奶茶或咖啡'),
--('😴 懒觉补丁', 200, 5, '现实奖励：明早可以赖床30分钟'),
--('🎬 影院礼券', 500, 1, '大奖：兑换一张周末电影票');

-- 使用 MERGE 并明确指定列名，防止数据错位
--MERGE INTO player_status (id, username, level, current_xp, gold, str, intel) 
--KEY(id) 
--VALUES (1, 'Hero', 1, 0, 500, 10, 10);

-- 初始化奖励物品
DELETE FROM rewards;
INSERT INTO rewards (item_name, cost, stock, description) VALUES 
('Game Time', 100, -1, 'Reward: Play games for 1 hour'),
('Coffee Break', 50, 10, 'Reward: Buy a cup of coffee/milk tea'),
('Sleep Patch', 200, 5, 'Reward: Sleep in for 30 minutes'),
('Movie Ticket', 500, 1, 'Grand Prize: One weekend movie ticket');