import javax.swing.*;
import java.awt.*;

public class aboutWindow extends JFrame{
    private JPanel contentPanel;

    public aboutWindow(String version) {
        JFrame aboutFrame = new JFrame("About");
        aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        aboutFrame.setLocationRelativeTo(null);
        aboutFrame.setResizable(false);
        aboutFrame.setLayout(new BorderLayout());
        JPanel aboutPanel = new JPanel();
        //aboutFrame.add(contentPanel, BorderLayout.CENTER);
        //turns out this does just as well. I shouldn't have snuck this in refactored code, but it will be reduced and pretty-ified.
        JOptionPane.showMessageDialog(aboutFrame, "<html><div style='text-align: center;'>FMC: Software version "+version+"<br>Licensed under GPLv3 All Rights Reserved<br>Developed by Allen \"LF\"  B., Lloyd \"Koda\" C., Damian C.</div></html>", "About", JOptionPane.PLAIN_MESSAGE);
        aboutFrame.setVisible(false);
    }
}
