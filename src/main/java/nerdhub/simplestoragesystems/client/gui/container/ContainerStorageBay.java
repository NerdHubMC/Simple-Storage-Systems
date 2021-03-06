package nerdhub.simplestoragesystems.client.gui.container;

import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerStorageBay extends Container {

    public final Inventory inventory;
    public final PlayerInventory playerInventory;
    public final World world;

    public ContainerStorageBay(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(null, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.world;

        //Drive Slots
        int j;
        for(j = 0; j < 2; ++j) {
            for(int var4 = 0; var4 < 5; ++var4) {
                if (j == 1) {
                    this.addSlot(new Slot(inventory, var4, 36 + var4 * 22, 20));
                } else {
                    this.addSlot(new Slot(inventory, 5 + var4, 36 + var4 * 22, 42));
                }
            }
        }

        //Vanilla Player Slots
        int i;
        for(i = 0; i < 3; ++i) {
            for(int var4 = 0; var4 < 9; ++var4) {
                this.addSlot(new Slot(playerInventory, var4 + i * 9 + 9, 8 + var4 * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity playerEntity_1, int int_1) {
        return ItemStack.EMPTY;
    }
}
