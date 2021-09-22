package com.example.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.example.common.config.Constants;
import com.example.common.entity.EntityBase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Group")
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = Constants.TENANT_ID_COLUMN, contextProperty = Constants.TENANT_ID_PROP, length = Constants.ID_MAX_LENGTH)
@Table(name = "security_group", indexes = {
    @Index(name = "idx_security_group_name", columnList = "name", unique = false) })
@ToString(includeFieldNames = true, callSuper = true)
public class Group extends EntityBase {
  public static final int FULL_TEXT_SEARCH_MAX_LENGTH = 1024;
  public static final int NAME_MAX_LENGTH = 50;
  public static final int NAME_MIN_LENGTH = 5;

  @NotNull
  @Size(max = NAME_MAX_LENGTH, min = NAME_MIN_LENGTH)
  @Column(name = "name", length = NAME_MAX_LENGTH)
  private String name;

  @Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
  @Column(name = "tenant_id", nullable = false, length = Constants.ID_MAX_LENGTH, insertable = false, updatable = false)
  private String tenantId;

  @Column(name = "fulltext_search", length = FULL_TEXT_SEARCH_MAX_LENGTH)
  @Size(max = FULL_TEXT_SEARCH_MAX_LENGTH)
  private String fullTextSearch;
}
