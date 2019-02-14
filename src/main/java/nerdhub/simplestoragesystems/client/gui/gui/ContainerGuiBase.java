package nerdhub.simplestoragesystems.client.gui.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.api.ISimpleItemStack;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;

import java.util.List;

public abstract class ContainerGuiBase extends ContainerScreen {

    public ContainerGuiBase(Container container, PlayerInventory playerInventory, TextComponent textComponent) {
        super(container, playerInventory, textComponent);
    }

    public void drawItem(int x, int y, ItemStack stack, String text) {
        GlStateManager.pushMatrix();
        this.zOffset = 100.0f;
        this.itemRenderer.zOffset = 100.0f;
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepthTest();
        GuiLighting.enableForItems();
        itemRenderer.renderGuiItem(stack, x, y);
        itemRenderer.renderGuiItemOverlay(fontRenderer, stack, x, y, text);
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        this.itemRenderer.zOffset = 0.0f;
        this.zOffset = 0.0f;
        GlStateManager.popMatrix();
    }

    public void drawHoveringTooltip(ISimpleItemStack stack, int mouseX, int mouseY) {
        List<String> textLines = Lists.newArrayList(stack.getTooltip().split("\n"));
        List<String> smallTextLines = Lists.newArrayList();

        if(!stack.doesDisplayText()) {
            smallTextLines.add(I18n.translate(stack.getFormattedAmount()));
        }
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public boolean inBounds(int x, int y, int w, int h, int ox, int oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }
}
