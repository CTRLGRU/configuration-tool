// Component Abstract Base Class - an ABC that ensures composition of Module and Device are enforced for
// instantiations of Component subclasses. This also allows us to make sure that Device's toFile function are done
// with enough cohesion to export and make work on the controller.
abstract class Component implements ModuleInterface, DeviceInterface{
    public ModuleInterface module;
    //public method, vars of ModuleInterface
    public int getButtonQty(){
        return 0;
    };
    public void setButtonQty(int qty){

    };
    public int getAxes(){
        return 0;
    };
    public void setAxes(int axes){

    };
    public boolean initialization(){
        return true;
    };
    //DeviceInterface functions, ex:

    //public int FileIoHandler()
    public int fileWriter(Module module, int ID){
        return 0;
    }
}
