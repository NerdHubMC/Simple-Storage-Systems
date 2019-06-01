package nerdhub.simplestoragesystems.tiles;

import abused_master.abusedlib.tiles.BlockEntityBase;
import nerdhub.cardinalenergy.api.IEnergyHandler;
import nerdhub.cardinalenergy.impl.EnergyStorage;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.item.ICustomStorageStack;
import nerdhub.simplestoragesystems.api.network.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.network.ILinkerComponent;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityController extends BlockEntityBase implements IEnergyHandler, ILinkerComponent, INetworkComponent {

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
        storage.readEnergyFromTag(tag);

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
        storage.writeEnergyToTag(tag);

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
        storage.receiveEnergy(1000);

        int energyUsage = (terminalPositions.size() + storageBayPositions.size()) * SimpleStorageSystems.config.getInt("controller-idle");
        if(energyUsage > 0 && storage.getEnergyStored() >= energyUsage) {
            storage.extractEnergy(energyUsage);
        }
    }

    public boolean storeStack(ItemStack stack, boolean simulate) {
        for (BlockPos entityPos : storageBayPositions) {
            if(world.getBlockEntity(entityPos) instanceof BlockEntityStorageBay) {
                BlockEntityStorageBay storageBay = (BlockEntityStorageBay) world.getBlockEntity(entityPos);
                boolean stored = storageBay.storeStack(stack, simulate);
                if (!stored) {
                    continue;
                }

                return stored;
            }
        }

        return false;
    }

    public void extractFromSystem(ItemStack stack, int amount) {
        int extractionAmount = amount;

        for (BlockPos entityPos : storageBayPositions) {
            BlockEntityStorageBay storageBay = (BlockEntityStorageBay) world.getBlockEntity(entityPos);
            extractionAmount = amount - storageBay.extractItem(stack, extractionAmount);
            if(extractionAmount <= 0) {
                break;
            }
        }
    }

    public List<ICustomStorageStack> getStoredStacks() {
        List<ICustomStorageStack> list = new ArrayList<>();

        for (BlockPos entityPos : storageBayPositions) {
            BlockEntityStorageBay storageBay = (BlockEntityStorageBay) world.getBlockEntity(entityPos);
            list.addAll(storageBay.getCachedStorageList());
        }

        return list;
    }

    public void cacheStorageBayLists() {
        for (BlockPos entityPos : storageBayPositions) {
            if(world.getBlockEntity(entityPos) instanceof BlockEntityStorageBay) {
                BlockEntityStorageBay storageBay = (BlockEntityStorageBay) world.getBlockEntity(entityPos);
                storageBay.cacheStoredItems();
            }
        }
    }

    public void addComponent(INetworkComponent component, BlockPos entityPos) {
        EnumComponentTypes type = component.getComponentType();
        switch (type) {
            case TERMINAL:
                if(!terminalPositions.contains(entityPos))
                    this.terminalPositions.add(entityPos);
                this.updateEntity();
                break;
            case CRAFTING_TERMINAL:
                //TODO ADD THIS
                this.updateEntity();
                break;
            case STORAGE_BAY:
                if(!storageBayPositions.contains(entityPos))
                    this.storageBayPositions.add(entityPos);
                this.updateEntity();
                break;
            case AUTO_CRAFTING_INTERFACE:
                //TODO ADD THIS
                this.updateEntity();
                break;
            case AUTO_CRAFTING_PROCESSOR:
                //TODO ADD THIS
                this.updateEntity();
                break;
            default:
                this.updateEntity();
                break;
        }
    }

    public void removeComponent(INetworkComponent component, BlockPos entityPos) {
        EnumComponentTypes type = component.getComponentType();
        switch (type) {
            case TERMINAL:
                this.terminalPositions.remove(entityPos);
                this.updateEntity();
                break;
            case CRAFTING_TERMINAL:
                //TODO ADD THIS
                this.updateEntity();
                break;
            case STORAGE_BAY:
                this.storageBayPositions.remove(entityPos);
                this.updateEntity();
                break;
            case AUTO_CRAFTING_INTERFACE:
                //TODO ADD THIS
                this.updateEntity();
                break;
            case AUTO_CRAFTING_PROCESSOR:
                //TODO ADD THIS
                this.updateEntity();
                break;
            default:
                this.updateEntity();
                break;
        }
    }

    @Override
    public void link(PlayerEntity player, CompoundTag tag) {
        if(tag.containsKey("wirelessPointPos")) {
            BlockPos componentPos = TagHelper.deserializeBlockPos(tag.getCompound("wirelessPointPos"));
            if(!wirelessPointPositions.contains(componentPos)) {
                BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(componentPos);
                this.wirelessPointPositions.add(componentPos);
                point.setControllerPos(pos);

                player.addChatMessage(new TextComponent("Successfully linked component!"), true);
            }else {
                player.addChatMessage(new TextComponent("Component is already linked!"), true);
            }
        }else {
            player.addChatMessage(new TextComponent("Linker does not contain component positions!"), true);
        }

        this.updateEntity();
    }

    @Override
    public EnumComponentTypes getComponentType() {
        return EnumComponentTypes.CONTROLLER;
    }

    @Override
    public void setControllerPos(BlockPos pos) {
    }

    @Override
    public BlockEntityController getControllerEntity() {
        return this;
    }
}
