import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class programWindow{
    public programWindow(WindowController parent, Controller controller) {
        JFrame programFrame = new JFrame();
        programFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        programFrame.setLocationRelativeTo(null);
        programFrame.setResizable(true);
        programFrame.setSize(600, 400);

        JPanel contentPanel = new JPanel();
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        contentPanel.add(info, BorderLayout.EAST);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 1));
        JButton modulesBtn = new JButton("Get Modules");
        modulesBtn.addActionListener(e -> {
            // Update these in the WindowController?
            info.setText("Modules loaded: " + new String(controller.readHardware(), StandardCharsets.ISO_8859_1));
        });
        JButton configsBtn = new JButton("Get Mappings");
        configsBtn.addActionListener(e -> {
            byte[] data = controller.readMappings();
            if (data.length == 1) {
                info.setText("No controller mappings found!");
                return;
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
        });
        JButton resetBtn = new JButton("Wipe Mappings");
        resetBtn.addActionListener(e -> {
            controller.wipeMappings();
            info.setText("Controller mappings wiped!");
        });
        JButton saveBtn = new JButton("Save Mappings");
        saveBtn.addActionListener(e -> {
            controller.sendConfigs();
            info.setText("Uploaded controller mappings!");
        });
        JButton macrosBtn = new JButton("Toggle Macros");
        macrosBtn.addActionListener(e -> {
            controller.toggleMacros();
            info.setText("Switched macros " + (controller.macrosAllowed() ? "on!" : "off!"));
        });
        JButton joysBtn = new JButton("Calibrate Joysticks");
        joysBtn.addActionListener(e -> {
            controller.recalibrate();
            info.setText("Zeroed joystick inputs!");
        });
        JButton testBtn = new JButton("Run Tests");
        testBtn.addActionListener(e -> {
            info.setText(new String(controller.runTests(), StandardCharsets.ISO_8859_1));
        });
        JButton rawBtn = new JButton("Toggle Raw Input Mode");
        rawBtn.addActionListener(e -> {
            if (controller.isRaw()) {
                controller.usbInputMode();
                info.setText("Now receiving USB gamepad input!");
            } else {
                controller.rawInputMode();
                info.setText("Now receiving raw input!");
            }
        });
        btnPanel.add(modulesBtn);
        btnPanel.add(configsBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(macrosBtn);
        btnPanel.add(joysBtn);
        btnPanel.add(testBtn);
        btnPanel.add(rawBtn);
        programFrame.add(btnPanel, BorderLayout.WEST);

        JLabel programLabel = new JLabel("Controller Programming Options", SwingConstants.CENTER);
        programLabel.setFont(new Font(programLabel.getName(), Font.PLAIN, 20));
        JButton okButton = new JButton("Done");
        okButton.addActionListener(e -> programFrame.dispose());

        programFrame.add(programLabel, BorderLayout.NORTH);
        programFrame.add(okButton, BorderLayout.SOUTH);
        programFrame.add(contentPanel, BorderLayout.CENTER);

        programFrame.setVisible(true);
    }
}
