package nerdhub.simplestoragesystems.items;

import nerdhub.simplestoragesystems.SimpleStorageSystems;

public class ItemStorageCell extends ItemBase {

    private EnumStorageCells type;

    public ItemStorageCell(EnumStorageCells type) {
        super(type.getName(), SimpleStorageSystems.modItemGroup);
        this.type = type;
    }

    public EnumStorageCells getType() {
        return type;
    }
}
