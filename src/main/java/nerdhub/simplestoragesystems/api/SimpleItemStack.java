package nerdhub.simplestoragesystems.api;

import io.netty.buffer.ByteBuf;
import nerdhub.simplestoragesystems.client.gui.gui.ContainerGuiBase;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipOptions;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SimpleItemStack implements ISimpleItemStack {

    private DecimalFormat formatterWithUnits = new DecimalFormat("####0.#", DecimalFormatSymbols.getInstance(Locale.US));
    private DecimalFormat formatter = new DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale.US));
    private int hash;
    private ItemStack stack;
    private String name;
    private boolean displayText;
    private String modId;
    private String modName;
    private String tooltip;

    public SimpleItemStack(ItemStack stack) {
        formatterWithUnits.setRoundingMode(RoundingMode.DOWN);
        this.stack = stack;
    }

    public SimpleItemStack(ByteBuf buf) {
        formatterWithUnits.setRoundingMode(RoundingMode.DOWN);
        this.stack = new ItemStack(Item.byRawId(buf.readInt()), buf.readInt());
        stack.setTag(new PacketByteBuf(buf).readCompoundTag());
        this.hash = buf.readInt();
        setDisplayText(buf.readBoolean());
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
    public String getTooltip() {
        if(tooltip == null) {
            tooltip = getItemTooltip(stack).stream().collect(Collectors.joining("\n"));
        }

        return tooltip;
    }

    @Override
    public int getSize() {
        return doesDisplayText() ? 0 : stack.getAmount();
    }

    @Override
    public String getFormattedAmount() {
        return formatter.format(getSize());
    }

    @Override
    public void draw(ContainerGuiBase gui, int x, int y) {
        String text = null;

        if(displayText) {
            text = I18n.translate("gui.simplestoragesystems:craft");
        }else if(stack.getAmount() > 1) {
            text = formatWithUnits(getSize());
        }

        gui.drawItem(x, y, stack, true, text);
    }

    @Override
    public boolean doesDisplayText() {
        return displayText;
    }

    @Override
    public void setDisplayText(boolean display) {
        this.displayText = display;

        if(display) {
            this.stack.setAmount(1);
        }
    }

    public static String getModName(String modid) {
        ModContainer container = FabricLoader.INSTANCE.getModContainers().stream()
                .filter(m -> m.getInfo().getId().equals(modid))
                .findFirst()
                .orElse(null);

        return container == null ? null : container.getInfo().getName();
    }

    public static List<String> getItemTooltip(ItemStack stack) {
        List<TextComponent> lines = stack.getTooltipText(MinecraftClient.getInstance().player, MinecraftClient.getInstance().options.advancedItemTooltips ? TooltipOptions.Instance.ADVANCED : TooltipOptions.Instance.NORMAL);
        List<String> stringLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); ++i) {
            if (i == 0) {
                stringLines.set(i, stack.getRarity().formatting.getColor() + lines.get(i).getText());
            } else {
                stringLines.set(i, TextFormat.GRAY + lines.get(i).getText());
            }
        }

        return stringLines;
    }

    public String formatWithUnits(int qty) {
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

    @Override
    public int getHash() {
        return hash;
    }
}
