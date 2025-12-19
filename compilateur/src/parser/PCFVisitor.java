// Generated from C:/Users/siwar/Desktop/TP_Compil_final/compilateur/src/parser/PCF.g4 by ANTLR 4.13.2
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PCFParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PCFVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PCFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(PCFParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#letExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExp(PCFParser.LetExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#funExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunExp(PCFParser.FunExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#fixExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFixExp(PCFParser.FixExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#ifzExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfzExp(PCFParser.IfzExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#addSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(PCFParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#app}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApp(PCFParser.AppContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#mulDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(PCFParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by {@link PCFParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(PCFParser.PrimaryContext ctx);
}