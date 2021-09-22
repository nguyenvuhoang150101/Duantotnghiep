package com.example.security.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.common.dto.request.PatchRequest;
import com.example.security.dto.user.CreateUserRequest;
import com.example.security.dto.user.ForgotPasswordRequest;
import com.example.security.dto.user.RegisterUserRequest;
import com.example.security.dto.user.ResetPasswordRequest;
import com.example.security.dto.user.SearchUserRequest;
import com.example.security.dto.user.UpdatePasswordRequest;
import com.example.security.dto.user.UpdateUserRequest;
import com.example.security.entity.User;

public interface UserService {

  User create(CreateUserRequest user);

  void updateAllFields(String id, UpdateUserRequest user);

  void updateSomeFields(String id, @Valid PatchRequest<UpdateUserRequest> patchRequest);

  void deleteById(String id);

  int deleteByIdIn(List<String> ids);

  void setActive(String id, boolean isActive);

  Page<User> advanceSearch(String filter, SearchUserRequest searchRequest, Pageable pageable);

  void register(@Valid RegisterUserRequest request);

  void updatePassword(String id, @Valid UpdatePasswordRequest request);

  void resetPassword(@Valid ResetPasswordRequest request);

  String recoveryRequest(@Valid ForgotPasswordRequest request);

}