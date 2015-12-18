// Generated from org/blueskywalker/antlr/hello/RuleSetGrammar.g4 by ANTLR 4.3
package org.blueskywalker.antlr.hello;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RuleSetGrammarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IF=1, THEN=2, AND=3, OR=4, TRUE=5, FALSE=6, MULT=7, DIV=8, PLUS=9, MINUS=10, 
		GT=11, GE=12, LT=13, LE=14, EQ=15, LPAREN=16, RPAREN=17, DECIMAL=18, IDENTIFIER=19, 
		SEMI=20, COMMENT=21, WS=22;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'"
	};
	public static final String[] ruleNames = {
		"IF", "THEN", "AND", "OR", "TRUE", "FALSE", "MULT", "DIV", "PLUS", "MINUS", 
		"GT", "GE", "LT", "LE", "EQ", "LPAREN", "RPAREN", "DECIMAL", "IDENTIFIER", 
		"SEMI", "COMMENT", "WS"
	};


	public RuleSetGrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "RuleSetGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\30\u008d\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r"+
		"\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\5\23b\n\23"+
		"\3\23\6\23e\n\23\r\23\16\23f\3\23\3\23\6\23k\n\23\r\23\16\23l\5\23o\n"+
		"\23\3\24\3\24\7\24s\n\24\f\24\16\24v\13\24\3\25\3\25\3\26\3\26\3\26\3"+
		"\26\6\26~\n\26\r\26\16\26\177\3\26\5\26\u0083\n\26\3\26\3\26\3\27\6\27"+
		"\u0088\n\27\r\27\16\27\u0089\3\27\3\27\3\177\2\30\3\3\5\4\7\5\t\6\13\7"+
		"\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25"+
		")\26+\27-\30\3\2\7\3\2\62;\5\2C\\aac|\6\2\62;C\\aac|\3\3\f\f\5\2\13\f"+
		"\16\17\"\"\u0093\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\3/\3\2\2\2\5\62\3\2\2\2\7\67\3\2\2\2\t;\3\2\2\2\13>\3\2\2\2\rB"+
		"\3\2\2\2\17H\3\2\2\2\21J\3\2\2\2\23L\3\2\2\2\25N\3\2\2\2\27P\3\2\2\2\31"+
		"R\3\2\2\2\33U\3\2\2\2\35W\3\2\2\2\37Z\3\2\2\2!\\\3\2\2\2#^\3\2\2\2%a\3"+
		"\2\2\2\'p\3\2\2\2)w\3\2\2\2+y\3\2\2\2-\u0087\3\2\2\2/\60\7k\2\2\60\61"+
		"\7h\2\2\61\4\3\2\2\2\62\63\7v\2\2\63\64\7j\2\2\64\65\7g\2\2\65\66\7p\2"+
		"\2\66\6\3\2\2\2\678\7c\2\289\7p\2\29:\7f\2\2:\b\3\2\2\2;<\7q\2\2<=\7t"+
		"\2\2=\n\3\2\2\2>?\7v\2\2?@\7t\2\2@A\7w\2\2A\f\3\2\2\2BC\7h\2\2CD\7c\2"+
		"\2DE\7n\2\2EF\7u\2\2FG\7g\2\2G\16\3\2\2\2HI\7,\2\2I\20\3\2\2\2JK\7\61"+
		"\2\2K\22\3\2\2\2LM\7-\2\2M\24\3\2\2\2NO\7/\2\2O\26\3\2\2\2PQ\7@\2\2Q\30"+
		"\3\2\2\2RS\7@\2\2ST\7?\2\2T\32\3\2\2\2UV\7>\2\2V\34\3\2\2\2WX\7>\2\2X"+
		"Y\7?\2\2Y\36\3\2\2\2Z[\7?\2\2[ \3\2\2\2\\]\7*\2\2]\"\3\2\2\2^_\7+\2\2"+
		"_$\3\2\2\2`b\7/\2\2a`\3\2\2\2ab\3\2\2\2bd\3\2\2\2ce\t\2\2\2dc\3\2\2\2"+
		"ef\3\2\2\2fd\3\2\2\2fg\3\2\2\2gn\3\2\2\2hj\7\60\2\2ik\t\2\2\2ji\3\2\2"+
		"\2kl\3\2\2\2lj\3\2\2\2lm\3\2\2\2mo\3\2\2\2nh\3\2\2\2no\3\2\2\2o&\3\2\2"+
		"\2pt\t\3\2\2qs\t\4\2\2rq\3\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2u(\3\2\2"+
		"\2vt\3\2\2\2wx\7=\2\2x*\3\2\2\2yz\7\61\2\2z{\7\61\2\2{}\3\2\2\2|~\13\2"+
		"\2\2}|\3\2\2\2~\177\3\2\2\2\177\u0080\3\2\2\2\177}\3\2\2\2\u0080\u0082"+
		"\3\2\2\2\u0081\u0083\t\5\2\2\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084"+
		"\u0085\b\26\2\2\u0085,\3\2\2\2\u0086\u0088\t\6\2\2\u0087\u0086\3\2\2\2"+
		"\u0088\u0089\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008b"+
		"\3\2\2\2\u008b\u008c\b\27\2\2\u008c.\3\2\2\2\13\2aflnt\177\u0082\u0089"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}