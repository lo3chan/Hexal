package ram.talia.hexal.api.casting.eval.env

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import ram.talia.hexal.common.entities.BaseCastingWisp

class WispMishapEnv(wisp: BaseCastingWisp, level: ServerLevel) : MishapEnvironment(level, wisp.caster as? ServerPlayer) {
    override fun yeetHeldItemsTowards(pos: Vec3) {}
    override fun dropHeldItems() {}
    override fun drown() {}
    override fun damage(healthProportion: Float) {}
    override fun removeXp(amount: Int) {}
    override fun blind(ticks: Int) {}
    override fun nauseate(ticks: Int) {}
}
