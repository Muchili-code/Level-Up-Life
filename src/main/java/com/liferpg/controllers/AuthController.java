package com.liferpg.controllers;

import com.liferpg.models.User;
import com.liferpg.models.UserRepository;
import com.liferpg.models.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    // 1. 显示登录页面
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 稍后我们会创建 login.html
    }

    // 2. 处理登录逻辑
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, 
                              @RequestParam String password, 
                              HttpSession session, 
                              Model model) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            // 登录成功，将用户对象存入 Session
            session.setAttribute("currentUser", userOpt.get());
            return "redirect:/dashboard";
        } else {
            // 登录失败
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    // 3. 显示注册页面
    @GetMapping("/register")
    public String registerPage() {
        return "register"; 
    }

    // 4. 处理注册逻辑
    @PostMapping("/register")
    public String handleRegister(@RequestParam String username, 
                                 @RequestParam String password, 
                                 Model model) {
        if (userRepo.findByUsername(username).isPresent()) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        // 为新用户初始化一个 Player 角色
        Player newPlayer = new Player();
        newPlayer.setUsername(username); // 默认角色名同用户名
        newUser.setPlayer(newPlayer);

        userRepo.save(newUser);
        return "redirect:/login";
    }

    // 5. 注销登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 销毁 Session
        return "redirect:/login";
    }

    // 6.删除用户账户
    @PostMapping("/user/delete")
    public String deleteAccount(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            userRepo.delete(currentUser); // 触发级联删除
            session.invalidate(); // 销毁当前会话
        }
        return "redirect:/login";
    }
    
}