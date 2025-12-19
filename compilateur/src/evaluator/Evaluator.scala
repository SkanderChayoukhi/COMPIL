package evaluator

import ast.Term
import Term.*
import ast.Op.*

object Evaluator:

  type Env = Map[String, Value | IceCube]

  case class IceCube(x: String, t: Term, e: Env)

  // Type de retour : peut être un entier ou une fermeture
  enum Value:
    case IntValue(n: Int)
    case Closure(param: String, body: Term, env: Env)

  import Value.*

  def eval(term: Term, env: Env = Map()): Value =
    term match
      case Number(value) =>
        IntValue(value)

      case Var(name) =>
        env.getOrElse(name, throw Exception(s"Undefined variable: $name")) match
          case v: Value => v
          case ice: IceCube =>
            eval(Fix(ice.x, ice.t), ice.e)

      case BinaryTerm(op, exp1, exp2) =>
        val v1 = eval(exp1, env)
        val v2 = eval(exp2, env)
        (v1, v2) match
          case (IntValue(n1), IntValue(n2)) =>
            val result = op match
              case Plus => n1 + n2
              case Minus => n1 - n2
              case Times => n1 * n2
              case Div if n2 != 0 => n1 / n2
              case Div => throw new Exception("Division by zero")
            IntValue(result)
          case _ =>
            throw new Exception(s"Cannot apply $op to non-integers")

      case IfZero(cond, zBranch, nzBranch) =>
        eval(cond, env) match
          case IntValue(0) => eval(zBranch, env)
          case IntValue(_) => eval(nzBranch, env)
          case _ => throw new Exception("ifz condition must evaluate to an integer")

      case Let(name, value, body) =>
        val v = eval(value, env)
        eval(body, env + (name -> v))

      case Fun(param, body) =>
        Closure(param, body, env)  // ← Retourne une fermeture

      case App(func, arg) =>
        val argValue = eval(arg, env)
        eval(func, env) match
          case Closure(param, body, closureEnv) =>
            eval(body, closureEnv + (param -> argValue))
          case _ =>
            throw new Exception("Cannot apply non-function")

      case Fix(x, t) =>
        val ice = IceCube(x, t, env) // ← Crée le glaçon
        eval(t, env + (x -> ice)) // ← Évalue le corps avec le glaçon
