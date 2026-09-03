package ram.talia.hexal.api.gates;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class GateSavedData extends SavedData {
    public GateSavedData() {  }

    public GateSavedData(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        GateManager.readFromNbt(tag);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        GateManager.writeToNbt(tag);

        return tag;
    }


}
