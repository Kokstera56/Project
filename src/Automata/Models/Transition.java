package Automata.Models;

/**
 * Represents a transition between two states in an automaton.
 */
public class Transition {

    private String fromState;
    private String symbol;
    private String toState;

    /**
     * Creates a transition.
     *
     * @param fromState start state
     * @param symbol    transition symbol
     * @param toState   destination state
     */
    public Transition(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    public String getFromState() {
        return fromState;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getToState() {
        return toState;
    }

    /**
     * Prints transition information in the console.
     */
    public void printTransition() {
        System.out.println(fromState + " --" + symbol + "--> " + toState);
    }
}