package nerdhub.simplestoragesystems.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.client.gui.container.ContainerStorageBay;
import nerdhub.simplestoragesystems.client.gui.container.ContainerTerminal;
import nerdhub.simplestoragesystems.client.gui.gui.GuiStorageBay;
import nerdhub.simplestoragesystems.client.gui.gui.GuiTerminal;
import nerdhub.simplestoragesystems.tiles.BlockEntityController;
import nerdhub.simplestoragesystems.tiles.BlockEntityStorageBay;
import nerdhub.simplestoragesystems.tiles.BlockEntityTerminal;
import nerdhub.simplestoragesystems.tiles.BlockEntityWirelessPoint;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModBlockEntities {

    //Tiles
    public static BlockEntityType<BlockEntityController> CONTROLLER;
    public static BlockEntityType<BlockEntityStorageBay> STORAGE_BAY;
    public static BlockEntityType<BlockEntityTerminal> TERMINAL;
    public static BlockEntityType<BlockEntityWirelessPoint> WIRELESS_POINT;

    //GUIs
    public static final Identifier STORAGE_BAY_CONTAINER = new Identifier(SimpleStorageSystems.MODID, "storage_bay_container");
    public static final Identifier TERMINAL_CONTAINER = new Identifier(SimpleStorageSystems.MODID, "terminal_container");

    public static void registerBlockEntities() {
        CONTROLLER = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "controller"), BlockEntityController.class);
        STORAGE_BAY = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "storage_bay"), BlockEntityStorageBay.class);
        TERMINAL = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "terminal"), BlockEntityTerminal.class);
        WIRELESS_POINT = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "wireless_point"), BlockEntityWirelessPoint.class);
    }

    public static void registerServerGUIs() {
        ContainerProviderRegistry.INSTANCE.registerFactory(STORAGE_BAY_CONTAINER, (syncid, identifier, player, buf) -> new ContainerStorageBay(syncid, player.inventory, (BlockEntityStorageBay) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(TERMINAL_CONTAINER, (syncid, identifier, player, buf) -> new ContainerTerminal(syncid, player.inventory));
    }

    public static void registerClientGUIs() {
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
}
