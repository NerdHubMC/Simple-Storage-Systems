package nerdhub.simplestoragesystems.blocks;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.registry.ModBlockEntities;
import nerdhub.simplestoragesystems.tiles.BlockEntityStorageBay;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockStorageBay extends BlockWithEntityBase {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public BlockStorageBay() {
        super("storage_bay", Material.STONE, 1.0f, SimpleStorageSystems.modItemGroup);
        this.setDefaultState(this.stateFactory.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(ModBlockEntities.STORAGE_BAY_CONTAINER, player, buf -> buf.writeBlockPos(blockPos));
        }

        return true;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState state2, boolean boolean_1) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(state.getBlock() != state2.getBlock() && blockEntity instanceof BlockEntityStorageBay) {
            ItemScatterer.spawn(world, pos, (BlockEntityStorageBay) blockEntity);
            world.updateHorizontalAdjacent(pos, this);
        }

        super.onBlockRemoved(state, world, pos, state2, boolean_1);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
        return this.getDefaultState().with(FACING, itemPlacementContext_1.getPlayerHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState blockState_1, BlockRotation rotation_1) {
        return blockState_1.with(FACING, rotation_1.rotate(blockState_1.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState_1, BlockMirror mirror_1) {
        return blockState_1.rotate(mirror_1.getRotation(blockState_1.get(FACING)));
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(new Property[]{FACING});
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityStorageBay();
    }
}
