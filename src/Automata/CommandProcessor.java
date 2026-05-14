package Automata;
import Automata.Models.Automaton;
import Automata.Managers.AutomatonManager;
import Automata.Managers.FileManager;
import java.util.Scanner;

public class CommandProcessor {

    private FileManager fileManager = new FileManager();
    private AutomatonManager manager = new AutomatonManager();
    private int automatonCounter = 1;

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================");
        System.out.println("      AUTOMATA PROJECT");
        System.out.println("========================");
        System.out.println();
        System.out.println("Type 'help' to see commands.");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();

            if (command.equals("help")) {

                System.out.println("========== COMMANDS ==========");
                System.out.println("help                      - Shows all commands");
                System.out.println("open <file>               - Opens file");
                System.out.println("close                     - Closes current file");
                System.out.println("save                      - Saves current file");
                System.out.println("save as <file>            - Saves in new file");
                System.out.println("test                      - Creates test automaton");
                System.out.println("list                      - Lists automata");
                System.out.println("print <id>                - Prints automaton");
                System.out.println("empty <id>                - Checks if automaton is empty");
                System.out.println("deterministic <id>        - Checks if automaton is deterministic");
                System.out.println("recognize <id> <word>     - Checks word");
                System.out.println("union <id1> <id2>         - Unions automata");
                System.out.println("concat <id1> <id2>        - Concatenates automata");
                System.out.println("un <id>                   - Creates positive closure");
                System.out.println("reg <regex>               - Creates regex automaton");
                System.out.println("exit                      - Exits program");
                System.out.println("==============================");
            }

            else if (command.startsWith("open ")) {

                String fileName = command.substring(5);

                fileManager.openFile(fileName, manager);
            }

            else if (command.equals("close")) {
                fileManager.closeFile();
            }

            else if (command.equals("save")) {
                fileManager.saveFile(manager);
            }

            else if (command.equals("test")) {

                String automatonId = "A" + automatonCounter;

                Automaton automaton = new Automaton(automatonId);

                automaton.addState("q0");
                automaton.addState("q1");

                automaton.addFinalState("q1");

                automaton.addTransition("q0", "a", "q1");
                automaton.addTransition("q1", "b", "q0");

                manager.addAutomaton(automaton);

                System.out.println("Test automaton created: " + automatonId);

                automatonCounter++;
            }

            else if (command.startsWith("save as ")) {

                String fileName = command.substring(8);

                fileManager.saveAsFile(fileName, manager);
            }

            else if (command.equals("list")) {
                manager.listAutomata();
            }

            else if (command.startsWith("print ")) {

                String id = command.substring(6);

                Automaton automaton = manager.findAutomaton(id);

                if (automaton == null) {
                    System.out.println("Automaton not found.");
                }
                else {
                    automaton.printInfo();
                }
            }

            else if (command.startsWith("empty ")) {

                String id = command.substring(6);

                Automaton automaton = manager.findAutomaton(id);

                if (automaton == null) {
                    System.out.println("Automaton not found.");
                }
                else {

                    if (automaton.isEmpty()) {
                        System.out.println("Automaton language is empty.");
                    }
                    else {
                        System.out.println("Automaton language is NOT empty.");
                    }
                }
            }

            else if (command.startsWith("deterministic ")) {

                String id = command.substring(14);

                Automaton automaton = manager.findAutomaton(id);

                if (automaton == null) {
                    System.out.println("Automaton not found.");
                }
                else {

                    if (automaton.isDeterministic()) {
                        System.out.println("Automaton is deterministic.");
                    }
                    else {
                        System.out.println("Automaton is NOT deterministic.");
                    }
                }
            }

            else if (command.startsWith("recognize ")) {

                String text = command.substring(10);
                String[] parts = text.split(" ");

                if (parts.length != 2) {
                    System.out.println("Invalid command. Use: recognize <id> <word>");
                }
                else {
                    String id = parts[0];
                    String word = parts[1];

                    Automaton automaton = manager.findAutomaton(id);

                    if (automaton == null) {
                        System.out.println("Automaton not found.");
                    }
                    else {
                        if (automaton.recognize(word)) {
                            System.out.println("The word is recognized.");
                        }
                        else {
                            System.out.println("The word is NOT recognized.");
                        }
                    }
                }
            }

            else if (command.startsWith("union ")) {

                String text = command.substring(6);
                String[] parts = text.split(" ");

                if (parts.length != 2) {
                    System.out.println("Invalid command. Use: union <id1> <id2>");
                }
                else {

                    String firstId = parts[0];
                    String secondId = parts[1];

                    Automaton first = manager.findAutomaton(firstId);
                    Automaton second = manager.findAutomaton(secondId);

                    if (first == null || second == null) {
                        System.out.println("Automaton not found.");
                    }
                    else {

                        String newId = "A" + automatonCounter;

                        manager.unionAutomata(first, second, newId);

                        System.out.println("Union automaton created: " + newId);

                        automatonCounter++;
                    }
                }
            }

            else if (command.startsWith("concat ")) {

                String text = command.substring(7);
                String[] parts = text.split(" ");

                if (parts.length != 2) {
                    System.out.println("Invalid command. Use: concat <id1> <id2>");
                }
                else {

                    String firstId = parts[0];
                    String secondId = parts[1];

                    Automaton first = manager.findAutomaton(firstId);
                    Automaton second = manager.findAutomaton(secondId);

                    if (first == null || second == null) {
                        System.out.println("Automaton not found.");
                    }
                    else {

                        String newId = "A" + automatonCounter;

                        manager.concatAutomata(first, second, newId);

                        System.out.println("Concatenated automaton created: " + newId);

                        automatonCounter++;
                    }
                }
            }

            else if (command.startsWith("reg ")) {

                String regex = command.substring(4);

                String newId = "A" + automatonCounter;

                manager.regexAutomaton(regex, newId);

                System.out.println("Regex automaton created: " + newId);

                automatonCounter++;
            }

            else if (command.startsWith("un ")) {

                String id = command.substring(3);

                Automaton automaton = manager.findAutomaton(id);

                if (automaton == null) {
                    System.out.println("Automaton not found.");
                }
                else {

                    String newId = "A" + automatonCounter;

                    manager.unAutomaton(automaton, newId);

                    System.out.println("Positive closure automaton created: " + newId);

                    automatonCounter++;
                }
            }

            else if (command.equals("exit")) {
                System.out.println("Exiting...");
                break;
            }

            else {
                System.out.println("Unknown command");
            }
        }


        scanner.close();
    }
}