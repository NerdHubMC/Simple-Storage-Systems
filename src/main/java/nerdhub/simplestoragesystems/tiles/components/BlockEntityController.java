package nerdhub.simplestoragesystems.tiles.components;

import abused_master.energy.EnergyStorage;
import abused_master.energy.IEnergyReceiver;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityEnergyBase;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class BlockEntityController extends BlockEntityEnergyBase implements IEnergyReceiver {

    public EnergyStorage storage = new EnergyStorage(100000);
    public Set<BlockPos> storageBayPositions = new HashSet<>();
    public Set<BlockPos> terminalPositions = new HashSet<>();

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
            for (Tag tag1 : tags) {
                storageBayPositions.add(TagHelper.deserializeBlockPos((CompoundTag) tag1));
            }
        }

        if(tag.containsKey("terminalPositions")) {
            terminalPositions.clear();
            ListTag tags = tag.getList("terminalPositions", NbtType.COMPOUND);
            for (Tag tag1 : tags) {
                terminalPositions.add(TagHelper.deserializeBlockPos((CompoundTag) tag1));
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

        return tag;
    }

    @Override
    public void tick() {
        receiveEnergy(1);
    }

    @Override
    public boolean receiveEnergy(int amount) {
        return handleEnergyReceive(storage, amount);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return storage;
    }
}
