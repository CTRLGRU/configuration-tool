import java.nio.charset.StandardCharsets;

public class Controller implements DeviceInterface {
    private Mapping mapping;
    private Component[] modules;

    public Controller(int components, int playbacks) {
        mapping = new Mapping(components, playbacks);
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

    public int getPlaybackCount() {
        return mapping.getPlaybackCount();
    }

    public void setComponent(int index, byte code) {
        mapping.setComponent(index, code);
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

    public Component getModule(int index) {
        return modules[index];
    }

    // DeviceInterface implementations
    public String fileWriter(int ID) {
        // Data includes 1 B per component, 10 B per playback per component, and 4 B per trigger per component
        // Realized we should also have 1 B each for module and playback counts to properly read configs
        byte[] data = new byte[modules.length * (1 + 14 * mapping.getPlaybackCount()) + 2];
        data[0] = (byte) modules.length;
        data[1] = (byte) mapping.getPlaybackCount();
        int cur;
        for (cur = 2; cur < modules.length + 2; cur++) {
            data[cur] = mapping.getComponent(cur - 2);
        }
        for (int i = 0; i < mapping.getPlaybackCount(); i++) {
            System.arraycopy(mapping.getTrigger(i), 0, data, cur, mapping.getTrigger(i).length);
            cur += mapping.getTrigger(i).length;
        }
        for (int i = 0; i < mapping.getPlaybackCount(); i++) {
            System.arraycopy(mapping.getPlayback(i), 0, data, cur, mapping.getPlayback(i).length);
            cur += mapping.getPlayback(i).length;
        }
        return new String(data, StandardCharsets.ISO_8859_1);
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
