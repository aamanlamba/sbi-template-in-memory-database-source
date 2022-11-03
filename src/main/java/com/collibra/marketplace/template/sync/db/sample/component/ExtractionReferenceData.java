/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.component;

import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import com.collibra.marketplace.template.sync.db.sample.model.ReferenceData;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class ExtractionReferenceData {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtractionReferenceData.class);
    private final ApplicationConfig appConfig;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExtractionReferenceData(ApplicationConfig appConfig, JdbcTemplate jdbcTemplate) {
        this.appConfig = appConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Extract the reference data tables and code values from database.
     */
    public List<ReferenceData> read() {

        SqlRowSet referenceDataTables = readFromQuery();
        List<ReferenceData> referenceDataList = new ArrayList<>();

        while (referenceDataTables.next()) {
            String schemaName = referenceDataTables.getString("TABLE_SCHEMA");
            String tableName = referenceDataTables.getString("TABLE_NAME");
            ReferenceData referenceData = new ReferenceData(schemaName, tableName);
            SqlRowSet referenceDataCodeValues = readCodeValues(referenceData);
            referenceData.setCodeValues(referenceDataCodeValues);
            referenceDataList.add(referenceData);
        }

        return referenceDataList;
    }

    /**
     * Extract reference data tables from the database.
     *
     * @return SqlRowSet Data returned by the database for the performed SQL query (defined in .properties file).
     */
    private SqlRowSet readFromQuery() {
        LOGGER.debug("Starting Execution of Select reference Data Tables Query");
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(appConfig.getReferenceDataDbQuery());
        LOGGER.debug("Finished Execution of Select reference Data Tables Query");
        return resultSet;
    }

    /**
     * Extract reference data code values from the database.
     *
     * @param referenceDataPojo it contains 'tableName' and 'schemaName' attributes: - 'tableName', the name of the
     *                          database table containing code values. - 'schemaName', the name of the database schema
     *                          which contains 'tableName'.
     * @return SqlRowSet Data returned by the database for the performed SQL query (built using the given schema and
     * table name).
     */
    private SqlRowSet readCodeValues(ReferenceData referenceDataPojo) {
        LOGGER.debug("Starting Execution of Select Reference Data Code Values Query");
        String sqlQuery = String.format("%s %s.%s", "SELECT * FROM ", referenceDataPojo.getSchemaName(),
            referenceDataPojo.getTableName());
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(sqlQuery);
        LOGGER.debug("Finished Execution of Select Reference Data Code Values Query");
        return resultSet;
    }
}
