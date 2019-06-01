package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.container.ContainerStorageBay;
import nerdhub.simplestoragesystems.tiles.BlockEntityStorageBay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GuiStorageBay extends ContainerGuiBase {

    public Identifier storageBayGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/storage_bay_gui.png");
    public BlockEntityStorageBay tile;

    public GuiStorageBay(BlockEntityStorageBay tile, ContainerStorageBay container) {
        super(container, container.playerInventory, new TranslatableComponent("gui.simplestoragesystems.storage_bay"));
        this.tile = tile;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(int var1, int var2, float var3) {
        this.renderBackground();
        super.render(var1, var2, var1);
        this.drawMouseoverTooltip(var1, var2);
    }

    @Override
    public void drawForeground(int int_1, int int_2) {
        this.font.draw(this.title.getFormattedText(), (float)(this.containerWidth / 2 - this.font.getStringWidth(this.title.getFormattedText()) / 2), 6.0F, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(storageBayGui);
        blit(left, top, 0, 0, containerWidth, containerHeight);
    }
}
