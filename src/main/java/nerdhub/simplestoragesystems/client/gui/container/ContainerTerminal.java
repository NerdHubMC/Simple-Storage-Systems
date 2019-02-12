package nerdhub.simplestoragesystems.client.gui.container;

import nerdhub.simplestoragesystems.tiles.components.BlockEntityTerminal;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerTerminal extends Container {

    public final PlayerInventory playerInventory;
    public final World world;
    public final BlockEntityTerminal terminal;

    public ContainerTerminal(int syncId, PlayerInventory playerInventory, BlockEntityTerminal terminal) {
        super(null, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.world;
        this.terminal = terminal;

        //Vanilla Player Slots
        int i;
        for(i = 0; i < 3; ++i) {
            for(int var4 = 0; var4 < 9; ++var4) {
                this.addSlot(new Slot(playerInventory, var4 + i * 9 + 9, 8 + var4 * 18, 111 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 169));
        }
    }

    @Override
    public boolean canUse(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack onSlotClick(int int_1, int int_2, SlotActionType slotActionType_1, PlayerEntity playerEntity_1) {
        return super.onSlotClick(int_1, int_2, slotActionType_1, playerEntity_1);
    }
}
