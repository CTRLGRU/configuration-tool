public class sim4Buttons implements simModule{
    private byte commBuffer;
    private boolean select;
    private byte inputBuffer;
    static byte deviceID = 'B';
    byte collectedData;
    boolean[] buttonStates = new boolean[4];

    @Override
    public byte bufferTransfer(byte rx) {
        if(!select){
            return -1;
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
        byte temp = 0;
        temp |= 0x70;

        if(buttonStates[0]){
            temp |= (1 << 3);
        }
        if(buttonStates[1]){
            temp |= (1 << 2);
        }
        if(buttonStates[2]){
            temp |= (1 << 1);
        }
        if(buttonStates[3]){
            temp |= 1;
        }

        if(!parity(temp)){
            temp |= (1 << 7);
        }

        if(commBuffer=='X'){
            commBuffer = deviceID;
        }
        if(commBuffer=='R'){
            commBuffer = temp;
        }
    }

    private boolean parity(byte buff){
        int count = 0;
        for(int i = 0; i < 8; i++){
            if((buff & (1 << i))==1){
                count++;
            }
        }
        return (count & 1) != 0;
    }
}
