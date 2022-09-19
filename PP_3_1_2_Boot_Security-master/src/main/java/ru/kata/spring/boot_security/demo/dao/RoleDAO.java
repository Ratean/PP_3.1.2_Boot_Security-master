package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleDAO extends JpaRepository<Role, Long> {
//    List<Role> getRoleByName();
    Role getRoleByName(String name);
}
