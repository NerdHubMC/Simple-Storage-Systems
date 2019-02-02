package nerdhub.simplestoragesystems.items;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class ItemBase extends Item {

    private String name;

    public ItemBase(String name, ItemGroup tab) {
        super(new Settings().itemGroup(tab));
        this.name = name;
    }

    public ItemBase(String name, Settings itemSettings) {
        super(itemSettings);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Identifier getNameIdentifier() {
        return new Identifier(SimpleStorageSystems.MODID, getName());
    }
}