package com.keshe.crop.controller;

import com.keshe.crop.filter.AuthFilter;
import com.keshe.crop.model.User;
import com.keshe.crop.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Year;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final FavoriteService favoriteService;

    public GlobalControllerAdvice(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * 注意使用 request.getSession(false)，游客访问时不创建 Session，
     * 避免 URL 上出现 ;jsessionid= 后缀。
     */
    @ModelAttribute("currentUser")
    public User currentUser(HttpServletRequest request) {
        return sessionUser(request);
    }

    @ModelAttribute("myFavoriteCount")
    public long myFavoriteCount(HttpServletRequest request) {
        User user = sessionUser(request);
        if (user == null) {
            return 0;
        }
        return favoriteService.countByUser(user.getId());
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    @ModelAttribute("currentYear")
    public int currentYear() {
        return Year.now().getValue();
    }

    private User sessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute(AuthFilter.SESSION_USER);
    }
}
