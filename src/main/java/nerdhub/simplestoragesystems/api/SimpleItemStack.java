package nerdhub.simplestoragesystems.api;

import nerdhub.simplestoragesystems.client.gui.gui.ContainerGuiBase;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.ModContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SimpleItemStack implements ISimpleItemStack {

    private DecimalFormat formatterWithUnits = new DecimalFormat("####0.#", DecimalFormatSymbols.getInstance(Locale.US));
    private DecimalFormat formatter = new DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale.US));
    private ItemStack stack;
    private String name;
    private boolean craftingObject;
    private String modId;
    private String modName;
    private int size;

    public SimpleItemStack(ItemStack stack) {
        formatterWithUnits.setRoundingMode(RoundingMode.DOWN);
        this.stack = stack;
        this.size = stack.getAmount();
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public String getName() {
        if(name == null) {
            name = stack.getDisplayName().getFormattedText();
        }

        return name;
    }

    @Override
    public String getModid() {
        if(modId == null) {
            modId = Registry.ITEM.getId(stack.getItem()).getNamespace();

            if(modId == null) {
                modId = "null";
            }
        }

        return modId;
    }

    @Override
    public String getModName() {
        if(modName == null) {
            modName = getModName(getModid());

            if(modName == null) {
                modName = "null";
            }
        }

        return modName;
    }

    @Override
    public int getSize() {
        return isCraftingObject() ? 0 : size;
    }

    @Override
    public String getFormattedAmount() {
        return formatter.format(getAmount());
    }

    @Override
    public void draw(ContainerGuiBase gui, int x, int y) {
        String text = null;

        if(size > 1) {
            text = formatWithUnits((long) getSize());
        }

        gui.drawItem(x, y, stack, text);
    }

    @Override
    public boolean isCraftingObject() {
        return craftingObject;
    }

    @Override
    public void setIsCrafingObject(boolean object) {
        this.craftingObject = object;

        if(object) {
            this.stack.setAmount(1);
        }
    }

    @Override
    public void addAmount(int amount) {
        this.size += amount;
    }

    @Override
    public void setAmount(int amount) {
        this.size = amount;
    }

    @Override
    public int getAmount() {
        return size;
    }

    public static String getModName(String modid) {
        ModContainer container = FabricLoader.INSTANCE.getModContainers().stream()
                .filter(m -> m.getInfo().getId().equals(modid))
                .findFirst()
                .orElse(null);

        return container == null ? null : container.getInfo().getName();
    }

    public String formatWithUnits(long qty) {
        if (qty >= 1_000_000) {
            float qtyShort = (float) qty / 1_000_000F;

            if (qty >= 100_000_000) {
                qtyShort = Math.round(qtyShort); // XXX.XM looks weird.
            }

            return formatterWithUnits.format(qtyShort) + "M";
        } else if (qty >= 1000) {
            float qtyShort = (float) qty / 1000F;

            if (qty >= 100_000) {
                qtyShort = Math.round(qtyShort); // XXX.XK looks weird.
            }

            return formatterWithUnits.format(qtyShort) + "K";
        }

        return String.valueOf(qty);
    }
}
