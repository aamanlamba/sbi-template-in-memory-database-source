/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.component;

import com.collibra.marketplace.library.integration.CollibraAsset;
import com.collibra.marketplace.library.integration.CollibraRelation.Direction;
import com.collibra.marketplace.library.integration.constants.CollibraConstants;
import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import com.collibra.marketplace.template.sync.db.sample.model.ReferenceData;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransformerReferenceData {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerReferenceData.class);
    private final ApplicationConfig appConfig;

    @Autowired
    public TransformerReferenceData(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Transform code values from one database table to one Code Set asset representing the table and one CodeValue
     * asset per code value, with a relation to the parent Code Set asset.
     *
     * @param referenceDatas List containing a referenceData object with 'tableName' and 'codeValues' attributes: -
     *                       'tableName', the name of the database table containing code values. This will be the name
     *                       of the Code Set asset. - 'codeValues', Database result set containing all code values in
     *                       the table.
     * @return List of CollibraAsset.
     */
    public List<CollibraAsset> transformAssets(List<ReferenceData> referenceDatas) {
        LOGGER.debug("Starting transformation of codes database result set to list of Asset POJOs");
        List<CollibraAsset> codeValueAssets = new ArrayList<>();

        for (ReferenceData referenceData : referenceDatas) {
            List<CollibraAsset> codeValueAsset = transformAsset(referenceData);
            codeValueAssets.addAll(codeValueAsset);
        }
        LOGGER.debug("Finished transformation of codes database result set to list of Asset POJOs");
        return codeValueAssets;
    }

    private List<CollibraAsset> transformAsset(ReferenceData referenceData) {
        List<CollibraAsset> codeAssets = new ArrayList<>();
        // @formatter:off
        CollibraAsset codeSetAsset =
            new CollibraAsset.Builder()
                .name(referenceData.getTableName())
                .domainNameAndCommunityName(
                        appConfig.getReferenceDataDomain(),
                        appConfig.getReferenceDataCommunity())
                .type(CollibraConstants.AssetType.CODE_SET)
                .status(CollibraConstants.Status.CANDIDATE)
                .build();
        // @formatter:on
        codeAssets.add(codeSetAsset);

        while (referenceData.getCodeValues().next()) {
            String codeValueName = referenceData.getCodeValues().getString(2);
            // @formatter:off
            CollibraAsset codeValueAsset =
                new CollibraAsset.Builder()
                    .name(
                        String.format(
                                "%s>%s", referenceData.getTableName(), codeValueName))
                    .domainNameAndCommunityName(
                        appConfig.getReferenceDataDomain(),
                        appConfig.getReferenceDataCommunity())
                    .displayName(codeValueName)
                    .type(CollibraConstants.AssetType.CODE_VALUE)
                    .status(CollibraConstants.Status.CANDIDATE)
                    .addRelationByDomainNameAndCommunityName(
                        CollibraConstants.RelationType.CODEVALUE_ISPARTOF_CODESET,
                        Direction.TARGET,
                        referenceData.getTableName(),
                        appConfig.getReferenceDataDomain(),
                        appConfig.getReferenceDataCommunity())
                    .build();
            // @formatter:on
            codeAssets.add(codeValueAsset);
        }

        return codeAssets;
    }
}
