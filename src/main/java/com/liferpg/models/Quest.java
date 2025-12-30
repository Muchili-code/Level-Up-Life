package com.liferpg.models;

import jakarta.persistence.*;
import java.time.LocalDate;

// 已移除 @Data
@Entity
@Table(name = "quests")
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    
    private Integer difficulty; // 1-5 

    @Column(name = "reward_xp")
    private Integer rewardXp;

    @Column(name = "reward_gold")
    private Integer rewardGold;

    @Column(name = "attribute_type")
    private String attributeType; // STR or INT

    private String status; // TODO or DONE

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Quest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getRewardXp() {
        return rewardXp;
    }

    public void setRewardXp(Integer rewardXp) {
        this.rewardXp = rewardXp;
    }

    public Integer getRewardGold() {
        return rewardGold;
    }

    public void setRewardGold(Integer rewardGold) {
        this.rewardGold = rewardGold;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    @Override
    public String toString() {
        return "Quest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", difficulty=" + difficulty +
                ", rewardXp=" + rewardXp +
                ", rewardGold=" + rewardGold +
                ", attributeType='" + attributeType + '\'' +
                ", status='" + status + '\'' +
                ", deadline=" + deadline +
                '}';
    }




}