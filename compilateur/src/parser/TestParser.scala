package parser

import java.io.{FileInputStream, InputStream}

@main
def main(args: String*): Unit =
  val in: InputStream =
    if args.isEmpty then
      System.in
    else
      FileInputStream(args(0))
  val concreteTree = ConcreteParser.analyze(in)
  val visitor = new ASTVisitor
  val abstractTree = visitor.visit(concreteTree).asInstanceOf[ast.Term]
  println(abstractTree)
