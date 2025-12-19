package test
import PCF.PCF.main
import java.nio.file.Paths

@main
def runSuite(): Unit =
  // PCF Vert (Green)
  runFile("green0")
  runFile("green1")
  runFile("green2")
  runFile("green3")
  runFile("green4")
  runFile("green5")
  runFile("green6")
  runFile("green7")
  runFile("green8")
  runFile("green9")

  // PCF Bleu (Blue)
  runFile("blue0")
  runFile("blue1")
  runFile("blue2")
  runFile("blue3")
  runFile("blue4")
  runFile("blue5")
  runFile("blue6")
  runFile("blue7")
  runFile("blue8")
  runFile("blue9")
  runFile("blue10")

  //  // PCF Rouge (Red)
  runFile("red0")
  runFile("red1")
  runFile("red2")
  runFile("red3")
  runFile("red4")
  runFile("red5")
  runFile("red6")
  runFile("red7")
  runFile("red11")
  runFile("red13")
  runFile("red14")
  runFile("red15")
  runFile("red16")
  runFile("red17")
  runFile("red18")
  runFile("red19")
  runFile("red20")
  runFile("red40")
  runFile("red41")
  runFile("red42")
  runFile("red43")

  // PCF Noir (Black)
  runFile("black0")
  runFile("black1")
  runFile("black2")
  runFile("black3")
  runFile("black4")  // fix x x - infinite loop
  runFile("black5")  // loop - infinite loop

private def runFile(file: String): Unit =
  val path = Paths.get("compilateur", "src", "test", s"$file.pcf").toString
  val args = Array(path)
  println(s"********* $file *********")
  try main(args)
  catch case e: Exception => println(e)
