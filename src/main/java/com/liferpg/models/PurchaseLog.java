package com.liferpg.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_logs")
public class PurchaseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;   // 购买物品名称
    private Integer cost;      // 消费金额
    private LocalDateTime purchaseTime; // 购买时间

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 关联用户

    public PurchaseLog() {}

    // 构造函数方便创建记录
    public PurchaseLog(String itemName, Integer cost, User user) {
        this.itemName = itemName;
        this.cost = cost;
        this.user = user;
        this.purchaseTime = LocalDateTime.now(); // 记录当前系统时间
    }

    // Getter 和 Setter
    public Long getId() { return id; }
    public String getItemName() { return itemName; }
    public Integer getCost() { return cost; }
    public LocalDateTime getPurchaseTime() { return purchaseTime; }
    public User getUser() { return user; }
}