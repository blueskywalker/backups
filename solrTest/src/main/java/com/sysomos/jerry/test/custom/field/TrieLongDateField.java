package com.sysomos.jerry.test.custom.field;

import org.apache.lucene.index.IndexableField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TrieDateField;

import java.util.Date;
import java.util.List;

/**
 * Created by kkim on 11/12/14.
 */
public class TrieLongDateField extends TrieDateField {


    @Override
    public List<IndexableField> createFields(SchemaField field, Object value, float boost) {
        Date ts = new Date();
        long tmp = Long.parseLong((String)value)*1000;
        ts.setTime(tmp);
        return super.createFields(field, ts, boost);
    }
}
