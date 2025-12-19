package test

object CompileTestGreen extends App with Test with TestGreen {
  val verbose = true
  test()
}
object CompileTestBlue extends App with Test with TestBlue {
  val verbose = true
  test()
}
object CompileTestRed extends App with Test with TestRed {
  val verbose = true
  test()
}

object CompileTestBlack extends App with Test with TestBlack {
  val verbose = true
  test()
}