/*
 * 
 */
package com.mwired.grid.commons.commons.solr.custom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

/**
 *
 * @author kkim
 */
public class DateDay extends ValueSourceParser {

    ValueSource source;
    @Override
    public ValueSource parse(FunctionQParser fqp) throws SyntaxError {
        source = fqp.parseValueSource();
        
        return new ValueSource() {

            @Override
            public FunctionValues getValues(Map map, AtomicReaderContext arc) throws IOException {
                final FunctionValues vals = source.getValues(map, arc);
                return new FunctionValues() {

                    @Override
                    public long longVal(int doc) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        cal.setTimeInMillis(vals.longVal(doc)*1000);
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        cal.clear();
                        cal.set(year,month,day);
                        return cal.getTimeInMillis();                    }

                    @Override
                    public int intVal(int doc) {
                        return (int)(longVal(doc)/1000);
                    }

                    @Override
                    public float floatVal(int doc) {
                        return (float)intVal(doc);
                    }
                    
                    
                    @Override
                    public String toString(int doc) {
                        Date day = new Date(longVal(doc));
                        return new SimpleDateFormat("yyyy-MM-dd").format(day);
                    }
                };
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return source.hashCode();
            }

            @Override
            public String description() {
                return getClass().getName();
            }
        };
        
    }
    
}
