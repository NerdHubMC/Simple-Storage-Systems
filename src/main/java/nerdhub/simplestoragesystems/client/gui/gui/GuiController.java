package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

public class GuiController extends Screen {

    public Identifier controllerGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/controller_gui.png");
    public BlockEntityController tile;
    public int left, top;
    public int screenWidth, screenHeight;
    public String name;

    public GuiController(BlockEntityController tile) {
        super();
        this.tile = tile;
        this.screenWidth = 176;
        this.screenHeight = 129;
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        this.left = (this.width - this.screenWidth) / 2;
        this.top = (this.height - this.screenHeight) / 2;
        name = I18n.translate("gui.simplestoragesystems.guicontroller");
    }

    @Override
    public void draw(int i, int i1, float var3) {
        this.drawBackground();
        super.draw(i, i1, var3);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(controllerGui);
        drawTexturedRect(left, top, 0, 0, screenWidth, screenHeight);

        renderEnergy();
        String name = "Storage Controller";
        this.fontRenderer.draw(name, (float)(this.width / 2 - this.fontRenderer.getStringWidth(name) / 2), top + 3, 4210752);

        if(this.isPointWithinBounds(10, 13, 16, 60, i, i1)) {
            this.drawTooltip(tile.storage.getEnergyStored() + " / " + tile.storage.getEnergyCapacity() + " Energy", i, i1);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void renderEnergy() {
        if(this.tile.storage.getEnergyStored() > 0) {
            int k = 85;
            int i = tile.storage.getEnergyStored() * k / tile.storage.getEnergyCapacity();
            this.drawTexturedRect(left + 10, top + 71 - i, 178, 61 - i, 14, i);
        }
    }

    public boolean isPointWithinBounds(int int_1, int int_2, int int_3, int int_4, double double_1, double double_2) {
        int int_5 = this.left;
        int int_6 = this.top;
        double_1 -= (double)int_5;
        double_2 -= (double)int_6;
        return double_1 >= (double)(int_1 - 1) && double_1 < (double)(int_1 + int_3 + 1) && double_2 >= (double)(int_2 - 1) && double_2 < (double)(int_2 + int_4 + 1);
    }
}
