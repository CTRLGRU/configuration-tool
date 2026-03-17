public class simController {




    char[][] mapping = new char[2][4];

    simCore0 externalIO;
    simCore1 internalIO;

    public void init(){
        mapping[1][0] = 'J';
        mapping[1][1] = 'B';
        mapping[1][2] = 'J';
        mapping[1][3] = 'B';

    }

    public void simulate(){
        internalIO.simulate();
        mapping[0]=internalIO.getPartMap();
        externalIO.set
        externalIO.simulate();
    }

    public void setRx(byte[] in){
        externalIO.setRx(in);
    }

    public byte[] getTx(){
        return externalIO.getTx();
    }

}
