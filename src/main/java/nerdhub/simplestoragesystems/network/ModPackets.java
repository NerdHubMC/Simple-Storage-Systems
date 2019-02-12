package nerdhub.simplestoragesystems.network;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityController;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityWirelessPoint;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

public class ModPackets {

    public static final Identifier PACKET_STORE_STACK = new Identifier(SimpleStorageSystems.MODID, "packet_store_stack");
    public static final Identifier PACKET_LINK_COMPONENTS = new Identifier(SimpleStorageSystems.MODID, "packet_link_components");
    public static final Identifier PACKET_LINK_POINT = new Identifier(SimpleStorageSystems.MODID, "packet_link_point");
    public static final Identifier PACKET_POINT_DESTROYED = new Identifier(SimpleStorageSystems.MODID, "packet_point_destroyed");

    public static void registerPackets() {
        CustomPayloadPacketRegistry.SERVER.register(PACKET_STORE_STACK, ((context, packetByteBuf) -> {
            BlockEntityController controller = (BlockEntityController) context.getPlayer().world.getBlockEntity(packetByteBuf.readBlockPos());
            if(controller != null) {
                controller.storeStack(packetByteBuf.readItemStack(), false);
            }
        }));

        CustomPayloadPacketRegistry.SERVER.register(PACKET_LINK_COMPONENTS, (((context, packetByteBuf) -> {
            BlockPos controllerPos = packetByteBuf.readBlockPos();
            INetworkComponent networkComponent = (INetworkComponent) context.getPlayer().world.getBlockEntity(TagHelper.deserializeBlockPos(packetByteBuf.readCompoundTag()));
            if(networkComponent != null) {
                networkComponent.setIsLinked(true);
                networkComponent.setControllerPos(controllerPos);
            }
        })));

        CustomPayloadPacketRegistry.SERVER.register(PACKET_LINK_POINT, (((context, packetByteBuf) -> {
            BlockPos controllerPos = packetByteBuf.readBlockPos();
            BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) context.getPlayer().world.getBlockEntity(TagHelper.deserializeBlockPos(packetByteBuf.readCompoundTag()));
            point.setControllerPos(controllerPos);
        })));

        CustomPayloadPacketRegistry.SERVER.register(PACKET_POINT_DESTROYED, (((context, packetByteBuf) -> {
            INetworkComponent networkComponent = (INetworkComponent) context.getPlayer().world.getBlockEntity(packetByteBuf.readBlockPos());
            if(networkComponent != null) {
                networkComponent.setIsLinked(false);
                networkComponent.setControllerPos(null);
            }
        })));
    }

    public static void registerClientPackets() {
    }
}
