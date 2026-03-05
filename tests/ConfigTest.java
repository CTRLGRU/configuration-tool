import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {
    private static File testFile = new File(System.getProperty("user.dir")+"/assets/test.fmc");

    @BeforeAll
    public static void createFile() {
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This test ensures that a config saved with specific components and playback counts is saved properly
    @Test
    public void configSave() {
        Controller test = new Controller();
        test.setComponent(0, (byte) 'J');
        test.setComponent(1, (byte) 'B');
        test.setComponent(2, (byte) 0);
        test.setComponent(3, (byte) 'B');
        FileWriter writer;
        try {
            writer = new FileWriter(testFile, StandardCharsets.ISO_8859_1);
            writer.write(test.fileWriter(0));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer;
        FileReader reader;
        try {
            reader = new FileReader(testFile, StandardCharsets.ISO_8859_1);
            int components = reader.read();
            int playbacks = reader.read();
            // Check that component and playback counts were saved correctly
            assertEquals(Mapping.DEFAULT_COMPONENT_COUNT, components, "Component count not preserved.");
            assertEquals(Mapping.DEFAULT_PLAYBACK_COUNT, playbacks, "Playback count not preserved.");
            buffer = new byte[components * (1 + 14 * playbacks) + 2];
            buffer[0] = (byte) components;
            buffer[1] = (byte) playbacks;
            int i = 2;
            for (int r = reader.read(); r != -1 && i < components + 2; r = reader.read()) {
                buffer[i] = (byte) r;
                i++;
            }
            // Check that component codes were saved correctly
            assertEquals((byte) 'J', buffer[2], "Component code not preserved.");
            assertEquals((byte) 'B', buffer[3], "Component code not preserved.");
            assertEquals((byte) 0, buffer[4], "Component code not preserved.");
            assertEquals((byte) 'B', buffer[5], "Component code not preserved.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void deleteFile() {
        testFile.delete();
    }
}