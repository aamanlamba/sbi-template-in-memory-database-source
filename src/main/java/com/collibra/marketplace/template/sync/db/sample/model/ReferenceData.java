/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law.
 * You may only install and use this software subject to the license agreement available at https://marketplace.collibra.com/binary-code-license-agreement/.
 * If such an agreement is not in place, you may not use the software.
 */
package com.collibra.marketplace.template.sync.db.sample.model;

import lombok.Data;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Pojo which will be used to encapsulate the parameters that will be needed in 'read' and 'transform' methods for
 * 'ReferenceData' synchronization.
 */
@Data
public class ReferenceData {

    private String schemaName;
    private String tableName;
    private SqlRowSet codeValues;

    public ReferenceData(String schemaName, String tableName) {
        this.schemaName = schemaName;
        this.tableName = tableName;
    }
}
