public class simCore0 implements simCore{

    byte[][] workBuffer = new byte[6][8];

    byte[] rx = new byte[64];
    byte[] tx = new byte[64];
    boolean rxFlag = false;

    byte[][] mapping = new byte[2][8];



    simMemory mapSystem;
    int currentMapping = 0;
    byte outputMode = 'G';  //default to generic USB




    boolean[] flags = new boolean[8];
    /*
    flags:
    0|change output map
    1|buffer ready for transfer
    2|change mode
    3|recalibrate
    4|
    5|
    6|
    7|
    */


    @Override
    public void simulate() {
        if (rxFlag) {
            commandReceived();
        }

        if (flags[0]) {
            incrementMapping();
        }
    }


    public void setWorkBuffer(byte[][] input) {
            for(int i = 0; i < 6; i++){
                System.arraycopy(input[i], 0, workBuffer[i], 0, 8);
            }
    }

    public void setRx(byte[] in){
        System.arraycopy(in, 0, rx, 0, 64);
        rxFlag=true;
    }

    public byte[] getTx(){
        return tx;
    }

    private void commandReceived(){

    }

    private void incrementMapping(){
        currentMapping++;
        if(currentMapping >= mapSystem.getTotalMappings()){
            currentMapping=0;
        }
        mapping[1] = mapSystem.setMapping(currentMapping);
    }
}
