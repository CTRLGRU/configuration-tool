import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class ModulePanel extends JPanel {
    private JComboBox<String> pModule;
    private JComboBox<String> vModule;
    private JTextArea pInput;
    private JTextArea vInput;

    public ModulePanel(String name, String[] physical, String[] virtual) {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setPreferredSize(new Dimension(200, 200));
        setBackground(new Color(0, 0, 0, 0));

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(new JLabel(name, SwingConstants.CENTER), c);

        c.gridy = 1;
        JPanel dropdowns = new JPanel(new GridLayout(1, 2));
        pModule = new JComboBox<String>(physical);
        pModule.setBackground(Color.LIGHT_GRAY);
        dropdowns.add(pModule);
        vModule = new JComboBox<String>(virtual);
        dropdowns.add(vModule);
        add(dropdowns, c);

        c.gridy = 2;
        c.weighty = 0.4;
        c.fill = GridBagConstraints.BOTH;
        JPanel data = new JPanel(new GridLayout(1, 2));
        pInput = new JTextArea();
        pInput.setMinimumSize(new Dimension(100, 100));
        pInput.setMaximumSize(new Dimension(100, 100));
        pInput.setEditable(false);
        pInput.setFocusable(false);
        pInput.setBackground(Color.LIGHT_GRAY);
        pInput.setMargin(new Insets(2, 2, 2, 2));
        data.add(pInput);
        vInput = new JTextArea();
        vInput.setMinimumSize(new Dimension(100, 100));
        vInput.setMaximumSize(new Dimension(100, 100));
        vInput.setEditable(false);
        vInput.setFocusable(false);
        vInput.setBackground(Color.GRAY);
        vInput.setMargin(new Insets(2, 2, 2, 2));
        data.add(vInput);
        add(data, c);

        c.gridy = 3;
        c.weighty = 0.6;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        add(filler, c);
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
        switch(getPhysical()) {
            case "Joystick":
                pInput.setText(fromJoystick(data));
                break;
            case "DPad":
                pInput.setText(fromDPad(data));
                break;
            case "ABXY":
                pInput.setText(fromABXY(data));
                break;
            default:
                pInput.setText("");
        }
    }

    public void setVirtualInput(byte[] data) {
        boolean joy = getPhysical().equals("Joystick");
        switch(getVirtual()) {
            case "Joystick":
                if (joy) { // Joystick to joystick
                    vInput.setText(fromJoystick(data));
                } else { // 4-button to joystick
                    vInput.setText(fromJoystick(toJoystick(data)));
                }
                break;
            case "4-Button":
                if (!joy) { // 4-button to 4-button
                    vInput.setText(fromDPad(data));
                } else { // Joystick to 4-button
                    vInput.setText(fromDPad(to4Button(data)));
                }
                break;
            default:
                vInput.setText("");
        }
    }

    private String fromJoystick(byte[] data) {
        return "X: " + (byte) (data[0] & 0b11111110)
            + "\nY: " + (byte) (data[1] & 0b11111110)
            + "\nButton: " + (data[0] & 1);
    }

    private byte[] toJoystick(byte[] buttonData) {
        byte[] data = {0, 0, 0};
        if ((buttonData[0] & 0b00001111) == 0b00001111) { // Recenter and press button when all four are down
            data[0] = 1;
            data[1] = 0;
        } else { // (right - left) * 127 for X, (up - down) * 127 for y, omit bit 0
            data[0] = (byte) ((127 * ((buttonData[0] & 1) - ((buttonData[0] >> 1) & 1))) & 0b11111110);
            data[1] = (byte) ((127 * (((buttonData[0] >> 3) & 1) - ((buttonData[0] >> 2) & 1)))  & 0b11111110);
        }
        return data;
    }

    private String fromDPad(byte[] data) {
        return "Up: " + ((data[0] >> 3) & 1)
            + "\nDown: " + ((data[0] >> 2) & 1)
            + "\nLeft: " + ((data[0] >> 1) & 1)
            + "\nRight: " + (data[0] & 1);
    }
    private String fromABXY(byte[] data) {
        return "A: " + ((data[0] >> 2) & 1)
            + "\nB: " + (data[0] & 1)
            + "\nX: " + ((data[0] >> 1) & 1)
            + "\nY: " + ((data[0] >> 3) & 1);
    }

    private byte[] to4Button(byte[] joystickData) {
        byte[] data = {0b01110000, 0, 0};
        if (joystickData[0] > 64) { // >50% right, set right bit
            data[0] |= 0b00000001;
        } else if (joystickData[0] < -64) { // >50% left, set left bit
            data[0] |= 0b00000010;
        }
        if (joystickData[1] > 64) { // >50% up, set up bit
            data[0] |= 0b00001000;
        } else if (joystickData[1] < -64) { // >50% down, set down bit
            data[0] |= 0b00000100;
        }
        return data;
    }
}