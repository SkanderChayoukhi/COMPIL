//package PCF
//
//import ast.Term
//import ast.Term.*
//import ast.Op
//import evaluator.Evaluator
//import evaluator.Evaluator.Value.*
//import generator.{Generator,ATerm}
//
//import scala.io.StdIn
//import java.io.{ByteArrayInputStream, FileInputStream, InputStream}
//import org.antlr.v4.runtime.*
//import org.antlr.v4.runtime.tree.*
//import parser.{PCFParser, PCFLexer}
//
//object PCF:
//  def main(args: Array[String]): Unit =
//    val in: InputStream =
//      if args.isEmpty then
//        val lines = Iterator.continually(StdIn.readLine())
//          .takeWhile(line => line != null && line.trim.nonEmpty)
//          .mkString(" ")
//        ByteArrayInputStream(lines.getBytes())
//      else
//        FileInputStream(args(0))
//
//    val exp = parseFromStream(in)
//    val result = Evaluator.eval(exp)
//    result match
//      case IntValue(n) => println(s"==> $n")
//      case Closure(_, _, _) => println("==> <function>")
//
//  def compile(in: InputStream): List[generator.Ins]  =
//    val term = parseFromStream(in)
//    val aterm = term.annotate(List())
//    val code = Generator.gen(aterm)
//    if check(term, code) then code
//    else throw Exception("Implementation Error")
//
//  def check(term: Term, code: List[generator.Ins]): Boolean =
//    val value = Evaluator.eval(term, Map())
//    println(value)
//    println(code)
//    val value2 = vm.VM.execute(code)
//
//    // comparaison correcte des valeurs (Int et closures)
//    (value, value2) match
//      case (IntValue(n1), vm.Value.IntVal(n2)) => n1 == n2
//      case (Closure(_, _, _), vm.Value.Closure(_, _)) => true
//      case _ => false
//
//
//  private def parseFromStream(in: InputStream): Term =
//    val input = CharStreams.fromStream(in)
//    val lexer = new PCFLexer(input)
//    val tokens = new CommonTokenStream(lexer)
//    val parser = new PCFParser(tokens)
//    val parseTree = parser.term()
//    convertToAST(parseTree)
//
//  private def convertToAST(ctx: PCFParser.TermContext): Term =
//    if ctx.letExp() != null then
//      convertLetExp(ctx.letExp())
//    else if ctx.funExp() != null then
//      convertFunExp(ctx.funExp())
//    else if ctx.fixExp() != null then
//      convertFixExp(ctx.fixExp())
//    else if ctx.addSub() != null then
//      convertAddSub(ctx.addSub())
//    else
//      throw new RuntimeException("Expression non reconnue")
//
//  private def convertLetExp(ctx: PCFParser.LetExpContext): Term =
//    val name = ctx.ID().getText
//    val t1 = convertToAST(ctx.term(0))
//    val t2 = convertToAST(ctx.term(1))
//    Let(name, t1, t2)
//
//  private def convertFunExp(ctx: PCFParser.FunExpContext): Term =
//    val param = ctx.ID().getText
//    val body = convertToAST(ctx.term())
//    Fun(param, body)
//
//  private def convertFixExp(ctx: PCFParser.FixExpContext): Term =
//    val name = ctx.ID().getText
//    val body = convertToAST(ctx.term())
//    Fix(name, body)
//
//  private def convertAddSub(ctx: PCFParser.AddSubContext): Term =
//    var result = convertApp(ctx.app(0))
//    for i <- 1 until ctx.app().size() do
//      val op = if ctx.PLUS(i-1) != null then Op.Plus else Op.Minus
//      val nextTerm = convertApp(ctx.app(i))
//      result = BinaryTerm(op, result, nextTerm)
//    result
//
//  private def convertApp(ctx: PCFParser.AppContext): Term =
//    var result = convertMulDiv(ctx.mulDiv(0))
//    for i <- 1 until ctx.mulDiv().size() do
//      val arg = convertMulDiv(ctx.mulDiv(i))
//      result = App(result, arg)
//    result
//
//  private def convertMulDiv(ctx: PCFParser.MulDivContext): Term =
//    var result = convertPrimary(ctx.primary(0))
//    for i <- 1 until ctx.primary().size() do
//      val op = if ctx.TIMES(i-1) != null then Op.Times else Op.Div
//      val nextTerm = convertPrimary(ctx.primary(i))
//      result = BinaryTerm(op, result, nextTerm)
//    result
//
//  private def convertPrimary(ctx: PCFParser.PrimaryContext): Term =
//    if ctx.NUMBER() != null then
//      Number(ctx.NUMBER().getText.toInt)
//    else if ctx.ID() != null then
//      Var(ctx.ID().getText)
//    else if ctx.ifzExp() != null then
//      convertIfzExp(ctx.ifzExp())
//    else if ctx.funExp() != null then
//      convertFunExp(ctx.funExp())
//    else if ctx.term().size() == 1 then
//      // Expression entre parenthèses
//      convertToAST(ctx.term(0))
//    else if ctx.term().size() == 3 && ctx.IFZ() != null then
//      // ifz entre parenthèses
//      val cond = convertToAST(ctx.term(0))
//      val zBranch = convertToAST(ctx.term(1))
//      val nzBranch = convertToAST(ctx.term(2))
//      IfZero(cond, zBranch, nzBranch)
//    else if ctx.term().size() == 2 then
//      // Opération binaire entre parenthèses
//      val left = convertToAST(ctx.term(0))
//      val right = convertToAST(ctx.term(1))
//      val opText = if ctx.PLUS() != null then "+"
//      else if ctx.MINUS() != null then "-"
//      else if ctx.TIMES() != null then "*"
//      else if ctx.DIV() != null then "/"
//      else throw new RuntimeException("Opérateur non reconnu")
//      BinaryTerm(Op.parse(opText), left, right)
//    else
//      throw new RuntimeException("Expression primaire non reconnue")
//
//  private def convertIfzExp(ctx: PCFParser.IfzExpContext): Term =
//    val cond = convertToAST(ctx.term(0))
//    val zBranch = convertToAST(ctx.term(1))
//    val nzBranch = convertToAST(ctx.term(2))
//    IfZero(cond, zBranch, nzBranch)



// scala
//package PCF
//
//import ast.Term
//import ast.Term.*
//import ast.Op
//import evaluator.Evaluator
//import evaluator.Evaluator.Value.*
//import generator.{Generator, ATerm}
//import scala.io.StdIn
//import java.io.{ByteArrayInputStream, FileInputStream, InputStream}
//import org.antlr.v4.runtime.*
//import org.antlr.v4.runtime.tree.*
//import parser.{PCFParser, PCFLexer}
//
//object PCF:
//
//  def main(args: Array[String]): Unit =
//    val in: InputStream =
//      if args.isEmpty then
//        val lines = Iterator.continually(StdIn.readLine())
//          .takeWhile(line => line != null && line.trim.nonEmpty)
//          .mkString(" ")
//        ByteArrayInputStream(lines.getBytes())
//      else
//        FileInputStream(args(0))
//
//    if args.contains("-i") then
//      println(s"==> ${interpret(in)}")
//    else
//      println(compile(in))
//
//  // Analyse (parsing + typage) -> retourne (Term, Type)
//  def analyze(in: InputStream): (Term, typer.Type) =
//    val term = parseFromStream(in)
//    val t = typer.Typer.eval(term, Map())
//    (term, t)
//
//  // Interprète : utilise analyze puis évalue et renvoie "valeur:type"
//  def interpret(in: InputStream): String =
//    val (term, a) = analyze(in)
//    val value = Evaluator.eval(term, Map())
//    s"$value:$a"
//
//  // Compile : utilise analyze puis génère le code (ne lance pas la VM)
//  def compile(in: InputStream): generator.Code =
//    val (term, _) = analyze(in)
//    val aterm = term.annotate(List())
//    Generator.gen(aterm)
//
//  def check(term: Term, code: List[generator.Ins]): Boolean =
//    val value = Evaluator.eval(term, Map())
//    println(value)
//    println(code)
//    val value2 = vm.VM.execute(code)
//
//    // comparaison correcte des valeurs (Int et closures)
//    (value, value2) match
//      case (IntValue(n1), vm.Value.IntVal(n2)) => n1 == n2
//      case (Closure(_, _, _), vm.Value.Closure(_, _)) => true
//      case _ => false
//
//  private def parseFromStream(in: InputStream): Term =
//    val input = CharStreams.fromStream(in)
//    val lexer = new PCFLexer(input)
//    val tokens = new CommonTokenStream(lexer)
//    val parser = new PCFParser(tokens)
//    val parseTree = parser.term()
//    convertToAST(parseTree)
//
//  private def convertToAST(ctx: PCFParser.TermContext): Term =
//    if ctx.letExp() != null then
//      convertLetExp(ctx.letExp())
//    else if ctx.funExp() != null then
//      convertFunExp(ctx.funExp())
//    else if ctx.fixExp() != null then
//      convertFixExp(ctx.fixExp())
//    else if ctx.addSub() != null then
//      convertAddSub(ctx.addSub())
//    else
//      throw new RuntimeException("Expression non reconnue")
//
//  private def convertLetExp(ctx: PCFParser.LetExpContext): Term =
//    val name = ctx.ID().getText
//    val t1 = convertToAST(ctx.term(0))
//    val t2 = convertToAST(ctx.term(1))
//    Let(name, t1, t2)
//
//  private def convertFunExp(ctx: PCFParser.FunExpContext): Term =
//    val param = ctx.ID().getText
//    val body = convertToAST(ctx.term())
//    Fun(param, body)
//
//  private def convertFixExp(ctx: PCFParser.FixExpContext): Term =
//    val name = ctx.ID().getText
//    val body = convertToAST(ctx.term())
//    Fix(name, body)
//
//  private def convertAddSub(ctx: PCFParser.AddSubContext): Term =
//    var result = convertApp(ctx.app(0))
//    for i <- 1 until ctx.app().size() do
//      val op = if ctx.PLUS(i-1) != null then Op.Plus else Op.Minus
//      val nextTerm = convertApp(ctx.app(i))
//      result = BinaryTerm(op, result, nextTerm)
//    result
//
//  private def convertApp(ctx: PCFParser.AppContext): Term =
//    var result = convertMulDiv(ctx.mulDiv(0))
//    for i <- 1 until ctx.mulDiv().size() do
//      val arg = convertMulDiv(ctx.mulDiv(i))
//      result = App(result, arg)
//    result
//
//  private def convertMulDiv(ctx: PCFParser.MulDivContext): Term =
//    var result = convertPrimary(ctx.primary(0))
//    for i <- 1 until ctx.primary().size() do
//      val op = if ctx.TIMES(i-1) != null then Op.Times else Op.Div
//      val nextTerm = convertPrimary(ctx.primary(i))
//      result = BinaryTerm(op, result, nextTerm)
//    result
//
//  private def convertPrimary(ctx: PCFParser.PrimaryContext): Term =
//    if ctx.NUMBER() != null then
//      Number(ctx.NUMBER().getText.toInt)
//    else if ctx.ID() != null then
//      Var(ctx.ID().getText)
//    else if ctx.ifzExp() != null then
//      convertIfzExp(ctx.ifzExp())
//    else if ctx.funExp() != null then
//      convertFunExp(ctx.funExp())
//    else if ctx.term().size() == 1 then
//      // Expression entre parenthèses
//      convertToAST(ctx.term(0))
//    else if ctx.term().size() == 3 && ctx.IFZ() != null then
//      // ifz entre parenthèses
//      val cond = convertToAST(ctx.term(0))
//      val zBranch = convertToAST(ctx.term(1))
//      val nzBranch = convertToAST(ctx.term(2))
//      IfZero(cond, zBranch, nzBranch)
//    else if ctx.term().size() == 2 then
//      // Opération binaire entre parenthèses
//      val left = convertToAST(ctx.term(0))
//      val right = convertToAST(ctx.term(1))
//      val opText = if ctx.PLUS() != null then "+"
//      else if ctx.MINUS() != null then "-"
//      else if ctx.TIMES() != null then "*"
//      else if ctx.DIV() != null then "/"
//      else throw new RuntimeException("Opérateur non reconnu")
//      BinaryTerm(Op.parse(opText), left, right)
//    else
//      throw new RuntimeException("Expression primaire non reconnue")
//
//  private def convertIfzExp(ctx: PCFParser.IfzExpContext): Term =
//    val cond = convertToAST(ctx.term(0))
//    val zBranch = convertToAST(ctx.term(1))
//    val nzBranch = convertToAST(ctx.term(2))
//    IfZero(cond, zBranch, nzBranch)


// scala
package PCF

import ast.Term
import ast.Term.*
import ast.Op
import evaluator.Evaluator
import evaluator.Evaluator.Value.*
import generator.{Generator, ATerm}
import scala.io.StdIn
import java.io.{ByteArrayInputStream, FileInputStream, InputStream}
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.*
import parser.{PCFParser, PCFLexer}

object PCF:

  def main(args: Array[String]): Unit =
    val in: InputStream =
      if args.isEmpty then
        val lines = Iterator.continually(StdIn.readLine())
          .takeWhile(line => line != null && line.trim.nonEmpty)
          .mkString(" ")
        ByteArrayInputStream(lines.getBytes())
      else
        FileInputStream(args(0))

    if args.contains("-i") then
      println(s"==> ${interpret(in)}")
    else
      println(compile(in))

  // Analyse (parsing + typage) -> retourne (Term, Type)
  def analyze(in: InputStream): (Term, typer.Type) =
    val term = parseFromStream(in)
    // Annotation avec indices de De Bruijn
    val aterm = term.annotate(List())
    // Typage avec le TypeChecker (supporte let-polymorphisme)
    val gtype = generator.TypeChecker.check(aterm)
    // Conversion du type générique vers le type du Typer pour compatibilité
    val ttype = convertGTypeToType(gtype)
    (term, ttype)
  
  // Conversion de GType (TypeChecker) vers Type (Typer) pour compatibilité affichage
  private def convertGTypeToType(gt: generator.GType): typer.Type =
    import generator._
    gt match
      case TInt => typer.INT
      case TFun(arg, res) => typer.FUNCTION(convertGTypeToType(arg), convertGTypeToType(res))
      case TVar(id, inst) =>
        inst match
          case Some(t) => convertGTypeToType(t)
          case None => typer.INT // fallback pour les variables non instanciées

  // Interprète : utilise analyze puis évalue et renvoie "valeur:type"
  def interpret(in: InputStream): String =
    val (term, a) = analyze(in)
    val value = Evaluator.eval(term, Map())
    s"$value:$a"

  // Compile : utilise analyze puis génère le code (ne lance pas la VM)
  def compile(in: InputStream): generator.Code =
    val (term, _) = analyze(in)
    val aterm = term.annotate(List())
    Generator.gen(aterm)

  def check(term: Term, code: List[generator.Ins]): Boolean =
    val value = Evaluator.eval(term, Map())
    println(value)
    println(code)
    val value2 = vm.VM.execute(code)

    // comparaison correcte des valeurs (Int et closures)
    (value, value2) match
      case (IntValue(n1), vm.Value.IntVal(n2)) => n1 == n2
      case (Closure(_, _, _), vm.Value.Closure(_, _)) => true
      case (Closure(_, _, _), vm.Value.RecClosure(_, _)) => true
      case _ => false

  private def parseFromStream(in: InputStream): Term =
    val input = CharStreams.fromStream(in)
    val lexer = new PCFLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new PCFParser(tokens)
    val parseTree = parser.term()
    convertToAST(parseTree)

  private def convertToAST(ctx: PCFParser.TermContext): Term =
    if ctx.letExp() != null then
      convertLetExp(ctx.letExp())
    else if ctx.funExp() != null then
      convertFunExp(ctx.funExp())
    else if ctx.fixExp() != null then
      convertFixExp(ctx.fixExp())
    else if ctx.addSub() != null then
      convertAddSub(ctx.addSub())
    else
      throw new RuntimeException("Expression non reconnue")

  private def convertLetExp(ctx: PCFParser.LetExpContext): Term =
    val name = ctx.ID().getText
    val t1 = convertToAST(ctx.term(0))
    val t2 = convertToAST(ctx.term(1))
    Let(name, t1, t2)

  private def convertFunExp(ctx: PCFParser.FunExpContext): Term =
    val param = ctx.ID().getText
    val body = convertToAST(ctx.term())
    Fun(param, body)

  private def convertFixExp(ctx: PCFParser.FixExpContext): Term =
    val name = ctx.ID().getText
    val body = convertToAST(ctx.term())
    Fix(name, body)

  private def convertAddSub(ctx: PCFParser.AddSubContext): Term =
    var result = convertApp(ctx.app(0))
    for i <- 1 until ctx.app().size() do
      val op = if ctx.PLUS(i-1) != null then Op.Plus else Op.Minus
      val nextTerm = convertApp(ctx.app(i))
      result = BinaryTerm(op, result, nextTerm)
    result

  private def convertApp(ctx: PCFParser.AppContext): Term =
    var result = convertMulDiv(ctx.mulDiv(0))
    for i <- 1 until ctx.mulDiv().size() do
      val arg = convertMulDiv(ctx.mulDiv(i))
      result = App(result, arg)
    result

  private def convertMulDiv(ctx: PCFParser.MulDivContext): Term =
    var result = convertPrimary(ctx.primary(0))
    for i <- 1 until ctx.primary().size() do
      val op = if ctx.TIMES(i-1) != null then Op.Times else Op.Div
      val nextTerm = convertPrimary(ctx.primary(i))
      result = BinaryTerm(op, result, nextTerm)
    result

  private def convertPrimary(ctx: PCFParser.PrimaryContext): Term =
    if ctx.NUMBER() != null then
      Number(ctx.NUMBER().getText.toInt)
    else if ctx.ID() != null then
      Var(ctx.ID().getText)
    else if ctx.ifzExp() != null then
      convertIfzExp(ctx.ifzExp())
    else if ctx.funExp() != null then
      convertFunExp(ctx.funExp())
    else if ctx.term().size() == 1 then
      // Expression entre parenthèses
      convertToAST(ctx.term(0))
    else if ctx.term().size() == 3 && ctx.IFZ() != null then
      // ifz entre parenthèses
      val cond = convertToAST(ctx.term(0))
      val zBranch = convertToAST(ctx.term(1))
      val nzBranch = convertToAST(ctx.term(2))
      IfZero(cond, zBranch, nzBranch)
    else if ctx.term().size() == 2 then
      // Opération binaire entre parenthèses
      val left = convertToAST(ctx.term(0))
      val right = convertToAST(ctx.term(1))
      val opText = if ctx.PLUS() != null then "+"
      else if ctx.MINUS() != null then "-"
      else if ctx.TIMES() != null then "*"
      else if ctx.DIV() != null then "/"
      else throw new RuntimeException("Opérateur non reconnu")
      BinaryTerm(Op.parse(opText), left, right)
    else
      throw new RuntimeException("Expression primaire non reconnue")

  private def convertIfzExp(ctx: PCFParser.IfzExpContext): Term =
    val cond = convertToAST(ctx.term(0))
    val zBranch = convertToAST(ctx.term(1))
    val nzBranch = convertToAST(ctx.term(2))
    IfZero(cond, zBranch, nzBranch)
