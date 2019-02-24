package nerdhub.simplestoragesystems;

import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.client.gui.container.ContainerStorageBay;
import nerdhub.simplestoragesystems.client.gui.container.ContainerTerminal;
import nerdhub.simplestoragesystems.client.gui.gui.GuiStorageBay;
import nerdhub.simplestoragesystems.client.gui.gui.GuiTerminal;
import nerdhub.simplestoragesystems.client.render.WirelessPointRenderer;
import nerdhub.simplestoragesystems.tiles.BlockEntityStorageBay;
import nerdhub.simplestoragesystems.tiles.BlockEntityWirelessPoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import static nerdhub.simplestoragesystems.network.ModPackets.PACKET_UPDATE_CURSOR;
import static nerdhub.simplestoragesystems.registry.ModBlockEntities.STORAGE_BAY_CONTAINER;
import static nerdhub.simplestoragesystems.registry.ModBlockEntities.TERMINAL_CONTAINER;

public class SimpleStorageSystemsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        this.registerClientGUIs();
        this.registerClientPackets();

        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityWirelessPoint.class, new WirelessPointRenderer());
    }

    public void registerClientGUIs() {
        ScreenProviderRegistry.INSTANCE.registerFactory(STORAGE_BAY_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockEntityStorageBay storageBay = (BlockEntityStorageBay) player.world.getBlockEntity(buf.readBlockPos());
            return new GuiStorageBay(storageBay, new ContainerStorageBay(syncid, player.inventory, storageBay));
        }));
        ScreenProviderRegistry.INSTANCE.registerFactory(TERMINAL_CONTAINER, ((syncid, identifier, player, buf) -> {
            INetworkComponent component = (INetworkComponent) player.world.getBlockEntity(buf.readBlockPos());
            component.setControllerPos(buf.readBlockPos());
            return new GuiTerminal(component, new ContainerTerminal(syncid, player.inventory));
        }));
    }

    public void registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_UPDATE_CURSOR, ((context, packetByteBuf) -> context.getPlayer().inventory.setCursorStack(packetByteBuf.readItemStack())));
    }
}
