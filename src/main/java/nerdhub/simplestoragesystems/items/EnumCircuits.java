package nerdhub.simplestoragesystems.items;

import nerdhub.simplestoragesystems.SimpleStorageSystems;

public enum EnumCircuits {
    CIRCUIT_FRAME,
    IRON_CIRCUIT,
    GOLD_CIRCUIT,
    DIAMOND_CIRCUIT;

    private ItemBase item;

    EnumCircuits() {
        this.item = new ItemBase(this.getName(), SimpleStorageSystems.modItemGroup);
    }

    public ItemBase getItem() {
        return item;
    }

    public String getName() {
        return this.toString().toLowerCase();
    }
}
