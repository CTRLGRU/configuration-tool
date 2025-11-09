import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import javax.swing.JButton;
import javax.swing.JFrame;

// Example Action Listener Override for what will soon be methods for context or button menus.
public class WindowFrameActionListener extends JFrame implements ActionListener {
    private JButton button;
    public WindowFrameActionListener() {
        button = new JButton("Click Me");
    }
    public void actionPerformed(ActionEvent e) {
        button.setText("Click Me");
    }

}
