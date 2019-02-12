package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.container.ContainerController;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Identifier;

public class GuiController extends ContainerScreen {

    public Identifier controllerGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/controller_gui.png");
    public BlockEntityController tile;
    public int guiLeft, guiTop;

    public GuiController(BlockEntityController tile, ContainerController container) {
        super(container, container.playerInventory, new StringTextComponent("Storage Controller"));
        this.tile = tile;
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        this.guiLeft = (this.width - this.containerWidth) / 2;
        this.guiTop = (this.height - this.containerHeight) / 2;
    }

    @Override
    public void draw(int var1, int var2, float var3) {
        this.drawBackground();
        super.draw(var1, var2, var3);
        this.drawMousoverTooltip(var1, var2);
    }

    @Override
    public void drawForeground(int int_1, int int_2) {
        String string_1 = "Storage Controller";
        this.fontRenderer.draw(string_1, (float)(this.containerWidth / 2 - this.fontRenderer.getStringWidth(string_1) / 2), 3, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(controllerGui);
        drawTexturedRect(guiLeft, guiTop, 0, 0, containerWidth, containerHeight);

        renderEnergy(guiLeft, guiTop);

        if(this.isPointWithinBounds(10, 13, 16, 60, i, i1)) {
            this.drawTooltip(tile.storage.getEnergyStored() + " / " + tile.storage.getEnergyCapacity() + " Energy", i, i1);
        }
    }

    public void renderEnergy(int guiLeft, int guiTop) {
        if(this.tile.storage.getEnergyStored() > 0) {
            int k = 85;
            int i = tile.storage.getEnergyStored() * k / tile.storage.getEnergyCapacity();
            this.drawTexturedRect(guiLeft + 10, guiTop + 71 - i, 178, 61 - i, 14, i);
        }
    }
}
