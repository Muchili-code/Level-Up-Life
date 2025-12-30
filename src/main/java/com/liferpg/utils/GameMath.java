package com.liferpg.utils;

public class GameMath {
    
    // 预设属性上限，方便前端雷达图引用
    public static final int MAX_ATTRIBUTE = 100;

    /**
     * 计算下一级所需的总经验值
     * 算法：基础值 * 等级 * 难度系数
     * 例如：1级升2级需要 100 XP
     * 2级升3级需要 200 XP
     */
    public static int calculateNextLevelXp(int currentLevel) {
        return 100 + (currentLevel * 50);
    }

    /**
     * 检查是否可以升级
     * @return 升级后的等级 (如果没升级则返回原等级)
     */
    public static boolean checkLevelUp(int currentXp, int currentLevel) {
        int required = calculateNextLevelXp(currentLevel);
        return currentXp >= required;
    }
    
    /**
     * 升级扣除经验处理 (保留溢出的经验)
     */
    public static int getXpAfterLevelUp(int currentXp, int currentLevel) {
        return currentXp - calculateNextLevelXp(currentLevel);
    }
}