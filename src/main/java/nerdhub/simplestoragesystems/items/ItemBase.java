package nerdhub.simplestoragesystems.items;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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

    @Override
    public void onEntityTick(ItemStack itemStack_1, World world_1, Entity entity_1, int int_1, boolean boolean_1) {
        super.onEntityTick(itemStack_1, world_1, entity_1, int_1, boolean_1);
    }

    public String getName() {
        return name;
    }

    public Identifier getNameIdentifier() {
        return new Identifier(SimpleStorageSystems.MODID, getName());
    }
}