package com.liferpg.controllers;

import com.liferpg.models.Player;
import com.liferpg.models.PlayerRepository;
import com.liferpg.models.PurchaseLog;
import com.liferpg.models.PurchaseLogRepository;
import com.liferpg.models.Quest;
import com.liferpg.models.QuestRepository; // 新增导入
import com.liferpg.utils.GameMath; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import com.liferpg.models.User;
import java.util.List; // 新增导入

@Controller
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepo;
    
    @Autowired
    private QuestRepository questRepo; // 注入任务仓库，用于初始化时删除任务

    @Autowired
    private PurchaseLogRepository logRepo;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        // 强制查库：确保拿到的是数据库里的最新状态
        Player player = playerRepo.findById(currentUser.getPlayer().getId()).orElse(currentUser.getPlayer());
        
        // 重要：查完库后，顺手把 Session 也更新一下，防止 Header 栏显示旧数据
        currentUser.setPlayer(player);
        session.setAttribute("currentUser", currentUser);

        int nextLevelXp = GameMath.calculateNextLevelXp(player.getLevel());
        model.addAttribute("player", player);
        model.addAttribute("nextLevelXp", nextLevelXp);
        
        double progress = 0;
        if (nextLevelXp > 0) {
            progress = ((double) player.getCurrentXp() / nextLevelXp) * 100;
        }
        model.addAttribute("xpProgress", (int) progress);

        return "dashboard"; 
    }

    // 初始化/重置全部数据
    @GetMapping("/player/reset")
    public String resetData(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login"; 
        
        // 1. 先删除该用户的所有任务和购买记录 (包括回收站的和进行中的)
        List<PurchaseLog> userLogs = logRepo.findByUserOrderByPurchaseTimeDesc(user);
        logRepo.deleteAll(userLogs);
        
        List<Quest> userQuests = questRepo.findByUser(user);
        questRepo.deleteAll(userQuests);

        // 2. 重置玩家属性
        Player p = playerRepo.findById(user.getPlayer().getId()).orElse(user.getPlayer());
        p.setLevel(1);
        p.setCurrentXp(0);
        p.setGold(0);
        p.setStr(10);
        p.setIntel(10);
        p.setTitle("初级冒险者");
        playerRepo.save(p);

        // 3. 更新 Session
        user.setPlayer(p);
        session.setAttribute("currentUser", user);

        System.out.println(">>> 用户数据已完全重置");
        return "redirect:/dashboard";
    }

    // 查看头衔面板
    @GetMapping("/player/titles")
    public String viewTitles(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        // 获取最新玩家数据
        Player player = playerRepo.findById(currentUser.getPlayer().getId()).orElse(currentUser.getPlayer());
        model.addAttribute("player", player);

        // 定义头衔配置
        model.addAttribute("titlesConfig", List.of(
            new TitleInfo("初级冒险者", 1, "初出茅庐的菜鸟。"),
            new TitleInfo("资深冒险者", 5, "已经在冒险界小有名气。"),
            new TitleInfo("王国英雄", 10, "名字被吟游诗人传唱。"),
            new TitleInfo("传奇勇者", 20, "你的存在本身就是神话。")
        ));

        return "titles_board";
    }

    // 内部辅助类
    public static class TitleInfo {
        private String name;
        private int level;
        private String desc;
        public TitleInfo(String name, int level, String desc) {
            this.name = name; this.level = level; this.desc = desc;
        }
        public String getName() { return name; }
        public int getLevel() { return level; }
        public String getDesc() { return desc; }
    }
}