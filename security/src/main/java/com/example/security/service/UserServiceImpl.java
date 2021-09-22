package com.example.security.service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.webjars.NotFoundException;

import com.example.common.dto.request.PatchRequest;
import com.example.common.enums.TokenTypeEnum;
import com.example.common.model.SecurityConfigParam;
import com.example.common.service.TokenService;
import com.example.common.util.SearchUtil;
import com.example.common.util.SecurityUtil;
import com.example.security.dto.user.CreateUserRequest;
import com.example.security.dto.user.ForgotPasswordRequest;
import com.example.security.dto.user.RegisterUserRequest;
import com.example.security.dto.user.ResetPasswordRequest;
import com.example.security.dto.user.SearchUserRequest;
import com.example.security.dto.user.UpdatePasswordRequest;
import com.example.security.dto.user.UpdateUserRequest;
import com.example.security.entity.User;
import com.example.security.enums.UserTypeEnum;
import com.example.security.repo.UserRepo;

@Service
@Transactional
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private TokenService tokenService;
  @Autowired
  private SecurityConfigParam securityParam;

  @Override
  public User create(@Valid CreateUserRequest user) {
    try {
      User u = new User();
      PropertyUtils.copyProperties(u, user);
      u.setPassword(passwordEncoder.encode("Admin123@"));
      return userRepo.save(u);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updateAllFields(String id, @Valid UpdateUserRequest user) {
    Optional<User> oUser = userRepo.findById(id);
    if (!oUser.isPresent()) {
      throw new NotFoundException("common.error.not-found");
    } else {
      try {
        User u = oUser.get();
        PropertyUtils.copyProperties(u, user);
        userRepo.save(u);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void updateSomeFields(String id, @Valid PatchRequest<UpdateUserRequest> patchRequest) {
    Optional<User> oUser = userRepo.findById(id);
    if (!oUser.isPresent()) {
      throw new NotFoundException("common.error.not-found");
    } else {
      try {
        User u = oUser.get();
        for (String fieldName : patchRequest.getUpdateFields()) {
          Object newValue = PropertyUtils.getProperty(patchRequest.getData(), fieldName);
          PropertyUtils.setProperty(u, fieldName, newValue);
        }
        userRepo.save(u);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void deleteById(String id) {
    Optional<User> oUser = userRepo.findById(id);
    if (!oUser.isPresent()) {
      throw new NotFoundException("common.error.not-found");
    } else {
      userRepo.deleteById(id);
    }
  }

  @Override
  public int deleteByIdIn(List<String> ids) {
    int count = userRepo.deleteByIdIn(ids);
    if (count < ids.size()) {
      throw new NotFoundException("common.error.not-found");
    }
    return count;
  }

  @Override
  public void setActive(String id, boolean isActive) {
    int count = userRepo.updateIsActive(isActive, id);
    if (count == 0) {
      throw new NotFoundException("common.error.not-found");
    }
  }

  @Override
  public Page<User> advanceSearch(String filter, @Valid SearchUserRequest searchRequest, Pageable pageable) {
    if (searchRequest != null) {
      List<Specification<User>> specList = getAdvanceSearchSpecList(searchRequest);
      if (filter != null && !filter.isEmpty()) {
        specList.add(SearchUtil.like("fullTextSearch", "%" + filter + "%"));
      }
      if (specList.size() > 0) {
        Specification<User> spec = specList.get(0);
        for (int i = 1; i < specList.size(); i++) {
          spec = spec.and(specList.get(i));
        }
        return userRepo.findAll(spec, pageable);
      }
    }
    return userRepo.findAll(pageable);
  }

  private List<Specification<User>> getAdvanceSearchSpecList(@Valid SearchUserRequest s) {
    List<Specification<User>> specList = new ArrayList<>();
    if (s.getName() != null && !s.getName().isEmpty()) {
      specList.add(SearchUtil.like("name", "%" + s.getName() + "%"));
    }
    if (s.getIsActive() != null) {
      specList.add(SearchUtil.eq("isActive", s.getIsActive()));
    }
    if (s.getCreateDate() != null) {
      if (s.getCreateDate().getFromValue() != null) {
        specList.add(SearchUtil.ge("createdDate", s.getCreateDate().getFromValue()));
      }
      if (s.getCreateDate().getFromValue() != null) {
        specList.add(SearchUtil.lt("createdDate", s.getCreateDate().getToValue()));
      }
    }
    if (s.getTestSelectId() != null && s.getTestSelectId().size() > 0) {
      if (s.getTestSelectId().size() == 1) {
        specList.add(SearchUtil.eq("testSelectId", s.getTestSelectId().get(0)));
      } else {
        specList.add(SearchUtil.in("testSelectId", s.getTestSelectId()));
      }
    }
    if (s.getUserType() != null && s.getUserType().size() > 0) {
      if (s.getUserType().size() == 1) {
        specList.add(SearchUtil.eq("userType", s.getUserType().get(0)));
      } else {
        specList.add(SearchUtil.in("userType", s.getUserType()));
      }
    }
    return specList;
  }

  @Override
  public void register(@Valid RegisterUserRequest request) {
    try {
      String userName = request.getName();
      Optional<User> oUser = userRepo.findByNameIgnoreCase(userName);
      if (oUser.isPresent()) {
        throw new DuplicateKeyException("common.error.dupplicate");
      }
      User u = new User();
      PropertyUtils.copyProperties(u, request);
      u.setActive(true);
      u.setAffectDate(LocalDateTime.now());
      u.setPassword(passwordEncoder.encode(request.getPassword()));
      u.setUserType(UserTypeEnum.WebApp);
      userRepo.save(u);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updatePassword(String id, @Valid UpdatePasswordRequest request) {
    Optional<User> oUser = userRepo.findById(id);
    if (!oUser.isPresent()) {
      throw new NotFoundException("common.error.not-found");
    } else {
      String oldPwd = oUser.get().getPassword();
      if (!passwordEncoder.matches(request.getCurrentPassword(), oldPwd)) {
        throw new NotFoundException("common.error.not-found");
      }
      String pwd = passwordEncoder.encode(request.getNewPassword());
      userRepo.updatePassword(pwd, id);
    }
  }

  @Override
  public void resetPassword(@Valid ResetPasswordRequest request) {
    // Decrypt and validate token
    String token;
    try {
      token = SecurityUtil.decryptDefault(request.getResetPasswordToken());
    } catch (Exception e) {
      throw new IllegalArgumentException("common.error.not-valid-token");
    }
    if (token == null) {
      throw new IllegalArgumentException("common.error.not-valid-token");
    }
    String[] parts = token.split("\n");
    if (parts.length != 4) {
      throw new IllegalArgumentException("common.error.not-valid-token");
    }
    String userId = parts[0];
    String value = parts[1];
    long affectDate = Long.parseLong(parts[2]);
    long expireDate = Long.parseLong(parts[3]);
    long c = System.currentTimeMillis();
    if (c < affectDate || c > expireDate) {
      throw new IllegalArgumentException("common.error.not-valid-token");
    }
    boolean b = tokenService.isTokenValid(userId, value);
    if (!b) {
      throw new IllegalArgumentException("common.error.not-valid-token");
    }

    // Update password
    String pwd = passwordEncoder.encode(request.getNewPassword());
    userRepo.updatePassword(pwd, userId);
  }

  @Override
  public String recoveryRequest(@Valid @RequestBody ForgotPasswordRequest request) {
    Optional<User> oUser = userRepo.findByNameIgnoreCase(request.getName());
    if (!oUser.isPresent()) {
      throw new NotFoundException("common.error.not-found");
    } else {
      // TODO: Send email, remove return result
      String token = generateResetPasswordToken(oUser.get().getId());
      return token;
    }
  }

  private String generateResetPasswordToken(String userId) {
    try {
      String token = UUID.randomUUID().toString();
      LocalDateTime issueDate = LocalDateTime.now();
      LocalDateTime expireDate = issueDate;
      expireDate.plusSeconds(securityParam.getResetPasswordTokenExpirationSecond());
      tokenService.saveToken(userId, TokenTypeEnum.AccessToken, token, issueDate, expireDate);
      return SecurityUtil.encryptDefault(
          String.format("%s\n%s\n%d\n%d", userId, token, issueDate.atZone(ZoneId.systemDefault()).toEpochSecond(),
              expireDate.atZone(ZoneId.systemDefault()).toEpochSecond()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
