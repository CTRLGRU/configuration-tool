// DeviceInterface class - Implementations specific to the entire PHYSICAL CONTROLLER that are not MODULE SPECIFIC.
// for example, an object encapsulating all 4 modules for controller and view interaction, or java.FileIO
public interface DeviceInterface {
    // fileIO wrapping function for prettyifying stuff- particularly for whatever format the RP2050 accepts.
    public String fileWriter(int ID);

    // Device connect function
    public boolean connect(int baud);

    // Device disconnect function
    public boolean disconnect();

    // Checks how many bytes are ready to be read
    public int inputBuffer();

    // Reads data sent by the device to identify modules
    public byte[] readHardware();

    // Reads data sent by the device in the form of configurations
    public byte[] readMappings();

    // Reads input buffer sent by the device to verify functioning
    public byte[] readInput();

    // Sends module, mapping, and configuration data to the device
    public boolean sendConfigs();

    // Wipes configuration data from the controller
    public boolean wipeMappings();

    // Runs hardware tests and displays output
    public byte[] runTests();

    // Prompts the controller to recalibrate joysticks
    public boolean recalibrate();

    // Set the controller to send raw input buffer data
    public boolean rawInputMode();

    // Set the controller to send USB gamepad data
    public boolean usbInputMode();
}