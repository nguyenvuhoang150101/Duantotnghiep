package com.example.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.security.entity.UserGroupRel;

@Transactional
@Repository
public interface UserGroupRelRepo
    extends JpaRepository<UserGroupRel, String>, JpaSpecificationExecutor<UserGroupRel> {
  List<UserGroupRel> findByUserId(String userId);

  List<UserGroupRel> findByGroupId(String groupId);

  @Modifying
  long removeByUserId(String userId);

  @Modifying
  long removeByGroupId(String groupId);
}
