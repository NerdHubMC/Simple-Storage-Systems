package nerdhub.simplestoragesystems.utils;

import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ComponentHelper {

    public static void linkComponent(IWorld world, INetworkComponent component, BlockPos controllerPos, boolean linked) {
        BlockEntityController controller = (BlockEntityController) world.getBlockEntity(controllerPos);
        if(controller != null && component != null && component.getComponentType().isLinkable()) {
            component.setControllerPos(controllerPos);
            component.setIsLinked(linked);
        }
    }

    public static void removeComponentLinks(IWorld world, INetworkComponent component) {
        component.setControllerPos(null);
        component.setIsLinked(false);
    }
}
