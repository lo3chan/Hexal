package ram.talia.hexal.api.casting.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.TreeList
import net.minecraft.network.chat.Component
import net.minecraft.world.item.DyeColor
import ram.talia.hexal.api.linkable.ILinkable

class MishapLinkToSelf(val linkable: ILinkable) : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.MAGENTA)

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component = error("link_to_self")

    override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: TreeList<Iota>): TreeList<Iota> = stack
}
