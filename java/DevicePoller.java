import java.util.List;

public class DevicePoller implements Runnable {
    private boolean isRunning = true;
    private Controller controller;
    private ModulePanel[] modules;
    private List<VirtualModuleInterface> components;

    public DevicePoller(Controller cont, ModulePanel[] mods, List<VirtualModuleInterface> comps) {
        controller = cont;
        modules = mods;
        components = comps;
    }

    public void interpret() {
        if (USBController.isNull()) { // Taking data from the simulator
            byte[] data = components.getFirst().getStatesAsOutput();
            modules[0].setPhysicalInput(data);
            modules[0].setVirtualInput(data);
            data = components.get(1).getStatesAsOutput();
            modules[1].setPhysicalInput(data);
            modules[1].setVirtualInput(data);
            data = components.get(2).getStatesAsOutput();
            modules[2].setPhysicalInput(data);
            modules[2].setVirtualInput(data);
            data = components.get(3).getStatesAsOutput();
            modules[3].setPhysicalInput(data);
            modules[3].setVirtualInput(data);
            modules[4].setPhysicalInput(new byte[]{0, 0, 0}); // Have yet to implement
            modules[4].setVirtualInput(new byte[]{0, 0, 0}); // sliders for triggers
            modules[5].setPhysicalInput(new byte[]{0, 0, 0});
            modules[5].setVirtualInput(new byte[]{0, 0, 0});
            return;
        }
        byte[] data = controller.readInput();
        if (controller.isRaw()) { // Raw module-by-module bytes (6 modules * 3 bytes)
            byte[] dataPiece = {0, 0, 0};
            System.arraycopy(data, 0, dataPiece, 0, 3);
            modules[0].setPhysicalInput(dataPiece);
            components.getFirst().getStatesFromInput(dataPiece);
            System.arraycopy(data, 3, dataPiece, 0, 3);
            modules[1].setPhysicalInput(dataPiece);
            components.get(1).getStatesFromInput(dataPiece);
            System.arraycopy(data, 6, dataPiece, 0, 3);
            modules[2].setPhysicalInput(dataPiece);
            components.get(2).getStatesFromInput(dataPiece);
            System.arraycopy(data, 9, dataPiece, 0, 3);
            modules[3].setPhysicalInput(dataPiece);
            components.get(3).getStatesFromInput(dataPiece);
            System.arraycopy(data, 12, dataPiece, 0, 3);
            modules[4].setPhysicalInput(dataPiece);
            System.arraycopy(data, 15, dataPiece, 0, 3);
            modules[5].setPhysicalInput(dataPiece);
            return;
        }
        byte[] dataPiece = {0, 0, 0};
        if (controller.getFormat() == 0) { // Xbox layout
            System.arraycopy(data, 0, dataPiece, 0, 2);
            modules[0].setPhysicalInput(dataPiece); // Get left analog in top-left first
            components.getFirst().getStatesFromInput(dataPiece);
            System.arraycopy(data, 2, dataPiece, 0, 2);
            modules[2].setPhysicalInput(dataPiece); // Get right analog in bottom-right next
            components.get(2).getStatesFromInput(dataPiece);
            dataPiece[0] = data[4]; // Get left trigger next
            dataPiece[1] = 0;
            modules[4].setPhysicalInput(dataPiece);
            dataPiece[0] = data[5]; // Get right trigger next
            modules[5].setPhysicalInput(dataPiece);
            dataPiece[0] = data[6]; // Get DPad next
            modules[3].setPhysicalInput(dataPiece);
            components.get(3).getStatesFromInput(dataPiece);
            dataPiece[0] = data[7]; // Get ABXY next
            modules[1].setPhysicalInput(dataPiece);
            components.get(1).getStatesFromInput(dataPiece);
        } else { // PlayStation format
            System.arraycopy(data, 0, dataPiece, 0, 2);
            modules[3].setPhysicalInput(dataPiece); // Get left analog in top-left first
            components.get(3).getStatesFromInput(dataPiece);
            System.arraycopy(data, 2, dataPiece, 0, 2);
            modules[2].setPhysicalInput(dataPiece); // Get right analog in bottom-right next
            components.get(2).getStatesFromInput(dataPiece);
            dataPiece[0] = data[4]; // Get left trigger next
            dataPiece[1] = 0;
            modules[4].setPhysicalInput(dataPiece);
            dataPiece[0] = data[5]; // Get right trigger next
            modules[5].setPhysicalInput(dataPiece);
            dataPiece[0] = data[6]; // Get DPad next
            modules[0].setPhysicalInput(dataPiece);
            components.getFirst().getStatesFromInput(dataPiece);
            dataPiece[0] = data[7]; // Get "ABXY" next
            modules[1].setPhysicalInput(dataPiece);
            components.get(1).getStatesFromInput(dataPiece);
        }
    }

    public void pause() {
        isRunning = false;
    }

    public void resume() {
        isRunning = true;
    }

    // Runnable implementations
    @Override
    public void run() {
        while(isRunning) {
            interpret();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
