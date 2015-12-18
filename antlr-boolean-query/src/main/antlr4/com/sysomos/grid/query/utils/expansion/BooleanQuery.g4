grammar BooleanQuery ;


LPAREN : '(' ;
RPAREN : ')' ;
AND : 'AND' ;
OR : 'OR' ;
NOT : 'NOT' ;
WS : [ \r\t\u000C\n]+  -> skip ;
QUOTE : '"' (.*?) '"' ;
FIELD : [A-Za-z][A-Za-z0-9_]+ ':' (WORD | QUOTE);
WORD : (~[ \r\t\u000C\n"():])+ ;

andexpr : orexpr (AND orexpr)* ;

orexpr : condition (OR condition)* ;

condition : (NOT term | term) ;

term : FIELD
     | QUOTE
     | WORD
     | LPAREN andexpr RPAREN
     ;
