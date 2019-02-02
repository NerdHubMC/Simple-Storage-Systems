package nerdhub.simplestoragesystems.items;

//TODO ADD STORAGE CELLS
public enum EnumStorageCells {
    ;

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
