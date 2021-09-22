package com.example.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.security.entity.Group;

@Transactional
@Repository
public interface GroupRepo extends JpaRepository<Group, String>, JpaSpecificationExecutor<Group> {

}
