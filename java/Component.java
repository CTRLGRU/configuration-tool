// Component Abstract Base Class - an ABC that ensures composition of Module and Device are enforced for
// instantiations of Component subclasses. This also allows us to make sure that Device's toFile function are done
// with enough cohesion to export and make work on the controller.
abstract class Component implements ModuleInterface, DeviceInterface{
    //public method, vars of ModuleInterface
    //DeviceInterface functions, ex:

    //public int FileIoHandler()

}
