package typer

import ast.Op.{Div, Minus, Plus, Times}
import ast.Term
import ast.Term.*
import typer.FUNCTION
import unify.TVar
import unify.Structure

object Typer :
  type Env = Map[String, Type]
  def eval(term: Term, e: Env): Type = term match
    case Number(_) => INT
    case IfZero(cond, zBranch, nzBranch) =>
      if eval(cond, e) === INT then
        if (eval(zBranch, e) === eval(nzBranch, e)) then
          eval(zBranch, e)
        else throw TyperException("z branch and nz branch has not the same type")
      else throw TyperException("condition is not an int")
    case Let(n, u1, u2) =>
      val v1 = eval(u1,e)
      val e1 = e + (n -> v1)
      eval(u2,e1)
    case BinaryTerm(op, term1, term2) =>
      val v1 = eval(term1, e)
      val v2 = eval(term2, e)
      if v1 === INT && v2 === INT then
        INT  // ✓ Changez v1 en INT
      else
        throw TyperException("V1 or v2 are not an int")

    case Fix(x, t) =>
      val xType= TVar()
      val tType = eval(t, e + (x->xType))
      if xType === tType then
        xType
      else
        throw TyperException("Type of x and type of body does not match")

    case Var(name) =>
      e.get(name) match
        case Some(t: Type) => t
        case _ => throw TyperException("Variable not found in environment")

    case Fun(name,u1) =>
      val nameType = TVar()
      val e1 = e + (name-> nameType)
      val u1Type= eval(u1, e1)
      FUNCTION(nameType, u1Type)

    case App(t, u) =>
      val uType = eval(u, e)
      eval(t, e) match
        case s:Structure
          if s.functor == "function" =>
          val nameType = s.types(0)
          val u1Type = s.types(1)
          if nameType === uType then u1Type
          else throw TyperException("name Type doesn't match uType")
        case _ =>  throw TyperException("name is not a structor")
        
    case _ => throw TyperException("no Term found")

