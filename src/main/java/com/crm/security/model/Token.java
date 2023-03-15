package com.crm.security.model;

import com.crm.security.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
