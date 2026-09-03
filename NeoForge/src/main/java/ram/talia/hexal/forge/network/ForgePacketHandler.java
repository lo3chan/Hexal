package ram.talia.hexal.forge.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import ram.talia.hexal.api.HexalAPI;

public class ForgePacketHandler {
	public static void init() {
		// Handled via RegisterPayloadHandlersEvent
	}

	public static void register(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(HexalAPI.MOD_ID);
		registrar.playToClient(
			HexalPacketPayload.TYPE,
			HexalPacketPayload.STREAM_CODEC,
			HexalPacketPayload::handleClient
		);
		registrar.playToServer(
			HexalPacketPayload.TYPE,
			HexalPacketPayload.STREAM_CODEC,
			HexalPacketPayload::handleServer
		);
	}
}