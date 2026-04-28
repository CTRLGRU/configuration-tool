import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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
    private ButtonGroup deviceChoices = new ButtonGroup();
    public String version = "0.2.0";

    public WindowController(Controller controller){
        super("Controller Window");
        Pcontroller = controller;
        dropdowns = new ArrayList<JComboBox<String>>(controller.getModuleCount());
        inputs = new ArrayList<JTextArea>(controller.getModuleCount());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Panel inside the window
        contentPanel.setLayout(new GridLayout(0, 6)); // Number of columns only matters when number of rows is unset???
        contentPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                contentPanel.repaint();
            }
        });
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
        deviceMenu.setMnemonic(KeyEvent.VK_D);
        deviceMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                List<JRadioButtonMenuItem> devices = refreshDevices();
                for (int i = 0; i < devices.size(); i++) {
                    deviceMenu.add(devices.get(i));
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                deviceMenu.removeAll();
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                deviceMenu.removeAll();
            }
        });
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
                        default:
                            Pcontroller.setComponent(iCopy, (byte) 'X');
                            Pcontroller.setModule(iCopy, 0, 0, "",
                                "Unset or empty module."
                            );
                    }
                }
            });
            JPanel moduleSettings = new JPanel(new GridLayout(2, 2));
            moduleSettings.setBackground(new Color(0, 0, 0, 0));
            JTextArea input = new JTextArea();
            moduleSettings.add(dropdown);
            moduleSettings.add(new JLabel("Module " + (i + 1) + ":"));
            moduleSettings.add(new JLabel());
            moduleSettings.add(input);
            contentPanel.add(moduleSettings);
            dropdowns.add(dropdown);
            inputs.add(input);
        }

        // Below code block assumes default 4-module setup with 2 hardwired triggers
        JPanel moduleSettings = new JPanel(new GridLayout(2, 2));
        moduleSettings.setBackground(new Color(0, 0, 0, 0));
        JTextArea input = new JTextArea();
        moduleSettings.add(new JLabel());
        moduleSettings.add(new JLabel("Trigger 1:"));
        moduleSettings.add(new JLabel());
        moduleSettings.add(input);
        contentPanel.add(moduleSettings);
        inputs.add(input);

        moduleSettings = new JPanel(new GridLayout(2, 2));
        moduleSettings.setBackground(new Color(0, 0, 0, 0));
        input = new JTextArea();
        moduleSettings.add(new JLabel());
        moduleSettings.add(new JLabel("Trigger 2:"));
        moduleSettings.add(new JLabel());
        moduleSettings.add(input);
        contentPanel.add(moduleSettings);
        inputs.add(input);

        List<JPanel> modules = setupModules();

        //We should set a default size to open at, I think 1200x800 makes sense in WxH
        setPreferredSize(new Dimension(1200, 800));
        setContentPane(contentPanel);
        pack();
        setVisible(true);
    }

    private List<JPanel> setupModules() { // Defaults to the 4-module setup for a 1200x800 window
        List<JPanel> modules = new ArrayList<JPanel>(4);
        ButtonPanel module1 = new ButtonPanel("Up", "Left", "Right", "Down");
        AnalogStickPanel module2 = new AnalogStickPanel(25);
        AnalogStickPanel module3 = new AnalogStickPanel(25);
        ButtonPanel module4 = new ButtonPanel("Y", "X", "B", "A");
        contentPanel.add(new JLabel()); // Row 2 of 200x200 panels
        contentPanel.add(module1);
        contentPanel.add(new JLabel());
        contentPanel.add(new JLabel());
        contentPanel.add(module4);
        contentPanel.add(new JLabel());
        contentPanel.add(new JLabel()); // Row 3 of 200x200 panels
        contentPanel.add(module2);
        contentPanel.add(new JLabel());
        contentPanel.add(new JLabel());
        contentPanel.add(module3);
        contentPanel.add(new JLabel());
        contentPanel.add(new JLabel()); // Row 4 exists
        modules.add(module1);
        modules.add(module2);
        modules.add(module3);
        modules.add(module4);
        return modules;
    }

    private List<JRadioButtonMenuItem> refreshDevices() {
        List<JRadioButtonMenuItem> devices = new ArrayList<JRadioButtonMenuItem>();
        deviceChoices = new ButtonGroup();

        JRadioButtonMenuItem simToggle = new JRadioButtonMenuItem("Simulation");
        simToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pcontroller.disconnect();
                Pcontroller.setPort(null);
            }
        });
        simToggle.setSelected(true);
        deviceChoices.add(simToggle);
        devices.add(simToggle);

        SerialPort[] ports = USBController.scan();
        for (SerialPort port : ports) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(port.getDescriptivePortName());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Pcontroller.disconnect();
                    Pcontroller.setPort(port);
                    Pcontroller.connect(115200);
                }
            });
            if (Pcontroller.getPort() == port) {
                item.setSelected(true);
            }
            deviceChoices.add(item);
            devices.add(item);
        }

        return devices;
    }

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
