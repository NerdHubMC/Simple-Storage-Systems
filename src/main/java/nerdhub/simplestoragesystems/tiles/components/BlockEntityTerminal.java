package nerdhub.simplestoragesystems.tiles.components;

import nerdhub.simplestoragesystems.api.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

public class BlockEntityTerminal extends BlockEntityBase implements INetworkComponent {

    public boolean isLinked = false;
    public BlockPos controllerPos;

    public BlockEntityTerminal() {
        super(ModBlockEntities.TERMINAL);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        isLinked = tag.getBoolean("isLinked");

        if(tag.containsKey("controllerPos")) {
            controllerPos = TagHelper.deserializeBlockPos(tag.getCompound("controllerPos"));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putBoolean("isLinked", isLinked);
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
    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
        this.updateEntity();
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
