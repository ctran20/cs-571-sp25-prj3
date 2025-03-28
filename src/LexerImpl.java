import java.util.EnumMap;

public class LexerImpl implements Lexer {
    EnumMap<TokenType, Automaton> automata;
    EnumMap<TokenType, Boolean> prev_accepts;

    public LexerImpl() {
        automata = new EnumMap<>(TokenType.class);
    }

    @Override
    public void add_automaton(TokenType ty, Automaton a) {
        automata.put(ty, a);
    }

    @Override
    public TokenList scan(String input) {

        TokenList ret = null;
        TokenList ret_tail = null;

        int start_pos = 0;
        int current_pos = 0;

        
        while (start_pos < input.length()) {
            for(Automaton a : automata.values()) {
                a.reset();
            }
            boolean has_prev_accept = false;
            boolean has_transitions = true;

            EnumMap<TokenType, Boolean> current_accepts;

            while(has_transitions) {
                has_transitions = false;

                if(current_pos < input.length()) {
                    for(Automaton a : automata.values()) {
                        char c = input.charAt(current_pos);
                        if(a.hasTransitions(c)) {
                            has_transitions = true;
                        }
                        a.apply(c);
                    }
                }

                current_accepts = new EnumMap<>(TokenType.class);
                boolean has_current_accept = false;
                for (TokenType ty : automata.keySet()) {
                    if(automata.get(ty).accepts() && current_pos < input.length()) {
                        current_accepts.put(ty, (Boolean) true);
                        has_current_accept = true;
                    } else {
                        current_accepts.put(ty, (Boolean) false);
                    }
                }

                current_pos ++;

                if(!has_prev_accept && has_current_accept) {
                    prev_accepts = current_accepts;
                    has_prev_accept = true;
                }
            }

            assert(current_pos > start_pos);
            assert(has_prev_accept);

            TokenType new_ty = TokenType.WHITE_SPACE;
            boolean done2 = false;

            for(TokenType ty : prev_accepts.keySet()) {
                if(prev_accepts.get(ty)) {
                    new_ty = ty;
                    done2 = true;
                    break;
                }
            }
            assert(done2);

            current_pos--;

            if(ret_tail == null) {
                ret_tail = new TokenList(new Token(new_ty, input.substring(start_pos, current_pos)), null);
                ret = ret_tail;
            } else {
                ret_tail.rest = new TokenList(new Token(new_ty, input.substring(start_pos, current_pos)), null);
                ret_tail = ret_tail.rest;
            }

            start_pos = current_pos;
        }
        return ret;
    }
    
}
