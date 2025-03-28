/*
 * Interface for a nondetermninistic finite automaton. This
 * interface uses ints to represent states and chars to
 * represent transitions.
 */
public interface Automaton {
/*
* Creation operations
*/

    /*
     * Add a state "s" to the automaton. If the parameter 
     * "is_start" is true, then the state will be a start
     * state. If "is_accept" is true, then the state will
     * be an accepting state.
     */
    void addState(int s, boolean is_start, boolean is_accept);

    /*
     * Add a transition to the automaton from state "s_initial"
     * to state "s_final" with label "label"
     */
    void addTransition(int s_initial, char label, int s_final);

/*
* Execution operations.
*/
    
    /*
     * Reset the automaton to its initial start states.
     */
    void reset();

    /*
     * Apply all transitions with the same label as "input"
     */
    void apply(char input);

    /*
     * Returns true if the automaton is currently in an
     * accepting state, and false otherwise.
     */
    boolean accepts();

    /*
     * Returns true if the automaton still has any
     * transitions it can take for the input label, 
     * and false if no such transitions exist.
     */
    boolean hasTransitions(char label);
}