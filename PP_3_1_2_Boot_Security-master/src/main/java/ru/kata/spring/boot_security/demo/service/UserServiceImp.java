package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private UserDAO userDAO;

    @Autowired
    public UserServiceImp(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByName(username);
    }

    @Override
    @Transactional
    public void save(User user) {
        userDAO.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        userDAO.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userDAO.delete(userDAO.getById((long) id));
    }

    @Override
    @Transactional
    public User getUser(int id) {
        return userDAO.getById((long) id);
    }

    @Override
    @Transactional
    public List<User> getAllUser() {
        return userDAO.findAll();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new User(user.getName(), user.getSurname(), user.getPassword(), user.getAge(), user.getAuthorities());
    }
}
