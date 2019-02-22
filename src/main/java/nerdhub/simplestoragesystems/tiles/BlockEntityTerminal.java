package nerdhub.simplestoragesystems.tiles;

import abused_master.abusedlib.blocks.multipart.IMultipart;
import abused_master.abusedlib.tiles.BlockEntityBase;
import nerdhub.simplestoragesystems.api.network.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.api.util.EnumUsageType;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockEntityTerminal extends BlockEntityBase implements INetworkComponent, IMultipart {

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

    @Override
    public BlockEntity getMultipartEntity() {
        return this;
    }

    @Override
    public void onMultipartActivated(BlockPos pos, Direction direction, PlayerEntity playerEntity, Hand hand, ItemStack stack) {
        if (world != null && !world.isClient) {
            if(world.getBlockEntity(pos) instanceof BlockEntityWirelessPoint) {
                BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(pos);
                if (point.isLinked()) {
                    point.getControllerEntity().cacheStorageBayLists();

                    if (point.getControllerEntity().storage.getEnergyStored() >= EnumUsageType.OPEN.getUsageAmount()) {
                        ContainerProviderRegistry.INSTANCE.openContainer(ModBlockEntities.TERMINAL_CONTAINER, playerEntity, buf -> {
                            buf.writeBlockPos(pos);
                            buf.writeBlockPos(point.getControllerPos());
                        });
                    }
                }
            }
        }
    }
}
