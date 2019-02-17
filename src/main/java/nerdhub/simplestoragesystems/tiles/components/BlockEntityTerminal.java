package nerdhub.simplestoragesystems.tiles.components;

import abused_master.abusedlib.tiles.BlockEntityBase;
import nerdhub.simplestoragesystems.api.network.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

public class BlockEntityTerminal extends BlockEntityBase implements INetworkComponent {

    public BlockPos controllerPos;

    public BlockEntityTerminal() {
        super(ModBlockEntities.TERMINAL);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        if(tag.containsKey("controllerPos")) {
            controllerPos = TagHelper.deserializeBlockPos(tag.getCompound("controllerPos"));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if(controllerPos != null) {
            tag.put("controllerPos", TagHelper.serializeBlockPos(controllerPos));
        }
        return tag;
    }

    @Override
    public void tick() {
    }

    @Override
    public EnumComponentTypes getComponentType() {
        return EnumComponentTypes.TERMINAL;
    }

    @Override
    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
        this.updateEntity();
    }

    @Override
    public BlockEntityController getControllerEntity() {
        if(controllerPos != null && world.getBlockEntity(controllerPos) instanceof BlockEntityController) {
            return (BlockEntityController) world.getBlockEntity(controllerPos);
        }

        return null;
    }
}
