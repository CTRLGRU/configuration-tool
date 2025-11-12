import javax.swing.*;
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
        openMenuItem.addActionListener(e -> System.out.println("Open combo pressed")); //Where you add the OPEN functionality
        fileMenu.add(openMenuItem);
        // SAVE MENU
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(e -> System.out.println("Save combo pressed")); // Save functionality, and so on. Look for the e-> listener.
        fileMenu.add(saveMenuItem);
        // SAVE AS MENU
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(e -> System.out.println("Save as menu button pressed"));
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
        programMenuItem.addActionListener(e -> System.out.println("Program combo pressed"));
        optionMenu.add(programMenuItem);
        // UPDATE
        JMenuItem updateMenuItem = new JMenuItem("Update");
        updateMenuItem.addActionListener(e -> System.out.println("Update combo pressed"));
        optionMenu.add(updateMenuItem);
        // PREFERENCES
        JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
        preferencesMenuItem.addActionListener(e -> System.out.println("Preferences combo pressed"));
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
        add(aboutPanel, BorderLayout.SOUTH);
        JButton aboutButton = new JButton("OK");
        //add action listener to close the window with the button
        aboutButton.addActionListener(e -> aboutFrame.dispose());
        contentPanel.add(aboutButton);
        contentPanel.add(aboutLabel, BorderLayout.CENTER);
        aboutFrame.add(contentPanel, BorderLayout.CENTER);
        aboutFrame.setSize(600, 400);
        aboutFrame.setVisible(true);
    }

    // settings menu
    public void preferencesWindow(){
        JFrame preferencesFrame = new JFrame("Preferences");
    }

    //check for updates window and subroutine
    public void updateWindow(){

    }

    //program controller option that upload button will summarize (this just allows more parameters)
    public void programWindow(){

    }

    //THE CONFIG FILE SAVING WINDOW!!!
    public void saveFileWindow(){

    }


}
