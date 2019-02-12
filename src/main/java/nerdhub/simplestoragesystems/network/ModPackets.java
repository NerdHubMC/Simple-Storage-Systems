package nerdhub.simplestoragesystems.network;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModPackets {

    public static final Identifier PACKET_STORE_STACK = new Identifier(SimpleStorageSystems.MODID, "packet_store_stack");

    public static void registerPackets() {
        CustomPayloadPacketRegistry.SERVER.register(PACKET_STORE_STACK, ((context, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            ItemStack stack = packetByteBuf.readItemStack();
            if(context.getPlayer() != null && context.getPlayer().world != null) {
                BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(pos);

                if(blockEntity instanceof BlockEntityController) {
                    ((BlockEntityController) blockEntity).storeStack(stack, false);
                    context.getPlayer().inventory.setCursorStack(ItemStack.EMPTY);
                    context.getPlayer().inventory.updateItems();
                    context.getPlayer().inventory.markDirty();
                }
            }
        }));
    }

    public static void registerClientPackets() {
    }
}
