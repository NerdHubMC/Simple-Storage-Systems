package nerdhub.simplestoragesystems.blocks.components;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.blocks.BlockWithEntityBase;
import nerdhub.simplestoragesystems.tiles.components.BlockEntityWirelessPoint;
import nerdhub.simplestoragesystems.utils.ComponentHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockWirelessPoint extends BlockWithEntityBase {

    public static final BooleanProperty[] PROPS = new BooleanProperty[] {
            BooleanProperty.create("down"),
            BooleanProperty.create("up"),
            BooleanProperty.create("north"),
            BooleanProperty.create("south"),
            BooleanProperty.create("west"),
            BooleanProperty.create("east"),
            BooleanProperty.create("none")
    };

    public BlockWirelessPoint() {
        super("wireless_point", Material.STONE, 1.0f, SimpleStorageSystems.modItemGroup);
        this.setDefaultState(this.getStateFactory().getDefaultState().with(PROPS[6], true));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) context.getWorld().getBlockEntity(context.getBlockPos());
        if(point != null && point.getControllerPos() != null) {
            point.connectedComponents.clear();
            for (BlockPos entityPos : getAdjacentTiles(context.getWorld(), context.getBlockPos())) {
                if(context.getWorld().getBlockEntity(entityPos) instanceof INetworkComponent && point.getControllerPos() != null) {
                    ComponentHelper.linkComponent(context.getWorld(), (INetworkComponent) context.getWorld().getBlockEntity(entityPos), point.getControllerPos());
                    point.addComponent(entityPos);
                }
            }
        }

        return getStateWithProps(this.getDefaultState(), context.getWorld(), context.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState state2, IWorld world, BlockPos blockPos, BlockPos blockPos_2) {
        BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(blockPos);
        point.connectedComponents.clear();
        if(point.getControllerPos() != null) {
            for (BlockPos entityPos : getAdjacentTiles(world, blockPos)) {
                if(world.getBlockEntity(entityPos) instanceof INetworkComponent && point.getControllerPos() != null) {
                    ComponentHelper.linkComponent(world, (INetworkComponent) world.getBlockEntity(entityPos), point.getControllerPos());
                    point.addComponent(entityPos);
                }
            }
        }

        return getStateWithProps(state, world, blockPos);
    }

    public BlockState getStateWithProps(BlockState state, IWorld world, BlockPos pos) {
        for (int i = 0; i < PROPS.length - 1; i++) {
            Direction facing = Direction.values()[i];
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(facing));
            state = state.with(PROPS[i], blockEntity instanceof INetworkComponent && ((INetworkComponent) blockEntity).getComponentType().isLinkable());
        }

        return state;
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

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity playerEntity) {
        super.onBreak(world, pos, state, playerEntity);
        for (BlockPos entityPos : getAdjacentTiles(world, pos)) {
            if(world.getBlockEntity(entityPos) instanceof INetworkComponent) {
                ComponentHelper.removeComponentLinks((INetworkComponent) world.getBlockEntity(entityPos));
            }
        }
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateBuilder) {
        super.appendProperties(stateBuilder.with(PROPS));
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isSimpleFullBlock(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return false;
    }

    @Override
    public boolean skipRenderingSide(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
        return blockState_1.getBlock() == this ? true : super.skipRenderingSide(blockState_1, blockState_2, direction_1);
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState var1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, VerticalEntityPosition verticalEntityPosition_1) {
        return Block.createCuboidShape(5, 5, 5, 11, 11, 11);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityWirelessPoint();
    }
}
