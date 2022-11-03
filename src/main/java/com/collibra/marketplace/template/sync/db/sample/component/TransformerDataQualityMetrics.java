/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law. You may only
 * install and use this software subject to the license agreement available at
 * https://marketplace.collibra.com/binary-code-license-agreement/. If such an agreement is not in
 * place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.component;

import com.collibra.marketplace.library.integration.CollibraAsset;
import com.collibra.marketplace.library.integration.CollibraRelation.Direction;
import com.collibra.marketplace.library.integration.constants.CollibraConstants;
import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class TransformerDataQualityMetrics {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(TransformerDataQualityMetrics.class);
    private final ApplicationConfig appConfig;

    public TransformerDataQualityMetrics(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Transform the information of the 'metrics' obtained from the database to 'Asset' objects that will be uploaded to
     * Collibra.
     *
     * @param metrics SqlRowSet containing the information from the database.
     * @return List of CollibraAssets.
     */
    public List<CollibraAsset> transformAssets(SqlRowSet metrics) {
        LOGGER.debug("Starting Transformation of Metrics into a List of CollibraAsset");
        List<CollibraAsset> metricsAssets = new ArrayList<>();

        while (metrics.next()) {
            metricsAssets.add(createMetricAsset(metrics));
        }

        LOGGER.debug("Finished Transformation of Metrics Into a List of CollibraAsset");
        return metricsAssets;
    }

    /**
     * Receives the information of an element of the set of metrics and create the corresponding 'Asset' object for it.
     *
     * @param metrics SqlRowSet pointing to a particular element that will be processed.
     * @return CollibraAsset, created 'Asset'.
     */
    private CollibraAsset createMetricAsset(SqlRowSet metrics) {

        // Auxiliary variables necessary for the creation of the 'Asset
        String dbName = metrics.getString("ELT_CATALOG_NAME");
        String columnName = metrics.getString("ELT_COLUMN_NAME");
        String type = metrics.getString("AN_TYPE");
        String tableName = metrics.getString("ELT_TABLE_NAME");
        Long value = metrics.getLong("INDV_INT_VALUE");
        Long rowCount = metrics.getLong("INDV_ROW_COUNT");

        Double thresholdHigh = metrics.getDouble("INDV_ITHRESH_HI");
        String label = metrics.getString("IND_LABEL");

        String fullName;
        if (type != null && type.equals("TABLE")) {
            fullName = String.format("%s > %s", dbName, tableName);
        } else {
            fullName = String.format("%s > %s > %s", dbName, tableName, columnName);
        }

        long passedRows;
        if (Objects.equals(label, "Duplicate Count")) {
            passedRows = rowCount - value;
        } else {
            if ((value - thresholdHigh) <= 0) {
                passedRows = value;
            } else {
                passedRows = thresholdHigh.longValue();
            }
        }

        long failedRows;
        if (value - thresholdHigh.longValue() > 0) {
            failedRows = value - thresholdHigh.longValue();
        } else {
            failedRows = 0;
        }

        long passingFraction;
        if (value - thresholdHigh <= 0) {
            passingFraction = 100;
        } else {
            passingFraction = (passedRows / (passedRows + failedRows)) * 100;
        }

        long id = metrics.getLong("INDV_PK");
        String ok = metrics.getString("INDV_ITHRESH_OK");

        // @formatter:off
        return new CollibraAsset.Builder()
                .name(Long.toString(id))
                .domainNameAndCommunityName(
                        appConfig.getTalendMetricsDomain(), appConfig.getTalendMetricsCommunity())
                .displayName(String.format("%s for %s", label, fullName))
                .type(CollibraConstants.AssetType.DATA_QUALITY_METRIC)
                .status(CollibraConstants.Status.CANDIDATE)
                .addAttributeValue(CollibraConstants.AttributeType.THRESHOLD, thresholdHigh)
                .addAttributeValue(
                        CollibraConstants.AttributeType.LOADED_ROWS,
                    (label != null && label.equals("Unique Count")) ? value : rowCount)
                .addAttributeValue(CollibraConstants.AttributeType.ROWS_PASSED, passedRows)
                .addAttributeValue(CollibraConstants.AttributeType.ROWS_FAILED, failedRows)
                .addAttributeValue(
                        CollibraConstants.AttributeType.PASSING_FRACTION, passingFraction)
                .addAttributeValue(
                        CollibraConstants.AttributeType.RESULT, Objects.equals(ok,"Y") ? "true" : "false")
                .addRelationByDomainNameAndCommunityName(
                        CollibraConstants.RelationType.ASSET_COMPLIESTO_GOVERNANCEASSET,
                        Direction.SOURCE,
                        Long.toString(id),
                        appConfig.getTalendMetricsDomain(),
                        appConfig.getTalendMetricsCommunity())
                .build();
        // @formatter:on

    }
}
