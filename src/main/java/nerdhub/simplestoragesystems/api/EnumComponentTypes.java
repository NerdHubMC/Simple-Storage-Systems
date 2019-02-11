package nerdhub.simplestoragesystems.api;

public enum EnumComponentTypes {
    CONTROLLER(false),
    WIRELESS_POINT(false),
    STORAGE_BAY(true),
    TERMINAL(true),
    CRAFTING_TERMINAL(true),
    AUTO_CRAFTING_PROCESSOR(true),
    AUTO_CRAFTING_INTERFACE(true);

    private boolean linkable;

    EnumComponentTypes(boolean linkable) {
        this.linkable = linkable;
    }

    public boolean isLinkable() {
        return linkable;
    }
}