package com.example.security.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.example.common.config.Constants;
import com.example.common.entity.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "UserGroupRel")
@Table(name = "security_user_group_rel")
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = Constants.TENANT_ID_COLUMN, contextProperty = Constants.TENANT_ID_PROP, length = Constants.ID_MAX_LENGTH)
@ToString(includeFieldNames = true, callSuper = true)
public class UserGroupRel extends EntityBase {
  @NotNull
  @Column(name = "user_id", nullable = false, length = Constants.ID_MAX_LENGTH)
  @Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
  private String userId;

  @JsonIgnore
  @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
      CascadeType.REFRESH }, targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "id")
  private User user;

  @NotNull
  @Column(name = "group_id", nullable = false, length = Constants.ID_MAX_LENGTH)
  @Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
  private String groupId;

  @JsonIgnore
  @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
      CascadeType.REFRESH }, targetEntity = Group.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id", insertable = false, updatable = false, referencedColumnName = "id")
  private Group group;

  @Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
  @Column(name = "tenant_id", nullable = false, length = Constants.ID_MAX_LENGTH, insertable = false, updatable = false)
  private String tenantId;
}
