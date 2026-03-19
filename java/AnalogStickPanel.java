import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnalogStickPanel extends JPanel {
    private int x;
    private int y;
    private final int r;

    public AnalogStickPanel(int radius) {
        x = 0;
        y = 0;
        r = radius;
        setFocusable(true);
        setPreferredSize(new Dimension(200, 200));
        setBackground(new Color(0, 0, 0, 0));
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
                }
            }

            // MouseMotionListener events
            @Override
            public void mouseDragged(MouseEvent e) { // Left-click dragging sets it
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setStick(e.getX(), e.getY());
                }
            }
        };
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    public int getX() {
        return x;
    }

    public int getXAsOutput(byte bits) {
        if (bits <= 0) {
            return 0;
        }
        int max = (int) (Math.pow(2, bits)) - 1;
        return (int) (max * x / getMaxDistance());
    }

    public int getY() {
        return y;
    }

    public int getYAsOutput(byte bits) {
        if (bits <= 0) {
            return 0;
        }
        int max = (int) (Math.pow(2, bits)) - 1;
        return (int) (max * y / getMaxDistance());
    }

    public double getDistance() {
        int dx = x - getWidth() / 2;
        int dy = y - getHeight() / 2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getMaxDistance() {
        return getWidth() - r; // All the way to one side
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

    // JPanel re-implementations
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(
            getWidth() / 2 + x - r,
            getHeight() / 2 + y - r,
            r * 2,
            r * 2
        );
    }
}