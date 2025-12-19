grammar PCF;

term : letExp
     | funExp
     | fixExp
     | addSub
     ;

letExp : LET ID EQUALS term IN term ;

funExp : FUN ID ARROW term ;

fixExp : FIX ID term ;

ifzExp : IFZ term THEN term ELSE term ;

addSub : mulDiv ((PLUS | MINUS) mulDiv)* ;

mulDiv : app ((TIMES | DIV) app)* ;

app : primary (primary)* ;

primary : NUMBER
        | ID
        | ifzExp
        | funExp
        | LPAR term RPAR
        | LPAR IFZ term THEN term ELSE term RPAR
        | LPAR (PLUS | MINUS | TIMES | DIV) term term RPAR
        ;

// Tokens
LET    : 'let' ;
IN     : 'in' ;
FIX    : 'fix' ;
FUN    : 'fun' ;
ARROW  : '->' ;
IFZ    : 'ifz' ;
THEN   : 'then' ;
ELSE   : 'else' ;
EQUALS : '=' ;
PLUS   : '+' ;
MINUS  : '-' ;
TIMES  : '*' ;
DIV    : '/' ;
LPAR   : '(' ;
RPAR   : ')' ;

NUMBER : [0-9]+ ;
ID     : [a-zA-Z][a-zA-Z0-9]* ;
WS     : [ \t\r\n]+ -> skip ;
