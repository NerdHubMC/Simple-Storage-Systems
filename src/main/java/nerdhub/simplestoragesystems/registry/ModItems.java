package nerdhub.simplestoragesystems.registry;

import abused_master.abusedlib.registry.RegistryHelper;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.items.EnumCircuits;
import nerdhub.simplestoragesystems.items.EnumStorageCells;
import nerdhub.simplestoragesystems.items.ItemLinker;

public class ModItems {

    public static ItemLinker LINKER = new ItemLinker();

    public static void registerItems() {
        RegistryHelper.registerItem(SimpleStorageSystems.MODID, LINKER);
        RegistryHelper.registerItem(SimpleStorageSystems.MODID, LINKER);

        for (EnumCircuits circuit : EnumCircuits.values()) {
            RegistryHelper.registerItem(SimpleStorageSystems.MODID, circuit.getItem());
        }

        for (EnumStorageCells storageCell : EnumStorageCells.values()) {
            RegistryHelper.registerItem(SimpleStorageSystems.MODID, storageCell.getItem());
        }
    }
}
