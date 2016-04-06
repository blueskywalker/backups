grammar BooleanQuery ;


LPAREN : '(' ;
RPAREN : ')' ;
AND : 'AND' ;
OR : 'OR' ;
NOT : 'NOT' ;
NEAR : '~' ;
NUMBER : [0-9]+ ;
WS : [ \r\t\u000C\n]+  -> skip ;
QUOTE : '"' (.*?) '"' ;
FIELD : [A-Za-z][A-Za-z0-9_]+ ':' (WORD | QUOTE);
WORD : (~[ \r\t\u000C\n"():~])+ ;


andexpr : orexpr (AND orexpr)* ;

orexpr : condition (OR condition)* ;

condition : NOT ops  | ops  ;

ops: term | LPAREN andexpr RPAREN | term NEAR NUMBER;

term : FIELD | WORD| QUOTE ;


