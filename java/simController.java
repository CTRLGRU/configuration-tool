public class simController {



    char[][] mapping = new char[2][4];

    simCore0 internalIO;
    simCore1 externalIO;

    public void init(){
        mapping[1][0] = 'J';
        mapping[1][1] = 'B';
        mapping[1][2] = 'J';
        mapping[1][3] = 'B';

    }

    public void simulate(){
        internalIO.simulate();
        externalIO.simulate();
    }

}
