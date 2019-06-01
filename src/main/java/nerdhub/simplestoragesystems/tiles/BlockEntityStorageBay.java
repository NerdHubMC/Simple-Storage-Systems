package nerdhub.simplestoragesystems.tiles;

import abused_master.abusedlib.tiles.BlockEntityBase;
import nerdhub.simplestoragesystems.api.item.CustomStorageStack;
import nerdhub.simplestoragesystems.api.item.ICustomStorageStack;
import nerdhub.simplestoragesystems.api.network.EnumComponentTypes;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.items.ItemStorageCell;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockEntityStorageBay extends BlockEntityBase implements SidedInventory, INetworkComponent {

    public DefaultedList<ItemStack> inventory = DefaultedList.create(10, ItemStack.EMPTY);
    //Cache items once terminal is opened for the first time or items are removed/added
    private List<ICustomStorageStack> cachedStorageList = null;
    public BlockPos controllerPos = null;

    public BlockEntityStorageBay() {
        super(ModBlockEntities.STORAGE_BAY);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        inventory = DefaultedList.create(10, ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);

        if(tag.containsKey("controllerPos")) {
            controllerPos = TagHelper.deserializeBlockPos(tag.getCompound("controllerPos"));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);

        if(controllerPos != null) {
            tag.put("controllerPos", TagHelper.serializeBlockPos(controllerPos));
        }

        return tag;
    }

    public List<ICustomStorageStack> getCachedStorageList() {
        if(this.cachedStorageList == null || this.cachedStorageList.size() <= 0) {
            cacheStoredItems();
        }

        return cachedStorageList;
    }

    public void cacheStoredItems() {
        this.cachedStorageList = new ArrayList<>();
        this.cachedStorageList.clear();

        for (int i : getInvAvailableSlots(null)) {
            ItemStack cellStack = inventory.get(i);
            if(!cellStack.isEmpty() && cellStack.getTag() != null && cellStack.getTag().containsKey("data")) {
                ListTag dataList = cellStack.getTag().getList("data", NbtType.COMPOUND);

                for (Tag tag : dataList) {
                    this.cachedStorageList.add(new CustomStorageStack(ItemStack.fromTag((CompoundTag) tag)));
                }
            }
        }
    }

    public void cacheItem(ItemStack stack) {
        this.cachedStorageList.add(new CustomStorageStack(stack));
    }

    public int extractItem(ItemStack stack, int amount) {
        int extracted = 0;

        for (int i : getInvAvailableSlots(null)) {
            ItemStack cellStack = inventory.get(i);
            if (cellStack.isEmpty() || !(cellStack.getItem() instanceof ItemStorageCell) || cellStack.getTag() == null) {
                return extracted;
            }

            CompoundTag compoundTag = cellStack.getTag();
            ListTag dataList = compoundTag.getList("data", NbtType.COMPOUND);

            for (Iterator<Tag> it = dataList.iterator(); it.hasNext(); ) {
                Tag tag = it.next();
                ItemStack storedStack = ItemStack.fromTag((CompoundTag) tag);
                if (storedStack.getItem() == stack.getItem()) {
                    it.remove();
                    if (storedStack.getAmount() > amount) {
                        extracted = amount;
                        dataList.add(new ItemStack(storedStack.getItem(), storedStack.getAmount() - amount).toTag(new CompoundTag()));
                    } else {
                        extracted = storedStack.getAmount();
                    }
                }

                compoundTag.put("data", dataList);
                cellStack.setTag(compoundTag);
                inventory.set(i, cellStack);
                this.cacheStoredItems();
                return extracted;
            }
        }

        return extracted;
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
                    this.cacheItem(stack);

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
        return  Inventories.splitStack(inventory, i, i1);
    }

    @Override
    public ItemStack removeInvStack(int i) {
        return Inventories.removeStack(inventory, i);
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
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public EnumComponentTypes getComponentType() {
        return EnumComponentTypes.STORAGE_BAY;
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
