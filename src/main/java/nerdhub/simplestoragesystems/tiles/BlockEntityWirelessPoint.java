package nerdhub.simplestoragesystems.tiles;

import abused_master.abusedlib.blocks.multipart.IMultipart;
import abused_master.abusedlib.utils.MultipartHelper;
import abused_master.abusedlib.tiles.BlockEntityBase;
import com.google.common.collect.Maps;
import nerdhub.simplestoragesystems.api.network.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.network.ILinkerComponent;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.utils.ComponentHelper;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BlockEntityWirelessPoint extends BlockEntityBase implements ILinkerComponent, INetworkComponent {

    private BlockPos controllerPos = null;
    public List<BlockPos> connectedComponents = new ArrayList<>();
    public Map<Direction, IMultipart> terminalParts = Maps.newHashMap();

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

        if(tag.containsKey("terminalParts")) {
            this.terminalParts.clear();
            ListTag listTag = tag.getList("terminalParts", NbtType.COMPOUND);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag dataTag = listTag.getCompoundTag(i);
                Direction direction = Direction.byId(dataTag.getInt("direction"));
                IMultipart multipart = MultipartHelper.deserialize(dataTag);
                this.terminalParts.put(direction, multipart);
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

        if(!terminalParts.isEmpty()) {
            ListTag listTag = new ListTag();

            for (Direction direction : terminalParts.keySet()) {
                CompoundTag dataTag = new CompoundTag();
                dataTag.putInt("direction", direction.ordinal());
                MultipartHelper.serialize(terminalParts.get(direction), dataTag);
                listTag.add(dataTag);
            }

            tag.put("terminalParts", listTag);
        }

        return tag;
    }

    @Override
    public void tick() {
        if(getControllerEntity() != null && !connectedComponents.isEmpty()) {
            for (Iterator<BlockPos> it = connectedComponents.iterator(); it.hasNext();) {
                BlockPos blockPos = it.next();
                getControllerEntity().addComponent((INetworkComponent) world.getBlockEntity(blockPos), blockPos);
                it.remove();
            }
        }
    }

    public void addTerminalMultipart(Direction direction) {
        BlockEntityTerminal terminal = new BlockEntityTerminal();
        terminal.setWorld(world);
        this.terminalParts.put(direction, terminal);
        this.updateEntity();
    }

    public void addComponent(BlockPos entity) {
        this.connectedComponents.add(entity);
        this.updateEntity();
    }

    @Override
    public EnumComponentTypes getComponentType() {
        return EnumComponentTypes.WIRELESS_POINT;
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
        ComponentHelper.linkComponents(world, pos);
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
