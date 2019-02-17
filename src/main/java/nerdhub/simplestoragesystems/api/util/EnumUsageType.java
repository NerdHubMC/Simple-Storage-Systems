package nerdhub.simplestoragesystems.api.util;

import nerdhub.simplestoragesystems.SimpleStorageSystems;

public enum EnumUsageType {
    EXTRACTION(SimpleStorageSystems.config.getInt("item-extraction")),
    INSERTION(SimpleStorageSystems.config.getInt("item-insertion")),
    OPEN(SimpleStorageSystems.config.getInt("system-opened")),
    IDLE(SimpleStorageSystems.config.getInt("controller-idle"));

    private int usageAmount;

    EnumUsageType(int useAmount) {
        this.usageAmount = useAmount;
    }

    public int getUsageAmount() {
        return usageAmount;
    }
}
