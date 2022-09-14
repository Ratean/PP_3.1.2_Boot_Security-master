package ru.kata.spring.boot_security.demo.model;



import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 35, message = "Min 2 characters max 35")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Surname should not be empty")
    @Size(min = 2, max = 35, message = "Min 2 characters max 35")
    @Column(name = "surname")
    private String surname;

    private String password;

    @Min(value = 0, message = "Value not less than 0")
    @Column(name = "age")
    private int age;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public User(String name, String surname, String password, int age) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.age = age;
    }

    public User(String name, String surname, String password, int age, Collection<Role> roles) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
