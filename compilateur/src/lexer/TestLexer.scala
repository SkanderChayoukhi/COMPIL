package lexer

import java.io.{FileInputStream, InputStream}

@main
def main(args: String*): Unit =
  val in: InputStream =
    if args.isEmpty then
      System.in
    else
      FileInputStream(args(0))
  Lexer(in)  // Utilise l'objet Lexer au lieu d'instancier une nouvelle classe
  var token = Lexer.nextToken()
  while token != Token.EOF do
    println(token)
    token = Lexer.nextToken()
