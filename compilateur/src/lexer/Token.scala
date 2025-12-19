package lexer

enum Token:
  case NUMBER(value: Int)
  case LPAR
  case RPAR
  case PLUS
  case MINUS
  case MULTIPLY
  case DIVIDE
  case IFZ
  case EOF 
  case LET
  case IN
  case IDENT(name: String)
  case EQUALS
  case THEN
  case ELSE
  case FUN
  case ARROW
  case FIX
