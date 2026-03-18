public class simController {




    char[][] realPartMap = new char[2][4];

    simCore0 externalIO;
    simCore1 internalIO;

    public void init(){
        internalIO.init();
        externalIO.init();

    }

    public void simulate(){
        internalIO.simulate();
        externalIO.setPartMap(internalIO.getPartMap());
        externalIO.setWorkBuffer(internalIO.getBuffer());
        externalIO.simulate();
    }

    public void setRx(byte[] in){
        externalIO.setRx(in);
    }

    public byte[] getTx(){
        return externalIO.getTx();
    }

}
