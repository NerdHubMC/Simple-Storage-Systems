package nerdhub.simplestoragesystems.utils;

import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ComponentHelper {

    public static void linkComponent(IWorld world, INetworkComponent component, BlockPos controllerPos) {
        BlockEntityController controller = (BlockEntityController) world.getBlockEntity(controllerPos);
        if(controller != null && component != null && component.getComponentType().isLinkable()) {
            component.setControllerPos(controllerPos);
        }
    }

    public static void removeComponentLinks(INetworkComponent component) {
        component.setControllerPos(null);
    }
}
