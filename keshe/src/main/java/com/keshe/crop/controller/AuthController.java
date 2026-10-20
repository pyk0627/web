package com.keshe.crop.controller;

import com.keshe.crop.filter.AuthFilter;
import com.keshe.crop.model.LoginForm;
import com.keshe.crop.model.RegisterForm;
import com.keshe.crop.model.User;
import com.keshe.crop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String redirect,
                            @RequestParam(required = false) String logout,
                            HttpSession session, Model model) {
        if (session.getAttribute(AuthFilter.SESSION_USER) != null) {
            return "redirect:/";
        }
        LoginForm form = new LoginForm();
        form.setRedirect(redirect);
        model.addAttribute("loginForm", form);
        if (logout != null) {
            model.addAttribute("message", "你已安全退出登录");
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult,
                        HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        try {
            User user = authService.login(form.getUsername(), form.getPassword());
            session.setAttribute(AuthFilter.SESSION_USER, user);
            session.setMaxInactiveInterval(60 * 60);
            String target = safeRedirect(form.getRedirect());
            return "redirect:" + target;
        } catch (IllegalArgumentException e) {
            bindingResult.reject("loginError", e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model) {
        if (session.getAttribute(AuthFilter.SESSION_USER) != null) {
            return "redirect:/";
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
                           BindingResult bindingResult,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        if (form.getPassword() != null && form.getConfirmPassword() != null
                && !form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "两次输入的密码不一致");
        }
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            User user = authService.register(form);
            session.setAttribute(AuthFilter.SESSION_USER, user);
            redirectAttributes.addFlashAttribute("message", "注册成功，欢迎加入植物知识库！");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("registerError", e.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=1";
    }

    /**
     * 只允许跳转到本站路径，避免开放重定向漏洞。
     */
    private String safeRedirect(String redirect) {
        if (redirect == null || redirect.isBlank()) {
            return "/";
        }
        if (!redirect.startsWith("/") || redirect.startsWith("//") || redirect.contains("\\")) {
            return "/";
        }
        return redirect;
    }
}
