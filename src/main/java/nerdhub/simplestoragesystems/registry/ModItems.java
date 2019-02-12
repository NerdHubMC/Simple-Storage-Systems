package nerdhub.simplestoragesystems.registry;

import nerdhub.simplestoragesystems.items.EnumStorageCells;
import nerdhub.simplestoragesystems.utils.RegistryHelper;

public class ModItems {

    public static void registerItems() {
        for (EnumStorageCells storageCell : EnumStorageCells.values()) {
            RegistryHelper.registerItem(storageCell.getItem());
        }
    }
}
