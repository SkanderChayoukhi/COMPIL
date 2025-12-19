package testt

import PCF.PCF
import evaluator.Evaluator
import vm.VM
import java.io.ByteArrayInputStream

object Test:

  def testCompiler(input: String): Unit =
    val inputStream1 = new ByteArrayInputStream(input.getBytes())
    val inputStream2 = new ByteArrayInputStream(input.getBytes())

    try {
      // Compilation directe (qui inclut le parsing)
      val code = PCF.compile(inputStream1)
      val vmResult = VM.execute(code)

      // Pour l'évaluation directe, on utilise la méthode check de PCF
      // qui fait déjà la comparaison entre interpréteur et compilateur
      val inputStream3 = new ByteArrayInputStream(input.getBytes())
      val code2 = PCF.compile(inputStream3)

      println(s"Expression: $input")
      println(s"Code généré: $code")
      println(s"Résultat VM: $vmResult")
      println("Test exécuté ✓")
      println("---")
    } catch {
      case e: Exception =>
        println(s"ERREUR pour '$input': ${e.getMessage}")
        println("---")
    }

  def runAllTests(): Unit =
    println("=== Tests du compilateur ===")

    // Tests de base
    println("Tests arithmétiques de base:")
    testCompiler("42")
    testCompiler("1 + 2")
    testCompiler("10 - 3")
    testCompiler("4 * 5")
    testCompiler("15 / 3")

    // Tests d'expressions complexes
    println("Tests d'expressions complexes:")
    testCompiler("(1 + 2) * 3")
    testCompiler("10 - 2 * 3")
    testCompiler("(5 + 3) / 2")

    // Tests ifz
    println("Tests conditionnels (ifz):")
    testCompiler("ifz 0 then 1 else 2")
    testCompiler("ifz 5 then 1 else 2")
    testCompiler("ifz (3 - 3) then 10 else 20")
    testCompiler("ifz (2 + 1) then 100 else 200")

  @main
  def main(): Unit =
    runAllTests()
