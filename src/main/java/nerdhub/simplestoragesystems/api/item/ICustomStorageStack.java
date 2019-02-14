package nerdhub.simplestoragesystems.api.item;

import nerdhub.simplestoragesystems.client.gui.gui.ContainerGuiBase;
import net.minecraft.item.ItemStack;

public interface ICustomStorageStack {

    ItemStack getStack();

    String getName();

    String getModid();

    String getModName();

    String getFormattedAmount();

    void draw(ContainerGuiBase gui, int x, int y);

    boolean isCraftingObject();

    void setIsCrafingObject(boolean object);

    void addAmount(int amount);

    void setAmount(int amount);

    int getAmount();
}
