package com.example.security.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.security.entity.User;
import com.example.security.enums.UserTypeEnum;

@Transactional
@Repository
public interface UserRepo extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
  Optional<User> findByName(String name);
  Optional<User> findByNameIgnoreCase(String name);

  Page<User> findByUserType(UserTypeEnum userType, Pageable pageable);

  Page<User> findByFullTextSearchContains(String filter, Pageable pageable);
  
  @Modifying
  @Query("update User u set u.password = :pwd where u.id = :id")
  int updatePassword(@Param("pwd") String pwd, @Param("id") String id);
  
  @Modifying
  @Query("update User u set u.isActive = :isActive where u.id = :id")
  int updateIsActive(@Param("isActive") boolean isActive, @Param("id") String id);
  
  @Modifying
  int deleteByIdIn(List<String> ids);
}
