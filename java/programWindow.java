import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class programWindow{
    public programWindow(Controller controller) {
        JFrame programFrame = new JFrame("Program...");
        programFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        programFrame.setLocationRelativeTo(null);
        programFrame.setResizable(true);
        programFrame.setSize(600, 400);

        JPanel contentPanel = new JPanel();
        JTextArea output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 1));
        JButton modulesBtn = new JButton("Get Modules");
        modulesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        JButton configsBtn = new JButton("Get Mappings");
        JButton resetBtn = new JButton("Wipe Mappings");
        JButton saveBtn = new JButton("Save Mappings");
        JButton testBtn = new JButton("Run Tests");
        btnPanel.add(modulesBtn);
        btnPanel.add(configsBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(testBtn);
        programFrame.add(btnPanel, BorderLayout.WEST);

        JLabel programLabel = new JLabel("Program options n stuff", SwingConstants.CENTER);
        programLabel.setFont(new Font(programLabel.getName(), Font.PLAIN, 20));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> programFrame.dispose());

        programFrame.add(programLabel, BorderLayout.NORTH);
        programFrame.add(okButton, BorderLayout.SOUTH);
        programFrame.add(contentPanel, BorderLayout.CENTER);

        programFrame.setVisible(true);
    }
}
