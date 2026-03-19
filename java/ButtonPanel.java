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
        upBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mouseClicked(MouseEvent e) { // Arm on left-click or toggle on right-click
                if (e.getButton() == MouseEvent.BUTTON1) {
                    upBtn.getModel().setArmed(true);
                    up = true;
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    upBtn.getModel().setArmed(!upBtn.getModel().isArmed());
                    up = !up;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release only
                    upBtn.getModel().setArmed(false);
                    up = false;
                }
            }
        });
        final JButton leftBtn = new JButton(leftLabel);
        leftBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mouseClicked(MouseEvent e) { // Arm on left-click or toggle on right-click
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftBtn.getModel().setArmed(true);
                    left = true;
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    leftBtn.getModel().setArmed(!leftBtn.getModel().isArmed());
                    left = !left;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release only
                    leftBtn.getModel().setArmed(false);
                    left = false;
                }
            }
        });
        final JButton rightBtn = new JButton(rightLabel);
        rightBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mouseClicked(MouseEvent e) { // Arm on left-click or toggle on right-click
                if (e.getButton() == MouseEvent.BUTTON1) {
                    rightBtn.getModel().setArmed(true);
                    right = true;
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    rightBtn.getModel().setArmed(!rightBtn.getModel().isArmed());
                    right = !right;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release only
                    rightBtn.getModel().setArmed(false);
                    right = false;
                }
            }
        });
        final JButton downBtn = new JButton(bottomLabel);
        downBtn.addMouseListener(new MouseAdapter() {
            // MouseListener events
            @Override
            public void mouseClicked(MouseEvent e) { // Arm on left-click or toggle on right-click
                if (e.getButton() == MouseEvent.BUTTON1) {
                    downBtn.getModel().setArmed(true);
                    down = true;
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    downBtn.getModel().setArmed(!downBtn.getModel().isArmed());
                    down = !down;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Disarm on left-click release only
                    downBtn.getModel().setArmed(false);
                    down = false;
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

    public byte getStatesAsOutput() {
        byte value = 0;
        if (up) {
            value |= 0b00001000;
        }
        if (down) {
            value |= 0b00000100;
        }
        if (left) {
            value |= 0b00000010;
        }
        if (right) {
            value |= 0b00000001;
        }
        return value;
    }
}
