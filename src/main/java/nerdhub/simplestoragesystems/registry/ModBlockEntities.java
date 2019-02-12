package nerdhub.simplestoragesystems.registry;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.container.ContainerController;
import nerdhub.simplestoragesystems.client.gui.container.ContainerStorageBay;
import nerdhub.simplestoragesystems.client.gui.container.ContainerTerminal;
import nerdhub.simplestoragesystems.client.gui.gui.GuiController;
import nerdhub.simplestoragesystems.client.gui.gui.GuiStorageBay;
import nerdhub.simplestoragesystems.client.gui.gui.GuiTerminal;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityStorageBay;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityTerminal;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityWirelessPoint;
import nerdhub.simplestoragesystems.utils.RegistryHelper;
import net.fabricmc.fabric.api.client.gui.GuiProviderRegistry;
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

    //Guis
    public static final Identifier CONTROLLER_CONTAINER = new Identifier(SimpleStorageSystems.MODID, "controller_container");
    public static final Identifier STORAGE_BAY_CONTAINER = new Identifier(SimpleStorageSystems.MODID, "storage_bay_container");
    public static final Identifier TERMINAL_CONTAINER = new Identifier(SimpleStorageSystems.MODID, "terminal_container");

    public static void registerBlockEntities() {
        CONTROLLER = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "controller"), BlockEntityController.class);
        STORAGE_BAY = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "storage_bay"), BlockEntityStorageBay.class);
        TERMINAL = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "terminal"), BlockEntityTerminal.class);
        WIRELESS_POINT = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "wireless_point"), BlockEntityWirelessPoint.class);
    }

    public static void registerServerGUIs() {
        ContainerProviderRegistry.INSTANCE.registerFactory(CONTROLLER_CONTAINER, (syncid, identifier, player, buf) -> new ContainerController(syncid, player.inventory));
        ContainerProviderRegistry.INSTANCE.registerFactory(STORAGE_BAY_CONTAINER, (syncid, identifier, player, buf) -> new ContainerStorageBay(syncid, player.inventory, (BlockEntityStorageBay) player.world.getBlockEntity(buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(TERMINAL_CONTAINER, (syncid, identifier, player, buf) -> new ContainerTerminal(syncid, player.inventory, (BlockEntityTerminal) player.world.getBlockEntity(buf.readBlockPos())));
    }

    public static void registerClientGUIs() {
        GuiProviderRegistry.INSTANCE.registerFactory(CONTROLLER_CONTAINER, ((syncid, identifier, player, buf) -> new GuiController((BlockEntityController) player.world.getBlockEntity(buf.readBlockPos()), new ContainerController(syncid, player.inventory))));
        GuiProviderRegistry.INSTANCE.registerFactory(STORAGE_BAY_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityStorageBay storageBay = (BlockEntityStorageBay) player.world.getBlockEntity(pos);
            return new GuiStorageBay(storageBay, new ContainerStorageBay(syncid, player.inventory, storageBay));
        }));
        GuiProviderRegistry.INSTANCE.registerFactory(TERMINAL_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityTerminal terminal = (BlockEntityTerminal) player.world.getBlockEntity(pos);
            return new GuiTerminal(terminal, new ContainerTerminal(syncid, player.inventory, terminal));
        }));
    }
}
