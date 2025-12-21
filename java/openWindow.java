import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class openWindow{
    public openWindow(WindowController parent) {
        JFrame openFrame = new JFrame("Open Window");
        openFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        openFrame.setLocationRelativeTo(null);
        openFrame.setSize(600, 400);
        openFrame.setResizable(true);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Open File...");
        int result = fileChooser.showOpenDialog(openFrame); //hopefully you work
        if(result == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(openFrame, "Selected File" + file.getAbsolutePath(), "Open Window", JOptionPane.INFORMATION_MESSAGE);
            // According to pico2wmultithreadedcentralcontroller.ino's setCustomMapping function, a string of >= 4 chars is needed
            // So, each module can be represented by 1 byte, where 'J'/'B'/'X' corresponds to joystick/4-button/unknown
            // Presumably, ABXY and the D-pad are programmatically equivalent
            int[][] data = parseConfig(file);
            char[] modules = parseData(data);
            parent.setModules(modules, data);
        }
        else {
            JOptionPane.showMessageDialog(openFrame, "Cancelled Operation", "Open Window", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int[][] parseConfig(File in) {
        int[][] data = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        try {
            InputStream is = new FileInputStream(in);
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            int r = reader.read();

            char c = (char) r;
            boolean valid = (c == '[');
            byte module = 0;
            byte attribute = 0;
            while (valid && r != -1 && c != ']') { // First char is '[', no read error, no ']' reached
                if (c == '=') { // New module delimiter
                    module++;
                } else if (c == ':') { // New piece of data delimiter
                    attribute++;
                    attribute %= 3;
                } else if (c != '\n' && c != ',' && c != ' ' && c != '[') { // Anything meaningful that's left (digits)
                    data[module - 1][attribute] = (int) c - '0';
                }
                r = reader.read();
                c = (char) r;
            }
            return data;
        } catch (Exception ex) {
            ex.printStackTrace();
            return data;
        }
    }

    private char[] parseData(int[][] data) {
        char[] modules = {'X', 'X', 'X', 'X'};
        for (int i = 0; i < 4; i++) {
            if (data[i][2] < 0 || data[i][2] > 4) {
                continue;
            }
            if (data[i][1] == 2) {
                modules[data[i][2] - 1] = 'J';
            } else if (data[i][0] == 4) {
                // Currently no way to differentiate Dpad and ABXY (4 button, 0 axes)
                modules[data[i][2] - 1] = 'B';
            } else {
                modules[data[i][2] - 1] = 'X';
            }
        }
        return modules;
    }
}
