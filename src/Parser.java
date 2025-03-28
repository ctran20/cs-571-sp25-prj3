
public abstract class Parser {
    TokenList tokens;
    int idx;

    public Expr parse(TokenList token_list) throws Exception {
        tokens = token_list;
        idx = 0;
        return do_parse();
    }

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    public abstract Expr do_parse() throws Exception;

    boolean peek(TokenType t, int k) {
        TokenList cur = tokens;
        while(k > 0) {
            k--;
            if (cur == null) {
                return false;
            }
            cur = cur.rest;
        }
        if (cur == null) {
            return false;
        }
        return cur.elem.ty == t;
    }

    Token consume(TokenType t) throws Exception {
        if (t != tokens.elem.ty) {
            throw new Exception("Parsing error on token: " + tokens.elem.lexeme);
        }
        Token ret = tokens.elem;
        tokens = tokens.rest;
        return ret;
    }

    public Parser() {}
}
