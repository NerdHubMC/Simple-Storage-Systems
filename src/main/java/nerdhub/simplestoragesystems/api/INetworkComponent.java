package nerdhub.simplestoragesystems.api;

import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import net.minecraft.util.math.BlockPos;

public interface INetworkComponent {

    EnumComponentTypes getComponentType();
    void setIsLinked(boolean isLinked);
    void setControllerPos(BlockPos pos);
    BlockEntityController getControllerEntity();
}
