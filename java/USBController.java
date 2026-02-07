import com.fazecast.jSerialComm.*;

public class USBController {
    private static SerialPort port;

    public static boolean close() {
        return port.closePort();
    }

    public static SerialPort[] scan() {
        return SerialPort.getCommPorts();
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
            SerialPort.TIMEOUT_WRITE_BLOCKING,
            0,
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
            SerialPort.TIMEOUT_WRITE_BLOCKING,
            0,
            0
        );

        return port.openPort();
    }

    public static boolean writeBytes(byte[] data, int byteRate) {
        if (port == null || !port.isOpen()) {
            return false;
        }
        // When true, chunk size would be 0
        if (byteRate < 10) {
            byteRate = Math.min(port.getBaudRate() / 8, port.getDeviceWriteBufferSize() * 10);
        }
        int progress = 0;
        while (progress < data.length) {
            // Create a new chunk with remaining bytes or max bytes per 100 ms
            byte[] chunk = new byte[Math.min(byteRate / 10, data.length - progress)];
            System.arraycopy(data, progress, chunk, 0, chunk.length);
            if (!sendBytes(chunk)) {
                return false;
            }
            progress += chunk.length;
            try {
                // Pause for 100 ms, as non-Windows OSs only have decisecond granularity
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static boolean sendBytes(byte[] data) {
        if (port == null || !port.isOpen()) {
            return false;
        }
        return data.length == port.writeBytes(data, data.length);
    }
}