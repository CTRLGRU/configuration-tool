// DeviceInterface class - Implementations specific to the entire PHYSICAL CONTROLLER that are not MODULE SPECIFIC.
// for example, an object encapsulating all 4 modules for controller and view interaction, or java.FileIO
public interface DeviceInterface {
    // connect function

    // fileIO wrapping function for prettyifying stuff- particularly for whatever format the RP2050 accepts.
    public String fileWriter(int ID);
}
