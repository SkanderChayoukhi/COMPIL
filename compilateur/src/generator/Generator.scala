// scala
package generator

import generator.ATerm.*
import generator.Ins.*

object Generator:

  def gen(t: ATerm): List[Ins] =
    // vérifie les types avant génération : lève TypeError si problème
    try
      TypeChecker.check(t)
      val (code, _) = genAM(t, 0)
      code
    catch
      case e: TypeError => throw new Exception(s"Type error: ${e.getMessage}")

  /**
   * Génération du code abstrait avec index unique pour chaque fermeture
   * @param t Le terme annoté
   * @param nextIdx Le prochain index disponible pour une fermeture
   * @return (code généré, prochain index après génération)
   */
  def genAM(t: ATerm, nextIdx: Int): (List[Ins], Int) = t match
    case AInt(n) =>
      (List(Ldi(n)), nextIdx)

    case AVar(name, index) =>
      (List(Search(index)), nextIdx)

    case AAdd(left, right) =>
      val (codeL, idx1) = genAM(left, nextIdx)
      val (codeR, idx2) = genAM(right, idx1)
      (codeL ++ List(Push) ++ codeR ++ List(Add), idx2)

    case ASub(left, right) =>
      val (codeL, idx1) = genAM(left, nextIdx)
      val (codeR, idx2) = genAM(right, idx1)
      (codeL ++ List(Push) ++ codeR ++ List(Sub), idx2)

    case AMul(left, right) =>
      val (codeL, idx1) = genAM(left, nextIdx)
      val (codeR, idx2) = genAM(right, idx1)
      (codeL ++ List(Push) ++ codeR ++ List(Mul), idx2)

    case ADiv(left, right) =>
      val (codeL, idx1) = genAM(left, nextIdx)
      val (codeR, idx2) = genAM(right, idx1)
      (codeL ++ List(Push) ++ codeR ++ List(Div), idx2)

    case AIf(cond, thenBranch, elseBranch) =>
      val (codeCond, idx1) = genAM(cond, nextIdx)
      val (codeThen, idx2) = genAM(thenBranch, idx1)
      val (codeElse, idx3) = genAM(elseBranch, idx2)
      (codeCond ++ List(Test(codeThen, codeElse)), idx3)

    case ALet(name, value, body) =>
      val (codeVal, idx1) = genAM(value, nextIdx)
      val (codeBody, idx2) = genAM(body, idx1)
      (List(Pushenv) ++ codeVal ++ List(Extend) ++ codeBody ++ List(Popenv), idx2)

    case AFun(param, body) =>
      val (codeBody, idx1) = genAM(body, nextIdx + 1)
      val closureCode = codeBody ++ List(Return)
      (List(Mkclos(nextIdx, closureCode)), idx1)

    case AApp(func, arg) =>
      val (codeArg, idx1) = genAM(arg, nextIdx)
      val (codeFunc, idx2) = genAM(func, idx1)
      (List(Pushenv) ++ codeArg ++ List(Push) ++ codeFunc ++ List(Apply, Popenv), idx2)

    case AFix(name, body) =>
      // Fix simple (non utilisé normalement car transformé en FixFun)
      genAM(body, nextIdx)

    case AFixFun(name, param, body) =>
      // fixfun f x -> body
      // Génère une fermeture récursive (closure qui se référence elle-même)
      val (codeBody, idx1) = genAM(body, nextIdx + 1)
      val closureCode = codeBody ++ List(Return)
      (List(Mkfixclos(nextIdx, closureCode)), idx1)

  /**
   * Ancienne méthode pour compatibilité
   */
  def genA(t: ATerm): List[Ins] =
    val (code, _) = genAM(t, 0)
    code

  /**
   * Collecte tous les corps de fermetures (closures) du code
   * @param code Le code abstrait
   * @return Liste des corps de fermetures dans l'ordre de leurs index
   */
  def collectBodies(code: List[Ins]): List[List[Ins]] =
    var bodies = scala.collection.mutable.Map[Int, List[Ins]]()
    
    def collect(ins: Ins): Unit = ins match
      case Mkclos(idx, body) =>
        bodies(idx) = body
        body.foreach(collect)
      case Mkfixclos(idx, body) =>
        bodies(idx) = body
        body.foreach(collect)
      case Test(c1, c2) =>
        c1.foreach(collect)
        c2.foreach(collect)
      case _ => ()
    
    code.foreach(collect)
    
    // Retourne les corps dans l'ordre des index (0, 1, 2, ...)
    val maxIdx = if bodies.isEmpty then -1 else bodies.keys.max
    (0 to maxIdx).map(i => bodies.getOrElse(i, List())).toList

