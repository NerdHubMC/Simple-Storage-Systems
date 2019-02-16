package nerdhub.simplestoragesystems.network;

import io.netty.buffer.Unpooled;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.util.EnumExtractionType;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.CustomPayloadClientPacket;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class ModPackets {

    public static final Identifier PACKET_STORE_STACK = new Identifier(SimpleStorageSystems.MODID, "packet_store_stack");
    public static final Identifier PACKET_EXTRACT_STACK = new Identifier(SimpleStorageSystems.MODID, "packet_extract_stack");
    public static final Identifier PACKET_UPDATE_CURSOR = new Identifier(SimpleStorageSystems.MODID, "packet_update_cursor");

    public static void registerPackets() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_STORE_STACK, ((context, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            ItemStack stack = packetByteBuf.readItemStack();

            if (context.getPlayer() != null && context.getPlayer().world != null) {
                context.getTaskQueue().execute(() -> {
                    BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(pos);

                    if (blockEntity instanceof BlockEntityController) {
                        ((BlockEntityController) blockEntity).storeStack(stack, false);
                        context.getPlayer().inventory.setCursorStack(ItemStack.EMPTY);
                    }
                });
            }
        }));

        ServerSidePacketRegistry.INSTANCE.register(PACKET_EXTRACT_STACK, ((context, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            ItemStack stack = packetByteBuf.readItemStack();
            int stackAmount = packetByteBuf.readInt();
            EnumExtractionType type = EnumExtractionType.values()[packetByteBuf.readInt()];

            if(context.getPlayer() != null && context.getPlayer().world != null) {
                context.getTaskQueue().execute(() -> {
                    BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(pos);

                    if(blockEntity instanceof BlockEntityController) {
                        int extractionAmount;
                        switch (type) {
                            case HALF_EXTRACTION:
                                extractionAmount = stackAmount > 64 ? 32 : stackAmount / 2;
                                break;
                            case SINGULAR_EXTRACTION:
                                extractionAmount = 1;
                                break;
                            case NORMAL_EXTRACTION:
                            case SHIFT_EXTRACTION:
                            default:
                                extractionAmount = stackAmount > 64 ? 64 : stackAmount;
                                break;
                        }

                        ((BlockEntityController) blockEntity).extractFromSystem(stack, extractionAmount);
                        ((ServerPlayerEntity) context.getPlayer()).networkHandler.sendPacket(new CustomPayloadClientPacket(PACKET_UPDATE_CURSOR, new PacketByteBuf(Unpooled.buffer()).writeItemStack(new ItemStack(stack.getItem(), extractionAmount))));
                        context.getPlayer().inventory.setCursorStack(new ItemStack(stack.getItem(), extractionAmount));
                    }
                });
            }
        }));
    }

    public static void registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_UPDATE_CURSOR, ((context, packetByteBuf) -> context.getPlayer().inventory.setCursorStack(packetByteBuf.readItemStack())));
    }
}
