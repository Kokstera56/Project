package Automata.Models;

import java.util.ArrayList;

/**
 * Represents a finite automaton with states, final states and transitions.
 */
public class Automaton {

    private String id;
    private ArrayList<String> states;
    private ArrayList<String> finalStates;
    private ArrayList<Transition> transitions;

    /**
     * Creates automaton with given id.
     *
     * @param id automaton identifier
     */
    public Automaton(String id) {

        this.id = id;

        states = new ArrayList<>();
        finalStates = new ArrayList<>();
        transitions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public ArrayList<String> getFinalStates() {
        return finalStates;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Adds state to automaton.
     *
     * @param state state name
     */
    public void addState(String state) {
        states.add(state);
    }


    /**
     * Adds final state to automaton.
     *
     * @param state final state name
     */
    public void addFinalState(String state) {
        finalStates.add(state);
    }


    /**
     * Prints full information about the automaton.
     */
    public void printInfo() {

        System.out.println("========== AUTOMATON ==========");
        System.out.println("ID: " + id);
        System.out.println();

        System.out.println("States:");

        for (String state : states) {
            System.out.println(" - " + state);
        }

        System.out.println();

        System.out.println("Final states:");

        for (String state : finalStates) {
            System.out.println(" - " + state);
        }

        System.out.println();

        System.out.println("Transitions:");

        for (Transition transition : transitions) {
            transition.printTransition();
        }

        System.out.println("===============================");
    }

    /**
     * Checks if automaton language is empty.
     *
     * @return true if there are no final states
     */
    public boolean isEmpty() {

        return finalStates.isEmpty();
    }

    /**
     * Adds transition between two states.
     *
     * @param fromState start state
     * @param symbol    transition symbol
     * @param toState   destination state
     */
    public void addTransition(String fromState, String symbol, String toState) {

        Transition transition =
                new Transition(fromState, symbol, toState);

        transitions.add(transition);
    }

    /**
     * Checks if automaton is deterministic.
     *
     * @return true if automaton is deterministic
     */
    public boolean isDeterministic() {
        for (Transition transition : transitions) {
            if (transition.getSymbol().equals("eps")) {
                return false;
            }
        }
        for (int i = 0; i < transitions.size(); i++) {

            Transition first = transitions.get(i);

            for (int j = i + 1; j < transitions.size(); j++) {

                Transition second = transitions.get(j);

                boolean sameState =
                        first.getFromState().equals(second.getFromState());

                boolean sameSymbol =
                        first.getSymbol().equals(second.getSymbol());

                if (sameState && sameSymbol) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if word is recognized by the automaton.
     *
     * @param word input word
     * @return true if word is recognized
     */
    public boolean recognize(String word) {

        ArrayList<String> currentStates = new ArrayList<>();
        currentStates.add("q0");

        currentStates = epsilonClosure(currentStates);

        for (int i = 0; i < word.length(); i++) {

            String symbol = String.valueOf(word.charAt(i));

            ArrayList<String> nextStates = new ArrayList<>();

            for (String state : currentStates) {

                for (Transition transition : transitions) {

                    if (transition.getFromState().equals(state) && transition.getSymbol().equals(symbol)
                            && !nextStates.contains(transition.getToState())) {

                        nextStates.add(transition.getToState());
                    }
                }
            }

            currentStates = epsilonClosure(nextStates);
        }

        for (String state : currentStates) {

            if (finalStates.contains(state)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Creates epsilon closure for current states.
     *
     * @param currentStates current automaton states
     * @return epsilon closure states
     */
    private ArrayList<String> epsilonClosure(ArrayList<String> currentStates) {

        ArrayList<String> result = new ArrayList<>(currentStates);

        boolean changed = true;

        while (changed) {

            changed = false;

            for (Transition transition : transitions) {

                if (transition.getSymbol().equals("eps")
                        && result.contains(transition.getFromState())
                        && !result.contains(transition.getToState())) {

                    result.add(transition.getToState());
                    changed = true;
                }
            }
        }

        return result;
    }

    /**
     * Checks if automaton language is finite.
     *
     * @return true if language is finite
     */
    public boolean isFinite() {

        for (String state : states) {
            if (hasCycleFromState(state, new ArrayList<String>())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if there is cycle starting from given state.
     *
     * @param state   current state
     * @param visited visited states
     * @return true if cycle exists
     */
    private boolean hasCycleFromState(String state, ArrayList<String> visited) {

        if (visited.contains(state)) {
            return true;
        }

        visited.add(state);

        for (Transition transition : transitions) {

            if (transition.getFromState().equals(state)) {

                ArrayList<String> newVisited = new ArrayList<>(visited);

                if (hasCycleFromState(transition.getToState(), newVisited)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Creates deterministic automaton from current automaton.
     *
     * @param newId identifier of new automaton
     * @return deterministic automaton
     */
    public Automaton determinize(String newId) {

        Automaton result = new Automaton(newId);

        ArrayList<String> alphabet = getAlphabetWithoutEps();

        ArrayList<String> startStates = new ArrayList<>();
        startStates.add("q0");

        ArrayList<String> startClosure = epsilonClosure(startStates);

        ArrayList<ArrayList<String>> unprocessed = new ArrayList<>();
        ArrayList<ArrayList<String>> processed = new ArrayList<>();

        unprocessed.add(startClosure);

        String startName = createStateName(startClosure);
        result.addState(startName);

        if (containsFinalState(startClosure)) {
            result.addFinalState(startName);
        }

        while (!unprocessed.isEmpty()) {

            ArrayList<String> currentStates = unprocessed.remove(0);
            processed.add(currentStates);

            String currentName = createStateName(currentStates);

            for (String symbol : alphabet) {

                ArrayList<String> nextStates = new ArrayList<>();

                for (String state : currentStates) {

                    for (Transition transition : transitions) {

                        if (transition.getFromState().equals(state)
                                && transition.getSymbol().equals(symbol)
                                && !nextStates.contains(transition.getToState())) {

                            nextStates.add(transition.getToState());
                        }
                    }
                }

                nextStates = epsilonClosure(nextStates);

                if (nextStates.isEmpty()) {
                    continue;
                }

                String nextName = createStateName(nextStates);

                if (!result.getStates().contains(nextName)) {
                    result.addState(nextName);

                    if (containsFinalState(nextStates)) {
                        result.addFinalState(nextName);
                    }
                }

                result.addTransition(currentName, symbol, nextName);

                if (!containsStateSet(processed, nextStates)
                        && !containsStateSet(unprocessed, nextStates)) {

                    unprocessed.add(nextStates);
                }
            }
        }

        return result;
    }

    private ArrayList<String> getAlphabetWithoutEps() {

        ArrayList<String> alphabet = new ArrayList<>();

        for (Transition transition : transitions) {

            String symbol = transition.getSymbol();

            if (!symbol.equals("eps") && !alphabet.contains(symbol)) {
                alphabet.add(symbol);
            }
        }

        return alphabet;
    }

    private boolean containsFinalState(ArrayList<String> currentStates) {

        for (String state : currentStates) {

            if (finalStates.contains(state)) {
                return true;
            }
        }

        return false;
    }

    private String createStateName(ArrayList<String> currentStates) {

        String name = "";

        for (String state : currentStates) {
            name += state + "_";
        }

        if (name.endsWith("_")) {
            name = name.substring(0, name.length() - 1);
        }

        return name;
    }

    private boolean containsStateSet(ArrayList<ArrayList<String>> list, ArrayList<String> statesToFind) {

        for (ArrayList<String> states : list) {

            if (states.size() == statesToFind.size()
                    && states.containsAll(statesToFind)) {
                return true;
            }
        }

        return false;
    }
}

