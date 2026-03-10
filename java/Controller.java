import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;

public class Controller implements DeviceInterface {
    private SerialPort port;
    private final Mapping mapping;
    private final Component[] modules;

    public Controller(int components, int macros, int triggerLen, int playbackLen) {
        mapping = new Mapping(components, macros, triggerLen, playbackLen);
        modules = new Component[components];
    }

    public Controller(int components, int macros) {
        mapping = new Mapping(components, macros);
        modules = new Component[components];
    }

    public Controller() {
        mapping = new Mapping();
        modules = new Component[Mapping.DEFAULT_COMPONENT_COUNT];
    }

    public void initializeController() {
        for (int i = 0; i < modules.length; i++) {
            if (modules[i] == null) {
                modules[i] = new Component();
                modules[i].initialization();
            }
        }
    }

    public int getModuleCount() {
        return modules.length;
    }

    public Component getModule(int index) {
        return modules[index];
    }

    public void setModule(int index, int axes, int buttonQty, String name, String description) {
        modules[index].setAxes(axes);
        modules[index].setButtonQty(buttonQty);
        modules[index].setName(name);
        modules[index].setDescription(description);
        modules[index].setModuleNumber(index + 1);
        switch(name) {
            case "Joystick":
                mapping.setComponent(index, (byte) 'J');
                break;
            case "DPad":
            case "ABXY":
                mapping.setComponent(index, (byte) 'B');
                break;
            default:
                mapping.setComponent(index, (byte) 0);
        }
    }

    public int getMacroCount() {
        return mapping.getMacroCount();
    }

    public void setComponent(int index, byte code) {
        mapping.setComponent(index, code);
    }

    // DeviceInterface implementations
    @Override
    public String fileWriter(int ID) {
        // Data includes 1 B per component, 1 B per playback length per playback per component, and 1 B
        // per trigger length per trigger per component.
        // Realized we should also have 1 B each for module and macro counts to properly read configs
        byte[] data = new byte[
            modules.length *
            (1 + (mapping.getMacro(0).getTriggerLength() + mapping.getMacro(0).getPlaybackLength()) *
            mapping.getMacroCount()) + 2
        ];
        data[0] = (byte) modules.length;
        data[1] = (byte) mapping.getMacroCount();
        int cur;
        for (cur = 2; cur < modules.length + 2; cur++) {
            data[cur] = mapping.getComponent(cur - 2);
        }
        for (int i = 0; i < mapping.getMacroCount(); i++) {
            System.arraycopy(mapping.getMacro(i).getTrigger(), 0, data, cur, mapping.getMacro(i).getTriggerLength());
            cur += mapping.getMacro(i).getTriggerLength();
        }
        for (int i = 0; i < mapping.getMacroCount(); i++) {
            System.arraycopy(mapping.getMacro(i).getPlayback(), 0, data, cur, mapping.getMacro(i).getPlaybackLength());
            cur += mapping.getMacro(i).getPlaybackLength();
        }
        return new String(data, StandardCharsets.ISO_8859_1);
    }

    @Override
    public boolean connect(int baud) {
        return USBController.open(port.getSystemPortName(), baud);
    }

    @Override
    public boolean disconnect() {
        return USBController.close();
    }

    // Need to look at module-firmware for communication
    @Override
    public byte[] readHardware() {
        return null;
    }
    @Override
    public byte[] readInput() {
        return null;
    }
    @Override
    public boolean sendConfig(byte[] data) {
        return false;
    }

    // Fixed module count functions (backwards-compatibility)
    public int returnModule1State() {
        if (modules.length == 0 || modules[0] == null) {
            return -1;
        }
        return modules[0].getModuleNumber() + modules[0].getButtonQty() + modules[0].getAxes();
    }

    public int returnModule2State() {
        if (modules.length < 2 || modules[1] == null) {
            return -1;
        }
        return modules[1].getModuleNumber() + modules[1].getButtonQty() + modules[1].getAxes();
    }

    public int returnModule3State() {
        if (modules.length < 3 || modules[2] == null) {
            return -1;
        }
        return modules[2].getModuleNumber() + modules[2].getButtonQty() + modules[2].getAxes();
    }

    public int returnModule4State() {
        if (modules.length < 4 || modules[3] == null) {
            return -1;
        }
        return modules[3].getModuleNumber() + modules[3].getButtonQty() + modules[3].getAxes();
    }

    public void setModule1(int axes, int buttonQty, String name) {
        setModule(0, axes, buttonQty, name, "");
    }

    public void setModule2(int axes, int buttonQty, String name) {
        setModule(1, axes, buttonQty, name, "");
    }

    public void setModule3(int axes, int buttonQty, String name) {
        setModule(2, axes, buttonQty, name, "");
    }

    public void setModule4(int axes, int buttonQty, String name) {
        setModule(3, axes, buttonQty, name, "");
    }
}
