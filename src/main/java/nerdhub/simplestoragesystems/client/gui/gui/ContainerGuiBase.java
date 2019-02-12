package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;

public abstract class ContainerGuiBase extends ContainerScreen {

    public ContainerGuiBase(Container container, PlayerInventory playerInventory, TextComponent textComponent) {
        super(container, playerInventory, textComponent);
    }

    public void drawItem(int x, int y, ItemStack stack) {
        drawItem(x, y, stack, false);
    }

    public void drawItem(int x, int y, ItemStack stack, boolean withOverlay) {
        drawItem(x, y, stack, withOverlay, null);
    }

    public void drawItem(int x, int y, ItemStack stack, boolean withOverlay, String text) {
        zOffset = 200.0f;
        itemRenderer.zOffset = 200.0f;

        try {
            itemRenderer.renderGuiItem(stack, x, y);
        } catch (Throwable t) {
        }

        if (withOverlay) {
            drawItemOverlay(stack, text, x, y);
        }

        zOffset = 0.0F;
        itemRenderer.zOffset = 0.0F;
    }

    public void drawItemOverlay(ItemStack stack, String text, int x, int y) {
        try {
            itemRenderer.renderGuiItemOverlay(fontRenderer, stack, x, y, "");
        } catch (Throwable t) {
        }

        if (text != null) {
            drawQuantity(x, y, text);
        }
    }

    public void drawQuantity(int x, int y, String qty) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x, y, 1);

        GlStateManager.scalef(0.5f, 0.5f, 1);

        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableDepthTest();
        fontRenderer.drawWithShadow(qty, 30 - fontRenderer.getStringWidth(qty), 22, 16777215);
        GlStateManager.enableDepthTest();
        GlStateManager.enableTexture();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
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
