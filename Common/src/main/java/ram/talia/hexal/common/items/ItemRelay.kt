package ram.talia.hexal.common.items

import net.minecraft.world.item.BlockItem
import ram.talia.hexal.common.lib.HexalBlocks
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.animation.Animation
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil

abstract class ItemRelay(properties: Properties) : BlockItem(HexalBlocks.RELAY, properties), GeoItem {
    private val cache = GeckoLibUtil.createInstanceCache(this)

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache? {
        return cache
    }

    override fun registerControllers(data: ControllerRegistrar) {
        data.add(AnimationController(this, "controller", 0) { event -> predicate(event) })
    }

    private fun <E : GeoAnimatable?> predicate(event: AnimationState<E>): PlayState {
        event.controller.setAnimation(
                RawAnimation.begin()
                        .then("animation.model.inv", Animation.LoopType.HOLD_ON_LAST_FRAME)
        )
        return PlayState.CONTINUE
    }
}