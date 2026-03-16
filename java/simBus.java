public class simBus {
    simModule[] lines = new simModule[4];

    public byte transmit(byte data, int line){
        byte receipt = lines[line].bufferTransfer(data);
        for(int i = 0; i < lines.length; i++){
            lines[i].simulate();
        }
        return receipt;
    }
}
