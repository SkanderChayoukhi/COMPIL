package test
import PCF.PCF
import java.io.FileInputStream
import generator.GeneratorWasm

object TestInterp extends App:

  // Active la génération WAT (mettre à true pour tester WAT)
  val testWAT = true

  // Active l'affichage du code WAT généré
  GeneratorWasm.verbose = true

  println("=== Tests du compilateur ===\n")

  // ...existing code...

  test("green0")
  test("green1")
  test("green2")
  test("green3")
  test("green4")
  test("green5")
  test("green6")
  test("green7")
  test("green8")
  test("green9")

  test("blue0")
  test("blue1")
  test("blue2")
  test("blue3")
  test("blue4")
  test("blue5")
  test("blue6")
  test("blue7")
  test("blue8")
  test("blue9")
  test("blue10")

  test("red0")
  test("red1")
  test("red2")
  test("red3")
  test("red4")
  test("red5")
  test("red6")
  test("red7")
  test("red11")
  test("red13")
  test("red14")
  test("red15")
  test("red16")
  test("red17")
  test("red18")
  test("red19")
  test("red40")
  test("red41")
  test("red42")
  test("red43")

  test("black0")
  test("black1")
  test("black2")
  test("black3")
  // test("black4") // loops
  // test("black5") // loops

  private def test(filename: String): Unit =
    val dir = "compilateur/test/"
    val filepath = s"$dir$filename.pcf"

    try {
      val inputStream = new FileInputStream(filepath)
      val code = PCF.compile(inputStream)
      inputStream.close()

      val vmResult = vm.VM.execute(code)

      println(s"Fichier: $filename.pcf")
      println(s"Code généré: $code")
      println(s"Résultat VM: $vmResult")

      // Génération WAT optionnelle
      if TestInterp.testWAT then
        try {
          val wat = GeneratorWasm.genWAT(code)
          println(s"WAT généré: OK (${wat.length} chars)")
          if GeneratorWasm.verbose then
            println(wat)
        } catch {
          case e: Exception =>
            println(s"WAT: ERREUR - ${e.getMessage}")
        }

      println("Test exécuté ✓")
      println("---")
    } catch {
      case e: Exception =>
        println(s"ERREUR pour '$filename.pcf': ${e.getMessage}")
        println("---")
    }

