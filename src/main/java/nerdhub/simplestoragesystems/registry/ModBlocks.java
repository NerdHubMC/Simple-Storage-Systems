package nerdhub.simplestoragesystems.registry;

import nerdhub.simplestoragesystems.blocks.components.BlockController;
import nerdhub.simplestoragesystems.blocks.components.BlockStorageBay;
import nerdhub.simplestoragesystems.blocks.components.BlockTerminal;
import nerdhub.simplestoragesystems.blocks.components.BlockWirelessPoint;
import nerdhub.simplestoragesystems.utils.RegistryHelper;

public class ModBlocks {

    public static BlockController CONTROLLER = new BlockController();
    public static BlockStorageBay STORAGE_BAY = new BlockStorageBay();
    public static BlockTerminal TERMINAL = new BlockTerminal();
    public static BlockWirelessPoint WIRELESS_POINT = new BlockWirelessPoint();

    public static void registerBlocks() {
        RegistryHelper.registerBlock(CONTROLLER);
        RegistryHelper.registerBlock(STORAGE_BAY);
        RegistryHelper.registerBlock(TERMINAL);
        RegistryHelper.registerBlock(WIRELESS_POINT);
    }
}
