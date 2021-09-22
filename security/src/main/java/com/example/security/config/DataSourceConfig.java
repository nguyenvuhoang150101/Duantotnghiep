	package com.example.security.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.common.config.MultiTenantEntityManagerFactoryBean;
import com.example.common.config.MultiTenantTransactionManager;

@Configuration
//@EnableLoadTimeWeaving(aspectjWeaving = AspectJWeaving.ENABLED)
@EnableJpaRepositories(basePackages = { DataSourceConfig.PACKAGE_REPO })
public class DataSourceConfig {
  public static final String PACKAGE_REPO = SecurityConstants.PACKAGE_BASE + ".repo";
  public static final String PACKAGE_ENTITY = SecurityConstants.PACKAGE_BASE + ".entity";
  public static final String TARGET_DB = TargetDatabase.MySQL4;
  @Value("${app.dao.ddl-generation:none}")
  private String ddlGeneration;
  @Value("${app.dao.eclipselink.logging.level}")
  private String elLoggingLevel;
  @Bean
  PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  @Bean
  JpaVendorAdapter jpaAdapter() {
    return new EclipseLinkJpaVendorAdapter();
  }

  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaAdapter) {
    LocalContainerEntityManagerFactoryBean ret = new MultiTenantEntityManagerFactoryBean();
//    LocalContainerEntityManagerFactoryBean ret = new LocalContainerEntityManagerFactoryBean();
    ret.setPackagesToScan(new String[] { DataSourceConfig.PACKAGE_ENTITY });
    ret.setDataSource(dataSource);
    ret.setJpaVendorAdapter(jpaAdapter);
    ret.setJpaPropertyMap(getVendorProperties());
    return ret;
  }

  @Bean
  PlatformTransactionManager transactionManager(DataSource dataSource, JpaVendorAdapter jpaAdapter) {
    JpaTransactionManager ret = new MultiTenantTransactionManager();
    ret.setEntityManagerFactory(entityManagerFactory(dataSource, jpaAdapter).getObject());
    return ret;
  }

  private Map<String, Object> getVendorProperties() {
    HashMap<String, Object> map = new HashMap<>();
    // Add this line to server launch configuration:
    // -javaagent:spring-instrument-5.2.5.RELEASE.jar
    map.put(PersistenceUnitProperties.WEAVING, "false");
    map.put(PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES, "true");
    map.put(PersistenceUnitProperties.DDL_GENERATION, ddlGeneration);
    map.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);
    map.put(PersistenceUnitProperties.TARGET_DATABASE, TARGET_DB);
    map.put(PersistenceUnitProperties.LOGGING_LEVEL, elLoggingLevel);
    map.put(PersistenceUnitProperties.BATCH_WRITING_SIZE, "1000");
    map.put(PersistenceUnitProperties.BATCH_WRITING, "JDBC");
    map.put(PersistenceUnitProperties.CACHE_STATEMENTS, "true");
    map.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, "false");
    map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT, "true");
    map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE, FlushModeType.COMMIT.name());
    map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT, "false");
    return map;
  }
}
