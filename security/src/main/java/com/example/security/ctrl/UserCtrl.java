package com.example.security.ctrl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import com.example.common.config.enums.SortOrderEnum;
import com.example.common.dto.request.PatchRequest;
import com.example.common.dto.response.InsertSuccessResponse;
import com.example.common.dto.response.PageResponse;
import com.example.common.dto.response.SuccessResponse;
import com.example.common.util.SearchUtil;
import com.example.common.util.StringUtil;
import com.example.security.dto.user.CreateUserRequest;
import com.example.security.dto.user.SearchUserRequest;
import com.example.security.dto.user.UpdateUserRequest;
import com.example.security.entity.User;
import com.example.security.enums.UserTypeEnum;
import com.example.security.repo.UserRepo;
import com.example.security.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "User Controller")
public class UserCtrl {
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public InsertSuccessResponse create(@Valid @RequestBody CreateUserRequest user) {
    User u = userService.create(user);
    return new InsertSuccessResponse(u.getId());
  }

//  @PutMapping(value = "/{id}")
  @PostMapping(value = "/{id}/put")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse updateAllFields(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest user) throws NotFoundException {
    userService.updateAllFields(id, user);
    return new SuccessResponse();
  }

//  @PatchMapping(value = "/{id}")
  @PostMapping(value = "/{id}/patch")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse updateSomeFields(@PathVariable("id") String id,
      @Valid @RequestBody PatchRequest<UpdateUserRequest> patchRequest) throws NotFoundException {
    userService.updateSomeFields(id, patchRequest);
    return new SuccessResponse();
  }

//  @DeleteMapping(value = "/{ids}")
  @PostMapping(value = "/{ids}/delete")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse deleteByIdIn(@PathVariable("ids") List<String> ids) throws SQLException  {
    userService.deleteByIdIn(ids);
    return new SuccessResponse();
  }

  @PostMapping(value = "/{id}/set-active/{is-active}")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse setActive(@PathVariable("id") String id,@PathVariable("is-active") boolean isActive) throws NotFoundException {
    userService.setActive(id, isActive);
    return new SuccessResponse();
  }

  @GetMapping(value = "/{id}")
  @ResponseBody
  public Optional<User> findById(@PathVariable("id") String id) throws NotFoundException {
    return userRepo.findById(id);
  }

  @GetMapping(value = "/search/name/{name}")
  @ResponseBody
  public Optional<User> findByName(@PathVariable("name") String name) {
    return userRepo.findByNameIgnoreCase(name);
  }

  @GetMapping(value = "/search/user-type/{userType}")
  @ResponseBody
  public PageResponse<User> findByUserType(@PathVariable("userType") UserTypeEnum userType,
      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
      @Positive @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort,
      @RequestParam(required = false) SortOrderEnum order) {
    Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
    Page<User> pageData = userRepo.findByUserType(userType, pageable);
    return new PageResponse<>(pageData);
  }

  @GetMapping
  @ResponseBody
  public PageResponse<User> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
      @Positive @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort,
      @RequestParam(required = false) SortOrderEnum order) {
    Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
    Page<User> pageData = userRepo.findAll(pageable);
    return new PageResponse<>(pageData);
  }

  @GetMapping(value = "/search")
  @ResponseBody
  public PageResponse<User> fullTextSearch(@RequestParam(required = false) String filter,
      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
      @Positive @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort,
      @RequestParam(required = false) SortOrderEnum order) {
    Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
    Page<User> pageData;
    if (filter == null || filter.length() == 0) {
      pageData = userRepo.findAll(pageable);
    } else {
      filter = StringUtil.toFullTextSearch(filter);
      pageData = userRepo.findByFullTextSearchContains(filter, pageable);
    }
    return new PageResponse<>(pageData);
  }

  @PostMapping(value = "/search")
  @ResponseBody
  public PageResponse<User> advanceSearch(@RequestParam(required = false) String filter, @Valid @RequestBody SearchUserRequest searchRequest,
      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
      @Positive @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort,
      @RequestParam(required = false) SortOrderEnum order) {
    Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
    Page<User> pageData = userService.advanceSearch(filter, searchRequest, pageable);
    return new PageResponse<User>(pageData);
  }
}
