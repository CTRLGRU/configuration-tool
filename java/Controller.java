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
        modules = new Component[4];
    }

    public void initializeController() {
        for (Component module : modules) {
            if (module == null) {
                module = new Component();
                module.initialization();
            }
        }
    }

    public void setModule(int index, int axes, int buttonQty, String name, String description) {
        modules[index].setAxes(axes);
        modules[index].setButtonQty(buttonQty);
        modules[index].setName(name);
        modules[index].setDescription(description);
        modules[index].setModuleNumber(index + 1);
    }

    public Component getModule(int index) {
        return modules[index];
    }

    // DeviceInterface implementations
    public String fileWriter(int ID) {
        // Data includes 1 B per component, 10 B per playback per component, and 4 B per trigger per component
        byte[] data = new byte[mapping.getComponentCount() * (1 + 14 * mapping.getPlaybackCount())];
        int cur;
        for (cur = 0; cur < mapping.getComponentCount(); cur++) {
            data[cur] = mapping.getComponent(cur);
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
