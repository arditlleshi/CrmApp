package com.crm.security.repository;

import com.crm.security.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("select t from tokens t inner join users u on t.user.id = u.id " +
            "where u.id = :userId and (t.expired = false or t.revoked = false )")
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);

    Token findByUserEmail(String email);
}