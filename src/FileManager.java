public class FileManager {

    private String currentFile;
    private boolean fileOpened;

    public FileManager() {

        currentFile = "";
        fileOpened = false;
    }

    public void openFile(String fileName) {

        currentFile = fileName;
        fileOpened = true;

        System.out.println("Successfully opened " + fileName);
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

    public void saveFile() {

        if (!fileOpened) {
            System.out.println("No opened file.");
            return;
        }

        System.out.println("File saved: " + currentFile);
    }

    public boolean hasOpenedFile() {
        return fileOpened;
    }
}