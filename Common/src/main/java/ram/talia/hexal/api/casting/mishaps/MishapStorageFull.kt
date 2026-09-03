package ram.talia.hexal.api.casting.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.TreeList
import net.minecraft.network.chat.Component
import net.minecraft.world.item.DyeColor
import ram.talia.hexal.api.casting.iota.MoteIota
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import java.util.UUID

class MishapStorageFull(val storage: UUID) : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.RED)

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component = error("full_storage")

    override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: TreeList<Iota>): TreeList<Iota> {
        val allRecords = MediafiedItemManager.getAllRecords(storage) ?: return stack
        val index = allRecords.keys.randomOrNull() ?: return stack
        val iota = MoteIota(index)
        val toDrop = iota.getStacksToDrop((iota.item?.defaultMaxStackSize ?: 64).toLong())
        // drop item if level available
        return stack
    }
}
