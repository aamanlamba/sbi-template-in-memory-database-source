package com.collibra.marketplace.template.sync.db.sample.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Class to create beans to access multiple databases defined in application.properties
 * The prefix defines the properties to use
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Qualifier("pubsDataSource")
    @Primary
    @ConfigurationProperties(prefix="pubs.datasource")
    DataSource pubsDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("pubsJdbcTemplate")
    JdbcTemplate pubsJdbcTemplate(@Qualifier("pubsDataSource")DataSource pubsDataSource) {
        return new JdbcTemplate(pubsDataSource);
    }

    @Bean
    @Qualifier("nwDataSource")
    @ConfigurationProperties(prefix="nw.datasource")
    DataSource nwDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("nwJdbcTemplate")
    JdbcTemplate nwJdbcTemplate(@Qualifier("nwDataSource")DataSource nwDataSource) {
        return new JdbcTemplate(nwDataSource);
    }
}
