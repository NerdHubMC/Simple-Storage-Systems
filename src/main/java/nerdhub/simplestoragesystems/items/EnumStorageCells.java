package nerdhub.simplestoragesystems.items;

public enum EnumStorageCells {
    STORAGE_CELL(512),
    STORAGE_CELL_4(2048),
    STORAGE_CELL_16(8192),
    STORAGE_CELL_64(32768);

    private ItemStorageCell storageCellItem;
    private int storageCapacity;

    EnumStorageCells(int storageCapacity) {
        this.storageCellItem = new ItemStorageCell(this);
        this.storageCapacity = storageCapacity;
    }

    public ItemStorageCell getItem() {
        return storageCellItem;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public String getName() {
        return this.toString().toLowerCase();
    }
}
