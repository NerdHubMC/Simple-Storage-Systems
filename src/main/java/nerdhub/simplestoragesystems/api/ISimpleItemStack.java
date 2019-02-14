package nerdhub.simplestoragesystems.api;

import nerdhub.simplestoragesystems.client.gui.gui.ContainerGuiBase;
import net.minecraft.item.ItemStack;

public interface ISimpleItemStack {

    ItemStack getStack();

    String getName();

    String getModid();

    String getModName();

    int getSize();

    String getFormattedAmount();

    void draw(ContainerGuiBase gui, int x, int y);

    boolean isCraftingObject();

    void setIsCrafingObject(boolean object);

    void addAmount(int amount);

    void setAmount(int amount);

    int getAmount();
}
