import javax.swing.*;
import java.awt.*;
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
        String currentDirectory = System.getProperty("user.dir"); //this may get squashed to one line in the future.
        contentPanel = new BackgroundPanel(currentDirectory+"/assets/bg.jpg");
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
        openMenuItem.addActionListener(e -> new openWindow()); //Where you add the OPEN functionality
        fileMenu.add(openMenuItem);
        // SAVE MENU
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(e -> new saveWindow("Save...")); // Save functionality, and so on. Look for the e-> listener.
        fileMenu.add(saveMenuItem);
        // SAVE AS MENU
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(e -> new saveWindow("Save As..."));
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
        programMenuItem.addActionListener(e -> new programWindow());
        optionMenu.add(programMenuItem);
        // UPDATE
        JMenuItem updateMenuItem = new JMenuItem("Update");
        updateMenuItem.addActionListener(e -> new updateWindow(version));
        optionMenu.add(updateMenuItem);
        // PREFERENCES
        JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
        preferencesMenuItem.addActionListener(e -> new preferencesWindow());
        optionMenu.add(preferencesMenuItem);
        // ABOUT
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> new aboutWindow(version));
        optionMenu.add(aboutMenuItem);
        //Once we have our panels set, we add a component listener
        contentPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                contentPanel.repaint();
            }
        });
        setJMenuBar(menuBar);
        String[] modulesD1 = {"Joystick", "DPad", "ABXY"};
        //these are the dropdown boxes for the modules. they should all have the same implementation, just differing locations. 
        JComboBox<String> dropdown1 = new JComboBox<>(modulesD1);
        contentPanel.add(dropdown1, BorderLayout.SOUTH);
        dropdown1.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String selection1 = (String) dropdown1.getSelectedItem();
               System.out.println(selection1);
               //Switch; Case; here.
           }
        });

        JComboBox<String> dropdown2 = new JComboBox<>(modulesD1);
        contentPanel.add(dropdown2, BorderLayout.SOUTH);
        dropdown2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection2 = (String) dropdown2.getSelectedItem();
                System.out.println(selection2);
                //Switch; Case; here.
            }
        });

        JComboBox<String> dropdown3 = new JComboBox<>(modulesD1);
        contentPanel.add(dropdown3, BorderLayout.SOUTH);
        dropdown3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection3 = (String) dropdown3.getSelectedItem();
                System.out.println(selection3);
                //Switch; Case; here.
            }
        });

        JComboBox<String> dropdown4 = new JComboBox<>(modulesD1);
        contentPanel.add(dropdown4, BorderLayout.SOUTH);
        dropdown4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection4 = (String) dropdown4.getSelectedItem();
                System.out.println(selection4);
                //Switch; Case; here.
            }
        });

        //We should set a default size to open at, I think 1200x800 makes sense in WxH
        setPreferredSize(new Dimension(1200, 800));
        setContentPane(contentPanel);
        pack();
        setVisible(true);
    }

    public void run(){
        //should be overridden
    }
}
