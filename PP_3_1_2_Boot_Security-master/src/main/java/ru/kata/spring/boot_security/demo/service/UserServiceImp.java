package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByName(username);
    }

    @Override
    @Transactional
    public void save(User user, String roleName) {
        if (roleName != null) {
            user.setRoles(getRoleList(roleName));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    @Override
    @Transactional
    public void update(User user, String roleName) {
        User oldUser = getUser(user.getId());

        if (roleName == null) {
            user.setRoles(oldUser.getRoles());
        } else {
            List<Role> roles = getRoleList(roleName);
            user.setRoles(roles);
        }

        if (user.getPassword().equals("*******")) {
            user.setPassword(oldUser.getPassword());
            userDAO.save(user);
        } else {
            save(user, null);
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        userDAO.delete(userDAO.getById(id));
    }

    @Override
    @Transactional
    public User getUser(long id) {
        return userDAO.getById(id);
    }

    @Override
    @Transactional
    public List<User> getAllUser() {
        return userDAO.findAll();
    }

    @Override
    public List<Role> getAllRole() {
        return roleDAO.findAll();
    }

    @Override
    public Role getRoleById(long roleId) {
        return roleDAO.getById(roleId);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleDAO.getRoleByName(name);
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

    @PostConstruct
    private void creteAdmin() {
        ArrayList<Role> roles = new ArrayList<>();
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleAdmin.setId(1);
        roles.add(roleAdmin);
        Role roleUser = new Role("ROLE_USER");
        roleUser.setId(2);
        roles.add(roleUser);
        for (Role r: roles) {
            roleDAO.save(r);
        }

        User admin = new User("admin", "admin", "100", 23, roles);

        User otherUser = findByUsername(admin.getUsername());
        if (otherUser != null) {
            admin.setId(otherUser.getId());
        }
        save(admin, null);
    }

    private List<Role> getRoleList(String roleName) {
        String[] rolesArr = roleName.split(",");

        List<Role> roles = new ArrayList<>();

        for (int i = 0; i < rolesArr.length; i++) {
            roles.add(getRoleByName(rolesArr[i]));
        }
        return roles;
    }
}
