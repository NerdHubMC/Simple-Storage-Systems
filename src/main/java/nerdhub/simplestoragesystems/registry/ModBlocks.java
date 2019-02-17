package nerdhub.simplestoragesystems.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.blocks.components.BlockController;
import nerdhub.simplestoragesystems.blocks.components.BlockStorageBay;
import nerdhub.simplestoragesystems.blocks.components.BlockTerminal;
import nerdhub.simplestoragesystems.blocks.components.BlockWirelessPoint;

public class ModBlocks {

    public static BlockController CONTROLLER = new BlockController();
    public static BlockStorageBay STORAGE_BAY = new BlockStorageBay();
    public static BlockTerminal TERMINAL = new BlockTerminal();
    public static BlockWirelessPoint WIRELESS_POINT = new BlockWirelessPoint();

    public static void registerBlocks() {
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, CONTROLLER);
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, STORAGE_BAY);
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, TERMINAL);
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, WIRELESS_POINT);
    }
}
