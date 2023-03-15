package com.crm.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User extends Person{
    private String password;
    @ManyToMany(fetch = EAGER)
    private List<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
