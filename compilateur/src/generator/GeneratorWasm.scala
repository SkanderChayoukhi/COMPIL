package generator

import generator.Ins.*

object GeneratorWasm:

  type CodeWAT = List[WAT]

  enum WAT:
    case Ins(ins: String)
    case IfElse(code1: CodeWAT, code2: CodeWAT)

  var verbose: Boolean = false

  private def prelude(): String =
    """
       |(memory 1)
       |(global $sp (mut i32) (i32.const 0))
       |(global $ep (mut i32) (i32.const 1000))
       |(global $hp (mut i32) (i32.const 2000))
       |
       |(func $push (param $val i32)
       |  (i32.store (global.get $sp) (local.get $val))
       |  (global.set $sp (i32.add (global.get $sp) (i32.const 4))))
       |
       |(func $pop (result i32)
       |  (global.set $sp (i32.sub (global.get $sp) (i32.const 4)))
       |  (i32.load (global.get $sp)))
       |
       |(func $pushenv
       |  (call $push (global.get $ep)))
       |
       |(func $popenv
       |  (local $val i32)
       |  (local.set $val (call $pop))
       |  (global.set $ep (call $pop))
       |  (call $push (local.get $val)))
       |
       |(func $extend (param $val i32)
       |  (i32.store (global.get $ep) (local.get $val))
       |  (global.set $ep (i32.add (global.get $ep) (i32.const 4))))
       |
       |(func $search (param $index i32) (result i32)
       |  (local $addr i32)
       |  (local.set $addr 
       |    (i32.sub 
       |      (global.get $ep) 
       |      (i32.mul 
       |        (i32.add (local.get $index) (i32.const 1)) 
       |        (i32.const 4))))
       |  (i32.load (local.get $addr)))
       |
       |(func $pair (param $a i32) (param $b i32) (result i32)
       |  (local $addr i32)
       |  (local.set $addr (global.get $hp))
       |  (i32.store (local.get $addr) (local.get $a))
       |  (i32.store (i32.add (local.get $addr) (i32.const 4)) (local.get $b))
       |  (global.set $hp (i32.add (global.get $hp) (i32.const 8)))
       |  (local.get $addr))
       |
       |(func $fst (param $pair i32) (result i32)
       |  (i32.load (local.get $pair)))
       |
       |(func $snd (param $pair i32) (result i32)
       |  (i32.load (i32.add (local.get $pair) (i32.const 4))))
       |
       |(func $apply (param $arg i32) (param $closure i32) (result i32)
       |  (local $idx i32)
       |  (local $env i32)
       |  (local.set $idx (call $fst (local.get $closure)))
       |  (local.set $env (call $snd (local.get $closure)))
       |  (global.set $ep (local.get $env))
       |  (local.get $arg)
       |  (call $extend)
       |  (call_indirect (type 0) (local.get $idx)))""".stripMargin

  /**
   * Génère le code WAT complet avec table des fonctions
   */
  def genWAT(code: List[Ins]): String =
    val bodies = Generator.collectBodies(code)
    val watCode = emit(code)
    val formatted = format(1, watCode)
    
    s"""(module
       |(type (func (result i32)))
       |${prelude()}
       |
       |${emitTable(bodies.size)}
       |
       |(func (export "main") (result i32)
       |${formatted}
       |  return)
       |
       |${emitFunctions(bodies)})""".stripMargin

  /**
   * Génère la table des fonctions (table + elem)
   */
  private def emitTable(size: Int): String =
    if size == 0 then
      ""
    else
      val funcNames = (0 until size).map(i => s"$$closure$i").mkString(" ")
      s"""(table $size funcref)
         |(elem (i32.const 0) $funcNames)""".stripMargin

  /**
   * Génère toutes les fonctions des fermetures
   */
  private def emitFunctions(bodies: List[List[Ins]]): String =
    bodies.zipWithIndex.map { case (body, idx) =>
      val formattedBody = format(1, emit(body))
      s"""(func $$closure$idx (type 0)
         |$formattedBody)""".stripMargin
    }.mkString("\n")

  def emit(code: List[Ins]): CodeWAT =
    code.flatMap(emitIns)

  def emitIns(ins: Ins): CodeWAT = ins match
    case Ldi(n) => List(WAT.Ins(s"i32.const $n"))
    case Add => List(WAT.Ins("i32.add"))
    case Sub => List(WAT.Ins("i32.sub"))
    case Mul => List(WAT.Ins("i32.mul"))
    case Div => List(WAT.Ins("i32.div_s"))
    case Push => List(WAT.Ins("call $push"))
    case Test(c1, c2) => List(WAT.IfElse(emit(c1), emit(c2)))
    case Pushenv => List(WAT.Ins("call $pushenv"))
    case Popenv => List(WAT.Ins("call $popenv"))
    case Extend => List(WAT.Ins("call $extend"))
    case Search(n) => List(WAT.Ins(s"i32.const $n"), WAT.Ins("call $search"))
    case Mkclos(idx, _) =>
      // Créer une fermeture = paire (index, environnement)
      List(
        WAT.Ins(s"i32.const $idx"),
        WAT.Ins("global.get $ep"),
        WAT.Ins("call $pair")
      )
    case Mkfixclos(idx, _) =>
      // Fermeture récursive : même chose mais l'environnement contiendra la closure elle-même
      List(
        WAT.Ins(s"i32.const $idx"),
        WAT.Ins("global.get $ep"),
        WAT.Ins("call $pair")
      )
    case Apply =>
      List(WAT.Ins("call $apply"))
    case Return =>
      List(WAT.Ins("return"))

  private def format(depth: Int, code: CodeWAT): String =
    code.map(formatIns(depth, _)).mkString("\n")

  private def formatIns(depth: Int, ins: WAT): String = ins match
    case WAT.Ins(s) => spaces(depth) + s
    case WAT.IfElse(c1, c2) =>
      s"""${spaces(depth)}(if (result i32)
         |${format(depth + 1, c1)}
         |${spaces(depth)}else
         |${format(depth + 1, c2)}
         |${spaces(depth)})""".stripMargin

  private def spaces(depth: Int): String = "  " * depth


