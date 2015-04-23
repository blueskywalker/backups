/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwired.grid.commons.commons.solr.custom;

import java.util.Date;
import java.util.List;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TrieDateField;

/**
 *
 * @author kkim
 */
public class TrieLongDateField extends TrieDateField {


    @Override
    public List<IndexableField> createFields(SchemaField field, Object value, float boost) {
        Date ts = new Date();
        ts.setTime(Long.parseLong((String)value)*1000);
        return super.createFields(field, ts, boost);
    }
}