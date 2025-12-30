package com.liferpg.controllers;

import com.liferpg.models.Player;
import com.liferpg.models.Reward;
import com.liferpg.models.User;
import com.liferpg.models.PlayerRepository;
import com.liferpg.models.PurchaseLog;
import com.liferpg.models.PurchaseLogRepository;
import com.liferpg.models.RewardRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private RewardRepository rewardRepo;
    
    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private PurchaseLogRepository logRepo;

    // 商店页面
    @GetMapping
    public String shopPage(Model model, HttpSession session) {
        // 1. 安全校验
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        // 2. 强制从数据库获取最新 Player 数据 (解决金币不刷新问题)
        // 使用 Session 中保存的 Player ID 来查询
        Player player = playerRepo.findById(currentUser.getPlayer().getId()).orElse(null);
        if (player == null) return "redirect:/login";

        List<Reward> rewards = rewardRepo.findAll();

        List<PurchaseLog> history = logRepo.findByUserOrderByPurchaseTimeDesc(currentUser);
        
        // 3. 传入数据到页面
        model.addAttribute("rewards", rewards);
        model.addAttribute("player", player);
        model.addAttribute("history", history);
        return "shop";
    }

    // 购买物品
    @PostMapping("/buy/{id}")
    public String buyItem(@PathVariable Long id, HttpSession session) {
        // 1. 安全校验
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        Reward item = rewardRepo.findById(id).orElse(null);
        // 2. 强制查库获取最新玩家状态
        Player player = playerRepo.findById(currentUser.getPlayer().getId()).orElse(null);

        if (item == null || player == null) return "redirect:/shop?error";

        // 3. 检查钱够不够且库存是否有
        if (player.getGold() >= item.getCost() && (item.getStock() > 0 || item.getStock() == -1)) {
            // 扣钱
            player.setGold(player.getGold() - item.getCost());
            
            // 扣库存
            if (item.getStock() > 0) {
                item.setStock(item.getStock() - 1);
                rewardRepo.save(item);
            }
            
            // 保存数据库
            playerRepo.save(player);

            // 记录购买日志
            PurchaseLog log = new PurchaseLog(item.getItemName(), item.getCost(), currentUser);
            logRepo.save(log);

            // 4. 更新 Session，防止页面跳转后金币显示旧数据
            currentUser.setPlayer(player);
            session.setAttribute("currentUser", currentUser);
            
            System.out.println(">>> 购买成功：" + item.getItemName());
            return "redirect:/shop?success"; 
        } else {
            return "redirect:/shop?error"; 
        }
    }
}