import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

// Window Class - this is where making variable instantiations of windows should be allowed to be created!
public class WindowController extends JFrame implements ViewInterface, Runnable{
    private JPanel contentPanel = new BackgroundPanel(System.getProperty("user.dir")+"/assets/bg.jpg");
    private Controller Pcontroller;
    private List<JComboBox<String>> dropdowns;
    private List<JTextArea> inputs;
    public String version = "0.2.0";

    public WindowController(Controller controller){
        super("Controller Window");
        Pcontroller = controller;
        dropdowns = new ArrayList<JComboBox<String>>(controller.getModuleCount());
        inputs = new ArrayList<JTextArea>(controller.getModuleCount());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Panel inside the window
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridwidth = 1;
        g.gridheight = 1;
        g.weightx = 1;
        g.weighty = 1;
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
        JMenu deviceMenu = new JMenu("Devices");
        optionMenu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(deviceMenu);
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
        openMenuItem.addActionListener(e -> new openWindow(this, Pcontroller)); //Where you add the OPEN functionality
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
        programMenuItem.addActionListener(e -> new programWindow(this, Pcontroller));
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
        // DEVICE MENU SUB ITEMS
        JMenuItem simToggle = new JMenuItem("Simulation");
        simToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pcontroller.disconnect();
                Pcontroller.setPort(null); // Internal behavior changes when Controller.port == null
            }
        });
        deviceMenu.add(simToggle);
        SerialPort[] ports = USBController.scan();
        for (SerialPort port : ports) {
            JMenuItem item = new JMenuItem(port.getDescriptivePortName());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Pcontroller.disconnect();
                    Pcontroller.setPort(port);
                    Pcontroller.connect(9600); // Can probably be higher, i.e. 115.2k
                }
            });
            deviceMenu.add(item);
        }
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
        for (int i = 0; i < controller.getModuleCount(); i++) {
            final int iCopy = i;
            JComboBox<String> dropdown = new JComboBox<String>(modulesD1);
            dropdown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch ((String) dropdowns.get(iCopy).getSelectedItem()) {
                        case "Joystick":
                            Pcontroller.setComponent(iCopy, (byte) 'J');
                            Pcontroller.setModule(iCopy,2,1,"Joystick",
                                "Joystick with a press button and two axes of analog movement."
                            );
                            break;
                        case "DPad":
                            Pcontroller.setComponent(iCopy, (byte) 'B');
                            Pcontroller.setModule(iCopy,0,4,"DPad",
                                "Four directional buttons intended to act as up or down and/or left or right."
                            );
                            break;
                        case "ABXY":
                            Pcontroller.setComponent(iCopy, (byte) 'B');
                            Pcontroller.setModule(iCopy,0,4,"ABXY",
                                "Four general-purpose buttons intended to act as A, B, X, and/or Y."
                            );
                            break;
                        case "":
                            Pcontroller.setComponent(iCopy, (byte) 'X');
                            Pcontroller.setModule(iCopy, 0, 0, "",
                                "Unset or empty module."
                            );
                            break;
                        default:
                            JOptionPane.showMessageDialog(controlPanel,"Invalid Selection", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = i;
            g.gridy = 1;
            contentPanel.add(dropdown, g);
            dropdowns.add(dropdown);
            g.gridx = 2 * i;
            g.gridy = 0;
            contentPanel.add(new JLabel("Module " + (i + 1) + ":"));
            g.fill = GridBagConstraints.BOTH;
            g.gridx = 2 * i + 1;
            JTextArea input = new JTextArea("" + i);
            inputs.add(input);
            contentPanel.add(input, g);
        }

        // Below code block assumes default 4-module setup with 2 hardwired triggers
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 8;
        g.gridy = 0;
        contentPanel.add(new JLabel("Trigger 1:"));
        g.fill = GridBagConstraints.BOTH;
        g.gridx = 9;
        inputs.add(new JTextArea("T1"));
        contentPanel.add(inputs.getLast(), g);
        g.gridx = 10;
        contentPanel.add(new JLabel("Trigger 2:"));
        g.gridx = 11;
        inputs.add(new JTextArea("T2"));
        contentPanel.add(inputs.getLast(), g);
        List<JPanel> modules = setupModules(g);

        //We should set a default size to open at, I think 1200x800 makes sense in WxH
        setPreferredSize(new Dimension(1200, 800));
        setContentPane(contentPanel);
        pack();
        setVisible(true);
    }

    private List<JPanel> setupModules(GridBagConstraints g) { // Defaults to the 4-module setup for a 1200x800 window
        List<JPanel> modules = new ArrayList<JPanel>(4);
        JPanel module1 = new JPanel(new GridLayout(3, 3));
        module1.add(new JLabel());
        module1.add(new JButton("Up"));
        module1.add(new JLabel());
        module1.add(new JButton("Left"));
        module1.add(new JLabel());
        module1.add(new JButton("Right"));
        module1.add(new JLabel());
        module1.add(new JButton("Down"));
        AnalogStickPanel module2 = new AnalogStickPanel(25);
        AnalogStickPanel module3 = new AnalogStickPanel(25);
        JPanel module4 = new JPanel(new GridLayout(3, 3));
        module4.add(new JLabel());
        module4.add(new JButton("Y"));
        module4.add(new JLabel());
        module4.add(new JButton("X"));
        module4.add(new JLabel());
        module4.add(new JButton("B"));
        module4.add(new JLabel());
        module4.add(new JButton("A"));
        g.fill = GridBagConstraints.BOTH;
        g.gridwidth = 2;
        g.gridheight = 2;
        g.gridx = 2;
        g.gridy = 2;
        contentPanel.add(module1, g);
        modules.add(module1);
        g.gridx = 8;
        contentPanel.add(module4, g);
        modules.add(module4);
        g.gridx = 3;
        g.gridy = 4;
        contentPanel.add(module2, g);
        modules.add(module2);
        g.gridx = 7;
        contentPanel.add(module3, g);
        modules.add(module3);
        g.gridwidth = 1;
        g.gridheight = 1;
        g.gridx = 11;
        g.gridy = 7;
        contentPanel.add(new JLabel(), g);
        return modules;
    }

    /*private void updateModules(List<JPanel> modules, byte[] components) {
        for (int i = 0; i < modules.size(); i++) {
            switch(components[i]) {
                case 'J':
                    modules.set(i, new AnalogStickPanel(12));
                    break;
                case 'B':
                    JPanel module = new JPanel(new GridBagLayout());
                    GridBagConstraints g = new GridBagConstraints();
                    g.weightx = 1;
                    g.weighty = 1;
                    g.gridx = 1;
                    g.gridy = 0;
                    module.add(new JToggleButton("Up"), g);
                    g.gridy = 2;
                    module.add(new JToggleButton("Down"), g);
                    g.gridx = 0;
                    g.gridy = 1;
                    module.add(new JToggleButton("Left"), g);
                    g.gridx = 2;
                    module.add(new JToggleButton("Right"), g);
                    modules.set(i, module);
            }
        }
    } // GUI is too finincky for me to update these on the fly, I have found */

    public void setMapping(int ID, Mapping mapping) {
        int current = Pcontroller.getCurrentMapping();
        Pcontroller.setCurrentMapping(ID);
        for (int i = 0; i < Pcontroller.getModuleCount(); i++) {
            switch(mapping.getComponent(i)) {
                case 'J':
                    Pcontroller.setComponent(i, (byte) 'J');
                    Pcontroller.setModule(i, 2, 1, "Joystick",
                        "Joystick with a press button and two axes of analog movement."
                    );
                    dropdowns.get(i).setSelectedItem("Joystick");
                    break;
                case 'B':
                    Pcontroller.setComponent(i, (byte) 'B');
                    Pcontroller.setModule(i, 0, 4, "ABXY",
                        "Four general-purpose buttons intended to act as A, B, X, and/or Y."
                    );
                    dropdowns.get(i).setSelectedItem("ABXY");
                    break;
                case 'X':
                    Pcontroller.setComponent(i, (byte) 'X');
                    Pcontroller.setModule(i, 0, 0, "",
                        "Unset or empty module."
                    );
                default:
                    JOptionPane.showMessageDialog(contentPanel,"Invalid Selection", "Error", JOptionPane.ERROR_MESSAGE);
                    dropdowns.get(i).setSelectedItem("");
            }
        }
        Pcontroller.setCurrentMapping(current);
    }

    // Runnable implementations
    @Override
    public void run(){
        //should be overridden
    }
}
