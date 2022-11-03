/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law. You may only
 * install and use this software subject to the license agreement available at
 * https://marketplace.collibra.com/binary-code-license-agreement/. If such an agreement is not in
 * place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "trigger.api")
public class ApplicationConfig {

    private String password;
    private String username;
    private boolean schedulerCronEnabled;
    private boolean schedulerCronReferenceDataEnabled;
    private boolean schedulerCronDataQualityMetricsEnabled;
    private boolean schedulerCronSystemsApplicationsDatabasesEnabled;

    @Value("${collibra.domain.reference.data}")
    private String referenceDataDomain;

    @Value("${collibra.community.reference.data}")
    private String referenceDataCommunity;

    @Value("${collibra.domain.talend.metrics}")
    private String talendMetricsDomain;

    @Value("${collibra.community.talend.metrics}")
    private String talendMetricsCommunity;

    @Value("${collibra.domain.systemsApplicationsDatabases}")
    private String systemsApplicationsDatabasesDomain;

    @Value("${talend.metrics.database.query.select}")
    private String talendMetricsDbQuery;

    @Value("${query.select.reference.data.tables}")
    private String referenceDataDbQuery;

    @Value("${query.select.systemsApplicationsDatabases.data}")
    private String systemsApplicationsDatabasesDbQuery;

    @Value("${collibra.domain.systemsApplicationsDatabases}")
    private String systemsApplicationDatabasesDomain;

    @Value("${collibra.community.systemsApplicationsDatabases}")
    private String systemsApplicationDatabasesCommunity;

    @Value("${query.select.schemasTablesColumns.data}")
    private String schemasTablesColumnsDbQuery;

    @Value("${collibra.domain.schemasTablesColumns}")
    private String schemasTablesColumnsDomain;

    @Value("${collibra.community.schemasTablesColumns}")
    private String schemasTablesColumnsCommunity;

}
