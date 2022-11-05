package com.collibra.marketplace.template.sync.db.sample.component;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.collibra.marketplace.library.integration.CollibraAsset;
import com.collibra.marketplace.library.integration.CollibraRelation;
import com.collibra.marketplace.library.integration.constants.CollibraConstants;
import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;

@Component
public class TransformerSchemasTablesColumns {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(TransformerSchemasTablesColumns.class);
    private final ApplicationConfig appConfig;

    @Autowired
    public TransformerSchemasTablesColumns(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }


    public List<CollibraAsset> transformSchemasTables(SqlRowSet schemasTablesList){
        LOGGER.info("Starting transformation of Schemas and Tables");
        List<CollibraAsset> schemasTablesAssets = new ArrayList<>();
        while (schemasTablesList.next()){
            String schemaName = schemasTablesList.getString("schema_name");
            String tableName = schemasTablesList.getString("table_name");
            String collibraDomainName = appConfig.getSchemasTablesColumnsDomain();
            String collibraCommunityName = appConfig.getSchemasTablesColumnsCommunity();

            CollibraAsset schemaAsset = new CollibraAsset.Builder()
                                                .name(schemaName)
                                                .typeName("Schema")
                                                .domainNameAndCommunityName(
                                                   collibraDomainName,collibraCommunityName)
                                                .status(CollibraConstants.Status.CANDIDATE)
                                                .build();

            CollibraAsset tableAsset = new CollibraAsset.Builder()
                                                    .name(tableName)
                                                    .typeName("Table")
                                                    .domainNameAndCommunityName(
                                                        collibraDomainName,collibraCommunityName)
                                                    .status(CollibraConstants.Status.CANDIDATE)
                                                    .addRelation(CollibraConstants.RelationType.SCHEMA_CONTAINS_TABLE,CollibraRelation.Direction.SOURCE, 
                                                      new CollibraRelation.Builder()
                                                            .relatedAssetName(schemaName)
                                                            .relatedAssetByDomainNameAndCommunityName
                                                                (collibraDomainName, collibraCommunityName)
                                                            .build())
                                                    .build();
            schemasTablesAssets.add(schemaAsset);
            schemasTablesAssets.add(tableAsset);
        }
        return schemasTablesAssets;
    }
    /**
     * Transform each element on codeValueList to its corresponding Asset.
     *
     * @param codeValueList SqlRowSet
     * @return List of CollibraAsset.
     */
    public List<CollibraAsset> transformAssets(SqlRowSet codeValueList) {
        LOGGER.debug("Starting Transformation ofTransformerSchemasTablesColumns to Collibra Assets");
        List<CollibraAsset> codeValueAssets = new ArrayList<>();
        while (codeValueList.next()) {
            //iterate schemas
            String schemaName = codeValueList.getString("schema_name");
            String tableName = codeValueList.getString("table_name");
            String columnName = codeValueList.getString("column_name");
            String columnDataType = codeValueList.getString("data_type");
            Integer columnMaxLength = Integer.parseInt(codeValueList.getString("max_length"));
            Integer columnPrecision = Integer.parseInt(codeValueList.getString("precision"));
            String collibraDomainName = appConfig.getSchemasTablesColumnsDomain();
            String collibraCommunityName = appConfig.getSchemasTablesColumnsCommunity();
            CollibraAsset schemaAsset = new CollibraAsset.Builder()
                                                .name(schemaName)
                                                .type(CollibraConstants.AssetType.SCHEMA)
                                                .domainNameAndCommunityName(
                                                   collibraDomainName,collibraCommunityName)
                                                .status(CollibraConstants.Status.CANDIDATE)
                                                .build();

            CollibraAsset tableAsset = new CollibraAsset.Builder()
                                                    .name(tableName)
                                                    .type(CollibraConstants.AssetType.TABLE)
                                                    .domainNameAndCommunityName(
                                                        collibraDomainName,collibraCommunityName)
                                                    .status(CollibraConstants.Status.CANDIDATE)
                                                    .addRelation(CollibraConstants.RelationType.SCHEMA_CONTAINS_TABLE,CollibraRelation.Direction.SOURCE, 
                                                      new CollibraRelation.Builder()
                                                            .relatedAssetName(schemaName)
                                                            .relatedAssetByDomainNameAndCommunityName
                                                                (collibraDomainName, collibraCommunityName)
                                                            .build())
                                                    .build();
            // @formatter:off
            CollibraAsset columnAsset =    new CollibraAsset.Builder()
                    .name(columnName)
                    .type(CollibraConstants.AssetType.COLUMN)
                    .domainNameAndCommunityName(
                        collibraDomainName,collibraCommunityName)
                    .addAttributeValue(CollibraConstants.AttributeType.TECHNICAL_DATA_TYPE,columnDataType)
                    .addAttributeValue(CollibraConstants.AttributeType.MAX_LENGTH,columnMaxLength)
                    .addAttributeValue(CollibraConstants.AttributeType.DATA_TYPE_PRECISION,columnPrecision)
                    .addRelation(CollibraConstants.RelationType.COLUMN_ISPARTOF_TABLE,
                                                CollibraRelation.Direction.TARGET, 
                                                    new CollibraRelation.Builder()
                                                            .relatedAssetName(tableName)
                                                            .relatedAssetByDomainNameAndCommunityName
                                                                (collibraDomainName, collibraCommunityName)
                                                            .build())
                    .status(CollibraConstants.Status.CANDIDATE)
                    .build();
            // @formatter:on
            codeValueAssets.add(schemaAsset);
            codeValueAssets.add(tableAsset);
            codeValueAssets.add(columnAsset);
        }
        LOGGER.debug("Finished Transformation of Systems-Applications to Collibra Assets");
        return codeValueAssets;
    }
}
