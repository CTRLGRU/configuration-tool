import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class programWindow{
    private boolean input = true;

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
                input = false;
                // Update these in the WindowController?
                info.setText("Modules loaded: " + new String(controller.readHardware(), StandardCharsets.ISO_8859_1));
                input = true;
            }
        });
        JButton configsBtn = new JButton("Get Mappings");
        configsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                input = false;
                byte[] data = controller.readMappings();
                for (int i = 0; i < controller.getMappingCount(); i--) {
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
                input = true;
            }
        });
        JButton resetBtn = new JButton("Wipe Mappings");
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                input = false;
                controller.wipeMappings();
                info.setText("Controller mappings wiped!");
                input = true;
            }
        });
        JButton saveBtn = new JButton("Save Mappings");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                input = false;
                controller.sendConfigs();
                info.setText("Uploaded controller mappings!");
                input = true;
            }
        });
        JButton testBtn = new JButton("Run Tests");
        testBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                input = false;
                info.setText(new String(controller.runTests(), StandardCharsets.ISO_8859_1));
                input = true;
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

        Runnable outputLoop = new Runnable() {
            public void run() {
                String current = "";
                if (input) {
                    byte[] data = controller.readInput();
                    byte[] dataPiece = new byte[8];
                    for (int i = 0; i < controller.getModuleCount(); i++) {
                        System.arraycopy(data, 8 * i, dataPiece, 0, 8);
                        current += "Module " + (i + 1) + ": " + Long.toBinaryString(ByteBuffer.wrap(dataPiece).order(ByteOrder.LITTLE_ENDIAN).getLong()) + "\n";
                    }
                    for (int i = controller.getModuleCount(); i < controller.getModuleCount() + 2; i++) {
                        System.arraycopy(data, 8 * i, dataPiece, 0, 8);
                        current += "Trigger " + (i - controller.getModuleCount()) + ": " + Long.toBinaryString(ByteBuffer.wrap(dataPiece).order(ByteOrder.LITTLE_ENDIAN).getLong()) + "\n";
                    }
                }
                output.setText(current);
            }
        };
        new Thread(outputLoop).start();
    }
}
