package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.tiles.BlockEntityController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class GuiController extends Screen {

    public Identifier controllerGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/controller_gui.png");
    public BlockEntityController tile;
    public int left, top;
    public int screenWidth, screenHeight;

    public GuiController(BlockEntityController tile) {
        super(new TranslatableComponent("gui.simplestoragesystems.guicontroller"));
        this.tile = tile;
        this.screenWidth = 176;
        this.screenHeight = 129;
    }

    @Override
    protected void init() {
        super.init();
        this.left = (this.width - this.screenWidth) / 2;
        this.top = (this.height - this.screenHeight) / 2;
    }

    @Override
    public void render(int i, int i1, float var3) {
        this.renderBackground();
        super.render(i, i1, var3);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(controllerGui);
        blit(left, top, 0, 0, screenWidth, screenHeight);

        renderEnergy();
        this.font.draw(title.getFormattedText(), (float)(this.width / 2 - this.font.getStringWidth(title.getFormattedText()) / 2), top + 3, 4210752);

        if(this.isPointWithinBounds(10, 13, 16, 60, i, i1)) {
            this.renderTooltip(tile.storage.getEnergyStored() + " / " + tile.storage.getCapacity() + " Energy", i, i1);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == GLFW.GLFW_KEY_E) {
            this.onClose();
            return true;
        } else {
            return super.keyPressed(int_1, int_2, int_3);
        }
    }

    public void renderEnergy() {
        if(this.tile.storage.getEnergyStored() > 0) {
            int k = 85;
            int i = tile.storage.getEnergyStored() * k / tile.storage.getCapacity();
            this.blit(left + 10, top + 71 - i, 178, 61 - i, 14, i);
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
