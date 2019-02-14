package nerdhub.simplestoragesystems.items;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import net.minecraft.client.item.TooltipOptions;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStorageCell extends ItemBase {

    private EnumStorageCells type;

    public ItemStorageCell(EnumStorageCells type) {
        super(type.getName(), new Settings().itemGroup(SimpleStorageSystems.modItemGroup).stackSize(1));
        this.type = type;
    }

    public EnumStorageCells getType() {
        return type;
    }

    @Override
    public void buildTooltip(ItemStack stack, @Nullable World world, List<TextComponent> list, TooltipOptions tooltipOptions) {
        int dataUsed = stack.getTag() == null ? 0 : stack.getTag().getInt("amount");
        list.add(new StringTextComponent("Data Used: " + dataUsed + " / " + type.getStorageCapacity() + " bytes"));
    }
}
