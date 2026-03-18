import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {
    // This test ensures that a config saved with specific components and playback counts is saved properly
    @Test
    public void configSave() {
        Controller test = new Controller();
        test.setComponent(0, (byte) 'J');
        test.setComponent(1, (byte) 'B');
        test.setComponent(2, (byte) 0);
        test.setComponent(3, (byte) 'B');
        String data = test.fileWriter(0);
        byte[] buffer = data.getBytes(StandardCharsets.ISO_8859_1);

        // Check that component and playback counts were saved correctly
        //assertEquals(Mapping.DEFAULT_COMPONENT_COUNT, buffer[0], "Component count not preserved.");
        //assertEquals(Mapping.DEFAULT_MACRO_COUNT, buffer[1], "Macro count not preserved.");
        // Check that component codes were saved correctly
        assertEquals((byte) 'J', buffer[0], "Component code not preserved.");
        assertEquals((byte) 'B', buffer[1], "Component code not preserved.");
        assertEquals((byte) 0, buffer[2], "Component code not preserved.");
        assertEquals((byte) 'B', buffer[3], "Component code not preserved.");
    }
}