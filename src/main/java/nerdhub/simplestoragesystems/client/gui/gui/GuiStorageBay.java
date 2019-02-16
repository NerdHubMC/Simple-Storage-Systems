package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.container.ContainerStorageBay;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityStorageBay;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;

public class GuiStorageBay extends ContainerGuiBase {

    public Identifier storageBayGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/storage_bay_gui.png");
    public BlockEntityStorageBay tile;

    public GuiStorageBay(BlockEntityStorageBay tile, ContainerStorageBay container) {
        super(container, container.playerInventory, new TranslatableTextComponent("gui.simplestoragesystems.storage_bay"));
        this.tile = tile;
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
    }

    @Override
    public void draw(int var1, int var2, float var3) {
        this.drawBackground();
        super.draw(var1, var2, var3);
        this.drawMousoverTooltip(var1, var2);
    }

    @Override
    public void drawForeground(int int_1, int int_2) {
        this.fontRenderer.draw(this.name.getFormattedText(), (float)(this.containerWidth / 2 - this.fontRenderer.getStringWidth(this.name.getFormattedText()) / 2), 6.0F, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(storageBayGui);
        drawTexturedRect(left, top, 0, 0, containerWidth, containerHeight);
    }
}
