package ram.talia.hexal.forge;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import ram.talia.hexal.api.everbook.Everbook;
import ram.talia.hexal.client.RegisterClientStuff;
import ram.talia.hexal.common.lib.HexalBlockEntities;
import ram.talia.hexal.common.lib.HexalItems;
import ram.talia.hexal.forge.client.blocks.BlockEntityRelayRenderer;
import ram.talia.hexal.forge.client.items.ItemRelayRenderer;
import ram.talia.hexal.forge.client.items.IRenderPropertiesSetter;
import thedarkcolour.kotlinforforge.KotlinModLoadingContext;

public class ForgeHexalClientInitializer {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent event) {
		event.enqueueWork(RegisterClientStuff::init);

		if (FMLEnvironment.dist == Dist.CLIENT)
			cursedItemPropertiesNonsense();
		NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post tickEvent)-> {
			Everbook.checkSaveTime();
		});
	}

	@OnlyIn(Dist.CLIENT)
	private static void cursedItemPropertiesNonsense() {
		// this is *so* dumb
		//noinspection DataFlowIssue
		((IRenderPropertiesSetter) HexalItems.RELAY).setRenderProperties(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new ItemRelayRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		RegisterClientStuff.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
		evt.registerBlockEntityRenderer(HexalBlockEntities.RELAY, context -> new BlockEntityRelayRenderer());
	}
}
