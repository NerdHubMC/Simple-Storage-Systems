package nerdhub.simplestoragesystems.registry;

import nerdhub.simplestoragesystems.items.EnumStorageCells;
import nerdhub.simplestoragesystems.items.ItemLinker;
import nerdhub.simplestoragesystems.utils.RegistryHelper;

public class ModItems {

    public static ItemLinker LINKER = new ItemLinker();

    public static void registerItems() {
        RegistryHelper.registerItem(LINKER);

        for (EnumStorageCells storageCell : EnumStorageCells.values()) {
            RegistryHelper.registerItem(storageCell.getItem());
        }
    }
}
