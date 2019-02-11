package nerdhub.simplestoragesystems.tiles;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.network.packet.BlockEntityUpdateClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public abstract class BlockEntityBase extends BlockEntity implements Tickable, BlockEntityClientSerializable {

    public BlockEntityBase(BlockEntityType<?> blockEntityType_1) {
        super(blockEntityType_1);
    }

    @Override
    public void tick() {
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return this.toTag(tag);
    }

    @Override
    public BlockEntityUpdateClientPacket toUpdatePacket() {
        return super.toUpdatePacket();
    }

    public void updateEntity() {
        this.markDirty();
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }
}