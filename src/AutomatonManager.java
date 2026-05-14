import java.util.ArrayList;

public class AutomatonManager {

    private ArrayList<Automaton> automata;

    public AutomatonManager() {
        automata = new ArrayList<>();
    }

    public void addAutomaton(Automaton automaton) {
        automata.add(automaton);
    }

    public void listAutomata() {

        if (automata.isEmpty()) {
            System.out.println("No automata loaded.");
            return;
        }

        System.out.println("Loaded automata:");

        for (Automaton automaton : automata) {
            System.out.println(automaton.getId());
        }
    }

    public Automaton findAutomaton(String id) {

        for (Automaton automaton : automata) {

            if (automaton.getId().equals(id)) {
                return automaton;
            }
        }

        return null;
    }
    public Automaton unionAutomata(Automaton first, Automaton second, String newId) {

        Automaton result = new Automaton(newId);

        for (String state : first.getStates()) {
            result.addState(state);
        }

        for (String state : second.getStates()) {

            if (!result.getStates().contains(state)) {
                result.addState(state);
            }
        }

        for (String state : first.getFinalStates()) {
            result.addFinalState(state);
        }

        for (String state : second.getFinalStates()) {

            if (!result.getFinalStates().contains(state)) {
                result.addFinalState(state);
            }
        }

        for (Transition transition : first.getTransitions()) {

            result.addTransition(
                    transition.getFromState(),
                    transition.getSymbol(),
                    transition.getToState()
            );
        }

        for (Transition transition : second.getTransitions()) {

            result.addTransition(
                    transition.getFromState(),
                    transition.getSymbol(),
                    transition.getToState()
            );
        }

        automata.add(result);

        return result;
    }
    public Automaton concatAutomata(Automaton first, Automaton second, String newId) {

        Automaton result = new Automaton(newId);

        for (String state : first.getStates()) {
            result.addState(state);
        }

        for (String state : second.getStates()) {
            if (!result.getStates().contains(state)) {
                result.addState(state);
            }
        }

        for (String state : second.getFinalStates()) {
            result.addFinalState(state);
        }

        for (Transition transition : first.getTransitions()) {
            result.addTransition(
                    transition.getFromState(),
                    transition.getSymbol(),
                    transition.getToState()
            );
        }

        for (Transition transition : second.getTransitions()) {
            result.addTransition(
                    transition.getFromState(),
                    transition.getSymbol(),
                    transition.getToState()
            );
        }

        automata.add(result);

        return result;
    }
}