package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;

@Environment(EnvType.CLIENT)
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
        itemRenderer.renderGuiItemOverlay(fontRenderer, stack, x, y, "");

        GlStateManager.translatef(x, y, 1);
        GlStateManager.scalef(0.65f, 0.65f, 1);
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepthTest();
        fontRenderer.drawWithShadow(text, 24 - fontRenderer.getStringWidth(text), 16, 0xFFFFFFFF);
        GlStateManager.enableTexture();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();

        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        this.itemRenderer.zOffset = 0.0f;
        this.zOffset = 0.0f;
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
