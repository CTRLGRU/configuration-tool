import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Controller programController = new Controller();
        programController.initializeController();
        WindowController windowController = new WindowController(programController, "0.3.0");
    }
}