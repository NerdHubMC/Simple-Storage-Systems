package nerdhub.simplestoragesystems.mixins;

import nerdhub.simplestoragesystems.SimpleStorageSystems;
import nerdhub.simplestoragesystems.utils.RecipeGenerator;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager {

    @Inject(method = "onResourceReload", at = @At("RETURN"))
    public void onResourceReload(ResourceManager resourceManager_1, CallbackInfo ci) {
        for (RecipeGenerator.Output output : RecipeGenerator.recipes) {
            this.add(RecipeManager.deserialize(new Identifier(SimpleStorageSystems.MODID, output.name), output.recipe));
        }
    }

    @Shadow
    public abstract void add(Recipe<?> recipe_1);
}
