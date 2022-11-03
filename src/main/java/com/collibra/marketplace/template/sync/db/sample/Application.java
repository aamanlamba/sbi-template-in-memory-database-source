/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law. You may only
 * install and use this software subject to the license agreement available at
 * https://marketplace.collibra.com/binary-code-license-agreement/. If such an agreement is not in
 * place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Shows how you can adapt the result of a SQL query on a database to any desired Collibra metamodel and import it into
 * Collibra.
 */
@SpringBootApplication(scanBasePackages = {"com.collibra"})
public class Application {

    /**
     * Integration starting point.
     */
    public static void main(String[] args) {
        System.setProperty("jasypt.encryptor.algorithm", "PBEWithHmacSHA512AndAES_256");
        System.setProperty(
            "jasypt.encryptor.iv-generator-classname", "org.jasypt.iv.RandomIvGenerator");
        SpringApplication.run(Application.class, args);
    }
}
