package nerdhub.simplestoragesystems.api.network;

import nerdhub.simplestoragesystems.tiles.BlockEntityController;
import net.minecraft.util.math.BlockPos;

public interface INetworkComponent {

    EnumComponentTypes getComponentType();

    void setControllerPos(BlockPos pos);

    BlockEntityController getControllerEntity();

    default boolean isLinked() {
        return this.getControllerEntity() != null;
    }
}
