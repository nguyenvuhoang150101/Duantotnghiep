package com.example.security.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import com.example.common.config.Constants;
import com.example.common.entity.EntityBase;
import com.example.common.util.StringUtil;
import com.example.security.enums.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "User")
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = Constants.TENANT_ID_COLUMN, contextProperty = Constants.TENANT_ID_PROP, length = Constants.ID_MAX_LENGTH)
@Table(name = "security_user", indexes = {
    @Index(name = "idx_security_user_name", columnList = "name", unique = false) })
@ToString(includeFieldNames = true, callSuper = true)
public class User extends EntityBase {
  public static final int FULL_TEXT_SEARCH_MAX_LENGTH = 1024;
  public static final int NAME_MAX_LENGTH = 50;
  public static final int NAME_MIN_LENGTH = 5;
  public static final int DISPLAY_NAME_MAX_LENGTH = 100;
  public static final int DISPLAY_NAME_MIN_LENGTH = 1;
  public static final int PASSWORD_MAX_LENGTH = 256;
  public static final int PASSWORD_MIN_LENGTH = 32;
  public static final int FAIL_LOGIN_COUNT_MIN = 0;
  public static final int FAIL_LOGIN_COUNT_MAX = 10;

  @NotNull
  @Size(max = NAME_MAX_LENGTH, min = NAME_MIN_LENGTH)
  @Pattern(regexp = "^(?=[a-zA-Z0-9._]{5,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")
  @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
  private String name;

  @NotNull
  @Size(max = DISPLAY_NAME_MAX_LENGTH, min = DISPLAY_NAME_MIN_LENGTH)
  @Column(name = "display_name", nullable = false, length = DISPLAY_NAME_MAX_LENGTH)
  private String displayName;

  @NotNull
  @Size(max = PASSWORD_MAX_LENGTH, min = PASSWORD_MIN_LENGTH)
  @JsonIgnore
  @Column(name = "password", nullable = false, length = PASSWORD_MAX_LENGTH)
  private String password;

  @Min(FAIL_LOGIN_COUNT_MIN)
  @Max(FAIL_LOGIN_COUNT_MAX)
  @Column(name = "fail_login_count")
  private Integer failLoginCount;

  @Column(name = "is_lock", nullable = false)
  private boolean isLock;

  @Column(name = "affect_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime affectDate;

  @Column(name = "expire_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime expireDate;

  @Column(name = "test_amount")
  private Long testAmount;

  @Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
  @Column(name = "test_select_id", length = Constants.ID_MAX_LENGTH)
  private String testSelectId;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "user_type")
  private UserTypeEnum userType;

  @Size(max = Constants.ID_MAX_LENGTH, min = Constants.ID_MAX_LENGTH)
  @Column(name = "tenant_id", nullable = false, length = Constants.ID_MAX_LENGTH, insertable = false, updatable = false)
  private String tenantId;

  @Column(name = "fulltext_search", length = FULL_TEXT_SEARCH_MAX_LENGTH)
  @Size(max = FULL_TEXT_SEARCH_MAX_LENGTH)
  @JsonIgnore
  private String fullTextSearch;

  @PrePersist
  @PreUpdate
  public void updateData() {
    StringBuilder sb = new StringBuilder();
    if (name != null && !name.isEmpty()) {
      sb.append("\n").append(name);
    }
    if (displayName != null && !displayName.isEmpty()) {
      sb.append("\n").append(displayName);
    }
    if (sb.length() > 1) {
      fullTextSearch = StringUtil.toFullTextSearch(sb.substring(1));
    } else {
      fullTextSearch = null;
    }
  }
}
