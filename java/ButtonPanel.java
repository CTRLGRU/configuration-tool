import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonPanel extends JPanel {
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    public ButtonPanel(String topLabel, String leftLabel, String rightLabel, String bottomLabel) {
        super(new GridLayout(3, 3));
        setPreferredSize(new Dimension(200, 200));
        setBackground(new Color(0, 0, 0, 0));
        final JButton upBtn = new JButton(topLabel);
        upBtn.setContentAreaFilled(false);
        upBtn.setOpaque(true);
        upBtn.setBackground(Color.LIGHT_GRAY);
        upBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mousePressed(MouseEvent e) { // Arm on left-click only
                if (e.getButton() == MouseEvent.BUTTON1) {
                    upBtn.setBackground(new Color(0, 191, 31));
                    up = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release
                    upBtn.setBackground(Color.LIGHT_GRAY);
                    up = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Toggle on right-click release
                    up = !up;
                    upBtn.setBackground(up ? Color.LIGHT_GRAY : new Color(0, 191, 31));
                }
            }
        });
        final JButton leftBtn = new JButton(leftLabel);
        leftBtn.setContentAreaFilled(false);
        leftBtn.setOpaque(true);
        leftBtn.setBackground(Color.LIGHT_GRAY);
        leftBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mousePressed(MouseEvent e) { // Arm on left-click only
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftBtn.setBackground(new Color(0, 191, 31));
                    left = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release
                    leftBtn.setBackground(Color.LIGHT_GRAY);
                    left = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Toggle on right-click release
                    left = !left;
                    leftBtn.setBackground(left ? Color.LIGHT_GRAY : new Color(0, 191, 31));
                }
            }
        });
        final JButton rightBtn = new JButton(rightLabel);
        rightBtn.setContentAreaFilled(false);
        rightBtn.setOpaque(true);
        rightBtn.setBackground(Color.LIGHT_GRAY);
        rightBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mousePressed(MouseEvent e) { // Arm on left-click only
                if (e.getButton() == MouseEvent.BUTTON1) {
                    rightBtn.setBackground(new Color(0, 191, 31));
                    right = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release
                    rightBtn.setBackground(Color.LIGHT_GRAY);
                    right = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Toggle on right-click release
                    right = !right;
                    rightBtn.setBackground(right ? Color.LIGHT_GRAY : new Color(0, 191, 31));
                }
            }
        });
        final JButton downBtn = new JButton(bottomLabel);
        downBtn.setContentAreaFilled(false);
        downBtn.setOpaque(true);
        downBtn.setBackground(Color.LIGHT_GRAY);
        downBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mousePressed(MouseEvent e) { // Arm on left-click only
                if (e.getButton() == MouseEvent.BUTTON1) {
                    downBtn.setBackground(new Color(0, 191, 31));
                    down = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release
                    downBtn.setBackground(Color.LIGHT_GRAY);
                    down = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Toggle on right-click release
                    down = !down;
                    downBtn.setBackground(down ? Color.LIGHT_GRAY : new Color(0, 191, 31));
                }
            }
        });
        add(new JLabel());
        add(upBtn);
        add(new JLabel());
        add(leftBtn);
        add(new JLabel());
        add(rightBtn);
        add(new JLabel());
        add(downBtn);
    }

    public boolean getUp() {
        return up;
    }

    public boolean getLeft() {
        return left;
    }

    public boolean getRight() {
        return right;
    }

    public boolean getDown() {
        return down;
    }

    public byte[] getStatesAsOutput() {
        byte[] data = {0b01110000, 0, 0};
        if (up) {
            data[0] |= 0b00001000;
        }
        if (down) {
            data[0] |= 0b00000100;
        }
        if (left) {
            data[0] |= 0b00000010;
        }
        if (right) {
            data[0] |= 0b00000001;
        }
        return data;
    }
}
