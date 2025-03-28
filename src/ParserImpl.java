
public class ParserImpl extends Parser {

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
    @Override
    public Expr do_parse() throws Exception {
        // Parse the start nonterminal: T
        Expr result = parseT();

        // If there are extra tokens remaining, consider it an error (optional, 
        // depending on whether you want to allow trailing whitespace or not).
        // Typically, we only throw an error if the leftover token isn't whitespace
        // and isn't null. For simplicity, let's just throw if anything remains:
        if (tokens != null) {
            // check if the leftover token is whitespace; 
            // if you want to allow trailing whitespace, you can skip them
            // before throwing an error. For now, let's throw on any leftover token.
            throw new Exception("Parsing error: extra tokens at the end.");
        }

        return result;
    }

    /**
     * parseT implements:
     *   T -> F (AddOp T)* 
     * In normal left-associative style, we parse a first F,
     * then while next token is PLUS or MINUS, parse (AddOp + F).
     */
    private Expr parseT() throws Exception {
        // First parse F
        Expr expr = parseF();

        // While the next token is PLUS or MINUS, parse more
        while (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = consumeAddOp();     // either PLUS or MINUS
            Expr right = parseF();
            if (op.ty == TokenType.PLUS) {
                expr = new PlusExpr(expr, right);
            } else {
                // must be MINUS
                expr = new MinusExpr(expr, right);
            }
        }

        return expr;
    }

    /**
     * parseF implements:
     *   F -> Lit (MulOp F)*
     * in a left-associative manner. We parse one Lit, then while
     * the next token is TIMES or DIV, we parse (MulOp + Lit).
     */
    private Expr parseF() throws Exception {
        // parse the first Lit
        Expr expr = parseLit();

        // while next token is TIMES or DIV, consume and parse next Lit
        while (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = consumeMulOp();     // either TIMES or DIV
            Expr right = parseLit();
            if (op.ty == TokenType.TIMES) {
                expr = new TimesExpr(expr, right);
            } else {
                // must be DIV
                expr = new DivExpr(expr, right);
            }
        }

        return expr;
    }

    /**
     * parseLit implements:
     *   Lit -> NUM
     *        | LPAREN T RPAREN
     */
    private Expr parseLit() throws Exception {
        // Check the next token type
        if (peek(TokenType.NUM, 0)) {
            // Lit -> NUM
            Token numTok = consume(TokenType.NUM);
            float val = Float.parseFloat(numTok.lexeme);
            return new FloatExpr(val);
        } else if (peek(TokenType.LPAREN, 0)) {
            // Lit -> LPAREN T RPAREN
            consume(TokenType.LPAREN);
            Expr inside = parseT();
            consume(TokenType.RPAREN);
            return inside;
        } else {
            throw new Exception("Parsing error: expected NUM or LPAREN, got: " 
                                + (tokens == null ? "null" : tokens.elem.ty));
        }
    }

    /**
     * Helper to consume an AddOp => PLUS or MINUS
     */
    private Token consumeAddOp() throws Exception {
        if (peek(TokenType.PLUS, 0)) {
            return consume(TokenType.PLUS);
        } else if (peek(TokenType.MINUS, 0)) {
            return consume(TokenType.MINUS);
        }
        throw new Exception("Parsing error: expected '+' or '-'");
    }

    /**
     * Helper to consume a MulOp => TIMES or DIV
     */
    private Token consumeMulOp() throws Exception {
        if (peek(TokenType.TIMES, 0)) {
            return consume(TokenType.TIMES);
        } else if (peek(TokenType.DIV, 0)) {
            return consume(TokenType.DIV);
        }
        throw new Exception("Parsing error: expected '*' or '/'");
    }

}
