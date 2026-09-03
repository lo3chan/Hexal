package ram.talia.hexal.forge.eventhandlers;

import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import ram.talia.hexal.api.gates.GateManager;
import ram.talia.hexal.api.gates.GateSavedData;

/**
 * Responsible for saving and loading the [GateManager] data.
 */
public class GateEventHandler {
    static final String FILE_GATE_MANAGER = "hexal_gate_manager";
    private static final SavedData.Factory<GateSavedData> FACTORY = new SavedData.Factory<>(GateSavedData::new, GateSavedData::new, null);

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        var savedData = event.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, FILE_GATE_MANAGER);
        savedData.setDirty();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        var savedData = event.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, FILE_GATE_MANAGER);
        GateManager.shouldClearOnWrite = true;
        savedData.setDirty();
    }
}
