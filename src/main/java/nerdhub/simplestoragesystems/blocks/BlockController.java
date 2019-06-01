package nerdhub.simplestoragesystems.blocks;

import abused_master.abusedlib.blocks.BlockWithEntityBase;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.client.gui.gui.GuiController;
import nerdhub.simplestoragesystems.tiles.BlockEntityController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockController extends BlockWithEntityBase {

    public static final EnumProperty<ControllerState> STATE = EnumProperty.create("state", ControllerState.class);

    public BlockController() {
        super("controller", Material.STONE, 1.0f, SimpleStorageSystems.modItemGroup);
        this.setDefaultState(this.getStateFactory().getDefaultState().with(STATE, ControllerState.offline));
    }

    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if(world.isClient) {
            MinecraftClient.getInstance().openScreen(new GuiController((BlockEntityController) world.getBlockEntity(blockPos)));
        }

        return true;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context).with(STATE, ControllerState.offline);
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateBuilder) {
        super.appendProperties(stateBuilder.add(STATE));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2) {
        return blockState_1.with(STATE, ((BlockEntityController) iWorld_1.getBlockEntity(blockPos_1)).storage.getEnergyStored() > 0 ? ControllerState.online : ControllerState.offline);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockEntityController();
    }

    public enum ControllerState implements StringIdentifiable {
        online,
        offline;

        @Override
        public String asString() {
            return this.name();
        }
    }
}
