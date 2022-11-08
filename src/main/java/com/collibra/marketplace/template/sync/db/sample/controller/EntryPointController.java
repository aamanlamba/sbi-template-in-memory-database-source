/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.controller;

import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import com.collibra.marketplace.template.sync.db.sample.service.IntegrationProcessorService;
import com.collibra.marketplace.template.sync.db.sample.util.CustomConstants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryPointController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPointController.class);
    private final ApplicationConfig appConfig;
    private final IntegrationProcessorService integrationProcessorService;

    @Autowired
    public EntryPointController(
        ApplicationConfig appConfig,
        IntegrationProcessorService integrationProcessorService) {
        this.appConfig = appConfig;
        this.integrationProcessorService = integrationProcessorService;
    }

    @ApiOperation(value="Syncs schemas-tables-columns")
    @PostMapping(path  = "/sync/schemas-tables-columns/{dbName}", produces = "application/json")
    public String syncSchemasTablesColumnsTriggeredByApiRequest(@PathVariable("dbName") String dbName){
        LOGGER.info("Synchronization via API request (Schemas-Tables-Columns) for db: "+dbName);
        return this.integrationProcessorService.start(
            CustomConstants.IntegrationType.SCHEMAS_TABLES_COLUMNS,dbName);
    }
}
