public class Mapping {
    public static final int DEFAULT_COMPONENT_COUNT = 4;
    public static final int DEFAULT_MACRO_COUNT = 8;
    private byte[] components;
    private Macro[] macros;

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
        macros = new Macro[count];
        for (int i = 0; i < macros.length; i++) {
            macros[i] = new Macro(triggerLen, playbackLen);
        }
    }

    public void setMacroCount(int count) {
        macros = new Macro[count];
        for (int i = 0; i < macros.length; i++) {
            macros[i] = new Macro();
        }
    }

    public byte getComponent(int index) {
        return components[index];
    }

    public void setComponent(int index, byte component) {
        components[index] = component;
    }

    public Macro getMacro(int index) {
        return macros[index];
    }

    public void setMacro(int index, Macro macro) {
        macros[index] = macro;
    }
}