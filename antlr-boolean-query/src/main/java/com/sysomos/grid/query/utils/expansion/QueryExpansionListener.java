package com.sysomos.grid.query.utils.expansion;

import com.sysomos.gird.query.utils.expansion.BooleanQueryBaseListener;
import com.sysomos.gird.query.utils.expansion.BooleanQueryParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kkim on 11/23/15.
 */
public class QueryExpansionListener extends BooleanQueryBaseListener {
    static final Logger logger = Logger.getLogger(QueryExpansionListener.class);
    static final Pattern fieldQueryPattern = Pattern.compile("([A-Za-z][A-Za-z0-9_]+):([^ \\r\\t\\n]+)");

    static final String HASHTAG_FIELD = "hashTags";
    static final String SCREEN_NAME_FIELD = "actorScreenName";
    static final String REF_SCREEN_NAME_FIELD = "refScreenName";

    StringBuilder output;

    public QueryExpansionListener() {
        output = new StringBuilder();
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        logger.debug(String.format("[%s:%s]",node.toString(),node.getSymbol().getType()));
        Token token = node.getSymbol();
        String query = null;
        String expansion = null;

        switch (token.getType()) {

            case  BooleanQueryParser.WORD:
                query = token.getText();
                switch (query.charAt(0)) {
                    case '#':
                        expansion=String.format("( %s OR %s:%s )",query,HASHTAG_FIELD,query.substring(1));
                        break;
                    case '@':
                        expansion=String.format("( %s OR %s:%s )",query,SCREEN_NAME_FIELD,query.substring(1));
                        break;
                    default:
                        expansion=query;
                }
                output.append(expansion+ " ");
                break;
            case  BooleanQueryParser.QUOTE:
                query = token.getText().substring(1,token.getText().length()-1);
                switch (query.charAt(0)) {
                    case '#':
                        expansion=String.format("( \"%s\" OR %s:\"%s\" )",query,HASHTAG_FIELD,query.substring(1));
                        break;
                    case '@':
                        expansion=String.format("( \"%s\" OR %s:\"%s\" )",query,SCREEN_NAME_FIELD,query.substring(1));
                        break;
                    default:
                        expansion=query;
                }
                output.append(expansion+ " ");
                break;
            case BooleanQueryParser.FIELD:
                Matcher matcher=fieldQueryPattern.matcher(token.getText());
                if(matcher.matches()) {
                    String field = matcher.group(1);
                    if (field.equals("from") || field.equals("author")) {
                        expansion = String.format("%s:%s",SCREEN_NAME_FIELD ,matcher.group(2));
                    } else if (field.equals("to")) {
                        expansion = String.format("%s:%s",REF_SCREEN_NAME_FIELD,matcher.group(2));
                    } else {
                        expansion=token.getText();
                    }
                } else {
                    expansion=token.getText();
                }
                output.append(expansion + " ");
                break;
            default:
                output.append(token.getText()+" ");
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        logger.debug(node.toString());
    }


    public String query() {
        return output.toString();
    }
}
