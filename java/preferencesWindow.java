import javax.swing.*;
import java.awt.*;

public class preferencesWindow extends JFrame{
    private JPanel contentPanel;

    public preferencesWindow(){
        JFrame preferencesFrame = new JFrame("Preferences");
        preferencesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel contentPanel = new JPanel();
        JLabel selectedContentLabel;
        preferencesFrame.setLocationRelativeTo(null);
        preferencesFrame.setResizable(true);
        preferencesFrame.setSize(600, 400);
        //create menu bar panel
        JPanel settingsNavPanel = new JPanel();
        settingsNavPanel.setLayout(new GridLayout(0,1,0,5));
        settingsNavPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        //create menu items
        String[] navPanelItems = {"Theme", "Controller Settings", "File Settings", "Update Settings"};
        //for each item, add a navigation and a sub menu to update the panel window
        JButton ThemeButton = new JButton(navPanelItems[0]);
        ThemeButton.addActionListener(e -> System.out.println("Theme button pressed"));
        settingsNavPanel.add(ThemeButton);
        JButton ControllerSettingsButton = new JButton(navPanelItems[1]);
        ControllerSettingsButton.addActionListener(e -> System.out.println("Controller settings button pressed"));
        settingsNavPanel.add(ControllerSettingsButton);
        JButton FileSettingsButton = new JButton(navPanelItems[2]);
        FileSettingsButton.addActionListener(e -> System.out.println("File settings button pressed"));
        settingsNavPanel.add(FileSettingsButton);
        JButton UpdateSettingsButton = new JButton(navPanelItems[3]);
        UpdateSettingsButton.addActionListener(e -> System.out.println("Update settings button pressed"));
        settingsNavPanel.add(UpdateSettingsButton);
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        selectedContentLabel = new JLabel("Selected Content", SwingConstants.CENTER);
        selectedContentLabel.setFont(new Font(selectedContentLabel.getName(), Font.PLAIN, 20));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(150);
        add(splitPane, BorderLayout.WEST);
        contentPanel.add(selectedContentLabel, BorderLayout.CENTER);
        contentPanel.add(settingsNavPanel, BorderLayout.WEST);
        contentPanel.setVisible(true);
        preferencesFrame.add(contentPanel, BorderLayout.CENTER);
        preferencesFrame.setVisible(true);
    }
}
