package at.petrak.hexcasting.api.casting.eval

import net.minecraft.server.level.ServerPlayer

val CastingEnvironment.caster: ServerPlayer?
	get() = this.castingEntity as? ServerPlayer
