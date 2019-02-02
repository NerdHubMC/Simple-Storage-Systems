package nerdhub.simplestoragesystems.registry;

import nerdhub.simplestoragesystems.blocks.components.BlockStorageBay;
import nerdhub.simplestoragesystems.utils.RegistryHelper;

public class ModBlocks {

    public static BlockStorageBay STORAGE_BAY = new BlockStorageBay();

    public static void registerBlocks() {
        RegistryHelper.registerBlock(STORAGE_BAY);
    }
}
