package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import io.netty.buffer.Unpooled;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.container.ContainerTerminal;
import nerdhub.simplestoragesystems.client.gui.widget.Scrollbar;
import nerdhub.simplestoragesystems.client.gui.widget.TerminalViewHelper;
import nerdhub.simplestoragesystems.network.ModPackets;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityTerminal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.packet.CustomPayloadServerPacket;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class GuiTerminal extends ContainerGuiBase {

    public Identifier terminalGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/terminal_gui.png");
    public BlockEntityTerminal tile;

    public TextFieldWidget searchBar;
    private int slotNumber;
    private Scrollbar scrollbar;
    private TerminalViewHelper view;

    public GuiTerminal(BlockEntityTerminal tile, ContainerTerminal container) {
        super(container, container.playerInventory, new StringTextComponent("Storage Terminal"));
        this.containerWidth = 194;
        this.containerHeight = 193;
        this.tile = tile;
        this.view = new TerminalViewHelper(this);
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

        this.scrollbar = new Scrollbar(174, getTopHeight(), 12, (4 * 18) - 2);
        updateScrollbar();
    }

    @Override
    public void draw(int var1, int var2, float var3) {
        this.drawBackground();
        super.draw(var1, var2, var3);
        this.drawMousoverTooltip(var1, var2);

        if(isOverSlotWithStack()) {
            //TODO RENDER TOOLTIP IF MOUSE IS OVER STACK
        }

        if(scrollbar != null) {
            scrollbar.update(this, var1 - left, var2 - top);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        String string_1 = "Terminal";
        this.fontRenderer.draw(string_1, 8, 10, 4210752);

        int x = 8;
        int y = 26;
        this.slotNumber = -1;
        int slot = scrollbar != null ? (scrollbar.getOffset() * 9) : 0;

        for (int i = 0; i < 9 * 4; ++i) {
            if (inBounds(x, y, 16, 16, mouseX, mouseY)) {
                this.slotNumber = slot;
            }

            if (slot < view.getStacks().size()) {
                view.getStacks().get(slot).draw(this, x, y);
            }

            if (inBounds(left + x, top + y, 16, 16, mouseX, mouseY)) {
                int color = -2130706433;

                GlStateManager.disableLighting();
                GlStateManager.disableDepthTest();
                zOffset = 190;
                GlStateManager.colorMask(true, true, true, false);
                drawGradientRect(x, y, x + 16, y + 16, color, color);
                zOffset = 0;
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepthTest();
            }

            slot++;

            x += 18;

            if ((i + 1) % 9 == 0) {
                x = 8;
                y += 18;
            }
        }
    }

    @Override
    public void drawBackground(float v, int i, int i1) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(terminalGui);
        drawTexturedRect(left, top, 0, 0, containerWidth, containerHeight);
        this.searchBar.render(i, i1, v);

        if(scrollbar != null) {
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            scrollbar.draw(this);
        }
    }

    @Override
    public void update() {
        super.update();
        if(searchBar != null) {
            searchBar.tick();
        }

        if(tile.isLinked) {
            view.sort();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        super.mouseClicked(mouseX, mouseY, clickedButton);
        if(tile.isLinked) {
            ItemStack stack = MinecraftClient.getInstance().player.inventory.getCursorStack();

            int x = 8;
            int y = 26;

            for (int i = 0; i < 9 * 4; ++i) {
                if (inBounds(left + x, top + y, 16, 16, (int) mouseX, (int) mouseY)) {
                    if(!stack.isEmpty()) {
                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeItemStack(stack);
                        buf.writeBlockPos(tile.getPos());
                        MinecraftClient.getInstance().getNetworkHandler().getClientConnection().sendPacket(new CustomPayloadServerPacket(ModPackets.PACKET_STORE_STACK, buf));
                        MinecraftClient.getInstance().player.inventory.setCursorStack(ItemStack.EMPTY);
                        MinecraftClient.getInstance().player.inventory.markDirty();
                        MinecraftClient.getInstance().player.inventory.updateItems();
                    }else {
                    }
                }

                x += 18;

                if ((i + 1) % 9 == 0) {
                    x = 8;
                    y += 18;
                }
            }
        }else {
            System.out.println("jsadnjnsad");
        }

        return false;
    }

    /**
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int int_1) {
        if(getSlotUnderMouse(mouseX, mouseY) != null) {
            ItemStack stack = MinecraftClient.getInstance().player.inventory.getCursorStack();
            if(stack.isEmpty()) {

            }else {
                if(tile != null && tile.getControllerEntity().storeStack(stack, false)) {
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeItemStack(stack);
                    buf.writeBlockPos(tile.getPos());
                    MinecraftClient.getInstance().getNetworkHandler().getClientConnection().sendPacket(new CustomPayloadServerPacket(ModPackets.PACKET_STORE_STACK, buf));
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, int_1);
    }
    */

    @Override
    public boolean mouseScrolled(double double_1) {
        if(scrollbar != null && double_1 != 0) {
            scrollbar.wheel((int) double_1);
        }
        return true;
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

    public boolean isOverSlotWithStack() {
        return tile.isLinked && isMouseOverSlot() && slotNumber < view.getStacks().size();
    }

    public boolean isMouseOverSlot() {
        return slotNumber > 0;
    }

    public boolean isOverSlotArea(int mouseX, int mouseY) {
        return inBounds(7, 25, 162, 18 * 4, mouseX, mouseY);
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void updateScrollbar() {
        if (scrollbar != null) {
            scrollbar.setEnabled(getRows() > 4);
            scrollbar.setMaxOffset(getRows() - 4);
        }
    }

    public int getRows() {
        return Math.max(0, (int) Math.ceil((float) view.getStacks().size() / 9F));
    }

    public int getTopHeight() {
        return 26;
    }
}
