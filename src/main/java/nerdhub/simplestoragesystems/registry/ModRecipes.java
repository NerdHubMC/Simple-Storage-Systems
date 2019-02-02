package nerdhub.simplestoragesystems.registry;

import nerdhub.simplestoragesystems.utils.RecipeGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModRecipes {

    public static void registerRecipes() {
        new RecipeGenerator($ -> {
           //Dummy Recipe, remove later
           $.createShapeless(new ItemStack(Items.DIAMOND), null, new RecipeGenerator.ShapelessParser(new ItemStack(Blocks.DIRT)));
        });
    }
}
