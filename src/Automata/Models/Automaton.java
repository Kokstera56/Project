package Automata.Models;
import java.util.ArrayList;

public class Automaton {

    private String id;
    private ArrayList<String> states;
    private ArrayList<String> finalStates;
    private ArrayList<Transition> transitions;

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

    public void addState(String state) {
        states.add(state);
    }

    public void addFinalState(String state) {
        finalStates.add(state);
    }

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
    public boolean isEmpty() {

        return finalStates.isEmpty();
    }
    public void addTransition(String fromState, String symbol, String toState) {

        Transition transition =
                new Transition(fromState, symbol, toState);

        transitions.add(transition);
    }
    public boolean isDeterministic() {

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
    
    public boolean recognize(String word) {

        ArrayList<String> currentStates = new ArrayList<>();
        currentStates.add("q0");

        currentStates = epsilonClosure(currentStates);

        for (int i = 0; i < word.length(); i++) {

            String symbol = String.valueOf(word.charAt(i));

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

            currentStates = epsilonClosure(nextStates);
        }

        for (String state : currentStates) {

            if (finalStates.contains(state)) {
                return true;
            }
        }

        return false;
    }

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
}

