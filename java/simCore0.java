public class simCore0 implements simCore{

    byte[][] workBuffer = new byte[6][8];

    byte[] rx = new byte[64];
    byte[] tx = new byte[64];
    boolean rxFlag = false;

    byte[][] mapping = new byte[2][8];


    simMappingStorage mapSystem;
    int currentMapping = 0;
    char outputMode = 'G';  //default to generic USB

    public void setPartMap(byte[] in){
        mapping[0] = in;
    }

    public void init(){
        mapping[1][0] = 'J';
        mapping[1][1] = 'B';
        mapping[1][2] = 'J';
        mapping[1][3] = 'B';
    }


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
        rxHandling();
        flagHandling();

        translation();

        output();



    }


    private void translation(){
        for(int i = 0; i < 4; i++){
            if(mapping[0][i] == mapping[1][i] || mapping[0][i] == 'X'){
                continue;
            } else{
                translateModule(i);
            }
        }
    }

    private void translateModule(int line){
        switch(mapping[0][line]){
            case 'B':
                switch(mapping[1][line]){
                    case 'J':
                        translateButtonToJoystick(line);
                }
            case 'J':
                switch(mapping[1][line]){
                    case 'B':
                        translateJoystickToButton(line);
                }
        }
    }

    private void translateButtonToJoystick(int line){

    }

    private void translateJoystickToButton(int line){

    }

    private void output(){

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

    private void rxHandling(){
        if(rxFlag){

        }
    }

    private void flagHandling(){
        if (flags[0]) {
            incrementMapping();
        }
    }

    private void incrementMapping(){
        currentMapping++;
        if(currentMapping >= mapSystem.getTotalMappings()){
            currentMapping=0;
        }
        mapSystem.setMapping(currentMapping,mapping);
    }
}
