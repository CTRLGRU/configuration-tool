import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class openWindow{
    public openWindow(WindowController parent) {
        JFrame openFrame = new JFrame("Open Window");
        openFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        openFrame.setLocationRelativeTo(null);
        openFrame.setSize(600, 400);
        openFrame.setResizable(true);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Open File...");
        int result = fileChooser.showOpenDialog(openFrame);
        if(result == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(openFrame, "Selected File" + file.getAbsolutePath(), "Open Window", JOptionPane.INFORMATION_MESSAGE);
            parent.setMapping(generateMapping(parseConfig(file)));
        }
        else {
            JOptionPane.showMessageDialog(openFrame, "Cancelled Operation", "Open Window", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] parseConfig(File in) {
        try {
            byte[] data;
            InputStream is = new FileInputStream(in);
            Reader reader = new InputStreamReader(is, StandardCharsets.ISO_8859_1);

            int components = reader.read();
            int playbacks = reader.read();
            if (components != -1 && playbacks != -1) {
                data = new byte[components * (1 + 14 * playbacks) + 2];
                data[0] = (byte) components;
                data[1] = (byte) playbacks;
            } else {
                return new byte[1];
            }

            int i = 2;
            for (int r = reader.read(); r != -1 && i < data.length; r = reader.read()) {
                data[i] = (byte) r;
                i++;
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[1];
        }
    }

    private Mapping generateMapping(byte[] data) {
        Mapping mapping = new Mapping(data[0], data[1]); // First 2 data bytes used
        int cur;
        for (cur = 2; cur < mapping.getComponentCount() + 2; cur++) { // 1 byte for each component starting at 2
            mapping.setComponent(cur - 2, data[cur]);
        }
        for (int i = 0; i < mapping.getPlaybackCount(); i++) { // Copies a 10*component byte array for each trigger
            System.arraycopy(data, cur, mapping.getTrigger(i), 0, mapping.getTrigger(i).length);
            cur += mapping.getTrigger(i).length;
        }
        for (int i = 0; i < mapping.getPlaybackCount(); i++) {
            System.arraycopy(data, cur, mapping.getPlayback(i), 0, mapping.getPlayback(i).length);
            cur += mapping.getPlayback(i).length;
        }
        return mapping;
    }
}
