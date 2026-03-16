public class simJoystick implements simModule{
    private byte commBuffer;
    private boolean select;
    private byte inputBuffer;
    static byte deviceID = 'J';
    byte[] collectedData = new byte[4];
    byte[] intermediateData = new byte[4];
    byte[] finalData = new byte[4];
    short[] axes = new short[2];
    boolean click;
    int byteToSend = 0;

    @Override
    public byte bufferTransfer(byte rx) {
        if(!select){
            return 0;
        }
        byte temp = commBuffer;
        commBuffer = rx;
        return temp;
    }

    @Override
    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public void simulate() {
        collectedData[0] = (byte) (axes[0] & 0xFF);
        collectedData[1] = (byte) ((axes[0] >> 8) & 0xFF);
        collectedData[2] = (byte) (axes[1] & 0xFF);
        collectedData[3] = (byte) ((axes[1] >> 8) & 0xFF);

        if(click){
            collectedData[0] |= (1 << 6);
        }

        collectedData[0] |= 0x3c;


        if(!parity(collectedData)){
            temp |= (1 << 7);
        }

        System.arraycopy(collectedData, 0, intermediateData, 0, 4);

        if(commBuffer=='X'){
            commBuffer = deviceID;
        }

        if(commBuffer=='R'){
            commBuffer = intermediateData[byteToSend=0];
        }

        if(byteToSend>3){
            commBuffer = ++intermediateData[byteToSend];
        }
    }

    private boolean parity(byte[] buff){
        int count = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 8; j++) {
                if((buff[i] & (1 << j))==1){
                    count++;
                }
            }
        }
        return (count & 1) != 0;
    }
}
