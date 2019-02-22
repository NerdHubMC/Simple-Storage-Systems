package nerdhub.simplestoragesystems.utils;

import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.registry.ModBlocks;
import nerdhub.simplestoragesystems.tiles.BlockEntityController;
import nerdhub.simplestoragesystems.tiles.BlockEntityWirelessPoint;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ComponentHelper {

    public static void linkComponents(IWorld world, BlockPos pointPos) {
        if(world.getBlockEntity(pointPos) instanceof BlockEntityWirelessPoint) {
            BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(pointPos);
            point.connectedComponents.clear();

            if(point.isLinked()) {
                for (BlockPos entityPos : getAdjacentTiles(world, pointPos)) {
                    if (world.getBlockEntity(entityPos) instanceof INetworkComponent && ((INetworkComponent) world.getBlockEntity(entityPos)).getComponentType().isLinkable()) {
                        ((INetworkComponent) world.getBlockEntity(entityPos)).setControllerPos(point.getControllerPos());
                        point.addComponent(entityPos);
                        point.updateEntity();
                        point.getControllerEntity().updateEntity();
                    }
                }
            }
        }
    }

    public static void removeComponentLinks(IWorld world, BlockPos pos) {
        for (BlockPos entityPos : getAdjacentTiles(world, pos)) {
            if(world.getBlockEntity(entityPos) instanceof INetworkComponent) {
                INetworkComponent component = (INetworkComponent) world.getBlockEntity(pos);

                removeComponentController(world, component.getControllerEntity().getPos(), component, entityPos);
                component.setControllerPos(null);
            }
        }
    }

    public static void removeComponentController(IWorld world, BlockPos controllerPos, INetworkComponent component, BlockPos componentPos) {
        if(world.getBlockEntity(controllerPos) instanceof BlockEntityController) {
            BlockEntityController controller = (BlockEntityController) world.getBlockEntity(controllerPos);
            controller.removeComponent(component, componentPos);
        }
    }

    public static List<BlockPos> getAdjacentTiles(IWorld world, BlockPos pos) {
        List<BlockPos> list = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockEntity entity = world.getBlockEntity(pos.offset(direction));
            if (entity instanceof INetworkComponent && ((INetworkComponent) entity).getComponentType().isLinkable()) {
                list.add(pos.offset(direction));
            }
        }

        return list;
    }


    public static void linkNeighborTerminals(@Nullable BlockEntityWirelessPoint point, IWorld world, BlockPos pos, Block lookingForBlock) {
        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = pos.offset(direction);
            if (point == null && world.getBlockEntity(offsetPos) instanceof BlockEntityWirelessPoint) {
                point = (BlockEntityWirelessPoint) world.getBlockEntity(offsetPos);
            } else if(point == null) {
                continue;
            }

            if (world.getBlockState(offsetPos).getBlock() == lookingForBlock) {
                if (lookingForBlock == ModBlocks.TERMINAL) {
                    if (point.terminalParts.get(direction) != null) {
                        continue;
                    }
                    point.addTerminalMultipart(direction);
                    world.getWorld().setBlockState(offsetPos, Blocks.AIR.getDefaultState());
                } else if (lookingForBlock == ModBlocks.WIRELESS_POINT) {
                    if (point.terminalParts.get(direction.getOpposite()) != null) {
                        continue;
                    }
                    point.addTerminalMultipart(direction.getOpposite());
                    world.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
                }
                world.getWorld().updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        }
    }
}
