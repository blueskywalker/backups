package com.sysomos.grid.query.utils.expansion;


import org.antlr.v4.runtime.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;



/**
 * Created by kkim on 11/23/15.
 */
public class SyntaxVerifier {

    public String expansion(InputStream is) throws IOException {
        BooleanQueryLexer lexer = new BooleanQueryLexer(new ANTLRInputStream(is));
        BooleanQueryParser parser = new BooleanQueryParser(new CommonTokenStream(lexer));

        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException(String.format("%s,%d,%d,%s",offendingSymbol,line,charPositionInLine,msg),e);
            }
        });

        QueryExpansionListener listener = new QueryExpansionListener();
        parser.addParseListener(listener);

        parser.andexpr();

        return listener.query();
    }

    public static void main(String[] args) {

        SyntaxVerifier verifier = new SyntaxVerifier();
        InputStream is = SyntaxVerifier.class.getClassLoader().getResourceAsStream("samples.txt");

        try {
            String data = IOUtils.toString(is);
            for(String line : data.split("\n")) {
                if (line.startsWith("#"))
                    continue;

                System.out.println(verifier.expansion(IOUtils.toInputStream(line)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
