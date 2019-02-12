package nerdhub.simplestoragesystems.tiles.components;

import abused_master.energy.EnergyStorage;
import abused_master.energy.IEnergyReceiver;
import io.netty.buffer.Unpooled;
import nerdhub.simplestoragesystems.api.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.ILinkerComponent;
import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.network.ModPackets;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityEnergyBase;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.packet.CustomPayloadServerPacket;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityController extends BlockEntityEnergyBase implements IEnergyReceiver, ILinkerComponent {

    public EnergyStorage storage = new EnergyStorage(100000);
    public List<BlockPos> storageBayPositions = new ArrayList<>();
    public List<BlockPos> terminalPositions = new ArrayList<>();
    public List<BlockPos> wirelessPointPositions = new ArrayList<>();

    public BlockEntityController() {
        super(ModBlockEntities.CONTROLLER);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        storage.readFromNBT(tag);

        if(tag.containsKey("storageBayPositions")) {
            storageBayPositions.clear();
            ListTag tags = tag.getList("storageBayPositions", NbtType.COMPOUND);
            for (Tag pos : tags) {
                storageBayPositions.add(TagHelper.deserializeBlockPos((CompoundTag) pos));
            }
        }

        if(tag.containsKey("terminalPositions")) {
            terminalPositions.clear();
            ListTag tags = tag.getList("terminalPositions", NbtType.COMPOUND);
            for (Tag pos : tags) {
                terminalPositions.add(TagHelper.deserializeBlockPos((CompoundTag) pos));
            }
        }

        if(tag.containsKey("wirelessPointPositions")) {
            wirelessPointPositions.clear();
            ListTag tags = tag.getList("wirelessPointPositions", NbtType.COMPOUND);
            for (Tag pos : tags) {
                wirelessPointPositions.add(TagHelper.deserializeBlockPos((CompoundTag) pos));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        storage.writeEnergyToNBT(tag);

        if(storageBayPositions.size() > 0) {
            ListTag tags = new ListTag();
            for (BlockPos pos : storageBayPositions) {
                tags.add(TagHelper.serializeBlockPos(pos));
            }

            tag.put("storageBayPositions", tags);
        }

        if(terminalPositions.size() > 0) {
            ListTag tags = new ListTag();
            for (BlockPos pos : terminalPositions) {
                tags.add(TagHelper.serializeBlockPos(pos));
            }

            tag.put("terminalPositions", tags);
        }

        if(wirelessPointPositions.size() > 0) {
            ListTag tags = new ListTag();
            for (BlockPos pos : wirelessPointPositions) {
                tags.add(TagHelper.serializeBlockPos(pos));
            }

            tag.put("wirelessPointPositions", tags);
        }

        return tag;
    }

    @Override
    public void tick() {
        int energyUsage = (terminalPositions.size() + storageBayPositions.size()) * 5;
        if(energyUsage > 0 && storage.getEnergyStored() >= energyUsage) {
            storage.extractEnergy(energyUsage);
        }
    }

    public boolean storeStack(ItemStack stack, boolean simulate) {
        if(storageBayPositions.size() > 0) {
            for (BlockPos entity : storageBayPositions) {
                BlockEntityStorageBay storageBay = (BlockEntityStorageBay) world.getBlockEntity(entity);
                boolean stored = storageBay.storeStack(stack, simulate);
                if(!stored) {
                    continue;
                }

                return stored;
            }
        }

        return false;
    }

    public void addComponent(BlockEntity entity, BlockPos entityPos) {
        EnumComponentTypes type = ((INetworkComponent) entity).getComponentType();
        switch (type) {
            case TERMINAL:
                this.terminalPositions.add(entityPos);
                this.updateEntity();
                break;
            case CRAFTING_TERMINAL:
            case STORAGE_BAY:
                this.storageBayPositions.add(entityPos);
                this.updateEntity();
                break;
            case AUTO_CRAFTING_INTERFACE:
                this.updateEntity();
                break;
            case AUTO_CRAFTING_PROCESSOR:
                this.updateEntity();
                break;
            default:
                this.updateEntity();
                break;
        }
    }

    @Override
    public boolean receiveEnergy(int amount) {
        return handleEnergyReceive(storage, amount);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return storage;
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        if(tag.containsKey("wirelessPointPos")) {
            BlockPos componentPos = TagHelper.deserializeBlockPos(tag.getCompound("wirelessPointPos"));
            if(!wirelessPointPositions.contains(componentPos)) {
                BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(componentPos);
                this.wirelessPointPositions.add(componentPos);

                world.sendPacket(new CustomPayloadServerPacket(ModPackets.PACKET_LINK_POINT, new PacketByteBuf(Unpooled.buffer()).writeBlockPos(pos).writeCompoundTag(TagHelper.serializeBlockPos(componentPos))));
                for (BlockPos connectedComponents : point.connectedComponents) {
                    world.sendPacket(new CustomPayloadServerPacket(ModPackets.PACKET_LINK_COMPONENTS, new PacketByteBuf(Unpooled.buffer()).writeBlockPos(pos).writeCompoundTag(TagHelper.serializeBlockPos(connectedComponents))));
                }
                player.addChatMessage(new StringTextComponent("Successfully linked component!"), true);
            }else {
                player.addChatMessage(new StringTextComponent("Component is already linked!"), true);
            }
        }else {
            player.addChatMessage(new StringTextComponent("Linker does not contain component positions!"), true);
        }

        this.updateEntity();
    }
}