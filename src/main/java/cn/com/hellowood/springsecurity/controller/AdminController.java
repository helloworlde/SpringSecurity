package cn.com.hellowood.springsecurity.controller;

import cn.com.hellowood.springsecurity.model.UserModel;
import cn.com.hellowood.springsecurity.service.AdminService;
import cn.com.hellowood.springsecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * The type Admin controller.
 *
 * @author HelloWood
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The Admin service.
     */
    @Autowired
    AdminService adminService;

    /**
     * The User service.
     */
    @Autowired
    UserService userService;

    /**
     * Admin index page.
     *
     * @return the page url
     */
    @GetMapping("/index")
    public String adminIndex() {
        return "/admin/index";
    }


    /**
     * Dashboard string.
     *
     * @param request  the request
     * @param response the response
     * @return the string
     */
    @GetMapping(value = "/dashboard")
    public String dashboard(HttpServletRequest request, HttpServletResponse response) {
        return "/admin/dashboard";
    }

    /**
     * User management string.
     *
     * @param request  the request
     * @param response the response
     * @param model    the model
     * @return the string
     */
    @GetMapping("/user/manage")
    public String userManagement(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<UserModel> userList = adminService.getAllUsers();
        model.addAttribute("userList", userList);
        return "/admin/user/manage";
    }
}
