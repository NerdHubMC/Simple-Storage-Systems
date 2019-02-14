package nerdhub.simplestoragesystems.api.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public interface ILinkerComponent {

    void link(PlayerEntity player, CompoundTag tag);
}
