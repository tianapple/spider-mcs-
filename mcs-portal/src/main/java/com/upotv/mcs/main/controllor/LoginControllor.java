package com.upotv.mcs.main.controllor;

import com.upotv.mcs.main.entity.Mcs_menu;
import com.upotv.mcs.main.entity.Mcs_user;
import com.upotv.mcs.main.entity.TreeData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.upotv.mcs.main.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tianapple on 2017/5/10.
 */
@Controller
public class LoginControllor {
    private static Logger LOGGER = LoggerFactory.getLogger(LoginControllor.class);

    @Autowired
    private LoginService loginService;

    @RequestMapping("/")
    public String index(Model model) {
        if (isRelogin()) {
            return "main";
        } else {
            return "login";
        }
    }

    /**
     * 用户登陆
     *
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("/login") //url
    public void dologin(HttpServletResponse response,HttpSession session, Mcs_user user, Model model) throws IOException {
        String info = loginUser(user);
        session.setAttribute("loginMsg",info);

        if ("success".equals(info)) {
            response.sendRedirect("/main");
        } else {
            response.sendRedirect("/");
        }
    }

    private String loginUser(Mcs_user user) {
        if (isRelogin()) {
            return "success"; // 如果已经登陆，无需重新登录
        }
        return shiroLogin(user); // 调用shiro的登陆验证
    }

    /**
     * shiro登陆
     *
     * @param user
     * @return
     */
    private String shiroLogin(Mcs_user user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        token.setRememberMe(true);
        // shiro登陆验证
        try {
            subject.login(token);
        } catch (UnknownAccountException ex) {
            return "用户不存在或者密码错误！";
        } catch (IncorrectCredentialsException ex) {
            return "用户不存在或者密码错误！";
        } catch (LockedAccountException ex) {
            return "账号被锁定！";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "内部错误，请重试！";
        }
        return "success";
    }

    /**
     * 验证是否登陆
     *
     * @return
     */
    private boolean isRelogin() {
        Subject us = SecurityUtils.getSubject();
        if (us.isAuthenticated()) {
            return true; // 参数未改变，无需重新登录，默认为已经登录成功
        }
        return false; // 需要重新登陆
    }

    /**
     * 登出
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            try {
                subject.logout();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        response.sendRedirect("/");
    }

    /**
     * 获得根菜单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMenu")
    public List<TreeData> getMenu(int pid) {
        Mcs_user user = getUser();
        List<Mcs_menu> menuList = loginService.getMenuList(user.getUserId(), pid);

        if (user.isAdmin()) {
            List<Mcs_menu> menuAdminList = loginService.getAdminMenuList(pid);
            if (menuAdminList.size() > 0) {
                menuList.addAll(menuAdminList);
            }
        }
        menuList = menuList.stream().distinct().collect(Collectors.toList()); //排除重复

        List<TreeData> trees = new ArrayList<>();
        for (Mcs_menu mcsMenu : menuList) {
            TreeData treeData = TreeData.parse(mcsMenu);
            trees.add(treeData);
        }
        return trees;
    }


    private Mcs_user getUser() {
        Subject subject = SecurityUtils.getSubject();
        Object user = subject.getSession().getAttribute("user");
        if (user != null) {
            return (Mcs_user) user;
        }
        return null;
    }
}