package parser

import scala.jdk.CollectionConverters._
import ast.*
import ast.Term.*
import ast.Op.*

class ASTVisitor[AST] extends PCFBaseVisitor[AST]:

  override def visitFixExp(ctx: PCFParser.FixExpContext): AST =
    val name = ctx.ID().getText
    val body = visit(ctx.term()).asInstanceOf[Term]
    Fix(name, body).asInstanceOf[AST]

  override def visitTerm(ctx: PCFParser.TermContext): AST =
    if ctx.letExp() != null then
      visit(ctx.letExp())
    else if ctx.funExp() != null then
      visit(ctx.funExp())
    else if ctx.fixExp() != null then // ← Ajoutez cette condition
      visit(ctx.fixExp())
    else
      visit(ctx.addSub())


  override def visitFunExp(ctx: PCFParser.FunExpContext): AST =
    val param = ctx.ID().getText
    val body = visit(ctx.term()).asInstanceOf[Term]
    Fun(param, body).asInstanceOf[AST]


  override def visitApp(ctx: PCFParser.AppContext): AST =
    var result = visit(ctx.mulDiv(0)).asInstanceOf[Term]
    for i <- 1 until ctx.mulDiv().size() do
      val arg = visit(ctx.mulDiv(i)).asInstanceOf[Term]
      result = App(result, arg)
    result.asInstanceOf[AST]


  override def visitLetExp(ctx: PCFParser.LetExpContext): AST =
    val name = ctx.ID().getText
    val value = visit(ctx.term(0)).asInstanceOf[Term]
    val body = visit(ctx.term(1)).asInstanceOf[Term]
    Let(name, value, body).asInstanceOf[AST]

  override def visitIfzExp(ctx: PCFParser.IfzExpContext): AST =
    val cond = visit(ctx.term(0)).asInstanceOf[Term]
    val thenBranch = visit(ctx.term(1)).asInstanceOf[Term]
    val elseBranch = visit(ctx.term(2)).asInstanceOf[Term]
    IfZero(cond, thenBranch, elseBranch).asInstanceOf[AST]

  override def visitAddSub(ctx: PCFParser.AddSubContext): AST =
    var result = visit(ctx.app(0)).asInstanceOf[Term]
    for i <- 1 until ctx.app().size() do
      val op = if ctx.PLUS(i - 1) != null then Plus else Minus
      val right = visit(ctx.app(i)).asInstanceOf[Term]
      result = BinaryTerm(op, result, right)
    result.asInstanceOf[AST]


  override def visitMulDiv(ctx: PCFParser.MulDivContext): AST =
    var result = visit(ctx.primary(0)).asInstanceOf[Term]
    for i <- 1 until ctx.primary().size() do
      val op = if ctx.TIMES(i - 1) != null then Times else Div
      val right = visit(ctx.primary(i)).asInstanceOf[Term]
      result = BinaryTerm(op, result, right)
    result.asInstanceOf[AST]

  override def visitPrimary(ctx: PCFParser.PrimaryContext): AST =
    if ctx.NUMBER() != null then
      Number(ctx.NUMBER().getText.toInt).asInstanceOf[AST]
    else if ctx.ID() != null then
      Var(ctx.ID().getText).asInstanceOf[AST]
    else if ctx.ifzExp() != null then
      visit(ctx.ifzExp())
    else if ctx.term().size() == 1 then
      visit(ctx.term(0))
    else if ctx.IFZ() != null then
      // (ifz cond then branch else branch)
      val cond = visit(ctx.term(0)).asInstanceOf[Term]
      val thenBranch = visit(ctx.term(1)).asInstanceOf[Term]
      val elseBranch = visit(ctx.term(2)).asInstanceOf[Term]
      IfZero(cond, thenBranch, elseBranch).asInstanceOf[AST]
    else
      // (op exp1 exp2)
      val op = ctx.PLUS() match
        case null =>
          ctx.MINUS() match
            case null =>
              ctx.TIMES() match
                case null => Div
                case _ => Times
            case _ => Minus
        case _ => Plus
      val exp1 = visit(ctx.term(0)).asInstanceOf[Term]
      val exp2 = visit(ctx.term(1)).asInstanceOf[Term]
      BinaryTerm(op, exp1, exp2).asInstanceOf[AST]
