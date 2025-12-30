package com.liferpg.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name")
    private String itemName;
    
    private Integer cost;
    private Integer stock; // -1 表示无限库存

    private String description;

    public Reward() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", cost=" + cost +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                '}';
    }
}