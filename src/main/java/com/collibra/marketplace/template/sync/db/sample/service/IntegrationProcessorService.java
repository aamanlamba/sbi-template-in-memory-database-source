/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law. You may only
 * install and use this software subject to the license agreement available at
 * https://marketplace.collibra.com/binary-code-license-agreement/. If such an agreement is not in
 * place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.service;

import com.collibra.marketplace.library.integration.CollibraImportApiHelper;
import com.collibra.marketplace.template.sync.db.sample.util.CustomConstants;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationProcessorService.class);
    private final CollibraImportApiHelper collibraImportApiHelper;
    private final ReferenceDataIntegration referenceDataIntegration;
    private final DataQualityMetricsIntegration
        dataQualityMetricsIntegration;
    private final SystemsApplicationsIntegration
        systemsApplicationsIntegration;
    private final SchemasTablesColumnsIntegration
        schemasTablesColumnsIntegration;
    private final Object lock = new Object();

    @Autowired
    public IntegrationProcessorService(
        CollibraImportApiHelper collibraImportApiHelper,
        ReferenceDataIntegration referenceDataIntegration,
        DataQualityMetricsIntegration
            dataQualityMetricsIntegration,
        SystemsApplicationsIntegration
            systemsApplicationsIntegration,
        SchemasTablesColumnsIntegration
            schemasTablesColumnsIntegration) {
        this.collibraImportApiHelper = collibraImportApiHelper;
        this.referenceDataIntegration = referenceDataIntegration;
        this.dataQualityMetricsIntegration =
            dataQualityMetricsIntegration;
        this.systemsApplicationsIntegration =
            systemsApplicationsIntegration;
        this.schemasTablesColumnsIntegration =
            schemasTablesColumnsIntegration;
    }

    /**
     * Main method used to synchronize the metadata.
     *
     * @param integrationType Enum representing which integration to be triggered
     */
    public String start(CustomConstants.IntegrationType integrationType) {
        synchronized (lock) {
            LOGGER.info("Started synchronizing the metadata");
            UUID uuid = UUID.randomUUID();

            switch (integrationType) {
                case REFERENCE_DATA:
                    referenceDataIntegration.startIntegration(uuid);
                    break;
                case DATA_QUALITY_METRICS:
                    dataQualityMetricsIntegration.startIntegration(uuid);
                    break;
                case SYSTEMS_APPLICATION:
                    systemsApplicationsIntegration.startIntegration(uuid);
                    break;
                case SCHEMAS_TABLES_COLUMNS:
                    schemasTablesColumnsIntegration.startIntegration(uuid);
                    break;
                default:
                    referenceDataIntegration.startIntegration(uuid);
                    dataQualityMetricsIntegration.startIntegration(uuid);
                    systemsApplicationsIntegration.startIntegration(uuid);
                    break;
            }

            return collibraImportApiHelper.getImportResponse(uuid).toString();
        }
    }
}
