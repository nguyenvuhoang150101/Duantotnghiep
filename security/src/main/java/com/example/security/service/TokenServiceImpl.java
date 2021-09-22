package com.example.security.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.common.enums.TokenTypeEnum;
import com.example.security.entity.Token;
import com.example.security.repo.TokenRepo;

@Service
public class TokenServiceImpl implements TokenService {
  @Autowired
  private TokenRepo tokenRepo;

  @Override
  public void saveToken(String userId, TokenTypeEnum tokenType, String value, LocalDateTime affectDate,
      LocalDateTime expireDate) {
    Token t = new Token();
    t.setActive(true);
    t.setAffectDate(affectDate);
    t.setExpireDate(expireDate);
    t.setTokenType(tokenType);
    t.setUserId(userId);
    t.setValue(value);
    
    tokenRepo.save(t);
  }

  @Override
  public boolean isTokenValid(String userId, String value) {
    Optional<Token> t = tokenRepo.findByUserIdAndValue(userId, value);
    if (t.isPresent()) {
      Token token = t.get();
      LocalDateTime now = LocalDateTime.now();
      return now.isAfter(token.getAffectDate()) && now.isBefore(token.getExpireDate());
    }
    return false;
  }
}
