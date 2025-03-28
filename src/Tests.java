public class Tests {
    public static void applyAll(String s, Automaton a) {
        for(int i = 0; i < s.length(); i++) {
            a.apply(s.charAt(i));
        }
    }
    public static void test_automata() {
        Automaton a1 = new AutomatonImpl();
        a1.addState(0,true,false);
        a1.addState(1, false, true);
        a1.addTransition(0, 'a', 1);

        a1.reset();
        System.out.println("a1 accepts empty string (should be false): " + a1.accepts());
        System.out.println("a1 has transitions on 'a' (should be true): " + a1.hasTransitions('a'));
        a1.apply('a');
        System.out.println("a1 accepts 'a' (should be true): " + a1.accepts());
        System.out.println("a1 has transitions on 'b' (should be false): " + a1.hasTransitions('b'));
        a1.apply('b');
        System.out.println("a1 accepts 'ab' (should be false): " + a1.accepts());

        Automaton a2 = new AutomatonImpl();
        a2.addState(0, true, false);

        a2.addState(1, false, true);
        a2.addTransition(0, 'a', 1);

        a2.addState(2, false, false);
        a2.addTransition(0, 'a', 2);

        a2.addState(3, false, false);
        a2.addTransition(2, 'b', 3);
        
        a2.addState(4, false, true);
        a2.addTransition(3, 'c', 4);

        a2.reset();
        System.out.println("a2 accepts empty string (should be false): " + a2.accepts());
        a2.apply('a');
        System.out.println("a2 accepts 'a' (should be true): " + a2.accepts());
        a2.apply('b');
        System.out.println("a2 accepts 'ab' (should be false): " + a2.accepts());
        System.out.println("a2 has transition to 'c' (should be true): " + a2.hasTransitions('c'));
        a2.apply('c');
        System.out.println("a2 accepts 'abc' (should be true): " + a2.accepts());
        System.out.println("a2 has transition to 'z' (should be false): " + a2.hasTransitions('z'));
        System.out.println("a2 has transition to 'a' (should be false): " + a2.hasTransitions('a'));


        Automaton a_num = new AutomatonImpl();
        a_num.addState(0, true, false);
        a_num.addState(1, false, true);

        for(char i = '0'; i <= '9'; i++) {
            a_num.addTransition(0, i, 1);
        }
        for(char i = '0'; i <= '9'; i++) {
            a_num.addTransition(1, i, 1);
        }

        String num1 = "3";
        String num2 = "124";
        String num3 = "90983724847619547905718498504";
        String num4 = "124.3f";
        String num5 = "1243f";

        a_num.reset();
        applyAll(num5, a_num);
        System.out.println("a_num accepts \"1243f\" (should be false): " + a_num.accepts());

        a_num.reset();
        applyAll(num1, a_num);
        System.out.println("a_num accepts \"3\" (should be true): " + a_num.accepts());

        a_num.reset();
        applyAll(num2, a_num);
        System.out.println("a_num accepts \"124\" (should be true): " + a_num.accepts());

        a_num.reset();
        applyAll(num3, a_num);
        System.out.println("a_num accepts \"90983724847619547905718498504\" (should be true): " + a_num.accepts());

        a_num.reset();
        applyAll(num4, a_num);
        System.out.println("a_num accepts \"124.3f\" (should be false): " + a_num.accepts());
    }

    public static void test_lexer1() {
        // A test lexer that recognizes integers:
        Automaton a_num = new AutomatonImpl();
        a_num.addState(0, true, false);
        a_num.addState(1, false, true);

        for(char i = '0'; i <= '9'; i++) {
            a_num.addTransition(0, i, 1);
        }
        for(char i = '0'; i <= '9'; i++) {
            a_num.addTransition(1, i, 1);
        }

        // This lexer does not recognize arithmetic
        Automaton a_plus = new AutomatonImpl();
        a_plus.addState(0, true, false);
        a_plus.addState(1, false, true);

        Automaton a_minus = new AutomatonImpl();
        a_minus.addState(0, true, false);
        a_minus.addState(1, false, true);

        Automaton a_times = new AutomatonImpl();
        a_times.addState(0, true, false);
        a_times.addState(1, false, true);

        Automaton a_div = new AutomatonImpl();
        a_div.addState(0, true, false);
        a_div.addState(1, false, true);

        Automaton a_lparen = new AutomatonImpl();
        a_lparen.addState(0, true, false);
        a_lparen.addState(1, false, true);

        Automaton a_rparen = new AutomatonImpl();
        a_rparen.addState(0, true, false);
        a_rparen.addState(1, false, true);

        //Only spaces count as whitespace
        Automaton a_ws = new AutomatonImpl();
        a_ws.addState(0, true, false);
        a_ws.addState(1, false, true);
        a_ws.addTransition(0, ' ', 1);
        a_ws.addTransition(1, ' ', 1);


        Lexer test_lexer = new LexerImpl();
        test_lexer.add_automaton(TokenType.NUM, a_num);
        test_lexer.add_automaton(TokenType.PLUS, a_plus);
        test_lexer.add_automaton(TokenType.MINUS, a_minus);
        test_lexer.add_automaton(TokenType.DIV, a_div);
        test_lexer.add_automaton(TokenType.TIMES, a_times);
        test_lexer.add_automaton(TokenType.WHITE_SPACE, a_ws);
        test_lexer.add_automaton(TokenType.LPAREN, a_lparen);
        test_lexer.add_automaton(TokenType.RPAREN, a_rparen);

        TokenList tokens1 = test_lexer.scan("1 2  3 4414");
        System.out.println("Lexing \"1 2  3 4414\" with simple lexer.");
        System.out.println("result: " + tokens1.toString());

    }

    static TokenList test_lexer(String input) {
        CompilerFrontend front = new CompilerFrontendImpl(true);
        return front.run_lexer(input);
    }

    public static void test_lexer2() {
        System.out.println(test_lexer("100.0 + -.02 032.1* (   0.2 / 3.0) /\n\t 4.05"));
        System.out.println(test_lexer("1.0 + 2.0 + 3.0 + 4.0"));
        System.out.println(test_lexer("1.0 + 2.0 - 3.0 + 4.0"));
    }

    static Expr test_compiler(String input) throws Exception {
        CompilerFrontend front = new CompilerFrontendImpl(true);
        return front.run(input);
    }

    static void test_recognizer() {
        try {
            test_compiler("(1.0 - 2.0 + 3.0");
        } catch (Exception e) {
            System.out.println("Parsing Failed");
        }

        try {
            test_compiler("(1.0 + 2.0) - 3.0");
        } catch (Exception e) {
            System.out.println("Parsing Failed");
        }

        try {
            test_compiler("1.0 + 2.0 + 4.0 + 3.0");
        } catch (Exception e) {
            System.out.println("Parsing Failed");
        }

        try {
            test_compiler("1.0 - 2.0 + 3.0 + 4.0");
        } catch (Exception e) {
            System.out.println("Parsing Failed");
        }

        try {
            test_compiler("100.0 + .02 -032.1* (   0.2 / 3.5) /\n\t 4.05");
        } catch (Exception e) {
            System.out.println("Parsing Failed");
        }
    }

    static void test_parser() throws Exception {
        System.out.println(test_compiler("100.0 + .02 -032.1* (   0.2 / 3.5) /\n\t 4.05"));
        System.out.println(test_compiler("1.0 + 2.0 + 4.0 + 3.0"));
        System.out.println(test_compiler("1.0 - 2.0 + 3.0 + 4.0"));
    }
}
