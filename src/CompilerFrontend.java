public abstract class CompilerFrontend {
    boolean debug = false;
    Lexer lex;
    Parser parse;

    public CompilerFrontend() {
        parse = new ParserImpl();
    }

    public CompilerFrontend(boolean debug_) {
        this();
        debug = debug_;
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    protected abstract void init_lexer();

    public TokenList removeWhitespace(TokenList lst) {
        if(lst == null) {
            return null;
        }

        lst.rest = removeWhitespace(lst.rest);
        if(lst.elem.ty == TokenType.WHITE_SPACE) {
            return lst.rest;
        } else {
            return lst;
        }
    }

    public TokenList run_lexer(String input) {
        init_lexer();

        if(debug) {
            System.out.println("Input: " + input);
        }

        return lex.scan(input);
    }

    public Expr run(String input) throws Exception {
        init_lexer();

        if (debug) {
            System.out.println("Input: " + input);
        }

        TokenList tokens = lex.scan(input);

        if (debug) {
            System.out.println("Tokens: " + tokens.toString());
        }

        TokenList tokens_nows = removeWhitespace(tokens);

        if (debug) {
            System.out.println("Tokens without whitespace: " + tokens_nows.toString());
        }

        Expr e = parse.parse(tokens_nows);

        return e;
    }
}
