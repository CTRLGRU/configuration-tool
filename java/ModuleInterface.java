// ModuleInterface - An interface for module objects/classes
public interface ModuleInterface {
    //variables/functions every module should have, like ButtonQty or Axes (0,1,2)
    public int getButtonQty();
    public void setButtonQty(int qty);
    public int getAxes();
    public void setAxes(int axes); //this could be shortened to Bool (not bool) since it is a tri state type
    //it's a start
    public boolean initialization(); //an initialization routine for a module, such as using the above getters and setters.
}
