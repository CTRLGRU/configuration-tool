// Component Abstract Base Class - an ABC that ensures composition of Module and Device are enforced for
// instantiations of Component subclasses. This also allows us to make sure that Device's toFile function are done
// with enough cohesion to export and make work on the controller.
class Component implements ModuleInterface, DeviceInterface{
    public ModuleInterface module;

    private String name;
    private String description;
    private int ButtonQty;
    private int Axes;
    private int ModuleNumber;
    //public method, vars of ModuleInterface
    public int getButtonQty(){
        return ButtonQty;
    };

    public void setButtonQty(int qty){
        ButtonQty = qty;
    };

    public int getAxes(){
        return Axes;
    };

    public void setAxes(int axes){
        if(0<=axes && axes<=3){
            Axes = axes;
        }
        else{
            System.out.println("Invalid Axes quantity");
        }
    };

    public int getModuleNumber(){
        return ModuleNumber;
    }

    public void setModuleNumber(int number){
        ModuleNumber = number;
    }

    public String getName(){
        return name;
    }

    public void setName(String named){
        name = named;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String descriptiond){
        description = descriptiond;
    }

    // initialization technically is a clearing function.
    public boolean initialization(){
        Axes = 0;
        ModuleNumber = 0;
        ButtonQty = 0;
        name = "null";
        description = "null";
        return true;
    };
    //DeviceInterface functions, ex:

    //public int FileIoHandler()
    public String fileWriter(Component module, int ID){
        return "="+module.getButtonQty()+":"+module.getAxes()+":"+module.getModuleNumber()+":\n";
    }
}
