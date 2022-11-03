package com.collibra.marketplace.template.sync.db.sample.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.collibra.marketplace.library.integration.CollibraAsset;
import com.collibra.marketplace.library.integration.CollibraImportApiHelper;
import com.collibra.marketplace.library.integration.constants.CollibraImportResponseType;
import com.collibra.marketplace.library.integration.interfaces.ImportIntoCollibraFromExternalSystem;
import com.collibra.marketplace.template.sync.db.sample.component.ExtractionSchemasTablesColumns;
import com.collibra.marketplace.template.sync.db.sample.component.TransformerSchemasTablesColumns;

@Component
public class SchemasTablesColumnsIntegration implements ImportIntoCollibraFromExternalSystem {
    private static final Logger LOGGER =
        LoggerFactory.getLogger(SchemasTablesColumnsIntegration.class);
    private final CollibraImportApiHelper collibraImportApiHelper;
    private final TransformerSchemasTablesColumns schemasTablesColumnsTransformer;
    private final ExtractionSchemasTablesColumns extractionSchemasTablesColumns;

    @Autowired
    public SchemasTablesColumnsIntegration(
        CollibraImportApiHelper collibraImportApiHelper,
        TransformerSchemasTablesColumns schemasTablesColumnsTransformer,
        ExtractionSchemasTablesColumns extractionSchemasTablesColumns) {
        this.collibraImportApiHelper = collibraImportApiHelper;
        this.schemasTablesColumnsTransformer = schemasTablesColumnsTransformer;
        this.extractionSchemasTablesColumns = extractionSchemasTablesColumns;
    }

    /**
     * Main method of the functionality, in charge of calling the extraction, transformation and loading functions.
     *
     * @param input the ID that is used to identify which import response should be retrieved.
     */
    public Object startIntegration(Object... input) {

        LOGGER.info("Started Synchronising SchemasTablesColumns");
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
        return extractionSchemasTablesColumns.read();
    }

    /**
     * Transform the data in order to import into Collibra.
     */
    @Override
    public List<CollibraAsset> transformIntoCollibraImport(Object... transformInput) {
        return schemasTablesColumnsTransformer.transformAssets(
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
