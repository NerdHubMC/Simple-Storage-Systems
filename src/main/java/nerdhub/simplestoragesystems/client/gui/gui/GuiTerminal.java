package nerdhub.simplestoragesystems.client.gui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import io.netty.buffer.Unpooled;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.item.ICustomStorageStack;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.api.util.EnumExtractionType;
import nerdhub.simplestoragesystems.api.util.EnumUsageType;
import nerdhub.simplestoragesystems.client.gui.container.ContainerTerminal;
import nerdhub.simplestoragesystems.network.ModPackets;
import nerdhub.simplestoragesystems.utils.gui.Scrollbar;
import nerdhub.simplestoragesystems.utils.gui.TerminalDisplayHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class GuiTerminal extends ContainerGuiBase {

    public Identifier terminalGui = new Identifier(SimpleStorageSystems.MODID, "textures/gui/terminal_gui.png");
    public INetworkComponent tile;

    public TextFieldWidget searchBar;
    private Scrollbar scrollbar;
    public TerminalDisplayHandler view;

    public GuiTerminal(INetworkComponent tile, ContainerTerminal container) {
        super(container, container.playerInventory, new TranslatableComponent("gui.simplestoragesystems.terminal"));
        this.containerWidth = 194;
        this.containerHeight = 193;
        this.tile = tile;
        this.view = new TerminalDisplayHandler(this);
    }

    @Override
    protected void init() {
        super.init();
        this.searchBar = new TextFieldWidget(font, left + 62, top + 10, 106, 12, "");
        this.searchBar.setMaxLength(50);
        this.searchBar.setHasBorder(false);
        this.searchBar.setVisible(true);
        this.searchBar.setEditableColor(16777215);
        this.children.add(this.searchBar);

        this.scrollbar = new Scrollbar(174, getTopHeight(), 12, (4 * 18) - 2);
        updateScrollbar();

        this.updateItemsView();
    }

    @Override
    public void render(int var1, int var2, float var3) {
        this.renderBackground();
        super.render(var1, var2, var3);
        this.drawMouseoverTooltip(var1, var2);

        if (scrollbar != null) {
            scrollbar.update(this, var1 - left, var2 - top);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.getFormattedText(), 8, 10, 4210752);
        int x = 8;
        int y = 26;
        int slot = scrollbar != null ? (scrollbar.getOffset() * 9) : 0;

        for (int i = 0; i < 9 * 4; ++i) {
            if (slot < view.stacks.size()) {
                view.stacks.get(slot).draw(this, x, y);
            }

            if (inBounds(left + x, top + y, 16, 16, mouseX, mouseY)) {
                int color = 0x80FFFFFF;

                GlStateManager.disableLighting();
                GlStateManager.disableDepthTest();
                itemRenderer.zOffset = 190;
                GlStateManager.colorMask(true, true, true, false);
                blit(x, y, x + 16, y + 16, color, color);
                itemRenderer.zOffset = 0;
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepthTest();

                if(slot < view.stacks.size() && !view.stacks.get(slot).getStack().isEmpty() && this.minecraft.player.inventory.getCursorStack().isEmpty()) {
                    renderTooltip(view.stacks.get(slot).getStack(), mouseX, mouseY);
                }
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
        minecraft.getTextureManager().bindTexture(terminalGui);
        blit(left, top, 0, 0, containerWidth, containerHeight);
        this.searchBar.render(i, i1, v);

        if (scrollbar != null) {
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            scrollbar.draw(this);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.useEnergy(EnumUsageType.OPEN.getUsageAmount());
        if (searchBar != null) {
            searchBar.tick();
        }
    }

    public void updateItems() {
        this.view.setStacksInView(tile.getControllerEntity().getStoredStacks());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        super.mouseClicked(mouseX, mouseY, clickedButton);
        if (tile.isLinked()) {
            ItemStack stack = MinecraftClient.getInstance().player.inventory.getCursorStack();
            int x = 8;
            int y = 26;

            int slot = scrollbar != null ? (scrollbar.getOffset() * 9) : 0;

            for (int i = 0; i < 9 * 4; ++i) {
                if (inBounds(left + x, top + y, 16, 16, (int) mouseX, (int) mouseY)) {
                    if (!stack.isEmpty() && tile.getControllerEntity().storeStack(stack, true)) {
                        if(useEnergy(EnumUsageType.INSERTION.getUsageAmount() * stack.getAmount())) {
                            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                            buf.writeBlockPos(tile.getControllerEntity().getPos());
                            buf.writeItemStack(stack);
                            MinecraftClient.getInstance().getNetworkHandler().getClientConnection().send(new CustomPayloadC2SPacket(ModPackets.PACKET_STORE_STACK, buf));
                            MinecraftClient.getInstance().player.inventory.setCursorStack(ItemStack.EMPTY);
                            MinecraftClient.getInstance().player.playerContainer.sendContentUpdates();
                        }
                    } else if (stack.isEmpty() && slot > -1 && slot < view.stacks.size() && !view.stacks.get(slot).getStack().isEmpty()) {
                        EnumExtractionType type = EnumExtractionType.NORMAL_EXTRACTION;
                        ICustomStorageStack slotStack = view.stacks.get(slot);
                        int energyUsageAmount = 0;

                        if (clickedButton == 1) {
                            //Extract half
                            type = EnumExtractionType.HALF_EXTRACTION;
                            energyUsageAmount = slotStack.getAmount() > 64 ? 32 * EnumUsageType.EXTRACTION.getUsageAmount() : slotStack.getAmount() * EnumUsageType.EXTRACTION.getUsageAmount();
                        }

                        if (clickedButton == 2) {
                            //Extract only one
                            type = EnumExtractionType.SINGULAR_EXTRACTION;
                            energyUsageAmount = EnumUsageType.EXTRACTION.getUsageAmount();
                        }

                        if (hasShiftDown()) {
                            //Extract shift
                            type = EnumExtractionType.SHIFT_EXTRACTION;
                            energyUsageAmount = slotStack.getAmount() > 64 ? 64 * EnumUsageType.EXTRACTION.getUsageAmount() : slotStack.getAmount() * EnumUsageType.EXTRACTION.getUsageAmount();
                        }

                        if(type == EnumExtractionType.NORMAL_EXTRACTION) {
                            energyUsageAmount = slotStack.getAmount() > 64 ? 64 * EnumUsageType.EXTRACTION.getUsageAmount() : slotStack.getAmount() * EnumUsageType.EXTRACTION.getUsageAmount();
                        }

                        if(useEnergy(energyUsageAmount)) {
                            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                            buf.writeBlockPos(tile.getControllerEntity().getPos());
                            buf.writeItemStack(slotStack.getStack());
                            buf.writeInt(slotStack.getAmount());
                            buf.writeInt(type.ordinal());
                            MinecraftClient.getInstance().getNetworkHandler().getClientConnection().send(new CustomPayloadC2SPacket(ModPackets.PACKET_EXTRACT_STACK, buf));
                            MinecraftClient.getInstance().player.playerContainer.sendContentUpdates();
                        }
                    }
                }

                slot++;
                x += 18;

                if ((i + 1) % 9 == 0) {
                    x = 8;
                    y += 18;
                }
            }
        }

        this.updateItemsView();
        return true;
    }

    public boolean useEnergy(int amount) {
        if(tile.isLinked()) {
            if (tile.getControllerEntity().storage.getEnergyStored() >= EnumUsageType.OPEN.getUsageAmount()) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeBlockPos(tile.getControllerEntity().getPos());
                buf.writeInt(amount);
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(ModPackets.PACKET_USE_ENERGY, buf));
                return true;
            } else {
                this.onClose();
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double double_1, double double_2, double double_3) {
        if (scrollbar != null && double_1 != 0) {
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
    public void resize(MinecraftClient minecraftClient_1, int int_1, int int_2) {
        String string_1 = this.searchBar.getText();
        this.init(minecraftClient_1, int_1, int_2);
        this.searchBar.setText(string_1);
    }

    public void updateItemsView() {
        if (tile.isLinked()) {
            this.updateItems();
            view.sortTerminalStackView();
        }
    }

    public void updateScrollbar() {
        if (scrollbar != null) {
            scrollbar.setEnabled(getRows() > 4);
            scrollbar.setMaxOffset(getRows() - 4);
        }
    }

    public int getRows() {
        return Math.max(0, (int) Math.ceil((float) view.stacks.size() / 9F));
    }

    public int getTopHeight() {
        return 26;
    }
}