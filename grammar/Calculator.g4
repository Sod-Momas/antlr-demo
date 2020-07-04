grammar Calculator;

prog:stat+
    ;

stat: expr NEWLINE        #PrintExpr
    | ID '=' expr NEWLINE #Assign
    | NEWLINE             #Blank
    ;

expr: expr op=('*'|'/') expr #MulDiv
    | expr op=('+'|'-') expr #AddSub
    | INT                    #Int
    | ID                     #Id
    | '(' expr ')'           #Parens
    ;

INT : [0-9]+ ;
NEWLINE : '\r'?'\n' ;
ID : [a-zA-Z]+ ;
WS : [ \t]+ -> skip ;

ADD : '+' ;
SUB : '-' ;
MUL : '*' ;
DIV : '/' ;