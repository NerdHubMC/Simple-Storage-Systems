package nerdhub.simplestoragesystems.tiles.components;

import nerdhub.simplestoragesystems.api.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.ILinkerComponent;
import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.blocks.components.BlockWirelessPoint;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityBase;
import nerdhub.simplestoragesystems.utils.ComponentHelper;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockEntityWirelessPoint extends BlockEntityBase implements ILinkerComponent, INetworkComponent {

    private BlockPos controllerPos;
    public List<BlockPos> connectedComponents = new ArrayList<>();
    public boolean isLinked;

    public BlockEntityWirelessPoint() {
        super(ModBlockEntities.WIRELESS_POINT);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        if(tag.containsKey("controllerPos")) {
            this.controllerPos = TagHelper.deserializeBlockPos(tag.getCompound("controllerPos"));
        }

        if(tag.containsKey("connectedComponents")) {
            connectedComponents.clear();
            ListTag tags = tag.getList("connectedComponents", NbtType.COMPOUND);
            for (Tag posTag : tags) {
                connectedComponents.add(TagHelper.deserializeBlockPos((CompoundTag) posTag));
            }
        }

        this.isLinked = tag.getBoolean("isLinked");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if(controllerPos != null) {
            tag.put("controllerPos", TagHelper.serializeBlockPos(controllerPos));
        }

        if(connectedComponents.size() > 0) {
            ListTag tags = new ListTag();
            for (BlockPos pos : connectedComponents) {
                tags.add(TagHelper.serializeBlockPos(pos));
            }

            tag.put("connectedComponents", tags);
        }

        tag.putBoolean("isLinked", isLinked);
        return tag;
    }

    @Override
    public void tick() {
        if(getControllerEntity() != null && !connectedComponents.isEmpty()) {
            for (Iterator<BlockPos> it = connectedComponents.iterator(); it.hasNext();) {
                BlockPos blockPos = it.next();
                getControllerEntity().addComponent(world.getBlockEntity(blockPos), blockPos);
                it.remove();
            }
        }
    }

    public void addComponent(BlockPos entity) {
        this.connectedComponents.add(entity);
        this.updateEntity();
    }

    @Override
    public EnumComponentTypes getComponentType() {
        return EnumComponentTypes.WIRELESS_POINT;
    }

    @Override
    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;

        for (BlockPos entityPos : BlockWirelessPoint.getAdjacentTiles(world, pos)) {
            if(world.getBlockEntity(entityPos) instanceof INetworkComponent) {
                ComponentHelper.linkComponent(world, (INetworkComponent) world.getBlockEntity(entityPos), controllerPos, true);
                this.addComponent(entityPos);
            }
        }

        this.updateEntity();
    }

    @Override
    public BlockEntityController getControllerEntity() {
        if(controllerPos != null && world.getBlockEntity(controllerPos) instanceof BlockEntityController) {
            return (BlockEntityController) world.getBlockEntity(controllerPos);
        }

        return null;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        if(tag.containsKey("wirelessPointPos")) {
            tag.remove("wirelessPointPos");
        }

        tag.put("wirelessPointPos", TagHelper.serializeBlockPos(pos));
        player.addChatMessage(new StringTextComponent("Saved component position!"), true);
    }
}
