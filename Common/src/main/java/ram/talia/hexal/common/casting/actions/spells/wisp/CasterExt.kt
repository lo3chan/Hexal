package ram.talia.hexal.common.casting.actions.spells.wisp

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.server.level.ServerPlayer

val CastingEnvironment.caster: ServerPlayer?
	get() = this.castingEntity as? ServerPlayer
