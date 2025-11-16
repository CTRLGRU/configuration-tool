import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
// Window Class - this is where making variable instantiations of windows should be allowed to be created!

public class Window extends JFrame implements ViewInterface, Runnable{
    private JPanel contentPanel;
    public String version = "0.1.0a";

    public Window(){
        super("Controller Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        //Panel inside the window
        contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
        //This is where components of the panel go, it should be a sub-panel instantiation of it
        //Below is the main controls (middle)
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Test Controls"));
        add(controlPanel, BorderLayout.NORTH);
        //menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);
        JMenu optionMenu = new JMenu("Options");
        optionMenu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(optionMenu);
        //menu bar sub items
        // FILE MENU SUB ITEMS
        // NEW MENU
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newMenuItem.addActionListener(e -> System.out.println("New combo pressed")); //Where you add the NEW functionality
        fileMenu.add(newMenuItem);
        // OPEN MENU
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.addActionListener(e -> openWindow()); //Where you add the OPEN functionality
        fileMenu.add(openMenuItem);
        // SAVE MENU
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(e -> saveFileWindow("Save...")); // Save functionality, and so on. Look for the e-> listener.
        fileMenu.add(saveMenuItem);
        // SAVE AS MENU
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(e -> saveFileWindow("Save As..."));
        fileMenu.add(saveAsMenuItem);
        // EXIT MENU
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        //
        // EDIT MENU SUB ITEMS
        // UNDO
        JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoMenuItem.addActionListener(e -> System.out.println("Undo combo pressed"));
        editMenu.add(undoMenuItem);
        // REDO
        JMenuItem redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        redoMenuItem.addActionListener(e -> System.out.println("Redo combo pressed"));
        editMenu.add(redoMenuItem);
        // In the original snapshots there is a LOAD feature here for loading unsupported modules. I'm not adding that!
        // GLHF future programmer.
        //
        // OPTIONS MENU SUB ITEMS
        // PROGRAM
        JMenuItem programMenuItem = new JMenuItem("Program");
        programMenuItem.addActionListener(e -> programWindow());
        optionMenu.add(programMenuItem);
        // UPDATE
        JMenuItem updateMenuItem = new JMenuItem("Update");
        updateMenuItem.addActionListener(e -> updateWindow());
        optionMenu.add(updateMenuItem);
        // PREFERENCES
        JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
        preferencesMenuItem.addActionListener(e -> preferencesWindow());
        optionMenu.add(preferencesMenuItem);
        // ABOUT
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> aboutWindow());
        optionMenu.add(aboutMenuItem);
        //Once we have our panels set, we add a component listener
        contentPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                contentPanel.repaint();
            }
        });
        //We should set a default size to open at, I think 1200x800 makes sense in WxH
        setPreferredSize(new Dimension(1200, 800));
        setJMenuBar(menuBar);
        pack();
        setVisible(true);
    }

    public void run(){
        //should be overridden
    }

    public void aboutWindow(){
        JFrame aboutFrame = new JFrame("About");
        aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        aboutFrame.setLocationRelativeTo(null);
        aboutFrame.setResizable(false);
        aboutFrame.setLayout(new BorderLayout());
        JPanel aboutPanel = new JPanel();
        aboutPanel.setBackground(Color.LIGHT_GRAY);
        //hey this about page! please keep it for us :)
        JLabel aboutLabel = new JLabel("<html><div style='text-align: center;'>FMC: Software version "+version+"<br>Licensed under GPLv3 All Rights Reserved<br>Developed by Allen \"LF\"  B., Lloyd \"Koda\" C., Damian C.</div></html>", SwingConstants.CENTER);
        aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutLabel.setFont(new Font(aboutLabel.getName(), Font.PLAIN, 20));
        //add(aboutPanel, BorderLayout.CENTER);
        JButton aboutButton = new JButton("OK");
        //add action listener to close the window with the button
        aboutButton.addActionListener(e -> aboutFrame.dispose());
        contentPanel.add(aboutLabel, BorderLayout.NORTH);
        contentPanel.add(aboutButton, BorderLayout.CENTER);
        aboutFrame.add(contentPanel, BorderLayout.CENTER);
        aboutFrame.setSize(600, 400);
        aboutFrame.setVisible(true);
    }

    // settings menu
    public void preferencesWindow(){
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

    //check for updates window and subroutine
    public void updateWindow(){
        /*For this there should be three steps: first open a window and let the user know it's checking for updates
        * Then it should actually do the logic of if no updates available, then display so with text, and an OK box
        * IF THERE IS, then it should transition the box to "an update is available (current ver. xx.xx -> new ver xx.xx)
        * and then have an "ok" or "cancel" with "update"
        * */
        JFrame updateFrame = new JFrame("Update Window");
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.setLocationRelativeTo(null);
        updateFrame.setResizable(false);
        updateFrame.setSize(400, 200);
        updateFrame.setLayout(new BorderLayout());
        JPanel updatePanel = new JPanel();
        updatePanel.setBackground(Color.LIGHT_GRAY);
        //here is where you do this process logic!
        JLabel updateLabelInitial = new JLabel("<html><div style='text-align: center;'>No updates available</div></html>", SwingConstants.CENTER);
        updateLabelInitial.setHorizontalAlignment(SwingConstants.CENTER);
        updateLabelInitial.setFont(new Font(updateLabelInitial.getName(), Font.PLAIN, 30));
        add(updatePanel, BorderLayout.NORTH);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> updateFrame.dispose());
        updatePanel.add(okButton, BorderLayout.SOUTH);
        updatePanel.add(updateLabelInitial, BorderLayout.NORTH);
        updateFrame.add(updatePanel, BorderLayout.CENTER);
        //updateFrame.add(contentPanel, BorderLayout.CENTER);
        //turns out this does it infinitely better.
        JOptionPane.showMessageDialog(updateFrame, "There are no updates available at this time. Current version is "+version+".", "Update Window", JOptionPane.INFORMATION_MESSAGE);
        updateFrame.setVisible(false);// originally true
    }

    //program controller option that upload button will summarize (this just allows more parameters)
    public void programWindow(){
        JFrame programFrame = new JFrame("Program...");
        programFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        programFrame.setLocationRelativeTo(null);
        programFrame.setResizable(true);
        programFrame.setSize(600, 400);
        JPanel contentPanel = new JPanel();
        JLabel programLabel = new JLabel("Program options n stuff", SwingConstants.CENTER);
        programLabel.setFont(new Font(programLabel.getName(), Font.PLAIN, 20));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> programFrame.dispose());
        contentPanel.add(programLabel, BorderLayout.NORTH);
        contentPanel.add(okButton, BorderLayout.SOUTH);
        programFrame.add(contentPanel, BorderLayout.CENTER);
        programFrame.setVisible(true);
    }

    //THE CONFIG FILE SAVING WINDOW!!!
    //I should mention this SHOULD be a separate class, just like all these windows.
    //I've decided to pass in an argument to make this easier to manipulate per use
    public void saveFileWindow(String windowTextArg){
        JFrame saveFileFrame = new JFrame(windowTextArg);
        saveFileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        saveFileFrame.setLocationRelativeTo(null);
        saveFileFrame.setSize(600, 400);
        saveFileFrame.setResizable(true);
        //the actual logic for the save window
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save File...");
        fileChooser.setSelectedFile(new File("untitled.fmc"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("FMC Controller Format files", "*.fmc");
        fileChooser.setFileFilter(filter);
        int userSelection = fileChooser.showSaveDialog(saveFileFrame);
        if(userSelection == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile(); //THIS SHOULD BE PASSED IN AND CALL UPON THE FILE CONSTRUCTION METHOD OF COMPONENT ABClass
            String filePath = file.getAbsolutePath();
            if(!filePath.toLowerCase().endsWith(".fmc")){
                filePath = filePath + ".fmc";
                file = new File(filePath);
            }
            try(FileWriter writer = new FileWriter(file)){
                writer.write("Test Formatting construct, content should be passed in here from Component");
                JOptionPane.showMessageDialog(saveFileFrame, "File saved successfully to" + filePath);
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(saveFileFrame, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace(); //ideally this should be sent to a NEW file in a directory of the program! later todo!!!
            }
        }
    }

    public void openWindow(){
        JFrame openFrame = new JFrame("Open Window");
        openFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        openFrame.setLocationRelativeTo(null);
        openFrame.setSize(600, 400);
        openFrame.setResizable(true);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Open File...");
        int result = fileChooser.showOpenDialog(openFrame); //hopefully you work
        if(result == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(openFrame, "Selected File" + file.getAbsolutePath(), "Open Window", JOptionPane.INFORMATION_MESSAGE);
            //Implement the file loading logic here, or call to it with the file object.
        }
        else {
            JOptionPane.showMessageDialog(openFrame, "Cancelled Operation", "Open Window", JOptionPane.ERROR_MESSAGE);
        }
    }
}
