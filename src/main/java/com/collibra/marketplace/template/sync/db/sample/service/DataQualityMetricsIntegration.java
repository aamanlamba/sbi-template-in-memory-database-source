/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.service;

import com.collibra.marketplace.library.integration.CollibraAsset;
import com.collibra.marketplace.library.integration.CollibraImportApiHelper;
import com.collibra.marketplace.library.integration.constants.CollibraImportResponseType;
import com.collibra.marketplace.library.integration.interfaces.ImportIntoCollibraFromExternalSystem;
import com.collibra.marketplace.template.sync.db.sample.component.ExtractionDataQualityMetrics;
import com.collibra.marketplace.template.sync.db.sample.component.TransformerDataQualityMetrics;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

@Service
public class DataQualityMetricsIntegration
    implements ImportIntoCollibraFromExternalSystem {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(DataQualityMetricsIntegration.class);
    private final ExtractionDataQualityMetrics extractionDataQualityMetrics;
    private final TransformerDataQualityMetrics transformerDataQualityMetrics;
    private final CollibraImportApiHelper collibraImportApiHelper;

    @Autowired
    public DataQualityMetricsIntegration(
        CollibraImportApiHelper collibraImportApiHelper,
        ExtractionDataQualityMetrics metricsReader,
        TransformerDataQualityMetrics metricsTransformer) {
        this.collibraImportApiHelper = collibraImportApiHelper;
        this.extractionDataQualityMetrics = metricsReader;
        this.transformerDataQualityMetrics = metricsTransformer;
    }

    /**
     * Main method of the functionality, in charge of calling the extraction, transformation and loading functions.
     *
     * @param input contains the ID that is used to identify which import response should be retrieved.
     */
    @Override
    public Object startIntegration(Object... input) {
        LOGGER.info("Started Synchronising Data Quality Metrics");
        // Extract
        SqlRowSet codeValueList = readFromExternalSystem();
        // Transform
        List<CollibraAsset> codeValueAssets = transformIntoCollibraImport(codeValueList);
        // Load
        collibraImportApiHelper.importAssets(
            (UUID) input[0], codeValueAssets, CollibraImportResponseType.COUNTS);
        LOGGER.info("Finished syncing Data Quality Metrics");
        return null;
    }

    /**
     * Read the data From the External System.
     */
    @Override
    public SqlRowSet readFromExternalSystem(Object... readInput) {
        return extractionDataQualityMetrics.read();
    }

    /**
     * Transform the data in order to import into Collibra.
     */
    @Override
    public List<CollibraAsset> transformIntoCollibraImport(Object... transformInput) {
        return transformerDataQualityMetrics.transformAssets((SqlRowSet) transformInput[0]);
    }

    @Override
    public Object importIntoCollibra(Object... importInput) {
        return null;
    }
}
