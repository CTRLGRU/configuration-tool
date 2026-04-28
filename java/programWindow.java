import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class programWindow{
    public programWindow(WindowController parent, Controller controller) {
        JFrame programFrame = new JFrame("Program...");
        programFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        programFrame.setLocationRelativeTo(null);
        programFrame.setResizable(true);
        programFrame.setSize(600, 400);

        JPanel contentPanel = new JPanel();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        contentPanel.add(info);
        JTextArea output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        contentPanel.add(output);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 1));
        JButton modulesBtn = new JButton("Get Modules");
        modulesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update these in the WindowController?
                info.setText("Modules loaded: " + new String(controller.readHardware(), StandardCharsets.ISO_8859_1));
            }
        });
        JButton configsBtn = new JButton("Get Mappings");
        configsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                byte[] data = controller.readMappings();
                if (data.length == 1) {
                    info.setText("No controller mappings found!");
                }
                for (int i = 0; i < controller.getMappingCount(); i++) {
                    byte[] dataPiece = new byte[data.length / controller.getMappingCount()];
                    System.arraycopy(data, data.length * i / controller.getMappingCount(), dataPiece, 0, dataPiece.length);
                    Mapping mapping = Mapping.generateMapping(dataPiece, controller.getModuleCount(), controller.getMacroCount());
                    controller.setMapping(
                        i,
                        mapping
                    );
                    parent.setMapping(i, mapping);
                }
                info.setText("Loaded controller mappings!");
            }
        });
        JButton resetBtn = new JButton("Wipe Mappings");
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.wipeMappings();
                info.setText("Controller mappings wiped!");
            }
        });
        JButton saveBtn = new JButton("Save Mappings");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.sendConfigs();
                info.setText("Uploaded controller mappings!");
            }
        });
        JButton testBtn = new JButton("Run Tests");
        testBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                info.setText(new String(controller.runTests(), StandardCharsets.ISO_8859_1));
            }
        });
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
