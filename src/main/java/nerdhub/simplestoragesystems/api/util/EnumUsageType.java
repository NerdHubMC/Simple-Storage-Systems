package nerdhub.simplestoragesystems.api.util;

import nerdhub.simplestoragesystems.SimpleStorageSystems;

public enum EnumUsageType {
    EXTRACTION(SimpleStorageSystems.config.getInt("item-extraction-cost")),
    INSERTION(SimpleStorageSystems.config.getInt("item-insertion-cost")),
    OPEN(SimpleStorageSystems.config.getInt("system-opened-cost")),
    IDLE(SimpleStorageSystems.config.getInt("controller-idle-cost"));

    private int usageAmount;

    EnumUsageType(int useAmount) {
        this.usageAmount = useAmount;
    }

    public int getUsageAmount() {
        return usageAmount;
    }
}
