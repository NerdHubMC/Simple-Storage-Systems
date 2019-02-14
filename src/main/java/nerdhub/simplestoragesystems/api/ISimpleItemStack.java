package nerdhub.simplestoragesystems.api;

import nerdhub.simplestoragesystems.client.gui.gui.ContainerGuiBase;
import net.minecraft.item.ItemStack;

public interface ISimpleItemStack {

    ItemStack getStack();

    String getName();

    String getModid();

    String getModName();

    String getTooltip();

    int getSize();

    String getFormattedAmount();

    void draw(ContainerGuiBase gui, int x, int y);

    boolean doesDisplayText();

    void setDisplayText(boolean display);

    void addAmount(int amount);

    void setAmount(int amount);

    int getAmount();
}
