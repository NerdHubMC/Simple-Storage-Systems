package nerdhub.simplestoragesystems.tiles.components;

import io.netty.buffer.Unpooled;
import nerdhub.simplestoragesystems.api.ILinkerComponent;
import nerdhub.simplestoragesystems.blocks.components.BlockWirelessPoint;
import nerdhub.simplestoragesystems.network.ModPackets;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityBase;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.packet.CustomPayloadServerPacket;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockEntityWirelessPoint extends BlockEntityBase implements ILinkerComponent {

    private BlockPos controllerPos;
    public List<BlockPos> connectedComponents = new ArrayList<>();

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

        return tag;
    }

    @Override
    public void tick() {
        if(controllerPos != null && !connectedComponents.isEmpty()) {
            BlockEntityController controller = (BlockEntityController) world.getBlockEntity(controllerPos);
            for (Iterator<BlockPos> it = connectedComponents.iterator(); it.hasNext();) {
                BlockPos blockPos = it.next();
                controller.addComponent(world.getBlockEntity(blockPos), blockPos);
                it.remove();
            }
        }
    }

    public void addComponent(BlockPos entity) {
        this.connectedComponents.add(entity);
        this.updateEntity();
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;

        for (BlockPos entityPos : BlockWirelessPoint.getAdjacentTiles(world, pos)) {
            world.sendPacket(new CustomPayloadServerPacket(ModPackets.PACKET_LINK_COMPONENTS, new PacketByteBuf(Unpooled.buffer()).writeBlockPos(controllerPos).writeCompoundTag(TagHelper.serializeBlockPos(pos))));
            this.addComponent(entityPos);
        }
        this.updateEntity();
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
