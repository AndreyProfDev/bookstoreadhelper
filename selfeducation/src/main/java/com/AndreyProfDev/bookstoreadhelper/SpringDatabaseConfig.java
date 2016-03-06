package com.AndreyProfDev.bookstoreadhelper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.annotation.PreDestroy;

@Configuration
public class SpringDatabaseConfig {

  @Value("${app.env:prod}")
  private String appEnv;

  @Bean
  public EmbeddedDatabase dataSource(){
    switch (appEnv) {
      case "test": {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("/static/env/common/dbscripts/create-db.sql")
                .addScript("/static/env/test/dbscripts/insert-data.sql")
                .build();
      }
      default: {
        return null;
      }
    }
  }

  @Bean
  public JdbcTemplate jdbcTemplate(){
    return new JdbcTemplate(dataSource());
  }

  @PreDestroy
  public void closeDatabaseConnection(){
    dataSource().shutdown();
  }
}
