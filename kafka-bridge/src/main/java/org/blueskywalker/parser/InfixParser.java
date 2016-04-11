package org.blueskywalker.parser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kkim on 3/18/16.
 */
public class InfixParser {

    public static List<String> tokenizer(String input) throws Exception {

        List<String> tokens = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            switch (c) {
                case ' ':
                    if (sb.length() > 0) {
                        tokens.add(sb.toString());
                        sb.setLength(0);
                    }
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    if (sb.length() > 0) {
                        tokens.add(sb.toString());
                        sb.setLength(0);
                    }
                    tokens.add(String.valueOf(c));
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    sb.append(c);
                    break;
                default:
                    throw new Exception("Illegal Character");
            }
        }

        if (sb.length() > 0)
            tokens.add(sb.toString());

        return tokens;
    }

    // exp -> factor [+|-] exp
    // factor -> term [*|/] exp
    // term -> num

    public static int findTerm(List<String> tokens) {
        for (int i=0; i<tokens.size(); i++) {
            if (tokens.get(i).equals("*") || tokens.get(i).equals("/")) {
                return i;
            }
        }
        return -1;
    }

    public static int findFactor(List<String> tokens) {
        for (int i=0; i<tokens.size(); i++) {
            if (tokens.get(i).equals("+") || tokens.get(i).equals("-")) {
                return i;
            }
        }
        return -1;
    }

    public static int exp(List<String> tokens) throws Exception {
        System.out.printf("exp:%s\n",tokens);

        int op = findFactor(tokens);

        if (op < 0)
            return factor(tokens);

        if (tokens.get(op).equals("+"))
            return factor(tokens.subList(0, op)) + exp(tokens.subList(op + 1, tokens.size()));

        return factor(tokens.subList(0, op)) - exp(tokens.subList(op + 1, tokens.size()));

    }

    public static int factor(List<String> tokens) throws Exception {
        System.out.printf("factor:%s\n",tokens);

        int op = findTerm(tokens);

        if (op < 0)
            return term(tokens);

        if (tokens.get(op).equals("*"))
            return term(tokens.subList(0, op)) * exp(tokens.subList(op + 1, tokens.size()));

        return term(tokens.subList(0, op)) / exp(tokens.subList(op + 1, tokens.size()));
    }

    public static int term(List<String> tokens) throws Exception {
        System.out.printf("term:%s\n",tokens);

        if (tokens.size() == 1)
            return Integer.parseInt(tokens.get(0));
        else
            throw new Exception("syntax error");
    }

    public static void main(String[] args) {
        try {

            List<String> tokens = InfixParser.tokenizer(" 3 + 4 * 3 + 50 / 5 + 4 * 3 - 12  ");
//            System.out.println(tokens);


            int result = InfixParser.exp(tokens);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
