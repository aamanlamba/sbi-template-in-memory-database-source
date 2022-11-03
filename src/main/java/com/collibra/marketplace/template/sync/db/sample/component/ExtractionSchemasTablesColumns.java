package com.collibra.marketplace.template.sync.db.sample.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;

@Component
public class ExtractionSchemasTablesColumns {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ExtractionSystemsApplications.class);
    private final ApplicationConfig appConfig;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExtractionSchemasTablesColumns(ApplicationConfig appConfig, JdbcTemplate jdbcTemplate) {
        this.appConfig = appConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Extract the information of the 'Systems-Applications-Databases' from the database.
     *
     * @return SqlRowSet Data returned by the database for the performed SQL query.
     */
    public SqlRowSet read() {
        LOGGER.debug("Starting Execution of Select SchemasTablesColumns query");
        String sqlQuery = appConfig.getSchemasTablesColumnsDbQuery();
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(sqlQuery);
        LOGGER.debug("Finished Execution of Select Systems-Applications-Databases query");
        return resultSet;
    }
}