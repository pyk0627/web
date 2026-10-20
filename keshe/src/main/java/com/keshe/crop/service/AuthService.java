package com.keshe.crop.service;

import com.keshe.crop.model.RegisterForm;
import com.keshe.crop.model.Role;
import com.keshe.crop.model.User;
import com.keshe.crop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterForm form) {
        String username = form.getUsername() == null ? "" : form.getUsername().trim();
        String email = form.getEmail() == null ? "" : form.getEmail().trim().toLowerCase();
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已被注册，请换一个");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已被注册，请换一个");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.setDisplayName(form.getDisplayName().trim());
        user.setEmail(email);
        user.setPhone(form.getPhone() == null ? null : form.getPhone().trim());
        user.setRole(Role.USER);
        user.setEnabled(true);
        Long id = userRepository.save(user);
        user.setId(id);
        return sanitize(user);
    }

    public User login(String username, String rawPassword) {
        String name = username == null ? "" : username.trim();
        Optional<User> optional = userRepository.findByUsername(name);
        if (optional.isEmpty() || !passwordEncoder.matches(rawPassword, optional.get().getPasswordHash())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        User user = optional.get();
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("该账号已被停用，请联系管理员");
        }
        return sanitize(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(this::sanitize);
    }

    /**
     * 放入 Session 的用户对象不保留密码哈希。
     */
    public User sanitize(User user) {
        User copy = new User();
        copy.setId(user.getId());
        copy.setUsername(user.getUsername());
        copy.setDisplayName(user.getDisplayName());
        copy.setEmail(user.getEmail());
        copy.setPhone(user.getPhone());
        copy.setRole(user.getRole());
        copy.setEnabled(user.isEnabled());
        copy.setCreatedAt(user.getCreatedAt());
        return copy;
    }
}
