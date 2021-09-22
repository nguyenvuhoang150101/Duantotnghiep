package com.example.security.dto.user;

import java.util.List;

import com.example.common.dto.request.DateRange;
import com.example.security.enums.UserTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserRequest {
  String name;
  List<UserTypeEnum> userType;
  List<String> testSelectId;
  DateRange createDate;
  Boolean isActive;
}
