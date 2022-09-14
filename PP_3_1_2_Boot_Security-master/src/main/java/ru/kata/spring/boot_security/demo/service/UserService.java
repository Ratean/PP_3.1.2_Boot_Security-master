package ru.kata.spring.boot_security.demo.service;





import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    void save(User user);
    void update(User user);
    void delete(int id);
    User getUser(int id);
    List<User> getAllUser();
}
