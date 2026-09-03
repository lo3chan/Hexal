package ram.talia.hexal.forge.eventhandlers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import ram.talia.hexal.client.LinkablePacketHolder;

public class RenderLinkEventHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        LinkablePacketHolder.maybeRetry();
    }
}
