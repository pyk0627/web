package com.keshe.crop.filter;

import com.keshe.crop.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 登录与角色过滤器。
 * 对应课程评分中的 Filter 技术点：
 * - 未登录用户不能访问收藏和后台；
 * - 普通用户不能访问 /admin/**；
 * - 管理员可以访问后台全部功能。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class AuthFilter extends OncePerRequestFilter {

    public static final String SESSION_USER = "loginUser";

    private static final String[] PUBLIC_PREFIXES = {
            "/css/", "/js/", "/images/", "/vendor/", "/favicon.ico", "/error"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (isPublic(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(SESSION_USER);

        if (path.startsWith("/admin")) {
            if (user == null) {
                redirectToLogin(request, response, path);
                return;
            }
            if (!user.isAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "只有管理员可以访问该页面");
                return;
            }
        } else if (path.startsWith("/favorites")) {
            if (user == null) {
                redirectToLogin(request, response, path);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        if (path.equals("/") || path.equals("/search") || path.equals("/login")
                || path.equals("/register") || path.equals("/logout") || path.equals("/about")) {
            return true;
        }
        if (path.startsWith("/crop/")) {
            return true;
        }
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response,
                                 String targetPath) throws IOException {
        String query = request.getQueryString();
        String target = targetPath + (query == null ? "" : "?" + query);
        String encoded = URLEncoder.encode(target, StandardCharsets.UTF_8);
        response.sendRedirect(request.getContextPath() + "/login?redirect=" + encoded);
    }
}
