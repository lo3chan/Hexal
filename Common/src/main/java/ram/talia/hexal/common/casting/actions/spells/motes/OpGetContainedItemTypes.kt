
package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.utils.TreeList
import net.minecraft.nbt.CompoundTag
import ram.talia.hexal.api.caster
import ram.talia.hexal.api.casting.castables.UserDataConstMediaAction
import ram.talia.hexal.api.casting.iota.MoteIota
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import ram.talia.hexal.api.casting.mishaps.MishapNoBoundStorage

object OpGetContainedItemTypes : UserDataConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, userData: CompoundTag, env: CastingEnvironment): List<Iota> {
        val storage: java.util.UUID = (if (userData.contains(MoteIota.TAG_TEMP_STORAGE)) {
            userData.getUUID(MoteIota.TAG_TEMP_STORAGE)
        } else {
            env.caster?.let { MediafiedItemManager.getBoundStorage(it) }
        }) ?: throw MishapNoBoundStorage()
        if (!MediafiedItemManager.isStorageLoaded(storage))
            throw MishapNoBoundStorage("storage_unloaded")

        return listOf(ListIota(TreeList.from(MediafiedItemManager.getAllContainedItemTypes(storage)?.map { NullIota() } ?: listOf())))
    }
}