// Generated from C:/Users/siwar/Desktop/TP_Compil_final/compilateur/src/parser/PCF.g4 by ANTLR 4.13.2
package parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PCFParser}.
 */
public interface PCFListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PCFParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(PCFParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(PCFParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#letExp}.
	 * @param ctx the parse tree
	 */
	void enterLetExp(PCFParser.LetExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#letExp}.
	 * @param ctx the parse tree
	 */
	void exitLetExp(PCFParser.LetExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#funExp}.
	 * @param ctx the parse tree
	 */
	void enterFunExp(PCFParser.FunExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#funExp}.
	 * @param ctx the parse tree
	 */
	void exitFunExp(PCFParser.FunExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#fixExp}.
	 * @param ctx the parse tree
	 */
	void enterFixExp(PCFParser.FixExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#fixExp}.
	 * @param ctx the parse tree
	 */
	void exitFixExp(PCFParser.FixExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#ifzExp}.
	 * @param ctx the parse tree
	 */
	void enterIfzExp(PCFParser.IfzExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#ifzExp}.
	 * @param ctx the parse tree
	 */
	void exitIfzExp(PCFParser.IfzExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#addSub}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(PCFParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#addSub}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(PCFParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#app}.
	 * @param ctx the parse tree
	 */
	void enterApp(PCFParser.AppContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#app}.
	 * @param ctx the parse tree
	 */
	void exitApp(PCFParser.AppContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#mulDiv}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(PCFParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#mulDiv}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(PCFParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by {@link PCFParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(PCFParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PCFParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(PCFParser.PrimaryContext ctx);
}