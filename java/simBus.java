public class simBus {
    simModule[] lines = new simModule[4];

    public simBus() {
        for(int i = 0; i < 4; i++){
            lines[i] = new simNothing();
        }
    }

    public byte transmit(byte data, int line){
        byte receipt = lines[line].bufferTransfer(data);
        for(int i = 0; i < lines.length; i++){
            lines[i].simulate();
        }
        return receipt;
    }
}
