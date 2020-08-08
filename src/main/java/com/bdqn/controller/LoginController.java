package com.bdqn.controller;


import com.bdqn.pojo.User;
import com.bdqn.service.user.UserService;
import com.bdqn.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping("/")
    public String frame(){
        return "login";
    }
    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param userCode
     * @param password
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/login.do")
    public String login(@RequestParam(value = "userCode",required = false) String userCode,
                        @RequestParam(value = "userPassword",required = false) String password,
                        HttpSession session,
                        Model model) throws Exception {

        User user = userService.login(userCode, password);
        //判断用户是否登录
        if(user != null){
            session.setAttribute(Constants.USER_SESSION, user);
            return "redirect:/user/list";
        }else{
            //跳转到登录页面，显示错误信息
            model.addAttribute("error", "用户名或密码错误");
            return "login";
//            throw new RuntimeException("用户名或者密码不正确！");
        }
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping("/logout")
    public String logOut(HttpSession session){
        //销毁session
        session.invalidate();
        return "redirect:/login";
    }

//    /**
//     * 登录时异常
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value={RuntimeException.class})
//    public String handlerException(RuntimeException e, Model model){
//        model.addAttribute("e", e);
//        return "error";
//    }
}
