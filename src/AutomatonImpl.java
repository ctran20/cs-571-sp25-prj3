import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;
        public StateLabelPair(int state_, char label_) { state = state_; label = label_; }

        @Override
        public int hashCode() {
            return Objects.hash((Integer) state, (Character) label);
        }

        @Override
        public boolean equals(Object o) {
            StateLabelPair o1 = (StateLabelPair) o;
            return (state == o1.state) && (label == o1.label);
        }
    }

    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states;
    HashMap<StateLabelPair, HashSet<Integer>> transitions;

    public AutomatonImpl() {
        start_states = new HashSet<Integer>();
        accept_states = new HashSet<Integer>();
        current_states = new HashSet<Integer>();
        transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
    }

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        if (is_start) {
            start_states.add(s);
        }
        if (is_accept) {
            accept_states.add(s);
        }
        // No separate tracking of all states is strictly necessary,
        // unless you need it for other uses. 
        // The interface only requires start & accept sets plus transitions.
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair pair = new StateLabelPair(s_initial, label);
        // If no transitions have been added for this (state, label) pair yet, create a new set:
        if (!transitions.containsKey(pair)) {
            transitions.put(pair, new HashSet<>());
        }
        transitions.get(pair).add(s_final);
    }

    @Override
    public void reset() {
        // Resets current_states to all the start states
        current_states.clear();
        current_states.addAll(start_states);
    }

    @Override
    public void apply(char input) {
        // Compute the new set of states reachable by the current input (label)
        HashSet<Integer> nextStates = new HashSet<>();
        for (int state : current_states) {
            StateLabelPair pair = new StateLabelPair(state, input);
            // If transitions exist from this (state, input) pair, add them all
            if (transitions.containsKey(pair)) {
                nextStates.addAll(transitions.get(pair));
            }
        }
        current_states = nextStates;
    }

    @Override
    public boolean accepts() {
        // Returns true if the current set of states intersects with accept_states
        for (int s : current_states) {
            if (accept_states.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
        // Check if there exists any state among current_states that can move on 'label'
        for (int state : current_states) {
            StateLabelPair pair = new StateLabelPair(state, label);
            if (transitions.containsKey(pair) && !transitions.get(pair).isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
