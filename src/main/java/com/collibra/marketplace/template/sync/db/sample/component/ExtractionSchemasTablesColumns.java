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
        LoggerFactory.getLogger(ExtractionSchemasTablesColumns.class);
    private final ApplicationConfig appConfig;

    private final JdbcTemplate pubsJdbcTemplate;
    private final JdbcTemplate nwJdbcTemplate;

    @Autowired
    public ExtractionSchemasTablesColumns(ApplicationConfig appConfig,  
                        JdbcTemplate pubsJdbcTemplate, JdbcTemplate nwJdbcTemplate) {
        this.appConfig = appConfig;
        this.pubsJdbcTemplate = pubsJdbcTemplate;
        this.nwJdbcTemplate = nwJdbcTemplate;
    }
    /**
     * Extract the information of the 'Systems-Applications-Databases' from the database.
     * @param readInput
     *
     * @return SqlRowSet Data returned by the database for the performed SQL query.
     */
    public SqlRowSet readData(String query, String dbName){
        LOGGER.info("Starting Execution of query: "+ query + " retrieving schema-table-columns for db: " + dbName);
        SqlRowSet resultSet;
        switch(dbName){
            case "pubs":
                resultSet = pubsJdbcTemplate.queryForRowSet(query);
                break;
            case "nw":
                resultSet = nwJdbcTemplate.queryForRowSet(query);
                break;
            default:
                resultSet = null;
                break;
        }
        LOGGER.info("Finished execution of query");
        return resultSet;
    }
}
