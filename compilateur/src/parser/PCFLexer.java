// Generated from C:/Users/siwar/Desktop/TP_Compil_final/compilateur/src/parser/PCF.g4 by ANTLR 4.13.2
package parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class PCFLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LET=1, IN=2, FIX=3, FUN=4, ARROW=5, IFZ=6, THEN=7, ELSE=8, EQUALS=9, PLUS=10, 
		MINUS=11, TIMES=12, DIV=13, LPAR=14, RPAR=15, NUMBER=16, ID=17, WS=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LET", "IN", "FIX", "FUN", "ARROW", "IFZ", "THEN", "ELSE", "EQUALS", 
			"PLUS", "MINUS", "TIMES", "DIV", "LPAR", "RPAR", "NUMBER", "ID", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'let'", "'in'", "'fix'", "'fun'", "'->'", "'ifz'", "'then'", "'else'", 
			"'='", "'+'", "'-'", "'*'", "'/'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LET", "IN", "FIX", "FUN", "ARROW", "IFZ", "THEN", "ELSE", "EQUALS", 
			"PLUS", "MINUS", "TIMES", "DIV", "LPAR", "RPAR", "NUMBER", "ID", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public PCFLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PCF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0012f\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0004\u000fU\b\u000f\u000b\u000f\f\u000fV\u0001\u0010\u0001"+
		"\u0010\u0005\u0010[\b\u0010\n\u0010\f\u0010^\t\u0010\u0001\u0011\u0004"+
		"\u0011a\b\u0011\u000b\u0011\f\u0011b\u0001\u0011\u0001\u0011\u0000\u0000"+
		"\u0012\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006"+
		"\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"+
		"\u001d\u000f\u001f\u0010!\u0011#\u0012\u0001\u0000\u0004\u0001\u00000"+
		"9\u0002\u0000AZaz\u0003\u000009AZaz\u0003\u0000\t\n\r\r  h\u0000\u0001"+
		"\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005"+
		"\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001"+
		"\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000"+
		"\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000"+
		"\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000"+
		"\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000"+
		"\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000"+
		"\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000"+
		"\u0000\u0000#\u0001\u0000\u0000\u0000\u0001%\u0001\u0000\u0000\u0000\u0003"+
		")\u0001\u0000\u0000\u0000\u0005,\u0001\u0000\u0000\u0000\u00070\u0001"+
		"\u0000\u0000\u0000\t4\u0001\u0000\u0000\u0000\u000b7\u0001\u0000\u0000"+
		"\u0000\r;\u0001\u0000\u0000\u0000\u000f@\u0001\u0000\u0000\u0000\u0011"+
		"E\u0001\u0000\u0000\u0000\u0013G\u0001\u0000\u0000\u0000\u0015I\u0001"+
		"\u0000\u0000\u0000\u0017K\u0001\u0000\u0000\u0000\u0019M\u0001\u0000\u0000"+
		"\u0000\u001bO\u0001\u0000\u0000\u0000\u001dQ\u0001\u0000\u0000\u0000\u001f"+
		"T\u0001\u0000\u0000\u0000!X\u0001\u0000\u0000\u0000#`\u0001\u0000\u0000"+
		"\u0000%&\u0005l\u0000\u0000&\'\u0005e\u0000\u0000\'(\u0005t\u0000\u0000"+
		"(\u0002\u0001\u0000\u0000\u0000)*\u0005i\u0000\u0000*+\u0005n\u0000\u0000"+
		"+\u0004\u0001\u0000\u0000\u0000,-\u0005f\u0000\u0000-.\u0005i\u0000\u0000"+
		"./\u0005x\u0000\u0000/\u0006\u0001\u0000\u0000\u000001\u0005f\u0000\u0000"+
		"12\u0005u\u0000\u000023\u0005n\u0000\u00003\b\u0001\u0000\u0000\u0000"+
		"45\u0005-\u0000\u000056\u0005>\u0000\u00006\n\u0001\u0000\u0000\u0000"+
		"78\u0005i\u0000\u000089\u0005f\u0000\u00009:\u0005z\u0000\u0000:\f\u0001"+
		"\u0000\u0000\u0000;<\u0005t\u0000\u0000<=\u0005h\u0000\u0000=>\u0005e"+
		"\u0000\u0000>?\u0005n\u0000\u0000?\u000e\u0001\u0000\u0000\u0000@A\u0005"+
		"e\u0000\u0000AB\u0005l\u0000\u0000BC\u0005s\u0000\u0000CD\u0005e\u0000"+
		"\u0000D\u0010\u0001\u0000\u0000\u0000EF\u0005=\u0000\u0000F\u0012\u0001"+
		"\u0000\u0000\u0000GH\u0005+\u0000\u0000H\u0014\u0001\u0000\u0000\u0000"+
		"IJ\u0005-\u0000\u0000J\u0016\u0001\u0000\u0000\u0000KL\u0005*\u0000\u0000"+
		"L\u0018\u0001\u0000\u0000\u0000MN\u0005/\u0000\u0000N\u001a\u0001\u0000"+
		"\u0000\u0000OP\u0005(\u0000\u0000P\u001c\u0001\u0000\u0000\u0000QR\u0005"+
		")\u0000\u0000R\u001e\u0001\u0000\u0000\u0000SU\u0007\u0000\u0000\u0000"+
		"TS\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000"+
		"\u0000VW\u0001\u0000\u0000\u0000W \u0001\u0000\u0000\u0000X\\\u0007\u0001"+
		"\u0000\u0000Y[\u0007\u0002\u0000\u0000ZY\u0001\u0000\u0000\u0000[^\u0001"+
		"\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000"+
		"]\"\u0001\u0000\u0000\u0000^\\\u0001\u0000\u0000\u0000_a\u0007\u0003\u0000"+
		"\u0000`_\u0001\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000b`\u0001\u0000"+
		"\u0000\u0000bc\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000de\u0006"+
		"\u0011\u0000\u0000e$\u0001\u0000\u0000\u0000\u0004\u0000V\\b\u0001\u0006"+
		"\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}