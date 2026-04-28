public class simROM {
    byte[] sector = new byte[2048];
    public void write(int addr, byte inp){
        sector[addr]=inp;
    }

    public byte read(int addr){
        return sector[addr];
    }
}
