package Automata.Managers;

import Automata.Models.Automaton;
import Automata.Models.Transition;

import java.util.ArrayList;

/**
 * Manages all automata in the system.
 */
public class AutomatonManager {

    private ArrayList<Automaton> automata;

    public AutomatonManager() {
        automata = new ArrayList<>();
    }

    /**
     * Adds automaton to manager.
     *
     * @param automaton automaton object
     */
    public void addAutomaton(Automaton automaton) {
        automata.add(automaton);
    }

    /**
     * Prints all loaded automata.
     */
    public void listAutomata() {

        if (automata.isEmpty()) {
            System.out.println("[INFO] No automata loaded.");
            return;
        }

        System.out.println("[INFO] Loaded automata:");

        for (Automaton automaton : automata) {
            System.out.println(automaton.getId());
        }
    }

    /**
     * Finds automaton by identifier.
     *
     * @param id automaton identifier
     * @return found automaton
     */
    public Automaton findAutomaton(String id) {

        for (Automaton automaton : automata) {

            if (automaton.getId().equals(id)) {
                return automaton;
            }
        }

        return null;
    }

    /**
     * Creates union of two automata.
     *
     * @param first  first automaton
     * @param second second automaton
     * @param newId  identifier of new automaton
     * @return new automaton
     */
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

    /**
     * Concatenates two automata.
     *
     * @param first  first automaton
     * @param second second automaton
     * @param newId  identifier of new automaton
     * @return concatenated automaton
     */
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

    /**
     * Creates positive closure of automaton.
     *
     * @param automaton source automaton
     * @param newId     identifier of new automaton
     * @return new automaton
     */
    public Automaton unAutomaton(Automaton automaton, String newId) {

        Automaton result = new Automaton(newId);

        for (String state : automaton.getStates()) {
            result.addState(state);
        }

        for (String state : automaton.getFinalStates()) {
            result.addFinalState(state);
        }

        for (Transition transition : automaton.getTransitions()) {

            result.addTransition(
                    transition.getFromState(),
                    transition.getSymbol(),
                    transition.getToState()
            );
        }

        automata.add(result);

        return result;
    }

    public ArrayList<Automaton> getAutomata() {
        return automata;
    }

    public void clearAutomata() {
        automata.clear();
    }

    /**
     * Creates automaton from regular expression.
     *
     * @param regex regular expression
     * @param newId identifier of new automaton
     * @return created automaton
     */
    public Automaton regexAutomaton(String regex, String newId) {

        Automaton automaton = new Automaton(newId);

        automaton.addState("q0");
        automaton.addState("q1");

        automaton.addFinalState("q1");

        automaton.addTransition("q0", regex, "q1");

        automata.add(automaton);

        return automaton;
    }

    /**
     * Creates deterministic version of automaton.
     *
     * @param automaton source automaton
     * @param newId     identifier of new automaton
     * @return deterministic automaton
     */
    public Automaton determinizeAutomaton(Automaton automaton, String newId) {

        Automaton result = automaton.determinize(newId);

        automata.add(result);

        return result;
    }
}