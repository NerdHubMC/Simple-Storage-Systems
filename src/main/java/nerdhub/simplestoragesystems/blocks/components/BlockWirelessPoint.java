package nerdhub.simplestoragesystems.blocks.components;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.INetworkComponent;
import nerdhub.simplestoragesystems.blocks.BlockBase;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class BlockWirelessPoint extends BlockBase {

    public static final BooleanProperty[] PROPS = new BooleanProperty[]{
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
        return getStateWithProps(this.getDefaultState(), context.getWorld(), context.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState state2, IWorld world, BlockPos blockPos, BlockPos blockPos_2) {
        return getStateWithProps(state, world, blockPos);
    }

    public BlockState getStateWithProps(BlockState state, IWorld world, BlockPos pos) {
        for (int i = 0; i < PROPS.length - 1; i++) {
            Direction facing = Direction.values()[i];
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(facing));
            state = state.with(PROPS[i], blockEntity instanceof INetworkComponent);
        }

        return state;
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
}
