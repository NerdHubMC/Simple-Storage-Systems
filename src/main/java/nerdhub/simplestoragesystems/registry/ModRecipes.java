package nerdhub.simplestoragesystems.registry;

import abused_master.abusedlib.registry.RecipeGenerator;
import nerdhub.simplestoragesystems.items.EnumCircuits;
import nerdhub.simplestoragesystems.items.EnumStorageCells;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModRecipes {

    public static RecipeGenerator registerRecipes() {
        RecipeGenerator generator = new RecipeGenerator($ -> {
            $.createShaped(new ItemStack(ModBlocks.CONTROLLER), null, new RecipeGenerator.ShapedParser("SDS", " C ", "SDS", 'S', Blocks.STONE, 'D', EnumCircuits.DIAMOND_CIRCUIT.getItem(), 'C', Blocks.CHEST));
            $.createShaped(new ItemStack(ModBlocks.STORAGE_BAY), null, new RecipeGenerator.ShapedParser("IRI", "CGC", "IRI", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'C', Blocks.CHEST, 'G', EnumCircuits.GOLD_CIRCUIT.getItem()));
            $.createShaped(new ItemStack(ModBlocks.WIRELESS_POINT), null, new RecipeGenerator.ShapedParser(" R ", "RIR", " R ", 'R', Items.REDSTONE, 'I', EnumCircuits.IRON_CIRCUIT.getItem()));
            $.createShaped(new ItemStack(ModBlocks.TERMINAL), null, new RecipeGenerator.ShapedParser("OGO", "GDG", "OGO", 'O', Blocks.OBSIDIAN, 'G', Blocks.GLASS, 'D', EnumCircuits.DIAMOND_CIRCUIT.getItem()));
            $.createShaped(new ItemStack(ModItems.LINKER), null, new RecipeGenerator.ShapedParser("OOO", " I ", "OOO", 'O', Blocks.OBSIDIAN, 'I', EnumCircuits.IRON_CIRCUIT.getItem()));

            //Cell Recipes, Temporary -> Make better recipes bish
            $.createShaped(new ItemStack(EnumStorageCells.STORAGE_CELL.getItem()), null, new RecipeGenerator.ShapedParser("GCG", "CIC", "SSS", 'G', Blocks.GLASS, 'C', Items.CLAY_BALL, 'I', EnumCircuits.IRON_CIRCUIT.getItem(), 'S', Items.GLOWSTONE_DUST));
            $.createShaped(new ItemStack(EnumStorageCells.STORAGE_CELL_4.getItem()), null, new RecipeGenerator.ShapedParser("GSG", "SCS", "RSR", 'G', Blocks.GLASS, 'S', EnumStorageCells.STORAGE_CELL.getItem(), 'C', EnumCircuits.GOLD_CIRCUIT.getItem(), 'R', Items.REDSTONE));
            $.createShaped(new ItemStack(EnumStorageCells.STORAGE_CELL_16.getItem()), null, new RecipeGenerator.ShapedParser("GSG", "SCS", "ISI", 'G', Blocks.GLASS, 'S', EnumStorageCells.STORAGE_CELL_4.getItem(), 'C', EnumCircuits.DIAMOND_CIRCUIT.getItem(), 'I', Items.GOLD_INGOT));
            $.createShaped(new ItemStack(EnumStorageCells.STORAGE_CELL_64.getItem()), null, new RecipeGenerator.ShapedParser("GSG", "SCS", "DSD", 'G', Blocks.GLASS, 'S', EnumStorageCells.STORAGE_CELL_16.getItem(), 'C', EnumCircuits.DIAMOND_CIRCUIT.getItem(), 'D', Items.DIAMOND));

            //Circuit Recipes, Temporary -> Make into a Circuit Press
            $.createShaped(new ItemStack(EnumCircuits.CIRCUIT_FRAME.getItem()), null, new RecipeGenerator.ShapedParser(" I ", "I I", " I ", 'I', Items.IRON_INGOT));
            $.createShapeless(new ItemStack(EnumCircuits.IRON_CIRCUIT.getItem()), null, new RecipeGenerator.ShapelessParser(new ItemStack(EnumCircuits.CIRCUIT_FRAME.getItem()), new ItemStack(Items.IRON_INGOT)));
            $.createShapeless(new ItemStack(EnumCircuits.GOLD_CIRCUIT.getItem()), null, new RecipeGenerator.ShapelessParser(new ItemStack(EnumCircuits.CIRCUIT_FRAME.getItem()), new ItemStack(Items.GOLD_INGOT)));
            $.createShapeless(new ItemStack(EnumCircuits.DIAMOND_CIRCUIT.getItem()), null, new RecipeGenerator.ShapelessParser(new ItemStack(EnumCircuits.CIRCUIT_FRAME.getItem()), new ItemStack(Items.DIAMOND)));
        });

        generator.accept();
        return generator;
    }
}
