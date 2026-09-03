
package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.nbt.CompoundTag
import ram.talia.hexal.api.casting.castables.UserDataConstMediaAction
import ram.talia.hexal.api.config.HexalConfig
import ram.talia.hexal.api.getBoundStorage
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import ram.talia.hexal.api.casting.mishaps.MishapNoBoundStorage

object OpGetStorageRemainingCapacity : UserDataConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, userData: CompoundTag, env: CastingEnvironment): List<Iota> {
        val storageId = getBoundStorage(userData, env)
        if (!MediafiedItemManager.isStorageLoaded(storageId))
            throw MishapNoBoundStorage("storage_unloaded")
        val storage = MediafiedItemManager.getStorage(storageId)?.get() ?: throw MishapNoBoundStorage()

        return (HexalConfig.server.maxRecordsInMediafiedStorage - storage.storedItems.size).asActionResult
    }
}