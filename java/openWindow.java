import javax.swing.*;
import java.io.File;

public class openWindow{
    public openWindow() {
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
            //Implement the file loading logic here, or call to it with the file object.
        }
        else {
            JOptionPane.showMessageDialog(openFrame, "Cancelled Operation", "Open Window", JOptionPane.ERROR_MESSAGE);
        }
    }
}
