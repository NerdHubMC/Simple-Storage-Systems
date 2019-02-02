package nerdhub.simplestoragesystems.registry;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.container.ContainerStorageBay;
import nerdhub.simplestoragesystems.client.gui.gui.GuiStorageBay;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityStorageBay;
import nerdhub.simplestoragesystems.utils.RegistryHelper;
import net.fabricmc.fabric.api.client.gui.GuiProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModBlockEntities {

    //Tiles
    public static BlockEntityType<BlockEntityStorageBay> STORAGE_BAY;

    //Guis
    public static final Identifier STORAGE_BAY_CONTAINER = new Identifier(SimpleStorageSystems.MODID, "storage_bay_container");

    public static void registerBlockEntities() {
        STORAGE_BAY = RegistryHelper.registerTile(new Identifier(SimpleStorageSystems.MODID, "storage_bay"), BlockEntityStorageBay.class);
    }

    public static void registerServerGUIs() {
        ContainerProviderRegistry.INSTANCE.registerFactory(STORAGE_BAY_CONTAINER, (syncid, identifier, player, buf) -> new ContainerStorageBay(syncid, player.inventory, (BlockEntityStorageBay) player.world.getBlockEntity(buf.readBlockPos())));
    }

    public static void registerClientGUIs() {
        GuiProviderRegistry.INSTANCE.registerFactory(STORAGE_BAY_CONTAINER, ((syncid, identifier, player, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntityStorageBay storageBay = (BlockEntityStorageBay) player.world.getBlockEntity(pos);
            return new GuiStorageBay(storageBay, new ContainerStorageBay(syncid, player.inventory, storageBay));
        }));
    }
}
