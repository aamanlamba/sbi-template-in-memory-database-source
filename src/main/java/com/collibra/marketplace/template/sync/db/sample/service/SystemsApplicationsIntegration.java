/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law. You may only
 * install and use this software subject to the license agreement available at
 * https://marketplace.collibra.com/binary-code-license-agreement/. If such an agreement is not in
 * place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.service;

import com.collibra.marketplace.library.integration.CollibraAsset;
import com.collibra.marketplace.library.integration.CollibraImportApiHelper;
import com.collibra.marketplace.library.integration.constants.CollibraImportResponseType;
import com.collibra.marketplace.library.integration.interfaces.ImportIntoCollibraFromExternalSystem;
import com.collibra.marketplace.template.sync.db.sample.component.ExtractionSystemsApplications;
import com.collibra.marketplace.template.sync.db.sample.component.TransformerSystemsApplications;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class SystemsApplicationsIntegration
    implements ImportIntoCollibraFromExternalSystem {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(SystemsApplicationsIntegration.class);
    private final CollibraImportApiHelper collibraImportApiHelper;
    private final TransformerSystemsApplications systemsApplicationsDatabasesTransformer;
    private final ExtractionSystemsApplications extractionSystemsApplication;

    @Autowired
    public SystemsApplicationsIntegration(
        CollibraImportApiHelper collibraImportApiHelper,
        TransformerSystemsApplications systemsApplicationsDatabasesTransformer,
        ExtractionSystemsApplications extractionSystemsApplication) {
        this.collibraImportApiHelper = collibraImportApiHelper;
        this.systemsApplicationsDatabasesTransformer = systemsApplicationsDatabasesTransformer;
        this.extractionSystemsApplication = extractionSystemsApplication;
    }

    /**
     * Main method of the functionality, in charge of calling the extraction, transformation and loading functions.
     *
     * @param input the ID that is used to identify which import response should be retrieved.
     */
    public Object startIntegration(Object... input) {

        LOGGER.info("Started Synchronising Systems Applications Databases");
        // Extract
        SqlRowSet codeValueList = readFromExternalSystem();
        // Transform
        List<CollibraAsset> codeValueAssets = transformIntoCollibraImport(codeValueList);
        // Load
        importIntoCollibra(input[0], codeValueAssets);
        collibraImportApiHelper.importAssets(
            (UUID) input[0], codeValueAssets, CollibraImportResponseType.COUNTS);
        LOGGER.info("Finished Synchronising Systems Applications Databases");
        return null;
    }

    /**
     * Read the data From the External System.
     */
    @Override
    public SqlRowSet readFromExternalSystem(Object... readInput) {
        return extractionSystemsApplication.read();
    }

    /**
     * Transform the data in order to import into Collibra.
     */
    @Override
    public List<CollibraAsset> transformIntoCollibraImport(Object... transformInput) {
        return systemsApplicationsDatabasesTransformer.transformAssets(
            (SqlRowSet) transformInput[0]);
    }

    /**
     * Import the data into Collibra.
     */
    @Override
    public Object importIntoCollibra(Object... importInput) {
        return null;
    }
}
