package nerdhub.simplestoragesystems.items;

import abused_master.abusedlib.items.ItemBase;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.api.network.ILinkerComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLinker extends ItemBase {

    public ItemLinker() {
        super("linker", new Settings().stackSize(1).itemGroup(SimpleStorageSystems.modItemGroup));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getItemStack();

        if(!world.isClient && player.isSneaking()) {
            CompoundTag tag;

            if(stack.getTag() != null) {
                tag = stack.getTag();
            }else {
                tag = new CompoundTag();
            }

            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof ILinkerComponent) {
                ((ILinkerComponent) blockEntity).link(player, tag);
            }else {
                player.addChatMessage(new TextComponent("Cannot link a non component!"), true);
            }

            context.getItemStack().setTag(tag);
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if(player.isSneaking()) {
            clearTag(player.getStackInHand(hand));
            if(!world.isClient) {
                player.addChatMessage(new TextComponent("Cleared linker settings"), true);
            }
        }

        return super.use(world, player, hand);
    }

    public void clearTag(ItemStack stack) {
        stack.setTag(null);
    }

    @Override
    public void buildTooltip(ItemStack stack, @Nullable World world, List<Component> list, TooltipContext tooltipOptions) {
        CompoundTag tag = stack.getTag();
        if(tag != null) {
            if(tag.containsKey("wirelessPointPos")) {
                BlockPos pos = TagHelper.deserializeBlockPos(tag.getCompound("wirelessPointPos"));
                list.add(new TextComponent("Component Pos, x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()));
            }else {
                list.add(new TextComponent("No components saved to linker!"));
            }
        }else {
            list.add(new TextComponent("No components saved to linker!"));
        }
    }

    @Override
    public Rarity getRarity(ItemStack itemStack_1) {
        return Rarity.UNCOMMON;
    }
}
