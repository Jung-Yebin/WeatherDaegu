package com.bank_of_korea.bank_of_korea.controller;

import com.bank_of_korea.bank_of_korea.entity.Users;
import com.bank_of_korea.bank_of_korea.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    @Autowired
    private UsersService usersService;
    @GetMapping("/weather/login")
    public String login(){
        return "login";
    }

    @PostMapping("/weather/loginpro")
    public String loginpro(Users users, HttpSession session, Model model){
        //로그인 성공
        if(usersService.isIdExists(users.getEmail())){
            if(usersService.checkPwd(users.getEmail()).equals(users.getPassword())){
                session.setAttribute("loginEmail", users.getEmail());
                return "redirect:/weather/main";
            }
            else {
                model.addAttribute("message", "아이디 또는 비밀번호가 틀립니다.");
                model.addAttribute("searchUrl", "/weather/login");
                return "message";
            }
        }
        //로그인 실패
        else {
            model.addAttribute("message", "아이디 또는 비밀번호가 틀립니다.");
            model.addAttribute("searchUrl", "/weather/login");
            return "message";
        }
    }

    @GetMapping("/weather/signin")
    public String signin(){
        return "signin";
    }

    @PostMapping("/weather/signinpro")
    public String siginpro(Users users, Model model){
        if (usersService.isIdExists(users.getEmail())){
            model.addAttribute("message", "이미 존재하는 회원입니다.");
            model.addAttribute("searchUrl", "/weather/signin");
            return "message";
        }
        else {
            usersService.write(users);
            model.addAttribute("message", "회원 가입 완료.");
            model.addAttribute("searchUrl", "/weather/login");
            return "message";
        }
    }

    @GetMapping("/weather/logoutpro")
    public String logoutPro(HttpSession session, Model model){
        session.removeAttribute("loginEmail");

        model.addAttribute("message", "로그아웃 완료");
        model.addAttribute("searchUrl", "/weather");
        return "message";
    }

}
