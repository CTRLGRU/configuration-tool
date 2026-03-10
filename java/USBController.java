import com.fazecast.jSerialComm.*;

public abstract class USBController {
    private static SerialPort port;
    private static byte[] buffer;

    public static boolean close() {
        return port.closePort();
    }

    public static void initialize(int bufferSize, String name, int baud, int stopBits, int parity) {
        buffer = new byte[bufferSize];
        USBController.open(name, baud, stopBits, parity);
    }

    public static void initialize(int bufferSize, String name, int baud) {
        buffer = new byte[bufferSize];
        USBController.open(name, baud);
    }

    public static void initialize(int bufferSize) {
        buffer = new byte[bufferSize];
    }

    public static boolean open(String name, int baud, int stopBits, int parity) {
        port = SerialPort.getCommPort(name);
        port.setComPortParameters(
            baud,
            8,
            stopBits,
            parity
        );
        port.setComPortTimeouts(
            SerialPort.TIMEOUT_WRITE_BLOCKING | SerialPort.TIMEOUT_READ_BLOCKING,
            1000,
            0
        );

        return port.openPort();
    }

    public static boolean open(String name, int baud) {
        port = SerialPort.getCommPort(name);
        port.setComPortParameters(
            baud,
            8,
            SerialPort.ONE_STOP_BIT,
            SerialPort.EVEN_PARITY
        );
        port.setComPortTimeouts(
            SerialPort.TIMEOUT_WRITE_BLOCKING | SerialPort.TIMEOUT_READ_BLOCKING,
            1000,
            0
        );

        return port.openPort();
    }

    public static boolean readBytes(int bytes) {
        return bytes == port.readBytes(buffer, Math.min(bytes, buffer.length));
    }

    public static SerialPort[] scan() {
        return SerialPort.getCommPorts();
    }

    public static boolean sendBuffer() {
        if (port == null || !port.isOpen()) {
            return false;
        }
        return buffer.length == port.writeBytes(buffer, buffer.length);
    }
}