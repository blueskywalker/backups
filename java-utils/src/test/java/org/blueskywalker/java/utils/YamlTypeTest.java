package org.blueskywalker.java.utils;

import org.junit.Test;

import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by kkim on 12/15/15.
 */
public class YamlTypeTest {

    @Test
    public void testList() {
        InputStream is = YamlTypeTest.class.getClassLoader().getResourceAsStream("test.yaml");

        YamlType yaml = new YamlType(is);

        System.out.println(yaml.getYaml("map"));
        System.out.println(yaml.get("list"));
        System.out.println(new Date().toString());
    }
}