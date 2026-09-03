package ram.talia.hexal.api.casting.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.TreeList
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.DyeColor
import ram.talia.hexal.api.casting.iota.GateIota

class MishapIllegalInterworldIota(val iota: Iota) : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.GRAY)

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component = error("illegal_interworld_iota", iota.display())

    override fun execute(ctx: CastingEnvironment, errorCtx: Context, stack: TreeList<Iota>): TreeList<Iota> {
        val player = (ctx.castingEntity as? ServerPlayer)
        if (player != null) {
            player.hurt(player.damageSources().magic(), player.health - 0.5f)
        }
        return stack
    }

    companion object {
        fun getFromNestedIota(iota: Iota): Iota? {
            return when (iota) {
                is GateIota -> iota
                is EntityIota -> iota
                is ListIota -> {
                    for (sub in iota.list) {
                        val mishap = getFromNestedIota(sub)
                        if (mishap != null) return mishap
                    }
                    null
                }
                else -> null
            }
        }

        fun replaceInNestedIota(iota: Iota): Iota {
            return when (iota) {
                is GateIota -> GarbageIota()
                is EntityIota -> GarbageIota()
                is ListIota -> ListIota(TreeList.from(iota.list.map { replaceInNestedIota(it) }))
                else -> iota
            }
        }
    }
}
