package ram.talia.hexal.api

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import ram.talia.hexal.api.casting.iota.MoteIota
import ram.talia.hexal.api.casting.mishaps.MishapNoBoundStorage
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import java.util.UUID

val CastingEnvironment.caster: ServerPlayer?
	get() = this.castingEntity as? ServerPlayer

fun getBoundStorage(userData: CompoundTag, env: CastingEnvironment): UUID {
	if (userData.contains(MoteIota.TAG_TEMP_STORAGE)) {
		return userData.getUUID(MoteIota.TAG_TEMP_STORAGE)
	}
	val player = env.caster ?: throw MishapNoBoundStorage()
	return MediafiedItemManager.getBoundStorage(player) ?: throw MishapNoBoundStorage()
}
