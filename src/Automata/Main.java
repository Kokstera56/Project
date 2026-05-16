package Automata;

/**
 * Main class of the application.
 * Starts the command processor.
 */
public class Main {
    /**
     * Entry point of the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        CommandProcessor processor = new CommandProcessor();
        processor.start();
    }
}
