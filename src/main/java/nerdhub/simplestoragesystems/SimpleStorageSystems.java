package nerdhub.simplestoragesystems;

import abused_master.abusedlib.utils.Config;
import com.google.common.collect.Maps;
import nerdhub.simplestoragesystems.network.ModPackets;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.registry.ModBlocks;
import nerdhub.simplestoragesystems.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;

public class SimpleStorageSystems implements ModInitializer {

    public static final String MODID = "simplestoragesystems";
    public static final ItemGroup modItemGroup = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(ModBlocks.STORAGE_BAY));
    public static Config config;

    @Override
    public void onInitialize() {
        config = new Config(MODID, loadConfig());
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModBlockEntities.registerBlockEntities();
        ModBlockEntities.registerServerGUIs();
        ModPackets.registerPackets();
    }

    public Map<String, Object> loadConfig() {
        Map<String, Object> configOptions = Maps.newHashMap();
        configOptions.put("controller-idle-cost", 1);
        configOptions.put("system-opened-cost", 20);
        configOptions.put("item-insertion-cost", 10);
        configOptions.put("item-extraction-cost", 10);

        return configOptions;
    }
}
