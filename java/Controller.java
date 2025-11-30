public class Controller extends Component{
    private Component Module1;
    private Component Module2;
    private Component Module3;
    private Component Module4;

    public Controller(){
        //im a pretty encapsulation yes-i-am
    }

    public void initializeController(){
        Module1 = new Component();
        Module1.initialization();
        Module2 = new Component();
        Module2.initialization();
        Module3 = new Component();
        Module3.initialization();
        Module4 = new Component();
        Module4.initialization();
    }

    public void setModule1(int axes, int buttonQty, String name){
        Module1.setAxes(axes);
        Module1.setButtonQty(buttonQty);
        Module1.setName(name);
        Module1.setModuleNumber(1);
    }

    public void setModule2(int axes, int buttonQty, String name){
        Module2.setAxes(axes);
        Module2.setButtonQty(buttonQty);
        Module2.setName(name);
        Module2.setModuleNumber(2);
    }

    public void setModule3(int axes, int buttonQty, String name){
        Module3.setAxes(axes);
        Module3.setButtonQty(buttonQty);
        Module3.setName(name);
        Module3.setModuleNumber(3);
    }

    public void setModule4(int axes, int buttonQty, String name){
        Module4.setAxes(axes);
        Module4.setButtonQty(buttonQty);
        Module4.setName(name);
        Module4.setModuleNumber(4);
    }

    // side rant: the fact IntelliJ says "linter" is redundant, but without it being explicitly initialized it complains about it not
    // being initialized! Why!?
    public String[] getModulesFileWriter(){
        String[] linter = {Module1.fileWriter(Module1, Module1.getModuleNumber()),
                Module2.fileWriter(Module2, Module2.getModuleNumber()),
                Module3.fileWriter(Module3, Module3.getModuleNumber()),
                Module4.fileWriter(Module4, Module4.getModuleNumber())};
        return linter;
    }
}
