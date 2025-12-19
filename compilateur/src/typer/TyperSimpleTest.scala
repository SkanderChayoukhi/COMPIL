package typer

import ast.Term
import ast.Term._
import ast.Op._

object TyperSimpleTest:
  @main
  def runTests(): Unit =
    def testExpr(desc: String, expr: Term): Unit =
      println(s"\n=== Test: $desc ===")
      try {
        val result = Typer.eval(expr, Map.empty)
        println(s"✓ Type: $result")
      } catch {
        case e: Exception => println(s"✗ Erreur: ${e.getMessage}")
      }

    println("Démarrage des tests de typage...")

    // Test 1: Nombre simple
    testExpr("Nombre", Number(42))

    // Test 2: Addition simple
    testExpr("Addition", BinaryTerm(Plus, Number(1), Number(2)))

    // Test 3: Fonction identité
    testExpr("Fonction identité", Fun("x", Var("x")))

    // Test 4: Application simple
    testExpr("Application",
      App(Fun("x", BinaryTerm(Plus, Var("x"), Number(1))), Number(5)))

    // Test 5: Let simple
    testExpr("Let", Let("x", Number(5), Var("x")))
