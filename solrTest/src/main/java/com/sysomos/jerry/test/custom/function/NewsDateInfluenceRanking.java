package com.sysomos.jerry.test.custom.function;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.IOException;
import java.util.Map;

/**
 * Created by kkim on 11/4/14.
 */
public class NewsDateInfluenceRanking extends ValueSource {

    private ValueSource source;

    public NewsDateInfluenceRanking(ValueSource source) {
        this.source=source;
    }

    @Override
    public FunctionValues getValues(Map map, final AtomicReaderContext atomicReaderContext) throws IOException {
        final FunctionValues vals=source.getValues(map,atomicReaderContext);

        return new FunctionValues() {

            @Override
            public float floatVal(int doc) {
                return (float)longVal(doc);
            }

            @Override
            public byte byteVal(int doc) {
                return (byte)longVal(doc);
            }

            @Override
            public short shortVal(int doc) {
                return (short)longVal(doc);
            }

            @Override
            public int intVal(int doc) {
                return (int)longVal(doc);
            }

            @Override
            public long longVal(int doc) {
                DateTime nowday = new DateTime(vals.longVal(doc));
                DateTime beginning= new DateTime(0);

                return Days.daysBetween(beginning,nowday).getDays();
            }

            @Override
            public String toString(int doc) {
                return "dayId(" +vals.toString(doc)+")";
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String description() {
        return getClass().getName();
    }
}
