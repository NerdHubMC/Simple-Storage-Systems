package nerdhub.simplestoragesystems.tiles.components;

import nerdhub.simplestoragesystems.items.ItemStorageCell;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityBase;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.InventoryUtil;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEntityStorageBay extends BlockEntityBase implements SidedInventory {

    public DefaultedList<ItemStack> inventory = DefaultedList.create(10, ItemStack.EMPTY);

    public BlockEntityStorageBay() {
        super(ModBlockEntities.STORAGE_BAY);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        inventory = DefaultedList.create(10, ItemStack.EMPTY);
        InventoryUtil.deserialize(tag, this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        InventoryUtil.serialize(tag, this.inventory);
        return tag;
    }

    public List<ItemStack> getStoredItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i : getInvAvailableSlots(null)) {
            ItemStack inventoryStack = inventory.get(i);
            if(!inventoryStack.isEmpty() && inventoryStack.getTag() != null && inventoryStack.getTag().containsKey("data")) {
                ListTag dataList = inventoryStack.getTag().getList("data", NbtType.COMPOUND);
                for (Tag tag : dataList) {
                    items.add(new ItemStack((ItemProvider) tag));
                }
            }
        }

        return items;
    }

    public int getStoredItemsCount() {
        int count = 0;
        for (int i : getInvAvailableSlots(null)) {
            ItemStack inventoryStack = inventory.get(i);
            if(!inventoryStack.isEmpty() && inventoryStack.getTag() != null && inventoryStack.getTag().containsKey("stored")) {
                count += inventoryStack.getTag().getInt("stored");
            }
        }

        return count;
    }

    public boolean storeStack(ItemStack stack) {
        for (int i : getInvAvailableSlots(null)) {
            if(canStoreInCell(i, stack)) {
                this.writeStackToCell(i, stack);
                return true;
            }
        }

        return false;
    }

    private void writeStackToCell(int slot, ItemStack stack) {
        ItemStack inventoryStack = inventory.get(slot);

        CompoundTag tag = inventoryStack.getTag();
        ListTag dataList = tag.getList("data", NbtType.COMPOUND);

        tag.putInt("stored", tag.getInt("stored") + stack.getAmount());
        dataList.add(getStackTag(stack));
        tag.put("data", dataList);

        inventoryStack.setTag(tag);
        inventory.set(slot, inventoryStack);
    }

    private boolean canStoreInCell(int slot, ItemStack stack) {
        ItemStack inventoryStack = inventory.get(slot);
        if(inventoryStack.isEmpty() || !(inventoryStack.getItem() instanceof ItemStorageCell)) {
            return false;
        }else if(inventoryStack.getTag().containsKey("stored") && inventoryStack.getTag().getInt("stored") > ((ItemStorageCell) inventoryStack.getItem()).getType().getStorageCapacity()) {
            return false;
        }

        if(inventoryStack.getTag() == null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("stored", 0);
            tag.put("data", new ListTag());
            inventoryStack.setTag(tag);
            inventory.set(slot, inventoryStack);
        }

        return true;
    }

    private CompoundTag getStackTag(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        Identifier identifier_1 = Registry.ITEM.getId(stack.getItem());
        tag.putString("id", identifier_1 == null ? "minecraft:air" : identifier_1.toString());
        tag.putByte("Count", (byte) stack.getAmount());
        if(stack.getTag() != null) {
            tag.put("tag", stack.getTag());
        }

        return tag;
    }

    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
        if(inventory.get(i).isEmpty() && itemStack.getItem() instanceof ItemStorageCell) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
        if(!inventory.get(i).isEmpty()) {
            return true;
        }

        return false;
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
        return  InventoryUtil.splitStack(inventory, i, i1);
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
}
