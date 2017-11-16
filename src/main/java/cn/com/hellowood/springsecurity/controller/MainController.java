package cn.com.hellowood.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static cn.com.hellowood.springsecurity.common.constant.CommonConstant.LOGIN_ERROR_MSG;

/**
 * The type Main controller.
 *
 * @author HelloWood
 */
@Controller
public class MainController {

    /**
     * Root page.
     *
     * @return the index page url
     */
    @RequestMapping("/")
    public String root() {
        return "redirect:/index";
    }

    /**
     * Index page.
     *
     * @return the index page url
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * User index page.
     *
     * @return the user index page url
     */
    @RequestMapping("/user/index")
    public String userIndex() {
        return "user/index";
    }

    /**
     * Login page.
     *
     * @return the login page url
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Login error page.
     *
     * @param model the model
     * @return the login error page url
     */
    @RequestMapping("/login-error")
    public String loginError(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        model.addAttribute(LOGIN_ERROR_MSG, session.getAttribute(LOGIN_ERROR_MSG));
        session.removeAttribute(LOGIN_ERROR_MSG);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "login";
    }


}
