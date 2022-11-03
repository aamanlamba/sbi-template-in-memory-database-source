/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.controller;

import com.collibra.marketplace.library.integration.utils.workflow.CollibraWorkflowUtils;
import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import com.collibra.marketplace.template.sync.db.sample.service.IntegrationProcessorService;
import com.collibra.marketplace.template.sync.db.sample.util.CustomConstants;
import io.swagger.annotations.ApiOperation;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryPointController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPointController.class);
    private final ApplicationConfig appConfig;
    private final CollibraWorkflowUtils referenceDataCollibraWorkflowUtils;
    private final CollibraWorkflowUtils dataQualityMetricsCollibraWorkflowUtils;
    private final CollibraWorkflowUtils systemsApplicationsDatabasesCollibraWorkflowUtils;
    private final IntegrationProcessorService integrationProcessorService;

    @Autowired
    public EntryPointController(
        ApplicationConfig appConfig,
        CollibraWorkflowUtils referenceDataCollibraWorkflowUtils,
        CollibraWorkflowUtils dataQualityMetricsCollibraWorkflowUtils,
        CollibraWorkflowUtils systemsApplicationsDatabasesCollibraWorkflowUtils,
        IntegrationProcessorService integrationProcessorService) {
        this.appConfig = appConfig;
        this.referenceDataCollibraWorkflowUtils = referenceDataCollibraWorkflowUtils;
        this.dataQualityMetricsCollibraWorkflowUtils = dataQualityMetricsCollibraWorkflowUtils;
        this.systemsApplicationsDatabasesCollibraWorkflowUtils =
            systemsApplicationsDatabasesCollibraWorkflowUtils;
        this.integrationProcessorService = integrationProcessorService;
    }

    @PostConstruct
    public void initialize() {

        this.referenceDataCollibraWorkflowUtils.intializeByDomainName(
            appConfig.getReferenceDataDomain(),
            () -> {
                LOGGER.debug("Synchronization via Collibra Workflow (reference-data)");
                integrationProcessorService.start(
                    CustomConstants.IntegrationType.REFERENCE_DATA);
            });

        this.dataQualityMetricsCollibraWorkflowUtils.intializeByDomainName(
            appConfig.getTalendMetricsDomain(),
            () -> {
                LOGGER.debug("Synchronization via Collibra Workflow (data-quality-metrics)");
                integrationProcessorService.start(
                    CustomConstants.IntegrationType.DATA_QUALITY_METRICS);
            });

        this.systemsApplicationsDatabasesCollibraWorkflowUtils.intializeByDomainName(
            appConfig.getSystemsApplicationsDatabasesDomain(),
            () -> {
                LOGGER.debug("Synchronization via Collibra Workflow (System-Applications-DB)");
                integrationProcessorService.start(
                    CustomConstants.IntegrationType.SYSTEMS_APPLICATION);
            });
    }

    @ApiOperation(value = "Syncs all process")
    @PostMapping(path = "/sync", produces = "application/json")
    public String syncAllProcessesTriggeredByApiRequest() {
        LOGGER.debug("Synchronization via API request (ALL)");
        return this.integrationProcessorService.start(CustomConstants.IntegrationType.ALL);
    }

    @ApiOperation(value = "Syncs reference-data")
    @PostMapping(path = "/sync/reference-data", produces = "application/json")
    public String syncReferenceDataTriggeredByApiRequest() {
        LOGGER.debug("Synchronization via API request (reference-data)");
        return this.integrationProcessorService.start(
            CustomConstants.IntegrationType.REFERENCE_DATA);
    }

    @ApiOperation(value = "Syncs data-quality-metrics")
    @PostMapping(path = "/sync/data-quality-metrics", produces = "application/json")
    public String syncDataQualityMetricsTriggeredByApiRequest() {
        LOGGER.debug("Synchronization via API request (data-quality-metrics)");
        return this.integrationProcessorService.start(
            CustomConstants.IntegrationType.DATA_QUALITY_METRICS);
    }

    @ApiOperation(value = "Syncs systems-applications-databases")
    @PostMapping(path = "/sync/systems-applications-databases", produces = "application/json")
    public String syncSystemsApplicationsDatabasesTriggeredByApiRequest() {
        LOGGER.debug("Synchronization via API request (Systems-Applications-Databases)");
        return this.integrationProcessorService.start(
            CustomConstants.IntegrationType.SYSTEMS_APPLICATION);
    }

    @ApiOperation(value="Syncs schemas-tables-columns")
    @PostMapping(path  = "/sync/schemas-tables-columns", produces = "application/json")
    public String syncSchemasTablesColumnsTriggeredByApiRequest(){
        LOGGER.info("Synchronization via API request (Schemas-Tables-Columns");
        return this.integrationProcessorService.start(
            CustomConstants.IntegrationType.SCHEMAS_TABLES_COLUMNS);
    }
}
