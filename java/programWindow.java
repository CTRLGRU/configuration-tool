import javax.swing.*;
import java.awt.*;

public class programWindow{
    public programWindow() {
        JFrame programFrame = new JFrame("Program...");
        programFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        programFrame.setLocationRelativeTo(null);
        programFrame.setResizable(true);
        programFrame.setSize(600, 400);
        JPanel contentPanel = new JPanel();
        JLabel programLabel = new JLabel("Program options n stuff", SwingConstants.CENTER);
        programLabel.setFont(new Font(programLabel.getName(), Font.PLAIN, 20));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> programFrame.dispose());
        contentPanel.add(programLabel, BorderLayout.NORTH);
        contentPanel.add(okButton, BorderLayout.SOUTH);
        programFrame.add(contentPanel, BorderLayout.CENTER);
        programFrame.setVisible(true);
    }
}
