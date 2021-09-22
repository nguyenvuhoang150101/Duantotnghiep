package com.example.security.ctrl;

import javax.validation.Valid;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.dto.response.SuccessResponse;
import com.example.common.model.CustomUserDetails;
import com.example.security.dto.user.CurrentUserResponse;
import com.example.security.dto.user.ForgotPasswordRequest;
import com.example.security.dto.user.RegisterUserRequest;
import com.example.security.dto.user.ResetPasswordRequest;
import com.example.security.dto.user.UpdatePasswordRequest;
import com.example.security.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "User Management Controller")
public class UserMgtCtrl {
  @Autowired
  private UserService userService;
  
  @GetMapping("/me")
  @ResponseBody
  public CurrentUserResponse currentUser() {
    CustomUserDetails authUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    CurrentUserResponse ret = new CurrentUserResponse();
    ret.setId(authUser.getId());
    ret.setUserName(authUser.getUsername());
    ret.setDisplayName(authUser.getDisplayName());
    return ret;
  }

  @PostMapping("/register")
  @ResponseBody
  public SuccessResponse register(@Valid @RequestBody RegisterUserRequest request) {
    userService.register(request);
    return new SuccessResponse();
  }

  @PostMapping(value = "/reset-password")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    userService.resetPassword(request);
    return new SuccessResponse();
  }

  @PostMapping(value = "/forgot-password")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    String token = userService.recoveryRequest(request);
    throw new NotImplementedException(token);
  }

  @PostMapping(value = "/{id}/update-password")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse updatePassword(@PathVariable("id") String id,
      @Valid @RequestBody UpdatePasswordRequest request) {
    userService.updatePassword(id, request);
    return new SuccessResponse();
  }
}
