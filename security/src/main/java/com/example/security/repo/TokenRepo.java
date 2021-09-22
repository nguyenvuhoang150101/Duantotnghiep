package com.example.security.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.security.entity.Token;
import com.example.security.entity.User;

@Transactional
@Repository
public interface TokenRepo extends JpaRepository<Token, String>, JpaSpecificationExecutor<User> {
  Optional<Token> findByUserIdAndValue(String name, String value);
}
