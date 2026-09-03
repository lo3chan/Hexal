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
		registrar.playBidirectional(
			HexalPacketPayload.TYPE,
			HexalPacketPayload.STREAM_CODEC,
			(payload, context) -> {
				if (context.flow().isClientbound()) {
					HexalPacketPayload.handleClient(payload, context);
				} else {
					HexalPacketPayload.handleServer(payload, context);
				}
			}
		);
	}
}