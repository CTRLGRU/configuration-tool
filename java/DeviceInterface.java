// DeviceInterface class - Implementations specific to the entire PHYSICAL CONTROLLER that are not MODULE SPECIFIC.
// for example, an object encapsulating all 4 modules for controller and view interaction, or java.FileIO
public interface DeviceInterface {
    // fileIO wrapping function for prettyifying stuff- particularly for whatever format the RP2050 accepts.
    public String fileWriter(int ID);

    // Device connect function
    public boolean connect(int baud);

    // Device disconnect function
    public boolean disconnect();

    // Reads data sent by the device to identify modules
    public byte[] readHardware();

    // Reads input buffer sent by the device to verify functioning
    public byte[] readInput();

    // Sends module, mapping, and configuration data to the device
    public boolean sendConfig(byte[] data);
}