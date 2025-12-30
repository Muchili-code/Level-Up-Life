package com.liferpg.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
    // 只搜索属于特定用户的任务
    List<Quest> findByUserAndTitleContainingIgnoreCase(User user, String keyword);
    
    // 获取特定用户的所有任务
    List<Quest> findByUser(User user);
}