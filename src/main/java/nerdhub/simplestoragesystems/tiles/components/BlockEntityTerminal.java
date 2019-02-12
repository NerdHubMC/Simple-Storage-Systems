package nerdhub.simplestoragesystems.tiles.components;

import nerdhub.simplestoragesystems.api.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.InventoryUtil;

public class BlockEntityTerminal extends BlockEntityBase implements Inventory, INetworkComponent {

    public DefaultedList<ItemStack> inventory = DefaultedList.create(27, ItemStack.EMPTY);

    public BlockEntityTerminal() {
        super(ModBlockEntities.TERMINAL);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        inventory = DefaultedList.create(27, ItemStack.EMPTY);
        InventoryUtil.deserialize(tag, inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        InventoryUtil.serialize(tag, inventory);
        return tag;
    }

    @Override
    public void tick() {
    }

    @Override
    public int getInvSize() {
        return inventory.size();
    }

    @Override
    public boolean isInvEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getInvStack(int i) {
        return inventory.get(i);
    }

    @Override
    public ItemStack takeInvStack(int i, int i1) {
        return InventoryUtil.splitStack(inventory, i, i1);
    }

    @Override
    public ItemStack removeInvStack(int i) {
        return InventoryUtil.removeStack(inventory, i);
    }

    @Override
    public void setInvStack(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public void clearInv() {
        inventory.clear();
    }

    @Override
    public EnumComponentTypes getComponentType() {
        return EnumComponentTypes.TERMINAL;
    }
}
