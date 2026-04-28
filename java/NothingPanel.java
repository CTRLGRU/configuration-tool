import javax.swing.*;
import java.awt.*;

public class NothingPanel extends JPanel implements VirtualModuleInterface {
    public NothingPanel() {
        setPreferredSize(new Dimension(200, 200));
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }
    // VirtualModuleInterface implementations
    @Override
    public byte[] getStatesAsOutput() {
        return new byte[]{0, 0, 0};
    }

    @Override
    public void getStatesFromInput(byte[] data) {}
}
