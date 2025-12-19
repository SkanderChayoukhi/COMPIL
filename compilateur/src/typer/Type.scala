package typer
import unify.{Atom, HTerm, Structure, TVar}   

type Type = HTerm
object INT extends Atom("INT") :
  override def toString = "INT"
class FUNCTION(a: Type, b: Type) extends Structure("function", List(a, b)):
  override def toString = s"($a -> $b)"
    
object Test extends App :
  val a = INT
  val b = INT
  println(s"Is a === b true? ${a === b}")
  val v = TVar()
  println(s"variable v created, v = $v")
  println(s"Is v === a true? ${v === a}")
  println(s"v = $v")
  val v1 = TVar()
  println(s"variable v1 created, v1 = $v1")
  val v2 = TVar()
  println(s"variable v2 created, v2 = $v2")
  println(s"Is v1 === v2 true? ${v1 === v2}")
  println(s"v1 = $v1")
  println(s"v2 = $v2")
  println(s"Is v1 === v2 true? ${v1 === v2}")
  println(s"Is v1 === a true? ${v1 === a}")
  println(s"v1 = $v1")
  println(s"v2 = $v2")
  val v3 = TVar()
  val v4 = TVar()
  val a1 = FUNCTION(v3, TVar())
  val a2 = FUNCTION(TVar(), v4)
  println(s"a1 = $a1")
  println(s"a2 = $a2")
  v3 === v4
  println("v3 === v4")
  println(s"a1 = $a1")
  println(s"a2 = $a2")
  a1 === a2
  println("a1 === a2")
  println(s"a1 = $a1")
  println(s"a2 = $a2")
  v3 === INT
  println(s"a1 = $a1")
  println(s"a2 = $a2")
  // example of X === A where X appears in A
  val v5 = TVar()
  val a5 = FUNCTION(v5, v5)
  val result = v5 === a5
  println(s"$v5 === $a5 is $result" )
