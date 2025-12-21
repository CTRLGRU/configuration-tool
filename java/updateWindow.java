import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.File;

public class updateWindow extends JFrame{
    private JFrame contentPanel = new JFrame("Update Window");
    private HttpController http = new HttpController();

    public updateWindow(String version) {
        /*For this there should be three steps: first open a window and let the user know it's checking for updates
         * Then it should actually do the logic of if no updates available, then display so with text, and an OK box
         * IF THERE IS, then it should transition the box to "an update is available (current ver. xx.xx -> new ver xx.xx)
         * and then have an "ok" or "cancel" with "update"
         * */
        contentPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPanel.setLocationRelativeTo(null);
        contentPanel.setResizable(false);
        contentPanel.setSize(500, 150);
        JPanel updatePanel = new JPanel();
        updatePanel.setBackground(Color.LIGHT_GRAY);
        JLabel updating = new JLabel("Searching for updates...", SwingConstants.CENTER);
        updatePanel.add(updating, BorderLayout.CENTER);
        contentPanel.add(updatePanel, BorderLayout.CENTER);
        contentPanel.setVisible(true);

        // Make a REST API request for the latest release, then parse the tag name
        // Technically users not using any auth tokens are limited to 60 calls per hour
        HttpController requester = new HttpController();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        String newVersion = version;
        try {
            root = mapper.readTree(
                requester.makeStringRequest(
                    requester.fromGitHub("https://api.github.com/repos/femortix/Femotech/releases/latest", "GET", null, null)
                ).body()
            );
            JsonNode tag = root.get("tag_name");
            if (tag != null) {
                newVersion = tag.asText(version);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Determine if an update was found; failure to read results in no update found.
        contentPanel.remove(updating);
        if (version.equals(newVersion)) {
            JLabel noUpdateFound = new JLabel("There were no found available updates. Current version is " + version + ".", SwingConstants.CENTER);
            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> contentPanel.dispose());
            contentPanel.add(noUpdateFound, BorderLayout.NORTH);
            contentPanel.add(okButton, BorderLayout.SOUTH);
        } else {
            JLabel updateFound = new JLabel("An update is available: " + version + " -> " + newVersion + ".", SwingConstants.CENTER);
            JButton updateButton = new JButton("Update");
            final JsonNode workingRoot = root;
            updateButton.addActionListener(e -> attemptUpdate(workingRoot, requester));
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> contentPanel.dispose());
            contentPanel.add(updateFound, BorderLayout.NORTH);
            contentPanel.add(updateButton, BorderLayout.WEST);
            contentPanel.add(cancelButton, BorderLayout.EAST);
        }
    }

    private void attemptUpdate(JsonNode root, HttpController requester) {
        String url = null;
        if (root.path("assets").isArray()) {
            url = root.get("assets").get(0).get("browser_download_url").asText();
        }
        requester.resetRequestData();
        requester.setHeader("Accept", "application/octet-stream");
        System.out.println(System.getProperty("user.dir") + File.separator + "Downloads" + File.separator + "configuration-tool.jar");
        requester.makeFileRequest(
            requester.fromURL(url, "GET", null),
            System.getProperty("user.dir") + File.separator + "Downloads" + File.separator + "configuration-tool.jar"
        );
    }
}
