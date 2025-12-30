package com.liferpg.models;

import jakarta.persistence.*;

@Entity
@Table(name = "player_status")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private Integer level;
    
    @Column(name = "current_xp")
    private Integer currentXp;
    
    private Integer gold;
    private Integer str; // 力量
    
    @Column(name = "intel")
    private Integer intel; // 智力

    // --- 新增字段：称号 ---
    private String title;

    public Player() {
        this.level = 1;
        this.currentXp = 0;
        this.gold = 0;
        this.str = 10;
        this.intel = 10;
        this.title = "初级冒险者"; // 初始称号
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getCurrentXp() { return currentXp; }
    public void setCurrentXp(Integer currentXp) { this.currentXp = currentXp; }

    public Integer getGold() { return gold; }
    public void setGold(Integer gold) { this.gold = gold; }

    public Integer getStr() { return str; }
    public void setStr(Integer str) { this.str = str; }

    public Integer getIntel() { return intel; }
    public void setIntel(Integer intel) { this.intel = intel; }

    // --- 新增字段的 Getter/Setter ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", level=" + level +
                ", currentXp=" + currentXp +
                ", gold=" + gold +
                '}';
    }
}