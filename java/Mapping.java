public class Mapping {
    public static final int DEFAULT_COMPONENT_COUNT = 4;
    public static final int DEFAULT_MACRO_COUNT = 8;
    private byte[] components;
    private Macro[][] macros;

    public Mapping(int components, int macros, int triggerLen, int playbackLen) {
        setComponentCount(components);
        setMacroCount(macros, triggerLen, playbackLen);
    }

    public Mapping(int components, int macros) {
        setComponentCount(components);
        setMacroCount(macros);
    }

    public Mapping() {
        setComponentCount(DEFAULT_COMPONENT_COUNT);
        setMacroCount(DEFAULT_MACRO_COUNT);
    }

    public int getComponentCount() {
        return components.length;
    }

    public void setComponentCount(int count) {
        components = new byte[count];
    }

    public int getMacroCount() {
        return macros.length;
    }

    public void setMacroCount(int count, int triggerLen, int playbackLen) {
        macros = new Macro[count][components.length + 2];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < components.length; j++) {
                macros[i][j] = new Macro(triggerLen, playbackLen);
            }
        }
    }

    public void setMacroCount(int count) {
        macros = new Macro[count][components.length + 2];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < components.length + 2; j++) {
                macros[i][j] = new Macro();
            }
        }
    }

    public byte getComponent(int index) {
        return components[index];
    }

    public void setComponent(int index, byte component) {
        components[index] = component;
    }

    public Macro getMacro(int index, int component) {
        return macros[index][component];
    }

    public void setMacro(int index, int component, Macro macro) {
        macros[index][component] = macro;
    }

    public static Mapping generateMapping(byte[] data, int components, int macros) {
        Mapping mapping = new Mapping(components, macros);
        int cur = 0;
        for (; cur < components; cur++) {
            mapping.setComponent(cur, data[cur]);
        }
        for (int i = 0; i < mapping.getMacroCount(); i++) { // For every macro
            for (int j = 0; j < mapping.getMacro(i, 0).getTriggerLength(); j++) { // For every trigger step
                for (int k = 0; k < mapping.getComponentCount() + 2; k++) { // For every component, copy a byte
                    mapping.getMacro(i, k).getTrigger()[j] = data[cur];
                    cur++;
                }
            }
        }
        for (int i = 0; i < mapping.getMacroCount(); i++) { // For every macro
            for (int j = 0; j < mapping.getMacro(i, 0).getPlaybackLength(); j++) { // For every playback step
                for (int k = 0; k < mapping.getComponentCount() + 2; k++) { // For every component, copy a byte
                    mapping.getMacro(i, k).getPlayback()[j] = data[cur];
                    cur++;
                }
            }
        }
        return mapping;
    }
}