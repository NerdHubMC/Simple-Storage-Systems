package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.container.ContainerTerminal;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityTerminal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GuiTerminal extends ContainerScreen {

    public Identifier terminalGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/terminal_gui.png");
    public Identifier TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    public BlockEntityTerminal tile;
    public TextFieldWidget searchBar;
    public float scrollBarPosition;

    public GuiTerminal(BlockEntityTerminal tile, ContainerTerminal container) {
        super(container, container.playerInventory, new StringTextComponent("Storage Terminal"));
        this.containerWidth = 194;
        this.containerHeight = 176;
        this.tile = tile;
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        this.searchBar = new TextFieldWidget(0, fontRenderer, left + 62, top + 10, 106, 12);
        this.searchBar.setMaxLength(50);
        this.searchBar.setHasBorder(false);
        this.searchBar.setVisible(true);
        this.searchBar.method_1868(16777215);
        this.listeners.add(this.searchBar);
        this.scrollBarPosition = 0f;
    }

    @Override
    public void draw(int var1, int var2, float var3) {
        this.drawBackground();
        super.draw(var1, var2, var3);
        this.drawMousoverTooltip(var1, var2);
    }

    @Override
    public void drawForeground(int int_1, int int_2) {
        String string_1 = "Terminal";
        this.fontRenderer.draw(string_1, 8, 10, 4210752);
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(terminalGui);
        drawTexturedRect(left, top, 0, 0, containerWidth, containerHeight);
        this.searchBar.render(i, i1, v);

        int int_4 = top + 26;
        int int_5 = int_4 + 52;
        this.client.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedRect(left + 174, int_4 + ((int_5 - int_4 - 17) * this.scrollBarPosition), 232, 0, 12, 15);
    }

    @Override
    public boolean mouseScrolled(double double_1) {
        //int int_1 = (((CreativePlayerInventoryScreen.CreativeContainer) this.container).itemList.size() + 9 - 1) / 9 - 5;
        //this.scrollBarPosition = (float) ((double) this.scrollBarPosition - double_1 / (double) int_1);
        this.scrollBarPosition = MathHelper.clamp(this.scrollBarPosition, 0.0F, 1.0F);
        //TODO LOOK AT THIS FOR DISPLAYING ITEMS
        //((CreativePlayerInventoryScreen.CreativeContainer)this.container).method_2473(this.scrollPosition);
        return true;
    }

    @Override
    public void update() {
        super.update();
        if(searchBar != null) {
            searchBar.tick();
        }
    }

    @Override
    protected void onMouseClick(Slot slot_1, int int_1, int int_2, SlotActionType slotActionType_1) {
        super.onMouseClick(slot_1, int_1, int_2, slotActionType_1);
        if (slot_1 != null && this.playerInventory != slot_1.inventory) {
            this.searchBar.method_1872();
            this.searchBar.method_1884(0);
        }
    }

    @Override
    public void onScaleChanged(MinecraftClient minecraftClient_1, int int_1, int int_2) {
        String string_1 = this.searchBar.getText();
        this.initialize(minecraftClient_1, int_1, int_2);
        this.searchBar.setText(string_1);
    }
}
