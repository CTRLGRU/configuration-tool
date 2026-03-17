public class simMappingStorage {
    static int commercialLayouts = 2;
    static int customLayouts = 3;
    int totalMappings = simMappingStorage.commercialLayouts + simMappingStorage.customLayouts;
    byte[][] mapArray = new byte[2][8];

    byte[] setMapping(int mapping) {
        if (mapping < commercialLayouts) {
            setPremadeMapping(mapping);
        }
        setCustomMapping(mapping);
        return mapArray[1];
    }

    private void setPremadeMapping(int commercialMapping){
        switch (commercialMapping){
            case 0: //xbox layout
                mapArray[1][0] = 'J';
                mapArray[1][1] = 'B';
                mapArray[1][2] = 'J';
                mapArray[1][3] = 'B';
                break;

            case 1: //dualshock layout
                mapArray[1][0] = 'J';
                mapArray[1][1] = 'J';
                mapArray[1][2] = 'B';
                mapArray[1][3] = 'B';
                break;
        }
    }

    private void setCustomMapping(int customMapping){

    }

    public int getTotalMappings(){
        return totalMappings;
    }
}
