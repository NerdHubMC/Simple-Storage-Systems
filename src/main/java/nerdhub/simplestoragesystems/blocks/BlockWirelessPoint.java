package nerdhub.simplestoragesystems.blocks;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.network.INetworkComponent;
import nerdhub.simplestoragesystems.registry.ModBlocks;
import nerdhub.simplestoragesystems.tiles.BlockEntityWirelessPoint;
import nerdhub.simplestoragesystems.utils.ComponentHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);
        BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(pos);

        if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem && Block.getBlockFromItem(stack.getItem()) instanceof BlockTerminal)) {
            if(!point.terminalParts.isEmpty() && point.terminalParts.get(hitResult.getSide()) != null) {
                point.terminalParts.get(hitResult.getSide()).onMultipartActivated(pos, hitResult.getSide(), player, hand, stack);
            }
        }else {
            if(point.terminalParts.get(hitResult.getSide()) == null) {
                point.addTerminalMultipart(hitResult.getSide());
                stack.subtractAmount(1);
            }
        }

        return point.terminalParts.get(hitResult.getSide()) != null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity living, ItemStack stack) {
        BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(pos);
        ComponentHelper.linkNeighborTerminals(point, world, pos, ModBlocks.TERMINAL);
        ComponentHelper.linkComponents(world, pos);

        super.onPlaced(world, pos, state, living, stack);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos pos2, boolean boolean_1) {
        ComponentHelper.linkComponents(world, pos);

        super.neighborUpdate(state, world, pos, block, pos2, boolean_1);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity playerEntity) {
        BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(pos);
        ComponentHelper.removeComponentLinks(world, pos);

        if(point.isLinked() && point.getControllerEntity().wirelessPointPositions.contains(pos)) {
            point.getControllerEntity().wirelessPointPositions.remove(pos);
        }

        if(point.terminalParts.size() > 0) {
            if(!world.isClient) {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.TERMINAL, point.terminalParts.size())));
            }
        }

        super.onBreak(world, pos, state, playerEntity);
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
        BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) world.getBlockEntity(pos);

        for (int i = 0; i < PROPS.length - 1; i++) {
            Direction facing = Direction.values()[i];
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(facing));
            if(point != null) {
                if(point.terminalParts.get(facing) == null) {
                    state = state.with(PROPS[i], blockEntity instanceof INetworkComponent && ((INetworkComponent) blockEntity).getComponentType().isLinkable());
                }
            }else {
                state = state.with(PROPS[i], blockEntity instanceof INetworkComponent && ((INetworkComponent) blockEntity).getComponentType().isLinkable());
            }
        }

        return state;
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateBuilder) {
        super.appendProperties(stateBuilder.add(PROPS));
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
    public boolean isSideInvisible(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
        return blockState_1.getBlock() == this ? true : super.isSideInvisible(blockState_1, blockState_2, direction_1);
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
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext verticalEntityPosition) {
        if(view.getBlockEntity(pos) instanceof BlockEntityWirelessPoint) {
            BlockEntityWirelessPoint point = (BlockEntityWirelessPoint) view.getBlockEntity(pos);

            if (!point.terminalParts.isEmpty()) {
                VoxelShape pointShape = Block.createCuboidShape(5, 5, 5, 11, 11, 11);

                for (Direction direction : point.terminalParts.keySet()) {
                    pointShape = VoxelShapes.union(pointShape, BlockTerminal.getShapeFromDirection(direction));
                }

                return pointShape;
            } else {
                return Block.createCuboidShape(5, 5, 5, 11, 11, 11);
            }
        }

        return Block.createCuboidShape(5, 5, 5, 11, 11, 11);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityWirelessPoint();
    }
}
