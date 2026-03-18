public class simCore1 implements simCore{

    long timeSinceLast = 0;
    byte[][] intermediateBuffer = new byte[6][8];
    byte[] partMap = new byte[4];
    simBus bus;
    static char[] supportedIDs = {'B','J'};

    public byte[][] getBuffer(){
        return intermediateBuffer;
    }

    public void init(){

    }

    public byte[] getpartMap(){
        return partMap;
    }

    @Override
    public void simulate() {
        if ((System.currentTimeMillis() - timeSinceLast) >= 1) {
            return;
        }

        clearBuffer();


        for (int i = 0; i < 4; i++) {
            pollModule(i);
        }

        intermediateBuffer[5][0] = pollIntegralButtons();
        intermediateBuffer[6] = pollIntegratedTriggers();


    }

    private void pollModule(int line){
        int numBytes=0;
        switch(partMap[line]){
            case 'X':
                partMap[line] = getID(line);
                return;
            case 'J':
                numBytes = 4;
                break;
            case 'B':
                numBytes = 1;
                break;

        }
        bus.transmit((byte) 'R',line);
        for(int i = 0; i < numBytes; i++){
            intermediateBuffer[line][i] = bus.transmit((byte) 0,line);
        }

        int num1s = 0;
        boolean xcheck;
        boolean ycheck;
        boolean bcheck;

        switch (partMap[line]) {
            case 'J':
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 8; j++) {
                        if ((intermediateBuffer[line][i] & (1 << j)) != 0) {
                            num1s++;
                        }
                    }
                }
                xcheck = (intermediateBuffer[line][0] & 0x3C) == 0x3C;  //check if there are any 0s in the x section's padding
                ycheck = (intermediateBuffer[line][2] & 0xFC) != 0;       //check if there are any 1s in the y section's padding
                if (((num1s & 1) == 0) || !xcheck || !ycheck) {
                    getID(line);
                    for (int i = 0; i < 4; i++) {  //if the data's corrupted just wipe the module's state and data, try again next cycle
                        intermediateBuffer[line][i] = 0;
                    }
                }
                break;
            case 'B':
                for (int i = 0; i < 8; i++) {
                    if ((intermediateBuffer[line][i] & (1 << i)) != 0) {
                        num1s++;
                    }
                }
                bcheck = ((intermediateBuffer[line][0] & 0x70) == 0x70);
                if (((num1s & 1) == 0) || !bcheck) {
                    getID(line);
                    for (int i = 0; i < 4; i++) {
                        intermediateBuffer[line][i] = 0;
                    }
                }
                break;
        }
    }

    private byte getID(int line){
        byte returnedByte = bus.transmit((byte)'X',line);
        if(verifyModuleID(returnedByte)){
            return returnedByte;
        } else{
            return 'X';
        }
    }

    private boolean verifyModuleID(byte ID){
        for(int i = 0; i < supportedIDs.length; i++){
            if(ID == supportedIDs[i]){
                return true;
            }
        }
        return false;
    }

    public byte[] getPartMap(){
        return partMap;
    }

    private void clearBuffer(){
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 8; j++){
                intermediateBuffer[i][j]=0;
            }
        }
    }

    private byte pollIntegralButtons(){
        byte buttonData = 0;
        return buttonData;
    }

    private byte[] pollIntegratedTriggers(){
        byte[] triggerData = new byte[8];
        return triggerData;
    }
}