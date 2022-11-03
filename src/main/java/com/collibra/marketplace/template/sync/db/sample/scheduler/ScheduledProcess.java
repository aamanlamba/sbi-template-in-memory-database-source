/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.scheduler;

import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import com.collibra.marketplace.template.sync.db.sample.service.IntegrationProcessorService;
import com.collibra.marketplace.template.sync.db.sample.util.CustomConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledProcess.class);
    private final ApplicationConfig appConfig;
    private final IntegrationProcessorService integrationProcessorService;

    @Autowired
    public ScheduledProcess(
        ApplicationConfig appConfig, IntegrationProcessorService integrationProcessorService) {
        this.appConfig = appConfig;
        this.integrationProcessorService = integrationProcessorService;
    }

    /**
     * Sync Systems Applications Databases.
     */
    @Scheduled(cron = "${trigger.api.scheduler-cron-systemsApplicationsDatabases}")
    public void syncSystemsApplicationsDatabasesTriggeredByScheduler() {
        if (appConfig.isSchedulerCronSystemsApplicationsDatabasesEnabled()) {
            LOGGER.debug("Sync Systems Applications Databases triggered by Scheduler");
            integrationProcessorService.start(CustomConstants.IntegrationType.SYSTEMS_APPLICATION);
        }
    }

    /**
     * Sync Data Quality Metrics.
     */
    @Scheduled(cron = "${trigger.api.scheduler-cron-dataQualityMetrics}")
    public void syncDataQualityMetricsTriggeredByScheduler() {
        if (appConfig.isSchedulerCronDataQualityMetricsEnabled()) {
            LOGGER.debug("sync data quality metrics triggered by Scheduler");
            integrationProcessorService.start(CustomConstants.IntegrationType.DATA_QUALITY_METRICS);
        }
    }

    /**
     * Sync Reference Data.
     */
    @Scheduled(cron = "${trigger.api.scheduler-cron-referenceData}")
    public void syncReferenceDataTriggeredByScheduler() {
        if (appConfig.isSchedulerCronReferenceDataEnabled()) {
            LOGGER.debug("Sync reference data triggered by Scheduler");
            integrationProcessorService.start(CustomConstants.IntegrationType.REFERENCE_DATA);
        }
    }

    /**
     * Sync all processes.
     */
    @Scheduled(cron = "${trigger.api.scheduler-cron-expression}")
    public void syncAllProcessesTriggeredByScheduler() {
        if (appConfig.isSchedulerCronEnabled()) {
            LOGGER.debug("Sync all processes triggered by Scheduler");
            integrationProcessorService.start(CustomConstants.IntegrationType.ALL);
        }
    }
}
