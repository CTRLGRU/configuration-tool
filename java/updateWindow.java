import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class updateWindow extends JFrame{
    private final JFrame contentPanel = new JFrame("Update Window");
    private final HttpController http = new HttpController();

    public updateWindow(String version) {
        /*For this there should be three steps: first open a window and let the user know it's checking for updates
         * Then it should actually do the logic of if no updates available, then display so with text, and an OK box
         * IF THERE IS, then it should transition the box to "an update is available (current ver. xx.xx -> new ver xx.xx)"
         * and then have an "ok" or "cancel" with "update"
         * */
        contentPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPanel.setLocationRelativeTo(null);
        contentPanel.setResizable(false);
        contentPanel.setSize(500, 150);
        JPanel updatePanel = new JPanel();
        updatePanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setContentPane(updatePanel);
        JLabel updating = new JLabel("Searching for updates...", SwingConstants.CENTER);
        updatePanel.add(updating, BorderLayout.CENTER);
        contentPanel.setVisible(true);

        // Make a REST API request for the latest release, then parse the tag name
        // Technically users not using any auth tokens are limited to 60 calls per hour
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        String newVersion = version;
        try {
            root = mapper.readTree(
                http.makeStringRequest(
                    http.fromGitHub("https://api.github.com/repos/CTRLGRU/configuration-tool/releases/latest", "GET", null, null)
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
        updatePanel.remove(updating);
        updatePanel.revalidate();
        updatePanel.repaint();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> contentPanel.dispose());
        if (version.equals(newVersion)) {
            JLabel noUpdateFound = new JLabel("There were no found available updates. Current version is " + version + ".", SwingConstants.CENTER);
            updatePanel.add(noUpdateFound, BorderLayout.NORTH);
            updatePanel.add(okButton, BorderLayout.SOUTH);
        } else {
            JLabel updateFound = new JLabel("An update is available: " + version + " -> " + newVersion + ".", SwingConstants.CENTER);
            JLabel updated = new JLabel("<html>Successfully updated to the most recent version.<br><br>Restart the application for it to take effect.</html>", SwingConstants.CENTER);
            JButton updateButton = new JButton("Update");
            final JsonNode workingRoot = root;
            updateButton.addActionListener(e -> attemptUpdate(updatePanel, updated, okButton, workingRoot));
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> contentPanel.dispose());
            updatePanel.add(updateFound, BorderLayout.NORTH);
            updatePanel.add(updateButton, BorderLayout.SOUTH);
            updatePanel.add(cancelButton, BorderLayout.SOUTH);
        }
        updatePanel.revalidate();
        updatePanel.repaint();
    }

    private void attemptUpdate(JPanel panel, JLabel label, JButton button, JsonNode root) {
        String url = null;
        if (root.path("assets").isArray()) {
            url = root.get("assets").get(0).get("browser_download_url").asText();
        }
        http.resetRequestData();
        http.setHeader("Accept", "application/octet-stream");
        http.makeFileRequest(
            http.fromURL(url, "GET", null),
            "configuration-tool.jar"
        );
        panel.removeAll();
        panel.add(label, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);
        panel.revalidate();
        panel.repaint();
    }
}
