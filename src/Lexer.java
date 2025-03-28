enum TokenType {
    NUM,
    PLUS,
    MINUS,
    TIMES,
    DIV,
    WHITE_SPACE,
    LPAREN,
    RPAREN
}

class Token{
    TokenType ty;
    String lexeme;

    public Token(TokenType ty_, String lexeme_) {
        ty = ty_;
        lexeme = lexeme_;
    }

    public String toString() {
        return ty.toString() + "(\"" + lexeme + "\")";
    }
}

class TokenList {
    Token elem;
    TokenList rest;

    public TokenList(Token elem_, TokenList rest_) {
        elem = elem_;
        rest = rest_;
    }

    String toString_helper() {
        if(rest == null) {
            return elem.toString();
        } else{
            return elem.toString() + "; " + rest.toString_helper();
        }
    }

    public String toString() {
        return "[" + toString_helper() + "]";
    }

    public int length() {
        if(rest == null) {
            return 1;
        } else {
            return 1 + rest.length();
        }
    }
}

public interface Lexer {
    /*
     * Add an automaton to match tokens of type "ty".
     */
    public void add_automaton(TokenType ty, Automaton a);

    /*
     * Turn the input string into a sequence of tokens.
     */
    public TokenList scan(String input);
}