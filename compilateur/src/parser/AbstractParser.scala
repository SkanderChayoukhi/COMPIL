package parser

import java.io.InputStream
import ast.Term

object AbstractParser :
  def analyze(in: InputStream): Term =
    val concreteTree = ConcreteParser.analyze(in)
    val visitor = new ASTVisitor
    visitor.visit(concreteTree).asInstanceOf[Term]
 
