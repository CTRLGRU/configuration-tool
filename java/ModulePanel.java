import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class ModulePanel extends JPanel {
    private JComboBox<String> pModule;
    private JComboBox<String> vModule;
    private JTextArea pInput;
    private JTextArea vInput;

    public ModulePanel(String name, String[] physical, String[] virtual) {
        super(new GridLayout(4, 1));
        setPreferredSize(new Dimension(200, 200));
        setBackground(new Color(0, 0, 0, 0));

        add(new JLabel(name, SwingConstants.CENTER));
        JPanel dropdowns = new JPanel(new GridLayout(1, 2));
        pModule = new JComboBox<String>(physical);
        pModule.setBackground(Color.LIGHT_GRAY);
        dropdowns.add(pModule);
        vModule = new JComboBox<String>(virtual);
        dropdowns.add(vModule);
        add(dropdowns);
        JPanel data = new JPanel(new GridLayout(1, 2));
        pInput = new JTextArea();
        pInput.setEditable(false);
        pInput.setFocusable(false);
        pInput.setBackground(Color.LIGHT_GRAY);
        pInput.setMargin(new Insets(4, 4, 4, 4));
        data.add(pInput);
        vInput = new JTextArea();
        vInput.setEditable(false);
        vInput.setFocusable(false);
        vInput.setBackground(Color.GRAY);
        vInput.setMargin(new Insets(4, 4, 4, 4));
        data.add(vInput);
        add(data);
    }

    public JComboBox<String> getPhysicalDropdown() {
        return pModule;
    }

    public JComboBox<String> getVirtualDropdown() {
        return pModule;
    }

    public String getPhysical() {
        return (String) pModule.getSelectedItem();
    }

    public String getVirtual() {
        return (String) vModule.getSelectedItem();
    }

    public void setPhysical(String label) {
        pModule.setSelectedItem(label);
    }

    public void setVirtual(String label) {
        vModule.setSelectedItem(label);
    }

    public String getPhysicalInput() {
        return pInput.getText();
    }

    public String getVirtualInput() {
        return vInput.getText();
    }

    public void setPhysicalInput(byte[] data) {
        // Process bytes into human-readable results
        pInput.setText(new String(data, StandardCharsets.ISO_8859_1));
    }

    public void setVirtualInput(byte[] data) {
        // Process bytes into human-readable results
        vInput.setText("");
    }
}