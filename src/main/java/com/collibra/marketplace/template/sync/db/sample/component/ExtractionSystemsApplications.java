/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.component;

import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class ExtractionSystemsApplications {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ExtractionSystemsApplications.class);
    private final ApplicationConfig appConfig;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExtractionSystemsApplications(ApplicationConfig appConfig, JdbcTemplate jdbcTemplate) {
        this.appConfig = appConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Extract the information of the 'Systems-Applications-Databases' from the database.
     *
     * @return SqlRowSet Data returned by the database for the performed SQL query.
     */
    public SqlRowSet read() {
        LOGGER.debug("Starting Execution of Select Systems-Applications-Databases query");
        String sqlQuery = appConfig.getSystemsApplicationsDatabasesDbQuery();
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(sqlQuery);
        LOGGER.debug("Finished Execution of Select Systems-Applications-Databases query");
        return resultSet;
    }
}
