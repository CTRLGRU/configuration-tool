import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class updateWindow extends JFrame{
    private JFrame contentPanel;
    private HttpController http = new HttpController();

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

        HttpController requester = new HttpController();
        requester.makeRequest(requester.fromGitHub("https://api.github.com/repos/femortix/Femotech/releases/latest", "GET", null, null));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        String newVersion = version;
        try {
            root = mapper.readTree(requester.getResponse().body());
            newVersion = root.get("tag_name").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int option = -1;
        if (version.equals(newVersion)) {
            JOptionPane.showMessageDialog(updateFrame, "There were no found available updates. Current version is "+version+".", "Update Window", JOptionPane.INFORMATION_MESSAGE);
        } else {
            option = JOptionPane.showConfirmDialog(updateFrame, "An available update was found: Version " + newVersion + ".\nCurrent version is " + version + ".\nWould you like to update?", "Update Window", JOptionPane.YES_NO_OPTION);
        }

        switch(option) {
            case JOptionPane.YES_OPTION:
                String url = null;
                if (root.path("assets").isArray()) {
                  url = root.get("assets").get(0).get("browser_download_url").asText();
                }
                requester.resetRequestBuilder();
                requester.setHeader("Accept", "application/octet-stream");
                requester.makeRequest(requester.fromURL(url, "GET", null));
                // Process response into replacement executable
                break;
            case JOptionPane.NO_OPTION:
            default:
        }
        //updateFrame.add(updatePanel, BorderLayout.CENTER);
        //the below statement should go inside of one of the case statements for if(updateAvail = true); else{...
        //updateFrame.setVisible(true);// originally true
    }
}
