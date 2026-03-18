public class simMappingStorage {
    static int commercialLayouts = 2;
    static int customLayouts = 3;
    int totalMappings = simMappingStorage.commercialLayouts + simMappingStorage.customLayouts;

    void setMapping(int mapping, char[][] mapArray) {
        if (mapping < commercialLayouts) {
            setPremadeMapping(mapping, mapArray);
        }
        setCustomMapping(mapping, mapArray);
    }

    private void setPremadeMapping(int mapping, char[][] mapArray){

    }

    private void setCustomMapping(int mapping, char[][] mapArray){

    }

    public int getTotalMappings(){
        return totalMappings;
    }
}
