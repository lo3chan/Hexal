package ram.talia.hexal.api

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.server.level.ServerPlayer

val CastingEnvironment.caster: ServerPlayer?
	get() = this.castingEntity as? ServerPlayer
