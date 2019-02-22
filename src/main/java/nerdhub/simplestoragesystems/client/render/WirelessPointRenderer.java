package nerdhub.simplestoragesystems.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.blocks.BlockTerminal;
import nerdhub.simplestoragesystems.registry.ModBlocks;
import nerdhub.simplestoragesystems.tiles.BlockEntityWirelessPoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class WirelessPointRenderer extends BlockEntityRenderer<BlockEntityWirelessPoint> {

    @Override
    public void render(BlockEntityWirelessPoint tile, double x, double y, double z, float deltaTime, int int_1) {
        super.render(tile, x, y, z, deltaTime, int_1);
        MinecraftClient mc = MinecraftClient.getInstance();
        if(!tile.terminalParts.isEmpty()) {
            for (Direction direction : tile.terminalParts.keySet()) {
                GlStateManager.pushMatrix();
                GlStateManager.translated(x, y, z);
                GlStateManager.pushLightingAttributes();
                mc.getTextureManager().bindTexture(new Identifier(SimpleStorageSystems.MODID, "textures/blocks/terminal_front.png"));
                BakedModel model = mc.getBlockRenderManager().getModel(ModBlocks.TERMINAL.getDefaultState().with(BlockTerminal.FACING, direction));
                mc.getBlockRenderManager().getModelRenderer().render(model, 1, 1, 1, 1);
                GlStateManager.popAttributes();
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean method_3563(BlockEntityWirelessPoint blockEntity_1) {
        return true;
    }
}
