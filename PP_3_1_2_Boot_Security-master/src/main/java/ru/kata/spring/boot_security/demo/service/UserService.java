package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(User user, List<Long> roleIds);
    void updateUser(Long id, User user, List<Long> roleIds);
    void update(User user);  // Добавлен новый метод
    void deleteUser(Long id);
    User findByUsername(String username);
    void createDefaultAdmin();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    User findById(Long id);

    ;
}