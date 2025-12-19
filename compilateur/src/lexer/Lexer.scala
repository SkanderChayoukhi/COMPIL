package lexer

import java.io.InputStream
import Token.*

object Lexer:
  private var input: InputStream = _
  private var currentChar: Int = _

  def apply(in: InputStream): Unit =
    input = in
    currentChar = input.read()

  private def nextChar(): Unit = currentChar = input.read()

  def nextToken(): Token =
    while currentChar != -1 && currentChar.toChar.isWhitespace do nextChar()
    if currentChar == -1 then EOF
    else
      currentChar.toChar match
        case '(' => nextChar(); LPAR
        case ')' => nextChar(); RPAR
        case '+' => nextChar(); PLUS
        case '-' =>
          nextChar()
          if currentChar == '>' then  // ← Détecte ->
            nextChar()
            ARROW
          else
            MINUS
        case '*' => nextChar(); MULTIPLY
        case '/' => nextChar(); DIVIDE
        case '=' => nextChar(); EQUALS
        case d if d.isDigit =>
          var value = d - '0'
          nextChar()
          while currentChar != -1 && currentChar.toChar.isDigit do
            value = value * 10 + (currentChar - '0')
            nextChar()
          NUMBER(value)
        case c if c.isLetter =>
          var name = StringBuilder(c.toString)
          nextChar()
          while currentChar != -1 && currentChar.toChar.isLetterOrDigit do
            name.append(currentChar.toChar)
            nextChar()
          name.toString() match
            case "let" => LET
            case "in" => IN
            case "ifz" => IFZ
            case "then" => THEN
            case "else" => ELSE
            case "fun" => FUN
            case "fix" => FIX
            case id => IDENT(id)
        case c => throw new Exception(s"Unexpected character: $c, ascii ${c.toInt}")
