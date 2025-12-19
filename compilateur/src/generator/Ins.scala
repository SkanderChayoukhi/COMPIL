package generator

enum Ins :
  case Ldi(n: Int)
  case Add, Sub, Mul, Div, Push
  case Test(i: List[Ins], j: List[Ins])
  case Search(p: Int)
  case Extend
  case Pushenv
  case Popenv
  case Mkclos(idx: Int, code: List[Ins])  // idx = index unique de la fermeture
  case Mkfixclos(idx: Int, code: List[Ins]) // Pour les fermetures récursives (fixfun)
  case Apply
  case Return
