import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
// Window Class - this is where making variable instantiations of windows should be allowed to be created!

public class WindowController extends JFrame implements ViewInterface, Runnable{
    private JPanel contentPanel;
    private Controller Pcontroller;
    private JComboBox<String> dropdown1;
    private JComboBox<String> dropdown2;
    private JComboBox<String> dropdown3;
    private JComboBox<String> dropdown4;
    public String version = "0.1.2a";

    public WindowController(Controller controller){
        super("Controller Window");
        this.Pcontroller = controller;
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
        openMenuItem.addActionListener(e -> new openWindow(this)); //Where you add the OPEN functionality
        fileMenu.add(openMenuItem);
        // SAVE MENU
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(e -> new saveWindow("Save...", Pcontroller)); // Save functionality, and so on. Look for the e-> listener.
        fileMenu.add(saveMenuItem);
        // SAVE AS MENU
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(e -> new saveWindow("Save As...", Pcontroller));
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
        String[] modulesD1 = {" ","Joystick", "DPad", "ABXY"};
        //these are the dropdown boxes for the modules. they should all have the same implementation, just differing locations. 
        dropdown1 = new JComboBox<String>(modulesD1);
        contentPanel.add(dropdown1, BorderLayout.SOUTH);
        dropdown1.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String selection1 = (String) dropdown1.getSelectedItem();
               try {
                   switch (selection1) {
                       case "Joystick":
                           Pcontroller.setModule1(2,0,"Joystick");
                           break;
                       case "DPad":
                           Pcontroller.setModule1(0,4,"DPad");
                           break;
                       case "ABXY":
                           Pcontroller.setModule1(0,4,"ABXY");
                           break;
                       case null:
                           throw new NullPointerException("NULL ARGUMENT IN DROPDOWN MENU");
                           //break;
                           //apparently you can't get here?
                       default:
                           JOptionPane.showMessageDialog(controlPanel,"Invalid Selection", "Error", JOptionPane.ERROR_MESSAGE);
                           break;
                   }
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
        });

        dropdown2 = new JComboBox<String>(modulesD1);
        contentPanel.add(dropdown2, BorderLayout.SOUTH);
        dropdown2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection2 = (String) dropdown2.getSelectedItem();
                try {
                    switch (selection2) {
                        case "Joystick":
                            Pcontroller.setModule2(2,0,"Joystick");
                            break;
                        case "DPad":
                            Pcontroller.setModule2(0,4,"DPad");
                            break;
                        case "ABXY":
                            Pcontroller.setModule2(0,4,"ABXY");
                            break;
                        case null:
                            throw new NullPointerException("NULL ARGUMENT IN DROPDOWN MENU");
                            //break;
                        default:
                            JOptionPane.showMessageDialog(controlPanel,"Invalid Selection", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        dropdown3 = new JComboBox<String>(modulesD1);
        contentPanel.add(dropdown3, BorderLayout.SOUTH);
        dropdown3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection3 = (String) dropdown3.getSelectedItem();
                try {
                    switch (selection3) {
                        case "Joystick":
                            Pcontroller.setModule3(2,0,"Joystick");
                            break;
                        case "DPad":
                            Pcontroller.setModule3(0,4,"DPad");
                            break;
                        case "ABXY":
                            Pcontroller.setModule3(0,4,"ABXY");
                            break;
                        case null:
                            throw new NullPointerException("NULL ARGUMENT IN DROPDOWN MENU");
                            //break;
                        default:
                            JOptionPane.showMessageDialog(controlPanel,"Invalid Selection", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        dropdown4 = new JComboBox<String>(modulesD1);
        contentPanel.add(dropdown4, BorderLayout.SOUTH);
        dropdown4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection4 = (String) dropdown4.getSelectedItem();
                try {
                    switch (selection4) {
                        case "Joystick":
                            Pcontroller.setModule4(2,0,"Joystick");
                            break;
                        case "DPad":
                            Pcontroller.setModule4(0,4,"DPad");
                            break;
                        case "ABXY":
                            Pcontroller.setModule4(0,4,"ABXY");
                            break;
                        case null:
                            throw new NullPointerException("NULL ARGUMENT IN DROPDOWN MENU");
                            //break;
                        default:
                            JOptionPane.showMessageDialog(controlPanel,"Invalid Selection", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //leaving this here for testing!! REMOVE IN PRODUCTION!!
                //System.out.println(Arrays.toString(Pcontroller.getModulesFileWriter()));
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

    public void setModules(char[] modules, int[][] data) {
        String name = null;
        for (int i = 0; i < 4; i++) {
            switch (modules[i]) {
                case 'J':
                    name = "Joystick";
                    break;
                case 'B':
                    name = "ABXY";
                    break;
                default:
                    name = " ";
            }
            switch (data[i][2]) {
                case 1:
                    Pcontroller.setModule1(data[i][1], data[i][0], name);
                    dropdown1.setSelectedItem(name);
                    break;
                case 2:
                    Pcontroller.setModule2(data[i][1], data[i][0], name);
                    dropdown2.setSelectedItem(name);
                    break;
                case 3:
                    Pcontroller.setModule3(data[i][1], data[i][0], name);
                    dropdown3.setSelectedItem(name);
                    break;
                case 4:
                    Pcontroller.setModule4(data[i][1], data[i][0], name);
                    dropdown4.setSelectedItem(name);
                    break;
                default:
            }
        }
    }
}
