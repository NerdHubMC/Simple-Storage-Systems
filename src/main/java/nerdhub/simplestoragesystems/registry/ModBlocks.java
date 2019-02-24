package nerdhub.simplestoragesystems.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.blocks.BlockController;
import nerdhub.simplestoragesystems.blocks.BlockStorageBay;
import nerdhub.simplestoragesystems.blocks.BlockTerminal;
import nerdhub.simplestoragesystems.blocks.BlockWirelessPoint;

public class ModBlocks {

    public static BlockController CONTROLLER;
    public static BlockStorageBay STORAGE_BAY;
    public static BlockTerminal TERMINAL;
    public static BlockWirelessPoint WIRELESS_POINT;

    public static void registerBlocks() {
        CONTROLLER = new BlockController();
        STORAGE_BAY = new BlockStorageBay();
        TERMINAL = new BlockTerminal();
        WIRELESS_POINT = new BlockWirelessPoint();
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, CONTROLLER);
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, STORAGE_BAY);
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, TERMINAL);
        RegistryHelper.registerBlock(SimpleStorageSystems.MODID, WIRELESS_POINT);
    }
}
