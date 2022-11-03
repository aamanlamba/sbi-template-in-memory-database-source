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
import com.collibra.marketplace.template.sync.db.sample.component.ExtractionReferenceData;
import com.collibra.marketplace.template.sync.db.sample.component.TransformerReferenceData;
import com.collibra.marketplace.template.sync.db.sample.model.ReferenceData;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenceDataIntegration
    implements ImportIntoCollibraFromExternalSystem {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(ReferenceDataIntegration.class);
    private final ExtractionReferenceData extractionReferenceData;
    private final TransformerReferenceData transformerReferenceData;
    private final CollibraImportApiHelper collibraImportApiHelper;

    @Autowired
    public ReferenceDataIntegration(
        ExtractionReferenceData referenceDataTablesReader,
        TransformerReferenceData codesTransformer,
        CollibraImportApiHelper collibraImportApiHelper) {
        this.extractionReferenceData = referenceDataTablesReader;
        this.transformerReferenceData = codesTransformer;
        this.collibraImportApiHelper = collibraImportApiHelper;
    }

    /**
     * Main method of the 'Sync Reference Data' functionality, in charge of calling the extraction, transformation and
     * loading functions.
     *
     * @param input contains the ID that is used to identify which import response should be retrieved.
     */
    public Object startIntegration(Object... input) {
        LOGGER.info("Started Synchronising Reference Data Assets");
        // Extract
        List<ReferenceData> codeValueList = readFromExternalSystem();
        // Transform
        List<CollibraAsset> codeValueAssets =
            transformerReferenceData.transformAssets(codeValueList);
        // Load
        collibraImportApiHelper.importAssets(
            (UUID) input[0], codeValueAssets, CollibraImportResponseType.COUNTS);
        LOGGER.info("Finished Synchronising Reference Data Assets");
        return null;
    }

    /**
     * Read the data From the External System.
     */
    @Override
    public List<ReferenceData> readFromExternalSystem(Object... readInput) {
        return extractionReferenceData.read();
    }

    @Override
    public Object transformIntoCollibraImport(Object... transformInput) {
        return null;
    }

    /**
     * Import the data into Collibra.
     */
    @Override
    public Object importIntoCollibra(Object... importInput) {
        return null;
    }
}
