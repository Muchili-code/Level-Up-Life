package com.liferpg.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseLogRepository extends JpaRepository<PurchaseLog, Long> {
    // 获取特定用户的所有购买记录，按时间倒序排列
    List<PurchaseLog> findByUserOrderByPurchaseTimeDesc(User user);
}