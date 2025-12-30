package com.liferpg.controllers;

import com.liferpg.models.Quest;
import com.liferpg.models.Player; 
import com.liferpg.models.QuestRepository;
import com.liferpg.models.User;
import com.liferpg.models.PlayerRepository;
import com.liferpg.utils.GameMath;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuestController {

    @Autowired
    private QuestRepository questRepository;
    @Autowired
    private PlayerRepository playerRepository;

    // 1. 显示任务列表 (保持高效，只过滤 list)
    @GetMapping("/quests")
    public String viewQuestBoard(@RequestParam(value = "keyword", required = false) String keyword, 
                                HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        List<Quest> quests;
        if (keyword != null && !keyword.isEmpty()) {
            quests = questRepository.findByUserAndTitleContainingIgnoreCase(currentUser, keyword);
            model.addAttribute("keyword", keyword);
        } else {
            quests = questRepository.findByUser(currentUser);
        }

        // 过滤掉回收站的任务
        quests = quests.stream().filter(q -> q.isDeleted() != true).toList();
        
        model.addAttribute("quests", quests);
        return "quest_board"; 
    }

    // 2. 显示发布表单
    @GetMapping("/quests/add")
    public String showAddQuestForm(Model model) {
        Quest quest = new Quest();
        quest.setDifficulty(1); 
        model.addAttribute("quest", quest);
        return "quest_form"; 
    }

    // 3. 保存任务
    @PostMapping("/quests/save")
    public String saveQuest(@ModelAttribute("quest") Quest quest, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        quest.setUser(currentUser);
        
        // 注意：这里的预览值仅供参考，实际结算以 completeQuest 中的实时计算为准
        int diff = quest.getDifficulty();
        quest.setRewardXp(diff * 20); 
        quest.setRewardGold(diff * 10);
        
        if (quest.getId() == null) {
            quest.setStatus("TODO");
            quest.setDeleted(false);
        }
        questRepository.save(quest);
        return "redirect:/quests";
    }

    // 4. 完成任务
    @GetMapping("/quests/complete/{id}")
    public String completeQuest(@PathVariable("id") Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        Quest quest = questRepository.findById(id).orElse(null);
        // 安全校验
        if (quest == null || !quest.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/quests";
        }
        if ("DONE".equals(quest.getStatus())) return "redirect:/quests";

        // 1. 获取最新玩家数据
        Player player = playerRepository.findById(currentUser.getPlayer().getId()).orElseThrow();
        
        // 2. 计算奖励
        // 公式1：金币 = (难度 * 10) + (STR + INT) / 2
        int attributeBonus = (player.getStr() + player.getIntel()) / 2;
        int goldGain = (quest.getDifficulty() * 10) + attributeBonus;

        // 公式2：XP = 难度 * (20 + 等级 * 2)
        int xpGain = quest.getDifficulty() * (20 + (player.getLevel() * 2));

        // 应用金币和经验
        player.setGold(player.getGold() + goldGain);
        player.setCurrentXp(player.getCurrentXp() + xpGain);

        // 公式3：属性增长 = 难度值
        int attributeGain = quest.getDifficulty();
        if ("STR".equals(quest.getAttributeType())) {
            player.setStr(player.getStr() + attributeGain);
        } else {
            player.setIntel(player.getIntel() + attributeGain);
        }
        
        System.out.println(">>> 结算: 难度" + quest.getDifficulty() + " | 金币+" + goldGain + " | XP+" + xpGain + " | 属性+" + attributeGain);

        // 升级逻辑
        int requiredXp = GameMath.calculateNextLevelXp(player.getLevel());
        while (player.getCurrentXp() >= requiredXp) {
            player.setCurrentXp(player.getCurrentXp() - requiredXp);
            player.setLevel(player.getLevel() + 1);
            
            // 重新计算下一级所需
            requiredXp = GameMath.calculateNextLevelXp(player.getLevel());
            
            // 更新头衔
            if (player.getLevel() >= 20) player.setTitle("传奇勇者");
            else if (player.getLevel() >= 10) player.setTitle("王国英雄");
            else if (player.getLevel() >= 5) player.setTitle("资深冒险者");
        }

        // 保存更改到数据库
        playerRepository.save(player);
        
        // 更新任务状态
        quest.setStatus("DONE");
        quest.setDeleted(true); 
        questRepository.save(quest);

        // *** 高效同步 ***
        // 不需要查 User 表，直接更新 Session 对象中的引用
        // 因为 currentUser 指向的对象还在内存里，我们更新了它的子对象 player
        currentUser.setPlayer(player); 
        session.setAttribute("currentUser", currentUser);

        return "redirect:/quests";
    }

    // 5. 移入回收站
    @GetMapping("/quests/delete/{id}")
    public String deleteQuest(@PathVariable("id") Long id) {
        Quest quest = questRepository.findById(id).orElse(null);
        if (quest != null) {
            quest.setDeleted(true); 
            questRepository.save(quest);
        }
        return "redirect:/quests";
    }

    // 6. 修改表单 
    @GetMapping("/quests/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Quest quest = questRepository.findById(id).orElse(null);
        if (quest == null || "DONE".equals(quest.getStatus())) return "redirect:/quests";
        model.addAttribute("quest", quest);
        return "quest_form";
    }

    // 7. 查看回收站
    @GetMapping("/quests/trash")
    public String viewTrash(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";
        
        List<Quest> deletedQuests = questRepository.findByUser(currentUser).stream()
                .filter(Quest::isDeleted)
                .toList();
                
        model.addAttribute("quests", deletedQuests);
        return "quest_trash";
    }

    // 8. 还原任务
    @GetMapping("/quests/restore/{id}")
    public String restoreQuest(@PathVariable("id") Long id) {
        Quest quest = questRepository.findById(id).orElse(null);
        if (quest != null) {
            quest.setDeleted(false);
            questRepository.save(quest);
        }
        return "redirect:/quests/trash";
    }

    // 9. 彻底删除
    @GetMapping("/quests/permanent-delete/{id}")
    public String permanentDelete(@PathVariable("id") Long id) {
        questRepository.deleteById(id);
        return "redirect:/quests/trash";
    }
}