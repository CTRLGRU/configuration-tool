public class Macro {
    public static final int DEFAULT_TRIGGER_LENGTH = 4;
    public static final int DEFAULT_PLAYBACK_LENGTH = 10;
    private byte[] trigger;
    private byte[] playback;

    public Macro(int triggerLen, int playbackLen) {
        trigger = new byte[triggerLen];
        playback = new byte[playbackLen];
    }

    public Macro() {
        trigger = new byte[DEFAULT_TRIGGER_LENGTH];
        playback = new byte[DEFAULT_PLAYBACK_LENGTH];
    }

    public int getTriggerLength() {
        return trigger.length;
    }

    public byte[] getTrigger() {
        return trigger;
    }

    public boolean setTrigger(byte[] data) {
        System.arraycopy(data, 0, trigger, 0, Math.min(data.length, trigger.length));
        if (data.length < trigger.length) {
            for (int i = data.length; i < trigger.length; i++) {
                trigger[i] = 0;
            }
        }
        return data.length > trigger.length;
    }

    public int getPlaybackLength() {
        return playback.length;
    }

    public byte[] getPlayback() {
        return playback;
    }

    public boolean setPlayback(byte[] data) {
        System.arraycopy(data, 0, playback, 0, Math.min(data.length, playback.length));
        if (data.length < playback.length) {
            for (int i = data.length; i < playback.length; i++) {
                playback[i] = 0;
            }
        }
        return data.length > playback.length;
    }
}