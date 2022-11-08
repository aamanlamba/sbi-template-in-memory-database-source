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
    private final SchemasTablesColumnsIntegration
        schemasTablesColumnsIntegration;
    private final Object lock = new Object();

    @Autowired
    public IntegrationProcessorService(
        CollibraImportApiHelper collibraImportApiHelper,
        SchemasTablesColumnsIntegration
            schemasTablesColumnsIntegration) {
        this.collibraImportApiHelper = collibraImportApiHelper;
        this.schemasTablesColumnsIntegration =
            schemasTablesColumnsIntegration;
    }

    /**
     * Main method used to synchronize the metadata.
     *
     * @param integrationType Enum representing which integration to be triggered
     * @param dbName
     */
    public String start(CustomConstants.IntegrationType integrationType, String dbName) {
        synchronized (lock) {
            LOGGER.info("Started synchronizing the metadata");
            UUID uuid = UUID.randomUUID();

            switch (integrationType) {
                case SCHEMAS_TABLES_COLUMNS:
                    schemasTablesColumnsIntegration.startIntegration(uuid,dbName);
                    break;
                default:
                    break;
            }

            return collibraImportApiHelper.getImportResponse(uuid).toString();
        }
    }
}
