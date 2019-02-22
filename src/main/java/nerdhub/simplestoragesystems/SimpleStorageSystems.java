package nerdhub.simplestoragesystems;

import abused_master.abusedlib.utils.Config;
import nerdhub.simplestoragesystems.client.render.WirelessPointRenderer;
import nerdhub.simplestoragesystems.network.ModPackets;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.registry.ModBlocks;
import nerdhub.simplestoragesystems.registry.ModItems;
import nerdhub.simplestoragesystems.registry.ModRecipes;
import nerdhub.simplestoragesystems.tiles.BlockEntityWirelessPoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SimpleStorageSystems implements ModInitializer, ClientModInitializer {

    public static final String MODID = "simplestoragesystems";
    public static final ItemGroup modItemGroup = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(ModBlocks.STORAGE_BAY));
    public static Config config = new Config(MODID, SimpleStorageSystems.class, true);

    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModBlockEntities.registerBlockEntities();
        ModBlockEntities.registerServerGUIs();
        ModRecipes.registerRecipes();
        ModPackets.registerPackets();
    }

    @Override
    public void onInitializeClient() {
        ModBlockEntities.registerClientGUIs();
        ModPackets.registerClientPackets();

        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityWirelessPoint.class, new WirelessPointRenderer());
    }
}
