package nerdhub.simplestoragesystems;

import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.registry.ModBlocks;
import nerdhub.simplestoragesystems.registry.ModItems;
import nerdhub.simplestoragesystems.registry.ModRecipes;
import nerdhub.simplestoragesystems.utils.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.logging.Logger;

public class SimpleStorageSystems implements ModInitializer, ClientModInitializer {

    public static final String MODID = "simplestoragesystems";
    public static final ItemGroup modItemGroup = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(Items.DIAMOND));
    public static Logger LOGGER = Logger.getLogger("SimpleStorageSystems");
    public static Config config = new Config(MODID, SimpleStorageSystems.class, true);

    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModBlockEntities.registerBlockEntities();
        ModBlockEntities.registerServerGUIs();
        ModRecipes.registerRecipes();
    }

    @Override
    public void onInitializeClient() {
        ModBlockEntities.registerClientGUIs();
    }
}
