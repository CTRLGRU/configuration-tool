import java.util.Random;

//simulates electrical noise from there not being anything plugged into a slot
//lol

public class simNothing implements simModule{
    private byte commBuffer;
    boolean select;

    @Override
    public byte bufferTransfer(byte rx) {
        Random r = new Random();
        byte[] oneRandomByte = new byte[1];
        r.nextBytes(oneRandomByte);
        return oneRandomByte[0];
    }

    @Override
    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public void simulate() {
        return;
    }

}
