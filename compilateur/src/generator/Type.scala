package generator

sealed trait GType
case object TInt extends GType
case class TFun(arg: GType, res: GType) extends GType
case class TVar(id: Int, var instance: Option[GType] = None) extends GType

case class TypeError(msg: String) extends Exception(msg)

// Schéma pour la généralisation (let-polymorphisme)
case class Scheme(vars: List[Int], t: GType)
