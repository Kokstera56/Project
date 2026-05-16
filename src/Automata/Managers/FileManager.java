package Automata.Managers;

import Automata.Models.Automaton;
import Automata.Models.Transition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {

    private String currentFile;
    private boolean fileOpened;

    public FileManager() {
        currentFile = "";
        fileOpened = false;
    }

    public void openFile(String fileName, AutomatonManager manager) {

        currentFile = fileName;
        fileOpened = true;

        manager.clearAutomata();

        try {

            File file = new File("files/" + fileName);
            Scanner scanner = new Scanner(file);

            Automaton currentAutomaton = null;

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();

                if (line.startsWith("AUTOMATON")) {

                    String[] parts = line.split(" ");

                    currentAutomaton = new Automaton(parts[1]);

                    manager.addAutomaton(currentAutomaton);
                } else if (line.startsWith("STATES")) {

                    String[] parts = line.split(" ");

                    for (int i = 1; i < parts.length; i++) {
                        currentAutomaton.addState(parts[i]);
                    }
                } else if (line.startsWith("FINAL")) {

                    String[] parts = line.split(" ");

                    for (int i = 1; i < parts.length; i++) {
                        currentAutomaton.addFinalState(parts[i]);
                    }
                } else if (line.startsWith("TRANSITION")) {

                    String[] parts = line.split(" ");

                    currentAutomaton.addTransition(
                            parts[1],
                            parts[2],
                            parts[3]
                    );
                }
            }

            scanner.close();

            System.out.println("Successfully opened " + fileName);
        } catch (Exception e) {
            System.out.println("Error while opening file.");
        }
    }

    public void closeFile() {
        if (!fileOpened) {
            System.out.println("No opened file.");
            return;
        }

        System.out.println("File closed.");

        currentFile = "";
        fileOpened = false;
    }

    public void saveFile(AutomatonManager manager) {
        if (!fileOpened) {
            System.out.println("No opened file.");
            return;
        }

        try {
            FileWriter writer = new FileWriter("files/" + currentFile);

            for (Automaton automaton : manager.getAutomata()) {
                writer.write("AUTOMATON " + automaton.getId() + "\n");

                writer.write("STATES ");
                for (String state : automaton.getStates()) {
                    writer.write(state + " ");
                }
                writer.write("\n");

                writer.write("FINAL ");
                for (String state : automaton.getFinalStates()) {
                    writer.write(state + " ");
                }
                writer.write("\n");

                for (Transition transition : automaton.getTransitions()) {
                    writer.write("TRANSITION "
                            + transition.getFromState() + " "
                            + transition.getSymbol() + " "
                            + transition.getToState() + "\n");
                }

                writer.write("END\n");
            }

            writer.close();

            System.out.println("[SUCCESS] File saved: " + currentFile);
        } catch (IOException e) {
            System.out.println("Error while saving file.");
        }
    }

    public boolean hasOpenedFile() {
        return fileOpened;
    }

    public void saveAsFile(String newFile, AutomatonManager manager) {

        currentFile = newFile;
        fileOpened = true;

        saveFile(manager);
    }

}