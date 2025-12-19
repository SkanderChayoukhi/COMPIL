package test

trait TestBlack {
  val verbose : Boolean
  def report(): Unit
  def test(verbose: Boolean, filename: String, message: String, expectation: Option[Int]): Unit
  def test(): Unit = {
    test(verbose, "test/black0.pcf", "count 0", Option(0))
    test(verbose, "test/black1.pcf", "count 2", Option(0))
    test(verbose, "test/black2.pcf", "multiply 3 4", Option(12))
    test(verbose, "test/black3.pcf", "factorial 3", Option(6))
    test(verbose, "test/black4.pcf", "loop", None)
    report()
  }
}
