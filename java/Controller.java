import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;

public class Controller implements DeviceInterface {
    public static final int DEFAULT_CONFIG_COUNT = 3;
    private final Mapping[] mappings;
    private final Component[] modules;
    private SerialPort port;
    private int curMapping = 0;

    public Controller(int components, int configs, int macros, int triggerLen, int playbackLen) {
        mappings = new Mapping[configs];
        for (int i = 0; i < configs; i++) {
            mappings[i] = new Mapping(components, macros, triggerLen, playbackLen);
        }
        modules = new Component[components];
    }

    public Controller(int components, int configs, int macros) {
        mappings = new Mapping[configs];
        for (int i = 0; i < configs; i++) {
            mappings[i] = new Mapping(components, macros);
        }
        modules = new Component[components];
    }

    public Controller() {
        mappings = new Mapping[DEFAULT_CONFIG_COUNT];
        for (int i = 0; i < DEFAULT_CONFIG_COUNT; i++) {
            mappings[i] = new Mapping(Mapping.DEFAULT_COMPONENT_COUNT, Mapping.DEFAULT_MACRO_COUNT);
        }
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
                mappings[curMapping].setComponent(index, (byte) 'J');
                break;
            case "DPad":
            case "ABXY":
                mappings[curMapping].setComponent(index, (byte) 'B');
                break;
            default:
                mappings[curMapping].setComponent(index, (byte) 0);
        }
    }

    public Mapping getMapping(int index) {
        return mappings[index];
    }

    public void setMapping(int index, Mapping mapping) {
        mappings[index] = mapping;
    }

    public int getMappingCount() {
        return mappings.length;
    }

    public int getMacroCount() {
        return mappings[curMapping].getMacroCount();
    }

    public void setComponent(int index, byte code) {
        mappings[curMapping].setComponent(index, code);
    }

    public SerialPort getPort() {
        return port;
    }

    public void setPort(SerialPort other) {
        port = other;
    }

    public void setCurrentMapping(int mapping) {
        curMapping = mapping;
    }

    public int getCurrentMapping() {
        return curMapping;
    }

    // DeviceInterface implementations
    @Override
    public String fileWriter(int ID) {
        // [Previous iteration not really in line with module-firmware repo]
        // 1B per module + 1B per macro per trigger step per component (modules plus hardwired triggers)
        // + 1B per macro per playback step per component = 4 + 8*(4 + 10)*6 = 676B per mapping
        int count = 1;
        if (ID < 0) {
            count = DEFAULT_CONFIG_COUNT;
            ID = 0;
        } else if (ID >= mappings.length) {
            ID = curMapping;
        }
        byte[] data = new byte[
            (modules.length + mappings[ID].getMacroCount() *
            (mappings[ID].getMacro(0, 0).getTriggerLength() + mappings[ID].getMacro(0, 0).getPlaybackLength()) *
            (mappings[ID].getComponentCount() + 2)) * count
        ];
        int cur = 0;
        do { // Do once no matter what
            for (int i = 0; i < modules.length; i++) { // Copy component character codes
                data[cur] = mappings[ID].getComponent(i);
                cur++;
            }
            for (int i = 0; i < mappings[ID].getMacroCount(); i++) { // For every macro
                for (int j = 0; j < mappings[ID].getMacro(i, 0).getTriggerLength(); j++) { // For every trigger step
                    for (int k = 0; k < mappings[ID].getComponentCount() + 2; k++) { // For every component, copy a byte
                        data[cur] = mappings[ID].getMacro(i, k).getTrigger()[j];
                        cur++;
                    }
                }
            }
            for (int i = 0; i < mappings[ID].getMacroCount(); i++) { // For every macro
                for (int j = 0; j < mappings[ID].getMacro(i, 0).getPlaybackLength(); j++) { // For every playback step
                    for (int k = 0; k < mappings[ID].getComponentCount() + 2; k++) { // For every component, copy a byte
                        data[cur] = mappings[ID].getMacro(i, k).getPlayback()[j];
                        cur++;
                    }
                }
            }
            ID++;
        } while(count != 1 && ID < mappings.length); // Only repeat if provided ID was negative, and is incomplete
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

    @Override
    public int inputBuffer() {
        return port.bytesAvailable();
    }

    @Override
    public byte[] readHardware() {
        byte[] command = {'M','O','D','U','L','E','S',0}; // null-terminated "MODULES"
        USBController.initialize(command.length);
        boolean success = USBController.fillBuffer(command);
        success = success && USBController.sendBuffer();
        USBController.initialize(modules.length);
        success = success && USBController.readBytes(modules.length);
        if (!success) { // Quick and dirty for now
            System.out.println("Controller: readHardware() failed.");
        }
        return USBController.retrieveBuffer();
    }

    @Override
    public byte[] readMappings() {
        int size = (modules.length + mappings[curMapping].getMacroCount() *
            (mappings[curMapping].getMacro(0, 0).getTriggerLength() + mappings[curMapping].getMacro(0, 0).getPlaybackLength()) *
            (modules.length + 2)) * 3;
        byte[] command = {'C','O','N','F','I','G','S',0}; // null-terminated "CONFIGS"
        USBController.initialize(command.length);
        boolean success = USBController.fillBuffer(command);
        success = success && USBController.sendBuffer();
        USBController.initialize(size);
        success = success && USBController.readBytes(size);
        if (!success) { // Quick and dirty for now
            System.out.println("Controller: readMapping() failed.");
        }
        return USBController.retrieveBuffer();
    }

    @Override
    public byte[] readInput() {
        int size = (modules.length + 2) * 8;
        USBController.initialize(size);
        if (!USBController.readBytes(size)) { // Quick and dirty for now
            System.out.println("Controller: readInput() failed.");
        }
        return USBController.retrieveBuffer();
    }

    @Override
    public boolean sendConfigs() {
        byte[] command = {'S', 'A', 'V', 'E', 0}; // null-terminated "SAVE"
        USBController.initialize(command.length);
        boolean success = USBController.fillBuffer(command);
        success = success && USBController.sendBuffer();
        USBController.initialize( // For now: 3 mappings * (4 modules + 8 macros * (4 trigger length + 10 playback length) * (4 modules + 2 hardwired triggers))
            (modules.length + mappings[curMapping].getMacroCount() *
            (mappings[curMapping].getMacro(0, 0).getTriggerLength() + mappings[curMapping].getMacro(0, 0).getPlaybackLength()) *
            (modules.length + 2)) * 3
        );
        success = success && USBController.fillBuffer(fileWriter(-1).getBytes(StandardCharsets.ISO_8859_1));
        return success && USBController.sendBuffer();
    }

    @Override
    public boolean wipeMappings() {
        byte[] command = {'W', 'I', 'P', 'E', 0}; // null-terminated "WIPE"
        USBController.initialize(command.length);
        boolean success = USBController.fillBuffer(command);
        return success && USBController.sendBuffer();
    }

    @Override
    public byte[] runTests() {
        byte[] command = {'T','E','S','T',0}; // null-terminated "TEST"
        USBController.initialize(command.length);
        boolean success = USBController.fillBuffer(command);
        success = success && USBController.sendBuffer();
        USBController.initialize(1024);
        success = success && USBController.readBytes(1024);
        if (!success) { // Quick and dirty for now
            System.out.println("Controller: runTests() failed.");
        }
        return USBController.retrieveBuffer();
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
