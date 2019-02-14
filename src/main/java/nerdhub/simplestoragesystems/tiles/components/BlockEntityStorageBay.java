package nerdhub.simplestoragesystems.tiles.components;

import nerdhub.simplestoragesystems.api.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.items.ItemStorageCell;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityBase;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.InventoryUtil;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEntityStorageBay extends BlockEntityBase implements SidedInventory, INetworkComponent {

    public DefaultedList<ItemStack> inventory = DefaultedList.create(10, ItemStack.EMPTY);
    public boolean isLinked = false;
    public BlockPos controllerPos = null;

    public BlockEntityStorageBay() {
        super(ModBlockEntities.STORAGE_BAY);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        inventory = DefaultedList.create(10, ItemStack.EMPTY);
        InventoryUtil.deserialize(tag, this.inventory);
        isLinked = tag.getBoolean("isLinked");

        if(tag.containsKey("controllerPos")) {
            controllerPos = TagHelper.deserializeBlockPos(tag.getCompound("controllerPos"));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        InventoryUtil.serialize(tag, this.inventory);
        tag.putBoolean("isLinked", this.isLinked);

        if(controllerPos != null) {
            tag.put("controllerPos", TagHelper.serializeBlockPos(controllerPos));
        }
        return tag;
    }

    public List<ItemStack> getStoredItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i : getInvAvailableSlots(null)) {
            ItemStack cellStack = inventory.get(i);
            if(!cellStack.isEmpty() && cellStack.getTag() != null && cellStack.getTag().containsKey("data")) {
                ListTag dataList = cellStack.getTag().getList("data", NbtType.COMPOUND);

                for (Tag tag : dataList) {
                    items.add(ItemStack.fromTag((CompoundTag) tag));
                }
            }
        }

        return items;
    }

    public int getStoredItemsCount() {
        int count = 0;
        for (int i : getInvAvailableSlots(null)) {
            ItemStack inventoryStack = inventory.get(i);
            if(!inventoryStack.isEmpty() && inventoryStack.getTag() != null && inventoryStack.getTag().containsKey("amount")) {
                count += inventoryStack.getTag().getInt("amount");
            }
        }

        return count;
    }

    public boolean storeStack(ItemStack stack, boolean simulate) {
        for (int i : getInvAvailableSlots(null)) {
            if(canStoreInCell(i, stack)) {
                if(!simulate)
                    this.writeStackToCell(i, stack);

                return true;
            }
        }

        return false;
    }

    private void writeStackToCell(int slot, ItemStack stack) {
        ItemStack cellStack = inventory.get(slot);

        CompoundTag tag = cellStack.getTag();
        ListTag dataList = tag.getList("data", NbtType.COMPOUND);

        tag.putInt("amount", tag.getInt("amount") + stack.getAmount());
        dataList.add(getStackTag(stack));
        tag.put("data", dataList);

        cellStack.setTag(tag);
        inventory.set(slot, cellStack);
    }

    private boolean canStoreInCell(int slot, ItemStack stack) {
        ItemStack cellStack = inventory.get(slot);
        if(cellStack.isEmpty() || !(cellStack.getItem() instanceof ItemStorageCell)) {
            return false;
        }else if(cellStack.getTag() != null) {
            if(cellStack.getTag().containsKey("amount") && (cellStack.getTag().getInt("amount") + stack.getAmount()) > ((ItemStorageCell) cellStack.getItem()).getType().getStorageCapacity()) {
                return false;
            }
        }

        if(cellStack.getTag() == null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("amount", 0);
            tag.put("data", new ListTag());
            cellStack.setTag(tag);
            inventory.set(slot, cellStack);
            this.updateEntity();
        }

        return true;
    }

    private CompoundTag getStackTag(ItemStack stack) {
        return stack.toTag(new CompoundTag());
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

    @Override
    public EnumComponentTypes getComponentType() {
        return EnumComponentTypes.STORAGE_BAY;
    }

    @Override
    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
        this.updateEntity();
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
}
