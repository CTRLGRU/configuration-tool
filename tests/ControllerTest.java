//Tests go here. I need to link JUnit library, and our SWING library!!!
//
//import org.junit.jupiter.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    public void testController() {
        Controller controller = new Controller();
        controller.initializeController();
        assertNotNull(controller);
        controller.setModule1(1,1,"test1");
        controller.setModule2(1,1,"test2");
        controller.setModule3(1,1,"test3");
        controller.setModule4(1,1,"test4");
        assertEquals(3, controller.returnModule1State(), "module 1 returns expected state operand");
        assertEquals(4, controller.returnModule2State(), "module 2 returns expected state operand");
        assertEquals(5, controller.returnModule3State(), "module 3 returns expected state operand");
        assertEquals(6, controller.returnModule4State(), "module 4 returns expected state operand");
    }

    //this project is not very well equipped for junit upon further inspection. python ui testing framework would add more value.
}
