import javax.swing.*;
import java.awt.*;

public class updateWindow extends JFrame{
    private JFrame contentPanel;

    public updateWindow(String version) {
        /*For this there should be three steps: first open a window and let the user know it's checking for updates
         * Then it should actually do the logic of if no updates available, then display so with text, and an OK box
         * IF THERE IS, then it should transition the box to "an update is available (current ver. xx.xx -> new ver xx.xx)
         * and then have an "ok" or "cancel" with "update"
         * */
        JFrame updateFrame = new JFrame("Update Window");
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.setLocationRelativeTo(null);
        updateFrame.setResizable(false);
        updateFrame.setLayout(new BorderLayout());
        JPanel updatePanel = new JPanel();
        updatePanel.setBackground(Color.LIGHT_GRAY);
        //
        // THIS SHOULD USE A NET CHECK TO A KNOWN URL; THEN DO SOME LOGIC!
        //
        updateFrame.add(updatePanel, BorderLayout.CENTER);
        //the below statement should go inside of one of the case statements for if(updateAvail = true); else{...
        JOptionPane.showMessageDialog(updateFrame, "There are no updates available at this time. Current version is "+version+".", "Update Window", JOptionPane.INFORMATION_MESSAGE);
        updateFrame.setVisible(false);// originally true
    }
}
