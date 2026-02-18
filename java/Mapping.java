public class Mapping {
    public static final int COMPONENT_COUNT = 4;
    public static final int PLAYBACK_COUNT = 8;
    private byte[] components;
    private byte[][] playbacks;
    private byte[][] triggers;

    public Mapping(int components, int playbacks) {
        setComponentCount(components);
        setPlaybackCount(playbacks);
    }

    public Mapping() {
        setComponentCount(COMPONENT_COUNT);
        setPlaybackCount(PLAYBACK_COUNT);
    }

    public int getComponentCount() {
        return components.length;
    }

    public void setComponentCount(int count) {
        components = new byte[count];
    }

    public int getPlaybackCount() {
        return playbacks.length;
    }

    public void setPlaybackCount(int count) {
        triggers = new byte[count][4 * components.length];
        playbacks = new byte[count][10 * components.length];
    }

    public byte getComponent(int index) {
        return components[index];
    }

    public void setComponent(int index, byte component) {
        components[index] = component;
    }

    public byte[] getPlayback(int index) {
        return playbacks[index];
    }

    public void setPlayback(int index, byte[] commands) {
        System.arraycopy(commands, 0, playbacks[index], 0, playbacks[0].length);
    }

    public byte[] getTrigger(int index) {
        return triggers[index];
    }

    public void setTrigger(int index, byte[] commands) {
        System.arraycopy(commands, 0, triggers[index], 0, triggers[0].length);
    }
}