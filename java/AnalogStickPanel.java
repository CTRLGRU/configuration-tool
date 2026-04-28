import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnalogStickPanel extends JPanel implements VirtualModuleInterface {
    private int x = 0;
    private int y = 0;
    private boolean btn = false;
    private Color color = Color.LIGHT_GRAY;
    private final int r;

    public AnalogStickPanel(int radius) {
        r = radius;
        setFocusable(true);
        setPreferredSize(new Dimension(200, 200));
        setOpaque(false);
        MouseAdapter mouse = new MouseAdapter() {
            // MouseListener events
            @Override
            public void mouseClicked(MouseEvent e) { // Any click sets it
                setStick(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) { // Releasing left-click unsets it
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setStick(getWidth() / 2, getHeight() / 2);
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Right-click toggles button
                    btn = !btn;
                    color = btn ? new Color(0, 191, 31) : Color.LIGHT_GRAY;
                }
            }

            // MouseMotionListener events
            @Override
            public void mouseDragged(MouseEvent e) { // Left-click dragging sets it
                setStick(e.getX(), e.getY());
            }
        };
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    public int getStickX() {
        return x;
    }

    public int getStickY() {
        return y;
    }

    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    public double getMaxDistance() {
        return Math.min(getWidth(), getHeight()) / 2.0 - r; // All the way to one side
    }

    public void setStick(int xStick, int yStick) {
        x = xStick - getWidth() / 2; // Negative to the left, positive to the right
        y = getHeight() / 2 - yStick; // Negative to the bottom, positive to the top
        double scale = getMaxDistance() / getDistance();
        if (scale < 1) { // Scale if beyond regular bounds
            x = (int) (x * scale);
            y = (int) (y * scale);
        }
        repaint();
    }

    // VirtualModuleInterface implementations
    @Override
    public byte[] getStatesAsOutput() {
        byte[] data = {0, 0, 0};
        if (btn) {
            data[0] |= 0b00000001;
        }
        data[0] |= (byte) ((byte) (128.0 * x / getMaxDistance()) & 0b11111110);
        data[1] |= (byte) ((byte) (128.0 * y / getMaxDistance()) & 0b11111110);
        return data;
    }

    @Override
    public void getStatesFromInput(byte[] data) {
        btn = (data[0] & 1) == 1;
        if (btn) {
            color = new Color(0, 191, 31);
        }
        x = (int) ((double) (data[0] & 0b11111110) / 127.0 * getMaxDistance());
        y = (int) ((double) (data[1] & 0b11111110) / 127.0 * getMaxDistance());
        repaint();
    }

    // JPanel implementations
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(color);
        g.fillOval(
            getWidth() / 2 + x - r,
            getHeight() / 2 - y - r,
            r * 2,
            r * 2
        );
    }
}