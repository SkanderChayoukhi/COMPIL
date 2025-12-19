package vm

import generator.Ins
import generator.Ins.*

enum Value:
  case IntVal(n: Int)
  case Closure(code: List[Ins], env: List[Value])
  case RecClosure(code: List[Ins], env: List[Value])

object VM:
  def execute(code: List[Ins]): Value =
    execute(List(), List(), List(), code)

  // Machine à pile PURE sans accumulateur (conforme TP1)
  private def execute(
                       s: List[Value],
                       env_stack: List[(List[Value], List[Ins])],
                       e: List[Value],
                       c: List[Ins]
                     ): Value =
    (s, env_stack, e, c) match
      case (v::_, _, _, Nil) => v

      case (_, _, _, Ldi(n)::c1) =>
        execute(Value.IntVal(n)::s, env_stack, e, c1)

      case (Value.IntVal(v2)::Value.IntVal(v1)::s1, _, _, Add::c1) =>
        execute(Value.IntVal(v1 + v2)::s1, env_stack, e, c1)

      case (Value.IntVal(v2)::Value.IntVal(v1)::s1, _, _, Sub::c1) =>
        execute(Value.IntVal(v1 - v2)::s1, env_stack, e, c1)

      case (Value.IntVal(v2)::Value.IntVal(v1)::s1, _, _, Mul::c1) =>
        execute(Value.IntVal(v1 * v2)::s1, env_stack, e, c1)

      case (Value.IntVal(v2)::Value.IntVal(v1)::s1, _, _, Div::c1) =>
        execute(Value.IntVal(v1 / v2)::s1, env_stack, e, c1)

      case (v::s1, _, _, Push::c1) =>
        execute(v::v::s1, env_stack, e, c1)

      case (Value.IntVal(n)::s1, _, _, Test(thenCode, elseCode)::c1) =>
        if n == 0 then
          execute(s1, env_stack, e, thenCode ++ c1)
        else
          execute(s1, env_stack, e, elseCode ++ c1)

      case (_, _, _, Search(index)::c1) =>
        execute(e(index)::s, env_stack, e, c1)

      case (v::s1, _, _, Extend::c1) =>
        execute(s1, env_stack, v::e, c1)

      case (_, _, _, Pushenv::c1) =>
        execute(s, (e, Nil)::env_stack, e, c1)

      case (v::s1, (savedEnv, _)::envStack1, _, Popenv::c1) =>
        execute(v::s1, envStack1, savedEnv, c1)

      case (_, _, _, Mkclos(idx, code)::c1) =>
        execute(Value.Closure(code, e)::s, env_stack, e, c1)

      case (_, _, _, Mkfixclos(idx, code)::c1) =>
        execute(Value.RecClosure(code, e)::s, env_stack, e, c1)

      case (Value.Closure(code, closure_env)::arg::s1, _, _, Apply::c1) =>
        execute(s1, (e, c1)::env_stack, arg::closure_env, code)

      case (Value.RecClosure(code, closure_env)::arg::s1, _, _, Apply::c1) =>
        val recClos = Value.RecClosure(code, closure_env)
        execute(s1, (e, c1)::env_stack, arg::recClos::closure_env, code)

      case (v::s1, (saved_env, saved_code)::envStack1, _, Return::_) =>
        execute(v::s1, envStack1, saved_env, saved_code)

      case _ =>
        throw new RuntimeException(s"unexpected VM state")
