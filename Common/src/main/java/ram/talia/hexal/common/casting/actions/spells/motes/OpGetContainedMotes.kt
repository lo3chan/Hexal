
package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.nbt.CompoundTag
import ram.talia.hexal.api.casting.castables.UserDataConstMediaAction
import ram.talia.hexal.api.casting.mishaps.MishapNoBoundStorage
import ram.talia.hexal.api.getBoundStorage
import ram.talia.hexal.api.getMoteOrItemType
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager

object OpGetContainedMotes : UserDataConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, userData: CompoundTag, env: CastingEnvironment): List<Iota> {
        val item = args.getMoteOrItemType(0, argc) ?: return listOf(NullIota())

        val storage = getBoundStorage(userData, env)
        if (!MediafiedItemManager.isStorageLoaded(storage))
            throw MishapNoBoundStorage("storage_unloaded")

        val results = item.map({itemIota ->
            itemIota.record?.let { MediafiedItemManager.getItemRecordsMatching(storage, it) }
        }, {
            MediafiedItemManager.getItemRecordsMatching(storage, it)
        }) ?: return listOf(NullIota())

        return ram.talia.hexal.api.asActionResult(results)
    }
}