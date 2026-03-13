import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class openWindow{
    public openWindow(WindowController parent, Controller controller) {
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
            byte[] data = parseConfig(file, controller);
            if (data == null) {
                System.out.println("openWindow: parseConfig() failed!");
                return;
            }
            for (int i = controller.getMappingCount() - 1; i >= 0; i--) {
                byte[] dataPiece = new byte[data.length / controller.getMappingCount()];
                System.arraycopy(data, data.length * i / controller.getMappingCount(), dataPiece, 0, dataPiece.length);
                Mapping mapping = Mapping.generateMapping(dataPiece, controller.getModuleCount(), controller.getMacroCount());
                controller.setMapping(
                    i,
                    mapping
                );
                parent.setMapping(mapping); // Mappings read backwards to preserve module dropdowns here
            }
        }
        else {
            JOptionPane.showMessageDialog(openFrame, "Cancelled Operation", "Open Window", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] parseConfig(File in, Controller controller) {
        try {
            InputStream is = new FileInputStream(in);
            Reader reader = new InputStreamReader(is, StandardCharsets.ISO_8859_1);

            byte[] data = new byte[
                (controller.getModuleCount() + controller.getMacroCount() *
                (controller.getMapping(0).getMacro(0, 0).getTriggerLength() + controller.getMapping(0).getMacro(0, 0).getPlaybackLength()) *
                (controller.getMapping(0).getComponentCount() + 2)) * controller.getMappingCount()
            ];

            int cur = 0;
            for (int r = reader.read(); r != -1 && cur < data.length; r = reader.read()) {
                data[cur] = (byte) r;
                cur++;
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
