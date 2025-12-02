import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class saveWindow{
    public saveWindow(String windowTextArg, Controller sController) {
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
                if(sController.returnModule1State() == 0 || sController.returnModule2State() == 0 || sController.returnModule3State() == 0 || sController.returnModule4State() == 0){
                    throw new IOException("Error saving file! File contains unpopulated module.");
                }
                writer.write(Arrays.toString(sController.getModulesFileWriter()));
                JOptionPane.showMessageDialog(saveFileFrame, "File saved successfully to" + filePath);
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(saveFileFrame, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace(); //ideally this should be sent to a NEW file in a directory of the program! later todo!!!
            }
        }
    }
}
