public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
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
    @Override
    protected void init_lexer() {
        // We'll build individual NFAs for each token, then add them to a new LexerImpl.

        // 1) NUM => [0-9]* '.' [0-9]+
        // States: 0 = start, 1 = in leading digits, 2 = after dot (no digits yet), 
        //         3 = digits after dot (accepting)
        Automaton a_num = new AutomatonImpl();
        a_num.addState(0, true, false);  // start
        a_num.addState(1, false, false);
        a_num.addState(2, false, false);
        a_num.addState(3, false, true);  // accept

        // Transitions for leading digits (zero or more)
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(0, d, 1); // from state0, digit => state1
            a_num.addTransition(1, d, 1); // stay in state1 on digits
        }
        // Dot transitions from both state0 and state1 go to state2
        a_num.addTransition(0, '.', 2);
        a_num.addTransition(1, '.', 2);

        // After the dot, must read one or more digits to accept
        for (char d = '0'; d <= '9'; d++) {
            a_num.addTransition(2, d, 3); // first digit after dot => state3
            a_num.addTransition(3, d, 3); // stay in state3 on additional digits
        }

        // 2) PLUS => \+
        Automaton a_plus = simpleSingleCharAutomaton('+');

        // 3) MINUS => -
        Automaton a_minus = simpleSingleCharAutomaton('-');

        // 4) TIMES => \*
        Automaton a_times = simpleSingleCharAutomaton('*');

        // 5) DIV => /
        Automaton a_div = simpleSingleCharAutomaton('/');

        // 6) LPAREN => \(
        Automaton a_lparen = simpleSingleCharAutomaton('(');

        // 7) RPAREN => \)
        Automaton a_rparen = simpleSingleCharAutomaton(')');

        // 8) WHITE_SPACE => one or more of ' ', '\n', '\r', '\t'
        // (If you literally want zero-or-more, you'd need to adjust the scanner logic to avoid 
        //  infinite empty-token loops. Typical lexers match one-or-more.)
        Automaton a_ws = new AutomatonImpl();
        a_ws.addState(0, true, false);   // start
        a_ws.addState(1, false, true);   // accept
        for (char c : new char[]{' ', '\n', '\r', '\t'}) {
            a_ws.addTransition(0, c, 1);
            a_ws.addTransition(1, c, 1);
        }

        // Now create a new LexerImpl and add the automata.
        LexerImpl lexerImpl = new LexerImpl();
        lexerImpl.add_automaton(TokenType.NUM, a_num);
        lexerImpl.add_automaton(TokenType.PLUS, a_plus);
        lexerImpl.add_automaton(TokenType.MINUS, a_minus);
        lexerImpl.add_automaton(TokenType.TIMES, a_times);
        lexerImpl.add_automaton(TokenType.DIV, a_div);
        lexerImpl.add_automaton(TokenType.LPAREN, a_lparen);
        lexerImpl.add_automaton(TokenType.RPAREN, a_rparen);
        lexerImpl.add_automaton(TokenType.WHITE_SPACE, a_ws);

        // Assign it to the field 'lex', so CompilerFrontend uses it.
        this.lex = lexerImpl;
    }

    /*
     * Helper method for tokens like +, -, *, /, (, ) which match exactly one character.
     */
    private Automaton simpleSingleCharAutomaton(char c) {
        Automaton a = new AutomatonImpl();
        a.addState(0, true, false);
        a.addState(1, false, true);
        a.addTransition(0, c, 1);
        return a;
    }

}
