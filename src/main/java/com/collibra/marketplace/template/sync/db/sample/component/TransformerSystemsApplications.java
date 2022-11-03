/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.component;

import com.collibra.marketplace.library.integration.CollibraAsset;
import com.collibra.marketplace.library.integration.constants.CollibraConstants;
import com.collibra.marketplace.template.sync.db.sample.config.ApplicationConfig;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class TransformerSystemsApplications {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(TransformerSystemsApplications.class);
    private final ApplicationConfig appConfig;

    @Autowired
    public TransformerSystemsApplications(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Transform each element on codeValueList to its corresponding Asset.
     *
     * @param codeValueList SqlRowSet
     * @return List of CollibraAsset.
     */
    public List<CollibraAsset> transformAssets(SqlRowSet codeValueList) {
        LOGGER.debug("Starting Transformation of Systems-Applications to Collibra Assets");
        List<CollibraAsset> codeValueAssets = new ArrayList<>();

        while (codeValueList.next()) {
            String assetName = codeValueList.getString("Name");
            String assetType = codeValueList.getString("Type");

            // @formatter:off
            CollibraAsset codeValueAsset =
                new CollibraAsset.Builder()
                    .name(assetName)
                    .domainNameAndCommunityName(
                        appConfig.getSystemsApplicationDatabasesDomain(),
                        appConfig.getSystemsApplicationDatabasesCommunity())
                    .typeName(assetType)
                    .status(CollibraConstants.Status.CANDIDATE)
                    .build();
            // @formatter:on
            codeValueAssets.add(codeValueAsset);
        }
        LOGGER.debug("Finished Transformation of Systems-Applications to Collibra Assets");
        return codeValueAssets;
    }
}
